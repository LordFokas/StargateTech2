package stargatetech2.core.machine.tabs;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;
import stargatetech2.core.base.BaseTab;
import stargatetech2.core.base.BaseGUI.ITab.TabColor;
import stargatetech2.core.machine.Face;
import stargatetech2.core.machine.FaceColor;
import stargatetech2.core.machine.TileEntityMachine;
import stargatetech2.core.packet.PacketToggleMachineFace;
import stargatetech2.core.reference.TextureReference;
import stargatetech2.core.util.IconRegistry;
import stargatetech2.core.util.Stacks;

public class TabConfiguration extends BaseTab{
	private static final Side[] sides = new Side[]{
		new Side(Face.BOTTOM, 42, 63),
		new Side(Face.TOP, 42, 27),
		new Side(Face.LEFT, 24, 45),
		new Side(Face.FRONT, 42, 45),
		new Side(Face.RIGHT, 60, 45),
		new Side(Face.BACK, 60, 63)
	};
	
	private static class Side{
		public final int x, y;
		public final Face face;
		
		public Side(Face face, int x, int y){
			this.face = face;
			this.x = x;
			this.y = y;
		}
	}
	
	private final Icon side, top, bottom;
	private final TileEntityMachine machine;
	
	public TabConfiguration(TileEntityMachine machine) {
		super("Configuration", Stacks.circuit, TabColor.RED);
		this.machine = machine;
		side = IconRegistry.blockIcons.get(TextureReference.MACHINE_SIDE_I);
		top = IconRegistry.blockIcons.get(TextureReference.MACHINE_TOP_I);
		bottom = IconRegistry.blockIcons.get(TextureReference.MACHINE_BOTTOM_I);
	}

	@Override
	public int getSizeY() {
		return 80;
	}

	@Override
	public void render() {
		for(int i = 0; i < 6; i++){
			Side s = sides[i];
			FaceColor color = machine.getColor(s.face);
			if(color == FaceColor.VOID){
				gui.drawIcon(s.x, s.y, machine.getBlockType().getIcon(i, 0), TextureMap.locationBlocksTexture, 16);
			}else{
				Icon icon;
				switch(s.face){
				case BOTTOM:
					icon = bottom;
					break;
				case TOP:
					icon = top;
					break;
				default:
					icon = side;
					break;
				}
				gui.drawIcon(s.x, s.y, icon, TextureMap.locationBlocksTexture, 16);
				gui.drawIcon(s.x, s.y, IconRegistry.blockIcons.get(color.getTexture()), TextureMap.locationBlocksTexture, 16);
			}
		}
	}
	
	@Override
	public boolean handleClick(int x, int y){
		for(Side side : sides){
			if(elementHit(side.x, side.y, x, y)){
				PacketToggleMachineFace packet = new PacketToggleMachineFace();
				packet.x = machine.xCoord;
				packet.y = machine.yCoord;
				packet.z = machine.zCoord;
				packet.face = side.face;
				packet.sendToServer();
				return false;
			}
		}
		return true;
	}
	
	private boolean elementHit(int ex, int ey, int cx, int cy){
		return cx >= ex && cx < ex + 16 && cy >= ey && cy < ey + 16;
	}
}
