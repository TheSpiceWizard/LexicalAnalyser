package Parse;
import ErrorMsg.ErrorMsg;


class Yylex implements Lexer {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

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
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int STRING = 1;
	private final int YYINITIAL = 0;
	private final int COMMENT = 2;
	private final int yy_state_dtrans[] = {
		0,
		54,
		69
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NOT_ACCEPT,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NOT_ACCEPT,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NOT_ACCEPT,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NOT_ACCEPT,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NOT_ACCEPT,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NOT_ACCEPT,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NOT_ACCEPT,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"51:3,46,51:4,1:2,2,51:2,44,51:18,50,51,43,51:3,21,51,13,14,8,9,3,7,4,6,41:1" +
"0,5,15,10,12,11,51:2,40:13,48,47,40:4,49,40:6,16,45,17,51,42,51,32,31,40,35" +
",23,28,40,26,27,40,33,22,40,34,29,37,40,30,39,24,40,38,25,40,36,40,18,20,19" +
",51:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,99,
"0,1:4,2,3,1:3,4,5,1:10,6,7,1:7,8:12,9,8:4,1:6,10,11,12,13,14,15,16,17,18,19" +
",20,21,22,23,8,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43," +
"44,45,46,47,8,48,49,50,51,52")[0];

	private int yy_nxt[][] = unpackFromString(53,52,
"1,2:2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,89,55,92,93,59,9" +
"4,62,93,95,96,93,97,64,93:2,98,93:2,23,24,25,-1,24:2,93:3,2,24,-1:64,26,-1:" +
"47,27,-1:54,28,29,-1:51,30,-1:61,93,66,93:17,68:2,-1:4,93:3,-1:43,23,-1:32," +
"93:19,68:2,-1:4,93:3,-1:24,93:13,87,93:5,68:2,-1:4,93:3,-1:2,1,48:42,49,48," +
"56,48:6,-1:22,93:4,72,93:2,31,93:6,73,93:4,68:2,-1:4,93:3,-1:26,50,-1:9,50," +
"-1:6,58,-1,50,-1,50,61,-1:3,50,-1:9,52,-1:84,63,-1:32,93:6,32,93:5,33,93:6," +
"68:2,-1:4,93:3,-1:8,53,-1:92,65,-1:26,93:6,34,93:12,68:2,-1:4,93:3,-1:43,50" +
",-1:32,93:7,35,93:11,68:2,-1:4,93:3,-1:50,67,-1:25,93:2,36,93:16,68:2,-1:4," +
"93:3,-1:51,50,-1:2,1,51,-1,51:3,57,51,60,51:35,-1,51:7,-1:22,93:17,79,93,68" +
":2,-1:4,93:3,-1:24,93:13,37,93:5,68:2,-1:4,93:3,-1:24,93,80,93:17,68:2,-1:4" +
",93:3,-1:24,93:15,81,93:3,68:2,-1:4,93:3,-1:24,93:5,82,93:13,68:2,-1:4,93:3" +
",-1:24,93:8,38,93:10,68:2,-1:4,93:3,-1:24,93:8,91,93:10,68:2,-1:4,93:3,-1:2" +
"4,39,93:18,68:2,-1:4,93:3,-1:24,93:8,40,93:10,68:2,-1:4,93:3,-1:24,93,41,93" +
":17,68:2,-1:4,93:3,-1:24,93:12,42,93:6,68:2,-1:4,93:3,-1:24,93,43,93:17,68:" +
"2,-1:4,93:3,-1:24,84,93:18,68:2,-1:4,93:3,-1:24,93:10,85,93:8,68:2,-1:4,93:" +
"3,-1:24,93,44,93:17,68:2,-1:4,93:3,-1:24,93:11,45,93:7,68:2,-1:4,93:3,-1:24" +
",93:14,46,93:4,68:2,-1:4,93:3,-1:24,93,88,93:17,68:2,-1:4,93:3,-1:24,93:6,4" +
"7,93:12,68:2,-1:4,93:3,-1:24,70,93:11,71,93:6,68:2,-1:4,93:3,-1:24,93,83,93" +
":17,68:2,-1:4,93:3,-1:24,93:10,86,93:8,68:2,-1:4,93:3,-1:24,93:4,74,93:14,6" +
"8:2,-1:4,93:3,-1:24,93:7,75,93:11,68:2,-1:4,93:3,-1:24,93:8,90,93:10,68:2,-" +
"1:4,93:3,-1:24,93:8,76,93:10,68:2,-1:4,93:3,-1:24,93:5,77,93:13,68:2,-1:4,9" +
"3:3,-1:24,93:10,78,93:8,68:2,-1:4,93:3,-1:2");

