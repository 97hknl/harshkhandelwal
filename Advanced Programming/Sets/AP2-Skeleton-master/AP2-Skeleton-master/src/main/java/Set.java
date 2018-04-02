
public class Set<E extends Comparable> implements SetInterface<E> {
	private ListInterface<E> list;
	public Set() {
		list = new List<E>();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public SetInterface<E> init() {
		list = new List<E>();
		return this;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public SetInterface<E> union(SetInterface<E> set) {
		if(set.isEmpty()) {
			return this;
		}
		set.goToFirst();
		do {
			if(!contains(set.retrieve())) {
				this.insert(set.retrieve());
			}
		} while(set.goToNext());
		return this;
	}

	@Override
	public SetInterface<E> intersection(SetInterface<E> set) {
		if(this.isEmpty()) {
			return this;
		}
		this.goToFirst();
		int size = this.size();
		do {
			if(!set.contains(this.retrieve())) {
				this.remove();
			} else {
				list.goToNext();
			}
			size--;
		} while(size > 0);
		return this;
	}

	@Override
	public SetInterface<E> complement(SetInterface<E> set) {
		if(this.isEmpty()) {
			return this;
		}
		list.goToFirst();
		int size = this.size();
		do {
			if(set.contains(list.retrieve())) {
				list.remove();
			} else {
				list.goToNext();
			}
			size--;
		} while(size > 0);
		return this;
	}

	@Override
	public SetInterface<E> symDifference(SetInterface<E> set) {
		if(!set.isEmpty()) {
			set.goToFirst();
			do {
				if(!contains(set.retrieve())) {
					insert(set.retrieve());
				} else {
					remove();
				}
			} while(set.goToNext());
		}
		return this;
	}

	@Override
	public boolean goToFirst() {
		return list.goToFirst();
	}
	
	@Override
	public boolean goToLast() {
		return list.goToLast();
	}
	
	@Override
	public boolean goToNext() {
		return list.goToNext();
	}
	
	@Override
	public boolean goToPrevious() {
		return list.goToPrevious();
	}
	
	@Override
	public E retrieve() {
		return list.retrieve();
	}
	
	@Override
	public boolean contains(E d) {
		return list.find(d);
	}

	@Override
	public void insert(E d) {
		if(!contains(d)) {
			list.insert(d);
		}
	}
	
	@Override
	public void remove() {
		list.remove();
	}

	@Override
	public String toString() {
		if(this.isEmpty()) {
			return "";
		}
		list.goToFirst();
		String result = "";
		do {
			result += (list.retrieve().toString() + " ");
		} while(list.goToNext());
		result = result.trim();
		return result;
	}

	@Override
	public SetInterface<E> copy() {
		SetInterface<E> set = new Set<>();
		set.init();
		if(this.isEmpty()) {
			return set;
		}
		list.goToFirst();
		do {
			set.insert(list.retrieve());
		} while(list.goToNext());
		return set;
	}
	
}
