@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package moe.fuqiuluo.api

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import moe.fuqiuluo.ext.failure
import moe.fuqiuluo.ext.fetchGet

fun Routing.requestToken() {
    get("/request_token") {
        val uin = fetchGet("uin")!!.toLong()
        val androidId = fetchGet("android_id", def = "")!!.toString()
        val guid = fetchGet("guid", def = "")!!.toString()
        val qimei36 = fetchGet("qimei36", def = "")!!.toString()
        val isForced = fetchGet("force", def = "false")!!.toString()

        val pair = runCatching {
            UnidbgFetchQSign.requestToken(uin, isForced == "true",androidId ,guid ,qimei36)
        }.onFailure {
            call.respond(failure(-1, it.message.toString()))
        }.getOrNull() ?: return@get

        val isSuccessful = pair.first
        val list = pair.second

        call.respond(
            if (!isSuccessful) {
                failure(-1, "request_token timeout")
            } else {
                APIResult(0, "submit success", list)
            }

        )
    }
}