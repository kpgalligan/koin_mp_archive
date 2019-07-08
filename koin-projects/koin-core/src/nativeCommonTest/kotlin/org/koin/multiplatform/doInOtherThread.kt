package org.koin.multiplatform

import kotlin.native.concurrent.*
import kotlin.random.Random

private val workers = Array(4) { Worker.start() }

actual fun <T> doInOtherThread(block: () -> T): T {
    val threadInt = Random.nextInt(4)
    return if (threadInt == 3)
        block()
    else {
        val future = workers[threadInt].execute(TransferMode.SAFE, { block.freeze() }) {
            try {
                it()
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
            /*println("trace 1")
            try {
                println("trace 2")
                it()
                println("trace 3")
            } catch (e: Throwable) {
                println("BEFORE STACK TRACE")
                e.printStackTrace()
                println("AFTER STACK TRACE")
                e
            }*/
        }

        future.result

        /*println("trace 0a")
        val res = future.result
        println("trace 0b")
        if(res is Throwable)
            throw res
        else
            return res as T*/
    }
}
