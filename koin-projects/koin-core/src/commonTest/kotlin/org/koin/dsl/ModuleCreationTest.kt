package org.koin.dsl

import org.koin.Simple
import org.koin.core.logger.Level
import org.koin.multiplatform.dispatchThread
import org.koin.test.assertDefinitionsCount
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class ModuleCreationTest {

    @Test
    @JsName("create_an_empty_module")
fun `create an empty module`() {
        val app = dispatchThread{
            koinApplication {
                modules(module {})
            }
        }

        dispatchThread{ app.assertDefinitionsCount(0) }
    }

    @Test
    @JsName("load_a_module_once_started")
fun `load a module once started`() {
        val app = koinApplication {}

        app.assertDefinitionsCount(0)

        dispatchThread {
            app.modules(module {
                single { Simple.ComponentA() }
            })
        }

        dispatchThread { app.assertDefinitionsCount(1) }
    }

    @Test
    @JsName("create_a_module_with_single")
fun `create a module with single`() {
        val app = dispatchThread{koinApplication {
            modules(
                module {
                    single { Simple.ComponentA() }
                })
        }}

        dispatchThread{
        app.assertDefinitionsCount(1)
        }
    }

    @Test
    @JsName("create_a_complex_single_DI_module")
fun `create a complex single DI module`() {

        val app = dispatchThread{
        koinApplication {
            modules(
                module {
                    single { Simple.ComponentA() }
                    single { Simple.ComponentB(get()) }
                })
        }
        }

        dispatchThread{
        app.assertDefinitionsCount(2)
        }
    }

    @Test
    @JsName("create_a_complex_factory_DI_module")
fun `create a complex factory DI module`() {

        val app = dispatchThread{
        koinApplication {
            modules(
                module {
                    single { Simple.ComponentA() }
                    single { Simple.ComponentB(get()) }
                    factory { Simple.ComponentC(get()) }
                })
        }
        }

        dispatchThread{
        app.assertDefinitionsCount(3)
        }
    }

    @Test
    @JsName("create_several_modules")
fun `create several modules`() {

        val app = dispatchThread{
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

        dispatchThread{
        app.assertDefinitionsCount(2)
        }
    }

    @Test
    @JsName("create_modules_list")
fun `create modules list`() {

        val app = dispatchThread{
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

        dispatchThread{
        app.assertDefinitionsCount(2)
        }
    }

    @Test
    @JsName("create_modules_list_timing")
fun `create modules list timing`() {

        dispatchThread{
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

        dispatchThread{
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
    @JsName("can_add_modules_for_list")
fun `can add modules for list`() {
        val modA = dispatchThread{
        module {
            single { Simple.ComponentA() }
        }
        }
        val modB = dispatchThread{
        module {
            single { Simple.ComponentB(get()) }
        }
        }

        assertEquals(modA + modB, listOf(modA, modB))
    }

    @Test
    @JsName("can_add_modules_to_list")
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
