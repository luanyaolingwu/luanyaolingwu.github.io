package moe.fuqiuluo.api

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import moe.fuqiuluo.ext.failure
import moe.fuqiuluo.ext.fetchGet
import moe.fuqiuluo.ext.hex2ByteArray
import moe.fuqiuluo.ext.toHexString
import moe.fuqiuluo.utils.EMPTY_BYTE_ARRAY

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
        if(!(data.startsWith("810_") || data.startsWith("812_"))) {
            failure(-2, "data参数不合法")
        }

        val mode = fetchGet("mode", def = when(data) {
            "810_d", "810_a", "810_f", "810_9" -> "v2"
            "810_2", "810_25", "810_7", "810_24" -> "v1"
            "812_b", "812_a" -> "v3"
            "812_5" -> "v4"
            else -> ""
        })?.also {
            if (it.isBlank()) failure(-3, "无法自动决断mode，请主动提供")
        }

        val androidId = fetchGet("android_id", def = "")
//        val guid = fetchGet("guid", def = null)?.hex2ByteArray()
//        val version = fetchGet("version", def = null)
//        val phone = fetchGet("phone", def = "86-13611451810")?.toByteArray() // 86-xxx
//        val receipt = fetchGet("receipt", def = null)?.toByteArray()
//        val code = fetchGet("code", def = null)?.toByteArray()

        var guid: ByteArray? = null
        var version = ""
        var phone: ByteArray? = null
        var receipt: ByteArray? = null
        var code = ""

        try {
            when (mode) {
                "v1", "v2" -> {
                    version = fetchGet("version", def = null)!!
                    guid = fetchGet("guid", def = null)?.hex2ByteArray()!!
                }

                "v3" -> {
                    version = fetchGet("version", def = null)!!
                    phone = fetchGet("phone", def = "86-13611451810")?.toByteArray()!! // 86-xxx
                }

                "v4" -> {
                    receipt = fetchGet("receipt", def = null)?.toByteArray()!!
                    code = fetchGet("code", def = null)!!
                }

                else -> {
                    EMPTY_BYTE_ARRAY
                }
            }
        } catch (e: Exception) {
            failure(-1, e.toString())
            e.printStackTrace()
        }

        var stackTraceMSG = ""
        val sign = kotlin.runCatching {
            UnidbgFetchQSign.energy(uin, data, mode, version, guid, androidId, phone, receipt, code)
        }.onFailure {
            stackTraceMSG = it.toString()
            it.printStackTrace()
        }.getOrNull()

        if (sign == null) {
            call.respond(failure(-1, stackTraceMSG))
        } else {
            call.respond(APIResult(0, "success", sign.toHexString()))
        }
    }
}