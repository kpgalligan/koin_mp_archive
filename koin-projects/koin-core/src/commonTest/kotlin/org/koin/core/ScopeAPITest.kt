package org.koin.core

import org.koin.core.error.NoScopeDefinitionFoundException
import org.koin.core.error.ScopeAlreadyCreatedException
import org.koin.core.mp.FrozenDelegate
import org.koin.core.mp.KoinMultiPlatform
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeCallback
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.js.JsName
import kotlin.test.*

class ScopeAPITest {

    val scopeKey = named("KEY")
    val koin = koinApplication {
        modules(
            module {
                scope(scopeKey) {
                }
            }
        )
    }.koin

    @Test
    @JsName("create_a_scope_instance")
fun `create a scope instance`() {
        val scopeId = "myScope"
        val scope1 = koin.createScope(scopeId, scopeKey)
        val scope2 = koin.getScope(scopeId)

        assertEquals(scope1, scope2)
    }

    @Test
    @JsName("can_t_find_a_non_created_scope_instance")
fun `can't find a non created scope instance`() {
        val scopeId = "myScope"
        try {
            koin.getScope(scopeId)
            fail()
        } catch (e: Exception) {
            KoinMultiPlatform.printStackTrace(e)
        }
    }

    @Test
    @JsName("create_different_scopes")
fun `create different scopes`() {
        val scope1 = koin.createScope("myScope1", scopeKey)
        val scope2 = koin.createScope("myScope2", scopeKey)

        assertNotEquals(scope1, scope2)
    }

    @Test
    @JsName("can_t_create_scope_instance_with_unknown_scope_def")
fun `can't create scope instance with unknown scope def`() {

        try {
            koin.createScope("myScope", named("a_scope"))
            fail()
        } catch (e: NoScopeDefinitionFoundException) {
            KoinMultiPlatform.printStackTrace(e)
        }
    }

    @Test
    @JsName("create_scope_instance_with_scope_def")
fun `create scope instance with scope def`() {

        assertNotNull(koin.createScope("myScope", scopeKey))
    }

    @Test
    @JsName("can_t_create_a_new_scope_if_not_closed")
fun `can't create a new scope if not closed`() {
        koin.createScope("myScope1", scopeKey)
        try {
            koin.createScope("myScope1", scopeKey)
            fail()
        } catch (e: ScopeAlreadyCreatedException) {
            KoinMultiPlatform.printStackTrace(e)
        }
    }

    @Test
    @JsName("can_t_get_a_closed_scope")
fun `can't get a closed scope`() {

        val scope = koin.createScope("myScope1", scopeKey)
        scope.close()
        try {
            koin.getScope("myScope1")
            fail()
        } catch (e: Exception) {
            KoinMultiPlatform.printStackTrace(e)
        }
    }

    @Test
    @JsName("find_a_scope_by_id")
fun `find a scope by id`() {
        val scopeId = "myScope"
        val scope1 = koin.createScope(scopeId, scopeKey)
        assertEquals(scope1, koin.getScope(scope1.id))
    }

    @Test
    @JsName("scope_callback")
fun `scope callback`() {
        val scopeId = "myScope"
        val scope1 = koin.createScope(scopeId, scopeKey)
        var closed : Boolean by FrozenDelegate(false)
        scope1.registerCallback(object : ScopeCallback {
            override fun onScopeClose(scope: Scope) {
                closed = true
            }
        })
        scope1.close()
        assertTrue(closed)
    }
}
