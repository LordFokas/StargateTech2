package stargatetech2.core.gui;

import java.util.ArrayList;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;

import org.lwjgl.opengl.GL11;

import stargatetech2.common.base.BaseContainer;
import stargatetech2.common.base.BaseGUI;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.core.packet.PacketExceptionsUpdate;
import stargatetech2.core.packet.PacketPermissionsUpdate;
import stargatetech2.core.tileentity.TileShieldEmitter;
import stargatetech2.core.util.IonizedParticles;
import stargatetech2.core.util.ShieldPermissions;

// TODO: add scrolling for the exception list. To be done later.
public class GUIShieldEmitter extends BaseGUI {
	private TileShieldEmitter shieldEmitter;
	private TankHoverHandler gauge = new TankHoverHandler();
	private TextHandler textHandler = new TextHandler();
	private ExceptionList exceptions = new ExceptionList();
	private ExceptionClickHandler addException;
	
	private class ExceptionList{
		public String[] names = new String[5];
		private int last = 0;
		private int lastMax = -1;
		
		public void lastInc(){
			if(last < lastMax) last++;
		}
		
		public void lastDec(){
			if(last > 0) last--;
		}
		
		public void buildFrom(ShieldPermissions perm){
			names = new String[5];
			ArrayList<String> ex = perm.getExceptionList();
			lastMax = ex.size() - 1;
			if(last > lastMax) last = lastMax;
			int s = last - 4;
			if(s < 0) s = 0;
			for(int i = 0; i < 5; i++){
				if(s+i > ex.size()-1) break;
				names[i] = ex.get(s+i);
			}
		}
	}
	
	private class PermissionClickHandler implements IClickHandler{
		private TileShieldEmitter emitter;
		private int permissionBit;
		
		public PermissionClickHandler(TileShieldEmitter tse, int permBit){
			emitter = tse;
			permissionBit = permBit;
		}
		
		@Override
		public void onClick(int x, int y) {
			ShieldPermissions perm = emitter.getPermissions();
			PacketPermissionsUpdate update = new PacketPermissionsUpdate();
			update.emitter = this.emitter;
			update.permissionFlag = this.permissionBit;
			update.isSetting = !perm.hasBit(permissionBit);
			update.sendToServer();
		}
	}
	
	private class ExceptionClickHandler implements IClickHandler{
		private TileShieldEmitter emitter;
		
		public ExceptionClickHandler(TileShieldEmitter tse){
			emitter = tse;
		}
		
		@Override
		public void onClick(int x, int y) {
			if(textHandler.getString(65).length() > 0){
				PacketExceptionsUpdate update = new PacketExceptionsUpdate();
				update.emitter = this.emitter;
				update.isSetting = true;
				update.playerName = textHandler.getString(65);
				update.sendToServer();
				textHandler = new TextHandler();
			}
		}
	}
	
	private class ExceptionRemoveCHandler implements IClickHandler{
		private TileShieldEmitter emitter;
		int slot;
		
		public ExceptionRemoveCHandler(TileShieldEmitter tse, int pos){
			emitter = tse;
			slot = pos;
		}
		
		@Override
		public void onClick(int x, int y) {
			String player = exceptions.names[slot];
			if(player != null){
				PacketExceptionsUpdate update = new PacketExceptionsUpdate();
				update.emitter = this.emitter;
				update.isSetting = false;
				update.playerName = player;
				update.sendToServer();
			}
		}
	}
	
	private class TankHoverHandler implements IHoverHandler{
		public boolean isHover = false;
		public int hoverX, hoverY;
		
		@Override
		public void onHover(int x, int y) {
			isHover = true;
			hoverX = x;
			hoverY = y;
		}

		@Override
		public void onLeave() {
			isHover = false;
		}
		
	}
	
	public GUIShieldEmitter(BaseContainer container) {
		super(container, 200, 100);
		shieldEmitter = (TileShieldEmitter) container.getTileEntity();
		bgImage = TextureReference.GUI_SHIELD_EMITTER;
		PermissionClickHandler player	= new PermissionClickHandler(shieldEmitter, ShieldPermissions.PERM_PLAYER);
		PermissionClickHandler villager = new PermissionClickHandler(shieldEmitter, ShieldPermissions.PERM_VILLAGER);
		PermissionClickHandler animal	= new PermissionClickHandler(shieldEmitter, ShieldPermissions.PERM_ANIMAL);
		PermissionClickHandler monster	= new PermissionClickHandler(shieldEmitter, ShieldPermissions.PERM_MONSTER);
		PermissionClickHandler minecart	= new PermissionClickHandler(shieldEmitter, ShieldPermissions.PERM_MINECART);
		addException = new ExceptionClickHandler(shieldEmitter);
		ExceptionRemoveCHandler remove0 = new ExceptionRemoveCHandler(shieldEmitter, 0);
		ExceptionRemoveCHandler remove1 = new ExceptionRemoveCHandler(shieldEmitter, 1);
		ExceptionRemoveCHandler remove2 = new ExceptionRemoveCHandler(shieldEmitter, 2);
		ExceptionRemoveCHandler remove3 = new ExceptionRemoveCHandler(shieldEmitter, 3);
		ExceptionRemoveCHandler remove4 = new ExceptionRemoveCHandler(shieldEmitter, 4);
		addClickHandler(player	, 32, 28, 8, 8);
		addClickHandler(villager, 32, 42, 8, 8);
		addClickHandler(animal	, 32, 56, 8, 8);
		addClickHandler(monster	, 32, 70, 8, 8);
		addClickHandler(minecart, 32, 84, 8, 8);
		addClickHandler(addException, 181, 12, 12, 12);
		addClickHandler(remove0, 101, 31, 8, 8);
		addClickHandler(remove1, 101, 43, 8, 8);
		addClickHandler(remove2, 101, 55, 8, 8);
		addClickHandler(remove3, 101, 67, 8, 8);
		addClickHandler(remove4, 101, 79, 8, 8);
		addHoverHandler(gauge, 8, 28, 16, 64);
	}
	
