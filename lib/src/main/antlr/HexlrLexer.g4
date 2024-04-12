lexer grammar HexlrLexer;

// directive keywords

DEFINE: '#define' -> pushMode(Define);

// patterns/symbols

COLON: ':';

INTROSPECTION : '{' | 'Introspection';
RETROSPECTION : '}' | 'Retrospection';
CONSIDERATION : '\\' | 'Consideration';

NUMERICAL_REFLECTION : 'Numerical Reflection';
BOOKKEEPERS_GAMBIT   : 'Bookkeeper\'s Gambit';

NUMBER: '-'? (Integer Decimal? | Decimal) Exponent?;

MASK: [-v]+;

PATTERN: PatternStartEnd (PatternMiddle* PatternStartEnd)?;

// comments/whitespace

COMMENT: (LineComment | InlineBlockComment) -> skip;

// not skipped because line breaks are required to separate patterns
// and treat block comments as line terminators to allow separating patterns with them
EOL: ([\r\n] | BlockComment)+;

WS: [ \t]+ -> skip;

// macro declaration

mode Define;

Define_PATTERN: PATTERN -> type(PATTERN);

EQUALS: '=' -> pushMode(DefineSignature);


Define_INTROSPECTION:
    INTROSPECTION -> type(INTROSPECTION), popMode;

Define_COMMENT : COMMENT -> skip;
Define_EOL     : EOL     -> type(EOL);
Define_WS      : WS      -> skip;

// macro signature

mode DefineSignature;

TYPE_NAME: [a-zA-Z]+;

COMMA   : ',';
ARROW   : '->';
LSQUARE : '[';
RSQUARE : ']';

DefineSignature_COMMENT : COMMENT -> skip;
DefineSignature_EOL     : EOL     -> type(EOL), popMode;
DefineSignature_WS      : WS      -> skip;

// fragments

fragment Integer  : [0-9]+;
fragment Decimal  : '.' Integer;
fragment Exponent : [eE] Integer;

fragment PatternStartEnd : [a-zA-Z];
fragment PatternMiddle   : PatternStartEnd | [ '+\-];

fragment LineComment: '//' (~[\r\n])*;

fragment InlineBlockComment:
    '/*' (InlineBlockComment | ~[\r\n])* '*/';

fragment BlockComment: '/*' (BlockComment | .)*? '*/';