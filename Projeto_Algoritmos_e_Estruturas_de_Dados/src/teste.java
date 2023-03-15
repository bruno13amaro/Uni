
import java.util.Scanner;



class Projeto2{

	LinkedList p;

	Projeto2(){
    	p = new LinkedList();
	}

	public class LinkedList{
    	Node root;
    	int size;

    	LinkedList(){
        	root = null;
        	size = 0;
    	}

    	public void show(){
        	Node x = root;
        	while(x.next != null){
            	System.out.print(x.val + " ");
            	x = x.next;
        	}
        	System.out.println(x.val);
    	}



    	public void add(int res){
        	Node node = new Node(res);
            	if(root == null){
                	root = node;
            	}else{
                	Node run = root;
                	while(run != null){
                    	if(run.next == null){
                        	run.next = node;
                        	break;
                    	}
                    	run = run.next;
                	}
            	}
        	size ++;
        }
   	 
    	public boolean isSort(){
    		Node run = root;
    		while(run.next != null){
    			if(run.val > run.next.val) return false;
    			run = run.next;
    		}
    		return true;
    	}
    	
    	public boolean isEqual(LinkedList old){
    		if(this.size != old.size) return false;
    		else{
    			Node run1 = root, run2 = old.root;
    			while(run1.next != null && run2.next != null){
    				if(run1.val != run2.val) return false;
    				run1 = run1.next; run2 = run2.next;
    			}
    			return true;
    		}
    	}

    	public int getIndex(Node x){
        	int i;
        	for(i = 0; i < size; i++)
            	if(getNode(i) == x) break;
        	return i;
    	}

    	public Node getNode(int index){
        	if(index == 0) return root;
        	Node x = root;
        	for(int i = 1; i < index; i ++) x = x.next;
        	return x.next;
    	}

    	public void remove(Node node){
        	if(node == null){
            	throw new IllegalArgumentException("Argument Node is NULL.");
        	}else{
            	Node run = root;
            	while(run.next != null){
                	if(run.val == node.val && run.next == node.next){
                    	run.next = run.next.next;
                    	break;
                	}
                	run = run.next;
            	}
        	}
    	}
    

    	public void swap(Node x, Node y){
        	swap(x.val, y.val);
    	}
   	 
    	private void swap(int x, int y){
        	if (x == y)
            	return;
        	Node prevX = null, currX = root;
        	while (currX != null && currX.val != x) {
            	prevX = currX;
            	currX = currX.next;
        	}
        	Node prevY = null, currY = root;
        	while (currY != null && currY.val != y) {
            	prevY = currY;
            	currY = currY.next;
        	}
        	if (currX == null || currY == null)
            	return;
        	if (prevX != null)
            	prevX.next = currY;
        	else
            	root = currY;
        	if (prevY != null)
            	prevY.next = currX;
        	else
            	root = currX;
        	Node temp = currX.next;
        	currX.next = currY.next;
        	currY.next = temp;
    	}

	}

	public class Node{
    	Node next;
    	int val;

    	Node(int val){
        	next = null;
        	this.val = val;
    	}
	}
 
	public static void bubbleSort(LinkedList l, int total_pass, int total_cmp, int total_exch){
    	boolean rep = true;
    	int h = 1, pass = 1, exch = 0, cmp = 0;
    	while(rep){
        	rep = false;
	        for(int i = 0; i < l.size-1; i ++){
	        	cmp ++;
	            if(l.getNode(i).val > l.getNode(i+1).val){
	            	rep = true;
	            	exch ++;
	            	l.swap(l.getNode(i), l.getNode(i+1));
	            }
	        }
	        if(l.isSort()) rep = false;
        	if(rep){
       		    System.out.print("\t"+h+"\t"+pass+"\t\t\t\t\t");
            	l.show();
            	pass++; total_pass++;
        	}else{
            	System.out.print("\t"+h+"\t"+pass+"\t\t"+cmp+"\t\t"+exch+"\t");
            	l.show();
        	}
    	}
    	total_exch += exch; total_cmp += cmp;
    	System.out.println("------------------------------------------");
        System.out.println("Total\t"+total_pass+"\t\t"+total_cmp+"\t\t"+total_exch);
	}

