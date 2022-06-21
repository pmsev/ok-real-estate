package base

import ICorExec
import ICorWorkerDsl

abstract class BaseCorWorkerDsl<T>(
    override var title: String = "",
    override var description: String = "",
    protected var blockOn: suspend T.() -> Boolean = { true },
    protected var blockHandle: suspend T.() -> Unit = {},
    protected var blockException: suspend T.(e: Throwable) -> Unit = { e: Throwable -> throw e },
) : ICorWorkerDsl<T> {

    abstract override fun build(): ICorExec<T>

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