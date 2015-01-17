package lordfokas.stargatetech2.enemy.gui;

import lordfokas.stargatetech2.abstraction.gui.BaseGUI;
import lordfokas.stargatetech2.abstraction.gui.ElementCheckBox;
import lordfokas.stargatetech2.abstraction.gui.ElementListBox;
import lordfokas.stargatetech2.abstraction.gui.ElementTextBox;
import lordfokas.stargatetech2.abstraction.gui.ListBoxText;
import lordfokas.stargatetech2.api.shields.ShieldPermissions;
import lordfokas.stargatetech2.core.reference.TextureReference;
import lordfokas.stargatetech2.enemy.PacketExceptionsUpdate;
import lordfokas.stargatetech2.enemy.PacketPermissionsUpdate;
import lordfokas.stargatetech2.enemy.TileShieldController;
import cofh.core.gui.element.TabInfo;
import cofh.lib.gui.element.ElementButton;
import cofh.lib.gui.element.ElementFluidTank;
import cofh.lib.gui.element.listbox.IListBoxElement;

public class GUIShieldController extends BaseGUI {
	private static final String INFO = "Control who or what can go through the shields!\n\nOpen exceptions!\n\nControl how the Controller is controlled!";
	private TileShieldController shieldController;
	private ElementListBox listBox;
	private ElementTextBox text;
	private ElementCheckBox cofh, players, villagers, animals, monsters, vessels;
	private ElementButton add, rem;
	
	public GUIShieldController(ContainerShieldController container) {
		super(container, 200, 193, TextureReference.GUI_SHIELD_CONTROLLER);
		shieldController = container.controller;
		shieldController.permissionsUpdated = true;
		shieldController.exceptionsUpdated = true;
		title = "Shield Controller";
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int arg0, int arg1) {
		super.drawGuiContainerForegroundLayer(arg0, arg1);
		fontRendererObj.drawString("Permissions", 8, 24, 0x404040);
		fontRendererObj.drawString("Status", 8, 122, 0x404040);
		fontRendererObj.drawString("Exceptions", 103, 24, 0x404040);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		cofh = new ElementCheckBox(this, 8, 33, "PermCoFH", 244, 48, 232, 48, 12, "StargateTech2:textures/gui/shieldController.png");
		players = new ElementCheckBox(this, 8, 47, "PermOthers", 244, 48, 232, 48, 12, "StargateTech2:textures/gui/shieldController.png");
		villagers = new ElementCheckBox(this, 8, 61, "PermVill", 244, 48, 232, 48, 12, "StargateTech2:textures/gui/shieldController.png");
		animals = new ElementCheckBox(this, 8, 75, "PermAnimal", 244, 48, 232, 48, 12, "StargateTech2:textures/gui/shieldController.png");
		monsters = new ElementCheckBox(this, 8, 89, "PermMobs", 244, 48, 232, 48, 12, "StargateTech2:textures/gui/shieldController.png");
		vessels = new ElementCheckBox(this, 8, 103, "PermVess", 244, 48, 232, 48, 12, "StargateTech2:textures/gui/shieldController.png");
		
		ElementFluidTank tank = new ElementFluidTank(this, 9, 132, shieldController.tank);
		
		add = new ElementButton(this, 167, 33, "Add", 224, 0, 224, 16, 224, 32, 16, 16, "StargateTech2:textures/gui/shieldController.png");
		rem = new ElementButton(this, 184, 33, "Rem", 240, 0, 240, 16, 240, 32, 16, 16, "StargateTech2:textures/gui/shieldController.png");
		text = new ElementTextBox(this, 104, 34, 61, 14);
		text.setFilter(16, "abcdefghijklmnopqrstuvwxyz1234567890_").setAction("Add");
		listBox = new ElementListBox(this, 104, 51, 95, 141);
		listBox.selectedLineColor = 0xFF0F0F0F;
		
		addElement(cofh.setLabel("CoFH Friends"));
		addElement(players.setLabel("Other Players"));
		addElement(villagers.setLabel("Villagers"));
		addElement(animals.setLabel("Animals"));
		addElement(monsters.setLabel("Monsters"));
		addElement(vessels.setLabel("Vessels"));
		
		addElement(tank);
		
		addElement(add);
		addElement(rem);
		addElement(text);
		addElement(listBox);
		
		addTab(new TabInfo(this, INFO));
		
		updatePermissions();
	}
	
