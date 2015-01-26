package lordfokas.stargatetech2.modules.automation;

public class AddressHelper {
	public static String convert(short address){
		String result = Integer.toHexString(address).toUpperCase();
		if(result.length() > 4){
			result = result.substring(4);
		}
		while(result.length() < 4){
			result = "0" + result;
		}
		return result;
	}
	
	public static short convert(String address) throws Exception{
		if(address.length() > 4) throw new Exception("Address too long! Max 4 chars.");
		return (short)(Integer.parseInt(address, 16) & 0x0000FFFF);
	}
}