import base.BaseCorChain
import base.BaseCorChainDsl

fun <T> rootChain(function: CorChainDsl<T>.() -> Unit) = CorChainDsl<T>().apply(function)

@CorDslMarker
fun <T> ICorChainDsl<T>.chain(function: CorChainDsl<T>.() -> Unit) {
    add(CorChainDsl<T>().apply(function))
}

class CorChain<T>(
    private val execs: List<ICorExec<T>>,
    title: String,
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    blockException: suspend T.(Throwable) -> Unit = {},
) : BaseCorChain<T>(
    title = title,
    description = description,
    blockOn = blockOn,
    blockException = blockException
) {
    override suspend fun handle(context: T) {
        execs.forEach { it.exec(context) }
    }
}

@CorDslMarker
class CorChainDsl<T>() : BaseCorChainDsl<T>() {
    override fun build(): ICorExec<T> = CorChain(
        title = title,
        description = description,
        execs = workers.map { it.build() }.toList(),
        blockOn = blockOn,
        blockException = blockException
    )
}
