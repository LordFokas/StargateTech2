package stargatetech2.core.gui;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import stargatetech2.api.shields.ShieldPermissions;
import stargatetech2.common.base.BaseGUI;
import stargatetech2.common.base.BaseGauge.TankGauge;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.core.packet.PacketExceptionsUpdate;
import stargatetech2.core.packet.PacketPermissionsUpdate;
import stargatetech2.core.tileentity.TileShieldEmitter;

public class GUIShieldEmitter extends BaseGUI {
	private TileShieldEmitter shieldEmitter;
	private TankGauge ionTank;
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
	
	
	
	public GUIShieldEmitter(ContainerShieldEmitter container) {
		super(container, 200, 100, true);
		shieldEmitter = container.emitter;
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
		ionTank = new TankGauge(8, 28, 64000);
		addGauge(ionTank);
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
			drawLocalQuad(101, eY, 248, 256, 8, 16, 8, 8);
			eY += 12;
		}
		bindImage(bgImage);
		ShieldPermissions perm = shieldEmitter.getPermissions();
		if(perm.hasBit(ShieldPermissions.PERM_PLAYER))		drawLocalQuad(32, 28, 248, 256, 0, 8, 8, 8);
		if(perm.hasBit(ShieldPermissions.PERM_VILLAGER))	drawLocalQuad(32, 42, 248, 256, 0, 8, 8, 8);
		if(perm.hasBit(ShieldPermissions.PERM_ANIMAL))		drawLocalQuad(32, 56, 248, 256, 0, 8, 8, 8);
		if(perm.hasBit(ShieldPermissions.PERM_MONSTER))		drawLocalQuad(32, 70, 248, 256, 0, 8, 8, 8);
		if(perm.hasBit(ShieldPermissions.PERM_MINECART))	drawLocalQuad(32, 84, 248, 256, 0, 8, 8, 8);
	}
	
	@Override
	protected void updateGauges(){
		ionTank.setCurrentValue(shieldEmitter.getIonAmount());
	}
	
	@Override
	protected void onKeyTyped(char key, int code){
		if(code == 28){
			addException.onClick(-1, -1);
		}else if(code == 200){
			exceptions.lastDec();
		}else if(code == 208){
			exceptions.lastInc();
		}else{
			textHandler.onKey(key, code);
		}
	}
	
	@Override
	protected void processMouseEvents(){
		int wheel = Mouse.getDWheel();
		if(wheel < 0){
			exceptions.lastInc();
		}else if(wheel > 0){
			exceptions.lastDec();
		}
	}
}