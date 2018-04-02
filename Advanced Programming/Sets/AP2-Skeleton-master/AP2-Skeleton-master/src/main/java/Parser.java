import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.omg.CORBA.UnionMember;

public class Parser<E extends Comparable> {
	
	public final char ASSIGN = '=';
	public final char PRINT = '?';
	public final char COMMENT = '/';
	public final String COLON = ":";
	public final char UNION = '+';
	public final char SYM_DIFFERENCE = '|';
	public final char COMPLEMENT = '-';
	public final char INTERSECTION  = '*';
	public final char ZERO = '0';
	public final char COMMA = ',';
	public final char OPENING_CURLY_BRACE = '{';
	public final char CLOSING_CURLY_BRACE = '}';
	public final char OPENING_SEMI_BRACE = '(';
	public final char CLOSING_SEMI_BRACE = ')';
	
	private Scanner tokenizer;
	private HashMap<String, SetInterface<BigInteger>> identifierSet;
	
	public Parser() {	
		identifierSet = new HashMap<>();
	}
	
	/** @precondition  -
     *	@postcondition - changes the input to current line
     **/
	public void changeInput(String input) {
		tokenizer = new Scanner(input);
		tokenizer.useDelimiter("");
	}
	
	/** @precondition  -
     *	@postcondition - The statement has been parsed.
     **/
	public void statement() throws APException {
		if(nextCharIs(PRINT)) {
			printStatement();
		} else if(nextCharIs(COMMENT)) {
			return;
		} else if(nextCharIsLetter()){
			assignment();
		} else {
			throw new APException("no statement");
		}
	}

	/** @precondition  -
     *	@postcondition - The assignment has been done
     **/
	public void assignment() throws APException {
		IdentifierInterface identifier = readNextIdentifier();
		if(nextCharIs(ASSIGN) && identifier != null) {
			readNextChar(); //reads the '='
			boolean nextCharIsOpeningBrace = nextCharIs(OPENING_CURLY_BRACE) || nextCharIs(OPENING_SEMI_BRACE);
			SetInterface<BigInteger> set = expression();
			if(set == null) {
				throw new APException("incomplete statement");
			} else {
				if(tokenizer.hasNext()) {
					if(nextCharIs(CLOSING_SEMI_BRACE) && nextCharIsOpeningBrace) {
						throw new APException("missing parenthesis");
					}
					throw new APException("no end of line");
				} else {
					identifierSet.put(identifier.getName(), set);
				}
			}
		} else {
			throw new APException("equal sign expected");
		}
	}
	
	/** @precondition  -
     *	@postcondition - The statement has been printed
     **/
	public void  printStatement() throws APException {
		readNextChar(); //reads the ?
		SetInterface<BigInteger> set = expression();
		if(set == null) {
			throw new APException("undefined variable");
		} else if(tokenizer.hasNext()) {
			throw new APException("no end of line");
		} else {
			System.out.println(set.toString());
		}
	}
	
	/** @precondition  -
     *	@postcondition - The expression has been parsed and the result has been returned as a set.
     **/
	public SetInterface<BigInteger> expression() throws APException {
		SetInterface<BigInteger> set = term();
		if(set == null) {
			throw new APException("undefined variable");
		}
		while(nextCharIsAdditiveOperator()) {
			char operator = readNextChar();
			SetInterface<BigInteger> newTerm = term();
			if(newTerm == null) {
				throw new APException("undefined variable");
			}
			switch(operator) {
				case UNION : set.union(newTerm);
						break;
				case COMPLEMENT : set.complement(newTerm);
						break;
				case SYM_DIFFERENCE : set.symDifference(newTerm);
						break;
				default:
					throw new APException("Invalid additive operartor in expression");
			}
		}
		return set;
	}
	
	/** @precondition  -
     *	@postcondition - The term has been parsed and the result has been returned as a set.
     **/
	public SetInterface<BigInteger> term() throws APException {
		SetInterface<BigInteger> set = factor();
		if(set == null) {
			throw new APException("Invalid term");
		}
		while(nextCharIsMultiplicativeOperator()) {
			char operator = readNextChar();
			SetInterface<BigInteger> newFactor = factor();
			if(newFactor == null) {
				throw new APException("Invalid factor in term");
			} else {
				set.intersection(newFactor);
			}
		}
		return set;
	}
	
