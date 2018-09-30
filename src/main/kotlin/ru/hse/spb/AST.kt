package ru.hse.spb

abstract class Node {
    abstract fun visit(visitor: Visitor)
}

abstract class ExpressionNode : Node() {
    override fun visit(visitor: Visitor) = visitor.visitExpressionNode(this)
}

data class FunctionCallNode(val id: IdentifierNode, val arguments: ArgumentsNode?) : ExpressionNode() {
    override fun visit(visitor: Visitor) = visitor.visitFunctionCallNode(this)
}

data class IdentifierNode(val name: String) : ExpressionNode() {
    override fun visit(visitor: Visitor) = visitor.visitIdentifierNode(this)
}

data class ArgumentsNode(val expressions: MutableList<ExpressionNode>) : ExpressionNode() {
    override fun visit(visitor: Visitor) = visitor.visitArgumentsNode(this)
}

data class BinaryExpressionNode(val left: ExpressionNode, val op: KeyWord, val right: ExpressionNode) : ExpressionNode() {
    override fun visit(visitor: Visitor) = visitor.visitBinaryExpressionNode(this)
}

data class LiteralNode(val value: Int) : ExpressionNode() {
    override fun visit(visitor: Visitor) = visitor.visitLiteralNode(this)
}

abstract class StatementNode : Node() {
    override fun visit(visitor: Visitor) = visitor.visitStatementNode(this)
}

data class BlockNode(val statementList: MutableList<StatementNode>, val scope: Scope) : StatementNode() {
    override fun visit(visitor: Visitor) = visitor.visitBlockNode(this)
}

data class FunctionNode(val id: IdentifierNode, val arguments: ParameterNamesNode?, val body: BlockWithBracesNode) : StatementNode() {
    override fun visit(visitor: Visitor) = visitor.visitFunctionNode(this)
}

data class ParameterNamesNode(val idList: MutableList<IdentifierNode>) : StatementNode() {
    override fun visit(visitor: Visitor) = visitor.visitParameterNamesNode(this)
}

data class BlockWithBracesNode(val block: BlockNode) : Node() {
    override fun visit(visitor: Visitor) = visitor.visitBlockWithBracesNode(this)
}

data class VariableNode(val id: IdentifierNode, var expr: ExpressionNode?) : StatementNode() {
    override fun visit(visitor: Visitor) = visitor.visitVariableNode(this)
}

data class WhileNode(val expr: ExpressionNode, val body: BlockWithBracesNode) : StatementNode() {
    override fun visit(visitor: Visitor) = visitor.visitWhileNode(this)
}

data class IfNode(val expr: ExpressionNode, val thenBlock: BlockWithBracesNode, val elseBlock: BlockWithBracesNode?) : StatementNode() {
    override fun visit(visitor: Visitor) = visitor.visitIfNode(this)
}

data class AssignmentNode(val id: IdentifierNode, val expr: ExpressionNode) : StatementNode() {
    override fun visit(visitor: Visitor) = visitor.visitAssignmentNode(this)
}

data class ReturnNode(val expr: ExpressionNode) : StatementNode() {
    override fun visit(visitor: Visitor) = visitor.visitReturnNode(this)
}
