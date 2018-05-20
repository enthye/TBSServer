package tbs.artist;

/**Creates an instance of an artist.  Each new artist has its own unique id.
 * Each artist has a list of acts that they can perform.
 * 
 * @author Kevin Xu
 *
 */

import java.util.ArrayList;
import java.util.List;
import tbs.server.IDSearchable;

public class Artist implements IDSearchable {
	private static int _idCount = 1000000;
	private String _id;
	private String _name;
	private List<Act> _acts; // acts that the artist play
	
	public Artist(String name) {
		_name = name;
		_id = "ART" + _idCount;
		_acts = new ArrayList<Act>();
		_idCount++;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getID() {
		return _id;
	}
	
	public void addAct(Act act) {
		if (act != null) {
			_acts.add(act);
		}
	}
	
	// get all the acts that this artist plays
	public List<Act> getActs() {
		return new ArrayList<Act>(_acts); // encapsulate!!!
	}
	
	
	
}
