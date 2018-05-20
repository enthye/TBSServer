package tbs.theatre;

import java.util.ArrayList;
import java.util.List;
import tbs.artist.Act;
import tbs.server.IDSearchable;

/** Theatres imported from file have a unique id, seating dimension and floor area (in sq m).
 * 	The theatre is arranged in a square block of seating dimension with half the seats
 * 	rounded down being premium seats, and the rest being regular seats.
 * 
 * 	Theatres also host performances for a given artist(s), so you can add performances and
 * 	find performances hosted at any theatre.
 * 
 * 	@author Kevin Xu
 */	

public class Theatre implements IDSearchable {
	
	private String _id;
	private int _seatDimension;
	private int _floorArea;
	private List<Performance> _performances;
	
	public Theatre(String id, int dimension, int floorArea) {
		_id = id;
		_seatDimension = dimension;
		_floorArea = floorArea;
		_performances = new ArrayList<Performance>(); // list of all performances this theatre will host
	}
	
	public String getID() {
		return _id;
	}
	
	public int getSeatingDimension() {
		return _seatDimension;
	}
	
	public int getFloorArea() {
		return _floorArea;
	}
	
	// add performance of an act from an artist to theatre
	public Performance addPerformance(Act act, String startTime, int premiumPrice, int regularPrice) {
		Performance performance = new Performance(act, startTime, premiumPrice, regularPrice, _seatDimension);
		_performances.add(performance);
		
		return performance;
	}
	
	public List<Performance> getPerformances() {
		return new ArrayList<Performance>(_performances);
	}
}
