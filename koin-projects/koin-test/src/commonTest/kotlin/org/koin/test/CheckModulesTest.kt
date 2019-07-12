package org.koin.test

import co.touchlab.testhelp.printStackTrace
import org.koin.core.logger.Level
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.check.checkModules
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.fail

class CheckModulesTest {

    @Test
    @JsName("check_a_scoped_module")
fun `check a scoped module`() {
        koinApplication {
            printLogger(Level.DEBUG)
            modules(
                module {
                    scope(named("scope")) {
                        scoped { Simple.ComponentA() }
                        scoped { Simple.ComponentB(get()) }
                    }
                }
            )
        }.checkModules()
    }

    @Test
    @JsName("check_a_scoped_module_and_ext_deps___failed")
fun `check a scoped module and ext deps - failed `() {
        try {
            koinApplication {
                printLogger(Level.DEBUG)
                modules(
                    module {
                        single { Simple.ComponentB(get()) }
                        scope(named("scope")) {
                            scoped { Simple.ComponentA() }
                        }
                    }
                )
            }.checkModules()
            fail()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @Test
    @JsName("check_a_scoped_module_and_ext_scope___failed")
fun `check a scoped module and ext scope - failed`() {
        try {
            koinApplication {
                printLogger(Level.DEBUG)
                modules(
                    module {
                        scope(named("scope2")) {
                            scoped { Simple.ComponentB(get()) }
                        }
                        scope(named("scope1")) {
                            scoped { Simple.ComponentA() }
                        }
                    }
                )
            }.checkModules()
            fail()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @Test
    @JsName("check_a_scoped_module_and_ext_scope___create_scope")
fun `check a scoped module and ext scope - create scope`() {
        koinApplication {
            printLogger(Level.DEBUG)
            modules(
                module {
                    scope(named("scope2")) {
                        scoped {
                            val a = getScope("scopei1").get<Simple.ComponentA>()
                            Simple.ComponentB(a)
                        }
                    }
                    scope(named("scope1")) {
                        scoped { Simple.ComponentA() }
                    }
                }
            )
        }.checkModules {
            koin.createScope("scopei1", named("scope1"))
        }
    }

    @Test
    @JsName("check_a_scoped_module_and_ext_scope___inject_scope")
fun `check a scoped module and ext scope - inject scope`() {
        koinApplication {
            printLogger(Level.DEBUG)
            modules(
                module {
                    scope(named("scope2")) {
                        scoped { (scope1: Scope) -> Simple.ComponentB(scope1.get()) }
                    }
                    scope(named("scope1")) {
                        scoped { Simple.ComponentA() }
                    }
                }
            )
        }.checkModules {
            create<Simple.ComponentB> { parametersOf(koin.createScope("scopei1", named("scope1"))) }
        }
    }

    @Test
    @JsName("check_a_simple_module")
fun `check a simple module`() {
        koinApplication {
            printLogger(Level.DEBUG)
            modules(
                module {
                    single { Simple.ComponentA() }
                }
            )
        }.checkModules()
    }

    @Test
    @JsName("check_a_module_with_link")
fun `check a module with link`() {
        koinApplication {
            printLogger(Level.DEBUG)
            modules(
                module {
                    single { Simple.ComponentA() }
                    single { Simple.ComponentB(get()) }
                }
            )
        }.checkModules()
    }

    @Test
    @JsName("check_a_broken_module")
fun `check a broken module`() {
        try {
            koinApplication {
                printLogger(Level.DEBUG)
                modules(
                    module {
                        single { Simple.ComponentB(get()) }
                    }
                )
            }.checkModules()
            fail("should not pass with borken definitions")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Test
    @JsName("check_a_module_with_params")
fun `check a module with params`() {
        koinApplication {
            printLogger(Level.DEBUG)
            modules(
                module {
                    single { (s: String) -> Simple.MyString(s) }
                    single(UpperCase) { (s: String) -> Simple.MyString(s.toUpperCase()) }
                }
            )
        }.checkModules {
            create<Simple.MyString> { parametersOf("param") }
            create<Simple.MyString>(UpperCase) { qualifier -> parametersOf(qualifier.toString()) }
        }
    }

    @Test
    @JsName("check_with_qualifier")
fun `check with qualifier`() {
        koinApplication {
            printLogger(Level.DEBUG)
            modules(module {
                single(named("test")) { Simple.ComponentA() }
            })
        }.checkModules()
    }

    @Test
    @JsName("check_a_module_with_property")
fun `check a module with property`() {
        koinApplication {
            printLogger(Level.DEBUG)
            properties(hashMapOf("aValue" to "value"))
            modules(
                module {
                    single { Simple.MyString(getProperty("aValue")) }
                }
            )
        }.checkModules()
    }
}
