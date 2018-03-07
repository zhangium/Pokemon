import java.io.*;
import java.util.*;
public class PokemonArena{
	public static ArrayList<Pokemon> all;
	public static ArrayList<Pokemon> goodguys;
	public static ArrayList<Pokemon> badguys;
	public static Pokemon goodguy;
	public static Pokemon badguy;
	private static Random rand = new Random();
	private static Scanner kb = new Scanner(System.in);
	
	public static void main(String [] args) throws IOException{
		//Extracting data from textfile
		Scanner file = new Scanner(new BufferedReader(new FileReader("pokemon.txt")));
    	int n = file.nextInt();
    	all = new ArrayList<Pokemon>();
    	goodguys = new ArrayList<Pokemon>();
    	badguys = new ArrayList<Pokemon>();
    	file.nextLine();
    	
    	//Make all Pokemon
    	for (int i=0; i<n; i++){
    		String poke = file.nextLine();
    		all.add(new Pokemon(poke));
    	}
 		
 		System.out.println("Welcome to the Pokemon Arena!");
 		System.out.println("Brought to you by The Pulse");
 		System.out.println("********************************");
 		//chooseTeams() - displays all Pokemon and allows user to select 4
    	chooseTeams();
    	
    	boolean gameRunning = true;
    	int battleNum = 1;
    	//Main game loop
    	while (gameRunning){
    		if (badguys.size()==0){
    			gameRunning = false;
    			System.out.println("You have won! You are Trainer Supreme.");
    			break;
    		}
    		//Are there any alive Pokemon left?
    		int alive = 0;
    		for (int i=0; i<4; i++){
    			if (!goodguys.get(i).isDead()){
    				alive += 1;
    			}
    		}
    		if (alive==0){
    			System.out.println("Sorry, you lose.");
    			break;
    		}
    		
    		//Choose a badguy out of all unselected Pokemon
	    	int bad = rand.nextInt(badguys.size());
	    	badguy = badguys.get(bad);
	    	System.out.println("A wild "+badguy.getName()+" appeared!");
	    	
	    	//Let player choose which Pokemon to battle badguy
	    	choosePokemon();
	    	
	    	//Randomly determine who goes first
	    	int turn = rand.nextInt(2);
	    	//1 - goodguy goes first
	    	//0 - badguy goes first
	    	if (turn == 1){	//Goodguy goes first
	    		System.out.printf("%s goes first!%n", goodguy.getName());
	    		battle(turn,battleNum);
	    	}
	    	else{ //turn == 0
	    		System.out.printf("%s goes first!%n", badguy.getName());
	    		battle(turn,battleNum);
	    	}
	    	battleNum += 1;
	    	//End of battle: every goodguy who's still awake
	    	//gets healed +20 HP
	    	for (int i=0; i<4; i++){
	    		if (!goodguys.get(i).isDead()){
	    			goodguys.get(i).heal();	//+20 HP
	    		}
	    	}
    	}
	}
	
	//Each battle has it's own winner
	public static String winner = "";
	//Battle method - where the main battle loop happens
	public static void battle(int turn, int battleNum){
    	System.out.printf("\nBATTLE %d%n", battleNum);
    	System.out.println("------------------------------------");
		boolean running = true;
		//Battle loop starts
		while(running){
    		if (goodguy.isDead()){	//Goodguy dies
    			System.out.println(goodguy.getName()+" fainted!");
    			for (int i=0; i<4; i++){
    				if (goodguy.getName().equals(goodguys.get(i).getName())){
    					goodguys.set(i, goodguy);
    				}
    			}
    			choosePokemon();
    		}
    		if (badguy.isDead()){	//Badguy dies
    			winner = goodguy.getName();
    			running = false;
    			//Get rid of badguy from list
    			for (int i=0; i<badguys.size(); i++){
    				if (badguy.getName().equals(badguys.get(i).getName())){
    					badguys.remove(badguys.get(i));
    				}
    			}
    		}
    		if (turn==1){	//1 is goodguy's turn
    			System.out.println("**********Goodguy's turn**********");
				playerTurn(goodguy, badguy);
				System.out.println("------------------------------------");
				//Displays current stats of each Pokemon
    			goodguy.display();
    			badguy.display();
    			System.out.println("------------------------------------");
    			//Everyone is recovered +10 energy
    			for (int i=0; i<4; i++){
    				goodguys.get(i).recover();
    			}
    			goodguy.recover();
    			badguy.recover();
	    		turn = 0;	//Badguy goes next
			}
			else{	//0 is badguy's turn
				System.out.println("**********Badguy's turn**********");
				enemyTurn(goodguy, badguy);
				System.out.println("------------------------------------");
    			goodguy.display();
    			badguy.display();
    			System.out.println("------------------------------------");
    			//Recover everyone +10 energy
    			for (int i=0; i<4; i++){
    				goodguys.get(i).recover();
    			}
    			goodguy.recover();
    			badguy.recover();
	    		turn = 1;	//Goodguy goes next
			}
    	}
    	System.out.println("END OF BATTLE "+battleNum+"\n");
    	System.out.printf("Winner: %s%n%n", winner);
	}
	
