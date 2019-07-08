package org.koin.dsl

import org.koin.core.logger.Level
import org.koin.core.mp.KoinMultiPlatform
import org.koin.multiplatform.doInOtherThread
import kotlin.test.Test
import kotlin.test.assertEquals

class EnvironmentPropertyDefinitionTest {

    @Test
    fun `load and get properties from environment`() {
        val sysProperties = KoinMultiPlatform.getSystemEnvironmentProperties()
        val aPropertyKey: String = sysProperties.keys.first()
        val aPropertyValue = sysProperties[aPropertyKey]

        val koin = doInOtherThread {
            koinApplication {
                printLogger(Level.DEBUG)
                environmentProperties()
            }.koin
        }

        doInOtherThread {
            val foundValue = koin.getProperty<String>(aPropertyKey)
            assertEquals(aPropertyValue, foundValue)
        }
    }
}
