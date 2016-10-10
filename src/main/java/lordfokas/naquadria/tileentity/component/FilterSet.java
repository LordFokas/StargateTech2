package lordfokas.naquadria.tileentity.component;

import java.util.ArrayList;

public abstract class FilterSet<C extends FilterSet<C, T>, T> implements IFilter<T>{
	protected ArrayList<IFilter<T>> filters = new ArrayList();
	
	public C with(IFilter<T> ... filters){
		for(IFilter<T> filter : filters)
			this.filters.add(filter);
		return (C) this;
	}
	
	public static class Any<T> extends FilterSet<Any<T>, T>{
		@Override
		public boolean matches(T object) {
			for(IFilter<T> filter : filters)
				if(filter.matches(object))
					return true;
			return false;
		}
	}
	
	public static class All<T> extends FilterSet<All<T>, T>{
		@Override
		public boolean matches(T object) {
			for(IFilter<T> filter : filters)
				if(!filter.matches(object))
					return false;
			return true;
		}
	}
}