	@Override
	public void handleElementButtonClick(String button, int mouse) {
		int packetToSend = button.startsWith("Perm") ? 1 : 0;
		PacketPermissionsUpdate p = new PacketPermissionsUpdate();
		p.x = shieldController.xCoord;
		p.y = shieldController.yCoord;
		p.z = shieldController.zCoord;
		p.isSetting = (mouse == 1);
		PacketExceptionsUpdate e = new PacketExceptionsUpdate();
		e.x = shieldController.xCoord;
		e.y = shieldController.yCoord;
		e.z = shieldController.zCoord;
		
		if(button.equals("Add")){
			e.isSetting = true;
			e.playerName = text.getText();
			packetToSend = 2;
			text.clear();
		}else if(button.equals("Rem")){
			IListBoxElement selected = listBox.getSelectedElement();
			if(selected != null){
				e.isSetting = false;
				e.playerName = ((String)selected.getValue());
				packetToSend = 2;
			}
		}else if(button.equals("PermCoFH")){
			p.permissionFlag = ShieldPermissions.PERM_FRIEND;
		}else if(button.equals("PermOthers")){
			p.permissionFlag = ShieldPermissions.PERM_PLAYER;
		}else if(button.equals("PermVill")){
			p.permissionFlag = ShieldPermissions.PERM_VILLAGER;
		}else if(button.equals("PermAnimal")){
			p.permissionFlag = ShieldPermissions.PERM_ANIMAL;
		}else if(button.equals("PermMobs")){
			p.permissionFlag = ShieldPermissions.PERM_MONSTER;
		}else if(button.equals("PermVess")){
			p.permissionFlag = ShieldPermissions.PERM_VESSEL;
		}
		if(packetToSend == 1){
			p.sendToServer();
		}else if(packetToSend == 2){
			e.sendToServer();
		}
	}
	
	private void updatePermissions(){
		if(shieldController.exceptionsUpdated){
			shieldController.exceptionsUpdated = false;
			listBox.clear();
			for(String player : shieldController.getPermissions().getExceptionList()){
				listBox.add(new ListBoxText(player));
			}
		}
		if(shieldController.permissionsUpdated){
			shieldController.permissionsUpdated = false;
			ShieldPermissions perms = shieldController.getPermissions();
			cofh.setChecked(perms.hasBit(ShieldPermissions.PERM_FRIEND));
			players.setChecked(perms.hasBit(ShieldPermissions.PERM_PLAYER));
			villagers.setChecked(perms.hasBit(ShieldPermissions.PERM_VILLAGER));
			animals.setChecked(perms.hasBit(ShieldPermissions.PERM_ANIMAL));
			monsters.setChecked(perms.hasBit(ShieldPermissions.PERM_MONSTER));
			vessels.setChecked(perms.hasBit(ShieldPermissions.PERM_VESSEL));
		}
	}
	
	@Override
	protected void updateElementInformation() {
		super.updateElementInformation();
		
		updatePermissions();
		
		boolean exists = false;
		String text = this.text.getText();
		for(int i = 0; i < listBox.getElementCount(); i++){
			IListBoxElement element = listBox.getElement(i);
			if(text.equals(element.getValue())){
				exists = true;
				break;
			}
		}
		add.setEnabled(!exists && text.length() > 0);
		IListBoxElement element = null;
		if(listBox.getElementCount() > Math.max(0, listBox.getSelectedIndex())){
			element = listBox.getSelectedElement();
		}
		rem.setEnabled(element != null);
	}
}