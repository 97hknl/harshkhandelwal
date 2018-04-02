
public class Identifier<E extends Comparable> implements IdentifierInterface {
	
	private String name;
	public Identifier(){
		
	}
	@Override
	public void add(char c) {
		name += c;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
}
