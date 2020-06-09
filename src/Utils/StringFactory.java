package Utils;

public class StringFactory {
 
	public static int[] convertString(String s) {
		String[] str = s.split(" ");
		int[] arr = new int[str.length];
		for(int i = 0; i < str.length ; i++){
			arr[i] = Integer.valueOf(str[i]);
		}
		return arr;
	}
	
	public static float C(int M, int kA, int kB) {
		float num = 1;
		for (int i=0; i<kB; i++) {
			num = num * (M-kA-i) / (M-i);
		}
		return num;
	}
	
	public static void main(String[] args) {
		System.out.println(C(925872,3,2));
	}
	
}
