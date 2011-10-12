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

import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class RequestedTeleportListener extends PlayerListener{

	// Link rtp to instance of RequestedTeleport registering the listener
	private final RequestedTeleport rtp;
	
	public RequestedTeleportListener(RequestedTeleport instance) {
		rtp = instance;
	}
	
	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		// Clean up the HashMaps
		// Teleport requests
		if (rtp.tpRequests.containsKey(player.getDisplayName())) {
			rtp.tpRequests.remove(player.getDisplayName());
		}

		if (rtp.tpRequests.containsValue(player.getDisplayName())) {
			
			rtp.tpRequests.remove(rtp.tpRequests.get(player
					.getDisplayName()));

			for (Entry<String, String> currentValue : rtp.tpRequests
					.entrySet()) {
				if (currentValue.getValue().equals(player.getDisplayName())) {
					rtp.tpRequests.remove(currentValue.getKey());
				}
			}
		}

	}
	
}
