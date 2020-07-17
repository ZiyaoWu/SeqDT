package SeqDT;
import java.util.ArrayList;
public class prunePEP {
public prunePEP(){
	
	}
	public  ArrayList<Integer> counts(Node root){
		ArrayList<Integer> misClassify=new ArrayList<Integer>();
		int leafCount=0;
		int error=0;
		if(root.nodeType==Tree.DecisionTreeNodeType.OUTPUT_LEAF_NODE){
			misClassify.add(root.error);
			misClassify.add(1);
			return misClassify;
		}else{
			if(root.leftChild!=null){
				misClassify=counts(root.leftChild);
				error+=misClassify.get(0);
				leafCount+=misClassify.get(1);
			}
			if(root.rightChild!=null){
				misClassify=counts(root.rightChild);
				error=misClassify.get(0);
				leafCount=misClassify.get(1);
			}
			misClassify.clear();
			misClassify.add(error);
			misClassify.add(leafCount);
			return misClassify;
		}
	}
	public int pruned_criterion(int pruned_errors){
		pruned_errors+=0.5;
		return pruned_errors;
	}
 
	public float Standard_deviation(int total_items,int errors){
		float deviation=(float)Math.sqrt((float)errors/total_items*(total_items-errors));
		return deviation;
	}
    
	public float unpruned_criterion(int unprune_errors,int leaf_count,float standard_deviation){
		float unpruned= unprune_errors+(float)0.5*leaf_count+standard_deviation;
		return unpruned;
	}
 
	public boolean criterion_comparison(float unpruned_errors,int pruned_errors){
	    if (unpruned_errors<pruned_errors){
	    	return false;
	    }else{
	    	return true;
	    }        
}
	public Node PEP_result(Node root){
		if(root.nodeType==Tree.DecisionTreeNodeType.OUTPUT_LEAF_NODE){
			return root;
		}else{
			ArrayList<Integer> misClassify;
			misClassify=counts(root);
			float standard_deviation=Standard_deviation(root.num,misClassify.get(0));
			float criterion_unprune=unpruned_criterion(misClassify.get(1),misClassify.get(0),standard_deviation);
			int criterion_pruned=pruned_criterion(root.error);
			boolean result=criterion_comparison(criterion_unprune,criterion_pruned);
			if(result==true){
				root.nodeType=Tree.DecisionTreeNodeType.OUTPUT_LEAF_NODE;
				return root;
			}else{
				if(root.leftChild.nodeType!=Tree.DecisionTreeNodeType.OUTPUT_LEAF_NODE){
					root.leftChild=PEP_result(root.leftChild);
				}
				if(root.rightChild.nodeType!=Tree.DecisionTreeNodeType.OUTPUT_LEAF_NODE){
					root.rightChild=PEP_result(root.rightChild);
				}			
			}
		}
	return root;
	}
}
