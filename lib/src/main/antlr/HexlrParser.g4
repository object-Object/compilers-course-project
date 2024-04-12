parser grammar HexlrParser;

options {
    tokenVocab=HexlrLexer;
}

start: EOL* (action (EOL+ action)*)? EOL* EOF;

action: pattern | directive;

pattern:
    PATTERN                             # NormalPattern
    | CONSIDERATION                     # NormalPattern
    | INTROSPECTION                     # NormalPattern
    | RETROSPECTION                     # NormalPattern
    | CONSIDERATION COLON? pattern      # EscapedPattern
    | NUMERICAL_REFLECTION COLON NUMBER # NumberPattern
    | BOOKKEEPERS_GAMBIT COLON MASK     # MaskPattern
    | PATTERN COLON PATTERN             # ArgumentPattern;

directive: defineDirective;

defineDirective:
    DEFINE name=PATTERN (EQUALS signature)? block;

block: INTROSPECTION pattern* RETROSPECTION;

signature:
    inputs=types ARROW outputs=types
    | inputs=types ARROW
    | ARROW outputs=types;

types: type (COMMA type)*;

type: TYPE_NAME | list;

list: LSQUARE (type (COMMA type)*)? RSQUARE;