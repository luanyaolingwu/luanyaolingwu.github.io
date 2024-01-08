package moe.fuqiuluo.api

import CONFIG
import com.tencent.crypt.Crypt
import com.tencent.mobileqq.channel.ChannelManager
import com.tencent.mobileqq.channel.SsoPacket
import com.tencent.mobileqq.qsec.qsecdandelionsdk.Dandelion
import com.tencent.mobileqq.sign.QQSecuritySign
import io.ktor.utils.io.core.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull
import moe.fuqiuluo.comm.EnvData
import moe.fuqiuluo.ext.toByteArray
import moe.fuqiuluo.ext.toUHexString
import moe.fuqiuluo.unidbg.session.Session
import moe.fuqiuluo.unidbg.session.SessionManager
import moe.fuqiuluo.utils.EMPTY_BYTE_ARRAY
import moe.fuqiuluo.utils.MD5
import java.nio.ByteBuffer
import kotlin.concurrent.timer
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.text.String
import kotlin.text.toByteArray

object UnidbgFetchQSign {
    suspend fun customEnergy(uin: Long, cmd: String, salt: ByteArray, androidId: String? = null, guid: String? = null, qimei36: String = ""): ByteArray {
        val session = initSession(uin) ?: run {
            if (androidId.isNullOrEmpty() || guid == null) {
                throw MissingKeyError
            }
            SessionManager.register(EnvData(uin, androidId, guid, qimei36, CONFIG.protocol.qua, CONFIG.protocol.version, CONFIG.protocol.code))

            findSession(uin)
        }

        val sign = session.withLock {
            Dandelion.energy(session.vm, cmd, salt)
        }
        return sign
    }

    // 但愿霉逝
    suspend fun addedSign (
        addUin: Long, source: Int,
        uin: Long, androidId: String? = null,
        guid: String? = null
    ): ByteArray {
        val session = initSession(uin) ?: run {
            if (androidId.isNullOrEmpty() || guid.isNullOrEmpty()) {
                throw MissingKeyError
            }
            SessionManager.register(EnvData(uin, androidId, guid.lowercase(), "", CONFIG.protocol.qua, CONFIG.protocol.version, CONFIG.protocol.code))
            findSession(uin)
        }

        val salt = (BytePacketBuilder().also {
            it.writeLong(uin)
            it.writeLong(addUin)
            it.writeInt(source)
        }).toByteArray()

        val sign = session.withLock {
            Dandelion.energy(session.vm, "add_friend", salt)
        }
        return sign
    }

    suspend fun groupsign(
        addUin: Long, source: Int,
        subsource: Int, uin: Long,
        androidId: String? = null,
        guid: String? = null
    ): ByteArray {
        val session = initSession(uin) ?: run {
            if (androidId.isNullOrEmpty() || guid.isNullOrEmpty()) {
                throw MissingKeyError
            }
            SessionManager.register(EnvData(uin, androidId, guid.lowercase(), "", CONFIG.protocol.qua, CONFIG.protocol.version, CONFIG.protocol.code))
            findSession(uin)
        }

        val salt = (BytePacketBuilder().also {
            it.writeLong(uin)
            it.writeLong(addUin)
            it.writeInt(source)
            it.writeInt(subsource)
        }).toByteArray()
        val sign = session.withLock {
            Dandelion.energy(session.vm, "add_group", salt)
        }
        return sign
    }

