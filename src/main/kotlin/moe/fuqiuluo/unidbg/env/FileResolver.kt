package moe.fuqiuluo.unidbg.env

import com.github.unidbg.Emulator
import com.github.unidbg.file.FileResult
import com.github.unidbg.file.linux.AndroidFileIO
import com.github.unidbg.linux.android.AndroidResolver
import com.github.unidbg.linux.file.ByteArrayFileIO
import com.github.unidbg.linux.file.DirectoryFileIO
import com.github.unidbg.linux.file.SimpleFileIO
import com.github.unidbg.unix.UnixEmulator
import moe.fuqiuluo.comm.CommonConfig
import moe.fuqiuluo.ext.hex2ByteArray
import moe.fuqiuluo.unidbg.QSecVM
import moe.fuqiuluo.unidbg.env.files.*
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*

class FileResolver(
    sdk: Int,
    val vm: QSecVM
): AndroidResolver(sdk) {
    private val tmpFilePath = vm.coreLibPath
    private val uuid = UUID.randomUUID()
    companion object {
        val logger = LoggerFactory.getLogger("FileResolver")!!

        fun getAppInstallFolder(packageName: String): String {
            return CommonConfig.appInstallFolder.replace("\${packageName}", packageName)
        }
    }
    init {
        for (s in arrayOf("stdin", "stdout", "stderr")) {
            tmpFilePath.resolve(s).also {
                if (it.exists()) it.delete()
            }
        }
    }

    override fun resolve(emulator: Emulator<AndroidFileIO>, path: String, oflags: Int): FileResult<AndroidFileIO>? {
        val result = super.resolve(emulator, path, oflags)
        if (result == null || !result.isSuccess) {
            return this.resolve(emulator, path, oflags, result)
        }
        return result
    }

    private fun resolve(emulator: Emulator<AndroidFileIO>, path: String, oflags: Int, def: FileResult<AndroidFileIO>?): FileResult<AndroidFileIO>? {
        if (path == "stdin" || path == "stdout" || path == "stderr") {
            return FileResult.success(SimpleFileIO(oflags, tmpFilePath.resolve(path).also {
                if (!it.exists()) it.createNewFile()
            }, path))
        }

        if (CommonConfig.virtualRootPath != null) {
            val dataFile = File(CommonConfig.virtualRootPath, path)
            if (dataFile.exists()) {
                if (dataFile.isFile) {
                    return FileResult.success(SimpleFileIO(oflags, dataFile, path))
                }
                if (dataFile.isDirectory) {
                    return FileResult.success(DirectoryFileIO(oflags, path, dataFile))
                }
            }
        }

        if (path == "/data/data/com.tencent.tim/lib/libwtecdh.so") {
            return FileResult.failed(UnixEmulator.ENOENT)
        }


        if (path == "/proc/sys/kernel/random/boot_id") {
            return FileResult.success(ByteArrayFileIO(oflags, path, uuid.toString().toByteArray()))
        }
        if (path == "/proc/self/status") {
            return FileResult.success(ByteArrayFileIO(oflags, path, fetchStatus(emulator.pid).toByteArray()))
        }
        if (path == "/proc/stat") {
            return FileResult.success(ByteArrayFileIO(oflags, path, fetchStat()))
        }
        if (path == "/proc/meminfo") {
            return FileResult.success(ByteArrayFileIO(oflags, path, fetchMemInfo()))
        }
        if (path == "/proc/cpuinfo") {
            return FileResult.success(ByteArrayFileIO(oflags, path, fetchCpuInfo()))
        }
        if (path == "/dev/__properties__") {
            return FileResult.success(DirectoryFileIO(oflags, path,
                DirectoryFileIO.DirectoryEntry(true, "properties_serial"),
                DirectoryFileIO.DirectoryEntry(true, "property_info"),
            ))
        }

        if ("/proc/self/maps" == path) {
            return FileResult.success(ByteArrayFileIO(oflags, path, byteArrayOf()))
        }

        if (path == "/system/lib") {
            return FileResult.success(DirectoryFileIO(oflags, path,
                DirectoryFileIO.DirectoryEntry(true, "libhwui.so"),
            ))
        }

        if (path == "/data/data/${vm.envData.packageName}") {
            return FileResult.success(DirectoryFileIO(oflags, path,
                DirectoryFileIO.DirectoryEntry(false, "files"),
                DirectoryFileIO.DirectoryEntry(false, "shared_prefs"),
                DirectoryFileIO.DirectoryEntry(false, "cache"),
                DirectoryFileIO.DirectoryEntry(false, "code_cache"),
            ))
        }

        if (path == "/dev/urandom" ||
            path == "/data/local/su" ||
            path == "/data/local/bin/su" ||
            path == "/data/local/xbin/su" ||
            path == "/sbin/su" ||
            path == "/su/bin/su" ||
            path == "/system/bin/su" ||
            path == "/system/bin/.ext/su" ||
            path == "/system/bin/failsafe/su" ||
            path == "/system/sd/xbin/su" ||
            path == "/system/usr/we-need-root/su" ||
            path == "/system/xbin/su" ||
            path == "/cache/su" ||
            path == "/data/su" ||
            path == "/dev/su" ||
            path.contains("busybox") ||
            path.contains("magisk") ||
            path.contains("supolicy")
        ) {
            logger.warn("""Return cannot access '$path': Not a No such file or directory""")
            return FileResult.failed(UnixEmulator.ENOENT)
        }

        if (path == "/sdcard/Android/" || path == "/storage/emulated/0/Android/") {
            return FileResult.success(DirectoryFileIO(oflags, path,
                DirectoryFileIO.DirectoryEntry(false, "data"),
                DirectoryFileIO.DirectoryEntry(false, "obb"),
            ))
        }

        if (path == "/system/lib64/libhoudini.so" || path == "/system/lib/libhoudini.so") {
            return FileResult.failed(UnixEmulator.ENOENT)
        }

        if (path == "/proc/self/cmdline"
            || path == "/proc/${emulator.pid}/cmdline"
        ) {
            if (vm.envData.packageName == "com.tencent.tim") {
                return FileResult.success(ByteArrayFileIO(oflags, path, vm.envData.packageName.toByteArray()))
            } else {
                return FileResult.success(ByteArrayFileIO(oflags, path, "${vm.envData.packageName}:MSF".toByteArray()))
            }
        }

        if (path == "/data/data") {
            return FileResult.failed(UnixEmulator.EACCES)
        }

        if (path.contains("star_est.xml")) {
            return FileResult.success(ByteArrayFileIO(oflags, path, """
            <?xml version='1.0' encoding='utf-8' standalone='yes' ?>
            <map>
                <string name="id">NS23gm77vjYiyYK554L4aY0SYG5Xgjje</string>
            </map>
            """.trimIndent().toByteArray()))
        }

        if (path == "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq") {
            return FileResult.success(ByteArrayFileIO(oflags, path, "1804800".toByteArray()))
        }

        if (path == "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq") {
            return FileResult.success(ByteArrayFileIO(oflags, path, "300000".toByteArray()))
        }

        if (path == "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq") {
            return FileResult.success(ByteArrayFileIO(oflags, path, "1804800".toByteArray()))
        }

        if (path == "/sys/devices/soc0/serial_number") {
            return FileResult.success(ByteArrayFileIO(oflags, path, CommonConfig.serialNumber.toByteArray()))
        }

        if (path == "/proc") {
            return FileResult.success(DirectoryFileIO(oflags, path,
                DirectoryFileIO.DirectoryEntry(false, emulator.pid.toString()),
                DirectoryFileIO.DirectoryEntry(false, "self"),
                DirectoryFileIO.DirectoryEntry(true, "cpuinfo"),
                DirectoryFileIO.DirectoryEntry(true, "meminfo"),
                DirectoryFileIO.DirectoryEntry(true, "stat"),
                DirectoryFileIO.DirectoryEntry(true, "version"),
            ))
        }


        // Tim签名存在例外!
        // if (path == "/data/app/~~vbcRLwPxS0GyVfqT-nCYrQ==/${vm.envData.packageName}-xJKJPVp9lorkCgR_w5zhyA==/lib/arm64") {
        //     return FileResult.success(DirectoryFileIO(oflags, path,
        //         DirectoryFileIO.DirectoryEntry(true, "libfekit.so"),
        //        DirectoryFileIO.DirectoryEntry(true, "libpoxy.so"),
        //     ))
        val appInstallFolder = getAppInstallFolder(vm.envData.packageName)
        if (path == "$appInstallFolder/lib/arm64") {
            logger.warn("Checking OUT: $path")
            val fileList = (tmpFilePath.listFiles { it -> it.name.endsWith(".so") }?.map {
                DirectoryFileIO.DirectoryEntry(true, it.name)
            } ?: emptyList()).toTypedArray()
            return FileResult.success(DirectoryFileIO(oflags, path, *fileList))
        }



        if(path == "$appInstallFolder/base.apk") {
            val f = tmpFilePath.resolve("QQ.apk")
            if (f.exists()) {
                return FileResult.success(SimpleFileIO(oflags, tmpFilePath.resolve("QQ.apk").also {
                    if (!it.exists()) it.createNewFile()
                }, path))
            } else {
                return FileResult.failed(UnixEmulator.ENOENT)
            }
        }

        if (path == "$appInstallFolder/lib/arm64/libfekit.so") {
            return FileResult.success(SimpleFileIO(oflags, tmpFilePath.resolve("libfekit.so"), path))
        }

        if (path == "$appInstallFolder/lib/arm64/libwtecdh.so") {
            tmpFilePath.resolve("libwtecdh.so").let {
                if (it.exists()) {
                    return FileResult.success(SimpleFileIO(oflags, it, path))
                }
            }
        }

        if (path == "/system/bin/sh" || path == "/system/bin/ls" || path == "/system/lib/libc.so") {
            return FileResult.success(ByteArrayFileIO(oflags, path, byteArrayOf(0x7f, 0x45, 0x4c, 0x46, 0x02, 0x01, 0x01, 0x00)))
        }

        if (path.startsWith("/data/user/")) {
            if (path != "/data/user/0" /*&& path != "/data/user/999"*/) {
                return FileResult.failed(UnixEmulator.ENOENT)
            } else {
                return FileResult.failed(UnixEmulator.EACCES)
            }
        }

        if (path.contains(".system_android_l2")
            || path.contains(".android_lq")
            || path =="/sdcard/Android/.android_lq"
            || path =="/storage/emulated/0/Android/.android_lq"
            || path =="/storage/self/primary/Android/.android_lq") {
            val newPath = if (path.startsWith("C:")) path.substring(2) else path
            val file = tmpFilePath.resolve(".system_android_l2")
            /* if (path =="/data/user/0/com.tencent.mobileqq/files/.android_lq"
                || path =="/data/data/com.tencent.mobileqq/files/.android_lq") {
                logger.info(""".android_lq_from_appdata: '$path'""")
                // if (!file.exists()) {
                    file.writeBytes("6176EF466C7C448C738038596BCB23C351E1B61130E555A229D14ECD14C63D432BB0A2E42FB10079D9AA9053F752AD71AFA102ADBDE16E34483EFF9F411C7AB80B".hex2ByteArray())
                // }
                return FileResult.success(SimpleFileIO(oflags, file, newPath))
            } else { */
                logger.info(""".android_lq_from_sdcard: '$path'""")
                if (!file.exists()) {
                    file.writeBytes("6176EF466C7C448C738038596BCB23C351E1B61130E555A229D14ECD14C63D432BB0A2E42FB10079D9AA9053F752AD71AFA102ADBDE16E34483EFF9F411C7AB80B".hex2ByteArray())
                }
                return FileResult.success(SimpleFileIO(oflags, file, newPath))
            // }
        }

        if (path == "/proc/version") {
            val newPath = if (path.startsWith("C:")) path.substring(2) else path
            val file = tmpFilePath.resolve("ProcVersion")
            if (!file.exists()) {
                file.writeBytes("4C696E75782076657273696F6E20342E31392E3135372D6C696E656167656F732B2028726F6F7440656135636333633963656564292028416E64726F69642028383439303137382C206261736564206F6E2072343530373834642920636C616E672076657273696F6E2031342E302E36202868747470733A2F2F616E64726F69642E676F6F676C65736F757263652E636F6D2F746F6F6C636861696E2F6C6C766D2D70726F6A6563742034633630336566623063636130373465393233386166386234313036633330616464343431386636292C204C4C442031342E302E362920233120534D5020505245454D5054204D6F6E204A616E20312030373A33333A30362055544320323032340A".hex2ByteArray())
            }
            return FileResult.success(SimpleFileIO(oflags, file, newPath))
        }

        if (path == "/data/user/0/com.tencent.mobileqq/files/5463306EE50FE3AA/6FAcBa17D93747A5"
            || path == "/data/data/com.tencent.mobileqq/files/5463306EE50FE3AA/6FAcBa17D93747A5") {
            return FileResult.success(ByteArrayFileIO(oflags, path, fetchQSpecFile()))
        }
        if (path == "/proc/cpuinfo/cmdline" || path == "/proc/meminfo/cmdline" || path == "/proc/version/cmdline" || path == "/proc/stat/cmdline") {
            logger.warn("""Return cannot access '$path': Not a directory""")
            return FileResult.failed(UnixEmulator.ENOTDIR)
        }

        logger.warn("Couldn't find file: $path")
        return def
    }
}