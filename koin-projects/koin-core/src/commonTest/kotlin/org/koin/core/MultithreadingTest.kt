package org.koin.core

import co.touchlab.testhelp.concurrency.ThreadOperations
import org.koin.Simple
import org.koin.core.mp.FrozenDelegate
import org.koin.core.mp.freeze
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.dsl.onRelease
import kotlin.test.Test
import kotlin.test.assertEquals

class MultithreadingTest {
    @Test
    fun accessFromOtherThread() {

        var result : String by FrozenDelegate("")
        val app = koinApplication {
            printLogger()
            modules(
                module {
                    scope(named("test")) {
                        scoped { Simple.Component1() } onRelease { result = "release" }
                    }
                })
        }

        val ops = ThreadOperations {}
        var ref : Throwable? by FrozenDelegate(null)
        ops.exe {
            val koin = app.koin

            try {
                val scopeInstance = koin.createScope("test", named("test"))
                scopeInstance.get<Simple.Component1>()
                scopeInstance.close()
            } catch (e: Exception) {
                ref = e.freeze()
            }
        }

        ops.run(1)

        val throwable = ref
        if(throwable != null)
        {
            throw throwable
        }

        val koin = app.koin
        val scopeInstance = koin.createScope("test", named("test"))
        scopeInstance.get<Simple.Component1>()
        scopeInstance.close()
        assertEquals("release", result)
    }
}
