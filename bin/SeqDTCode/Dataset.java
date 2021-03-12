package SeqDTCode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
public class Dataset {
	public ArrayList<Integer> dataId;
	public Map<String, Integer> label;
	public Map<Integer, HashSet<Integer>> indexes; 
	public Map<Integer, Integer> indexList; 
	public Inverted it;
	public int len;
	public float TGini;
	public float ub;
	public int seqlen;
	public String pre;
	public Dataset() {
		this.indexList=new HashMap<Integer,Integer>();
		this.label = new HashMap<String,Integer>();
		this.dataId = new ArrayList<Integer>();
		this.indexes = new HashMap<Integer, HashSet<Integer>>();
		this.len = 0;
		this.TGini=0;
		this.ub=-1;
		this.pre="";
		this.seqlen=0;
	}
	public float MultiGini(){
		TGini=0;
		for(String la:label.keySet()){
			float temp=(float)label.get(la)/len;
			TGini+=temp*temp;
		}
		return TGini; 
	}

	public void show() {
		System.out.println("database:");
		for(int i = 0;i<dataId.size(); i++) {
			System.out.print(dataId.get(i) + "\t");
		}
		System.out.println();
	}
	public void clear() {
		this.dataId.clear();
		this.indexes.clear();
		this.len = 0;
		this.TGini = 0;
		this.label.clear();
	}
}
