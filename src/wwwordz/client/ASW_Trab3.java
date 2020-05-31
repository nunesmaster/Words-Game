package wwwordz.client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import wwwordz.shared.Configs;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Puzzle.Solution;
import wwwordz.shared.Rank;
import wwwordz.shared.Table;
import wwwordz.shared.WWWordzException;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ASW_Trab3 implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final ManagerServiceAsync managerService = GWT.create(ManagerService.class);

	private final DeckPanel deck = new DeckPanel();
	private final int SIZE = 4;
	private int userPoints = 0;
	private final VerticalPanel loginPanel = new VerticalPanel();
	private final VerticalPanel joinPanel = new VerticalPanel();
	private final VerticalPanel playPanel = new VerticalPanel();
	private final VerticalPanel reportPanel = new VerticalPanel();
	private final VerticalPanel rankingPanel = new VerticalPanel();
	private final VerticalPanel infoPanel = new VerticalPanel();
	private final PlayPanel puzzleGrid = new PlayPanel();

	private final TextBox username = new TextBox();
	private final PasswordTextBox passwordText = new PasswordTextBox();

	private String playerUsername = new String();

	private final Label playerUsernameLabel = new Label();
	private final Label loginClockLabel = new Label();
	private final Label wordSelected = new Label();
	private final Label infoLabel = new Label();
	private final Label playHeader = new Label();
	private final Label wordsFound = new Label("");
	private final Label reportLabel = new Label("");
	private final Label rankingLabel = new Label("");

	private final DialogBox infoDialogBox = new DialogBox();

	private final HTML infoHtml = new HTML();

	private final Button infoButton = new Button("Close");
	private final Button loginButton = new Button("Login");

	private StringBuilder word = new StringBuilder();
	private StringBuilder tmpWord = new StringBuilder();
	private List<String> words = new ArrayList<String>();

	private final Grid wordsGrid = new Grid(2, 4);
	private final Grid rankingGrid = new Grid(2, 3);

	private Panels stage = Panels.LOGIN;

	private Logger logger = java.util.logging.Logger.getLogger("logger");

	private Timer clock = new Timer() {
		public void run() {
			try {
				managerService.timeToNextPlay(new AsyncCallback<Long>() {

					@Override
					public void onFailure(Throwable caught) {
						infoError("Remote Procedure Call - Failure",
								SERVER_ERROR + "<br>Error message: " + caught.getMessage());
					}

					@Override
					public void onSuccess(Long time) {
						switch (stage) {
						case LOGIN: {
							loginClock(time);
							break;
						}

						case JOIN: {

							try {
								joinClock(time);
							} catch (WWWordzException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println();

							break;
						}

						case PLAY: {
							try {
								playClock(time);
							} catch (WWWordzException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						}
						case REPORT: {
							try {
								reportClock(time);
							} catch (WWWordzException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						}
						default:
							break;
						}
					}
				});
			} catch (WWWordzException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		info();
		login();
		join();
		play();
		report();
		ranking();

		deck.add(loginPanel);
		deck.add(joinPanel);
		deck.add(playPanel);
		deck.add(reportPanel);

		RootPanel.get().add(deck);

		infoButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				infoDialogBox.hide();
				RootPanel.get().removeStyleName("standby");
				loginButton.setEnabled(true);
			}
		});
		
		loginButton.addStyleName("button-form");
		loginButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				try {
					register();
				} catch (WWWordzException e) {
					// TODO Auto-generated catch block
					System.out.println("nao funcemina");
					e.printStackTrace();
				}
			}
		});
		
		deck.showWidget(0);
		clock.scheduleRepeating(100);
	}

	/*
	 * 
	 * SETUPS
	 */

	private void info() {

		infoDialogBox.setText("Remote Procedure Call");
		infoDialogBox.setAnimationEnabled(true);

		infoButton.getElement().setId("closeButton");

		infoDialogBox.addStyleName("infoBox");
		infoPanel.add(infoHtml);
		infoPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		infoPanel.add(infoButton);
		infoDialogBox.setWidget(infoPanel);
	}
	
	private void infoError(String title, String message) {
		infoDialogBox.setText(title);
		infoHtml.addStyleName("label-error");
		infoHtml.setHTML(message);
		infoDialogBox.center();
		RootPanel.get().addStyleName("standby");
	}

	private void login() {

		HorizontalPanel clockPanel = new HorizontalPanel();
		HorizontalPanel userPanel = new HorizontalPanel();
		HorizontalPanel passwordPanel = new HorizontalPanel();

		clockPanel.add(new Label("Clock:"));
		clockPanel.add(loginClockLabel);
		clockPanel.addStyleName("teste2");
		
		userPanel.add(username);
		passwordPanel.add(passwordText);

		loginButton.addStyleName("button-form");
		loginPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);

		loginPanel.add(clockPanel);
		loginPanel.add(userPanel);
		loginPanel.add(passwordPanel);
		loginPanel.add(loginButton);
		loginPanel.getElement().setId("login");
	}

	private void join() {

		joinPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		joinPanel.add(new Label("Waiting for game to start..."));
	}

	private void play() {

		HorizontalPanel wordsFoundPanel = new HorizontalPanel();
		wordsFoundPanel.add(wordsFound);
		playPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		puzzleGrid.getElement().setId("grid");
		playHeader.addStyleName("cellsSelected");
		wordSelected.addStyleName("cellsSelected");
		playPanel.add(playHeader);
		playPanel.add(puzzleGrid);
		playPanel.add(wordSelected);
		playPanel.add(wordsFoundPanel);
		playPanel.getElement().setId("teste");

	}
	
	private void report() {
		reportPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		wordsGrid.setWidget(0,0,new Label("3 letter word"));
		wordsGrid.setWidget(0,1,new Label("4-5 letter words"));
		wordsGrid.setWidget(0,2,new Label("6-7 letter words"));
		wordsGrid.setWidget(0,3,new Label("8+ letter words"));
		
		for(int i = 0; i < 4; i++) {
			wordsGrid.setWidget(1, i, new HTML());
		}

		reportLabel.addStyleName("pointsLabel");
		reportPanel.add(reportLabel);
		reportPanel.add(wordsGrid);
	}
	
	private void ranking() {

		rankingPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		rankingGrid.setWidget(0,0,new Label("nick"));
		rankingGrid.setWidget(0,1,new Label("points"));
		rankingGrid.setWidget(0,2,new Label("accumulated"));
		
		for(int i = 0; i < 3; i++) {
			rankingGrid.setWidget(1, i, new HTML());
		}

		reportLabel.addStyleName("pointsLabel");
		reportPanel.add(rankingLabel);
		reportPanel.add(rankingGrid);
	}

	/*
	 * 
	 * 
	 * 
	 * CLOCKS
	 */

	private void loginClock(Long time) {

		long secs = time / 1000;
		long remainingsecs = (time - Configs.getJoinStageDuration()) / 1000;

		if (time > Configs.getJoinStageDuration()) {
			loginClockLabel.setText("(available in " + remainingsecs + " seconds)");
		} else {
			loginClockLabel.setText("(register within the next " + secs + " seconds)");
		}
	}

	private void joinClock(Long time) throws WWWordzException {

		long joinTime = Configs.getPlayStageDuration() + Configs.getRankingStageDuration()
				+ Configs.getReportStageDuration();
		if (time <= 0 || time > joinTime) {
			stage = Panels.PLAY;
			managerService.getPuzzle(new AsyncCallback<Puzzle>() {

				@Override
				public void onFailure(Throwable caught) {
					infoError("Remote Procedure Call - Failure",
							SERVER_ERROR + "<br>Error message: " + caught.getMessage());
				}

				@Override
				public void onSuccess(Puzzle puzzle) {
					puzzleGrid.setPuzzle(puzzle);
				}

			});
			deck.showWidget(Panels.getStageIndex(stage));
		}

		else {

			logger.log(Level.SEVERE, "o vasco lambe testiculos peludos");
		}
	}

	private void playClock(Long time) throws WWWordzException {

		HTML list;
		long playTime = time - ( Configs.getRankingStageDuration()
				+ Configs.getReportStageDuration());
		if (playTime <= 0) {
			logger.log(Level.SEVERE, time + " " + playTime);
			stage = Panels.REPORT;
			managerService.setPoints(playerUsername, userPoints, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					infoError("Remote Procedure Call - Failure",
							SERVER_ERROR + "<br>Error message: " + caught.getMessage());
				}

				@Override
				public void onSuccess(Void result) {
					logger.log(Level.SEVERE, "found here");
				}

			});
			reportLabel.setText("You got " + userPoints + " points this round");
			((HTML) (wordsGrid.getWidget(1, 0))).setHTML("");
			((HTML) (wordsGrid.getWidget(1, 1))).setHTML("");
			((HTML) (wordsGrid.getWidget(1, 2))).setHTML("");
			((HTML) (wordsGrid.getWidget(1, 3))).setHTML("");
			for(String w : words) {
				if(w.length() == 3) {
					list = ((HTML) (wordsGrid.getWidget(1, 0)));
				}
				else if(w.length() == 4 || w.length() == 5) {

					list = ((HTML) (wordsGrid.getWidget(1, 1)));
				}
				else if(w.length() == 6 || w.length() == 7) {

					list = ((HTML) (wordsGrid.getWidget(1,2)));
				}
				else {

					list = ((HTML) (wordsGrid.getWidget(1, 3)));
				}
				list.setHTML(list.getHTML() + "<br>" + w);
			}
			deck.showWidget(Panels.getStageIndex(stage));
		}

		else {

			playHeader.setText("Time " + playTime / 1000);
		}
	}
	
	private void reportClock(Long time) throws WWWordzException {
		
		long reportTime = Configs.getRankingStageDuration() + Configs.getReportStageDuration();
		if(time <= reportTime) {
			
			stage = Panels.RANKING;
			managerService.getRanking(new AsyncCallback<List<Rank>>() {

				@Override
				public void onFailure(Throwable caught) {
					infoError("Remote Procedure Call - Failure",
							SERVER_ERROR + "<br>Error message: " + caught.getMessage());
					loginButton.setEnabled(false);
				}

				@Override
				public void onSuccess(List<Rank> result) {
					HTML list2;
					
					reportLabel.setText("You got " + userPoints + " points this round");
					((HTML) (rankingGrid.getWidget(1, 0))).setHTML("");
					((HTML) (rankingGrid.getWidget(1, 1))).setHTML("");
					((HTML) (rankingGrid.getWidget(1, 2))).setHTML("");
					int it = 1;
					for(Rank rank : result) {

						logger.log(Level.SEVERE, ""+result.size());
						list2 = ((HTML) (rankingGrid.getWidget(it, 0)));
						list2.setHTML(list2.getHTML() + "<br>" + rank.getNick());
						list2 = ((HTML) (rankingGrid.getWidget(it, 1)));
						list2.setHTML(list2.getHTML() + "<br>" + rank.getPoints());
						list2 = ((HTML) (rankingGrid.getWidget(it, 2)));
						list2.setHTML(list2.getHTML() + "<br>" + rank.getAccumulated());
						it++;
					}
					deck.showWidget(Panels.getStageIndex(stage));
				}

			});
		}
	}

	private void endTime(Long time) {
		if (time <= Configs.getJoinStageDuration()) {
			stage = Panels.LOGIN;
			// showLoginPanel();
			rankingPanel.clear();
		}
	}

	private void setupinfo() {

		RootPanel.get("infoContainer").add(infoLabel);

		infoDialogBox.setText("Remote Procedure Call");
		infoDialogBox.setAnimationEnabled(true);

		infoButton.getElement().setId("closeButton");

		infoPanel.addStyleName("dialogVPanel");
		infoPanel.add(infoHtml);
		infoPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		infoPanel.add(infoButton);
		infoDialogBox.setWidget(infoPanel);
	}

	private void register() throws WWWordzException {
		final String name = username.getText();
		final String password = passwordText.getText();

		managerService.register(name, password, new AsyncCallback<Long>() {

			@Override
			public void onFailure(Throwable caught) {
				infoError("Remote Procedure Call - Failure",
						SERVER_ERROR + "<br>Error message: " + caught.getMessage());
				loginButton.setEnabled(false);
			}

			@Override
			public void onSuccess(Long time) {
				playerUsername = name;
				playerUsernameLabel.setText(playerUsername);
				// formPassword.setText("");
				stage = Panels.JOIN;
				deck.showWidget(Panels.getStageIndex(stage));
			}
		});
	}

	static enum Panels {
		LOGIN, JOIN, PLAY, RANKING, REPORT;

		public static int getStageIndex(Panels stage) {
			switch (stage) {
			case LOGIN:
				return 0;
			case JOIN:
				return 1;
			case PLAY:
				return 2;
			case REPORT:
				return 3;
			case RANKING:
				return 4;
			default:
				return -1;
			}
		}
	}

	public class PlayPanel extends Grid {

		Puzzle puzzle;

		public PlayPanel() {

			this.resize(SIZE, SIZE);
			for (int row = 0; row < SIZE; row++) {
				for (int column = 0; column < SIZE; column++) {

					final PuzzleCell cell = new PuzzleCell(row, column);
					cell.addStyleName("normalCell");
					cell.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							if (cell.isSelected) {
								wordSelected.removeStyleName("cellsSelectedActive");
								wordSelected.addStyleName("cellsSelected");
								cell.isSelected = false;
								cell.removeStyleName("selectedCell");
								cell.addStyleName("normalCell");
								String tmp = cell.getText();
								int index = tmpWord.indexOf(tmp);
								tmpWord.deleteCharAt(index);
								word.deleteCharAt(index);
								wordSelected.setText(tmpWord.toString());
							} else {
								cell.removeStyleName("normalCell");
								cell.addStyleName("selectedCell");
								tmpWord.append(cell.getText());
								word.append(cell.getText());
								cell.isSelected = true;
								wordSelected.setText(tmpWord.toString());
								if (isWord()) {

									logger.log(Level.SEVERE, "found");
								} else {

									wordSelected.removeStyleName("cellsSelectedActive");
									wordSelected.addStyleName("cellsSelected");
									logger.log(Level.SEVERE, "nope");
								}
							}
						}
					});
					this.setWidget(row, column, cell);
				}
			}
		}

		public void setPuzzle(Puzzle puzzle2) {

			puzzle = new Puzzle();
			puzzle.setTable(puzzle2.getTable());
			puzzle.setSolutions(puzzle2.getSolutions());
			for (int row = 0; row < SIZE; row++) {
				for (int column = 0; column < SIZE; column++) {
					((PuzzleCell) this.getWidget(row, column))
							.setText("" + puzzle.getTable().getCell(row + 1, column + 1).getLetter());
				}
			}
		}

		public boolean isWord() {

			String str = word.toString();
			List<Solution> solutions = puzzle.getSolutions();
			logger.log(Level.SEVERE, "solucoes" + solutions.toString());
			for (Solution s : solutions) {

				if (s.getWord().equals(str)) {

					if(words.contains(str)) {
						logger.log(Level.SEVERE, "Ja existe " + str);
						return false;
					}
					else {
						logger.log(Level.SEVERE, s.getWord());
						userPoints += s.getPoints();
						words.add(str);
						wordSelected.removeStyleName("cellsSelected");
						wordSelected.addStyleName("cellsSelectedActive");
						logger.log(Level.SEVERE, "Palavra encontrada " + str);
						//word = new StringBuilder();
						String tmpStr = wordsFound.getText();
						if(tmpStr == "") {
							wordsFound.getElement().setId("wordsFound");
							wordsFound.setText(str);
						}
						else
							wordsFound.setText(tmpStr + "," + str);
						return true;
					}
				}
			}
			logger.log(Level.SEVERE, str);
			return false;
		}
	}

	public class PuzzleCell extends HTML {

		private int row;
		private int column;
		private boolean isSelected;

		public PuzzleCell(int row, int column) {

			row = this.row;
			column = this.column;
			isSelected = false;
		}
	}

}
