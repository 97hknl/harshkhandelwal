package pad.ijvm;

import java.util.Arrays;
public class Stack {
    java.util.Stack<Word> stack;

    public Stack() {
        stack = new java.util.Stack<>();
    }

    public int size() {
        return stack.size();
    }

    public Word pop() {
        return stack.pop();
    }
    
    public void push(Word element) {
    	stack.push(element);
    }

    public Word topOfStack() {
        return stack.peek();
    }

    public int[] getStackContents() {
        int array[] = new int[stack.size()];
        for(int i = 0; i < stack.size(); i++) {
            array[i] = stack.get(i).toInteger();
        }
        return array;
    }
    
    public String toString() {
    	return Arrays.toString(this.getStackContents()); 
    }
}
