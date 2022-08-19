package api

import ReContext
import ReLogWrapper
import fromTransport
import heplers.asReError
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import mappers.toModel
import models.ReCommand
import models.ReState
import ru.otus.otuskotlin.realestate.api.v1.models.Request
import ru.otus.otuskotlin.realestate.api.v1.models.Response
import toLog
import toTransportAd


suspend inline fun <reified Q : Request, reified R : Response>
        ApplicationCall.controllerHelper(    logger: ReLogWrapper,
                                             logId: String,
                                             command: ReCommand? = null,
                                             crossinline block: suspend ReContext.() -> Unit) {
    val ctx = ReContext(
        timeStart = Clock.System.now(),
        principal = principal<JWTPrincipal>().toModel(),
    )
    try {
        logger.doWithLogging {
            val request = receive<Q>()
            ctx.fromTransport(request)
            logger.info(
                msg = "$command request is got",
                data = ctx.toLog("${logId}-got")
            )
            ctx.block()
            val response = ctx.toTransportAd()
            respond(response)
        }
    } catch (e: Throwable) {
        command?.also { ctx.command = it }
        ctx.state = ReState.FAILING
        ctx.errors.add(e.asReError())
        logger.error(
            msg = "Fail to handle $command request",
            e = e,
            data = ctx.toLog("${logId}-error")
        )
        ctx.block()
        val response = ctx.toTransportAd()
        respond(response)
    }
}
