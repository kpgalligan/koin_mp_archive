package org.koin.example

import co.touchlab.stately.concurrency.AtomicBoolean

class ElectricHeater : Heater {

    private var heating = AtomicBoolean(false)

    override fun on() {
        println("~ ~ ~ heating ~ ~ ~")
        heating.value = true
    }

    override fun off() {
        heating.value = false
    }

    override fun isHot(): Boolean = heating.value
}
