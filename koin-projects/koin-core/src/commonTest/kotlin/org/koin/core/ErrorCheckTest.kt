package org.koin.core

import org.koin.Errors
import org.koin.Simple
import org.koin.core.error.InstanceCreationException
import org.koin.core.error.NoBeanDefFoundException
import org.koin.core.mp.KoinMultiPlatform
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.fail

class ErrorCheckTest {

    @Test
    @JsName("unknown_definition")
fun `unknown definition`() {
        val app = koinApplication {
        }

        try {
            app.koin.get<Simple.ComponentA>()
            fail("should not get instance")
        } catch (e: NoBeanDefFoundException) {
            KoinMultiPlatform.printStackTrace(e)
        }
    }

    @Test
    @JsName("unknown_linked_dependency")
fun `unknown linked dependency`() {
        val app = koinApplication {
            modules(module {
                single { Simple.ComponentB(get()) }
            })
        }
        try {
            app.koin.get<Simple.ComponentB>()
            fail("should not get instance")
        } catch (e: InstanceCreationException) {
            KoinMultiPlatform.printStackTrace(e)
        }
    }

    @Test
    @JsName("error_while_creating_instance")
fun `error while creating instance`() {
        val app = koinApplication {
            modules(module {
                single { Errors.Boom() }
            })
        }

        try {
            app.koin.get<Errors.Boom>()
            fail("should got InstanceCreationException")
        } catch (e: InstanceCreationException) {
            KoinMultiPlatform.printStackTrace(e)
        }
    }
}
