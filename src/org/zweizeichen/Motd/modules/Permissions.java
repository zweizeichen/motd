//  Copyright 2011 by Sebastian Wagner
//
//  This file is part of motd.
//
//    motd is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//  motd is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with motd.  If not, see <http://www.gnu.org/licenses/>.
//

package org.zweizeichen.Motd.modules;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.zweizeichen.Motd.Motd;

public class Permissions {
	
	// Add logger for console messages
	protected static final Logger logger = Logger.getLogger("Minecraft");
	
	// Constructor
	public Permissions(Motd motd) {
			this.plugin = motd;
	}
	
	// Register plugin
	private final Motd plugin;
	
	// Function for cehcking for simple commands	
	// Use superPerms for permissionsEnabled = true, default to config if not
	public boolean checkCommand(String configKey, String permissionNode, Player player) {
		
		if (plugin.permissionsEnabled){
			return checkPermission(permissionNode, player);
		}
		else {
			return plugin.config.getBoolean(configKey);
		}
	
	}
	
	// Permission check for superPerms
	public boolean checkPermission(String permissionPath, Player player) {
		
		if (player.isPermissionSet(permissionPath)) {
			
			if(player.hasPermission(permissionPath)) {
				return true;
			}
			else {
				return false;
			}
			
		}
		else {
			logger.log(Level.WARNING, "[motd] " + player.getDisplayName() + " tried to use " + permissionPath + " but there is no permission set with that path.");
		}
		
		return false;
	}

}
