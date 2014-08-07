package lordfokas.stargatetech2.factory.util;

import net.minecraft.nbt.NBTTagCompound;

public abstract class Buffer<B extends Buffer, C> {
	public enum BufferType{
		ENERGY, FLUID, ITEM;
	}
	
	public final BufferType bufferType;
	
	public Buffer(BufferType type){
		this.bufferType = type;
	}
	
	public abstract void transferTo(B buffer);
	public abstract void writeToNBT(NBTTagCompound nbt);
	public abstract void readFromNBT(NBTTagCompound nbt);
	public abstract float getFill();
	public abstract C getContainer();
}
