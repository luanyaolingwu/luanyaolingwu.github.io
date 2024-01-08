package moe.fuqiuluo.comm

import kotlinx.serialization.Serializable
import moe.fuqiuluo.comm.CommonConfig
import java.io.File

@Serializable
data class CommonConfigExt(
    var virtualRootPath: String = "virtualRoot",
    var appInstallFolder: String = "/data/app/~~nNzv5koU9DgkrbtCpa02wQ==/\${packageName}-fR9VqAFGIZNVZ8MgZYh0Ow==",
    var screenSizeWidth: Int = 1080,
    var screenSizeHeight: Int = 2400,
    var density: String = "2.75",
    var serialNumber: String = "0x0000043be8571339",
    var androidVersion: String = "13",
    var androidSdkVersion: Int = 33,
    var targetSdkVersion: Int = 29,
    var storageSize: String = "137438953471"
) {
    fun apply() = CommonConfig.also {
        it.virtualRootPath = File(virtualRootPath)
        it.appInstallFolder = appInstallFolder
        it.screenSizeWidth = screenSizeWidth
        it.screenSizeHeight = screenSizeHeight
        it.density = density
        it.serialNumber = serialNumber
        it.androidVersion = androidVersion
        it.androidSdkVersion = androidSdkVersion
        it.targetSdkVersion = targetSdkVersion
        it.storageSize = storageSize
    }
}