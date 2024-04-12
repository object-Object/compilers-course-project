parser grammar HexlrParser;

options {
    tokenVocab=HexlrLexer;
}

start: EOL* actions? EOL* EOF;

actions  : action (EOL+ action)*;
patterns : pattern (EOL+ pattern)*;

action: pattern | directive;

pattern:
    name=(
        PATTERN
        | CONSIDERATION
        | INTROSPECTION
        | RETROSPECTION
    )                                   # NormalPattern
    | CONSIDERATION COLON? pattern      # EscapedPattern
    | NUMERICAL_REFLECTION COLON NUMBER # NumberPattern
    | BOOKKEEPERS_GAMBIT COLON MASK     # MaskPattern
    | name=PATTERN COLON arg=PATTERN    # ArgumentPattern;

directive: defineDirective;

defineDirective:
    DEFINE name=PATTERN (EQUALS signature)? EOL+ block;

block: INTROSPECTION (EOL+ patterns)? EOL+ RETROSPECTION;

signature:
    inputs=types ARROW outputs=types
    | inputs=types ARROW
    | ARROW outputs=types;

types: type (COMMA type)*;

type: TYPE_NAME | list;

list: LSQUARE (type (COMMA type)*)? RSQUARE;