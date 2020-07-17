package SeqDT;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.ArrayList;


public class Classification {
	private int g;
	public Classification(int g){
		this.g=g;
	}
	
	public Map<String,ArrayList<Integer>> buildDir(String[] s){
		Map<String,ArrayList<Integer>> Dir = new LinkedHashMap<String,ArrayList<Integer>>();
		int len = s.length;
		for(int i=0;i<len;i++){
			if(Dir.containsKey(s[i])){
				Dir.get(s[i]).add(i);
			}else{
				ArrayList<Integer> indexList = new ArrayList<Integer>();
				indexList.add(i);
				Dir.put(s[i], indexList);
			}
		}
		return Dir;
	}
	public String getLabel(Node root, String[] dataInstanceForTesting) {		
		Node tempRoot = new Node();
		tempRoot = root;	
		Map<String,ArrayList<Integer>> Dir = buildDir(dataInstanceForTesting);
		ArrayList<Integer> temp = new ArrayList<Integer>();
		while (tempRoot.nodeType == Tree.DecisionTreeNodeType.FEATURE_NODE) {
			int len = tempRoot.feature.length;
			ArrayList<Integer> index = new ArrayList<Integer>();
			temp.add(-1);
			for(int j=0;j<len;j++){
				Tree.copyList(temp,index);
				temp.clear();
				ArrayList<Integer> indexSet = new ArrayList<Integer>();
				if((indexSet = Dir.get(tempRoot.feature[j]))!=null){
					for(int current:index){
						if(current==-1){
							Tree.copyList(indexSet,temp);
							continue;
						}
						for(int pos: indexSet){
							if(((pos-current)<=(g+1))&&(pos>current)){							
								temp.add(pos);
							}
						}
					}
				}
			}
			if(temp.size()>0){
				tempRoot = tempRoot.leftChild;
			}
			if(temp.size()==0){
				tempRoot = tempRoot.rightChild;
			}
		}
			
		return tempRoot.label;
	}
}

