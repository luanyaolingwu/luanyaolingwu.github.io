package moe.fuqiuluo.api

import BuildConfig
import CONFIG
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import moe.fuqiuluo.comm.Protocol
import java.lang.management.ManagementFactory

@Serializable
data class APIResult<T>(
    val code: Int,
    val msg: String = "",
    @Contextual
    val data: T? = null
)

@Serializable
data class APIInfo(
    val version: String,
    val protocol: Protocol,
    val pid: Int
)

fun Routing.index() {
    get("/") {
        call.respond(APIResult(0, "IAA 云天明 章北海", APIInfo(
            version = BuildConfig.version,
            protocol = CONFIG.protocol,
            pid = runCatching{ ManagementFactory.getRuntimeMXBean().name.split("@")[0].toInt() }.getOrNull() ?: -1
        )))
    }
}
