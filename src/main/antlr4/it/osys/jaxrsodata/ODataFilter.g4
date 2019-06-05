grammar ODataFilter;

/*
 * Parser Rules
 */
 
filter: expr;

expr :
| CONTAINS BR_OPEN FIELD COMMA STRINGLITERAL BR_CLOSE
| TOLOWER BR_OPEN FIELD BR_CLOSE EQ STRINGLITERAL
| TOUPPER BR_OPEN FIELD BR_CLOSE EQ STRINGLITERAL
| BR_OPEN expr BR_CLOSE
| FIELD EQ NUMBER
| FIELD EQ TIME
| FIELD EQ DATETIME
| FIELD EQ STRINGLITERAL
| FIELD EQ NULL
| FIELD NE NUMBER
| FIELD NE TIME
| FIELD NE DATETIME
| FIELD NE STRINGLITERAL
| FIELD NE NULL
| FIELD GT NUMBER
| FIELD GT TIME
| FIELD GT DATETIME
| FIELD GE NUMBER
| FIELD GE TIME
| FIELD GE DATETIME
| FIELD LT NUMBER
| FIELD LT TIME
| FIELD LT DATETIME
| FIELD LE NUMBER
| FIELD LE TIME
| FIELD LE DATETIME
| expr AND expr
| expr OR expr
| NOT expr
; 

/*
 * Lexer Rules
 */
start: range;
range: period '-' period;
period: DATETIME;
fragment D: ('0'..'9');
fragment TZO: '.' D D D 'Z';
ISODate: D D D D '-' D D '-' D D;
ISOTime: D D ':' D D ':' D D ('.' D)?;
TIME: '\'' ISOTime ('Z' | TZO)? '\'';
DATETIME: '\'' (ISODate ('T' ISOTime ('Z' | TZO)?)?) '\'';
STRINGLITERAL: '\'' (~'\'')* '\'';
NULL: 'null';
COMMA: ',';
CONTAINS: 'contains';
TOLOWER: 'tolower';
TOUPPER: 'toupper';
NOT: 'not';
BR_OPEN: '(';
BR_CLOSE: ')';
AND: 'and';
OR: 'or';
EQ: 'eq';
NE: 'ne';
GT: 'gt';
GE: 'ge';
LT: 'lt';
LE: 'le';
NUMBER: [0-9]+;
FIELD: [A-Za-z0-9/]+;


WS: [ \t\r\n]+ -> skip;
