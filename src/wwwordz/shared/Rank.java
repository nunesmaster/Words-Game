package wwwordz.shared;

import java.io.Serializable;

/**
 * A row in the ranking table. Basically all the data of a player except the
 * password
 * 
 * @author vasco
 * @author Nuno
 */
public class Rank extends Object implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int accumulated;
	private String nick;
	private int points;
	
	
	Rank() {}

	/**
	 * An instance with all fields initialized
	 * 
	 * @param nick
	 * @param points
	 * @param accumulated
	 */
	public Rank(String nick, int points, int accumulated) {
		this.nick = nick;
		this.points = points;
		this.accumulated = accumulated;
	}

	/**
	 * Current accumulated points in this rank
	 * 
	 * @return
	 */
	public int getAccumulated() {
		return accumulated;
	}

	/**
	 * Change accumulated points in this rank
	 * 
	 * @param accumulated
	 */
	public void setAccumulated(int accumulated) {
		this.accumulated = accumulated;
	}

	/**
	 * Current nick in this rank
	 * 
	 * @return
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Change the nick in this rank
	 * 
	 * @param nick
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Current points in this rank
	 * 
	 * @return
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Change points in this rank
	 * 
	 * @param points
	 */
	public void setPoints(int points) {
		this.points = points;
	}

}
