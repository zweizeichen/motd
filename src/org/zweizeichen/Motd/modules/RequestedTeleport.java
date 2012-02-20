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

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zweizeichen.Motd.Motd;



public class RequestedTeleport implements CommandExecutor{

	// rtp request HashMap (Public cause of cleanup by MotdPlayerListener)
	public final HashMap<String, String> tpRequests = new HashMap<String, String>();
	
	private final Motd plugin;
	
	public RequestedTeleport(Motd motd) {
        this.plugin = motd;  
        
        // Register events for RequestedTeleportListener
        RequestedTeleportListener rtpListener = new RequestedTeleportListener(this);
    
        plugin.getServer().getPluginManager().registerEvents(rtpListener, plugin);
        
    }
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = (Player) sender;
		
		if (plugin.permissions.checkCommand("rtp_enabled", "motd.rtp.yes", player) || plugin.permissions.checkCommand("rtp_enabled", "motd.rtp.request", player)) {
			return requestTeleport(player, args);
		}
		
		player.sendMessage(ChatColor.RED + "Maybe you do not have the permission to do this.");
		return false;
	}
	
	// Request teleport Function
	private boolean requestTeleport(Player askingPlayer, String[] arg) {	
		
		// Check if we have no player name
		if (arg.length == 0) {
			
			return false;
			
		} else if (arg[0].equalsIgnoreCase("yes")) {
			
			// Look for teleport Requests
			if (tpRequests.containsKey(askingPlayer.getName())) {

				// Inform both players about teleport
				askingPlayer.sendMessage(ChatColor.YELLOW + "Teleporting " + tpRequests.get(askingPlayer.getDisplayName()) + " to you...");
				
				plugin.getServer().getPlayer(tpRequests.get(askingPlayer.getName())).sendMessage(ChatColor.YELLOW + "Teleporting you to " + askingPlayer.getDisplayName());

				// Now shit gets real
				plugin.getServer().getPlayer(tpRequests.get(askingPlayer.getName())).teleport(askingPlayer);

				// Clean up all the mess
				
				tpRequests.remove(askingPlayer.getName());
			} else {
				
				askingPlayer.sendMessage(ChatColor.BLUE + "You do not have any active teleport Requests.");
				
			}
			
			return true;
			
		} else if (!arg[0].equalsIgnoreCase(askingPlayer.getName())) {
			
			// Permissions Check
			if (plugin.permissionsEnabled && !plugin.permissions.checkCommand("rtp_enabled", "motd.rtp.request", askingPlayer)){
				return false;
			}
			
			// Check if player exists
			
			Player targetPlayer = plugin.getServer().getPlayer(arg[0]);

			if (targetPlayer == null) {
				askingPlayer.sendMessage(ChatColor.BLUE + "'" + arg[0] + "' is not a valid player!");
				return false;
			}

			// Check if another Request exists and let older request expire
			if (tpRequests.containsKey(targetPlayer.getName())) {

				// Notify both players about expiration
				plugin.getServer().getPlayer(tpRequests.get(targetPlayer.getName())).sendMessage(ChatColor.BLUE + "Your teleport request to " + targetPlayer.getDisplayName() + " has expired.");
				targetPlayer.sendMessage(ChatColor.BLUE + "" + tpRequests.get(targetPlayer) + "'s teleport request to you has expired.");

				// Clean tpRequest...
				tpRequests.remove(targetPlayer.getName());
			}

			// ...and add the new one!
			tpRequests.put(targetPlayer.getName(), askingPlayer.getName());

			// Ask for Teleport
			targetPlayer.sendMessage(ChatColor.YELLOW + askingPlayer.getDisplayName() + " wants to teleport to you. Type " + ChatColor.GREEN + "/rtp yes" + ChatColor.YELLOW + " to accept.");

			// Inform about request
			askingPlayer.sendMessage(ChatColor.YELLOW + "Your teleport request to " + targetPlayer.getDisplayName() + " has been created.");
			
			return true;
			
		} else {
			askingPlayer.sendMessage(ChatColor.BLUE + "Don't try to destroy teh internets by teleporting to yourself! :D");
			return true;
		}
	}
	
}
