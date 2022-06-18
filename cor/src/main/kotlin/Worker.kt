import base.BaseCorWorkerDsl

@CorDslMarker
fun <T> ICorChainDsl<T>.worker(
    function: CorWorkerDsl<T>.() -> Unit
) {
    add(
        CorWorkerDsl<T>().apply(function)
    )
}

@CorDslMarker
fun <T> ICorChainDsl<T>.worker(
    title: String,
    description: String = "",
    function: suspend T.() -> Unit
) {
    add(
        CorWorkerDsl<T>().apply {
            this.title = title
            this.description = description
            this.handle(function)
        }
    )
}

class CorWorker<T>(
    override val title: String,
    override val description: String = "",
    private val blockOn: suspend T.() -> Boolean = { true },
    private val blockHandle: suspend T.() -> Unit = {},
    private val blockException: suspend T.(Throwable) -> Unit = {},
) : ICorWorker<T> {
    override suspend fun on(context: T): Boolean = blockOn(context)
    override suspend fun handle(context: T) = blockHandle(context)
    override suspend fun exception(context: T, e: Throwable) = blockException(context, e)
}

/**
 * DLS context of a single execution. Cannot be expanded by other chains.
 */
@CorDslMarker
class CorWorkerDsl<T>() : BaseCorWorkerDsl<T>() {

    override fun build(): ICorExec<T> = CorWorker<T>(
        title = title,
        description = description,
        blockOn = blockOn,
        blockHandle = blockHandle,
        blockException = blockException
    )

    override fun on(function: suspend T.() -> Boolean) {
        blockOn = function
    }

    override fun handle(function: suspend T.() -> Unit) {
        blockHandle = function
    }

    override fun exception(function: suspend T.(e: Throwable) -> Unit) {
        blockException = function
    }

}