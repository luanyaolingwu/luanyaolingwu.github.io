package moe.fuqiuluo.api

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import moe.fuqiuluo.ext.fetchGet
import moe.fuqiuluo.ext.hex2ByteArray


fun Routing.submit() {
    get("/submit") {
        val uin = fetchGet("uin")!!.toLong()
        val cmd = fetchGet("cmd")!!
        val callbackId = fetchGet("callback_id")!!.toLong()
        val buffer = fetchGet("buffer")!!.hex2ByteArray()
        val androidId = fetchGet("android_id", def = "")!!
        val guid = fetchGet("guid", def = "")!!.lowercase()
        val qimei36 = fetchGet("qimei36", def = "")!!.lowercase()

        UnidbgFetchQSign.submit(uin, cmd, callbackId, buffer, androidId, guid, qimei36)

        call.respond(APIResult(0, "submit success", ""))
    }
}