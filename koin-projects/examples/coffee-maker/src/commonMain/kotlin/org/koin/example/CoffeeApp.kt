package org.koin.example

import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.inject
import org.koin.core.logger.Level
import org.koin.core.time.measureDurationOnly

class CoffeeApp : KoinComponent {

    val maker: CoffeeMaker by inject()
}

fun main(args: Array<String>) {
    run()
    run()
}

private fun run() {
    startKoin {
        printLogger(Level.DEBUG)
        modules(listOf(coffeeAppModule))
    }
    val coffeeShop = CoffeeApp()
    val appDuration = measureDurationOnly {
        coffeeShop.maker.brew()
    }

    println("executed in $appDuration ms")
    stopKoin()
}
