/**	@elements : objects of type E
 *	@structure : linear
 *	@domain : 	The elements in the set are unique and are sorted monotonically increasing.
 *				All rows of elements of type E are valid values for a set.
 *       		
 *	@constructor - Set();
 *	<dl>
 *		<dt><b>PRE-conditie</b><dd>		-
 *		<dt><b>POST-conditie</b><dd> 	The new Set-object is the empty set.
 * </dl>
 **/

public interface SetInterface<E extends Comparable> {
	
	/**	@precondition -
     *  @postcondition - FALSE: set is not empty.
     *  				TRUE:  set is empty.
     **/
	boolean isEmpty();
	
	/** @precondition  -
     *	@postcondition - set-POST is empty and has been returned.
     **/
	SetInterface<E> init();
	
	/**	@precondition  -
     *	@postcondition - The number of elements in the set has been returned.
     **/
	int size();
	
	/**	@precondition  -
     *	@postcondition - TRUE:  The set contains the element d.
     *     				FALSE: set does not contain the element d.
     **/
	boolean contains(E d);
	
	/**	@precondition  - The set does not has the element d.
     *	@postcondition - The passed value is inserted to it's correct position in set such that the set-POST is sorted.
     **/
	void insert(E d);
	
	/**	@precondition  -
     *	@postcondition - set-POST has the union of set-PRE and the passed set.
     **/
	SetInterface<E> union(SetInterface<E> set);
	
	/**	@precondition  -
     *	@postcondition - set-POST has the intersection of set-PRE and the passed set.
     **/
	SetInterface<E> intersection(SetInterface<E> set);
	
	/**	@precondition  -
     *	@postcondition - set-POST has the complement of set-PRE and the passed set.
     **/
	SetInterface<E> complement(SetInterface<E> set);
	
	/**	@precondition  -
     *	@postcondition - set-POST has the symmetric difference of set-PRE and the passed set.
     **/
	SetInterface<E> symDifference(SetInterface<E> set);
	
	/**	@precondition  -
     *	@postcondition - a copy of the set-PRE has been returned.
     **/
	SetInterface<E> copy();
	
	/** @precondition  -
     *	@postcondition - FALSE: set is empty
     *    				TRUE:  iterator is now at the first element of the set
     **/
	boolean goToFirst();
	
	/** @precondition  -
     *	@postcondition - FALSE: set is empty
     *    				TRUE:  iterator is now at the next element
     **/
	boolean goToNext();
	
	/** @precondition  -
     *	@postcondition - FALSE: set is empty
     *    				TRUE:  iterator is now at the last element of the set
     **/
	boolean goToLast();
	
	/** @precondition  -
     *	@postcondition - FALSE: set is empty
     *    				TRUE:  iterator is now at the previous element
     **/
	boolean goToPrevious();
	
	/** @precondition  - The set is not empty.
     *	@postcondition -The value of the element pointed to by the iterator has been returned.
     */
	E retrieve();
	
	/** @precondition  - The set is not empty.
     * 	@postcondition - The element pointed to by the iterator of set-PRE is not present in set-POST.
     * 	    			iterator-POST points to
     *    					- if set-POST is empty
     *   						null
     *   					- if set-POST is not empty
     *     						if iterator-PRE was the last element of set-PRE
     *     							the last element of set-POST
     *     						else
     *     							the element after iterator-PRE
     **/
	void remove();
}