    suspend fun energy(
        uin: Long, data: String,
        modeString: String? = null,
        version: String? = null, guid: ByteArray? = null,
        androidId: String? = null,
        phone: ByteArray? = null, receipt: ByteArray? = null,
        code: String? = null): ByteArray {
        val session = initSession(uin) ?: run {
            if (androidId.isNullOrEmpty() || guid == null) {
                throw MissingKeyError
            }
            SessionManager.register(EnvData(uin, androidId, guid.toUHexString("").lowercase(), "", CONFIG.protocol.qua, CONFIG.protocol.version, CONFIG.protocol.code))
            findSession(uin)
        }

        /* if (!(data.startsWith("810_") || data.startsWith("812_"))) {
            error("data参数不合法")
        } */
        val mode = modeString
        /*    ?: when (data) {
                "810_d", "810_a", "810_f", "810_9" -> "v2"
                "810_2", "810_25", "810_7", "810_24" -> "v1"
                "812_b", "812_a" -> "v3"
                "812_5" -> "v4"
                else -> ""
            }
        if (mode.isBlank()) error("无法自动决断mode，请主动提供") */


        val salt = when (mode) {
            "v1" -> {
                if (version == null) error("lack of version")
                if (guid == null) error("lack of guid")
                val sub = data.substring(4).toInt(16)
                val salt = ByteBuffer.allocate(8 + 2 + guid.size + 2 + 10 + 4)
                salt.putLong(uin)
                salt.putShort(guid.size.toShort())
                salt.put(guid)
                salt.putShort(version.length.toShort())
                salt.put(version.toByteArray())
                salt.putInt(sub)
                salt.array()
            }

            "v2" -> {
                if (version == null) error("lack of version")
                if (guid == null) error("lack of guid")
                val sub = data.substring(4).toInt(16)
                val salt = ByteBuffer.allocate(4 + 2 + guid.size + 2 + 10 + 4 + 4)
                salt.putInt(0)
                salt.putShort(guid.size.toShort())
                salt.put(guid)
                salt.putShort(version.length.toShort())
                salt.put(version.toByteArray())
                salt.putInt(sub)
                salt.putInt(0)
                salt.array()
            }

            "v3" -> { // 812_a
                if (version == null) error("lack of version")
                if (phone == null) error("lack of phone")
                val salt = ByteBuffer.allocate(phone.size + 2 + 2 + version.length + 2)
                // 38 36 2D 31 37 33 36 30 32 32 39 31 37 32
                // 00 00
                // 00 06
                // 38 2E 39 2E 33 38
                // 00 00
                // result => 0C051B17347DF3B8EFDE849FC233C88DBEA23F5277099BB313A9CD000000004B744F7A00000000
                salt.put(phone)
                println(String(phone))
                salt.putShort(0)
                salt.putShort(version.length.toShort())
                salt.put(version.toByteArray())
                salt.putShort(0)
                salt.array()
            }

            "v4" -> { // 812_5
                if (receipt == null) error("lack of receipt")
                if (code == null) error("lack of code")
                val key = MD5.toMD5Byte(code)
                val encrypt = Crypt().encrypt(receipt, key)
                val salt = ByteBuffer.allocate(receipt.size + 2 + encrypt.size)
                salt.put(receipt)
                salt.putShort(encrypt.size.toShort())
                salt.put(encrypt)
                salt.array()
            }

            else -> {
                EMPTY_BYTE_ARRAY
            }
        }

        val sign = session.withLock  {
            Dandelion.energy(session.vm, data, salt)
        }
        return sign
    }


    suspend fun sign(
        cmd: String,
        uin: Long,
        seq: Int,
        buffer: ByteArray,
        qua: String = CONFIG.protocol.version,
        qimei36: String = "",
        androidId: String = "",
        guid: String = ""
    ): Sign {
        val session = initSession(uin) ?: run {
            if (androidId.isEmpty() || guid.isEmpty()) {
                throw MissingKeyError
            }
            SessionManager.register(EnvData(
                uin,
                androidId,
                guid.lowercase(),
                qimei36,
                qua,
                CONFIG.protocol.version,
                CONFIG.protocol.code
            ))
            findSession(uin)
        }
        val vm = session.vm
        if (qimei36.isNotEmpty()) {
            vm.global["qimei36"] = qimei36
        }

        var o3did = ""
        val list = arrayListOf<SsoPacket>()

        val sign = session.withLock {
            QQSecuritySign.getSign(vm, qua, cmd, buffer, seq, uin.toString()).value.also {
                o3did = vm.global["o3did"] as? String ?: ""
                val requiredPacket = vm.global["PACKET"] as ArrayList<*>
                list.addAll(requiredPacket.map { it as SsoPacket })
                requiredPacket.clear()
            }
        }

        return sign.run {
            Sign(
                this.token,
                this.extra,
                this.sign, o3did, list
            )
        }
    }
    /**
     * 注册信息
     * @return 此前是否已注册过信息
     */
    fun register(
        uin: Long,
        androidId: String,
        guid: String,
        qimei36: String,
        overrideQua: String? = null,
        overrideVersion: String? = null,
        overrideCode: String? = null
    ): Boolean {
        val qua = overrideQua ?: CONFIG.protocol.qua
        val version = overrideVersion ?: CONFIG.protocol.version
        val code = overrideCode ?: CONFIG.protocol.code

        val hasRegister = uin in SessionManager
        SessionManager.register(EnvData(uin, androidId, guid, qimei36, qua, version, code))
        return hasRegister
    }

