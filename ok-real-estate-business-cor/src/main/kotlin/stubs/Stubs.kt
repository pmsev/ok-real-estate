package stubs

import ICorChainDsl
import ReContext
import chain
import models.ReState
import models.ReWorkMode

fun ICorChainDsl<ReContext>.stubs(title: String, block: ICorChainDsl<ReContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == ReWorkMode.STUB && state == ReState.RUNNING }
}
