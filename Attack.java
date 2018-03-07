public class Attack{
	private String name, special;
	private int energy;
	private int damage;
	//Attack constructor
	public Attack(String n, int e, int d, String s){
		name = n;
		energy = e;
		damage = d;
		special = s;
	}
	public String getName(){
		return name;
	}
	public String getSpecial(){
		return special;
	}
	public int getEnergy(){
		return energy;
	}
	public int getDamage(){
		return damage;
	}
	public void setDamage(int d){
		damage = d;
	}
	public void display(){
		System.out.printf("%s) Energy:%d, Damage:%d, Special:%s%n", name, energy, damage, special);
	}
}