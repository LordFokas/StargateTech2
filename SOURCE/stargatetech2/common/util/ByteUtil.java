package stargatetech2.common.util;

public class ByteUtil {
	public static int readInt(byte[] array, int pointer){
		byte b0, b1, b2, b3;
		b0 = array[pointer + 0];
		b1 = array[pointer + 1];
		b2 = array[pointer + 2];
		b3 = array[pointer + 3];
		return  ((b0 & 0xFF) << 24) | ((b1 & 0xFF) << 16) | ((b2 & 0xFF) << 8) | (b3 & 0xFF);
	}
	
	public static void writeInt(int value, byte[] array, int pointer){
		array[pointer + 0] = (byte)((value & 0xFF000000) >> 24 );
		array[pointer + 1] = (byte)((value & 0x00FF0000) >> 16 );
		array[pointer + 2] = (byte)((value & 0x0000FF00) >> 8 );
		array[pointer + 3] = (byte) (value & 0x000000FF);
	}
}