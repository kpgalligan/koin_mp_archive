package org.koin.dsl

import org.koin.Simple
import org.koin.core.error.NoBeanDefFoundException
import org.koin.core.logger.Level
import org.koin.core.mp.KoinMultiPlatform
import org.koin.core.qualifier.named
import org.koin.multiplatform.dispatchThread
import org.koin.test.assertDefinitionsCount
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class AdditionalTypeBindingTest {

    @Test
    @JsName("can_resolve_an_additional_type___bind")
fun `can resolve an additional type - bind`() {
        val app = dispatchThread {
            koinApplication {
                printLogger()
                modules(
                    module {
                        single { Simple.Component1() } bind Simple.ComponentInterface1::class
                    })
            }
        }

        dispatchThread {
            app.assertDefinitionsCount(1)

            val koin = app.koin
            val c1 = koin.get<Simple.Component1>()
            val c = koin.bind<Simple.ComponentInterface1, Simple.Component1>()

            assertEquals(c1, c)
        }

    }

    @Test
    @JsName("can_resolve_an_additional_type")
fun `can resolve an additional type`() {
        val app = dispatchThread {
            koinApplication {
                printLogger()
                modules(
                    module {
                        single { Simple.Component1() } bind Simple.ComponentInterface1::class
                    })
            }
        }

        dispatchThread {
            app.assertDefinitionsCount(1)

            val koin = app.koin
            val c1 = koin.get<Simple.Component1>()
            val c = koin.get<Simple.ComponentInterface1>()

            assertEquals(c1, c)
        }

    }

    @Test
    @JsName("can_t_resolve_an_additional_type")
fun `can't resolve an additional type`() {
        val app = dispatchThread{
            koinApplication {
                printLogger(Level.DEBUG)
                modules(
                    module {
                        single { Simple.Component1() } bind Simple.ComponentInterface1::class
                        single { Simple.Component2() } bind Simple.ComponentInterface1::class
                    })
            }
        }

        dispatchThread{
            app.assertDefinitionsCount(2)

            val koin = app.koin
            try {
                koin.get<Simple.ComponentInterface1>()
                fail()
            } catch (e: NoBeanDefFoundException) {
                KoinMultiPlatform.printStackTrace(e)
            }

            assertNotEquals(
                koin.bind<Simple.ComponentInterface1, Simple.Component1>(),
                koin.bind<Simple.ComponentInterface1, Simple.Component2>()
            )
        }
    }

    @Test
    @JsName("can_resolve_an_additional_type_in_DSL")
fun `can resolve an additional type in DSL`() {
        val app = dispatchThread{
            koinApplication {
                printLogger(Level.DEBUG)
                modules(
                    module {
                        single { Simple.Component1() } bind Simple.ComponentInterface1::class
                        single { Simple.Component2() } bind Simple.ComponentInterface1::class
                        single { Simple.UserComponent(bind<Simple.ComponentInterface1, Simple.Component1>()) }
                    })
            }
        }

        dispatchThread{
            app.assertDefinitionsCount(3)

            val koin = app.koin
            assertEquals(koin.get<Simple.UserComponent>().c1, koin.get<Simple.Component1>())
        }
    }

    @Test
    @JsName("additional_type_conflict")
fun `additional type conflict`() {
        val koin = dispatchThread{
            koinApplication {
                printLogger()
                modules(
                    module {
                        single { Simple.Component2() } bind Simple.ComponentInterface1::class
                        single<Simple.ComponentInterface1> { Simple.Component1() }
                    })
            }.koin
        }

        dispatchThread{
            assertTrue(koin.getAll<Simple.ComponentInterface1>().size == 2)

            assertTrue(koin.get<Simple.ComponentInterface1>() is Simple.Component1)
        }
    }

    @Test
    @JsName("should_not_conflict_name___default_type")
fun `should not conflict name & default type`() {
        val app = dispatchThread{
            koinApplication {
                printLogger()
                modules(
                    module {
                        single<Simple.ComponentInterface1>(named("default")) { Simple.Component2() }
                        single<Simple.ComponentInterface1> { Simple.Component1() }
                    })
            }
        }
        dispatchThread{
            val koin = app.koin
            koin.get<Simple.ComponentInterface1>(named("default"))
        }
    }

    @Test
    @JsName("can_resolve_an_additional_types")
fun `can resolve an additional types`() {
        val app = dispatchThread{
            koinApplication {
                modules(
                    module {
                        single { Simple.Component1() } binds arrayOf(
                            Simple.ComponentInterface1::class,
                            Simple.ComponentInterface2::class
                        )
                    })
            }
        }

        dispatchThread{
            app.assertDefinitionsCount(1)

            val koin = app.koin
            val c1 = koin.get<Simple.Component1>()
            val ci1 = koin.bind<Simple.ComponentInterface1, Simple.Component1>()
            val ci2 = koin.bind<Simple.ComponentInterface2, Simple.Component1>()

            assertEquals(c1, ci1)
            assertEquals(c1, ci2)
        }
    }

    @Test
    @JsName("conflicting_with_additional_types")
fun `conflicting with additional types`() {
        val koin = dispatchThread{
            koinApplication {
                modules(
                    module {
                        single<Simple.ComponentInterface1> { Simple.Component2() }
                        single { Simple.Component1() } binds arrayOf(
                            Simple.ComponentInterface1::class,
                            Simple.ComponentInterface2::class
                        )
                    })
            }.koin
        }

        dispatchThread{ assertTrue(koin.getAll<Simple.ComponentInterface1>().size == 2) }
    }
}
