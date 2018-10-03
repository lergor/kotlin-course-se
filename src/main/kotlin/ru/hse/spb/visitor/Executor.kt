package ru.hse.spb.visitor

import ru.hse.spb.parser.FunBaseVisitor
import ru.hse.spb.parser.FunParser
import java.io.Writer

val builtInFunctions: List<String> = listOf("println")

class Executor(private val writer: Writer) : FunBaseVisitor<Int?>() {

    private val scopes = Scopes()

    private fun evaluateBlock(block: FunParser.BlockContext): Int? {
        scopes.createNewScope()
        scopes.push()
        val result = visitBlock(block)
        scopes.pop()
        return result
    }

    override fun visitFile(ctx: FunParser.FileContext): Int? = evaluateBlock(ctx.block())

    override fun visitBlockWithBraces(ctx: FunParser.BlockWithBracesContext): Int? {
        return evaluateBlock(ctx.block())
    }

    override fun visitBlock(ctx: FunParser.BlockContext): Int? {
        for (statement in ctx.statement()) {
            val returnStatement = statement.returnStatement()
            if (returnStatement != null) return visit(returnStatement)
            val result = visitStatement(statement)
            if (result != null) return result
        }
        return null
    }

    override fun visitFunctionDeclaration(ctx: FunParser.FunctionDeclarationContext): Int? {
        val currentScope = scopes.currentScope()
        val name = ctx.Identifier().text
        val parameters = ctx.parameterNames().Identifier().map { p -> p.text }
        currentScope.addFunction(name, parameters, ctx.blockWithBraces())
        return null
    }

    override fun visitVariableDeclaration(ctx: FunParser.VariableDeclarationContext): Int? {
        val currentScope = scopes.currentScope()
        val name = ctx.Identifier().text
        currentScope.addVariable(name, if (ctx.expression() != null) visit(ctx.expression()) else null)
        return null
    }

    override fun visitWhileStatement(ctx: FunParser.WhileStatementContext): Int? {
        scopes.createNewScope()
        scopes.push()
        fun conditionIsTrue(): Boolean {
            scopes.createNewScope()
            val condition = (visit(ctx.expression()) == 1)
            if (!condition) scopes.push()
            return condition
        }

        while (conditionIsTrue()) {
            val result = visitBlockWithBraces(ctx.blockWithBraces())
            if (result != null) return result
        }
        return null
    }

    override fun visitIfStatement(ctx: FunParser.IfStatementContext): Int? {
        scopes.createNewScope()
        val condition = visit(ctx.expression()) == 1
        return when {
            condition -> visitBlockWithBraces(ctx.blockWithBraces()[0])
            (ctx.blockWithBraces().size == 2) -> visitBlockWithBraces(ctx.blockWithBraces()[1])
            else -> null
        }
    }

    override fun visitAssignmentStatement(ctx: FunParser.AssignmentStatementContext): Int? {
        val value = visit(ctx.expression())
        val name = ctx.Identifier().text
        when {
            (value != null) -> scopes.currentScope().setVariable(name, value)
            else -> throw IncorrectProgramException("Trying to assign variable $name to null")
        }
        return null
    }

    override fun visitReturnStatement(ctx: FunParser.ReturnStatementContext): Int? = visit(ctx.expression())

    override fun visitVariableIdentifier(ctx: FunParser.VariableIdentifierContext): Int? {
        return scopes.currentScope().getVariable(ctx.Identifier().text)
    }

    override fun visitAtomicExpression(ctx: FunParser.AtomicExpressionContext): Int? = visit(ctx.expression())

    private fun evaluateOperation(operation: String, left: FunParser.ExpressionContext?, right: FunParser.ExpressionContext?): Int? {
        if (left == null || right == null) throw IncorrectProgramException("Incorrect argument for operation $operation")
        val first = visit(left)
        val second = visit(right)
        if (first == null || second == null) throw WrongArguments("Wrong arguments for operation $operation")
        return Operation(operation).apply(first, second)
    }

    override fun visitEqExpression(ctx: FunParser.EqExpressionContext): Int? {
        val left = ctx.expression(0)
        val right = ctx.expression(1)
        val operation = ctx.EqualityOperations().text
        return evaluateOperation(operation, left, right)
    }

    override fun visitLogicalExpression(ctx: FunParser.LogicalExpressionContext): Int? {
        val left = ctx.expression(0)
        val right = ctx.expression(1)
        val operation = ctx.LogicalOperations().text
        return evaluateOperation(operation, left, right)
    }

    override fun visitAddExpression(ctx: FunParser.AddExpressionContext): Int? {
        val left = ctx.expression(0)
        val right = ctx.expression(1)
        val operation = ctx.AdditionOperations().text
        return evaluateOperation(operation, left, right)
    }

    override fun visitRelExpression(ctx: FunParser.RelExpressionContext): Int? {
        val left = ctx.expression(0)
        val right = ctx.expression(1)
        val operation = ctx.RelationOperations().text
        return evaluateOperation(operation, left, right)
    }

    override fun visitMultExpression(ctx: FunParser.MultExpressionContext): Int? {
        val left = ctx.expression(0)
        val right = ctx.expression(1)
        val operation = ctx.MultiplicationOperations().text
        return evaluateOperation(operation, left, right)
    }

    override fun visitLiteral(ctx: FunParser.LiteralContext): Int? {
        return ctx.Literal().text.toInt()
    }

    private fun callBuiltInFunction(function: String, args: FunParser.ArgumentsContext): Int? {
        when (function) {
            "println" -> {
                val string = args.expression().map { e -> visit(e).toString() }.joinToString(" ")
                writer.write(string + System.lineSeparator())
                writer.flush()
                return null
            }
        }
        throw FunctionNotFound("Function $function is not builtin")
    }

    override fun visitFunctionCallExpression(ctx: FunParser.FunctionCallExpressionContext): Int? {
        val name = ctx.Identifier().text
        if (name in builtInFunctions) return callBuiltInFunction(name, ctx.arguments())
        scopes.createNewScope()
        val scope = scopes.currentScope()
        val funcBody = scope.getFunction(name)
        for (index in 0 until funcBody.parameters.size) {
            val expression = ctx.arguments().expression(index)
            val result = visit(expression)
            if (result != null) scope.addVariable(funcBody.parameters[index], result)
        }
        scopes.push()
        val result = visitBlockWithBraces(funcBody.body) ?: return 0
        scopes.pop()
        return result
    }
}