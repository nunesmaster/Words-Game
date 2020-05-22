package wwwordz.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

@RemoteServiceRelativePath("greet")
public interface ManagerService extends RemoteService {

	public Puzzle getPuzzle() throws WWWordzException;

	public List<Rank> getRanking() throws WWWordzException;

	public long register(String nick, String password) throws WWWordzException;

	public void setPoints(String nick, int points) throws WWWordzException;

	public long timeToNextPlay() throws WWWordzException;

	public String greetServer(String name) throws IllegalArgumentException;
}