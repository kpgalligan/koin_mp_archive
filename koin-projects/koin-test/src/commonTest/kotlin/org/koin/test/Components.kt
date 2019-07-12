package org.koin.test

import org.koin.core.qualifier.Qualifier

@Suppress("unused")
class Simple {
    class ComponentA
    class ComponentB(val a: ComponentA)
    class MyString(val s: String)
}

object UpperCase : Qualifier
