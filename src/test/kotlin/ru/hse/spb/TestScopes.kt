package ru.hse.spb

import junit.framework.Assert.*
import org.junit.Test
import ru.hse.spb.parser.FunParser
import ru.hse.spb.visitor.*

class TestScopes {

    private fun scopes(): Scopes = Scopes()

    private fun makeParentScope(scope: Scope) {
        scope.addVariable("not_def")
        scope.addVariable("def", 17)
        scope.addFunction("func", listOf(), FunParser.BlockWithBracesContext(null, 1))
    }

    @Test
    fun createNewScopeWithEmptyStack() {
        val scopes = scopes()
        scopes.createNewScope()
        assertNotNull(scopes.currentScope())
    }

    @Test
    fun createNewScopeWithNotEmptyStack() {
        val scopes = scopes()
        scopes.createNewScope()
        val scope = scopes.currentScope()
        makeParentScope(scope)
        assertTrue(scope.containsVariable("def"))
        scopes.push()
        scopes.createNewScope()
        val newScope = scopes.currentScope()
        assertTrue(newScope.containsVariable("def"))
        assertTrue(newScope.containsVariable("not_def"))
        assertTrue(newScope.containsFunction("func"))
    }

    @Test
    fun createNewScopeAndPop() {
        val scopes = scopes()
        scopes.createNewScope()
        val scope = scopes.currentScope()
        makeParentScope(scope)
        scopes.push()
        scopes.createNewScope()
        val newScope = scopes.currentScope()
        val name = "azino"
        newScope.addVariable(name, 777)
        scopes.push()
        scopes.pop()
        val currentScope = scopes.currentScope()
        assertFalse(currentScope.containsVariable(name))
    }

    @Test
    fun createNewScopeAndNoPush() {
        val scopes = scopes()
        scopes.createNewScope()
        val scope = scopes.currentScope()
        makeParentScope(scope)
        scopes.push()
        scopes.createNewScope()
        val newScope = scopes.currentScope()
        val name = "azino"
        newScope.addVariable(name, 777)
        scopes.createNewScope()
        val currentScope = scopes.currentScope()
        assertFalse(currentScope.containsVariable(name))
    }



}