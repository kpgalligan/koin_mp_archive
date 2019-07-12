package org.koin.test

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level
import org.koin.test.mock.declare
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.fail

class DeclareTest : KoinTest {

    @Test
    @JsName("declare_on_the_fly_with_KoinTest")
    fun `declare on the fly with KoinTest`() {
        startKoin {
            printLogger(Level.DEBUG)
        }

        try {
            get<Simple.ComponentA>()
            fail()
        } catch (e: Exception) {
        }

        declare {
            single { Simple.ComponentA() }
        }

        get<Simple.ComponentA>()

        stopKoin()
    }
}
