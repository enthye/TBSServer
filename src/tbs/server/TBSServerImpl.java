/***Main TBSServer implementation from the interface TBSServer.
 *  TBSSever will manage all the theatres and performances, artists and their acts,
 *  and manage bookings and issue tickets.
 * 
 * @author Kevin Xu
 */

package tbs.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import tbs.artist.Act;
import tbs.artist.Artist;
import tbs.theatre.Performance;
import tbs.theatre.Theatre;

public class TBSServerImpl implements TBSServer {
	
	private List<Theatre> _theatres;
	private List<Artist> _artists;
	private ServerSearch _search; // for all your server searching needs

	public TBSServerImpl() {
		_theatres = new ArrayList<Theatre>();
		_artists = new ArrayList<Artist>();
		_search = new ServerSearch();
	}
	
	// gets all theatres in given file path and stores the info in server
	@Override
	public String initialise(String path) {
		
		// open file path
		File file = new File(path);
		
		Scanner reader;
		try {
			reader = new Scanner(file);
		} catch (FileNotFoundException e1) {
			return "ERROR incorrect file";
		}
		
		// catch potential file input errors
		try {
			while(reader.hasNext()) {
				// get next line
				String line = reader.nextLine();
				
				// path file values are in format ""<theatre>"\t<theatre_id>\t<seating_dimension>\t<floor_area>"
				// string splitter
				String[] array = line.split("\t");
				
				// get each string splitted info
				String id = array[1];
				int seatingDimension = Integer.parseInt(array[2]);
				int floorArea = Integer.parseInt(array[3]);
				
				// add newly detected theatre to list
				_theatres.add(new Theatre(id, seatingDimension, floorArea));
			}
		} catch (Exception e) {
			reader.close();
			return "ERROR incorrect input format";
		}
		
		reader.close();
		return "";
	}

	@Override
	public List<String> getTheatreIDs() {
		List<String> theatreIDs = _search.findListOfIDs(_theatres); // search for ids
		
		return theatreIDs;
	}

	@Override
	public List<String> getArtistIDs() {
		List<String> artistIDs = _search.findListOfIDs(_artists); // search for ids
		
		return artistIDs;
	}

	@Override
	public List<String> getArtistNames() {
		List<String> artistNames = new ArrayList<String>();
		
		for (Artist artist : _artists) {
			artistNames.add(artist.getName());
		}
		
		Collections.sort(artistNames);
		
		return artistNames;
	}
	
	@Override
	public List<String> getActIDsForArtist(String artistID) {
		List<String> actIDs = new ArrayList<String>();
		
		if (artistID == null || artistID.equals("")) {
			actIDs.add("ERROR artistID is not valid");
		}
		
		// find artist with id
		Artist artist = null;
		artist = (Artist)_search.findMatchingID(_artists, artistID);
		
		if (artist == null) {
			actIDs.add("ERROR no artist with specified artistID");
			return actIDs;
		}
		
		// find acts of artist
		actIDs = _search.findListOfIDs(artist.getActs());
		
		return actIDs;
	}
	
	@Override
	public List<String> getPeformanceIDsForAct(String actID) {
		List<String> perfIDs = new ArrayList<String>();
		
		if (actID.equals("") || actID == null) {
			perfIDs.add("ERROR invalid actID");
			return perfIDs;
		}
		
		Act act = null;

		// find artist that performs act
		for (Artist artist : _artists) {
			act = (Act)_search.findMatchingID(artist.getActs(), actID);
			if (act != null) {
				break;
			}
		}
		
		if (act == null) {
			perfIDs.add("ERROR actID not found");
			return perfIDs;
		}
		
		// find performances of this act
		perfIDs = _search.findListOfIDs(act.getPerformances());
		
		return perfIDs;
	}

	@Override
	public List<String> getTicketIDsForPerformance(String performanceID) {
		List<String> ids = new ArrayList<String>();
		
		if (performanceID == null || performanceID.isEmpty()) {
			ids.add("ERROR not a valid performanceID");
			return ids;
		}
		
		// find the perfID within one of the theatres
		Performance perf = null;
		for (Theatre theatre : _theatres) {
			perf = (Performance)_search.findMatchingID(theatre.getPerformances(), performanceID);
			if (perf != null) {
				break;
			}
		}
		
		if (perf == null) {
			ids.add("ERROR performanceID not found");
		} else {
			ids = perf.getIssuedTickets();
			Collections.sort(ids);
		}
		
		return ids;
	}
	
	// adds a new artist to server and returns artist's id if possible
	@Override
	public String addArtist(String name) {
		if (name == null || name.equals("")) {
			return "ERROR invalid artist name";
		}
		
		// deal with case insensitivity
		String temp = name.toLowerCase();
		
		for (Artist artist : _artists) {
			if (artist.getName().toLowerCase().equals(temp)) {
				return "ERROR artist already exists";
			}
		}
		
		// add to list
		Artist newArtist = new Artist(name);
		_artists.add(newArtist);
		
		return newArtist.getID();
	}
	
