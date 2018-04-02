/**	@elements : object of type string
 *	@structure : linear
 *	@domain : 	The object of type string begins with a letter and what follow could be letter or numbers.
 *				The object of type string is a  valid value for an identifier name.
 *	@constructor - Identifier();
 *	<dl>
 *		<dt><b>PRE-conditie</b><dd>		-
 *		<dt><b>POST-conditie</b><dd> 	The new Identifier-object is the empty name.
 * </dl>
 **/ 
public interface IdentifierInterface {
	
	/** @precondition  -
     *	@postcondition - the passed char c has been concatinated to the name-PRE
     *    				
     **/
	public void add(char c);
	/** @precondition  -
     *	@postcondition - the string name-PRE has been returned.
     *    				
     **/
	public String getName();
}
