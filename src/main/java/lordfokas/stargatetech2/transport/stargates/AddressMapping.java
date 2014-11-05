package lordfokas.stargatetech2.transport.stargates;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lordfokas.stargatetech2.api.stargate.Address;
import lordfokas.stargatetech2.api.stargate.Symbol;

public class AddressMapping {
	private final Address address;
	private final int d, x, y, z;
	
	public AddressMapping(Address address, int d, int x, int y, int z){
		this.address = address;
		this.d = d;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static AddressMapping readFromStream(DataInputStream stream) throws IOException{
		int l = stream.readInt();
		Symbol[] symbols = new Symbol[l];
		for(int i = 0; i < l; i++){
			symbols[i] = Symbol.values()[stream.readInt()];
		}
		int d = stream.readInt();
		int x = stream.readInt();
		int y = stream.readInt();
		int z = stream.readInt();
		return new AddressMapping(Address.create(symbols), d, x, y, z);
	}
	
	public void writeToStream(DataOutputStream stream) throws IOException{
		stream.writeInt(address.length());
		for(int i = 0; i < address.length(); i++){
			stream.writeInt(address.getSymbol(i).ordinal());
		}
		stream.writeInt(d);
		stream.writeInt(x);
		stream.writeInt(y);
		stream.writeInt(z);
	}
	
	public Address getAddress(){
		return address;
	}
	
	public int getDimension(){
		return d;
	}
	
	public int getXCoord(){
		return x;
	}
	
	public int getYCoord(){
		return y;
	}
	
	public int getZCoord(){
		return z;
	}
}