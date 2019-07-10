package org.koin.core

import org.koin.Simple
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class InstanceResolutionTest {

    @Test
    @JsName("can_resolve_a_single")
fun `can resolve a single`() {

        val app = koinApplication {
            modules(
                module {
                    single { Simple.ComponentA() }
                })
        }

        val koin = app.koin
        val a: Simple.ComponentA = koin.get()
        val a2: Simple.ComponentA = koin.get()

        assertEquals(a, a2)
    }

    @Test
    @JsName("can_resolve_all_ComponentInterface1")
fun `can resolve all ComponentInterface1`() {

        val koin = koinApplication {
            modules(
                module {
                    single { Simple.Component1() } bind Simple.ComponentInterface1::class
                    single { Simple.Component2() } bind Simple.ComponentInterface1::class
                })
        }.koin

        val a1: Simple.Component1 = koin.get()
        val a2: Simple.Component2 = koin.get()

        val instances = koin.getAll<Simple.ComponentInterface1>()

        assertTrue(instances.size == 2 && instances.contains(a1) && instances.contains(a2))
    }

    @Test
    @JsName("cannot_resolve_a_single")
fun `cannot resolve a single`() {

        val app = koinApplication {
            printLogger(Level.DEBUG)
            modules(
                module {
                })
        }

        val koin = app.koin
        val a: Simple.ComponentA? = koin.getOrNull()

        assertTrue(a == null)
    }

    @Test
    @JsName("cannot_inject_a_single")
fun `cannot inject a single`() {

        val app = koinApplication {
            printLogger(Level.DEBUG)
            modules(
                module {
                })
        }

        val koin = app.koin
        val a: Lazy<Simple.ComponentA?> = koin.injectOrNull()

        assertTrue(a.value == null)
    }

    @Test
    @JsName("can_lazy_resolve_a_single")
fun `can lazy resolve a single`() {

        val app = koinApplication {
            modules(
                module {
                    single { Simple.ComponentA() }
                })
        }

        val koin = app.koin
        val a: Simple.ComponentA by koin.inject()
        val a2: Simple.ComponentA = koin.get()

        assertEquals(a, a2)
    }

    @Test
    @JsName("can_resolve_a_singles_by_name")
fun `can resolve a singles by name`() {

        val app = koinApplication {
            modules(
                module {
                    val componentA = Simple.ComponentA()
                    single(named("A")) { componentA }
                    single(named("B")) { componentA }
                })
        }

        val koin = app.koin
        val a: Simple.ComponentA = koin.get(qualifier = named("A"))
        val b: Simple.ComponentA = koin.get(qualifier = named("B"))

        assertEquals(a, b)
    }

    @Test
    @JsName("can_resolve_a_factory_by_name")
fun `can resolve a factory by name`() {

        val app = koinApplication {
            modules(
                module {
                    val componentA = Simple.ComponentA()
                    factory(named("A")) { componentA }
                    factory(named("B")) { componentA }
                })
        }

        val koin = app.koin
        val a: Simple.ComponentA = koin.get(qualifier = named("A"))
        val b: Simple.ComponentA = koin.get(qualifier = named("B"))

        assertEquals(a, b)
    }

    @Test
    @JsName("can_resolve_a_factory")
fun `can resolve a factory`() {

        val app = koinApplication {
            modules(
                module {
                    factory { Simple.ComponentA() }
                })
        }

        val koin = app.koin
        val a: Simple.ComponentA = koin.get()
        val a2: Simple.ComponentA = koin.get()

        assertNotEquals(a, a2)
    }

    @Test
    @JsName("should_resolve_default")
fun `should resolve default`() {

        val app = koinApplication {
            modules(
                module {
                    single<Simple.ComponentInterface1>(named("2")) { Simple.Component2() }
                    single<Simple.ComponentInterface1> { Simple.Component1() }
                })
        }

        val koin = app.koin
        val component: Simple.ComponentInterface1 = koin.get()

        assertTrue(component is Simple.Component1)
        assertTrue(koin.get<Simple.ComponentInterface1>(named("2")) is Simple.Component2)
    }
}
