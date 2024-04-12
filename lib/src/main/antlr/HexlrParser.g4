parser grammar HexlrParser;

options {
    tokenVocab=HexlrLexer;
}

start: EOL* statements? EOL* EOF;

statements: statement (EOL+ statement)*;

statement: pattern | embeddedIota | directive;

// patterns

pattern:
    name=(
        PATTERN
        | CONSIDERATION
        | INTROSPECTION
        | RETROSPECTION
    )                                               # NamedPattern
    | CONSIDERATION COLON? (pattern | embeddedIota) # EscapedPattern
    | NUMERICAL_REFLECTION COLON NUMBER             # NumberPattern
    | BOOKKEEPERS_GAMBIT COLON MASK                 # MaskPattern
    | name=PATTERN COLON arg=PATTERN                # NamedPatternWithArg;

embeddedIota: LANGLE iota RANGLE;

iota:
    BOOLEAN                                                # BooleanIota
    | NUMBER                                               # NumberIota
    | LPAREN x=NUMBER COMMA y=NUMBER COMMA z=NUMBER RPAREN # VectorIota
    | LSQUARE (iota (COMMA iota)* COMMA?)? RSQUARE         # ListIota;

// preprocessor directives

directive: defineDirective;

defineDirective:
    DEFINE name=PATTERN (EQUALS signature)? EOL+ block EOL+ END_DEFINE;

block: INTROSPECTION (EOL+ statements)? EOL+ RETROSPECTION;

signature:
    inputs=types ARROW outputs=types
    | inputs=types ARROW
    | ARROW outputs=types;

types: type (COMMA type)*;

type: TYPE_NAME | typeList;

typeList: LSQUARE (type (COMMA type)*)? RSQUARE;