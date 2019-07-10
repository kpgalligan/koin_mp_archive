package org.koin.dsl

import org.koin.Simple
import org.koin.core.error.InstanceCreationException
import org.koin.core.mp.KoinMultiPlatform
import org.koin.multiplatform.dispatchThread
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ModuleAndPropertiesTest {

    @Test
    @JsName("get_a_property_from_a_module")
fun `get a property from a module`() {
        val key = "KEY"
        val value = "VALUE"
        val values = hashMapOf(key to value)

        val koin = dispatchThread{
            koinApplication {
                properties(values)
                modules(module {
                    single { Simple.MyStringFactory(getProperty(key)) }
                })
            }.koin
        }

        dispatchThread{
            val fact = koin.get<Simple.MyStringFactory>()
            assertEquals(value, fact.s)
        }
    }

    @Test
    @JsName("missing_property_from_a_module")
fun `missing property from a module`() {
        try {
            val key = "KEY"

            val koin = dispatchThread{
                koinApplication {
                    modules(module {
                        single { Simple.MyStringFactory(getProperty(key)) }
                    })
                }.koin
            }

            koin.get<Simple.MyStringFactory>()
            fail()
        } catch (e: InstanceCreationException) {
            KoinMultiPlatform.printStackTrace(e)
        }
    }
}
