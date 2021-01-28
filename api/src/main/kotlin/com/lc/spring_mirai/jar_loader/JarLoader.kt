package com.lc.spring_mirai.jar_loader

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.URL
import java.net.URLClassLoader
import java.util.zip.ZipFile
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

object JarLoader {
    fun load(path: String, handle: (Class<*>) -> Unit){
        val jarFile = File(path)
        val zipFile = ZipFile(jarFile)
        val classLoader = JarLoader::class.java.classLoader
        val url = jarFile.toURI().toURL()

        val enumeration = zipFile.entries()
        while (enumeration.hasMoreElements()) {
            val zipEntry = enumeration.nextElement()
            if (zipEntry.name.endsWith(".class")) {
                val className = zipEntry.name.replace("/", ".").replace(".class", "")
                if (className.contains("Logger"))
                    continue
                val clazz = classLoader.loadClass(className)
                handle(clazz)
            }

        }
    }

    fun load1(path: String, handle: (Class<*>) -> Unit) {
        val jarFile = File(path)
        val addUrl = URLClassLoader::class.java.getDeclaredMethod("addURL", URL::class.java)
        addUrl.isAccessible = true
        val classLoader = ClassLoader.getSystemClassLoader() as URLClassLoader
        val url = jarFile.toURI().toURL()
        addUrl.invoke(classLoader, url)
    }

    fun load2(path: String, handle: (Class<*>) -> Unit) {

    }

}
fun main() {
    val path = """E:\code\idea\bestjsMidi\out\artifacts\bestjs_jar\bestjs.jar"""
    JarLoader.load1(path) {
        println(it.kotlin.jvmName)
    }
}