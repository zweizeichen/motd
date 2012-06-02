//  Copyright 2012 by Sebastian Wagner
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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.zweizeichen.Motd.Motd;

public class Markup {
	
	// Constructor
		public Markup(Motd motd) {
				this.plugin = motd;
		}
		
		// Register plugin
		private final Motd plugin;
		
		//
		// Public markup helper functions
		//
		public String replaceColors(String inputString) {
			String outputString;

			outputString = inputString.replace("<red>", ChatColor.RED.toString()).replace("<black>", ChatColor.BLACK.toString()).replace("<darkblue>", ChatColor.DARK_BLUE.toString()).replace("<darkaqua>", ChatColor.DARK_AQUA.toString()).replace("<darkgreen>", ChatColor.DARK_GREEN.toString()).replace("<darkred>", ChatColor.DARK_RED.toString()).replace("<darkpurple>", ChatColor.DARK_PURPLE.toString()).replace("<gold>", ChatColor.GOLD.toString()).replace("<gray>", ChatColor.GRAY.toString()).replace("<darkgray>", ChatColor.DARK_GRAY.toString()).replace("<blue>", ChatColor.BLUE.toString()).replace("<green>", ChatColor.GREEN.toString()).replace("<aqua>", ChatColor.AQUA.toString()).replace("<lightpurple>", ChatColor.LIGHT_PURPLE.toString()).replace("<yellow>", ChatColor.YELLOW.toString()).replace("<white>", ChatColor.WHITE.toString());

			return outputString;
		}

		public String replacePlaceholders(String inputString, Player player) {
			String outputString;

			// Output string formatting
			outputString = inputString.replace("<who>", plugin.onlineUser(player)).replace("<numplayers>", Integer.toString(plugin.getServer().getOnlinePlayers().length)).replace("<maxplayers>", Integer.toString(plugin.getServer().getMaxPlayers())).replace("<playername>", player.getDisplayName());

			return outputString;
		}

		public String replaceUmlaute(String inputString) {
			String outputString;

			// Output string formatting
			outputString = inputString.replace("<ae>", "ä").replace("<Ae>", "Ä").replace("<oe>", "ö").replace("<Oe>", "Ö").replace("<ue>", "ü").replace("<Ue>", "Ü").replace("<oslash>", "ø").replace("<Oslash>", "Ø").replace("<aring>", "å").replace("<Aring>", "Å").replace("<aelig>", "æ").replace("<AElig>", "Æ");

			return outputString;
		}

		public String markupAll (String inputString, Player player) {
			String outputString;
			
			// Output string formatting
			outputString = replaceUmlaute(replacePlaceholders(replaceColors(inputString).replaceAll("<n>", ""), player));
			
			return outputString;
		}
}
