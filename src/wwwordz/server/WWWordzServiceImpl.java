package wwwordz.server;

import wwwordz.client.ManagerService;
import wwwordz.game.Manager;
import wwwordz.game.Round;
import wwwordz.shared.FieldVerifier;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class WWWordzServiceImpl extends RemoteServiceServlet implements ManagerService {
	

	static {
	    Round.setJoinStageDuration(5000);
	    Round.setPlayStageDuration(5000);
	    Round.setRankingStageSuration(30000);
	    Round.setReportStageDuration(5000);
	}
	
	public Puzzle getPuzzle() throws WWWordzException {

		System.out.println("aqui");
		return Manager.getInstance().getPuzzle();
	}

	public List<Rank> getRanking() throws WWWordzException	 {

		return Manager.getInstance().getRanking();
	}

	public long register(String nick, String password) throws WWWordzException {
		
		return Manager.getInstance().register(nick, password);
	}

	public void setPoints(String nick, int points) throws WWWordzException{
		
		System.out.println("O vasco e grande panuca");
		Manager.getInstance().setPoints(nick, points);
	}

	public long timeToNextPlay() throws WWWordzException {

		return Manager.getInstance().timeToNextPlay();
	}

	@Override
	public String greetServer(String name) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return "pandeleiro";
	}
}
