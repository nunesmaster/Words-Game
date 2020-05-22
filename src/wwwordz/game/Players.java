package wwwordz.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wwwordz.shared.WWWordzException;

/**
 * 
 * Persistent collection of players indexed by nick. Each player has nick,
 * password, points and accumulated points. Data is persisted using
 * serialization and backup each time a new user is created or points are
 * changed.
 */
public class Players extends Object implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Players players = null;
	private static HashMap<String, Player> playersMap = new HashMap<String, Player>();
	private static String str = "players.ser";
	private static File home = new File(System.getProperty("user.dir"));
	private static File playersFile = new File(home, str);

	/**
	 * add points to the player
	 * 
	 * @param nick
	 * @param points
	 * @throws WWWordzException
	 *             the player doesn't exists
	 */
	public void addPoints(String nick, int points) throws WWWordzException {

		if (!playersMap.containsKey(nick)) {
			throw new WWWordzException("That player doesn't exist");
		}

		else {
			Player name = playersMap.get(nick);
			name.setPoints(points);
			name.setAccumulated(name.getAccumulated() + points);
			backup(players);
		}
	}

	/**
	 * Clear the players file and remove all the nicks from the hashmap
	 */
	public void cleanup() {
		List<String> listPlayers = new ArrayList<String>(playersMap.keySet());
		playersFile.delete();
		players = null;
		for (String nick : listPlayers)
			playersMap.remove(nick);
	}

	/**
	 * Current home directory, where the data file is stored
	 * 
	 * @return
	 */
	public static File getHome() {
		return Players.home;
	}

	/**
	 * Get single instance of this class
	 * 
	 * @return
	 */
	public static Players getInstance() {
		if (restore() == null) {
			players = new Players();
		} else {
			players = restore();
		}
		return players;
	}

	/**
	 * get the players
	 * 
	 * @param nick
	 * @return player
	 */
	public Player getPlayer(String nick) {
		Player player = playersMap.get(nick);
		return player;
	}

	/**
	 * Reset points of the round
	 * 
	 * @param nick
	 * @throws WWWordzException
	 */
	public void resetPoints(String nick) throws WWWordzException {
		addPoints(nick, 0);
		backup(players);
	}

	/**
	 * Update home directory, where the data file is stored
	 * 
	 * @param home
	 */
	public void setHome(File home) {
		Players.home = home;
		Players.playersFile = new File(home, str);
	}

	/**
	 * Verify player's password.
	 * 
	 * @param nick
	 * @param password
	 * @return
	 */
	public boolean verify(String nick, String password) {

		if (!playersMap.containsKey(nick)) {
			Player newPlayer = new Player(nick, password);
			playersMap.put(nick, newPlayer);
			backup(players);
			return true;
		} else {
			Player player = playersMap.get(nick);
			if (player.getPassword() == password)
				return true;
			return false;
		}
	}

	/**
	 * restores the file to the previus instance backup
	 * 
	 * @return players
	 */
	private static Players restore() {

		if (playersFile.canRead()) {
			try (FileInputStream stream = new FileInputStream(playersFile);
					ObjectInputStream deserializer = new ObjectInputStream(
							stream);) {
				players = (Players) deserializer.readObject();
			} catch (IOException | ClassNotFoundException cause) {
				cause.printStackTrace();
			}
		}
		return players;
	}

	/**
	 * backup the data in a file
	 * 
	 * @param players
	 */
	private static void backup(Players players) {
		try (FileOutputStream stream = new FileOutputStream(playersFile);
				ObjectOutputStream serializer = new ObjectOutputStream(
						stream);) {
			serializer.writeObject(players);
		} catch (IOException cause) {
			cause.printStackTrace();
		}
	}

}
