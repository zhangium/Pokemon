import java.util.*;
public class Pokemon{
	private int hp, energy;
	private String name, type, weakness, resistance;
	private ArrayList<Attack> attacks;
	private boolean stunned = false;
	Random rand = new Random();
	//Variables for special attacks
	private static final String STUN = "stun";
	private static final String CARD = "wild card";
	private static final String STORM = "wild storm";
	private static final String DISABLE = "disable";
	private static final String RECHARGE = "recharge";
	
	//Pokemon constructor - takes in String of data
	public Pokemon(String poke){
		String[] stats = poke.split(",");
		energy = 50;
		name = stats[0];
		hp = Integer.parseInt(stats[1]);
		type = stats[2];
		resistance = stats[3];
		weakness = stats[4];
		int num = Integer.parseInt(stats[5]);
		attacks = new ArrayList<Attack>();
		for (int i=0; i<num; i++){
			String n = stats[6+4*i];
			int e = Integer.parseInt(stats[7+4*i]);
			int d = Integer.parseInt(stats[8+4*i]);
			String s = stats[9+4*i];
			Attack attack = new Attack(n,e,d,s);
			attacks.add(attack);
		}
	}
	
	public void attack(Attack attack, Pokemon enemy){
		int thisdamage = attack.getDamage();
		//Checks if pokemon's type the enemy's resistance
		if (type.equals(enemy.resistance)){
			thisdamage = attack.getDamage()/2;
			System.out.println("It's not very effective...");
		}
		//Checks if pokemon's type the enemy's weakness
		else if (type.equals(enemy.weakness)){
			thisdamage = attack.getDamage()*2;
			System.out.println("It's super effective!");
		}
		
		if (attack.getSpecial().equals(STUN)){
			int success = rand.nextInt(2);
			if (success==1){	//Forces enemy to pass next turn
				enemy.setStunned(true);
				System.out.println(name+" stunned "+enemy.getName()+"!");
			}
			energy = Math.max(0,energy-attack.getEnergy());
			enemy.setHp(Math.max(enemy.getHp()-thisdamage,0));
			System.out.println(name+" attacked "+enemy.getName()+" with "+attack.getName()+"!");
		}
		else if (attack.getSpecial().equals(CARD)){
			int success = rand.nextInt(2);
			if (success==1){
				energy = Math.max(0,energy-attack.getEnergy());
				enemy.setHp(Math.max(enemy.getHp()-thisdamage,0));
				System.out.printf("%s attacks %s with wildcard %s!%n",name,enemy.getName(),attack.getName());
			}
			else{
				System.out.printf("%s did not succeed using wildcard %s%n",name,attack.getName());
			}
		}
		else if (attack.getSpecial().equals(STORM)){
			int success = rand.nextInt(2);
			while (success==1 && energy > 0){	//Check energy here because it can go on forever
				energy = Math.max(0,energy-attack.getEnergy());
				enemy.setHp(Math.max(0,enemy.getHp()-thisdamage));
				System.out.printf("%s attacks %s with wildstorm %s!%n",name,enemy.getName(),attack.getName());
				success = rand.nextInt(2);
			}
			System.out.printf("%s did not succeed in using wildstorm %s!%n",name,attack.getName());
		}
		else if (attack.getSpecial().equals(DISABLE)){
			for (int i=0; i<enemy.getAttacks().size(); i++){
				int d = enemy.attacks.get(i).getDamage();
				enemy.attacks.get(i).setDamage(Math.max(0, d-10));
			}
			energy = Math.max(0,energy-attack.getEnergy());
			System.out.printf("%s disabled %s with %s!%n", name, enemy.getName(), attack.getName());
		}
		else if (attack.getSpecial().equals(RECHARGE)){
			energy = Math.min(50,energy+20);
			System.out.printf("%s is recharging!%n", name);
		}
		else{
			//System.out.println("attacked");
			energy = Math.max(0,energy-attack.getEnergy());
			enemy.setHp(Math.max(0,enemy.getHp()-thisdamage));
			System.out.printf("%s attacks %s with %s!%n", name, enemy.getName(), attack.getName());
		}
	}
	
	//Retreat is in PokemonArena because it needed to access list of goodguys
	
	public void pass(){
		System.out.printf("%s passes the turn%n", name);
	}
	
	public void display(){
		System.out.printf("%s) Hp:%d Energy:%d%n", name, hp, energy);
    	//for (int i=0; i<attacks.length; i++){
    		//System.out.printf("%d. %s: DAMAGE-%d%n", i+1, attacks[i].name, attacks[i].damage);
    	//}
	}
	
	public ArrayList<Attack> availableAttacks(){
		ArrayList<Attack> a = new ArrayList<Attack>();
		for (int i=0; i<attacks.size(); i++){
			if (attacks.get(i).getEnergy() <= energy){
				a.add(attacks.get(i));
			}
		}
		return a;
	}
	
	//Getters and setters
	public ArrayList<Attack> getAttacks(){
		return attacks;
	}
	public int getHp(){
		return hp;
	}
	public void setHp(int x){
		hp = x;
	}
	public void heal(){
		hp += 20;
	}
	public int getEnergy(){
		return energy;
	}
	public void recover(){
		energy = Math.min(50, energy+10);
	}
	public String getType(){
		return type;
	}
	public String getName(){
		return name;
	}
	public String getResistance(){
		return resistance;
	}
	public String getWeakness(){
		return weakness;
	}
	public boolean getStunned(){
		return stunned;
	}
	public void setStunned(boolean set){
		stunned = set;
	}
	public boolean isDead(){
		boolean dead = false;
		if (hp<=0){
			dead = true;
		}
		return dead;
	}
}