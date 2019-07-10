package org.koin.dsl

import org.koin.Simple
import org.koin.core.definition.DefinitionFactory
import org.koin.core.definition.Kind
import org.koin.core.instance.InstanceContext
import org.koin.core.parameter.emptyParametersHolder
import org.koin.core.qualifier.named
import org.koin.multiplatform.dispatchThread
import org.koin.test.getDefinition
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BeanDefinitionTest {

    val koin = koinApplication { }.koin

    @Test
    @JsName("equals_definitions")
fun `equals definitions`() {

        val def1 = DefinitionFactory.createSingle(definition = { Simple.ComponentA() })
        val def2 = DefinitionFactory.createSingle(definition = { Simple.ComponentA() })
        assertEquals(def1, def2)
    }

    @Test
    @JsName("scope_definition")
fun `scope definition`() {
        val scopeID = named("scope")

        val def1 = DefinitionFactory.createSingle(scopeName = scopeID, definition = { Simple.ComponentA() })

        assertEquals(scopeID, def1.scopeName)
        assertEquals(Kind.Single, def1.kind)
        assertEquals(scopeID, def1.scopeName)
    }

    @Test
    @JsName("equals_definitions___but_diff_kind")
fun `equals definitions - but diff kind`() {
        val def1 = DefinitionFactory.createSingle(definition = { Simple.ComponentA() })
        val def2 = DefinitionFactory.createFactory(definition = { Simple.ComponentA() })
        assertEquals(def1, def2)
    }

    @Test
    @JsName("definition_kind")
fun `definition kind`() {
        val app = dispatchThread{
            koinApplication {
                modules(
                    module {
                        single { Simple.ComponentA() }
                        factory { Simple.ComponentB(get()) }
                    }
                )
            }
        }

        dispatchThread{
            val defA = app.getDefinition(Simple.ComponentA::class) ?: error("no definition found")
            assertEquals(Kind.Single, defA.kind)

            val defB = app.getDefinition(Simple.ComponentB::class) ?: error("no definition found")
            assertEquals(Kind.Factory, defB.kind)
        }
    }

    @Test
    @JsName("definition_name")
fun `definition name`() {
        val name = named("A")
        val app = dispatchThread{
            koinApplication {
                modules(
                    module {
                        single(name) { Simple.ComponentA() }
                        factory { Simple.ComponentB(get()) }
                    }
                )
            }
        }

        dispatchThread{
            val defA = app.getDefinition(Simple.ComponentA::class) ?: error("no definition found")
            assertEquals(name, defA.qualifier)

            val defB = app.getDefinition(Simple.ComponentB::class) ?: error("no definition found")
            assertTrue(defB.qualifier == null)
        }
    }

    @Test
    @JsName("definition_function")
fun `definition function`() {
        val app = dispatchThread{
            koinApplication {
                modules(
                    module {
                        single { Simple.ComponentA() }
                    }
                )
            }
        }

        dispatchThread{
            val defA = app.getDefinition(Simple.ComponentA::class) ?: error("no definition found")
            val instance = defA.instance!!.get<Simple.ComponentA>(
                InstanceContext(
                    koin = app.koin,
                    _parameters = { emptyParametersHolder() })
            )
            assertEquals(instance, app.koin.get<Simple.ComponentA>())
        }
    }
}
