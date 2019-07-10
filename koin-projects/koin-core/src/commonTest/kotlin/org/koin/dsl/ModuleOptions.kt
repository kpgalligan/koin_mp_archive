package org.koin.dsl

import org.koin.Simple
import org.koin.test.getDefinition
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ModuleOptions {

    @Test
    @JsName("module_default_options")
fun `module default options`() {
        val module = module {
        }

        assertFalse(module.isCreatedAtStart)
        assertFalse(module.override)
    }

    @Test
    @JsName("module_override_option")
fun `module override option`() {
        val module = module(override = true) {
        }

        assertFalse(module.isCreatedAtStart)
        assertTrue(module.override)
    }

    @Test
    @JsName("module_created_options")
fun `module created options`() {
        val module = module(createdAtStart = true) {
        }

        assertTrue(module.isCreatedAtStart)
        assertFalse(module.override)
    }

    @Test
    @JsName("module_definitions_options_inheritance")
fun `module definitions options inheritance`() {

        val module = module(createdAtStart = true, override = true) {
            single { Simple.ComponentA() }
        }

        val app = koinApplication {
            modules(module)
        }

        assertTrue(module.isCreatedAtStart)
        assertTrue(module.override)

        val defA = app.getDefinition(Simple.ComponentA::class) ?: error("no definition found")
        assertTrue(defA.options.isCreatedAtStart)
        assertTrue(defA.options.override)
    }

    @Test
    @JsName("module_definitions_options_non_inheritance")
fun `module definitions options non inheritance`() {

        val module = module {
            single(createdAtStart = true) { Simple.ComponentA() }
            single(override = true) { Simple.ComponentB(get()) }
        }

        val app = koinApplication {
            modules(module)
        }

        assertFalse(module.isCreatedAtStart)
        assertFalse(module.override)

        val defA = app.getDefinition(Simple.ComponentA::class) ?: error("no definition found")
        assertTrue(defA.options.isCreatedAtStart)
        assertFalse(defA.options.override)

        val defB = app.getDefinition(Simple.ComponentB::class) ?: error("no definition found")
        assertFalse(defB.options.isCreatedAtStart)
        assertTrue(defB.options.override)
    }
}
