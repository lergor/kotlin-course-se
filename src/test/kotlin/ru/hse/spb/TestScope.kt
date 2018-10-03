package ru.hse.spb

import org.junit.Test
import org.junit.Assert.assertNotNull
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertFalse
import ru.hse.spb.parser.FunParser
import ru.hse.spb.visitor.*

class TestScope {

    private fun getScope(): Scope {
        val parentScope = Scope()
        parentScope.addVariable("not_def")
        parentScope.addVariable("def", 17)
        parentScope.addFunction("func", listOf(), FunParser.BlockWithBracesContext(null, 1))
        return Scope(parentScope)
    }

    @Test
    fun containsLocalVariable() {
        val scope = getScope()
        val name = "kek"
        scope.addVariable(name, 12)
        assertThat(scope.containsVariable(name)).isTrue()
        assertThat(scope.getVariable(name)).isEqualTo(12)
    }

    @Test
    fun containsGlobalVariable() {
        val scope = getScope()
        val name = "def"
        assertThat(scope.containsVariable(name)).isTrue()
        assertThat(scope.getVariable(name)).isEqualTo(17)
    }

    @Test
    fun localVariableOverridesGlobal() {
        val scope = getScope()
        val name = "def"
        assertThat(scope.containsVariable(name)).isTrue()
        scope.addVariable(name, 100500)
        assertThat(scope.containsVariable(name)).isTrue()
        assertThat(scope.getVariable(name)).isEqualTo(100500)
    }

    @Test(expected = NotDefinedException::class)
    fun undefinedVariable() {
        val scope = getScope()
        val name = "not_def"
        assertThat(scope.containsVariable(name)).isTrue()
        assertThat(scope.getVariable(name))
    }

    @Test(expected = VariableNotFound::class)
    fun undeclaredVariable() {
        val scope = getScope()
        val name = "lol"
        assertThat(scope.containsVariable(name)).isFalse()
        assertThat(scope.getVariable(name))
    }

    @Test(expected = VariableRedeclarationException::class)
    fun variableRedeclaration() {
        val scope = getScope()
        val name = "zzz"
        assertFalse(scope.containsVariable(name))
        scope.addVariable(name)
        scope.addVariable(name)
    }

    @Test
    fun containsLocalFunction() {
        val scope = getScope()
        scope.addFunction("foo", listOf(), FunParser.BlockWithBracesContext(null, 1))
        assertNotNull(scope.getFunction("foo"))
    }

    @Test
    fun containsGlobalFunction() {
        val scope = getScope()
        val name = "func"
        assertThat(scope.containsFunction(name)).isTrue()
        assertNotNull(scope.getFunction(name))
    }

    @Test(expected = FunctionNotFound::class)
    fun undeclaredFunction() {
        val scope = getScope()
        assertThat(scope.getFunction("zoo"))
    }

    @Test(expected = FunctionRedeclarationException::class)
    fun functionRedeclaration() {
        val scope = getScope()
        val name = "bar"
        assertFalse(scope.containsFunction(name))
        scope.addFunction(name, listOf(), FunParser.BlockWithBracesContext(null, 1))
        scope.addFunction(name, listOf(), FunParser.BlockWithBracesContext(null, 1))
    }

}