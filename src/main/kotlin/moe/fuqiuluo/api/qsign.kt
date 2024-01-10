@file:Suppress("UNCHECKED_CAST")

package moe.fuqiuluo.api

import CONFIG
import com.tencent.mobileqq.channel.SsoPacket
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.Serializable
import moe.fuqiuluo.ext.*

fun Routing.sign() {
    get("/sign") {
        val uin = fetchGet("uin")!!
        val qua = fetchGet("qua", CONFIG.protocol.qua)!!
        val cmd = fetchGet("cmd")!!
        val seq = fetchGet("seq")!!.toInt()
        val buffer = fetchGet("buffer")!!.hex2ByteArray()
        val qimei36 = fetchGet("qimei36", def = "")!!

        val androidId = fetchGet("android_id", def = "")!!
        val guid = fetchGet("guid", def = "")!!

        requestSign(cmd, uin, qua, seq, buffer, qimei36, androidId, guid)
    }

    post("/sign") {
        val param = call.receiveParameters()
        val uin = fetchPost(param, "uin")!!
        val qua = fetchPost(param, "qua", CONFIG.protocol.qua)!!
        val cmd = fetchPost(param, "cmd")!!
        val seq = fetchPost(param, "seq")!!.toInt()
        val buffer = fetchPost(param, "buffer")!!.hex2ByteArray()
        val qimei36 = fetchPost(param, "qimei36", def = "")!!

        val androidId = param["android_id"] ?: ""
        val guid = param["guid"] ?: ""

        requestSign(cmd, uin, qua, seq, buffer, qimei36, androidId, guid)
    }
}

@Serializable
private data class qSign(
    val token: String,
    val extra: String,
    val sign: String,
    val o3did: String,
    val requestCallback: List<SsoPacket>
)

private suspend fun PipelineContext<Unit, ApplicationCall>.requestSign(
    cmd: String,
    uin: String,
    qua: String,
    seq: Int,
    buffer: ByteArray,
    qimei36: String,
    androidId: String,
    guid: String
) {
    var stackTraceMSG = ""
    val sign = runCatching {
        UnidbgFetchQSign.sign(cmd, uin.toLong(), seq, buffer, qua, qimei36, androidId, guid)
    }.onFailure {
        stackTraceMSG = it.toString()
        it.printStackTrace()
    }.getOrNull()

    if (sign == null) {
        call.respond(failure(-1, stackTraceMSG))
    } else {
        call.respond(
            APIResult(
                0, "success", qSign(
                    sign.token.toHexString(),
                    sign.extra.toHexString(),
                    sign.sign.toHexString(), sign.o3did, sign.requestCallback
                )
            )
        )
    }
}