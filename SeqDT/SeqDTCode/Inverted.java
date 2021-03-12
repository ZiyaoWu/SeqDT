package SeqDTCode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
public class Inverted {
	public Map<String, Map<Integer, ArrayList<Integer>>> word;
	public Map<Integer, ArrayList<Integer>> document;
	public ArrayList<Integer> indexList;
	public Inverted() {
		this.word = new LinkedHashMap<String, Map<Integer, ArrayList<Integer>>>();
		this.document = new LinkedHashMap<Integer, ArrayList<Integer>>();
		this.indexList = new ArrayList<Integer>();
	}
	
	public void buildSubInvertedIndex(int documentID, String item, int pos ) {
		if(!word.containsKey(item)) {
			document = new LinkedHashMap<Integer, ArrayList<Integer>>();
			indexList = new ArrayList<Integer>();
			indexList.add(pos);
			document.put(documentID, indexList);
			word.put(item, document);
		} else {
			document = word.get(item);
			if (document.containsKey(documentID)) {
				indexList = document.get(documentID);
				indexList.add(pos);
				document.put(documentID, indexList);
			} else {
				indexList = new ArrayList<Integer>();
				indexList.add(pos);
				document.put(documentID, indexList);
			}
		}
	}
	
	public void buildInvertedIndex(int documentID, String[] itemSet) {
		for (int itemID = 0; itemID < itemSet.length; itemID++) {
			if (!word.containsKey(itemSet[itemID])) {
				document = new LinkedHashMap<Integer, ArrayList<Integer>>();
				indexList = new ArrayList<Integer>();
				indexList.add(itemID);
				document.put(documentID, indexList);
				word.put(itemSet[itemID], document);
			} else {
				document = word.get(itemSet[itemID]);
				if (document.containsKey(documentID)) {
					indexList = document.get(documentID);
					indexList.add(itemID);
					document.put(documentID, indexList);
				} else {
					indexList = new ArrayList<Integer>();
					indexList.add(itemID);
					document.put(documentID, indexList);
				}
			}
		}
	}
	
	public void showInvertedIndex() {
		Iterator<String> iterator = word.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String str = key + " : ";
			Map<Integer, ArrayList<Integer>> temp = word.get(key);
			for (Map.Entry<Integer, ArrayList<Integer>> e : temp.entrySet()) {
				str += e.getKey() + "{" + e.getValue() + "} , ";
			}
			System.out.println(str);
		}
	}
}
