parser grammar HexlrParser;

options {
    tokenVocab=HexlrLexer;
}

start: EOL* statements? EOL* EOF;

statements: statement (EOL+ statement)*;

statement: pattern | directive;

pattern:
    name=(
        PATTERN
        | CONSIDERATION
        | INTROSPECTION
        | RETROSPECTION
    )                                   # NamedPattern
    | CONSIDERATION COLON? pattern      # EscapedPattern
    | NUMERICAL_REFLECTION COLON NUMBER # NumberPattern
    | BOOKKEEPERS_GAMBIT COLON MASK     # MaskPattern
    | name=PATTERN COLON arg=PATTERN    # NamedPatternWithArg;

directive: defineDirective;

defineDirective:
    DEFINE name=PATTERN (EQUALS signature)? EOL+ block;

block: INTROSPECTION (EOL+ statements)? EOL+ RETROSPECTION;

signature:
    inputs=types ARROW outputs=types
    | inputs=types ARROW
    | ARROW outputs=types;

types: type (COMMA type)*;

type: TYPE_NAME | list;

list: LSQUARE (type (COMMA type)*)? RSQUARE;