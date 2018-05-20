package tbs.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**	ServerSearch provides all your ID searching needs.  Any class that implements
 * 	IDSearchable interface can have its ID searched from this class.
 * 
 * @author Kevin Xu
 *
 */

public class ServerSearch {
	
	// finds the instance from a list that matches id string
	public IDSearchable findMatchingID(List<? extends IDSearchable> list, String id) {
		
		IDSearchable found = null;
		
		for (IDSearchable idsearch : list) {
			if (idsearch.getID().equals(id)) {
				found = idsearch;
				break;
			}
		}
		
		return found;
	}
	
	// searches for all ids from a list sorted in lexicographical order
	public List<String> findListOfIDs(List<? extends IDSearchable> list) {
		
		List<String> ids = new ArrayList<String>();
		
		for (IDSearchable idsearch : list) {
			ids.add(idsearch.getID());
		}
		
		Collections.sort(ids);
		return ids;
	}
	
	
}
