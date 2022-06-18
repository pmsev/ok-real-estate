package stubs

import ICorChainDsl
import ReContext
import models.ReError
import models.ReErrorLevel
import models.ReState
import worker

fun ICorChainDsl<ReContext>.stubValidationBadTitle(title: String) = worker {
     this.title = title
     on { stubCase == ReStubs.BAD_TITLE && state == ReState.RUNNING }
     handle {
         state = ReState.FAILING
         this.errors.add(
             ReError(
                 group = "validation",
                 code = "validation-title",
                 field = "title",
                 message = "Wrong title field",
                 level = ReErrorLevel.ERROR
             )
         )
     }
 }