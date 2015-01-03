package mcp.mobius.waila.api;

import java.awt.Dimension;
import java.awt.Point;


public interface IWailaTooltipRenderer {
	/**
	 * 
	 * @param params Array of string parameters as passed to the RENDER arg in the tooltip ({rendername,param1,param2,...})
	 * @param accessor A global accessor for TileEntities and Entities
	 * @return Dimension of the reserved area
	 */
	Dimension getSize(String[] params, IWailaCommonAccessor accessor);
	
	/**
	 * 
	 * @param params Array of string parameters as passed to the RENDER arg in the tooltip ({rendername,param1,param2,...})
	 * @param accessor A global accessor for TileEntities and Entities
	 * @param x Top left coordinate of the reserved area
	 * @param y Top left coordinate of the reserved area
	 */
	void      draw   (String[] params, IWailaCommonAccessor accessor, int x, int y);
}
