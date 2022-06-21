package base

import ICorChainDsl
import ICorExec
import ICorExecDsl

abstract class BaseCorChainDsl<T>(
    override var title: String = "",
    override var description: String = "",
    protected val workers: MutableList<ICorExecDsl<T>> = mutableListOf(),
    protected var blockOn: suspend T.() -> Boolean = { true },
    protected var blockException: suspend T.(e: Throwable) -> Unit = { e: Throwable -> throw e },
) : ICorChainDsl<T> {

    abstract override fun build(): ICorExec<T>

    override fun add(worker: ICorExecDsl<T>) {
        workers.add(worker)
    }

    override fun on(function: suspend T.() -> Boolean) {
        blockOn = function
    }

    override fun exception(function: suspend T.(e: Throwable) -> Unit) {
        blockException = function
    }
}
