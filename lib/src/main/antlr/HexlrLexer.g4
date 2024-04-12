lexer grammar HexlrLexer;

// directive keywords

DEFINE: '#define' -> pushMode(Define);

// patterns/symbols

COLON: ':';

INTROSPECTION : '{';
RETROSPECTION : '}';
CONSIDERATION : '\\' | 'Consideration';

NUMERICAL_REFLECTION : 'Numerical Reflection';
BOOKKEEPERS_GAMBIT   : 'Bookkeeper\'s Gambit';

NUMBER: '-'? (Integer Decimal? | Decimal) Exponent?;

MASK: [-v]+;

PATTERN: PatternStartEnd (PatternMiddle* PatternStartEnd)?;

// comments

LINE_COMMENT: '//' (~[\r\n])* -> skip;

INLINE_COMMENT: '/*' (INLINE_COMMENT | ~[\r\n])* '*/' -> skip;

// treat block comments as line terminators to allow separating patterns with them
BLOCK_COMMENT: '/*' (Comment | .)*? '*/' -> type(EOL);

// whitespace

// no skip here because line breaks are required to separate patterns
EOL: [\r\n]+;

WS: [ \t]+ -> skip;

// macro declaration

mode Define;

Define_PATTERN: PATTERN -> type(PATTERN);

EQUALS: '=' -> popMode, pushMode(DefineSignature);

Define_INTROSPECTION:
    INTROSPECTION -> type(INTROSPECTION), popMode;

Define_Skip: (Comment | WS | EOL) -> skip;

// macro signature

mode DefineSignature;

TYPE_NAME: [a-zA-Z]+;

COMMA   : ',';
ARROW   : '->';
LSQUARE : '[';
RSQUARE : ']';

DefineSignature_INTROSPECTION:
    INTROSPECTION -> type(INTROSPECTION), popMode;

DefineSignature_Skip: (Comment | WS | EOL) -> skip;

// fragments

fragment Integer  : [0-9]+;
fragment Decimal  : '.' Integer;
fragment Exponent : [eE] Integer;

fragment PatternStartEnd : [a-zA-Z];
fragment PatternMiddle   : PatternStartEnd | [ '+\-];

fragment Comment: LINE_COMMENT | INLINE_COMMENT | BLOCK_COMMENT;