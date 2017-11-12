grammar HwLang;

file : block_=block;

LINE_COMMENT : '//' ~[\r\n]* -> skip;

block : (statement)*;
statement : function | variable | expression | whileSt | ifSt | assignment | returnSt;
function : FUN name_=IDENTIFIER '(' params_ = parameterNames ')' '{' block_=block '}';
variable : VAR name_=IDENTIFIER ('=' expr_=expression)?;
parameterNames : (IDENTIFIER (',' IDENTIFIER)*)?;
whileSt : WHILE '(' cond_=expression ')' '{' block_=block '}';
ifSt : IF '(' cond_=expression ')' '{' thenBlock_=block '}' (ELSE '{' elseBlock_=block '}')?;
assignment : name_=IDENTIFIER '=' expr_=expression;
returnSt : RETURN expr_=expression;
expression : lhs_=lhsExpression binaryOperation_=binaryOperation?;
lhsExpression : functionCall | reference | literal | '(' expression ')';
binaryOperation :
    op_=('+' | '-' | '*' | '/' | '%' | '>' | '<' | '>=' | '<=' | '==' | '!=' | '||' | '&&')
    rhs_=expression;
functionCall : name_=IDENTIFIER '(' args_=arguments ')';
arguments : (expression (',' expression)*)?;

reference : name_=IDENTIFIER;
literal : '-'? LITERAL | '0';

IF : 'if';
FUN : 'fun';
VAR : 'var';
WHILE : 'while';
ELSE : 'else';
RETURN : 'return';

IDENTIFIER : LETTER (LETTER | DIGIT)*;
LITERAL : DIGIT_NON_ZERO DIGIT*;

fragment LETTER : [a-zA-Z_];
fragment DIGIT_NON_ZERO : [1-9];
fragment DIGIT : [0-9];

WS : (' ' | '\t' | '\r'| '\n') -> skip;
