package tbs.artist;

import java.util.ArrayList;
import java.util.List;
import tbs.server.IDSearchable;
import tbs.theatre.Performance;

/**All artists have an act with which they perform of a certain duration.
 * They can perform multiple acts at multiple theatres and at different times.
 * 
 * @author Kevin Xu
 *
 */

public class Act implements IDSearchable {
	private String _title;
	private int _duration;
	private static int _idCount = 1000000;
	private String _ID;
	private List<Performance> _perfs;
	
	public Act(String title, int duration) {
		_title = title;
		_duration = duration;
		_ID = "ACT" + _idCount;
		_perfs = new ArrayList<Performance>(); // many of the same act can be performed
		_idCount++;
	}
	
	public String getID() {
		return _ID;
	}
	
	public String getTitle() {
		return _title;
	}
	
	public int getDuration() {
		return _duration;
	}
	
	public void addPerformance(Performance perf) {
		if (perf != null) {
			_perfs.add(perf);
		}
	}
	
	public List<Performance> getPerformances() {	
		return new ArrayList<Performance>(_perfs);
	}
}
