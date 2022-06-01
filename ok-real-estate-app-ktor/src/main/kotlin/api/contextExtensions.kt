package api

import models.ReError
import ru.otus.otuskotlin.realestate.api.v1.models.ResponseResult

fun buildError() = ReError(
    field = "_", code = ResponseResult.ERROR.value
)
