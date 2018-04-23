
	/**
	 *
	 * WAVLTree
	 *
	 * An implementation of a WAVL Tree with
	 * distinct integer keys and info
	 *
	 *Students:
	 *Yotam Manne יותם מנה 204717862 username: yotammanne
	 *Daniel Peer 206283442 דניאל פאר username: danielpeer
	 *
	 */

	public class WAVLTree {
		private WAVLNode root;
		private WAVLNode min;
		private WAVLNode max;
		private static WAVLNode sentinel = new WAVLNode();

		public WAVLTree() {
			this.root=new WAVLNode();
			this.root.left=sentinel;
			this.root.right=sentinel;
		  }
		  /**
		   * public boolean empty()
		   *
		   * returns true if and only if the tree is empty
		   *
		   */
		  public boolean empty() {
			  if (root.isRealNode()==true)
				  return false;
			  return true;
		  }

		 /**
		   * public String search(int k)
		   *
		   * returns the info of an item with key k if it exists in the tree
		   * otherwise, returns null
		   */  
		  public String search(int k) {
			  WAVLNode b=treePosition(k);
			  if(b.getKey()==k) {
				  return b.getValue();
			  }
			  return null;
		  }
		  
		     public WAVLNode treePosition(int k) {
			  WAVLNode NodeRoot=root;
			  WAVLNode NodePosition=NodeRoot;
			  while(NodeRoot!=null && NodeRoot.isRealNode()) {
				  NodePosition=NodeRoot;
				  if (NodeRoot.getKey()==k) 
					  break;
				  if(NodeRoot.getKey()>k)
					  NodeRoot=NodeRoot.getLeft();
				  else
					  NodeRoot=NodeRoot.getRight();  
			  }
			  return NodePosition;
			
			  }
	  
		 
		  /**
		   * public int insert(int k, String i)
		   *
		   * inserts an item with key k and info i to the WAVL tree.
		   * the tree must remain valid (keep its invariants).
		   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
		   * returns -1 if an item with key k already exists in the tree.
		   */
		   public int insert(int k, String i) {
			   WAVLNode nodeParent=treePosition(k);    /**find the parent**/
			   if (nodeParent.isRealNode()!=true) {           /** the the node is the root**/
				   WAVLNode root= new WAVLNode(k,i);
				   this.root=root;
				   this.max=root;
				   this.min=root;
				   this.root.setRight(sentinel);
				   this.root.setLeft(sentinel);
			   }
			   else if (nodeParent.isRealNode()==true && nodeParent.key!=k) {     /**the parent is real node**/
				   WAVLNode node= new WAVLNode(k,i,nodeParent);
				   if(k>this.max.getKey())
					   this.max=node;
				   if(k<this.min.getKey())
					   this.min=node;
			       if (nodeParent.getKey()>k)
			    	   		nodeParent.setLeft(node);
			       if (nodeParent.getKey()<k)
			    	   		nodeParent.setRight(node);
			       node.setRight(sentinel);
			       node.setLeft(sentinel);
			       this.increaseTreeSize(node);
			       return this.insertRebalance(nodeParent);
			       
			   }
			   else {
				   nodeParent.setValue(i);
				   return -1;
			   }
			  return 0;
		   }
		   
		   
		   private int insertRebalance(WAVLNode node) {
			   if(node==null) return 0;
			   int count=0;
			   int rankParent =node.rank;
			   int rankRight = (node.getRight()!=null) ? node.getRight().getRank() : -1;
			   int rankLeft = (node.getLeft()!=null) ? node.getLeft().getRank() : -1;
			   if(rankParent-rankRight == 1 && rankParent-rankLeft == 0
					   || rankParent-rankRight == 0 && rankParent-rankLeft == 1) {//if 1-0 node
				   return count+=promote(node)+insertRebalance(node.getParent());
			   }
			   if(rankParent-rankRight == 2 && rankParent-rankLeft == 0) {//if 0-2 node
				   if(node.left.getRank()-node.left.left.getRank()==1 
						   && node.left.getRank()-node.left.right.getRank()==2) {//if left child is 1-2 node
					   return count+rightRotate(node.left,true);//right rotate subtree with node.left as root
				   }
				   else if(node.left.getRank()-node.left.left.getRank()==2 
						   && node.left.getRank()-node.left.right.getRank()==1) {//if left child is 2-1 node
					   return count+doubleRotateRight(node.left,true);//right double rotate subtree with node.left as root
				   }
			   }
			   else if(rankParent-rankRight == 0 && rankParent-rankLeft == 2) {//if 2-0 node
				   if(node.right.getRank()-node.right.right.getRank()==1 
						   && node.right.getRank()-node.right.left.getRank()==2) {//if right child is 2-1 node
					   
					   return count+leftRotate(node.right,true);//left rotate subtree with node.right as root
				   }
				   else if(node.right.getRank()-node.right.right.getRank()==2 
						   && node.right.getRank()-node.right.left.getRank()==1) {//if right child is 1-2 node
					   
					   return count+doubleLeftRotate(node.right,true);//right double rotate subtree with node.right as root
				   }
			   }
			   return count;
		   }
		   
		   
		   private int deleteRebalance(WAVLNode node) {
			   if(node==null) {
				   return 0;
			   }
			   int count=0;
			   int rankParent =node.rank;
			   int rankRight = (node.getRight()!=null) ? node.getRight().getRank() : -1;
			   int rankLeft = (node.getLeft()!=null) ? node.getLeft().getRank() : -1;
			   if(rankParent-rankRight<3 && rankParent-rankLeft<3) {//no rebalance needed
				   if(rankRight==-1 && rankLeft==-1 && rankParent==1)
					   return count + demote(node) + deleteRebalance(node.getParent());
				   return count;
			   }
			   if(rankParent-rankRight==2 && rankParent-rankLeft==3
				   || rankParent-rankRight==3 && rankParent-rankLeft==2){//if 2-3 or 3-2 node demote and move problem up
					   return count + demote(node) + deleteRebalance(node.getParent());
				   }
			   if(rankParent-rankRight==1 && rankParent-rankLeft==3) { //if 3-1 node
				   if(node.right.rank-node.right.right.rank==2
						   && node.right.rank-node.right.left.rank==2) {//if right child is 2-2
					   return count + demote(node.right) + demote(node) + deleteRebalance(node.getParent());//move problem up
				   }
				   else if(node.right.rank-node.right.right.rank==1) {// right child is 1-1 or 2-1
					   count+=leftRotate(node.right,false);
					   if(node.rank-node.left.rank==2 && node.rank-node.right.rank==2) //if after rebalance node is 2-2 leaf, demote it
						   return count + demote(node) + deleteRebalance(node);
					   return count + deleteRebalance(node);
				   }
				   else {
					   return count+doubleLeftRotate(node.right,false);
				   }
			   }
			   else { //if 1-3 node (symetric case)
				   if(node.left.rank-node.left.left.rank==2
						   && node.left.getRank()-node.left.right.rank==2) {//if left child is 2-2
					   return count + demote(node.left) + demote(node) + deleteRebalance(node.getParent());//move problem up
				   }
				   else if(node.left.rank-node.left.left.rank==1) {// Left child is 1-1 or 1-2
					   count+=rightRotate(node.left,false);
					   if(node.rank-node.left.rank==2 && node.rank-node.right.rank==2) //if after rebalance node is 2-2 leaf, demote it
						   return count + demote(node) + deleteRebalance(node);
					   return count + deleteRebalance(node);
				   }
				   else {
					   return count+doubleRotateRight(node.left,false);
				   }
			   }
		   }
		   
		   
		   private int promote(WAVLNode node) {
			  node.rank++;
			  return 1;
		   }
		   
		   
		   private int demote(WAVLNode node) {
				  node.rank--;
				  return 1;
			   }
		   
		   
		   private void increaseTreeSize(WAVLNode node) {
			   while(node.getParent()!=null) {
				   node.getParent().subTreeSize++;
				   node=node.parent;
			   }
		   }
		   
		   
		   public int leftRotate(WAVLNode b, boolean isInsert) {
			   WAVLNode w=b;
			   WAVLNode t=w.getParent();
			   int sizeNode=b.subTreeSize;
			   int sizeLeftChild=b.left.subTreeSize;
			   if(this.root!=t) {
				   w.setParent(t.getParent());
			   if(w.getParent().right==t)
				   w.getParent().setRight(w);
			   if(w.getParent().left==t)
				   w.getParent().setLeft(w);}
			   else
			   { this.root=w;
			   w.parent=null;}
				   w.left.setParent(t);
				   t.setRight(w.left);
				   w.setLeft(t);
				   t.setParent(w);
				   t.rank--;
			   if(!isInsert) {
				   w.rank++;
				   if((t.rank-t.right.rank==2 && t.rank-t.left.rank==2)
						   || (t.rank-t.right.rank==2 && t.rank-t.left.rank==3)
						   || (t.rank-t.right.rank==3 && t.rank-t.left.rank==2)) {
					   t.rank--;
				   }
			   }
			   t.subTreeSize=t.subTreeSize-sizeNode+sizeLeftChild;
			   w.subTreeSize=w.subTreeSize-sizeLeftChild+t.subTreeSize;
			   
			   return 1;
			   
		   }
		   public int rightRotate(WAVLNode b,boolean isInsert) {
			   WAVLNode w=b;
			   WAVLNode t=w.getParent();
			   int sizeNode=b.subTreeSize;
			   int sizeRightChild=b.right.subTreeSize;
			   if(this.getRoot()!=t) {
				   w.setParent(t.getParent());
				   if(w.getParent().left==t)
					   w.getParent().setLeft(w);
				   if(w.getParent().right==t)
					   w.getParent().setRight(w);}
			   else {
				   this.root=w;
				   w.parent=null;
				   }
				   w.right.setParent(t);
				   t.setLeft(w.right);
				   w.setRight(t);
				   t.setParent(w);
				   t.rank--;
			   if(!isInsert) {
				   w.rank++;
				   if((t.rank-t.right.rank==2 && t.rank-t.left.rank==2)
						   || (t.rank-t.right.rank==2 && t.rank-t.left.rank==3)
						   || (t.rank-t.right.rank==3 && t.rank-t.left.rank==2)) {
					   t.rank--;
				   }
			   }
			   t.subTreeSize=t.subTreeSize-sizeNode+sizeRightChild;
			   w.subTreeSize=w.subTreeSize-sizeRightChild+t.subTreeSize;
			   return 1;
		   }
		   
		   
		   public int doubleLeftRotate(WAVLNode b,boolean isInsert) {
			   this.rightRotate(b.left,isInsert);
			   this.leftRotate(b.parent,isInsert);
			  if (isInsert)
				  b.getParent().rank++;
			   return 2;
		   }
		   
		   
		   public int doubleRotateRight(WAVLNode b,boolean isInsert) {
			   this.leftRotate(b.right,isInsert);
			   this.rightRotate(b.parent,isInsert);
			   if (isInsert)
				   b.getParent().rank++;	
			   return 2;
		   }
		   
		   
		   private void decreaseTreeSize(WAVLNode node) {
			   while(node.getParent()!=null) {
				   node.getParent().subTreeSize--;
				   node=node.parent;
			   }
		   }
			   
	
		   
		  /**
		   * public int delete(int k)
		   *
		   * deletes an item with key k from the binary tree, if it is there;
		   * the tree must remain valid (keep its invariants).
		   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
		   * returns -1 if an item with key k was not found in the tree.
		   */
		   public int delete(int k) {
			   WAVLNode node=treePosition(k); 
			   if(node.getKey()!=k)      
				   return -1;
			   WAVLNode nodeParent=node.parent;
			   if (node.getParent()==null) {
				   if(node.key<max.key) {//root have a successor
					   WAVLNode successor = this.successor(node);
					   if(successor==max)
						   max=this.root;	   
					   return delete(successor.getKey())&replace(node,successor.key,successor.value);
				   }
				   if (node==max&&node!=min) {
					WAVLNode g=this.root.predeccessor(this);
					if(min==g) {
						min=node;
					}
					return delete(g.getKey())&replace(node,g.key,g.value);
				   }
				   else {
				   this.root=new WAVLNode();
				   } 
				   }   
			   else if(node.getRight()==null || node.getLeft()==null) { 
				  if (nodeParent.getKey()>k) {           
					  if (node.right.rank!=-1) {
						  nodeParent.setLeft(node.right);
						  node.right.setParent(nodeParent);
					  }
					  
					  else if(node.left.rank!=-1) {
						  nodeParent.setLeft(node.getLeft());
						  node.getLeft().setParent(nodeParent);
						
					  }
					  else 
						  nodeParent.setLeft(WAVLTree.sentinel); 
					  		if(node==min)
					  			this.min=nodeParent;
					}

				  else {
					  if (node.getRight()!=null) {
						  nodeParent.setRight(node.getRight());
						  node.getRight().setParent(nodeParent);

					  }
					  else if(node.getLeft()!=null) {
						  nodeParent.setRight(node.getLeft());
						  node.getLeft().setParent(nodeParent);
						  if(node==max)
							  max=node.getParent();
					  }
				  
					  else {
						  nodeParent.setRight(WAVLTree.sentinel);
					  if(node==max)
						  max=node.getParent();
					  }
	
				  }	 
				  this.decreaseTreeSize(node);
				  return deleteRebalance(nodeParent);
			   }
		   else {
			  WAVLNode successor=successor(node);
			  return delete(successor.getKey())&replace(node,successor.key,successor.value);
			  }
			   return 0;
			 
		   }
		   
		   
		   public int replace(WAVLNode node,int key,String info) {
			   node.key=key;
			   node.value=info;
			   return 0;
			   
			   
		   }
	
		   
		   private WAVLNode successor(WAVLNode node) {
			   return node.successor(this);
		   }
		   
		   private WAVLNode predeccessor(WAVLNode node) {
			   return node.predeccessor(this);
		   }
		   
		   private static WAVLNode findMinNode(WAVLNode node) {
			   WAVLNode y=node;
			   while(y.getLeft()!=null) {
				   y=y.getLeft();
			   }
			   return y;
		   }
		   
		   
		   /**
		    * public String min()
		    *
		    * Returns the info of the item with the smallest key in the tree,
		    * or null if the tree is empty
		    */
		   public String min() {
			   return this.min.value;
		   }


		   private WAVLNode findMaxNode(WAVLNode node) {
			   WAVLNode y=node;
			   while(y.getRight()!=null) {
				   y=y.getRight();
			   }
			   return y;
		   }
		   
		   /**
		    * public String max()
		    *
		    * Returns the info of the item with the largest key in the tree,
		    * or null if the tree is empty
		    */
		   public String max()
		   {
			  return this.max.value;
		   }

		  /**
		   * public int[] keysToArray()
		   *
		   * Returns a sorted array which contains all keys in the tree,
		   * or an empty array if the tree is empty.
		   */	   
		   public int[] keysToArray()
			  {
			   int[]keys=new int[this.size()];
			   WAVLNode node = this.min;
			   for(int i=0;i<keys.length;i++) {
				   keys[i]=node.getKey();
				   node=node.successor(this);
			   }
			   return keys;
			  }
	 

	  /**
	   * public String[] infoToArray()
	   *
	   * Returns an array which contains all info in the tree,
	   * sorted by their respective keys,
	   * or an empty array if the tree is empty.
	   */
	  public String[] infoToArray()
	  {
		   String[]keys=new String[this.size()];
		   WAVLNode node = this.min;
		   for(int i=0;i<keys.length;i++) {
			   keys[i]=node.getValue();
			   node=node.successor(this);
		   }
		   return keys;
		  }
	 
	   /**
	    * public int size()
	    *
	    * Returns the number of nodes in the tree.
	    *
	    * precondition: none
	    * postcondition: none
	    */
	   public int size()
	   {
		   return this.root.subTreeSize; // to be replaced by student code
	   }
	   
	     /**
	    * public int getRoot()
	    *
	    * Returns the root WAVL node, or null if the tree is empty
	    *
	    * precondition: none
	    * postcondition: none
	    */
	   public WAVLNode getRoot()
	   {
		   if(this.root.isRealNode())
			   return this.root;
		   return null;
	   }
	   
	   
	     /**
	    * public int select(int i)
	    *
	    * Returns the value of the i'th smallest key (return -1 if tree is empty)
	    * Example 1: select(1) returns the value of the node with minimal key 
		* Example 2: select(size()) returns the value of the node with maximal key 
		* Example 3: select(2) returns the value 2nd smallest minimal node, i.e the value of the node minimal node's successor 	
	    *
		* precondition: size() >= i > 0
	    * postcondition: none
	    */   
	   public String select(int i)
	   {
		   String[]infos=this.infoToArray();
		   if(i<=infos.length-1) {
			   return infos[i];
		   }
		   return "-1"; 
	   }

	   public void setRoot(WAVLNode root) {
		   this.root=root;
	   }
	   
	   
		/**
		   * public interface IWAVLNode
		   * ! Do not delete or modify this - otherwise all tests will fail !
		   */
		
	   
	   public interface IWAVLNode{	
			public int getKey(); //returns node's key (for virtuval node return -1)
			public String getValue(); //returns node's value [info] (for virtuval node return null)
			public IWAVLNode getLeft(); //returns left child (if there is no left child return null)
			public IWAVLNode getRight(); 
			public boolean isRealNode(); 
			public int getSubtreeSize(); 
		}

	   /**
	   * public class WAVLNode
	   *
	   * If you wish to implement classes other than WAVLTree
	   * (for example WAVLNode), do it in this file, not in 
	   * another file.
	   * This class can and must be modified.
	   * (It must implement IWAVLNode)
	   */
	  public static class WAVLNode implements IWAVLNode{
		  
		  private int key;
		  private String value;
		  private WAVLNode left;
		  private WAVLNode right;
		  private WAVLNode parent;
		  private int rank;
		  private int subTreeSize;
		  
		  public WAVLNode(int key,String value,WAVLNode parent) {
		    	this.key=key;
		    	this.value=value;
		    	this.rank=0;
		    	this.parent=parent;
		    	this.subTreeSize=1;
		    }
		  public WAVLNode() {
		    	this.rank=-1;
		    	
		    	
		    }
		  public WAVLNode(int key,String value) {
		    	this.key=key;
		    	this.value=value;
		    	this.rank=0;
		    	this.subTreeSize=1;
		  }
		  
		  public int getKey()
			{
				if(this.isRealNode())
				return this.key;
				return -1;
			}
		  public String getValue()
			{
				if(this.isRealNode())
				return this.value;
				return null;
			}
		  public void setValue(String info) {
			  this.value=info;
		  }
		  
		  public void setRight(WAVLNode right) {
				this.right=right;
			}
		  public void setLeft(WAVLNode left) {
				this.left=left;
			}
			
		  public WAVLNode getLeft()
			{
				if(this.left.isRealNode())
				return this.left; 
				return null;
			}
		  public WAVLNode getRight()
			{
				if(this.right.isRealNode())
					return this.right;
					return null;
			}
			// Returns True if this is a non-virtual WAVL node (i.e not a virtual leaf or a sentinal)
		  public boolean isRealNode()
			{
				return this.rank!=-1; 
			}

		  public int getSubtreeSize()
			{
				if(this.isRealNode())
					return this.subTreeSize; 
				return 0;
			}
		  public WAVLNode getParent() {
				return this.parent;
			}
		  public int getRank() {
			  return this.rank;
		  }
		  public void setRank(int r) {
			  this.rank=r;
		  }
		  
		  public void setParent(WAVLNode node) {
			  this.parent=node;
		  }
		  
		   public WAVLNode successor(WAVLTree tree) {
			   WAVLNode node=this;
			   if(node==tree.max) return node;
			   if (node.getRight()!=null) { 
				   return findMinNode(node.getRight());
			   }
			   while (node.getParent().left.getKey()!=node.getKey()) {
				   node=node.getParent();
			   }
			   return node.getParent();
		   }
		   
		   public WAVLNode predeccessor(WAVLTree tree) {
			   WAVLNode node=this;
			   if(node==tree.min) return node;
			   if (node.getLeft()!=null) { 
				   return findMinNode(node.getLeft());
			   }
			   while (node.getParent().right.getKey()!=node.getKey()) {
				   node=node.getParent();
			   }
			   return node.getParent();
		   } 
		}
	}
	





