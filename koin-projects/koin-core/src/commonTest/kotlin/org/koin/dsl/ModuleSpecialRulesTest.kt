package org.koin.dsl

import co.touchlab.stately.collections.frozenLinkedList
import org.koin.core.qualifier.named
import org.koin.multiplatform.doInOtherThread
import kotlin.test.Test
import kotlin.test.assertEquals

class ModuleSpecialRulesTest {

    @Test
    fun `generic type declaration`() {
        val koin = doInOtherThread {
            koinApplication {
                modules(module {
                    single { arrayListOf<String>() }
                })
            }.koin
        }
        doInOtherThread {
            koin.get<ArrayList<String>>()
        }
    }

    @Test
    fun `generic types declaration`() {
        val koin = doInOtherThread {
            koinApplication {
                modules(module {
                    single(named("strings")) { frozenLinkedList<String>() }
                    single(named("ints")) { frozenLinkedList<Int>() }
                })
            }.koin
        }

        doInOtherThread {
            val strings = koin.get<MutableList<String>>(named("strings"))
            strings.add("test")

            assertEquals(1, koin.get<MutableList<String>>(named("strings")).size)

            assertEquals(0, koin.get<MutableList<String>>(named("ints")).size)
        }
    }
}
