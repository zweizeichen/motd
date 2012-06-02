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

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MessageOfTheDayListener implements Listener{

	// Link messageOfTheDay to instance of MessageOfTheDay registering the listener
	private final MessageOfTheDay messageOfTheDay;
	
	public MessageOfTheDayListener(MessageOfTheDay instance) {
		messageOfTheDay = instance;
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerJoin (PlayerJoinEvent event)	 {
		messageOfTheDay.showMotd(event.getPlayer());
	}
	
}
