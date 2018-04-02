/**
 * Token interface. 
 * @elements characters
 * @structure linear
 * @domain all rows of characters
 */
public interface Token {
    int     NUMBER_TYPE = 1,
            OPERATOR_TYPE = 2,
            PARENTHESIS_TYPE = 3;

    /**
     * @pre -
     * @post The value associated with this token has been returned a String.
     */
    String getValue();


    /**
     * @pre -
     * @post The type of this object, represented as an int, has been returned.
     */
    int getType();

    /**
     * @pre -
     * @post The precedence of the token, represented by an int, 
     * has been returned. Higher int's signify a higher precedence. 
     * If token type does not need a precedence,
     * the result of this method is -1.
     */
    int getPrecedence();
    
    /**
     * pre -
     * @return The double value of the current token if it is a number else return null
     */
    Double getDoubleValue();
    
    /**
     * @param Two operands of data type double
     * @return The result of operation on two numbers
     * pre-
     * @return The double value of the operation of that token on two input operands has been returned
     */
    Double applyOperatorToken(Double operand1, Double operand2);
}
