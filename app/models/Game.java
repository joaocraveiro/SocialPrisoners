package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import controllers.routes;
import play.data.validation.Constraints.Required;
import com.avaje.ebean.Model;

@Entity
public class Game extends Model {

	static final long serialVersionUID = 1L;
	
	public static final String version = "DESCEND";
	
	public static final int ROUNDLIMIT = 10;
	
	public static final int ECONDEF = -4;
	public static final int ECONLOSS = -5;
	public static final int ECONWIN = 0;
	public static final int ECONCOOP = -3;
	
	public static final int SOCIALDEF = 2;
	public static final int SOCIALLOSS = 0;
	public static final int SOCIALWIN = 6;
	public static final int SOCIALCOOP = 4;
	
	final int STARTROUNDS = ROUNDLIMIT; // RANDOM MATCHUP ALGORITHM
	
	@Required
	@Id
	public int id = 1;
	
	public int round = 1;
	
	public int waitingPairs;
	
	private boolean roundDone = true;
	
	@OneToMany(mappedBy="game")
	public List<Player> players = new ArrayList<Player>();
	
	@OneToMany(mappedBy="game")
	public List<Matchup> matchups = new ArrayList<Matchup>();
	
	public static Finder<String, Game> find = new Finder<String, Game>(
			String.class, Game.class);

	public static List<Game> getAll() {		
		return find.all();				
	}
	
	public void update(){			
		if(roundDone){			
			matchup();
			playBots();
			roundDone = false;
			this.save();
		}
	}
	
	public Matchup getMatchup(Player player){
		for(Matchup matchup : matchups){					
			if(matchup.player1.name.equals(player.name) && matchup.round == round){
				return matchup;				
			} else if(matchup.player2.name.equals(player.name) && matchup.round == round) {
				return matchup;				
			}
		}
		return null;
	}
	
	// TODO: MATCHUP NEEDS TO BECOME AN ENTITY TO BE PERSISTED
	public void matchup(){
		List<Player> allplayers = Player.getAll();
		ArrayList<Integer> taken = new ArrayList<Integer>();				
		// random begining
		if(round < STARTROUNDS){
			for(int i = 0; i < allplayers.size()/2; i++){
				Integer index1;
				Integer index2;
				index1 = (int)Math.round(Math.random()*(allplayers.size()-1));
				while(taken.contains(index1)){
					index1 = (int)Math.round(Math.random()*(allplayers.size()-1));
				}
				index2 = index1;
				while(index2 == index1 || taken.contains(index2)){
					index2 = (int)Math.round(Math.random()*(allplayers.size()-1));
				}
				taken.add(index1);
				taken.add(index2);
				Matchup m = new Matchup();
				m.id = Matchup.getAll().size()+1;
				m.player1 = allplayers.get(index1);					
				m.player2 = allplayers.get(index2);
				m.round = this.round;
				m.game = this;
				Matchup.create(m);
				matchups.add(m);
			}
		} else {
			// TODO: SOCIAL MATCHUP ALGORITHM
		}		
	}
	
	public void endround(){
		waitingPairs--;		
		if(waitingPairs == 0){
			System.out.println("ROUND END!");
			this.round++;
			this.roundDone = true;			
			waitingPairs = Player.getAll().size()/2;						
		}
		this.save();
	}
	
	public void playBots(){
		for(Player player : Player.getAll()){
			if(player.npc){
				Matchup matchup = getMatchup(player);								
				if(player.name.equals(matchup.player1.name)){
					matchup.p1choice1 = randomChoice();							
					matchup.p1choice2 = randomChoice();	
					if(matchup.p2choice2 != 0){ this.waitingPairs--; this.save(); }
				} else {
					matchup.p2choice1 = randomChoice();									
					matchup.p2choice2 = randomChoice();
					if(matchup.p1choice2 != 0){ this.waitingPairs--; this.save(); }
				}
				matchup.save();
			}
		}
	}
	
	private int randomChoice(){
		int choice = (int)Math.round(Math.random()*1);
		if(choice > 0){
			return -1;
		} else {
			return 1;
		}
	}
	
	public void restart(){
		this.round = 1;
		this.roundDone = true;
		this.waitingPairs = (int)Player.getAll().size()/2;
		for(Matchup matchup : Matchup.getAll()){
			matchup.delete();
		}
		matchups.clear();
		for(Player player : Player.getAll()){			
			player.restart();
		}
		this.save();
	}
	
}
