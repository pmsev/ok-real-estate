import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import java.time.Instant

suspend fun <T> Logger.wrapWithLogging(
    id: String = "",
    block: suspend () -> T,
): T = try {
    val timeStart = Instant.now()
    info("Entering $id")
    block().also {
        val diffTime = Instant.now().toEpochMilli() - timeStart.toEpochMilli()
        info("Finishing $id", Pair("metricHandleTime", diffTime))
    }
} catch (e: Throwable) {
    error("Failing $id", e)
    throw e
}

fun reLogger(loggerId: String): ReLogWrapper = reLogger(
    logger = LoggerFactory.getLogger(loggerId) as Logger
)

fun reLogger(cls: Class<out Any>): ReLogWrapper = reLogger(
    logger = LoggerFactory.getLogger(cls) as Logger
)

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun reLogger(logger: Logger): ReLogWrapper = ReLogWrapper(
    logger = logger,
    loggerId = logger.name,
)
