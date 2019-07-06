package org.koin.core.scope

import org.koin.core.definition.BeanDefinition
import org.koin.core.instance.InstanceContext
import org.koin.core.mp.KoinMultiPlatform
import org.koin.core.qualifier.Qualifier

/**
 * Imternal Scope Definition
 */
data class ScopeDefinition(val qualifier: Qualifier) {

    val definitions: MutableSet<BeanDefinition<*>> = KoinMultiPlatform.emptyMutableSet()

    internal fun release(instance: Scope) {
        definitions
            .forEach { it.instance?.release(InstanceContext(scope = instance)) }
    }
}
