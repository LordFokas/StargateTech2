package lordfokas.naquadria.tileentity.component;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import lordfokas.naquadria.tileentity.TileEntityMachine;

/** This interface currently doesn't add anything,
 * but it exists for semantic reasons.
 * 
 * It represents any component available to {@link TileEntityMachine}s
 */
public interface ITileComponent extends INBTSerializable<NBTTagCompound>{}
