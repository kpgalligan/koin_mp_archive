package org.koin.dsl

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PropertyDefinitionTest {

    @Test
    @JsName("load_and_get_properties")
fun `load and get properties`() {
        val key = "KEY"
        val value = "VALUE"
        val values = hashMapOf(key to value)
        val koin = koinApplication {
            properties(values)
        }.koin

        val gotValue = koin.getProperty<String>(key)

        assertEquals(value, gotValue)
    }

    @Test
    @JsName("default_value_properties")
fun `default value properties`() {
        val koin = koinApplication {}.koin

        val defaultValue = "defaultValue"
        val gotValue = koin.getProperty<String>("aKey", defaultValue)

        assertEquals(defaultValue, gotValue)
    }

    @Test
    @JsName("set_a_property")
fun `set a property`() {
        val key = "KEY"
        val value = "VALUE"

        val koin = koinApplication { }.koin

        koin.setProperty(key, value)
        val gotValue = koin.getProperty<String>(key)
        assertEquals(value, gotValue)
    }

    @Test
    @JsName("missing_property")
fun `missing property`() {
        val key = "KEY"
        val koin = koinApplication { }.koin

        val gotValue = koin.getProperty<String>(key)
        assertNull(gotValue)
    }

    @Test
    @JsName("overwrite_a_property")
fun `overwrite a property`() {
        val key = "KEY"
        val value = "VALUE"
        val value2 = "VALUE2"
        val values = hashMapOf(key to value)
        val koin = koinApplication {
            properties(values)
        }.koin

        koin.setProperty(key, value2)
        val gotValue = koin.getProperty<String>(key)
        assertEquals(value2, gotValue)
    }
}
