package moe.fuqiuluo.api

import CONFIG
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import moe.fuqiuluo.ext.fetchGet

fun Routing.additional() {
    post("/stop") {
        val qkey = fetchGet("qkey")!!

        if (qkey == CONFIG.qkey) {
            call.respond(APIResult(0, "The application is about to close", ""))
            // 停止应用程序
            System.exit(0)
        } else {
            throw WrongQuitTokenError
        }
    }
}