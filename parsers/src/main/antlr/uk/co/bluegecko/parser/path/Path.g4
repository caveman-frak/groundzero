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

move : M SPACE? destination=POINT ;

line : L SPACE? destination=POINT ;

horizontal : H SPACE? distance=NUMBER ;

vertical : V SPACE? distance=NUMBER ;

arc : A SPACE? radius=POINT SPACE rotation=NUMBER SPACE destination=POINT SPACE largeArc=OPTION SPACE sweep=OPTION ;

cubic : C SPACE? control1=POINT SPACE control2=POINT SPACE destination=POINT ;

quadratic : Q SPACE? control=POINT SPACE destination=POINT ;

close : Z ;

/*
 * Lexer Rules
 */

fragment DIGIT : [0-9] ;

fragment DOT : '.' ;

fragment COMMA : ( ',' ) ;

fragment ZERO_OR_ONE : ( '0' | '1' ) ;

fragment WHITESPACE : ( ' ' | '\t' ) ;

A : ( 'A' | 'a' ) ;
C : ( 'C' | 'c' ) ;
H : ( 'H' | 'h' ) ;
L : ( 'L' | 'l' ) ;
M : ( 'M' | 'm' ) ;
Q : ( 'Q' | 'q' ) ;
V : ( 'V' | 'v' ) ;
Z : ( 'Z' | 'z' ) ;

OPTION :  ZERO_OR_ONE ;

NUMBER : DIGIT+ ;

DECIMAL: DIGIT+ ( DOT DIGIT+ )? ;

SPACE: WHITESPACE+ ;

SEPARATOR : COMMA WHITESPACE? ;

NEWLINE : ( '\r'? '\n' | '\r' )+ ;

POINT : NUMBER SEPARATOR NUMBER ;

ANY : . ;