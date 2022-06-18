package api

import models.ReError
import models.ReErrorLevel
import ru.otus.otuskotlin.realestate.api.v1.models.ResponseResult

fun buildError() = ReError(
    code = ResponseResult.ERROR.value, field = "_", level = ReErrorLevel.ERROR
)