	//Used only in start of game
	//Allows user to pick 4 pokemons and makes badguys out of unchosen ones
	//Restarts if user enters same number twice
	private static void chooseTeams(){
		//Display all pokemon
    	System.out.println("Pick 4 to battle with! (Enter 4 numbers)");
    	for (int i=0; i<28; i++){
    		Pokemon p = all.get(i);
    		System.out.printf("%d. %s) Hp: %d Type: %s Resistance: %s Weakness: %s%n", i+1, p.getName(), p.getHp(), p.getType(), p.getResistance(), p.getWeakness());
    	}

    	int pick1 = kb.nextInt()-1;
    	goodguys.add(all.get(pick1));
    	int pick2 = kb.nextInt()-1;
    	if (pick2 == pick1){
      		System.out.println("Restart");
      		System.out.println("------------------------------------");
      		goodguys = new ArrayList<Pokemon>();
      		chooseTeams();
      		return;
    	}
    	else{
    		goodguys.add(all.get(pick2));
    	}
    	
    	int pick3 = kb.nextInt()-1;
    	if (pick3 == pick1 || pick3 == pick2){
      		System.out.println("Restart");
      		System.out.println("------------------------------------");
      		goodguys = new ArrayList<Pokemon>();
      		chooseTeams();
      		return;
    	}
    	else{
    		goodguys.add(all.get(pick3));
    	}
    	
    	int pick4 = kb.nextInt()-1;
    	if (pick4 == pick1 || pick4 == pick2 || pick4 == pick3){
      		System.out.println("Restart");
      		System.out.println("------------------------------------");
      		goodguys = new ArrayList<Pokemon>();
      		chooseTeams();
      		return;
    	}
    	else{
    		goodguys.add(all.get(pick4));
    	}
    	
    	//Create badguys with leftover Pokemons
    	for (int i=0; i<28; i++){
    		if (i!=pick1 && i!=pick2 && i!=pick3 && i!=pick4){
    			badguys.add(all.get(i));
    		}
    	}
	}
	
	//Displays available pokemon to choose
	//Used for Retreat
	//Used at the beginning of every new battle
	private static void choosePokemon(){
		System.out.println("Choose a Pokemon:");
    	for (int i=0; i<goodguys.size(); i++){
    		System.out.printf("%d. %s%n", i+1, goodguys.get(i).getName());
    	}
    	int select = kb.nextInt();
    	//Doesn't allow user to choose a dead pokemon
		if ((goodguys.get(select-1)).getHp() == 0){
			System.out.println("No can do. Choose a different one.");
			choosePokemon();
		}
		else{
			if (goodguy!=null){	//If there's a game happening
				//Replace the pokemon in the ArrayList with the updated one
				for (int i=0; i<4; i++){
					if ((goodguys.get(i).getName()).equals(goodguy.getName())){
						goodguys.set(i, goodguy);
					}
				}
			}
			goodguy = goodguys.get(select-1);
			System.out.printf("%s, I choose you!%n", goodguy.getName());
		}
	}
	
	//Displays a pokemon's attacks and let's user to choose one
	//If not enough energy, allows user to rechoose attack
	private static void chooseAttack(){
		System.out.println("Choose an attack:");
		for (int i=0; i<goodguy.getAttacks().size(); i++){
			System.out.print(i+1+". ");
			goodguy.getAttacks().get(i).display();
		}
		int pick = kb.nextInt()-1;
		Attack chosen = goodguy.getAttacks().get(pick);
		if (chosen.getEnergy() <= goodguy.getEnergy()){
			goodguy.attack(chosen, badguy);
		}
		else{
			System.out.println("Not enough energy");
			chooseAttack();
		}
	}
	
	//Method to carry out when the computer has a turn
	//Randomly determines the attack (can't retreat)
	//Unless not enough energy, then pass
	public static void enemyTurn(Pokemon goodguy, Pokemon badguy){
		if (badguy.getStunned()){	//Check if it was stunned
			badguy.pass();
			badguy.setStunned(false);
			return;
		}
		//Don't proceed if either player is dead
		if (goodguy.isDead()){
			return;
		}
		if (badguy.isDead()){
			return;
		}
		else{
			ArrayList<Attack> available = badguy.availableAttacks();
			int n = available.size();
			if (n>0){
				Attack chosen = badguy.getAttacks().get(rand.nextInt(n));
				//System.out.printf("%s attacks %s with %s!%n", p1.name, p2.name, attack.name);
				badguy.attack(chosen, goodguy);
			}
			else{
				badguy.pass();
			}
		}
	}
	
	//Method to carry out player's turn
	//Displays actions and lets user choose attack, retreat, or pass
	public static void playerTurn(Pokemon goodguy, Pokemon badguy){
		if (goodguy.getStunned()){	//Check if it was stunned
			goodguy.pass();
			goodguy.setStunned(false);
			return;
		}
		//Return if a player/enemy is already dead
		if (badguy.isDead()){
			return;
		}
		if (goodguy.isDead()){
			return;
		}
		else{
			System.out.println("Choose an action:");
	    	System.out.printf("1.%s%n2.%s%n3.%s%n", "Attack", "Retreat", "Pass");
	    	int choice = kb.nextInt();
			if (choice == 1){
				chooseAttack();
			}
			else if (choice == 2){
				choosePokemon();
			}
			else if (choice == 3){
				goodguy.pass();
			}
		}
	}
}