package online.monkegame.monkebotplugin2;

import org.bukkit.plugin.java.*;
import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.scheduler.*;
import java.sql.*;
import java.util.UUID;



/*
 * made by monkegame team
 * Mrs_Herobrine_
 * contact here: mrsherobrinenaomi@gmail.com, mrsherobrine (naomi)#6263
 * play.monkegame.online
 */



public final class pluginClass extends JavaPlugin{
	
	
	
	public int playerkills;
	public String killslog;
	@Override
    public void onEnable() {
		this.saveDefaultConfig();
		
		final String databaseLocation = this.getConfig().getString("db.dblocation");
		final String databaseTable = this.getConfig().getString("db.dbtable");
		
		getLogger().info("Thanks for using/enabling monkebotplugin!");
		BukkitScheduler scheduler = getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() { 
		@Override
		@SuppressWarnings("deprecation")
		public void run() {
			getLogger().info("Getting info...");
			Bukkit.getServer().broadcastMessage("[monkebot] UPDATING THE DATABASE EXPECT SOME LAG");
			for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
				playerkills = player.getStatistic(Statistic.PLAYER_KILLS);
				killslog = Integer.toString(playerkills);
				String playerName = player.getName();
				UUID playerUuid = player.getUniqueId();
				getLogger().info(playerName + " has " + killslog + " kills.");
				
			        // SQLite connection string
			        String url = "jdbc:sqlite:" + databaseLocation;
			        // SQL statement for updating the table
			        String sql = "INSERT OR REPLACE INTO " + databaseTable + " (uuid, username, killcount)\n"
			        		+ "VALUES ('" + playerUuid + "','" + playerName + "','" + playerkills  + "');";
			            	try (Connection conn = DriverManager.getConnection(url);
					                Statement stmt = conn.createStatement()) {
					            	stmt.execute(sql);
					        } catch (SQLException e) {
					            System.out.println(e.getMessage());
					        }
			    }
			}
		}, 0L, 24000L );
	}

	@Override
    public void onDisable() {
		getLogger().info("[monkebot] database will no longer update periodically");
	}
}
