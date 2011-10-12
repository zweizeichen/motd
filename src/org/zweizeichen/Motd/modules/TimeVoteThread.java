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


// Imports
import org.bukkit.ChatColor;

public class TimeVoteThread implements Runnable {
	public static TimeVote timeVote;
	public String callingPlayerName;

	public TimeVoteThread(TimeVote instance) {
		timeVote = instance;
	}
	@Override public void run() 
	{ 
		timeVote.plugin.getServer().broadcastMessage(ChatColor.BLUE + "The time vote has expired.");
		timeVote.timeVotes.clear();
	} 
}

