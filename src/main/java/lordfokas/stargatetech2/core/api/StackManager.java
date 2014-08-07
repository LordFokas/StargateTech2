package lordfokas.stargatetech2.core.api;

import java.util.Collection;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import lordfokas.stargatetech2.api.IStackManager;

public class StackManager implements IStackManager{
	public static final StackManager instance = new StackManager();
	
	private HashMap<String, ItemStack> stacks = new HashMap();
	
	@Override
	public ItemStack get(String stack){
		ItemStack s = stacks.get(stack);
		if(s == null) return null;
		else return s.copy();
	}

	@Override
	public ItemStack get(String stack, int size){
		ItemStack s = get(stack);
		if(s != null && size >= 1 && size <= 64){
			s.stackSize = size;
		}
		return s;
	}

	@Override
	public Collection<String> getAllStacks(){
		return stacks.keySet();
	}
	
	public void addStack(String name, ItemStack stack){
		ItemStack s = stack.copy();
		s.stackSize = 1;
		stacks.put(name, s);
	}
}
