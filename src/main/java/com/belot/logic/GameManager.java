package com.belot.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.belot.main.Broadcaster;

public class GameManager {

	private static Map<String, Game> games = new HashMap<String, Game>();

	public static Game getGame(String gameName) {
		return games.get(gameName);
	}

	public static Set<String> getGameNames() {
		return games.keySet();
	}

	public static boolean beginGame(String gameName, String playerName) {
		if (games.keySet().contains(gameName)) {
			return false;
		}
		Game newGame = new Game(gameName);
		games.put(gameName, newGame);
		games.get(gameName).addPlayer(playerName);
		return true;
	}

	public static boolean joinGame(String gameName, String playerName) {
		if (getGameNames().contains(gameName)) {
			if (games.get(gameName).getPlayerCount() >= 4) {
				return false;
			}
			if (games.get(gameName).getPlayerNames().contains(playerName)) {
				return false;
			}
			games.get(gameName).addPlayer(playerName);
			Broadcaster.broadcast("");
			return true;
		} else {
			return false;
		}
	}

}
