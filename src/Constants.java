import java.awt.Point;
import java.awt.Rectangle;

public class Constants {
	public static final int FULL_WINDOW_WIDTH = 960;
	public static final int FULL_WINDOW_HEIGHT = 1080;
	
	public static final int GAME_WINDOW_OFFSET_X = 103;
	public static final int GAME_WINDOW_OFFSET_Y = 85;
	public static final int GAME_WINDOW_WIDTH = 510;
	public static final int GAME_WINDOW_HEIGHT = 330;
	
	public static final int CHARACTER_CENTER_X = 356;
	public static final int CHARACTER_CENTER_Y = 247;
	
	public static final int INVENTORY_WINDOW_OFFSET_X = 655;
	public static final int INVENTORY_WINDOW_OFFSET_Y = 290;
	public static final int INVENTORY_WINDOW_WIDTH = 171;
	public static final int INVENTORY_WINDOW_HEIGHT = 350;
	public static final int INVENTORY_SLOT_WIDTH = 171 / 4;
	public static final int INVENTORY_SLOT_HEIGHT = 254 / 7;
	public static final int INVENTORY_NUM_ROWS = 4;
	public static final int INVENTORY_NUM_COLUMNS = 7;
	
	
	// ---------------------------------- ICONS ----------------------------------------------
	
	public static final int STATS_ICON_OFFSET_X = 660;
	public static final int STATS_ICON_OFFSET_Y = 250;
	public static final int STATS_ICON_WIDTH = 25;
	public static final int STATS_ICON_HEIGHT = 25;
	
	public static final int INVENTORY_ICON_OFFSET_X = 728;
	public static final int INVENTORY_ICON_OFFSET_Y = 250;
	public static final int INVENTORY_ICON_WIDTH = 25;
	public static final int INVENTORY_ICON_HEIGHT = 25;
	
	public static final int LOGOUT_ICON_OFFSET_X = 728;
	public static final int LOGOUT_ICON_OFFSET_Y = 528;
	public static final int LOGOUT_ICON_WIDTH = 25;
	public static final int LOGOUT_ICON_HEIGHT = 25;
	
	// ---------------------------------- STATS ----------------------------------------------
	
	public static final int MINING_XP_STAT_OFFSET_X = 777;
	public static final int MINING_XP_STAT_OFFSET_Y = 288;
	public static final int MINING_XP_STAT_WIDTH = 50;
	public static final int MINING_XP_STAT_HEIGHT = 23;
	
	public static Point getCharacterLocation() {
		return new Point(CHARACTER_CENTER_X, CHARACTER_CENTER_Y);
	}
	
	public static Rectangle getStatsIconRectangle() {
		return new Rectangle(STATS_ICON_OFFSET_X, STATS_ICON_OFFSET_Y, STATS_ICON_WIDTH, STATS_ICON_HEIGHT);
	}
	
	public static Rectangle getInventoryIconRectangle() {
		return new Rectangle(INVENTORY_ICON_OFFSET_X, INVENTORY_ICON_OFFSET_Y, INVENTORY_ICON_WIDTH, INVENTORY_ICON_HEIGHT);
	}
	
	public static Rectangle getLogoutIconRectangle() {
		return new Rectangle(LOGOUT_ICON_OFFSET_X, LOGOUT_ICON_OFFSET_Y, LOGOUT_ICON_WIDTH, LOGOUT_ICON_HEIGHT);
	}
	
	public static Rectangle getMiningXPRectangle() {
		return new Rectangle(MINING_XP_STAT_OFFSET_X, MINING_XP_STAT_OFFSET_Y, MINING_XP_STAT_WIDTH, MINING_XP_STAT_HEIGHT);
	}
}
