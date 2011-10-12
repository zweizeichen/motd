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

// Package
package org.zweizeichen.Motd;

// Imports

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.configuration.file.YamlConfiguration;

import org.zweizeichen.Motd.modules.*;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * Motd for Bukkit - Greeting, Request Teleport, Vote Time, IP and WhoList
 * 
 * @author zweizeichen
 */

// Starts the class
public class Motd extends JavaPlugin {

	// Multiworld-support for vTime
	public String vtimeWorldName;

	// Config version of this release
	private final int CONFIG_VERSION = 3;

	// Register config and config Module
	public YamlConfiguration config;
	public Config configModule;

	// Permissions support
	public PermissionHandler Permissions;
	public Boolean permissionsEnabled = false;

	@Override
	public void onDisable() {
		System.out.println("Motd Disabled");
	}

	@Override
	public void onEnable() {

		// Get the information from the yml file.
		PluginDescriptionFile pdfFile = this.getDescription();

		// Initialize the configuration
		configModule = new Config(this);
		config = configModule.initConfig(CONFIG_VERSION);

		// Register commands

		getCommand("ip").setExecutor(new Util(this));
		getCommand("who").setExecutor(new Who(this));
		getCommand("rules").setExecutor(new Rules(this));
		getCommand("motd").setExecutor(new MessageOfTheDay(this));
		getCommand("vtime").setExecutor(new TimeVote(this));
		getCommand("rtp").setExecutor(new RequestedTeleport(this));

		// Print that the plugin has been enabled!
		System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");

	}

	// Function for checking a player's permissions to use a command
	public boolean checkPermissions(String commandName, CommandSender sender) {

		// Make sure that the sender is a player
		if (sender instanceof Player) {

			Player player = (Player) sender;
			
			// When player is OP, always return true
			if (player.isOp()) {
				return true;
			}

			// Send message if command is not enabled
			if (!config.getBoolean(commandName + "_enabled")) {
				player.sendMessage(ChatColor.YELLOW + "/" + commandName + " is not enabled");
				return false;
			}

			// Send message if user is not permitted to use the command
			if (permissionsEnabled && !(Permissions.has(player, "motd." + commandName + ".use"))) {
				player.sendMessage(ChatColor.YELLOW + "You are not permitted to use /" + commandName + " (motd." + commandName + ".use)");
			}

			// Check for permission again and return
			if (permissionsEnabled && config.getBoolean(commandName + "_enabled") && Permissions.has(player, "motd." + commandName + ".use")) {
				return true;
			} else if (!permissionsEnabled && config.getBoolean(commandName + "_enabled")) {
				return true;
			}

		}

		return false;
	}

	// Function for checking a player's permissions to use a command and for a
	// certain node
	public boolean checkPermissions(String commandName, String permissionNode, CommandSender sender) {

		// Make sure that the sender is a player
		if (sender instanceof Player) {

			Player player = (Player) sender;

			// When player is OP, always return true
			if (player.isOp()) {
				return true;
			}

			// Send message if command is not enabled
			if (!config.getBoolean(commandName + "_enabled")) {
				player.sendMessage(ChatColor.YELLOW + "/" + commandName + " is not enabled");
				return false;
			}

			// Send message if user is not permitted to use the command
			if (permissionsEnabled && !(Permissions.has(player, permissionNode))) {
				player.sendMessage(ChatColor.YELLOW + "You are not permitted to use " + commandName + " (" + permissionNode + ")");
			}

			// Check for permission again and return
			if (permissionsEnabled && config.getBoolean(commandName + "_enabled") && Permissions.has(player, permissionNode)) {
				return true;
			} else if (!permissionsEnabled && config.getBoolean(commandName + "_enabled")) {
				return true;
			}

		}

		return false;
	}

