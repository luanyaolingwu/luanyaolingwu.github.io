package moe.fuqiuluo.api

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import moe.fuqiuluo.ext.fetchGet
import moe.fuqiuluo.ext.toHexString

fun Routing.addedSign() {
    get("/friend_sign") {
        val addUin = fetchGet("add_uin")!!.toLong()
        val source = fetchGet("source")!!.toInt()
        val uin = fetchGet("uin")!!.toLong()
        val androidId = fetchGet("android_id", def = "")
        val guid = fetchGet("guid", def = "")

        // val session = initSession(uin) ?: run {
        //     val androidId = fetchGet("android_id", def = "")
        //     val guid = fetchGet("guid", def = "")
        //     if (androidId.isNullOrEmpty() || guid.isNullOrEmpty()) {
        //         throw MissingKeyError
        //     }
        //     SessionManager.register(EnvData(uin, androidId, guid.lowercase(), "", CONFIG.protocol.qua, CONFIG.protocol.version, CONFIG.protocol.code))
        //     findSession(uin)
        // }

        // val sign = session.withRuntime {
        //     Dandelion.energy(session.vm, "add_friend", BytePacketBuilder().also {
        //         it.writeLong(uin)
        //         it.writeLong(addUin.toLong())
        //         it.writeInt(source.toInt())
        //     }.toByteArray())
        // }

        val sign = kotlin.runCatching {
            UnidbgFetchQSign.addedSign(addUin, source, uin, androidId, guid)
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()

        if (sign == null) {
            call.respond(APIResult(-1, "failed", null))
        } else {
            call.respond(APIResult(0, "success", sign.toHexString()))
        }

    }
    get("/group_sign") {
        val addUin = fetchGet("group_uin")!!.toLong()
        val source = fetchGet("source")!!.toInt()
        val subsource = fetchGet("sub_source")!!.toInt()
        val uin = fetchGet("uin")!!.toLong()
        val androidId = fetchGet("android_id", def = "")
        val guid = fetchGet("guid", def = "")

        // val session = initSession(uin) ?: run {
        //     if (androidId.isNullOrEmpty() || guid.isNullOrEmpty()) {
        //         throw MissingKeyError
        //     }
        //     SessionManager.register(EnvData(uin, androidId, guid.lowercase(), "", CONFIG.protocol.qua, CONFIG.protocol.version, CONFIG.protocol.code))
        //     findSession(uin)
        // }

        // val sign = session.withRuntime {
        //     Dandelion.energy(session.vm, "add_group", BytePacketBuilder().also {
        //         it.writeLong(uin)
        //         it.writeLong(addUin)
        //         it.writeInt(source)
        //         it.writeInt(subsource)
        //     }.toByteArray())
        // }

        val sign = kotlin.runCatching {
            UnidbgFetchQSign.groupsign(addUin, source, subsource, uin, androidId, guid)
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