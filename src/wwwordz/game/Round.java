package wwwordz.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wwwordz.puzzle.Generator;
import wwwordz.shared.Configs;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

public class Round extends Object {

	private static final Generator generator = new Generator();
	private Puzzle puzzle;
	private Map<String, Player> roundPlayers = new HashMap<String, Player>();
	private static final Players players = Players.getInstance();

	private static long joinStageDuration = Configs.JOIN_STAGE_DURATION;
	private static long playStageDuration = Configs.PLAY_STAGE_DURATION;
	private static long reportStageDuration = Configs.REPORT_STAGE_DURATION;
	private static long rankingStageSuration = Configs.RANKING_STAGE_DURATION;
	private static List<Rank> rankList = new ArrayList<Rank>();

	/*
	 * Duration of join stage in milliseconds
	 * 
	 */
	public static long getJoinStageDuration() {
		return joinStageDuration;
	}

	/*
	 * Duration of play stage in milliseconds
	 * 
	 */
	public static void setJoinStageDuration(long joinStageDuration) {
		Round.joinStageDuration = joinStageDuration;
	}

	/*
	 * Change join stage
	 * 
	 * 
	 */
	public static long getPlayStageDuration() {
		return playStageDuration;
	}

	/*
	 * Change play stage
	 * 
	 * 
	 */
	public static void setPlayStageDuration(long playStageDuration) {
		Round.playStageDuration = playStageDuration;
	}

	/*
	 * Duration of report stage in milliseconds
	 * 
	 * 
	 */
	public static long getReportStageDuration() {
		return reportStageDuration;
	}

	/*
	 * 
	 * Change report stage
	 * 
	 */
	public static void setReportStageDuration(long reportStageDuration) {
		Round.reportStageDuration = reportStageDuration;
	}

	/*
	 * List of players in this round sorted by points
	 * 
	 * 
	 */
	public static long getRankingStageSuration() {
		return rankingStageSuration;
	}

	/*
	 * Change ranking stage
	 * 
	 * 
	 */
	public static void setRankingStageSuration(long rankingStageSuration) {
		Round.rankingStageSuration = rankingStageSuration;
	}

	/*
	 * Complete duration of a round (all stages)
	 * 
	 * 
	 */
	public static long getRoundDuration() {
		return getJoinStageDuration() + getPlayStageDuration()
				+ getReportStageDuration() + getRankingStageSuration();
	}

	private Date join = new Date();
	private Date play = new Date(join.getTime() + joinStageDuration);
	private Date report = new Date(play.getTime() + playStageDuration);
	private Date ranking = new Date(report.getTime() + reportStageDuration);
	private Date end = new Date(ranking.getTime() + rankingStageSuration);

	/**
	 * time to the next play stage
	 * 
	 * @return time
	 */
	public long getTimetoNextPlay() {
		Date begin = new Date();
		if (begin.before(play)) {
			return play.getTime() - begin.getTime();
		} else {
			return end.getTime() - begin.getTime() + joinStageDuration;
		}
	}

	/**
	 * register a user with nick and password
	 * 
	 * @param nick
	 * @param password
	 * @return time to the next round
	 * @throws WWWordzException
	 *             if not in join stage
	 */
	public long register(String nick, String password) throws WWWordzException {

		Date begin = new Date();

		if (begin.after(play)) {
			throw new WWWordzException("Not valid in this stage");
		}

		else if (!players.verify(nick, password)) {
			throw new WWWordzException("User doesn't exist");
		}

		Player player = players.getPlayer(nick);
		roundPlayers.put(nick, player);

		return play.getTime() - begin.getTime();

	}

	/**
	 * get table of the round
	 * 
	 * @return puzzle in a table
	 * @throws WWWordzException
	 *             if is not in play stage
	 */
	public Puzzle getPuzzle() throws WWWordzException {

		Date begin = new Date();
		if (begin.before(play)) {
			throw new WWWordzException("Not valid before Play stage");
		} else if (begin.after(report)) {
			throw new WWWordzException("Not valid in Report stage");
		}

		puzzle = generator.generate();
		return puzzle;
	}

	/**
	 * set the pointers of the player in ths round
	 * 
	 * @param nick
	 * @param points
	 * @throws WWWordzException
	 *             if not in report stage
	 */
	public void setPoints(String nick, int points) throws WWWordzException {

		Date begin = new Date();
		/*if (begin.before(report)) {
			throw new WWWordzException("Not valid before Report stage");
		} else if (begin.after(ranking)) {
			throw new WWWordzException("Not valid in Ranking stage");
		} else if (!roundPlayers.containsKey(nick)) {
			throw new WWWordzException("cenas");
		}*/

		players.addPoints(nick, points);
	}

	/**
	 * list the ranking of the players
	 * 
	 * @return
	 * @throws WWWordzException
	 *             if is not in ranking stage
	 */
	public List<Rank> getRanking() throws WWWordzException {

		Date begin = new Date();
		if (begin.before(ranking) || begin.after(end)) {
			throw new WWWordzException("Not valid in this stage");
		}
		if (rankList.isEmpty()) {
			List<Player> listPlayers = new ArrayList<Player>(
					roundPlayers.values());

			Collections.sort(listPlayers, new Comparator<Player>() {
				@Override
				public int compare(Player player1, Player player2) {
					if (player1.getPoints() == player2.getPoints())
						return 0;
					return player1.getPoints() > player2.getPoints() ? -1 : 1;
				}
			});

			for (Player player : listPlayers) {
				Rank rank = new Rank(player.getNick(), player.getPoints(),
						player.getAccumulated());
				rankList.add(rank);
			}
		}
		return rankList;
	}

	public enum Relative implements Serializable, Comparable<Relative> {

		AFTER, BEFORE;
	}

	public enum Stage implements Serializable, Comparable<Stage> {

		JOIN, PLAY, RANKING, REPORT;
	}
}
