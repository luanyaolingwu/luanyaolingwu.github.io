@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package moe.fuqiuluo.api

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import moe.fuqiuluo.ext.fetchGet

fun Routing.requestToken() {
    get("/request_token") {
        val uin = fetchGet("uin")!!.toLong()
        val isForced = fetchGet("force", def = "false")

        val pair = runCatching {
            UnidbgFetchQSign.requestToken(uin, isForced == "true")
        }.onFailure {
            call.respond(APIResult(-1, it.message.toString(), ""))
        }.getOrNull() ?: return@get

        val isSuccessful = pair.first
        val list = pair.second

        call.respond(
            APIResult(
                if (!isSuccessful) -1 else 0,
                if (!isSuccessful) "request_token timeout" else "submit success",
                list
            )
        )
    }
}