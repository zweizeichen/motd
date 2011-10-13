package org.zweizeichen.Motd.modules;

import org.bukkit.entity.Player;
import org.zweizeichen.Motd.Motd;

public class Permissions {
	
	// Constructor
	public Permissions(Motd motd) {
			this.plugin = motd;
	}
	
	// Register plugin
	private final Motd plugin;
		
	public boolean checkCommand (String commandName, Player player) {
	
		return true;
	}
	
	public boolean checkPermission (String permissionPath, Player player) {
		
		return true;
	}

}
