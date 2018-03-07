public class test{
	public static void main(String [] args){
		int[] attacks = new int[2];
		attacks[0] = 2;
		attacks[1] = 55;
		attacks[1] /= 5;
		attacks[0] += 100;
		System.out.println(attacks[1]);
	}
}