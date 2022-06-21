package common

import ICorChainDsl
import ReContext
import chain
import models.ReCommand
import models.ReState

fun ICorChainDsl<ReContext>.operation(title: String, command: ReCommand, block: ICorChainDsl<ReContext>.() -> Unit) =
    chain {
        block()
        this.title = title
        on { this.command == command && state == ReState.RUNNING }
    }
