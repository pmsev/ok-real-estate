package repo

import models.ReError

interface ReDbResponse<T> {
    val result: T?
    val isSuccess: Boolean
    val errors: List<ReError>
}