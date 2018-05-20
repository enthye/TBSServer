package tbs.theatre;

/**Every performance has seat allocated. Seat stores the status, pricing and position of a seat.
 * 
 * @author Kevin Xu
 *
 */

public class Seat {
	
	private int _row;
	private int _column;
	private boolean _booked;
	private boolean _premium;
	
	public Seat(int row, int column, boolean isPremium) {
		_booked = false;
		_row = row;
		_column = column;
		_premium = isPremium;
	}
	
	public boolean isBooked() {
		return _booked;
	}
	
	public void setBooked() {
		_booked = true;
	}
	
	public int getRow() {
		return _row;
	}
	
	public int getColumn() {
		return _column;
	}
	
	public boolean isPremium() {
		return _premium;
	}
}
