package org.koin.dsl

import org.koin.Simple
import org.koin.multiplatform.doInOtherThread
import org.koin.test.getDefinition
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BeanOptionsTest {

    @Test
    fun `definition created at start`() {
        val app = doInOtherThread{
            koinApplication {
                modules(
                    module {
                        single(createdAtStart = true) { Simple.ComponentA() }
                        single { Simple.ComponentB(get()) }
                    }
                )
            }
        }

        doInOtherThread{
            val defA = app.getDefinition(Simple.ComponentA::class) ?: error("no definition found")
            assertTrue(defA.options.isCreatedAtStart)

            val defB = app.getDefinition(Simple.ComponentB::class) ?: error("no definition found")
            assertFalse(defB.options.isCreatedAtStart)
        }
    }

    @Test
    fun `definition override`() {
        val app = doInOtherThread{
            koinApplication {
                modules(
                    module {
                        single { Simple.ComponentA() }
                        single(override = true) { Simple.ComponentB(get()) }
                    }
                )
            }
        }

        doInOtherThread{
            val defA = app.getDefinition(Simple.ComponentA::class) ?: error("no definition found")
            assertFalse(defA.options.override)

            val defB = app.getDefinition(Simple.ComponentB::class) ?: error("no definition found")
            assertTrue(defB.options.override)
        }
    }
}
