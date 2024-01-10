package moe.fuqiuluo.unidbg.env.files

import moe.fuqiuluo.comm.CommonConfig.qSpecLucky
import moe.fuqiuluo.ext.hex2ByteArray
import java.security.SecureRandom
import java.util.*


fun fetchQSpecFile(): ByteArray {
    return "413337303737423543384244343030303933313532334642313039414633344145364530313344353546343933463144423437434635413638393637433138314542453131434441313132373937333244434431334634433843413146363934464441323439433245413942363536344546304644354444383936464232334638453830443345343136303542434346C81C89B1C942BE07273D2C$qSpecLucky".trimIndent().hex2ByteArray()
}

fun generateLucky(size: Int): String {
    val secureRandom = SecureRandom()
    val byteArray = ByteArray(size)
    secureRandom.nextBytes(byteArray)
    return byteArray.joinToString("") { "%02x".format(it) }.uppercase(Locale.getDefault())
}

// s处本身的值 D7D9DB72077276E300780AADFEFD7D221083DE0C61C2C73D42A9F8DBE4C97B43DD96E2A422