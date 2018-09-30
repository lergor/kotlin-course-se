package ru.hse.spb.visitor

import java.util.*

class Scopes {

    private val scopes: Stack<Scope> = Stack()
    private var currentScope: Scope? = null

    private fun createNewScope(): Scope = if (scopes.empty()) Scope() else scopes.peek().clone()

    fun newScope() {
        currentScope = createNewScope()
    }

    fun currentScope(): Scope = currentScope ?: scopes.peek()

    fun push() = scopes.push(currentScope ?: createNewScope()).also { currentScope = null }

    fun pop() = scopes.pop()
}