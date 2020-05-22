	package wwwordz.game;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import wwwordz.client.ManagerService;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

public class Manager implements ManagerService {

	static Manager manager;
	static ScheduledExecutorService worker = Executors
			.newSingleThreadScheduledExecutor();
	static long INITIAL_TIME = 0L;

	static Round round = null;

	private Manager() {
		round = new Round();
		worker.scheduleAtFixedRate(new Runnable() {
			public void run() {
				round = new Round();
			}
		}, Round.getRoundDuration(), Round.getRoundDuration(),
				TimeUnit.MILLISECONDS);
	}

	/**
	 * Single instance of Manager;
	 * 
	 * @return
	 */
	public static Manager getInstance() {

		if (manager == null)
			manager = new Manager();
		return manager;
	}

	/**
	 * Get table of the round
	 * 
	 * @return puzzle in a table
	 * @throws WWWordzException
	 *             if the is not started
	 */
	@Override
	public Puzzle getPuzzle() throws WWWordzException {
		return round.getPuzzle();
	}

	/**
	 * show the list of the players in a ranking
	 * 
	 * @return
	 * @throws WWWordzException
	 */
	@Override
	public List<Rank> getRanking() throws WWWordzException {
		return round.getRanking();
	}

	/**
	 * Register user with nick and password for current round
	 * 
	 * @param nick
	 * @param password
	 * @return time in seconds for next found
	 * @throws WWWordzException
	 *             if the user in not valid
	 */
	@Override
	public long register(String nick, String password) throws WWWordzException {
		return round.register(nick, password);
	}

	/**
	 * nick of the user points to set
	 * 
	 * @param nick
	 * @param points
	 * @throws WWWordzException
	 *             if the game is not finished
	 */
	@Override
	public void setPoints(String nick, int points) throws WWWordzException {
		round.setPoints(nick, points);
	}

	/**
	 * time to the next play
	 * 
	 * @return
	 */
	@Override
	public long timeToNextPlay() {
		return round.getTimetoNextPlay();
	}

	@Override
	public String greetServer(String name) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return "";
	}

}
