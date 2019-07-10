package org.koin.dsl

import org.koin.Simple
import org.koin.multiplatform.dispatchThread
import org.koin.test.getDefinition
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BeanOptionsTest {

    @Test
    @JsName("definition_created_at_start")
fun `definition created at start`() {
        val app = dispatchThread{
            koinApplication {
                modules(
                    module {
                        single(createdAtStart = true) { Simple.ComponentA() }
                        single { Simple.ComponentB(get()) }
                    }
                )
            }
        }

        dispatchThread{
            val defA = app.getDefinition(Simple.ComponentA::class) ?: error("no definition found")
            assertTrue(defA.options.isCreatedAtStart)

            val defB = app.getDefinition(Simple.ComponentB::class) ?: error("no definition found")
            assertFalse(defB.options.isCreatedAtStart)
        }
    }

    @Test
    @JsName("definition_override")
fun `definition override`() {
        val app = dispatchThread{
            koinApplication {
                modules(
                    module {
                        single { Simple.ComponentA() }
                        single(override = true) { Simple.ComponentB(get()) }
                    }
                )
            }
        }

        dispatchThread{
            val defA = app.getDefinition(Simple.ComponentA::class) ?: error("no definition found")
            assertFalse(defA.options.override)

            val defB = app.getDefinition(Simple.ComponentB::class) ?: error("no definition found")
            assertTrue(defB.options.override)
        }
    }
}
