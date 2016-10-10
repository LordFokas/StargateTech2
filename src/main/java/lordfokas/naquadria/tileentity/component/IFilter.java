package lordfokas.naquadria.tileentity.component;

public interface IFilter<T>{
	public static final IFilter ANY = new IFilter<Object>(){
		@Override public boolean matches(Object object) { return true; }
	};
	
	public static final IFilter NONE = new IFilter<Object>(){
		@Override public boolean matches(Object object) { return false; }
	};
	
	public boolean matches(T object);
}
