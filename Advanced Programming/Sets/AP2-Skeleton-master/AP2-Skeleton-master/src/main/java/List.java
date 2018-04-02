public class List<E extends Comparable> implements ListInterface<E>{
	
	private Node current;
	private Node head;

    private class Node {

        E data;
        Node prior,
                next;

        public Node(E d) {
            this(d, null, null);
        }

        public Node(E data, Node prior, Node next) {
            this.data = data == null ? null : data;
            this.prior = prior;
            this.next = next;
        }

    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public ListInterface<E> init() {
        head = null;
        current = null;
        return this;
    }

    @Override
    public int size() {
        Node currentNode = head;
        int size = 0;
        while(currentNode != null) {
        	size++;
        	currentNode = currentNode.next;
        }
        return size;
    }

    @Override
    public ListInterface<E> insert(E d) {
        current = new Node(d, null, null);
    	if(this.isEmpty()) {
        	head = current;
        } else if(head.data.compareTo(d) >= 0) { //function required for ascending?
        	current.next = head;
        	head = current;
        } else {
        	Node currentNode = head;
        	Node lastNodeSeen = null;
        	while(currentNode != null && currentNode.data.compareTo(current.data) < 0) {
        		lastNodeSeen = currentNode;
        		currentNode = currentNode.next;
        	}
        	current.next = lastNodeSeen.next;
        	current.prior = lastNodeSeen;
        	if(lastNodeSeen.next != null) {
        		lastNodeSeen.next.prior = current;
        	}
        	lastNodeSeen.next = current;
        }
    	return this;
    }

    @Override
    public E retrieve() {
    	if(this.isEmpty()) {
    		return null;
    	}
        return current.data;
    }

    @Override
    public ListInterface<E> remove() {
    	if(this.isEmpty()) {
    		return null;
    	}
    	if(current == head) {
    		current = head = current.next;
    	} else if(current.next == null) {
    		current = current.prior;
    		current.next = null;
    	} else {
    		current.next.prior = current.prior;
    		current.prior.next = current.next;
    		current = current.next;
    	}
    	return this;
    }

    @Override
    public boolean find(E d) {
        current = head;
        while(current != null) {
        	if(current.data.equals(d)) {
        		return true;
        	} else if(current.data.compareTo(d) > 0) {
        		break;
        	}
        	current = current.next;
        }
        if(current != head && current != null) {
        	current = current.prior;
        }
        return false;
    }

    @Override
    public boolean goToFirst() {
        if(this.isEmpty()) {
        	return false;
        }
        current = head;
        return true;
    }

    @Override
    public boolean goToLast() {
    	if(this.isEmpty()) {
    		return false;
    	}
    	current = head;
    	while(current.next != null) {
    		current = current.next;
    	}
    	return true;
    }

    @Override
    public boolean goToNext() {
        if(this.isEmpty() || current.next == null) {
        	return false;
        }
        current = current.next;
        return true;
    }

    @Override
    public boolean goToPrevious() {
        if(this.isEmpty() || current.prior == null) {
        	return false;
        }
        current = current.prior;
        return true;
    }

    @Override
    public ListInterface<E> copy() {
        List<E> newList = new List<E>();
        newList.init();
        Node currentNode = head;
        while(currentNode != null) {
        	newList.insert(currentNode.data);
        	currentNode = currentNode.next;
        }
        return newList;
    }
}
