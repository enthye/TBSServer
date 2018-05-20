package tbs.theatre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tbs.artist.Act;
import tbs.server.IDSearchable;

/**
 * Theatres have performances from many artist acts at different times
 * and at different costs.  Each artist can have many performances of the same act.
 * There are tickets and seats allocated for each performance.
 * Useful for customers/client to get booking information and prices about performance.
 * 
 * @author Kevin Xu
 *
 */

public class Performance implements IDSearchable {
	private static int _idPerfCount = 9000000;
	private static int _idTicketCount = 999999;
	private String _id;
	private Act _act;
	private int _premiumPrice;
	private int _regularPrice;
	private int _dimension;
	private Seat[][] _seats; // 2D array of seats
	private int _premiumRows;
	private String _startTime;
	private Map<String, Seat> _issuedTickets;
	
	public Performance(Act act, String startTime, int premiumPrice, int regularPrice, int dimension) {
		_id = "PFM" + _idPerfCount;
		_act = act;
		_startTime = startTime;
		_premiumPrice = premiumPrice;
		_regularPrice = regularPrice;
		_dimension = dimension;
		_issuedTickets = new HashMap<String, Seat>(); // <ticket id, seat>
		_seats = new Seat[dimension][dimension];
		_premiumRows = (int)Math.floor(dimension/2); // premium are rows up to and including this row
		setSeatsPremOrReg(); // set up seats in 2D array
		_idPerfCount++;
	}
	
	public String getID() {
		return _id;
	}
	
	// get a list of seats that haven't been booked
	public List<String> getUnbookedSeats() {
		List<String> bookings = new ArrayList<String>();
		
		for (int i=0; i<_dimension; i++) {
			for (int j=0; j<_dimension; j++) {
				if (!_seats[i][j].isBooked()) {
					bookings.add((i+1)+ "\t" + (j+1)); // string in format: <row>"\t"<col>
				}
			}
		}
		
		return bookings;
	}
	
	public String issueTicket(int row, int col) {
		Seat seat = _seats[row-1][col-1];
		
		if (row < 1 || row > _dimension || col < 1 || col > _dimension) {
			return "ERROR invalid seat position";
		}
		
		if (seat.isBooked()) {
			return "ERROR seat has already been taken";
		}
		
		// set booking and issue ticket for seat
		seat.setBooked();
		_idTicketCount++;
		String id = "TIK" + _idTicketCount;
		_issuedTickets.put(id, seat);
		
		return id;
	}
	
	public List<String> getIssuedTickets() {
		List<String> list = new ArrayList<String>();
		list.addAll(_issuedTickets.keySet()); // get key list for hashmap
		
		return list;
	}
	
	public int getTotalSales() {
		int premTickets = 0;
		int regTickets = 0;
		
		// iterate through hashmap
		for (Seat seat : _issuedTickets.values()) {
			if (seat.isPremium()) {
				premTickets++;
			} else {
				regTickets++;
			}
		}
		
		return premTickets*_premiumPrice + regTickets*_regularPrice;
	}
	
	// for constructor use to set premium/regular unbooked seats
	private void setSeatsPremOrReg() {
		for (int i=0; i<_dimension; i++) {
			for (int j=0; j<_dimension; j++) {
				if (i < _premiumRows) {
					_seats[i][j] = new Seat(i+1, j+1, true);
				} else {
					_seats[i][j] = new Seat(i+1, j+1, false);
				}
			}
		}
	}
	
	public String getActIDForPerformance() {
		return _act.getID();
	}
	
	public String getStartTime() {
		return _startTime;
	}
	
	public int getPremiumPrice() {
		return _premiumPrice;
	}
	
	public int getRegularPrie() {
		return _regularPrice;
	}
}
