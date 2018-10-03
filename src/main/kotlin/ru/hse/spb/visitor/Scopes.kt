package ru.hse.spb.visitor

import java.util.*

class Scopes {

    private val scopes: Stack<Scope> = Stack()
    private var currentScope: Scope? = null

    private fun makeNewScope(): Scope = if (scopes.empty()) Scope() else Scope(scopes.peek())

    fun createNewScope() {
        currentScope = makeNewScope()
    }

    fun currentScope(): Scope = currentScope ?: scopes.peek()

    fun push() = scopes.push(currentScope ?: makeNewScope()).also { currentScope = null }

    fun pop() = scopes.pop()
}