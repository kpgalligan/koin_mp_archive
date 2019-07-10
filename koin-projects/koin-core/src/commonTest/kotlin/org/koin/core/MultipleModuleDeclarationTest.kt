package org.koin.core

import org.koin.Simple
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.assertDefinitionsCount
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class MultipleModuleDeclarationTest {

    @Test
    @JsName("run_with_DI_with_several_modules")
fun `run with DI with several modules`() {

        val app = koinApplication {
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

        app.assertDefinitionsCount(2)
    }

    @Test
    @JsName("resolve_DI_with_several_modules")
fun `resolve DI with several modules`() {

        val app = koinApplication {
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

        val koin = app.koin
        val a = koin.get<Simple.ComponentA>()
        val b = koin.get<Simple.ComponentB>()

        assertEquals(a, b.a)
    }
}
