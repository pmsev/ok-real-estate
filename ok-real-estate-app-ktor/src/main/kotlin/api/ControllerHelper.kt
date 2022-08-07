package api

import ReContext
import fromTransport
import heplers.asReError
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import mappers.toModel
import models.ReCommand
import models.ReState
import ru.otus.otuskotlin.realestate.api.v1.models.Request
import ru.otus.otuskotlin.realestate.api.v1.models.Response
import toTransportAd


suspend inline fun <reified Q : Request, reified R : Response>
        ApplicationCall.controllerHelper(command: ReCommand? = null, block: ReContext.() -> Unit) {
    val ctx = ReContext(
        timeStart = Clock.System.now(),
        principal = principal<JWTPrincipal>().toModel(),
    )
    try {
        val request = receive<Q>()
        ctx.fromTransport(request)
        ctx.block()
        val response = ctx.toTransportAd()
        respond(response)
    } catch (e: Throwable) {
        command?.also { ctx.command = it }
        ctx.state = ReState.FAILING
        ctx.errors.add(e.asReError())
        ctx.block()
        val response = ctx.toTransportAd()
        respond(response)
    }
}
