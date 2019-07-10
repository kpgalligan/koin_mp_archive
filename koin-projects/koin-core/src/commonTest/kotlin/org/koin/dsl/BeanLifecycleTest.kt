package org.koin.dsl

import org.koin.Simple
import org.koin.core.mp.FrozenDelegate
import org.koin.core.qualifier.named
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class BeanLifecycleTest {

    @Test
    @JsName("declare_onClose_for_single")
fun `declare onClose for single`() {
        var result : String by FrozenDelegate("")
        val app = koinApplication {
            printLogger()
            modules(
                module {
                    single { Simple.Component1() } onClose { result = "closing" }
                })
        }

        val koin = app.koin
        koin.close()
        assertEquals("closing", result)
    }

    @Test
    @JsName("declare_onClose_for_factory")
fun `declare onClose for factory`() {
        var result : String by FrozenDelegate("")
        val app = koinApplication {
            printLogger()
            modules(
                module {
                    factory { Simple.Component1() } onClose { result = "closing" }
                })
        }

        val koin = app.koin
        koin.close()
        assertEquals("closing", result)
    }

    @Test
    @JsName("declare_onClose_for_scoped")
fun `declare onClose for scoped`() {
        var result : String by FrozenDelegate("")
        val app = koinApplication {
            printLogger()
            modules(
                module {
                    scope(named("test")) {
                        scoped { Simple.Component1() } onClose { result = "closing" }
                    }
                })
        }

        val koin = app.koin
        koin.createScope("id", named("test"))
        koin.close()
        assertEquals("closing", result)
    }

    @Test
    @JsName("declare_onRelease_for_single")
fun `declare onRelease for single`() {
        var result : String by FrozenDelegate("")
        val app = koinApplication {
            printLogger()
            modules(
                module {
                    single { Simple.Component1() } onRelease { result = "release" }
                })
        }

        val koin = app.koin
        koin.close()
        assertEquals("", result)
    }

    @Test
    @JsName("declare_onRelease_for_factory")
fun `declare onRelease for factory`() {
        var result : String by FrozenDelegate("")
        val app = koinApplication {
            printLogger()
            modules(
                module {
                    factory { Simple.Component1() } onRelease { result = "release" }
                })
        }

        val koin = app.koin
        koin.close()
        assertEquals("", result)
    }

    @Test
    @JsName("declare_onRelease_for_scoped")
fun `declare onRelease for scoped`() {
        var result : String by FrozenDelegate("")
        val app = koinApplication {
            printLogger()
            modules(
                module {
                    scope(named("test")) {
                        scoped { Simple.Component1() } onRelease { result = "release" }
                    }
                })
        }

        val koin = app.koin
        val scopeInstance = koin.createScope("test", named("test"))
        scopeInstance.get<Simple.Component1>()
        scopeInstance.close()
        assertEquals("release", result)
    }
}
