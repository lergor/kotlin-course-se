package ru.hse.spb.visitor

import ru.hse.spb.parser.FunParser
import java.util.HashMap

class Scope(private val parent: Scope? = null) {

    private val variables: MutableMap<String, VariableValue> = HashMap()
    private val functions: MutableMap<String, FunctionBody> = HashMap()

    data class FunctionBody(val parameters: List<String>, val body: FunParser.BlockWithBracesContext)
    data class VariableValue(val value: Int?)

    fun addVariable(name: String, value: Int? = null) {
        if (name in variables) throw VariableRedeclarationException("Redeclaration of variable $name")
        variables[name] = VariableValue(value)
    }

    fun addFunction(name: String, params: List<String>, body: FunParser.BlockWithBracesContext) {
        if (name in functions) throw FunctionRedeclarationException("Redeclaration of function $name")
        functions[name] = FunctionBody(params, body)
    }

    fun getVariable(name: String): Int {
        return when {
            name in variables -> variables[name]?.value ?: throw NotDefinedException("Undefined variable $name")
            parent != null -> parent.getVariable(name)
            else -> throw VariableNotFound("Undeclared variable $name")
        }
    }

    fun setVariable(name: String, value: Int) {
        return when {
            name in variables -> variables[name] = VariableValue(value)
            parent != null -> parent.setVariable(name, value)
            else -> throw VariableNotFound("Undeclared variable $name")
        }
    }

    fun getFunction(name: String): FunctionBody {
        return when {
            name in functions -> functions[name] ?: throw NotDefinedException("Undefined function $name")
            parent != null -> parent.getFunction(name)
            else -> throw FunctionNotFound("Undeclared function $name")
        }
    }

    fun containsVariable(name: String): Boolean {
        return name in variables || (parent != null && parent.containsVariable(name))
    }

    fun containsFunction(name: String): Boolean {
        return name in functions || (parent != null && parent.containsFunction(name))
    }

}