	/** @precondition  -
     *	@postcondition - The factor has been parsed and the result has been returned as a set.
     **/
	public SetInterface<BigInteger> factor() throws APException {
		SetInterface<BigInteger> set = new Set<>();
		if(nextCharIs(OPENING_CURLY_BRACE)) {
			set = readNextSet();
		} else if(nextCharIs(OPENING_SEMI_BRACE)) {
			readNextChar(); //reads the '('
			set = expression();
			if(!nextCharIs(CLOSING_SEMI_BRACE)) {
				throw new APException("missing parenthesis");
			}
			readNextChar(); //reads the ')'
		} else if(nextCharIsLetter()) {
			IdentifierInterface identifier = readNextIdentifier();
			if(identifier == null || !identifierSet.containsKey(identifier.getName())) {
				throw new APException("undefined variable");
			}
			set = identifierSet.get(identifier.getName()).copy();
		} else {
			throw new APException("incomplete statement");
		}
		return set;
	}
	
	/** @precondition  -
     *	@postcondition - returns the next character.
     **/
	public char readNextChar() {
		return tokenizer.next().charAt(0);
	}
	
	/** @precondition  -
     *	@postcondition - trims all the white space from input
     **/
	private void trim() {
		while(nextCharIsSpace() && tokenizer.hasNext()) { 
			tokenizer.next();
		}
	}
	
	/** @precondition  -
     *	@postcondition - trims all the white spaces and returns
     *					 TRUE - if the next character is c.
     *					 FALSE - if the next character is not c.
     **/
	public boolean nextCharIs(char c) {
		trim();
		return tokenizer.hasNext(Pattern.quote("" + c));
	}
	
	/** @precondition  -
     *	@postcondition - trims all the white spaces and returns
     *					 TRUE - if the next character is a letter.
     *					 FALSE - if the next character is not a letter.
     **/
	public boolean nextCharIsLetter() {
		trim();
		return tokenizer.hasNext("[a-zA-Z]");
	}

	/** @precondition  -
     *	@postcondition - trims all the white spaces and returns 
     *					 TRUE - if the next character is a digit.
     *					 FALSE - if the next character is not a digit.
     **/
	public boolean nextCharIsDigit() {
		trim();
		return tokenizer.hasNext("[0-9]");
	}
	
	/** @precondition  -
     *	@postcondition - returns 
     *					 TRUE - if the next character is a space or if there are no more characters left.
     *					 FALSE - otherwise
     **/
	public boolean nextCharIsSpace() {
		return tokenizer.hasNext(" ") || !tokenizer.hasNext(); 
	}
	
	/** @precondition  -
     *	@postcondition - return
     *					 TRUE - if the next character is a '+', '-', '|'.
     *					 FALSE - otherwise.
     **/
	public boolean nextCharIsAdditiveOperator() {
		return nextCharIs(UNION) || nextCharIs(COMPLEMENT) || nextCharIs(SYM_DIFFERENCE);
	}
	
	/** @precondition  -
     *	@postcondition - return
     *					 TRUE - if the next character is a '*'.
     *					 FALSE - otherwise.
     **/
	public boolean nextCharIsMultiplicativeOperator() {
		return nextCharIs(INTERSECTION);
	}
	
	/** @precondition  -
     *	@postcondition - the identifier name has been read and returned.
     **/
	public IdentifierInterface readNextIdentifier() throws APException {
		String token = "";
		if(!nextCharIsLetter()) {
			throw new APException("Identifier name should start with letter");
		}
		IdentifierInterface e = new Identifier<>();
		e.add(readNextChar());
		while(!nextCharIsSpace()) {
			if(nextCharIsLetter() || nextCharIsDigit()) {
				e.add(readNextChar());
			} else {
				break;
			}
		}
		
		return e;
	}
	
	/** @precondition  -
     *	@postcondition - a set has been read and returned
     **/
	public SetInterface<BigInteger> readNextSet() throws APException {
		boolean lastTokenWasComma = false;
		readNextChar(); //reads the '{'
		SetInterface<BigInteger> set = new Set<>();
		set.init();
		while(true) {
			if(nextCharIs(CLOSING_CURLY_BRACE) && !lastTokenWasComma) {
				break;
			} else {
					String token = "";
					if(!nextCharIsDigit()) {
						if(lastTokenWasComma) {
							throw new APException("number expected");
						} else {
							throw new APException("missing closing curly brace");
						}
					}
					while(nextCharIsDigit()) {
						token = token.concat(readNextChar() + "");
						if(nextCharIsSpace() && nextCharIsDigit()) {
							throw new APException("missing closing curly brace");
						}
					}
					if(token.length() > 1 && token.startsWith(ZERO + "")) {
						throw new APException("missing closing curly brace");
					}
					BigInteger number = new BigInteger(token);
					set.insert(number);
					lastTokenWasComma = false;
					if(nextCharIs(COMMA)) {
						readNextChar(); //reads the ','
						lastTokenWasComma = true;
						continue;
					}
			}
		}
		if(!nextCharIs(CLOSING_CURLY_BRACE)) { 
			throw new APException("missing closing curly brace");
		}
		readNextChar(); //reads the '}'
		return set;
	}
}

