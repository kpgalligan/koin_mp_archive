package org.koin.dsl

import org.koin.Simple
import org.koin.core.error.DefinitionOverrideException
import org.koin.core.logger.Level
import org.koin.core.mp.KoinMultiPlatform
import org.koin.core.qualifier.named
import org.koin.test.assertDefinitionsCount
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.fail

class ModuleDeclarationRulesTest {

    @Test
    @JsName("don_t_allow_redeclaration")
fun `don't allow redeclaration`() {
        try {
            koinApplication {
                modules(module {
                    single { Simple.ComponentA() }
                    single { Simple.ComponentA() }
                })
            }
            fail("should not redeclare")
        } catch (e: DefinitionOverrideException) {
            KoinMultiPlatform.printStackTrace(e)
        }
    }

    @Test
    @JsName("allow_redeclaration___different_names")
fun `allow redeclaration - different names`() {
        val app = koinApplication {
            printLogger(Level.INFO)
            modules(module {
                single(named("default")) { Simple.ComponentA() }
                single(named("other")) { Simple.ComponentA() }
            })
        }
        app.assertDefinitionsCount(2)
    }

    @Test
    @JsName("reject_redeclaration___same_names")
fun `reject redeclaration - same names`() {
        try {
            koinApplication {
                modules(module {
                    single(named("default")) { Simple.ComponentA() }
                    single(named("default")) { Simple.ComponentB(get()) }
                })
            }
            fail("Should not allow redeclaration for same qualifier")
        } catch (e: DefinitionOverrideException) {
            KoinMultiPlatform.printStackTrace(e)
        }
    }

    @Test
    @JsName("allow_redeclaration___default")
fun `allow redeclaration - default`() {
        val app = koinApplication {
            modules(module {
                single { Simple.ComponentA() }
                single(named("other")) { Simple.ComponentA() }
            })
        }
        app.assertDefinitionsCount(2)
    }

    @Test
    @JsName("don_t_allow_redeclaration_with_different_implementation")
fun `don't allow redeclaration with different implementation`() {

        try {
            koinApplication {
                modules(
                    module {
                        single<Simple.ComponentInterface1> { Simple.Component1() }
                        single<Simple.ComponentInterface1> { Simple.Component2() }
                    })
            }
        } catch (e: DefinitionOverrideException) {
            KoinMultiPlatform.printStackTrace(e)
        }
    }
}
