package SeqDT;
import java.util.LinkedHashMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
public class Tree {
	public int depth;
	public Map<String, Integer> label;
	public ArrayList<Sequence>dataBase;
	public int g;
	public int maxL;
	public Inverted it;
	public Top1Pattern top1;
	public enum DecisionTreeNodeType {
		FEATURE_NODE, OUTPUT_LEAF_NODE
	}
	public double threshold;
	public boolean pru;
	public ArrayList<Node> NodeList;
	public int minNum;
	public double minSplit;
	public LinkedHashMap<Node, ArrayList<String>> feature;
	public Tree( int g, int maxL, double threshold, int minNum, double minSplit, int depth,boolean pru) {		
		this.dataBase=new ArrayList<Sequence>();
		this.label=new HashMap<String,Integer>();
		this.g = g;
		this.maxL=maxL;
		this.threshold=threshold;
		this.pru=pru;
		this.minSplit=minSplit;
		this.minNum=minNum;
		this.NodeList=new ArrayList<Node>();
		this.depth=depth;
		this.top1 = new Top1Pattern(g,maxL);
	}
	public ArrayList<Sequence> NodeDataBase(ArrayList<Integer> dataId){
		ArrayList<Sequence> sList=new ArrayList<Sequence>();
		for(int id:dataId){
			sList.add(dataBase.get(id));
		}
		return sList;
	}
	
	public Node createRoot(ArrayList<String> trainList) throws Exception {
		Node root = new Node();
		Dataset dataset=new Dataset();
		top1.read(trainList,dataset);
		minSplit+=dataset.MultiGini();
		dataBase=top1.dataBase;
		it=top1.it;
		if(pru){
			buildTree(root,dataset,0);
			prunePEP pep=new prunePEP();
			root=pep.PEP_result(root);			
		}else{
			buildTree(root,dataset,0);
		}
		return root;
	}
	public void setNodeError(Node root, Dataset dataset){
		int num=0;
		int max=0;
		String label="";
		for(String key:dataset.label.keySet()){
			num+=dataset.label.get(key);
			if(max<dataset.label.get(key)){
				max=dataset.label.get(key);
				label=key;
			}
		}
		root.label=label;
		root.error=num-max;
		root.num=num;
	}
	public void leafNode(Node leaf, Dataset dataset){
		leaf.nodeType = DecisionTreeNodeType.OUTPUT_LEAF_NODE;
		setNodeError(leaf, dataset);
	}
	public void buildTree(Node root, Dataset dataset,int Tdepth) throws IOException {
		float purity = 1-dataset.MultiGini();
		NodeList.add(root);
		Tdepth++;
		setNodeError(root, dataset);
		root.nodeType=DecisionTreeNodeType.FEATURE_NODE;
		if(purity<=threshold){
			root.nodeType = DecisionTreeNodeType.OUTPUT_LEAF_NODE;
			return;
		}
		top1.label=dataset.label;
		top1.Num=dataset.len;
		String feature=top1.top1patternBre(dataset);
		if(feature.length()>0){
			feature=feature.substring(1);	
		}else{
			root.nodeType = DecisionTreeNodeType.OUTPUT_LEAF_NODE;
			return;
		}
		System.out.println("feature "+feature);
		root.feature=feature.split(" ");
		Dataset examplesPositive = new Dataset();
		Dataset examplesNegative = new Dataset();
		getFilteredExamplesOnBestFtr(dataset,root.feature,examplesPositive,examplesNegative);
		if(examplesNegative.len==dataset.len ||examplesPositive.len==dataset.len||
				top1.maxGini<minSplit||examplesNegative.len<minNum||examplesPositive.len<minNum||(depth>0&&Tdepth>depth)){
				root.nodeType = DecisionTreeNodeType.OUTPUT_LEAF_NODE;
				return;
			}
		Node leftChild = new Node();
		Node rightChild = new Node();
		root.rightChild = rightChild;
		root.leftChild = leftChild;
		buildTree(leftChild, examplesPositive,Tdepth);
		buildTree(rightChild, examplesNegative,Tdepth);	
	}
	static public void copyList(ArrayList<Integer>origin,ArrayList<Integer>copy){
		copy.clear();
		int len=origin.size();
		  for(int i=0;i<len;i++){
			copy.add(origin.get(i));
		}
	}
	private void getFilteredExamplesOnBestFtr(Dataset examples,String[] bestFeature,
		Dataset examplesPositive, Dataset examplesNegative){
		int Patternlen = bestFeature.length;
		ArrayList<Integer> preList = new ArrayList<Integer>();
		int len=examples.dataId.size();
		for(int i=0;i<len;i++){
			ArrayList<Integer> tempList = new ArrayList<Integer>();
			tempList.add(-1);
			int id=examples.dataId.get(i);
			for(int j=0; j<Patternlen ;j++){
				copyList(tempList, preList);
				tempList.clear();
				Map<Integer, ArrayList<Integer>> temp = it.word.get(bestFeature[j]);
				ArrayList<Integer> indexList=temp.get(id);
				if(indexList != null){
					for(int current: preList){
						if(current == -1){
							copyList(indexList,tempList);
							continue;
						}
						for(int pos: indexList){
							if(((pos-current)<=(g+1))&&(pos>current)){
								tempList.add(pos);
							}							
						}	
					}
					
				}
			}
			String Slabel=dataBase.get(id).label;
			LinkedHashSet<Integer> tempindexList = new LinkedHashSet<Integer>();
			tempindexList.add(-1);
			if(tempList.size()>0){
				examplesPositive.dataId.add(examples.dataId.get(i));
				examplesPositive.len++;
				if(examplesPositive.label.containsKey(Slabel)){
					int number=examplesPositive.label.get(Slabel);
					number++;
					examplesPositive.label.put(Slabel, number);
				}else{
					examplesPositive.label.put(Slabel, 1);
				}
				examplesPositive.indexes.put(examples.dataId.get(i), tempindexList);
			}else{
				examplesNegative.dataId.add(examples.dataId.get(i));
				examplesNegative.len++;
				if(examplesNegative.label.containsKey(Slabel)){
					int number=examplesNegative.label.get(Slabel);
					number++;
					examplesNegative.label.put(Slabel, number);
				}else{
					examplesNegative.label.put(Slabel, 1);
				}
				examplesNegative.indexes.put(examples.dataId.get(i), tempindexList);
			}
		}
	}
}