package org.koin.core

import org.koin.core.mp.KoinMultiPlatform
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.parameter.DefinitionParameters.Companion.MAX_PARAMS
import org.koin.core.parameter.parametersOf
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class ParametersHolderTest {

    @Test
    @JsName("create_a_parameters_holder")
fun `create a parameters holder`() {
        val myString = "empty"
        val myInt = 42
        val parameterHolder: DefinitionParameters = parametersOf(myString, myInt)

        assertEquals(2, parameterHolder.size())
        assertTrue(parameterHolder.isNotEmpty())
    }

    @Test
    @JsName("create_an_empty_parameters_holder")
fun `create an empty parameters holder`() {
        val parameterHolder: DefinitionParameters = parametersOf()

        assertEquals(0, parameterHolder.size())
        assertTrue(parameterHolder.isEmpty())
    }

    @Test
    @JsName("get_parameters_from_a_parameter_holder")
fun `get parameters from a parameter holder`() {
        val myString = "empty"
        val myInt = 42
        val parameterHolder: DefinitionParameters = parametersOf(myString, myInt)

        val (s: String, i: Int) = parameterHolder
        assertEquals(myString, s)
        assertEquals(myInt, i)
    }

    @Test
    @JsName("can_t_create_parameters_more_than_max_params")
fun `can't create parameters more than max params`() {
        try {
            parametersOf(1, 2, 3, 4, 5, 6)
            fail("Can't build more than $MAX_PARAMS")
        } catch (e: Exception) {
            KoinMultiPlatform.printStackTrace(e)
        }
    }
}
