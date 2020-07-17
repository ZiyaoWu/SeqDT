package SeqDT;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.LinkedHashMap;
public class Dataset {
	public ArrayList<Integer> dataId;
	public Map<String, Integer> label;
	public Map<Integer, LinkedHashSet<Integer>> indexes; 
	public Map<Integer, Integer> indexList; 
	public int len;
	public float TGini;
	public float ub;
	public String pre;
	public Dataset() {
		this.indexList=new LinkedHashMap<Integer,Integer>();
		this.label = new HashMap<String,Integer>();
		this.dataId = new ArrayList<Integer>();
		this.indexes = new HashMap<Integer, LinkedHashSet<Integer>>();
		this.len = 0;
		this.TGini=0;
		this.ub=-1;
		this.pre="";
	}
	public float MultiGini(){
		TGini=0;
		for(String la:label.keySet()){
			float temp=(float)label.get(la)/len;
			TGini+=temp*temp;
		}
		return TGini; 
	}

	public void clear() {
		this.dataId.clear();
		this.indexes.clear();
		this.len = 0;
		this.TGini = 0;
		this.label.clear();
	}
}
