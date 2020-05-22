package wwwordz.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

public interface ManagerServiceAsync {
	
	void getPuzzle(AsyncCallback<Puzzle> callback) throws WWWordzException;

	void getRanking(AsyncCallback<List<Rank>> callback) throws WWWordzException;

	void register(String nick, String password, AsyncCallback<Long> callback) throws WWWordzException;

	void setPoints(String nick, int points, AsyncCallback<Void> callback) throws WWWordzException;

	void timeToNextPlay(AsyncCallback<Long> callback) throws WWWordzException;

	void greetServer(String textToServer, AsyncCallback<String> asyncCallback);
}