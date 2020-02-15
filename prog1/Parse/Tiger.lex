package Parse;
import ErrorMsg.ErrorMsg;

%%

%implements Lexer
%function nextToken
%type java_cup.runtime.Symbol
%char

%{
private void newline() {
  errorMsg.newline(yychar);
}

private void err(int pos, String s) {
  errorMsg.error(pos,s);
}

private void err(String s) {
  err(yychar,s);
}

private java_cup.runtime.Symbol tok(int kind) {
    return tok(kind, null);
}

private java_cup.runtime.Symbol tok(int kind, Object value) {
    return new java_cup.runtime.Symbol(kind, yychar, yychar+yylength(), value);
}

private ErrorMsg errorMsg;

Yylex(java.io.InputStream s, ErrorMsg e) {
  this(s);
  errorMsg=e;
}

//Variables we'll use later
private int commentDepth = 0;
private StringBuffer buffer;
private boolean instring = false;

%}

%eofval{
	{
  if(!instring){return tok(sym.EOF, null);}
  }
%eofval}

%state STRING
%state COMMENT

CONT=[A-Za-z@\[\]\\\^_(0-9)]
ALPHA=[A-Za-z]
DIGIT=[0-9]
WHITE_SPACE_CHAR=[\ \n\t\b\012]

%%
<YYINITIAL> {WHITE_SPACE_CHAR}	{}
<YYINITIAL> \n	{newline();}
<YYINITIAL> ","	{return tok(sym.COMMA, null);}
<YYINITIAL> "." {return tok(sym.DOT, null);}
<YYINITIAL> ":" {return tok(sym.COLON, null);}
<YYINITIAL> "/" {return tok(sym.DIVIDE, null);}
<YYINITIAL> "-" {return tok(sym.MINUS, null);}
<YYINITIAL> "*" {return tok(sym.TIMES, null);}
<YYINITIAL> "+" {return tok(sym.PLUS, null);}
<YYINITIAL> "<" {return tok(sym.LT, null);}
<YYINITIAL> ">" {return tok(sym.GT, null);}
<YYINITIAL> "<=" {return tok(sym.LE, null);}
<YYINITIAL> ">=" {return tok(sym.GE, null);}
<YYINITIAL> "<>" {return tok(sym.NEQ, null);}
<YYINITIAL> "(" {return tok(sym.LPAREN, null);}
<YYINITIAL> ")" {return tok(sym.RPAREN, null);}
<YYINITIAL> ";" {return tok(sym.SEMICOLON, null);}
<YYINITIAL> "[" {return tok(sym.LBRACK, null);}
<YYINITIAL> "]" {return tok(sym.RBRACK, null);}
<YYINITIAL> "{" {return tok(sym.LBRACE, null);}
<YYINITIAL> "}" {return tok(sym.RBRACE, null);}
<YYINITIAL> "|" {return tok(sym.OR, null);}
<YYINITIAL> "=" {return tok(sym.EQ, null);}
<YYINITIAL> ":=" {return tok(sym.ASSIGN, null);}
<YYINITIAL> "&" {return tok(sym.AND, null);}
<YYINITIAL> "let" {return tok(sym.LET, null);}
<YYINITIAL> "while" {return tok(sym.WHILE, null);}
<YYINITIAL> "for" {return tok(sym.FOR, null);}
<YYINITIAL> "to" {return tok(sym.TO, null);}
<YYINITIAL> "break" {return tok(sym.BREAK, null);}
<YYINITIAL> "in" {return tok(sym.IN, null);}
<YYINITIAL> "do" {return tok(sym.DO, null);}
<YYINITIAL> "of" {return tok(sym.OF, null);}
<YYINITIAL> "nil" {return tok(sym.NIL, null);}
<YYINITIAL> "array" {return tok(sym.ARRAY, null);}
<YYINITIAL> "type" {return tok(sym.TYPE, null);}
<YYINITIAL> "if" {return tok(sym.IF, null);}
<YYINITIAL> "end" {return tok(sym.END, null);}
<YYINITIAL> "var" {return tok(sym.VAR, null);}
<YYINITIAL> "then" {return tok(sym.THEN, null);}
<YYINITIAL> "else" {return tok(sym.ELSE, null);}
<YYINITIAL> "typedef" {return tok(sym.TYPE, null);}

<YYINITIAL> {ALPHA}({ALPHA}|{DIGIT}|_)* {
  return tok(sym.ID, yytext());
}

<YYINITIAL> [0-9]+ {
  return tok(sym.INT, Integer.parseInt(yytext()));
}

<YYINITIAL> \" {
  //Found open quote meaning a string is coming
  yybegin(STRING);
  instring = true;

  //reset buffer
  buffer = new StringBuffer();
}

<STRING> [^\\\"] {
  //If it's not a \ or a " add it to the buffer
  buffer.append(yytext());
}

<STRING> \\("t"|"n"|\\|\"|\^CONT|[0-9][0-9][0-9]|\ ) {
  //Escape characters
  //Namely a \ with a n, t, ###, \, ", CONT, (WHITESPACE) after
  //esc is everything after the first backslash
  String esc = yytext().substring(1, yytext().length());
  char first = esc.charAt(0);

  //We'll need to do special stuff with control chars later
  if(first != '^'){
    buffer.append(first);
  }

  else if(first == '^'){
    char controlChar = esc.charAt(1);
    buffer.append(controlChar);
  }
}

<STRING> \" {
  //end of string
  yybegin(YYINITIAL);
  instring = false;
  return tok(sym.STRING, buffer);
}

<YYINITIAL> "/*" {
  commentDepth++;
  yybegin(COMMENT);
}

<COMMENT> . {
  //Ignore anything within comment
}
<COMMENT> "/*" {
  //Comment depth keeps track of how nested we currently are
  commentDepth++;
  }
<COMMENT> "*/" {
  commentDepth--;
  //If we're not nested anymore, go back to initial
  if(commentDepth==0)
    yybegin(YYINITIAL);
}

. {
  err("Illegal character: " + yytext() + "(code: " + (int)(yytext().charAt(0)) + ")");
}
