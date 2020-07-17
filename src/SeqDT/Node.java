package SeqDT;
public class Node {
	public Node leftChild;
	public Node rightChild;
	public Tree.DecisionTreeNodeType nodeType;
	public String[] feature;
	public String label;
	public int error;
	public int num;
	public Node() {
		this.label=null;
		this.leftChild = null;
		this.rightChild = null;
		this.nodeType = Tree.DecisionTreeNodeType.FEATURE_NODE;
		this.label=null;
		this.error=0;
		this.num=0;
	}
}
