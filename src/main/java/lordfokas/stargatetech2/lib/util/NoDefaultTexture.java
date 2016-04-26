package lordfokas.stargatetech2.lib.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker to suppress FileNotFound exceptions on the log.<br>
 * Items and Blocks annotated with this will instead attempt
 * to load a dummy texture.
 * 
 * @author LordFokas
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated // XXX can be removed
public @interface NoDefaultTexture {}
