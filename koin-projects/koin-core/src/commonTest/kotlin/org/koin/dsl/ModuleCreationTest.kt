package org.koin.dsl

import org.koin.Simple
import org.koin.core.logger.Level
import org.koin.multiplatform.doInOtherThread
import org.koin.test.assertDefinitionsCount
import kotlin.test.Test
import kotlin.test.assertEquals

class ModuleCreationTest {

    @Test
    fun `create an empty module`() {
        val app = doInOtherThread{
            koinApplication {
                modules(module {})
            }
        }

        doInOtherThread{ app.assertDefinitionsCount(0) }
    }

    @Test
    fun `load a module once started`() {
        val app = koinApplication {}

        app.assertDefinitionsCount(0)

        doInOtherThread {
            app.modules(module {
                single { Simple.ComponentA() }
            })
        }

        doInOtherThread { app.assertDefinitionsCount(1) }
    }

    @Test
    fun `create a module with single`() {
        val app = doInOtherThread{koinApplication {
            modules(
                module {
                    single { Simple.ComponentA() }
                })
        }}

        doInOtherThread{
        app.assertDefinitionsCount(1)
        }
    }

    @Test
    fun `create a complex single DI module`() {

        val app = doInOtherThread{
        koinApplication {
            modules(
                module {
                    single { Simple.ComponentA() }
                    single { Simple.ComponentB(get()) }
                })
        }
        }

        doInOtherThread{
        app.assertDefinitionsCount(2)
        }
    }

    @Test
    fun `create a complex factory DI module`() {

        val app = doInOtherThread{
        koinApplication {
            modules(
                module {
                    single { Simple.ComponentA() }
                    single { Simple.ComponentB(get()) }
                    factory { Simple.ComponentC(get()) }
                })
        }
        }

        doInOtherThread{
        app.assertDefinitionsCount(3)
        }
    }

    @Test
    fun `create several modules`() {

        val app = doInOtherThread{
        koinApplication {
            modules(
                listOf(
                    module {
                        single { Simple.ComponentA() }
                    },
                    module {
                        single { Simple.ComponentB(get()) }
                    })
            )
        }
        }

        doInOtherThread{
        app.assertDefinitionsCount(2)
        }
    }

    @Test
    fun `create modules list`() {

        val app = doInOtherThread{
        koinApplication {
            modules(
                listOf(
                    module {
                        single { Simple.ComponentA() }
                    },
                    module {
                        single { Simple.ComponentB(get()) }
                    })
            )
        }
        }

        doInOtherThread{
        app.assertDefinitionsCount(2)
        }
    }

    @Test
    fun `create modules list timing`() {

        doInOtherThread{
        koinApplication {
            printLogger(Level.DEBUG)
            modules(
                listOf(
                    module {
                        single { Simple.ComponentA() }
                    },
                    module {
                        single { Simple.ComponentB(get()) }
                    })
            )
        }
        }

        doInOtherThread{
        koinApplication {
            printLogger(Level.DEBUG)
            modules(
                listOf(
                    module {
                        single { Simple.ComponentA() }
                    },
                    module {
                        single { Simple.ComponentB(get()) }
                    })
            )
        }
        }
    }

    @Test
    fun `can add modules for list`() {
        val modA = doInOtherThread{
        module {
            single { Simple.ComponentA() }
        }
        }
        val modB = doInOtherThread{
        module {
            single { Simple.ComponentB(get()) }
        }
        }

        assertEquals(modA + modB, listOf(modA, modB))
    }

    @Test
    fun `can add modules to list`() {
        val modA = module {
            single { Simple.ComponentA() }
        }
        val modB = module {
            single { Simple.ComponentB(get()) }
        }
        val modC = module {
            single { Simple.ComponentC(get()) }
        }

        assertEquals(modA + modB + modC, listOf(modA, modB) + modC)
    }
}
