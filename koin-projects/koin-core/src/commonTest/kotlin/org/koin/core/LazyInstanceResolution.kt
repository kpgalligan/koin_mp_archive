package org.koin.core

import org.koin.Simple
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class LazyInstanceResolution {
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
        val a: Simple.ComponentA = koin.get()
        val a2: Simple.ComponentA = koin.get()

        assertEquals(a, a2)
    }
}