	// Functions for silently checking permissions
	public boolean checkPermissionsSilent(String commandName, CommandSender sender) {

		// Make sure that the sender is a player
		if (sender instanceof Player) {

			Player player = (Player) sender;
			
			// When player is OP, always return true
			if (player.isOp()) {
				return true;
			}

			// Send message if command is not enabled
			if (!config.getBoolean(commandName + "_enabled")) {
				player.sendMessage(ChatColor.YELLOW + "/" + commandName + " is not enabled");
				return false;
			}

			// Check for permission again and return
			if (permissionsEnabled && config.getBoolean(commandName + "_enabled") && Permissions.has(player, "motd." + commandName + ".use")) {
				return true;
			} else if (!permissionsEnabled && config.getBoolean(commandName + "_enabled")) {
				return true;
			}

		}

		return false;
	}
	public boolean checkPermissionsSilent(String commandName, String permissionNode, CommandSender sender) {

		// Make sure that the sender is a player
		if (sender instanceof Player) {

			Player player = (Player) sender;

			// When player is OP, always return true
			if (player.isOp()) {
				return true;
			}

			// Send message if command is not enabled
			if (!config.getBoolean(commandName + "_enabled")) {
				player.sendMessage(ChatColor.YELLOW + "/" + commandName + " is not enabled");
				return false;
			}

			// Check for permission again and return
			if (permissionsEnabled && config.getBoolean(commandName + "_enabled") && Permissions.has(player, permissionNode)) {
				return true;
			} else if (!permissionsEnabled && config.getBoolean(commandName + "_enabled")) {
				return true;
			}

		}

		return false;
	}

	
	// Permissions initialization
	public void setupPermissions() {
		Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");

		if (this.Permissions == null) {
			if (test != null) {
				this.Permissions = ((Permissions) test).getHandler();
				permissionsEnabled = true;
				System.out.println("motd: Permissions support enabled.");
			} else {
				System.out.println("motd: Permissions system not detected - you have to edit the config manually!");
			}
		}
	}

	//
	// Public helper functions
	//
	// TODO Move helper functions to own class
	//
	public String replaceColors(String inputString) {
		String outputString;

		outputString = inputString.replace("<red>", ChatColor.RED.toString()).replace("<black>", ChatColor.BLACK.toString()).replace("<darkblue>", ChatColor.DARK_BLUE.toString()).replace("<darkaqua>", ChatColor.DARK_AQUA.toString()).replace("<darkgreen>", ChatColor.DARK_GREEN.toString()).replace("<darkred>", ChatColor.DARK_RED.toString()).replace("<darkpurple>", ChatColor.DARK_PURPLE.toString()).replace("<gold>", ChatColor.GOLD.toString()).replace("<gray>", ChatColor.GRAY.toString()).replace("<darkgray>", ChatColor.DARK_GRAY.toString()).replace("<blue>", ChatColor.BLUE.toString()).replace("<green>", ChatColor.GREEN.toString()).replace("<aqua>", ChatColor.AQUA.toString()).replace("<lightpurple>", ChatColor.LIGHT_PURPLE.toString()).replace("<yellow>", ChatColor.YELLOW.toString()).replace("<white>", ChatColor.WHITE.toString());

		return outputString;
	}

	public String replacePlaceholders(String inputString, Player player) {
		String outputString;

		// Output string formatting
		outputString = inputString.replace("<who>", onlineUser(player)).replace("<numplayers>", Integer.toString(getServer().getOnlinePlayers().length)).replace("<maxplayers>", Integer.toString(getServer().getMaxPlayers())).replace("<playername>", player.getDisplayName());

		return outputString;
	}

	public String replaceUmlaute(String inputString) {
		String outputString;

		// Output string formatting
		outputString = inputString.replace("<ae>", "ä").replace("<Ae>", "Ä").replace("<oe>", "ö").replace("<Oe>", "Ö").replace("<ue>", "ü").replace("<Ue>", "Ü").replace("<oslash>", "ø").replace("<Oslash>", "Ø").replace("<aring>", "å").replace("<Aring>", "Å").replace("<aelig>", "æ").replace("<AElig>", "Æ");

		return outputString;
	}

	// Who list and greeting string generator

