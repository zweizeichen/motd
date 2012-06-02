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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zweizeichen.Motd.Motd;

public class MessageOfTheDay implements CommandExecutor{

	private final Motd plugin;
	
	public MessageOfTheDay(Motd motd) {
        this.plugin = motd;  
        
        // Register events for MessageOfTheDayListener
        MessageOfTheDayListener motdListener = new MessageOfTheDayListener(this);
    
        plugin.getServer().getPluginManager().registerEvents(motdListener, plugin);
    }
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			System.out.println("motd: Sender is not a player! args[0] is " + args[0]);
			return false;
		}
		
		Player player = (Player) sender;
		
		// Check if we have any args
		if (args.length == 0 && plugin.permissions.checkCommand("motd_command_enabled","motd.motd.use", player)) {
			return showMotd(player);
		}
		else if (args.length == 0 && !plugin.permissions.checkCommand("motd_command_enabled","motd.motd.use", player)){
			player.sendMessage(ChatColor.RED + "You do not have the permission to do this.");
			return true;
		}
		
		// If we have args, this section will be executed (in case Permissions are in effect)
		if (plugin.permissionsEnabled) {
			
			if (args[0].equalsIgnoreCase("edit") && plugin.permissions.checkPermission("motd.edit", player)) {
				Config configHelper = new Config(plugin);
				return configHelper.editConfigByCommand(args, player, plugin.config);
			}
			else if (args[0].equalsIgnoreCase("reload") && plugin.permissions.checkPermission("motd.reload", player)) {
				
				// Save and reload config
				System.out.println("motd: " + player.getDisplayName() + " called command '/motd reload'.");
				System.out.println("motd: Reloading config...");
				
				// Reload config
				// -> Catch the dangerous stuff
				try {
					plugin.config.load(plugin.configFile.getConfigFile());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				System.out.println("motd: Config reloaded!");
				player.sendMessage(ChatColor.GRAY + "motd: Config reloaded!");
				
				return true;
			}
			else if(!(plugin.permissions.checkPermission("motd.reload", player) || plugin.permissions.checkPermission("motd.edit", player)))
			{
				player.sendMessage(ChatColor.RED + "You do not have the permission to do this.");
				return true;
			}

		}
		
		player.sendMessage(ChatColor.RED + "Maybe you do not have the permission to do this.");
		return false;
	}
	
	// Public motd function (cause of /motd command)
	public boolean showMotd (Player player) {
		if (plugin.config.getBoolean("motd_enabled")) {
			String motdString;

			// Get motd from config
			motdString = plugin.config.getString("motd");

			// Make newlines
			for (String motdStringSplitted : motdString.split("<n>")) {
				// Clean \n and replace colors / placeholders
				player.sendMessage(plugin.markup.markupAll(motdStringSplitted, player));
			}
		}
		return true;
	}
}
