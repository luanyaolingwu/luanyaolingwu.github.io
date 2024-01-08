package moe.fuqiuluo.ext

import moe.fuqiuluo.utils.BytesUtil
import java.util.*
import kotlin.experimental.xor

@JvmOverloads fun String.hex2ByteArray(replace: Boolean = false): ByteArray {
    val s = if (replace) this.replace(" ", "")
        .replace("\n", "")
        .replace("\t", "")
        .replace("\r", "") else this
    val bs = ByteArray(s.length / 2)
    for (i in 0 until s.length / 2) {
        bs[i] = s.substring(i * 2, i * 2 + 2).toInt(16).toByte()
    }
    return bs
}

@JvmOverloads fun ByteArray.toHexString(uppercase: Boolean = true): String = this.joinToString("") {
    (it.toInt() and 0xFF).toString(16)
        .padStart(2, '0')
        .let { s -> if (uppercase) s.lowercase(Locale.getDefault()) else s }
}

fun ByteArray.xor(key: ByteArray): ByteArray {
    val result = ByteArray(this.size)
    for (i in 0 until this.size) {
        result[i] = (this[i] xor key[i % key.size] xor ((i and 0xFF).toByte()))
    }
    return result
}

fun ByteArray.sub(offset: Int, length: Int) = BytesUtil.subByte(this, offset, length)

fun ByteArray.toAsciiHexString() = joinToString("") {
    if (it in 32..127) it.toInt().toChar().toString() else "{${
        it.toUByte().toString(16).padStart(2, '0').uppercase(
            Locale.getDefault())
    }}"
}


public fun ByteArray.checkOffsetAndLength(offset: Int, length: Int) {
    require(offset >= 0) { "offset shouldn't be negative: $offset" }
    require(length >= 0) { "length shouldn't be negative: $length" }
    require(offset + length <= this.size) { "offset ($offset) + length ($length) > array.size (${this.size})" }
}

public fun ByteArray.toUHexString(
    separator: String = " ",
    offset: Int = 0,
    length: Int = this.size - offset
): String {
    this.checkOffsetAndLength(offset, length)
    if (length == 0) {
        return ""
    }
    val lastIndex = offset + length
    return buildString(length * 2) {
        this@toUHexString.forEachIndexed { index, it ->
            if (index in offset until lastIndex) {
                val ret = it.toUByte().toString(16).uppercase()
                if (ret.length == 1) append('0')
                append(ret)
                if (index < lastIndex - 1) append(separator)
            }
        }
    }
}