import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Knapsack_ID {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		File file=new File("input");
		Scanner in= new Scanner(file);
		String[] a= in.nextLine().split(" ");
		int count=0;
		double targetValue=999;
		double maxWeight=999;
		for(int i=0;i<a.length;i++) {
			if(count==0&&!a[i].equals("")) {
				targetValue=Double.parseDouble(a[i]);
				count++;
			}else if(count==1&&!a[i].equals("")) {
				maxWeight=Double.parseDouble(a[i]);
				count=0;
				break;
			}
		}

		//array stores all the items
		ArrayList<item> array= new ArrayList<item>();
		while(in.hasNextLine()) {
			String[] b=in.nextLine().split(" ");
			String name="default";
			double value=0;
			double weight=999;
			for(int i=0;i<b.length;i++) {
				if(count==0&&!b[i].equals("")) {
					name=b[i];
					count++;
				}else if(count==1&&!b[i].equals("")) {
					value=Double.parseDouble(b[i]);
					count++;
				}else if(count==2&&!b[i].equals("")) {
					weight=Double.parseDouble(b[i]);
					count=0;
					break;
				}
			}
			if(!name.equals("default")) {
				array.add(new item(name,value,weight));
			}
		}
		ArrayList<item> r= iterativeDeepening(array.size(),targetValue,maxWeight,array);
		if(r==null) {
			System.out.println("No solution");
		}else {
			for(int i=0;i<r.size();i++) {
				System.out.print(r.get(i).name);
			}
		}
		in.close();
	}
	//md is max depth, tv is target value, mw is max weight, array is all items
	static ArrayList<item> iterativeDeepening(int md, double tv, double mw, ArrayList<item> array) {
		ArrayList<item> result=null;
		for(int i=1;i<=md;i++) {//我怀疑这个i没有用
			for(int j=0;j<array.size();j++) {
				result=dfs(1,md,new ArrayList<item>(),0,0,tv,mw,array,j);
				if (result!=null) {return result;}
			}
		}
		return null;
	}
	
	//d is depth,md is maxdepth,k1 is current knapsack, v is total value, w is total weight, tv is target value,
	//mw is max weight, array is all items, index is alphabetically index
	static ArrayList<item> dfs(int d,int md,ArrayList<item> k1,double v,double w,double tv, double mw,ArrayList<item> array,int index) {
		k1.add(array.get(index));
		v+=array.get(index).value;
		w+=array.get(index).weight;
		if(v>=tv) {//我怀疑这里少检查了一个重量是否超标
			return k1;
		}
		ArrayList<item> complete=null;
		for(int i=index+1;i<array.size();i++) {
			if(w+array.get(i).weight<=mw&&d+1<=md) {
				complete=dfs(d+1,md,k1,v,w,tv,mw,array,i);
			}
			if(complete!=null) {return complete;}
		}
		if(k1.size()>0) {
			k1.remove(k1.size()-1);
		}
		return null;
	}

}

class item{
	String name;
	double value;
	double weight;

	item(String name,double value, double weight){
		this.name=name;
		this.value=value;
		this.weight=weight;
	}
	
}