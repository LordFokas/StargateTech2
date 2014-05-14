package stargatetech2.api.stargate;

import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.Event;

@Cancelable
public class DialEvent extends Event {

	public final Address sourceAddress;
	public final Address destAddress;
	public final int duration;
	
	public DialEvent(Address src, Address dst, int dur) {
		sourceAddress = src;
		destAddress = dst;
		duration = dur;
	}
}
