package org.koin.androidx.experimental.dsl

import androidx.lifecycle.ViewModel
import org.koin.androidx.viewmodel.dsl.setIsViewModel
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.DefinitionFactory
import org.koin.core.definition.Options
import org.koin.core.error.DefinitionOverrideException
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.ScopeSet
import org.koin.experimental.builder.create

/**
 * ViewModel DSL Extension
 * Allow to declare a ViewModel - be later inject into Activity/Fragment with dedicated injector
 *
 * @author Arnaud Giuliani
 *
 * @param qualifier - definition qualifier
 * @param override - allow definition override
 */
inline fun <reified T : ViewModel> ScopeSet.viewModel(
        name: Qualifier? = null,
        override: Boolean = false
): BeanDefinition<T> {
    val beanDefinition = DefinitionFactory.createFactory(name, qualifier) { create<T>() }
    declareDefinition(beanDefinition, Options(false, override))
    beanDefinition.setIsViewModel()
    if (!definitions.contains(beanDefinition)) {
        definitions.add(beanDefinition)
    } else {
        throw DefinitionOverrideException("Can't add definition $beanDefinition for scope ${this.qualifier} as it already exists")
    }
    return beanDefinition
}