	public String onlineUser(Player player) {

		String onlineuser = "";

		// If permissions are enabled, use prefixes and/or suffixes of group

		if (permissionsEnabled) {

			for (Player p : getServer().getOnlinePlayers()) {

				//
				// Get around the ADMINzweizeichen / [ADMIN ] zweizeichen issue
				//
				String prefixSpace;
				String suffixSpace;

				if (getPlayerPrefix(p).isEmpty() || getPlayerPrefix(p).matches("\u00A7$2")) {
					prefixSpace = "";
				} else {
					prefixSpace = " ";
				}

				if (getPlayerSuffix(p).isEmpty() || getPlayerPrefix(p).matches("\u00A7$2")) {
					suffixSpace = "";
				} else {
					suffixSpace = " ";
				}

				// Compose String

				onlineuser += getPlayerPrefix(p) + prefixSpace + p.getDisplayName() + suffixSpace + getPlayerSuffix(p) + ChatColor.WHITE + ", ";

			}

			// Get rid of the ,

			if (!onlineuser.equals("")) {
				onlineuser = onlineuser.substring(0, onlineuser.length() - 2);
			}

		} else {
			for (Player p : getServer().getOnlinePlayers()) {

				onlineuser += p.getDisplayName() + ", ";

			}

			// Get rid of the ,

			if (!onlineuser.equals("")) {
				onlineuser = onlineuser.substring(0, onlineuser.length() - 2);
			}
		}

		return onlineuser;

	}

	// PermissionsHelperFunctions
	//
	// DEPRECATION ALERT! --- DEPRECATION ALERT! --- DEPRECATION ALERT! ---
	// DEPRECATION ALERT! --- DEPRECATION ALERT! --- DEPRECATION ALERT! ---
	// DEPRECATION ALERT! --- DEPRECATION ALERT! --- DEPRECATION ALERT! ---
	//
	// We are replacing the color-codes with '*.replaceAll("(&([a-f0-9]))",
	// "\u00A7$2")'
	//
	// -> gives player's group
	@SuppressWarnings("deprecation")
	public String getPlayerGroup(Player p) {
		String output = Permissions.getGroup(p.getWorld().getName(), p.getName());
		return output;
	}

	// -> gives player's group prefix
	@SuppressWarnings("deprecation")
	public String getPlayerPrefix(Player player) {
		// get the prefix from the Permissions config
		String playerPrefix = Permissions.getUserPermissionString(player.getWorld().getName(), player.getName(), "prefix");

		if (playerPrefix != null && ! playerPrefix.isEmpty()) {
			return playerPrefix.replaceAll("(&([a-f0-9]))", "\u00A7$2");
		}

		// get the name of the group and check if it's not null
		String groupName = Permissions.getGroup(player.getWorld().getName(), player.getName());

		if (groupName == null) {
			return "";
		}

		String groupPrefix = Permissions.getGroupPrefix(player.getWorld().getName(), groupName);
		return groupPrefix.replaceAll("(&([a-f0-9]))", "\u00A7$2");

	}

	// -> gives player's group suffix
	@SuppressWarnings("deprecation")
	public String getPlayerSuffix(Player player) {
		// get the suffix from the Permissions config
		String playerSuffix = Permissions.getUserPermissionString(player.getWorld().getName(), player.getName(), "suffix");

		if (playerSuffix != null && !playerSuffix.isEmpty()) {
			return playerSuffix.replaceAll("(&([a-f0-9]))", "\u00A7$2");
		}

		// get the name of the group and check if it's not null
		String groupName = Permissions.getGroup(player.getWorld().getName(), player.getName());

		if (groupName == null) {
			return "";
		}

		String groupSuffix = Permissions.getGroupSuffix(player.getWorld().getName(), groupName);
		return groupSuffix.replaceAll("(&([a-f0-9]))", "\u00A7$2");
	}

	// DEPRECATION ALERT! --- DEPRECATION ALERT! --- DEPRECATION ALERT! ---
	// DEPRECATION ALERT! --- DEPRECATION ALERT! --- DEPRECATION ALERT! ---
	// DEPRECATION ALERT! --- DEPRECATION ALERT! --- DEPRECATION ALERT! ---

}
