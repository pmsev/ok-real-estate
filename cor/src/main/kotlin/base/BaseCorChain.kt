package base

import ICorWorker

abstract class BaseCorChain<T>(
    override val title: String,
    override val description: String = "",
    private val blockOn: suspend T.() -> Boolean = { true },
    private val blockException: suspend T.(Throwable) -> Unit = {},
) : ICorWorker<T> {

    override suspend fun on(context: T): Boolean = blockOn(context)
    override suspend fun exception(context: T, e: Throwable) = blockException(context, e)

    abstract override suspend fun handle(context: T)
}