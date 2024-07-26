grammar Path;

@header{
  package uk.co.bluegecko.parser.path;
}

/*
 * Parser Rules
 */

path : segment+ EOF ;

segment : command SPACE? NEWLINE* ;

command : ( move | line | horizontal | vertical | arc | cubic | quadratic | close ) ;

move : M SPACE? POINT ;

line : L SPACE? POINT ;

horizontal : H SPACE? NUMBER ;

vertical : V SPACE? NUMBER ;

arc : A SPACE? POINT SPACE NUMBER SPACE OPTION SPACE OPTION SPACE POINT ;

cubic : C SPACE? POINT SPACE POINT SPACE POINT ;

quadratic : Q SPACE? POINT SPACE POINT ;

close : Z ;

/*
 * Lexer Rules
 */

fragment DIGIT : [0-9] ;

fragment DOT : '.' ;

fragment COMMA : ( ',' ) ;

fragment ZERO_OR_ONE : ( '0' | '1' ) ;

fragment WHITESPACE : ( ' ' | '\t' ) ;

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

SPACE: WHITESPACE+ ;

SEPARATOR : COMMA WHITESPACE? -> skip ;

NEWLINE : ( '\r'? '\n' | '\r' )+ -> skip ;

X : NUMBER ;

Y : NUMBER ;

POINT : X SEPARATOR Y ;

ANY : . ;