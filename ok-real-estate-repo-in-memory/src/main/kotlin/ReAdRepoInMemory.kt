import com.benasher44.uuid.uuid4
import heplers.errorConcurrency
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import model.ReAdEntity
import models.*
import repo.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class ReAdRepoInMemory (
    initObjects: List<ReAd> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() }
) : ReAdRepository {
    /**
     * Инициализация кеша с установкой "времени жизни" данных после записи
     */
    private val cache = Cache.Builder()
        .expireAfterWrite(ttl)
        .build<String, ReAdEntity>()
    private val mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(ad: ReAd) {
        val entity = ReAdEntity(ad)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id!!, entity)
    }

    override suspend fun createReAd(rq: DbReAdRequest): DbReAdResponse {
        val key = randomUuid()
        val ad = rq.reAd.copy(id = ReAdId(key), lock = ReAdLock(randomUuid()))
        val entity = ReAdEntity(ad)
        mutex.withLock {
            cache.put(key, entity)
        }
        return DbReAdResponse(
            result = ad,
            isSuccess = true,
        )
    }

    override suspend fun readReAd(rq: DbReAdIdRequest): DbReAdResponse {
        val key = rq.id.takeIf { it != ReAdId.NONE }?.asString() ?: return resultErrorEmptyId
        return cache.get(key)
            ?.let {
                DbReAdResponse(
                    result = it.toInternal(),
                    isSuccess = true,
                )
            } ?: resultErrorNotFound
    }

    override suspend fun updateReAd(rq: DbReAdRequest): DbReAdResponse {
        val key = rq.reAd.id.takeIf { it != ReAdId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.reAd.lock.takeIf { it != ReAdLock.NONE }?.asString()
        val newAd = rq.reAd.copy(lock = ReAdLock(randomUuid()))
        val entity = ReAdEntity(newAd)
        mutex.withLock {
            val local = cache.get(key)
            when {
                local == null -> return resultErrorNotFound
                local.lock == null || local.lock == oldLock -> cache.put(key, entity)
                else -> return resultErrorConcurrent
            }
        }
        return DbReAdResponse(
            result = newAd,
            isSuccess = true,
        )
    }

    override suspend fun deleteReAd(rq: DbReAdIdRequest): DbReAdResponse {
        val key = rq.id.takeIf { it != ReAdId.NONE }?.asString() ?: return resultErrorEmptyId
        mutex.withLock {
            val local = cache.get(key) ?: return resultErrorNotFound
            if (local.lock == rq.lock.asString()) {
                cache.invalidate(key)
                return DbReAdResponse(
                    result = local.toInternal(),
                    isSuccess = true,
                    errors = emptyList()
                )
            } else {
                return resultErrorConcurrent
            }
        }
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchReAd(rq: DbReAdFilterRequest): DbReAdsResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                rq.sellerIdFilter.takeIf { it != ReUserId.NONE }?.let {
                    it.asString() == entry.value.sellerId
                } ?: true
            }
            .filter { entry ->
                rq.titleFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) ?: false
                } ?: true
            }
            .filter { entry ->
                rq.descriptionFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.description?.contains(it) ?: false
                } ?: true
            }

            .map { it.value.toInternal() }
            .toList()
        return DbReAdsResponse(
            result = result,
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
