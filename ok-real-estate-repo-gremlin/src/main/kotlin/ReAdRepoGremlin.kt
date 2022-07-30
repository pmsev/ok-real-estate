import ReAdGremlinConst.FIELD_DESCRIPTION
import ReAdGremlinConst.FIELD_LOCK
import ReAdGremlinConst.FIELD_SELLER_ID
import ReAdGremlinConst.FIELD_TITLE
import ReAdGremlinConst.RESULT_LOCK_FAILURE
import ReAdGremlinConst.RESULT_SUCCESS
import com.benasher44.uuid.uuid4
import heplers.asReError
import heplers.errorConcurrency
import heplers.errorDevOps
import mappers.addReAd
import mappers.label
import mappers.toReAd
import models.*
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.exception.ResponseException
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal
import org.apache.tinkerpop.gremlin.process.traversal.TextP
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__`.constant
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__`.select
import org.apache.tinkerpop.gremlin.structure.Vertex
import repo.*

class ReAdRepoGremlin(
    private val hosts: String,
    private val port: Int = 8182,
    private val enableSsl: Boolean = false,
    initObjects: List<ReAd> = emptyList(),
    initRepo: ((GraphTraversalSource) -> Unit)? = null,
    val randomUuid: () -> String = { uuid4().toString() },

    ) : ReAdRepository {

    val initializedObjects: List<ReAd>

    private val cluster by lazy {
        Cluster.build().apply {
            addContactPoints(*hosts.split(Regex("\\s*,\\s*")).toTypedArray())
            port(port)
            enableSsl(enableSsl)
        }.create()
    }
    private val g by lazy { AnonymousTraversalSource.traversal().withRemote(DriverRemoteConnection.using(cluster)) }

    init {
        if (initRepo != null) {
            initRepo(g)
        }
        initializedObjects = initObjects.map {
            val id = save(it)
            it.copy(id = ReAdId(id))
        }
    }

    private fun save(ad: ReAd): String = g.addV(ad.label()).addReAd(ad)?.next()?.id() as? String ?: ""


    override suspend fun createReAd(rq: DbReAdRequest): DbReAdResponse {
        val key = randomUuid()
        val ad = rq.reAd.copy(id = ReAdId(key), lock = ReAdLock(randomUuid()))
        val id = g.addV(ad.label()).addReAd(ad)
            ?.next()
            ?.id()
            .let {
                when (it) {
                    is String -> it
                    else -> return DbReAdResponse(
                        result = null, isSuccess = false,
                        errors = listOf(
                            errorDevOps(
                                violationCode = "badDbResponse",
                                description = "Unexpected result got. Please, contact administrator",
                                exception = WrongIdTypeException(
                                    "createReAd for ${this@ReAdRepoGremlin::class} " +
                                            "returned id = '$it' that is not admitted by the application"
                                )
                            )
                        )
                    )
                }
            }

        return DbReAdResponse(
            result = ad.copy(id = ReAdId(id)),
            isSuccess = true,
        )
    }

    override suspend fun readReAd(rq: DbReAdIdRequest): DbReAdResponse {
        val key = rq.id.takeIf { it != ReAdId.NONE }?.asString() ?: return resultErrorEmptyId
        val dbRes = try {
            g.V(key).elementMap<Any>().toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound
            }
            return DbReAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(e.asReError())
            )
        }
        when (dbRes.size) {
            0 -> return resultErrorNotFound
            1 -> return DbReAdResponse(
                result = dbRes.first().toReAd(),
                isSuccess = true,
            )
            else -> return DbReAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    errorDevOps(
                        violationCode = "duplicateObjects",
                        description = "Database consistency failure",
                        exception = DbDuplicatedElementsException("Db contains multiple elements for id = '$key'")
                    )
                )
            )
        }
    }

    override suspend fun updateReAd(rq: DbReAdRequest): DbReAdResponse {
        val key = rq.reAd.id.takeIf { it != ReAdId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.reAd.lock.takeIf { it != ReAdLock.NONE }
        val newLock = ReAdLock(randomUuid())
        val newAd = rq.reAd.copy(lock = newLock)
        val dbRes = try {
            g
                .V(key)
                .`as`("a")
                .choose(
                    select<Vertex, Any>("a")
                        .values<String>(FIELD_LOCK)
                        .`is`(oldLock?.asString()),
                    select<Vertex, Vertex>("a").addReAd(newAd),
                    select<Vertex, Vertex>("a")
                ).elementMap<Any>().toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound
            }
            return DbReAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(e.asReError())
            )
        }
        val adResult = dbRes.firstOrNull()?.toReAd()
        when {
            dbRes.size == 0 -> return resultErrorNotFound
            dbRes.size == 1 && adResult?.lock == oldLock -> return resultErrorConcurrent
            dbRes.size == 1 && adResult?.lock == newLock -> return DbReAdResponse(
                result = dbRes.first().toReAd(),
                isSuccess = true,
            )
            else -> return DbReAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    errorDevOps(
                        violationCode = "duplicateObjects",
                        description = "Database consistency failure",
                        exception = DbDuplicatedElementsException("Db contains multiple elements for id = '$key'")
                    )
                )
            )
        }
    }

    override suspend fun deleteReAd(rq: DbReAdIdRequest): DbReAdResponse {
        val key = rq.id.takeIf { it != ReAdId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.lock.takeIf { it != ReAdLock.NONE }?.asString() ?: return resultErrorEmptyLock
        val readResult = readReAd(rq)
        // В случае ошибок чтения, нет смысла продолжать. Выходим
        if (!readResult.isSuccess) return readResult
        val result = g
            .V(key)
            .`as`("a")
            .choose(
                select<Vertex, Any>("a")
                    .values<String>(FIELD_LOCK)
                    .`is`(oldLock),
                select<Vertex, String>("a").drop().inject(RESULT_SUCCESS),
                constant(RESULT_LOCK_FAILURE)
            ).toList().firstOrNull()

        return when (result) {
            RESULT_SUCCESS -> readResult
            RESULT_LOCK_FAILURE -> resultErrorConcurrent
            null -> resultErrorNotFound
            else -> throw WrongResponseFromDb("Unsupported response '$result' from DB Gremliln for ${this::deleteReAd::class}")
        }
    }

    override suspend fun searchReAd(rq: DbReAdFilterRequest): DbReAdsResponse {
        val result = try {
            g.V()
                .apply {
                    rq.sellerIdFilter.takeIf { it != ReUserId.NONE }?.also { has(FIELD_SELLER_ID, it.asString()) }
                }
                .apply { rq.titleFilter.takeIf { it.isNotBlank() }?.also { has(FIELD_TITLE, TextP.containing(it)) } }
                .apply {
                    rq.descriptionFilter.takeIf { it.isNotBlank() }
                        ?.also { has(FIELD_DESCRIPTION, TextP.containing(it)) }
                }
                .elementMap<Any>()
                .toList()
        } catch (e: Throwable) {
            return DbReAdsResponse(
                isSuccess = false,
                result = null,
                errors = listOf(e.asReError())
            )
        }
        return DbReAdsResponse(
            result = result.map { it.toReAd() },
            isSuccess = true
        )
    }

    companion object {
        val resultErrorEmptyId = DbReAdResponse(
            result = null,
            isSuccess = false,
            errors = listOf(
                ReError(
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )
        val resultErrorEmptyLock = DbReAdResponse(
            result = null,
            isSuccess = false,
            errors = listOf(
                ReError(
                    field = "lock",
                    message = "Lock must be provided"
                )
            )
        )
        val resultErrorConcurrent = DbReAdResponse(
            result = null,
            isSuccess = false,
            errors = listOf(
                errorConcurrency(
                    violationCode = "changed",
                    description = "Object has changed during request handling"
                ),
            )
        )
        val resultErrorNotFound = DbReAdResponse(
            isSuccess = false,
            result = null,
            errors = listOf(
                ReError(
                    field = "id",
                    message = "Not Found"
                )
            )
        )
    }


}