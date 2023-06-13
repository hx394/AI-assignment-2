import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Knapsack_HC {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		File file=new File("input");
		Scanner in= new Scanner(file);
		String[] a= in.nextLine().split(" ");
		double targetValue=999;
		
		double maxWeight=999;
		int count=0;
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
		double bestError=targetValue+maxWeight;
		state bestState=null;
		for(int i=0;i<11;i++) {
			state newState=new state(array,targetValue,maxWeight);
			hillClimbing(newState);
			if(newState.error<bestError) {
				bestError=newState.error;
				bestState=newState;
			}
		}
		if(bestState.error>0) {
			System.out.println("No solution");
		}else {
			for(int i=0;i<bestState.in.size();i++) {
				System.out.print(bestState.in.get(i).name);
			}
		}
		in.close();
	}
	
	static void hillClimbing(state st) {
		double nowError=st.error;
		double bestError=nowError;
		//op is operation,inIndex is object index in "in", outIndex is object index in "out"
		//op==0 means no change, op=1 means delete, op=2 means add, op=3 means swap
		int op=0;
		int inIndex=0;
		int outIndex=0;
		for(int i=0;i<st.in.size();i++) {
			item thisItem=st.in.get(i);
			double thisError=Math.max(st.weight-st.mw-thisItem.weight, 0)+Math.max(st.tv-st.value+thisItem.value, 0);
			if(thisError<bestError) {
				op=1;
				inIndex=i;
				bestError=thisError;
			}
		}
		for(int i=0;i<st.out.size();i++) {
			item thisItem=st.out.get(i);
			double thisError=Math.max(st.weight-st.mw+thisItem.weight, 0)+Math.max(st.tv-st.value-thisItem.value, 0);
			if(thisError<bestError) {
				op=2;
				outIndex=i;
				bestError=thisError;
			}
		}
		for(int i=0;i<st.in.size();i++) {
			for(int j=0;j<st.out.size();j++) {
				item thisItem1=st.in.get(i);
				item thisItem2=st.out.get(j);
				double thisError=Math.max(st.weight-st.mw-thisItem1.weight+thisItem2.weight, 0)+Math.max(st.tv-st.value+thisItem1.value-thisItem2.value, 0);
				if(thisError<bestError) {
					op=3;
					inIndex=i;
					outIndex=j;
					bestError=thisError;
				}
			}
		}
		if(op==1) {
			item thisItem=st.in.get(inIndex);
			st.weight-=thisItem.weight;
			st.value-=thisItem.value;
			st.error=Math.max(st.weight-st.mw, 0)+Math.max(st.tv-st.value, 0);
			st.in.remove(thisItem);
			st.out.add(thisItem);
			hillClimbing(st);
		}else if(op==2) {
			item thisItem=st.out.get(outIndex);
			st.weight+=thisItem.weight;
			st.value+=thisItem.value;
			st.error=Math.max(st.weight-st.mw, 0)+Math.max(st.tv-st.value, 0);
			st.in.add(thisItem);
			st.out.remove(thisItem);
			hillClimbing(st);
		}else if(op==3) {
			item thisItem1=st.in.get(inIndex);
			item thisItem2=st.out.get(outIndex);
			st.weight=st.weight-thisItem1.weight+thisItem2.weight;
			st.value=st.value-thisItem1.value+thisItem2.value;
			st.error=Math.max(st.weight-st.mw, 0)+Math.max(st.tv-st.value, 0);
			st.in.remove(thisItem1);
			st.in.add(thisItem2);
			st.out.add(thisItem1);
			st.out.remove(thisItem2);
			hillClimbing(st);
		}
		
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

class state{
	double weight;
	double value;
	double error;
	double tv;
	double mw;
	ArrayList<item> in;
	ArrayList<item> out;
	//tv is target value, mw is max weight,in is objects in state, out is objects out of state
	state(ArrayList<item> array,double tv,double mw){
		in=new ArrayList<item>();
		out=new ArrayList<item>();
		weight=0;
		value=0;
		this.tv=tv;
		this.mw=mw;
		error=Math.max(weight-mw, 0)+Math.max(tv-value, 0);
		for(int i=0;i<array.size();i++) {
			item thisItem=array.get(i);
			if(Math.random()<0.5) {
				in.add(thisItem);
				weight+=thisItem.weight;
				value+=thisItem.value;
				error=Math.max(weight-mw, 0)+Math.max(tv-value, 0);
			}else {
				out.add(thisItem);
			}
		}
	}
}