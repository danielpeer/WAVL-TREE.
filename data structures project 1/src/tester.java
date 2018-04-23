import java.util.*;

public class tester {
	public static void mesureTree(int multiplyer,int size) {
		Set<Integer> mesure1 = new HashSet<Integer>();
		Random rand = new Random();
		while(mesure1.size()<size*multiplyer) {
			mesure1.add(rand.nextInt(size*multiplyer+20)+1);
		}
		WAVLTree tree6 = new WAVLTree();
		int countInsert =0;
		int countDelete=0;
		int maxInsert=0;
		int maxDelete=0;
		double avgInsert,avgDelete;
		for(int k :mesure1) {
			int r=tree6.insert(k, Integer.toString(k));
			if(r>maxInsert) maxInsert=r;
			countInsert+=r;
		}
		avgInsert=(double)countInsert/(double)size;
		System.out.println(multiplyer+"  # of insert rebalance: "+countInsert +
				"\tmax # of rebalance opr in single insert: "+
				maxInsert+"\tavg num of insert rebalance opr: "+avgInsert);
		SortedSet<Integer> delSet = new TreeSet<Integer>(mesure1);
		for(int k :delSet) {
			int r=tree6.delete(k);
			if(r>maxDelete) maxDelete=r;
			countDelete+=r;
		}
		avgDelete = (double)countDelete/(double)size;
		

		System.out.println(multiplyer+"  # of delete rebalance: "+countDelete +
				"\tmax # of rebalance opr in single delete: "+maxDelete+
				"\tavg num of delete rebalance opr: "+avgDelete);
	}

	
	
	public static void main(String[] args) {
		for(int i=1;i<=10;i++) {
			mesureTree(i,10000);
		}
	}
		

}
