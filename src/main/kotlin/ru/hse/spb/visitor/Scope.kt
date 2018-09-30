package ru.hse.spb.visitor

import ru.hse.spb.parser.FunParser
import java.util.HashMap
import java.util.Optional

class Scope(private val variables: MutableMap<String, Optional<Int>> = HashMap(),
            private val functions: MutableMap<String, FunctionBody> = HashMap()) : Cloneable {

    data class FunctionBody(val parameters: List<String>, val body: FunParser.BlockWithBracesContext)

    fun addVariable(id: String, value: Int? = null) {
        if (containsVariable(id)) throw VariableRedeclarationException("Redeclaration of variable $id")
        variables[id] = if (value == null) Optional.empty() else Optional.of(value)
    }

    fun addFunction(id: String, params: List<String>, body: FunParser.BlockWithBracesContext) {
        if (containsFunction(id)) throw FunctionRedeclarationException("Redeclaration of function $id")
        functions[id] = FunctionBody(params, body)
    }

    fun getVariable(id: String): Int {
        if (!containsVariable(id)) throw VariableNotFound("Undeclared variable $id")
        if (!variables[id]!!.isPresent) throw NotDefinedException("Undefined variable $id")
        return variables[id]!!.get()
    }

    fun setVariable(id: String, value: Int) {
        variables[id] = Optional.of(value)
    }

    fun getFunction(id: String): FunctionBody {
        if (!containsFunction(id)) throw FunctionNotFound("Undeclared function $id")
        return functions[id]!!
    }

    private fun containsVariable(id: String) = variables.containsKey(id)

    private fun containsFunction(id: String) = functions.containsKey(id)

    public override fun clone(): Scope {
        val newVariables = mutableMapOf<String, Optional<Int>>()
        variables.forEach { id, value -> newVariables[id] = value }
        val newFunctions = mutableMapOf<String, FunctionBody>()
        functions.forEach { id, body -> newFunctions[id] = body }
        return Scope(newVariables, newFunctions)
    }
}