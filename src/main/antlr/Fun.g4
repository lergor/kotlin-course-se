grammar Fun;

/* parser rules */

file
    : block
    ;

block
    : statement*
    ;

blockWithBraces
    : LeftBrace block RightBrace
    ;

statement
    : functionDeclaration
    | variableDeclaration
    | expressionStatement
    | whileStatement
    | ifStatement
    | assignmentStatement
    | returnStatement
    ;

functionDeclaration
    : Fun Identifier LeftBracket parameterNames RightBracket blockWithBraces
    ;

variableDeclaration
    : Var Identifier (Assign expression)?
    ;

expressionStatement
    : expression
    ;

parameterNames :
    (Identifier (Comma Identifier)*)?
    ;

whileStatement
    : While LeftBracket expression RightBracket blockWithBraces
    ;

ifStatement
    : If LeftBracket expression RightBracket blockWithBraces (Else blockWithBraces)?
    ;

assignmentStatement
    : Identifier Assign expression
    ;

returnStatement
    : Return expression
    ;

expression
    : functionCallExpression                            # functionCall
    | expression MultiplicationOperations expression    # multExpression
    | expression AdditionOperations expression          # addExpression
    | expression RelationOperations expression          # relExpression
    | expression EqualityOperations expression          # eqExpression
    | expression LogicalOperations expression           # logicalExpression
    | LeftBracket expression RightBracket               # atomicExpression
    | Identifier                                        # variableIdentifier
    | Literal                                           # literal
    ;

functionCallExpression
    : Identifier LeftBracket arguments RightBracket
    ;

arguments
    : (expression (Comma expression)*)?
    ;

/* keywords */

Fun : 'fun' ;
Var : 'var' ;
While : 'while' ;
If : 'if' ;
Else : 'else' ;
Return : 'return' ;
Assign : '=' ;

Identifier : [a-zA-Z_] [a-zA-Z_0-9]* ;
Literal : ('-')? ('0' | [1-9][0-9]*) ;
Comment : '//' ~[\n\r]* -> channel(HIDDEN) ;
WS : [ \t\r\n] -> skip;
LeftBrace : '{' ;
RightBrace : '}' ;
LeftBracket : '(' ;
RightBracket : ')' ;
Comma : ',' ;

/* binary operations */

AdditionOperations : PLUS | MINUS ;
MultiplicationOperations : MULT | DIV | MOD ;
RelationOperations : GT | LT | GTEQ | LTEQ ;
EqualityOperations : EQ | NEQ ;
LogicalOperations : AND | OR ;

PLUS : '+';
MINUS : '-';
MULT : '*';
DIV : '/';
MOD : '%';
GT : '>';
LT : '<';
GTEQ : '>=';
LTEQ : '<=';
EQ : '==';
NEQ : '!=';
AND : '&&';
OR : '||';
