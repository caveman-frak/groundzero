grammar Path;

@header{
  package uk.co.bluegecko.parser.path;
}

/*
 * Parser Rules
 */

path : segment+ EOF ;

segment : command WHITESPACE? NEWLINE* ;

command : ( move | line | horizontal | vertical | arc | cubic | quadratic | close ) ;

move : M WHITESPACE? POINT ;

line : L WHITESPACE? POINT ;

horizontal : H WHITESPACE? NUMBER ;

vertical : V WHITESPACE? NUMBER ;

arc : A WHITESPACE? POINT WHITESPACE NUMBER WHITESPACE OPTION WHITESPACE OPTION WHITESPACE POINT ;
//arc : A WHITESPACE? POINT WHITESPACE POINT WHITESPACE POINT WHITESPACE NUMBER ;

cubic : C WHITESPACE? POINT WHITESPACE POINT WHITESPACE POINT ;

quadratic : Q WHITESPACE? POINT WHITESPACE POINT ;

close : Z ;

point : POINT ;

number : NUMBER ;

option : OPTION ;

/*
 * Lexer Rules
 */

fragment DIGIT : [0-9] ;

fragment DOT : '.' ;

fragment COMMA : ',' ;

fragment ZERO_OR_ONE : ( '0' | '1' ) ;

A : 'A' ;
C : 'C' ;
H : 'H' ;
L : 'L' ;
M : 'M' ;
Q : 'Q' ;
V : 'V' ;
Z : ( 'Z' | 'z' ) ;

OPTION :  ZERO_OR_ONE ;

NUMBER : DIGIT+ ;

DECIMAL: DIGIT+ ( DOT DIGIT+ )? ;

WHITESPACE : ( ' ' | '\t' )+ ;

SEPARATOR : COMMA WHITESPACE? -> skip ;

NEWLINE : ( '\r'? '\n' | '\r' )+ -> skip ;

X : NUMBER ;

Y : NUMBER ;

POINT : X SEPARATOR Y ;