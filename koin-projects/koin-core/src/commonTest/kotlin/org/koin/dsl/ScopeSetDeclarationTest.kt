package org.koin.dsl

import org.koin.Simple
import org.koin.core.error.DefinitionOverrideException
import org.koin.core.instance.ScopeDefinitionInstance
import org.koin.core.logger.Level
import org.koin.core.mp.KoinMultiPlatform
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class ScopeSetDeclarationTest {

    val scopeKey = named("KEY")

    @Test
    @JsName("can_declare_a_scoped_definition")
fun `can declare a scoped definition`() {
        val koin = koinApplication {
            modules(
                module {
                    scope(scopeKey) {
                        scoped { Simple.ComponentA() }
                    }
                }
            )
        }.koin
        val def = koin.scopeRegistry.getScopeDefinition(scopeKey.toString())
            ?.definitions?.first { it.primaryType == Simple.ComponentA::class }
        assertTrue(def!!.scopeName == scopeKey)

        val scope = koin.createScope("id", scopeKey)
        assertTrue(scope.beanRegistry.findDefinition(clazz = Simple.ComponentA::class)!!.instance is ScopeDefinitionInstance<*>)
    }

    @Test
    @JsName("can_declare_2_scoped_definitions_from_same_type_without_naming")
fun `can declare 2 scoped definitions from same type without naming`() {
        val koin = koinApplication {
            modules(
                module {
                    scope(named("B")) {
                        scoped { Simple.ComponentA() }
                    }
                    scope(named("A")) {
                        scoped { Simple.ComponentA() }
                    }
                }
            )
        }.koin
        val defA = koin.scopeRegistry.getScopeDefinition("A")
            ?.definitions?.first { it.primaryType == Simple.ComponentA::class && it.scopeName == named("A") }
        assertTrue(defA!!.scopeName == StringQualifier("A"))

        val defB = koin.scopeRegistry.getScopeDefinition("B")
            ?.definitions?.first { it.primaryType == Simple.ComponentA::class && it.scopeName == named("B") }
        assertTrue(defB!!.scopeName == StringQualifier("B"))

        val scopeA = koin.createScope("A", named("A")).get<Simple.ComponentA>()
        val scopeB = koin.createScope("B", named("B")).get<Simple.ComponentA>()
        assertNotEquals(scopeA, scopeB)
    }

//    @Test
//    @JsName("can_t_declare_other_than_scoped_in_scope")
//fun `can't declare other than scoped in scope`() {
//        val app = koinApplication {
//            modules(
//                    module {
//                        scope(named("")) {
//                            scoped { }
//                            factory { }
//                        }
//                    }
//            )
//        }
//        val def = app.koin.beanRegistry.findDefinition(clazz = Simple.ComponentA::class)
//        assertTrue(def!!.instance is ScopeDefinitionInstance)
//        assertTrue(def.getScopeName() == scopeKey)
//    }

    @Test
    @JsName("can_declare_a_scope_definition")
fun `can declare a scope definition`() {
        val app = koinApplication {
            modules(
                module {
                    scope(scopeKey) {
                    }
                }
            )
        }
        val def = app.koin.scopeRegistry.getScopeDefinition(scopeKey.toString())!!
        assertTrue(def.qualifier == scopeKey)
    }

    @Test
    @JsName("can_t_declare_2_scoped_same_definitions")
fun `can't declare 2 scoped same definitions`() {
        try {
            koinApplication {
                printLogger(Level.DEBUG)
                modules(
                    module {
                        scope(scopeKey) {
                            scoped { Simple.ComponentA() }
                            scoped { Simple.ComponentA() }
                        }
                    }
                )
            }
            fail()
        } catch (e: DefinitionOverrideException) {
            KoinMultiPlatform.printStackTrace(e)
        }
    }
}
