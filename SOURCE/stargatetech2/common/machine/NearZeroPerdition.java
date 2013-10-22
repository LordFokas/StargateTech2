package stargatetech2.common.machine;

import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PerditionCalculator;

/**
 * This actually doesn't work so well, because BC is a bit (ehem) retarded
 * and not only forces people to use their implementation of what should be
 * an interface, but also said implementation is crap.
 * 
 * The higher the amount of energy stored, the higher the precision loss,
 * leading to much much higher power loss (because if BC doesn't see any power
 * loss due to precision issues, it will make sure you lose power...)
 * 
 * This is my attempt at minimizing the side effects of BC's poor power system.
 * It does what it can to prevent any power loss, by exploiting
 * a hole in BC's code. It's not much, but it counts.
 * 
 * @author LordFokas
 */
public class NearZeroPerdition extends PerditionCalculator {
	private final float loss;
	
	public NearZeroPerdition(){
		this(0.001F);
	}
	
	public NearZeroPerdition(float perdition){
		loss = perdition;
	}
	
	@Override
	public float applyPerdition(PowerHandler powerHandler, float current, long ticksPassed) {
		current -= loss;
		if (current < 0) {
			current = 0;
		}
		return current;
	}
}
