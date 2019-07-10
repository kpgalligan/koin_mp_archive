package org.koin.dsl

import org.koin.core.qualifier.named
import org.koin.multiplatform.dispatchThread
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class ModuleSpecialRulesTest {

    @Test
    @JsName("generic_type_declaration")
fun `generic type declaration`() {
        val koin = dispatchThread {
            koinApplication {
                modules(module {
                    single { arrayListOf<String>() }
                })
            }.koin
        }
        dispatchThread {
            koin.get<ArrayList<String>>()
        }
    }

    @Test
    @JsName("generic_types_declaration")
fun `generic types declaration`() {
        val koin = dispatchThread {
            koinApplication {
                modules(module {
                    single(named("strings")) { testyList<String>() }
                    single(named("ints")) { testyList<Int>() }
                })
            }.koin
        }

        dispatchThread {
            val strings = koin.get<MutableList<String>>(named("strings"))
            strings.add("test")

            assertEquals(1, koin.get<MutableList<String>>(named("strings")).size)

            assertEquals(0, koin.get<MutableList<String>>(named("ints")).size)
        }
    }
}

expect fun <T> testyList(): MutableList<T>
