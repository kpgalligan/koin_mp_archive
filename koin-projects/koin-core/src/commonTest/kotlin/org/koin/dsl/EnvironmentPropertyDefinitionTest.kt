package org.koin.dsl

import org.koin.core.logger.Level
import org.koin.core.mp.KoinMultiPlatform
import org.koin.core.mp.getSystemEnvironmentProperties
import org.koin.multiplatform.dispatchThread
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class EnvironmentPropertyDefinitionTest {

    @Test
    @JsName("load_and_get_properties_from_environment")
fun `load and get properties from environment`() {
        val sysProperties = getSystemEnvironmentProperties()
        val keys = sysProperties.keys
        if(keys.isEmpty())
            return
        val aPropertyKey: String = keys.first()
        val aPropertyValue = sysProperties[aPropertyKey]

        val koin = dispatchThread {
            koinApplication {
                printLogger(Level.DEBUG)
                environmentProperties()
            }.koin
        }

        dispatchThread {
            val foundValue = koin.getProperty<String>(aPropertyKey)
            assertEquals(aPropertyValue, foundValue)
        }
    }
}