	public java_cup.runtime.Symbol nextToken ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

	{
  if(!instring){return tok(sym.EOF, null);}
  }
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{}
					case -3:
						break;
					case 3:
						{return tok(sym.COMMA, null);}
					case -4:
						break;
					case 4:
						{return tok(sym.DOT, null);}
					case -5:
						break;
					case 5:
						{return tok(sym.COLON, null);}
					case -6:
						break;
					case 6:
						{return tok(sym.DIVIDE, null);}
					case -7:
						break;
					case 7:
						{return tok(sym.MINUS, null);}
					case -8:
						break;
					case 8:
						{return tok(sym.TIMES, null);}
					case -9:
						break;
					case 9:
						{return tok(sym.PLUS, null);}
					case -10:
						break;
					case 10:
						{return tok(sym.LT, null);}
					case -11:
						break;
					case 11:
						{return tok(sym.GT, null);}
					case -12:
						break;
					case 12:
						{return tok(sym.EQ, null);}
					case -13:
						break;
					case 13:
						{return tok(sym.LPAREN, null);}
					case -14:
						break;
					case 14:
						{return tok(sym.RPAREN, null);}
					case -15:
						break;
					case 15:
						{return tok(sym.SEMICOLON, null);}
					case -16:
						break;
					case 16:
						{return tok(sym.LBRACK, null);}
					case -17:
						break;
					case 17:
						{return tok(sym.RBRACK, null);}
					case -18:
						break;
					case 18:
						{return tok(sym.LBRACE, null);}
					case -19:
						break;
					case 19:
						{return tok(sym.RBRACE, null);}
					case -20:
						break;
					case 20:
						{return tok(sym.OR, null);}
					case -21:
						break;
					case 21:
						{return tok(sym.AND, null);}
					case -22:
						break;
					case 22:
						{
  return tok(sym.ID, yytext());
}
					case -23:
						break;
					case 23:
						{
  return tok(sym.INT, Integer.parseInt(yytext()));
}
					case -24:
						break;
					case 24:
						{
  err("Illegal character: " + yytext() + "(code: " + (int)(yytext().charAt(0)) + ")");
}
					case -25:
						break;
					case 25:
						{
  //Found open quote meaning a string is coming
  yybegin(STRING);
  instring = true;
  //reset buffer
  buffer = new StringBuffer();
}
					case -26:
						break;
					case 26:
						{return tok(sym.ASSIGN, null);}
					case -27:
						break;
					case 27:
						{
  commentDepth++;
  yybegin(COMMENT);
}
					case -28:
						break;
					case 28:
						{return tok(sym.NEQ, null);}
					case -29:
						break;
					case 29:
						{return tok(sym.LE, null);}
					case -30:
						break;
					case 30:
						{return tok(sym.GE, null);}
					case -31:
						break;
					case 31:
						{return tok(sym.TO, null);}
					case -32:
						break;
					case 32:
						{return tok(sym.IF, null);}
					case -33:
						break;
					case 33:
						{return tok(sym.IN, null);}
					case -34:
						break;
					case 34:
						{return tok(sym.OF, null);}
					case -35:
						break;
					case 35:
						{return tok(sym.DO, null);}
					case -36:
						break;
					case 36:
						{return tok(sym.LET, null);}
					case -37:
						break;
					case 37:
						{return tok(sym.END, null);}
					case -38:
						break;
					case 38:
						{return tok(sym.FOR, null);}
					case -39:
						break;
					case 39:
						{return tok(sym.NIL, null);}
					case -40:
						break;
					case 40:
						{return tok(sym.VAR, null);}
					case -41:
						break;
					case 41:
						{return tok(sym.ELSE, null);}
					case -42:
						break;
					case 42:
						{return tok(sym.THEN, null);}
					case -43:
						break;
					case 43:
						{return tok(sym.TYPE, null);}
					case -44:
						break;
					case 44:
						{return tok(sym.WHILE, null);}
					case -45:
						break;
					case 45:
						{return tok(sym.BREAK, null);}
					case -46:
						break;
					case 46:
						{return tok(sym.ARRAY, null);}
					case -47:
						break;
					case 47:
						{return tok(sym.TYPE, null);}
					case -48:
						break;
					case 48:
						{
  //If it's not a \ or a " add it to the buffer
  buffer.append(yytext());
}
					case -49:
						break;
					case 49:
						{
  //end of string
  yybegin(YYINITIAL);
  instring = false;
  return tok(sym.STRING, buffer);
}
					case -50:
						break;
					case 50:
						{
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
					case -51:
						break;
					case 51:
						{
  //Ignore anything within comment
}
					case -52:
						break;
					case 52:
						{
  //Comment depth keeps track of how nested we currently are
  commentDepth++;
  }
					case -53:
						break;
					case 53:
						{
  commentDepth--;
  //If we're not nested anymore, go back to initial
  if(commentDepth==0)
    yybegin(YYINITIAL);
}
					case -54:
						break;
					case 55:
						{
  return tok(sym.ID, yytext());
}
					case -55:
						break;
					case 56:
						{
  err("Illegal character: " + yytext() + "(code: " + (int)(yytext().charAt(0)) + ")");
}
					case -56:
						break;
					case 57:
						{
  //Ignore anything within comment
}
					case -57:
						break;
					case 59:
						{
  return tok(sym.ID, yytext());
}
					case -58:
						break;
					case 60:
						{
  //Ignore anything within comment
}
					case -59:
						break;
					case 62:
						{
  return tok(sym.ID, yytext());
}
					case -60:
						break;
					case 64:
						{
  return tok(sym.ID, yytext());
}
					case -61:
						break;
					case 66:
						{
  return tok(sym.ID, yytext());
}
					case -62:
						break;
					case 68:
						{
  return tok(sym.ID, yytext());
}
					case -63:
						break;
					case 70:
						{
  return tok(sym.ID, yytext());
}
					case -64:
						break;
					case 71:
						{
  return tok(sym.ID, yytext());
}
					case -65:
						break;
					case 72:
						{
  return tok(sym.ID, yytext());
}
					case -66:
						break;
					case 73:
						{
  return tok(sym.ID, yytext());
}
					case -67:
						break;
					case 74:
						{
  return tok(sym.ID, yytext());
}
					case -68:
						break;
					case 75:
						{
  return tok(sym.ID, yytext());
}
					case -69:
						break;
					case 76:
						{
  return tok(sym.ID, yytext());
}
					case -70:
						break;
					case 77:
						{
  return tok(sym.ID, yytext());
}
					case -71:
						break;
					case 78:
						{
  return tok(sym.ID, yytext());
}
					case -72:
						break;
					case 79:
						{
  return tok(sym.ID, yytext());
}
					case -73:
						break;
					case 80:
						{
  return tok(sym.ID, yytext());
}
					case -74:
						break;
					case 81:
						{
  return tok(sym.ID, yytext());
}
					case -75:
						break;
					case 82:
						{
  return tok(sym.ID, yytext());
}
					case -76:
						break;
					case 83:
						{
  return tok(sym.ID, yytext());
}
					case -77:
						break;
					case 84:
						{
  return tok(sym.ID, yytext());
}
					case -78:
						break;
					case 85:
						{
  return tok(sym.ID, yytext());
}
					case -79:
						break;
					case 86:
						{
  return tok(sym.ID, yytext());
}
					case -80:
						break;
					case 87:
						{
  return tok(sym.ID, yytext());
}
					case -81:
						break;
					case 88:
						{
  return tok(sym.ID, yytext());
}
					case -82:
						break;
					case 89:
						{
  return tok(sym.ID, yytext());
}
					case -83:
						break;
					case 90:
						{
  return tok(sym.ID, yytext());
}
					case -84:
						break;
					case 91:
						{
  return tok(sym.ID, yytext());
}
					case -85:
						break;
					case 92:
						{
  return tok(sym.ID, yytext());
}
					case -86:
						break;
					case 93:
						{
  return tok(sym.ID, yytext());
}
					case -87:
						break;
					case 94:
						{
  return tok(sym.ID, yytext());
}
					case -88:
						break;
					case 95:
						{
  return tok(sym.ID, yytext());
}
					case -89:
						break;
					case 96:
						{
  return tok(sym.ID, yytext());
}
					case -90:
						break;
					case 97:
						{
  return tok(sym.ID, yytext());
}
					case -91:
						break;
					case 98:
						{
  return tok(sym.ID, yytext());
}
					case -92:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
