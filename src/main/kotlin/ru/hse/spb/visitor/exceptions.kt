package ru.hse.spb.visitor

sealed class FunException(msg: String) : Exception(msg)
class IncorrectProgramException(msg: String) : FunException(msg)
class VariableNotFound(msg: String) : FunException(msg)
class FunctionNotFound(msg: String) : FunException(msg)
class VariableRedeclarationException(msg: String) : FunException(msg)
class FunctionRedeclarationException(msg: String) : FunException(msg)
class NotDefinedException(msg: String) : FunException(msg)
class WrongArguments(msg: String) : FunException(msg)
