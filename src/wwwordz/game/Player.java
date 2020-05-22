package wwwordz.game;

import java.io.Serializable;

public class Player extends Object implements Serializable {

	/**
	 * A player of WWWordz, including authentication data (name and password),
	 * current round and accumulated points.
	 */
	private static final long serialVersionUID = 1L;
	private int accumulated;
	private String nick;
	private String password;
	private int points;

	/**
	 * Creates a player from nick and password
	 * 
	 * @param nick
	 * @param password
	 */
	public Player(String nick, String password) {

		this.nick = nick;
		this.password = password;
	}

	/**
	 * Current accumulated points of this player
	 * 
	 * @return
	 */
	public int getAccumulated() {
		return accumulated;
	}

	/**
	 * Update accumulated points of this player
	 * 
	 * @param accumulated
	 */
	public void setAccumulated(int accumulated) {
		this.accumulated = accumulated;
	}

	/**
	 * Current nick of this player
	 * 
	 * @return
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Update nick of this player
	 * 
	 * @param nick
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Current password of this player
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Update password of this player
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Current points of this player
	 * 
	 * @return
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Update points of current round for this player These points are added to
	 * accumulated points
	 * 
	 * @param points
	 */
	public void setPoints(int points) {
		setAccumulated(points + getAccumulated());
		this.points = points;
	}

}