	@Override
	protected void drawForeground(){
		bindImage(TextureReference.getTexture("blocks/" + BlockReference.SHIELD_EMITTER + ".png"));
		drawQuad(4, 3.5F, 0, 1, 0, 1, 8, 8);
		drawLeft("Shield Emitter", 16, 4, 0x444444);
		drawLeft("Players", 42, 28, 0x444444);
		drawLeft("Villagers", 42, 42, 0x444444);
		drawLeft("Animals", 42, 56, 0x444444);
		drawLeft("Monsters", 42, 70, 0x444444);
		drawLeft("Minecarts", 42, 84, 0x444444);
		drawLeft(textHandler.getString(11), 100, 15, 0x444444);
		exceptions.buildFrom(shieldEmitter.getPermissions());
		int eY = 31;
		for(int i = 0; i < 5; i++){
			String player = exceptions.names[i];
			if(player == null) break;
			drawLeft(player, 111, eY, 0x444444);
			bindImage(bgImage);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			drawLocalQuad(101, eY, 248, 256, 72, 80, 8, 8);
			eY += 12;
		}
		
		float ions = (float) shieldEmitter.getIonAmount();
		float total = shieldEmitter.getTankInfo(null)[0].capacity;
		float fill = ions / total;
		Icon f = IonizedParticles.fluid.getIcon();
		bindImage(TextureMap.field_110575_b);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if(fill > 0.75F) drawQuad(8, 28, f.getMinU(), f.getMaxU(), f.getMinV(), f.getMaxV(), 16, 16);
		if(fill > 0.50F) drawQuad(8, 44, f.getMinU(), f.getMaxU(), f.getMinV(), f.getMaxV(), 16, 16);
		if(fill > 0.25F) drawQuad(8, 60, f.getMinU(), f.getMaxU(), f.getMinV(), f.getMaxV(), 16, 16);
		if(fill > 0.00F) drawQuad(8, 76, f.getMinU(), f.getMaxU(), f.getMinV(), f.getMaxV(), 16, 16);
		fill = 1F - fill;
		bindImage(bgImage);
		drawLocalQuad(8, 28, 8, 24, 28, 28 + (64F * fill), 16, 64F*fill);
		drawLocalQuad(8, 28, 240, 256, 0, 64, 16, 64);
		ShieldPermissions perm = shieldEmitter.getPermissions();
		if(perm.hasBit(ShieldPermissions.PERM_PLAYER))
			drawLocalQuad(32, 28, 248, 256, 64, 72, 8, 8);
		if(perm.hasBit(ShieldPermissions.PERM_VILLAGER))
			drawLocalQuad(32, 42, 248, 256, 64, 72, 8, 8);
		if(perm.hasBit(ShieldPermissions.PERM_ANIMAL))
			drawLocalQuad(32, 56, 248, 256, 64, 72, 8, 8);
		if(perm.hasBit(ShieldPermissions.PERM_MONSTER))
			drawLocalQuad(32, 70, 248, 256, 64, 72, 8, 8);
		if(perm.hasBit(ShieldPermissions.PERM_MINECART))
			drawLocalQuad(32, 84, 248, 256, 64, 72, 8, 8);
		if(gauge.isHover){
			int baseX = gauge.hoverX+2;
			int baseY = gauge.hoverY-26;
			String str = String.format("%d / %d", (int)ions, (int)total);
			drawLocalQuad(baseX, baseY, 0, 96, 100, 124, 96, 24);
			drawLeft("Ionized Particles", baseX+4, baseY+3, 0x0044FF);
			drawLeft(str, baseX+4, baseY+14, 0x0044FF);
		}
	}
	
	@Override
	protected void onKeyTyped(char key, int code){
		if(code == 28){
			addException.onClick(-1, -1);
		}else{
			textHandler.onKey(key, code);
		}
	}
}