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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.zweizeichen.Motd.Motd;

public class Config {

	
	// Register File and config
	private File configFile = new File("plugins/motd/motd.yml");
	private File configDirectory = new File("plugins/motd");
	private YamlConfiguration config;
	

	public Config(Motd motd) {
		// Do nothing
	}

	// Initialize and return the config file
	//
	
	public YamlConfiguration initConfig(int CONFIG_VERSION) {

		// Check for existence of the plugin folder

		if (!configDirectory.exists()) {
			configDirectory.mkdir();
		}

		// Initialize config
		if (configFile.exists()) {

			config = new YamlConfiguration();
			
			// Catch the dangerous stuff
			try {
				config.load(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

			// Create config file in folder of server
			System.out.println("There is no config file for motd - creating 'plugins/motd/motd.yml' ...");

			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(configFile.getCanonicalFile()));

				// Write standard config
				writer.write("# MOTD related stuff. See manual for further instructions on editing the config.");
				writer.newLine();
				writer.write("motd: '<yellow>Hello <green><playername>!<n>Online players (<numplayers>/<maxplayers>): <blue><who>'");
				writer.newLine();
				writer.write("motd_enabled: true");
				writer.newLine();
				writer.newLine();
				writer.write("# Who - It is formatted just like the MOTD.");
				writer.newLine();
				writer.write("who: '<green>Online (<numplayers>/<maxplayers>): <who>'");
				writer.newLine();
				writer.write("who_alone: '<green>You are the only one on the server.'");
				writer.newLine();
				writer.write("who_enabled: true");
				writer.newLine();
				writer.newLine();
				writer.write("# Rules - They are formatted just like the MOTD.");
				writer.newLine();
				writer.write("rules: 'Rules for <green>My Server<n><n><aqua>1. Do not grief!<n><aqua>2. Do not grief!<n><aqua>3. Do not grief!'");
				writer.newLine();
				writer.write("rules_enabled: false");
				writer.newLine();
				writer.newLine();
				writer.write("# Commands");
				writer.newLine();
				writer.write("motd_command_enabled: true");
				writer.newLine();
				writer.write("rtp_enabled: true");
				writer.newLine();
				writer.write("vtime_enabled: true");
				writer.newLine();
				writer.write("who_enabled: true");
				writer.newLine();
				writer.write("ip_enabled: true");
				writer.newLine();
				writer.newLine();
				writer.write("# Third party plugins");
				writer.newLine();
				writer.write("permissions_enabled: false");
				writer.newLine();
				writer.newLine();
				writer.write("# Do not change please");
				writer.newLine();
				writer.write("config_version: " + CONFIG_VERSION);
				writer.close();

			} catch (IOException e) {
				System.out.println(e.getLocalizedMessage());
			}

			config = new YamlConfiguration();
			// Catch the dangerous stuff
			try {
				config.load(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (!config.get("config_version").equals(CONFIG_VERSION) && !config.get("config_version").equals(2)) {
			// Check if config is up to date

			System.out.println("motd: ********** !IMPORTANT! **********");
			System.out.println("motd: Wrong version of config file detected.");
			System.out.println("motd: Backup and delete your current config file and let the plugin rebuild it automatically on the next start!");
			System.out.println("motd: Otherwise the plugin will start to behave strange :D");
			System.out.println("motd: ********** !IMPORTANT! **********");

		} else if (config.get("config_version").equals(2)) {
			// Try to migrate config from version 2 to version 3

			System.out.println("motd: Migrating your config to version 3...");

			String motd = config.get("motd").toString();
			String motd_enabled = config.get("motd_enabled").toString();
			String motd_command_enabled = config.get("motd_command_enabled").toString();
			String rtp_enabled = config.get("rtp_enabled").toString();
			String vtime_enabled = config.get("vtime_enabled").toString();
			String who_enabled = config.get("who_enabled").toString();
			String ip_enabled = config.get("ip_enabled").toString();

			try {
				configFile.delete();
				BufferedWriter writer = new BufferedWriter(new FileWriter(configFile.getCanonicalFile()));

				// Write standard config with saved values
				writer.write("# MOTD related stuff. See manual for further instructions on editing the config.");
				writer.newLine();
				writer.write("motd: '" + motd + "'");
				writer.newLine();
				writer.write("motd_enabled: " + motd_enabled);
				writer.newLine();
				writer.newLine();
				writer.write("# Who - It is formatted just like the MOTD.");
				writer.newLine();
				writer.write("who: '<green>Online (<numplayers>/<maxplayers>): <who>'");
				writer.newLine();
				writer.write("who_alone: '<green>You are the only one on the server.'");
				writer.newLine();
				writer.write("who_enabled: " + who_enabled);
				writer.newLine();
				writer.newLine();
				writer.write("# Rules - They are formatted just like the MOTD.");
				writer.newLine();
				writer.write("rules: 'Rules for <green>My Server<n><n><aqua>1. Do not grief!<n><aqua>2. Do not grief!<n><aqua>3. Do not grief!'");
				writer.newLine();
				writer.write("rules_enabled: false");
				writer.newLine();
				writer.newLine();
				writer.write("# Commands");
				writer.newLine();
				writer.write("motd_command_enabled: " + motd_command_enabled);
				writer.newLine();
				writer.write("rtp_enabled: " + rtp_enabled);
				writer.newLine();
				writer.write("vtime_enabled: " + vtime_enabled);
				writer.newLine();
				writer.write("ip_enabled: " + ip_enabled);
				writer.newLine();
				writer.newLine();
				writer.write("# Third party plugins");
				writer.newLine();
				writer.write("permissions_enabled: false");
				writer.newLine();
				writer.newLine();
				writer.write("# Do not change please");
				writer.newLine();
				writer.write("config_version: " + CONFIG_VERSION);
				writer.close();

			} catch (IOException e) {
				System.out.println(e.getLocalizedMessage());
			}

			// Reload config
			// -> Catch the dangerous stuff
			try {
				config.load(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Inform user about successful upgrade
			System.out.println("motd: Successfully migrated your config from version 2 to version 3.");
			System.out.println("motd: ---> New property 'rules' set to 'Rules for <green>My Server<n><n><aqua>1. Do not grief!<n><aqua>2. Do not grief!<n><aqua>3. Do not grief!'");
			System.out.println("motd: ---> New property 'rules_enabled' set to 'false'");
			System.out.println("motd: -----------------------------------------------");
			System.out.println("motd: ---> New property 'who' set to '<green>Online (<numplayers>/<maxplayers>):<who>'");
			System.out.println("motd: ---> New property 'who_alone' set to '<green>You are the only one on the server.'");
			System.out.println("motd: -----------------------------------------------");
			System.out.println("motd: ---> New property 'permissions_enabled' set to 'false'");
		}

		return config;
	}
	
	// Function for getting the config path (eg. for calls of config.reload(PATH)
	//
	
	public File getConfigFile(){
		return configFile;
	}
	

	// Function for editing the config file by a Comamnd
	// Example:
	// args[0] = edit
	// args[1] = motd_enabled
	// args[2] = true
	//

	public boolean editConfigByCommand(String[] args, Player player,
			YamlConfiguration config) {
		// Check if we have a true/false statement or a text
		if (args[2].equals("true")) {

			// Print to server log
			System.out.println("motd: " + player.getDisplayName() + " is setting " + args[1] + " to 'true'. (Boolean)");
			// Print to chat
			player.sendMessage(ChatColor.GREEN + "Setting " + args[1] + " to 'true'.");
			// Set config
			config.set(args[1], true);

		} else if (args[2].equals("false")) {

			// Print to server log
			System.out.println("motd: " + player.getDisplayName() + " is setting " + args[1] + " to 'false'. (Boolean)");
			// Print to chat
			player.sendMessage(ChatColor.GREEN + "Setting " + args[1] + " to 'false'.");
			// Set config
			config.set(args[1], false);

		} else {
			// Print to server log
			System.out.println("motd: " + player.getDisplayName() + " is setting " + args[1] + " to " + argumentsFrom(args, 2) + ". (String)");
			// Print to chat
			player.sendMessage(ChatColor.GREEN + "Setting " + args[1] + " to " + argumentsFrom(args, 2) + ".");
			// Set config
			config.set(args[1], argumentsFrom(args, 2));

		}

		// Print to server log
		System.out.println("motd: Saving new values...");
		// Print to chat
		player.sendMessage(ChatColor.GREEN + "Saving new values...");
		// Save new config
		// -> Catch the dangerous stuff
		try {
			config.save(getConfigFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Print to server log
		System.out.println("motd: Reloading config...");
		// Print to chat
		player.sendMessage(ChatColor.GREEN + "Reloading config...");
		// Reload config
		// -> Catch the dangerous stuff
		try {
			config.load(getConfigFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Print to server log
		System.out.println("motd: Done!");
		// Print to chat
		player.sendMessage(ChatColor.GREEN + "Done!");

		return true;
	}

	// ArrayHelperFunction
	// -> returns all the other values after a specific index
	private String argumentsFrom(String[] args, int from) {

		String outputString = "";

		for (int i = 0 + from; i < args.length; i++) {
			if (i==0 + from) outputString += args[i];
			else outputString += " " + args[i];
		}

		return outputString;
	}

}
