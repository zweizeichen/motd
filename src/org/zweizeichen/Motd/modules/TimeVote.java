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

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zweizeichen.Motd.Motd;

public class TimeVote implements CommandExecutor {

	final Motd plugin;

	// timevote time value
	private int timeValue;

	// timevote HashMap (Public cause of clear by TimeVoteThread)
	public final HashMap<String, Boolean> timeVotes = new HashMap<String, Boolean>();

	// timevote World String
	public String timeVoteWorldName;
	
	// TimeVoteThreadId
	public Integer timeVoteThreadId = -1;

	// Links the TimeVoteThread
	private final TimeVoteThread timeVoteThread = new TimeVoteThread(this);

	public TimeVote(Motd motd) {
		plugin = motd;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = (Player)sender;
		
		if (plugin.permissions.checkCommand("vtime_enabled", "motd.vtime.use", player)) {

			return timeVote(player, args);

		}
		
		player.sendMessage(ChatColor.RED + "Maybe you do not have the permission to do this.");
		return false;
	}

	// Timevote Function
	private boolean timeVote(Player player, String[] arg) {

		// Check if we have an arg
		if (arg.length == 0) {
			return false;
		}

		if (plugin.getServer().getScheduler().isCurrentlyRunning(timeVoteThreadId) && !arg[0].equalsIgnoreCase("yes")) {
			player.sendMessage(ChatColor.YELLOW + "There is already a timevote going on.");
		}

		else {

			// Set timeValue for names
			if (arg[0].equalsIgnoreCase("dawn")) {
				timeValue = 22000;
				return createTimeVote(player, arg[0]);
			} else if (arg[0].equalsIgnoreCase("day")) {
				timeValue = 0;
				return createTimeVote(player, arg[0]);
			} else if (arg[0].equalsIgnoreCase("evening")) {
				timeValue = 12500;
				return createTimeVote(player, arg[0]);
			} else if (arg[0].equalsIgnoreCase("night")) {
				timeValue = 14500;
				return createTimeVote(player, arg[0]);
			}
			// Handle votes
			else if (arg[0].equalsIgnoreCase("yes")) {

				// Check if there is a vote going on
				if (timeVotes.isEmpty()) {
					player.sendMessage(ChatColor.YELLOW + "There is no vote going on.");
					return true;
				}

				// Check if player already voted
				if (timeVotes.containsKey(player.getName())) {
					player.sendMessage(ChatColor.YELLOW + "You already voted for this time change.");
					return true;
				} else {
					// Collect vote data
					timeVotes.put(player.getName(), true);
					
					// Calculate votes left for time change
					float timeVotesLeft = ((plugin.getServer().getOnlinePlayers().length) / 2) - timeVotes.size() + 1;
					
					// Calculate percentage
					float proVotePercentage = ((100 / plugin.getServer().getOnlinePlayers().length)) * timeVotes.size();
					
					player.sendMessage(ChatColor.YELLOW + "You voted for a time change.");

					// Check if enough players voted for time change
					if (proVotePercentage > 50) {

						plugin.getServer().broadcastMessage(ChatColor.BLUE + "Vote passed! - Changing time now...");

						// Stop expiration timer and clear vote HashMap
						plugin.getServer().getScheduler().cancelTask(timeVoteThreadId);
						timeVotes.clear();

						// Set time
						World world = plugin.getServer().getWorld(timeVoteWorldName);
						world.setTime(timeValue);
					} else {
						
						// Make sure grammar is correct	
						if (timeVotesLeft == 1) {
							// If there need to be more votes, notify player
							plugin.getServer().broadcastMessage(ChatColor.YELLOW + player.getDisplayName() + " voted for a time change in " + timeVoteWorldName + ". " + (int) timeVotesLeft + " more vote needed to pass the vote.");
						} else {
							// If there need to be more votes, notify players
							plugin.getServer().broadcastMessage(ChatColor.YELLOW + player.getDisplayName() + " voted for a time change in " + timeVoteWorldName + ". " + (int) timeVotesLeft + " more votes needed to pass the vote.");
						}
						
					}
				}

			} else {
				return false;
			}

		}

		return true;
	}

	// Function for creating time votes
	public boolean createTimeVote(Player player, String timeName) {
		// Clear HashMap "timeVotes"
		timeVotes.clear();

		// Collect vote data
		float timeVotesLeft = (plugin.getServer().getOnlinePlayers().length) / 2;

		// Check if player is alone on the server
		if (timeVotesLeft < 1) {
			plugin.getServer().broadcastMessage(ChatColor.BLUE + player.getDisplayName() + ", since you are the only one on this server you get a free time change! :D");
			World world = player.getWorld();
			world.setTime(timeValue);
		}

		// Create vote and put player in
		else {
			timeVotes.put(player.getName(), true);

			// Set world to the player's world who started the vote, so the time
			// is not changed on the world of the last player who accepts the
			// vote

			timeVoteWorldName = player.getWorld().getName();

			timeVoteThreadId = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, timeVoteThread, 1200);

			plugin.getServer().broadcastMessage(ChatColor.YELLOW + player.getDisplayName() + " voted for a time change to " + ChatColor.GREEN + timeName + ChatColor.YELLOW + " in " + timeVoteWorldName + ". " + (int) timeVotesLeft + " more votes to pass the vote.");

			plugin.getServer().broadcastMessage(ChatColor.YELLOW + "Type " + ChatColor.GREEN + "/vtime yes" + ChatColor.YELLOW + " to vote for the time change.");

			plugin.getServer().broadcastMessage(ChatColor.YELLOW + "The vote will expire in 60 seconds.");
		}
		return true;
	}

}
