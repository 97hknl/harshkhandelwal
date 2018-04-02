public class TokenImpl implements Token {
	private int type; 
	private String value;
	
	private final int NUMBER_TYPE = 1;
	private final int OPERATOR_TYPE = 2;
	private final int PARENTHESIS_TYPE = 3;
	private final int ERROR = -1;
	private final String OPERATORS = "-+/*^";
	private final String PARANTHESIS = "()";
	
	public TokenImpl() {}
	
	public TokenImpl(String element) {
		this.setValue(element);
		this.setType(getTokenType());
	}

	private void setType(int tokenType) {
		this.type = tokenType;
	}
	
	public String toString() {
		return "[" + getValue() + "] -> " + type;
	}
	@Override
	public String getValue() {
		return value;
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public int getPrecedence() {
		if(getType() == OPERATOR_TYPE) {
			return OPERATORS.indexOf(getValue()) / 2; 
			//returns 0 for + and -
			//returns 1 for * and /
			//returns 2 for ^
		}
		return ERROR;
	}
	
	private void setValue(String value) {
		this.value = value;
	}
	
	private int getTokenType() {
		String element = getValue();
		if(element == null) {
			return ERROR; //if the token is null then return -1(error)
		}
		int length = element.length();
		if(length == 1) {
			if(OPERATORS.contains(element)) {
				return OPERATOR_TYPE;
			} else if(PARANTHESIS.contains(element)) {
				return PARENTHESIS_TYPE;
			}
		}
		if(element.matches("^\\d+(\\.\\d+)?")) {
			return NUMBER_TYPE;
		}
		return ERROR; //if the token is not null or a number or a operator or a parenthesis, then return error
	}
	
	public Double applyOperatorToken(Double firstOperand, Double secondOperand) { 
		if(getType() != OPERATOR_TYPE) {
			return null;
		}
		Double result;
		switch(getValue().charAt(0)) {
			case '+' : result = secondOperand + firstOperand ;
				break;
			case '-' : result = secondOperand - firstOperand;
				break;
			case '*' : result = secondOperand * firstOperand;
				break;
			case '/' : result = secondOperand / firstOperand;
				break;
			case '^' : result = Math.pow(secondOperand, firstOperand);
				break;
			default : result = null;
		}
		return result;
	}
	
	public Double getDoubleValue() {
		if(getType() != NUMBER_TYPE) {
			return null;
		}
		return Double.parseDouble(getValue());
	}
}