	// adds an act to a specified artist
	@Override
	public String addAct(String title, String artistID, int minutesDuration) {
		if (title == null || title.equals("")) {
			return "ERROR title is empty";
		} else if (artistID == null || artistID.equals("")) {
			return "ERROR artistID is empty";
		} else if (minutesDuration <= 0) {
			return "ERROR act duration less than or equal to 0 minutes";
		}
		
		
		// find artist and add this act to the list of acts of the artist
		Artist artist = null;
		artist = (Artist)_search.findMatchingID(_artists, artistID);
		
		if (artist != null) {
			Act act = new Act(title, minutesDuration);
			artist.addAct(act);
			return act.getID();
		}
		
		return "ERROR artistID does not exist";
	}
	
	// schedule an artist's act to a theatre's performance list
	@Override
	public String schedulePerformance(String actID, String theatreID, String startTimeStr, String premiumPriceStr,
			String cheapSeatsStr) {
		
		// check if time is ISO-8601
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		date.setLenient(false);
		try {
			date.parse(startTimeStr);
		} catch (ParseException pe) {
			return "ERROR not valid start time.  Format should be ISO-8601 yyyy-MM-dd'T'HH:mm";
		}
		
		// check if valid prices
		int prem = -1;
		int reg = -1;
		try {
			if (premiumPriceStr.startsWith("$")) {
				premiumPriceStr = premiumPriceStr.substring(1);
				cheapSeatsStr = cheapSeatsStr.substring(1);
				
				prem = Integer.parseInt(premiumPriceStr);
				reg = Integer.parseInt(cheapSeatsStr);
				
				// negative numbers
				if (prem < 0 || reg < 0) {
					return "ERROR not a valid pricing format \"$d\"";
				}
			}
		} catch (Exception e) {
			// non int numbers
			return "ERROR not a valid pricing format \"$d\"";
		}
		
		Act act = null;
		Theatre theatre = null;
		
		// find act
		for (Artist artist : _artists) {
			act = (Act)_search.findMatchingID(artist.getActs(), actID);
			if (act != null) {
				break;
			}
		}
		
		if (act == null) {
			return "ERROR actID not found";
		}
		
		// find theatre
		theatre = (Theatre)_search.findMatchingID(_theatres, theatreID);
		if (theatre == null) {
			return "ERROR theatreID not found";
		}
		
		// add new performance to theatre and act separately
		Performance newPerf = theatre.addPerformance(act, startTimeStr, prem, reg);
		act.addPerformance(newPerf);
		
		return newPerf.getID();
	}
	
	// issue ticket to performance in the theatre that hosts the performance
	@Override
	public String issueTicket(String performanceID, int rowNumber, int seatNumber) {
		if (performanceID == null || performanceID.equals("")) {
			return "ERROR performanceID is empty";
		}
		
		Performance perf = null;
		
		// search all theatres for performanceID
		for (Theatre theatre : _theatres) {
			perf = (Performance)_search.findMatchingID(theatre.getPerformances(), performanceID);
			if (perf != null) {
				break;
			}
		}
		
		if (perf == null) {
			return "ERROR performanceID not found";
		}
		
		// issue ticket for performance
		String issued = perf.issueTicket(rowNumber, seatNumber);
		
		// return ticket id or error
		return issued;
	}
	
	// gets empty seats from performance
	@Override
	public List<String> seatsAvailable(String performanceID) {
		List<String> seats = new ArrayList<String>();
		
		if (performanceID == null || performanceID.equals("")) {
			seats.add("ERROR performanceID is empty");
		}
		
		// find performance
		Performance perf = null;
		for (Theatre theatre : _theatres) {
			perf = (Performance)_search.findMatchingID(theatre.getPerformances(), performanceID);
			if (perf != null) {
				break;
			}
		}
		
		if (perf == null) {
			seats.add("ERROR performanceID not found");
			return seats;
		}
		
		// gets seats
		seats = perf.getUnbookedSeats();
		
		return seats;
	}

	// sales report for all performances of given act
	@Override
	public List<String> salesReport(String actID) {
		List<String> sales = new ArrayList<String>();
		
		if (actID == null || actID.equals("")) {
			sales.add("ERROR actID is empty");
			return sales;
		}
		
		// find actID
		Act act = null;
		for (Artist artist : _artists) {
			act = (Act)_search.findMatchingID(artist.getActs(), actID);
			if (act != null) {
				break;
			}
		}
		
		if (act == null) {
			sales.add("ERROR actID not found");
			return sales;
		}
		
		// return performance information
		List<Performance> perfs = act.getPerformances();
		
		for (Performance perf : perfs) {
			// performanceID "\t" start time "\t" number of tickets sold 
			// "\t" totals sales receipts for performance
			sales.add(perf.getID() +"\t" + perf.getStartTime() + "\t" + perf.getIssuedTickets().size() + "\t$"
					+ perf.getTotalSales());
		}
		
		return sales;
	}

	@Override
	public List<String> dump() {
		return null;
	}

}
