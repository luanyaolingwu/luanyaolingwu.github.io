package moe.fuqiuluo.comm

import moe.fuqiuluo.unidbg.env.files.generateLucky
import java.io.File

object CommonConfig {
    @JvmStatic
    var virtualRootPath : File? = null
    @JvmStatic
    var appInstallFolder = "/data/app/~~nNzv5koU9DgkrbtCpa02wQ==/\${packageName}-fR9VqAFGIZNVZ8MgZYh0Ow=="
    @JvmStatic
    var screenSizeWidth = 1080
    @JvmStatic
    var screenSizeHeight = 2400
    @JvmStatic
    var density = "2.75"
    @JvmStatic
    var serialNumber = "0x0000043be8571339"
    @JvmStatic
    var androidVersion = "13"
    @JvmStatic
    var androidSdkVersion = 33
    @JvmStatic
    var targetSdkVersion = 29
    @JvmStatic
    var storageSize = "137438953471"
    @JvmStatic
    var lqLucky= generateLucky(32)
    @JvmStatic
    var qSpecLucky= generateLucky(37)
}
