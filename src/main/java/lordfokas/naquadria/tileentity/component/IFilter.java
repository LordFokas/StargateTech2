package lordfokas.naquadria.tileentity.component;

public interface IFilter<T>{
	public boolean matches(T object);
}
