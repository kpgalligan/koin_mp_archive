package org.koin.dsl

import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.error.KoinAppAlreadyStartedException
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger
import org.koin.multiplatform.doInOtherThread
import org.koin.test.assertDefinitionsCount
import org.koin.test.assertHasNoStandaloneInstance
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
    fun `make a Koin application`() {
        val app = doInOtherThread { koinApplication { } }

        app.assertDefinitionsCount(0)

        assertHasNoStandaloneInstance()
    }

    @Test
    fun `start a Koin application`() {
        val app = doInOtherThread {
            startKoin { }
        }

        assertEquals(GlobalContext.get(), app)

        doInOtherThread {
            stopKoin()
        }

        assertHasNoStandaloneInstance()
    }

    @Test
    fun `can't restart a Koin application`() {
        startKoin { }
        try {
            startKoin { }
            fail("should throw  KoinAppAlreadyStartedException")
        } catch (e: KoinAppAlreadyStartedException) {
        }
    }

    @Test
    fun `allow declare a logger`() {
        doInOtherThread{
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
    fun `allow declare a print logger level`() {
        doInOtherThread{
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