    fun destroy(
        uin: Long
    ): Boolean {
        return if (uin in SessionManager) {
            SessionManager.close(uin)
            true
        } else {
            false
        }
    }

    suspend fun requestToken(
        uin: Long,
        isForced: Boolean = false,
        androidId: String = "",
        guid: String = "",
        qimei36: String = "",
    ): Pair<Boolean, List<SsoPacket>> {
        val session = initSession(uin) ?: run {
            if (androidId.isEmpty() || guid.isEmpty()) {
                throw MissingKeyError
            }
            SessionManager.register(EnvData(
                uin,
                androidId,
                guid.lowercase(),
                qimei36,
                CONFIG.protocol.qua,
                CONFIG.protocol.version,
                CONFIG.protocol.code
            ))
            findSession(uin)
        }

        val vm = session.vm

        if ("HAS_SUBMIT" !in vm.global && !isForced) {
            error("QSign not initialized, unable to request_token, please submit the initialization package first.")
        } else {
            var isSuccessful = true
            val list = arrayListOf<SsoPacket>()
            session.withLock {
                val lock = vm.global["mutex"] as Mutex
                lock.tryLock()
                QQSecuritySign.requestToken(vm)

                withTimeoutOrNull(5000) {
                    lock.withLock {
                        val requiredPacket = vm.global["PACKET"] as ArrayList<*>
                        list.addAll(requiredPacket.map { it as SsoPacket })
                        requiredPacket.clear()
                    }
                } ?: {
                    isSuccessful = false
                }
            }
            return Pair(!isSuccessful, list)
        }
    }
    suspend fun submit(
        uin: Long,
        cmd: String,
        callbackId: Long,
        buffer: ByteArray,
        androidId: String = "",
        guid: String = "",
        qimei36: String = "",
    ) {
        val session = initSession(uin) ?: run {
            if (androidId.isEmpty() || guid.isEmpty()) {
                throw MissingKeyError
            }
            SessionManager.register(EnvData(
                uin,
                androidId,
                guid.lowercase(),
                qimei36,
                CONFIG.protocol.qua,
                CONFIG.protocol.version,
                CONFIG.protocol.code
            ))
            findSession(uin)
        }
        session.withLock {
            ChannelManager.onNativeReceive(session.vm, cmd, buffer, callbackId)
            session.vm.global["HAS_SUBMIT"] = true
        }
    }
}


internal fun initSession(uin: Long): Session? {
    return SessionManager[uin] ?: if (!CONFIG.autoRegister) {
        throw SessionNotFoundError(uin)
    } else {
        null
    }
}

internal fun findSession(uin: Long): Session {
    return SessionManager[uin] ?: throw SessionNotFoundError(uin)
}
internal suspend inline fun <T> Session.withLock(action: () -> T): T {
    return mutex.withLockAndTimeout(5000, action)
}
@OptIn(ExperimentalContracts::class)
internal suspend inline fun <T> Mutex.withLockAndTimeout(timeout: Long, action: () -> T): T {
    contract {
        callsInPlace(action, InvocationKind.EXACTLY_ONCE)
    }

    lock()
    val job = timer(initialDelay = timeout, period = timeout) {
        if (isLocked)
            unlock()
    }
    try {
        return action().also {
            job.cancel()
        }
    } finally {
        if (isLocked) try {
            unlock()
        } catch (_: Exception) {
        }
    }
}

class Sign(
    val token: ByteArray,
    val extra: ByteArray,
    val sign: ByteArray,
    val o3did: String,
    val requestCallback: List<SsoPacket>
)

class SessionNotFoundError(val uin: Long): RuntimeException("Uin $uin is not registered.")

object MissingKeyError: RuntimeException("First use must be submitted with android_id and guid.")
