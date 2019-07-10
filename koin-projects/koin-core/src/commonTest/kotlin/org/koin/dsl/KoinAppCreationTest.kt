package org.koin.dsl

import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.error.KoinAppAlreadyStartedException
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger
import org.koin.multiplatform.dispatchThread
import org.koin.test.assertDefinitionsCount
import org.koin.test.assertHasNoStandaloneInstance
import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class KoinAppCreationTest {

    @AfterTest
    fun after() {
        stopKoin()
    }

    @Test
    @JsName("make_a_Koin_application")
fun `make a Koin application`() {
        val app = dispatchThread { koinApplication { } }

        app.assertDefinitionsCount(0)

        assertHasNoStandaloneInstance()
    }

    @Test
    @JsName("start_a_Koin_application")
fun `start a Koin application`() {
        val app = dispatchThread {
            startKoin { }
        }

        assertEquals(GlobalContext.get(), app)

        dispatchThread {
            stopKoin()
        }

        assertHasNoStandaloneInstance()
    }

    @Test
    @JsName("can_t_restart_a_Koin_application")
fun `can't restart a Koin application`() {
        startKoin { }
        try {
            startKoin { }
            fail("should throw  KoinAppAlreadyStartedException")
        } catch (e: KoinAppAlreadyStartedException) {
        }
    }

    @Test
    @JsName("allow_declare_a_logger")
fun `allow declare a logger`() {
        dispatchThread{
            startKoin {
                logger(PrintLogger(Level.ERROR))
            }
        }

        assertEquals(KoinApplication.logger.level, Level.ERROR)

        KoinApplication.logger.debug("debug")
        KoinApplication.logger.info("info")
        KoinApplication.logger.error("error")
    }

    @Test
    @JsName("allow_declare_a_print_logger_level")
fun `allow declare a print logger level`() {
        dispatchThread{
            startKoin {
                printLogger(Level.ERROR)
            }
        }

        assertEquals(KoinApplication.logger.level, Level.ERROR)

        KoinApplication.logger.debug("debug")
        KoinApplication.logger.info("info")
        KoinApplication.logger.error("error")
    }
}
