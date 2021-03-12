package SeqDTCode;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
public class Classification {
	private int g;
	public Classification(int g){
		this.g=g;
	}
	private void copySet(LinkedHashSet<Integer> indexSet, LinkedHashSet<Integer> tempSet){
		tempSet.clear();
		for(int num:indexSet){
			tempSet.add(num);
		}
	}
	
	public Map<String,LinkedHashSet<Integer>> buildDir(String[] s){
		Map<String,LinkedHashSet<Integer>> Dir = new LinkedHashMap<String,LinkedHashSet<Integer>>();
		int len = s.length;
		for(int i=0;i<len;i++){
			if(Dir.containsKey(s[i])){
				Dir.get(s[i]).add(i);
			}else{
				LinkedHashSet<Integer> indexList = new LinkedHashSet<Integer>();
				indexList.add(i);
				Dir.put(s[i], indexList);
			}
		}
		return Dir;
	}
	public String getLabel(Node root, String[] dataInstanceForTesting) {
		Node tempRoot = new Node();
		tempRoot = root;	
		Map<String,LinkedHashSet<Integer>> Dir = buildDir(dataInstanceForTesting);
		LinkedHashSet<Integer> temp = new LinkedHashSet<Integer>();
		while (tempRoot.nodeType == Tree.DecisionTreeNodeType.FEATURE_NODE) {
			int len = tempRoot.feature.length;
			LinkedHashSet<Integer> index = new LinkedHashSet<Integer>();
			temp.add(-1);
			for(int j=0;j<len;j++){
				copySet(temp,index);
				temp.clear();
				LinkedHashSet<Integer> indexSet = new LinkedHashSet<Integer>();
				if((indexSet = Dir.get(tempRoot.feature[j]))!=null){
					for(int current:index){
						if(current==-1){
							copySet(indexSet,temp);
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
