grammar ODataOrderBy;

/*
 * Parser Rules
 */
 
filter: expr;

expr :
| FIELD ASC
| FIELD DESC
| FIELD ASC COMMA expr
| FIELD DESC COMMA expr
; 

/*
 * Lexer Rules
 */
COMMA: ',';
ASC: 'asc';
DESC: 'desc';
FIELD: [A-Za-z0-9]+;

WS: [ \t\r\n]+ -> skip;