	public static void bubble_h_sort(LinkedList l, int h, int total_pass, int total_cmp, int total_exch){
    	boolean rep = true;
    	int pass = 1, cmp = 0, exch = 0;
    	while(h > 1){
    		rep = true;
        	while(rep){
            	rep = false;
	            for(int i = h; i < l.size; i ++){
	            	cmp ++;
	                for(int j = i; j >= h; j -= h){
	               		if(l.getNode(j).val < l.getNode(j-h).val){
	                    	l.swap(l.getNode(j-h), l.getNode(j));
	                    	exch ++;
	                    	rep = true;
	               		}
	                }
	            }
            	if(rep){
           		 	System.out.print("\t"+h+"\t"+pass+"\t\t\t\t\t");
                	l.show();
                	total_pass++; pass++;
            	}else{
           		 	System.out.print("\t"+h+"\t"+pass+"\t\t"+cmp+"\t\t"+exch+"\t");
                	l.show();
                	total_pass ++;
            	}
        	}
        	h /= 3;
        	total_exch += exch; total_cmp += cmp;
        	pass = 1; cmp = 0; exch = 0;
    	}
    	bubbleSort(l, total_pass, total_cmp, total_exch);
	}
	
	public static void bubble_h_sort_noShow(LinkedList l, int h, int total_pass, int total_cmp, int total_exch){
    	boolean rep = true;
    	int pass = 1, cmp = 0, exch = 0;
    	while(h >= 1){
    		rep = true;
        	while(rep){
            	rep = false;
	            for(int i = h; i < l.size; i ++){
	            	cmp ++;
	                for(int j = i; j >= h; j -= h){
	               		if(l.getNode(j).val < l.getNode(j-h).val){
	                    	l.swap(l.getNode(j-h), l.getNode(j));
	                    	exch ++;
	                    	rep = true;
	               		}
	                }
	            }
            	pass ++;// total_pass ++;
        	}
        	System.out.println("\t"+h+"\t"+pass+"\t\t"+cmp+"\t\t"+exch+"\t");
        	h /= 3;
        	total_exch += exch; total_cmp += cmp; total_pass += pass;
        	pass = 1; cmp = 0; exch = 0;
    	}
    	total_pass --;
    	System.out.println("------------------------------------------");
        System.out.println("Total\t"+total_pass+"\t\t"+total_cmp+"\t\t"+total_exch);
	}


	public static void shellSort(LinkedList l){
    	int h = 1, total_pass = 1, total_cmp = 0, total_exch = 0;
    	while(h < l.size/3) h = (h*3) + 1;
    	if(l.size <= 20) bubble_h_sort(l, h, total_pass, total_cmp, total_exch);
    	else bubble_h_sort_noShow(l, h, total_pass, total_cmp, total_exch);
	}

    

	public static Projeto2 criar_lista(){
    	Scanner in = new Scanner(System.in);
    	String s  = in.nextLine();
    	in.close();
    	Projeto2 aed = new Projeto2();
    	int i = 0;
    	while(i < s.length()){
        	int res = s.charAt(i) - 48;
        	int j = 1;
        	while(j+i < s.length()){
            	int temp = s.charAt(j+i) - 48;
            	if(temp < 0 || temp > 9){
                	j ++;
                	break;
            	}else{
                	res = (res*10) + temp;
                	j ++;
            	}
        	}
        	i += j;
        	aed.p.add(res);
    	}
    	return aed;
	}

	public static void main(String[] args){
		System.out.println("Coloque um conjunt de números para ordenar");
    	Projeto2 a = criar_lista();
    	System.out.print("\th\tpass\tcmp\t  exch\t");
    	if(a.p.size <= 20) a.p.show();
    	else System.out.println();
    	System.out.println("------------------------------------------");
    	shellSort(a.p);
	}
}
