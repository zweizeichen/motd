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

import org.bukkit.entity.Player;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.configuration.file.YamlConfiguration;

import org.zweizeichen.Motd.modules.*;


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
	public Config configFile;
	
	// superPerms support
	public Permissions permissions = new Permissions(this);
	public Boolean permissionsEnabled = false;
	
	// Register and initialize markup class
	public Markup markup = new Markup(this);

	@Override
	public void onDisable() {
		System.out.println("Motd Disabled");
	}

	@Override
	public void onEnable() {

		// Get the information from the yml file.
		PluginDescriptionFile pdfFile = this.getDescription();

		// Initialize the configuration
		configFile = new Config(this);
		config = configFile.initConfig(CONFIG_VERSION);
		
		// Set-up permission system properly
		permissionsEnabled = config.getBoolean("permissions_enabled");
		System.out.println(permissionsEnabled + " is enabled!");

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

	// Who list and greeting string generator

	public String onlineUser(Player player) {

		String onlineuser = "";

			// Compose string

			for (Player p : getServer().getOnlinePlayers()) {

				onlineuser += p.getDisplayName() + ", ";

			}

			// Get rid of the ,

			if (!onlineuser.equals("")) {
				onlineuser = onlineuser.substring(0, onlineuser.length() - 2);
			}

		return onlineuser;

	}
	
	// Check if permissions are set
	//
	
}
