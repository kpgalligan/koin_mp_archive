package org.koin.core

import org.koin.core.definition.Properties
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AttributesTest {

    @Test
    @JsName("can_store___get_an_attribute_value")
fun `can store & get an attribute value`() {
        val attr = Properties()

        attr.set("myKey", "myString")

        val string = attr.get<String>("myKey")
        assertEquals("myString", string)
    }

    @Test
    @JsName("attribute_empty___no_value")
fun `attribute empty - no value`() {
        val attr = Properties()

        assertTrue(attr.getOrNull<String>("myKey") == null)
    }

    @Test
    @JsName("attribute_value_overwrite")
fun `attribute value overwrite`() {
        val attr = Properties()

        attr.set("myKey", "myString")
        attr.set("myKey", "myString2")

        val string = attr.get<String>("myKey")
        assertEquals("myString2", string)
    }
}
