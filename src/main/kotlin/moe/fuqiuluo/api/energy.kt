package moe.fuqiuluo.api

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import moe.fuqiuluo.ext.fetchGet
import moe.fuqiuluo.ext.hex2ByteArray
import moe.fuqiuluo.ext.toHexString

fun Routing.energy() {
    get("/custom_energy") {
        val uin = fetchGet("uin")!!.toLong()
        val data = fetchGet("data")!!
        val salt = fetchGet("salt")!!.hex2ByteArray()

        val androidId = fetchGet("android_id", def = "")
        val guid = fetchGet("guid", def = "")
        val sign = kotlin.runCatching {
            UnidbgFetchQSign.customEnergy(uin, data, salt, androidId, guid)
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()

        if (sign == null) {
            call.respond(APIResult(-1, "failed", null))
        } else {
            call.respond(APIResult(0, "success", sign.toHexString()))
        }
    }

    get("/energy") {
        val uin = fetchGet("uin")!!.toLong()

        val data = fetchGet("data")!!
        val androidId = fetchGet("android_id", def = "")
        val guid = fetchGet("guid", def = null)?.hex2ByteArray()
        val version = fetchGet("version", def = null)
        val phone = fetchGet("phone", def = null)?.toByteArray() // 86-xxx
        val receipt = fetchGet("receipt", def = null)?.toByteArray()
        val sign = kotlin.runCatching {
            UnidbgFetchQSign.energy(uin, data, null, version, guid, androidId, phone, receipt)
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()

        if (sign == null) {
            call.respond(APIResult(-1, "failed", null))
        } else {
            call.respond(APIResult(0, "success", sign.toHexString()))
        }
    }
}