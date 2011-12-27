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
