package org.koin.example


import co.touchlab.stately.concurrency.AtomicInt
import co.touchlab.stately.concurrency.value
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.koin.test.mock.declare
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CoffeeMakerTest : AutoCloseKoinTest() {

    private val coffeeMaker: CoffeeMaker by inject()
    private val heater: HeaterMock by inject()

    @BeforeTest
    fun before() {
        startKoin {
            printLogger(Level.DEBUG)
            modules(coffeeAppModule)

            declare {
                single<Heater> { HeaterMock() } bind HeaterMock::class
            }
        }
    }

    class HeaterMock():Heater{
        val onCount = AtomicInt(0)
        val offCount = AtomicInt(0)
        override fun on() {
            onCount.incrementAndGet()
        }

        override fun off() {
            offCount.incrementAndGet()
        }

        override fun isHot(): Boolean = true
    }

    @Test
    fun testHeaterIsTurnedOnAndThenOff() {
        coffeeMaker.brew()

        assertEquals(heater.onCount.value, 1)
        assertEquals(heater.offCount.value, 1)
    }
}
