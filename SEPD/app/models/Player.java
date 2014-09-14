package models;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;


@Entity
public class Player extends Model {

	static final long serialVersionUID = 2L;
		
	@Required
	@Id
	public String name;
		
	@Required
	public String password;
	
	@Required
	public boolean npc;
	
	@ManyToOne
	public Game game;// = Game.getAll().get(0);
	
	@OneToOne
	public Matchup matchup;
	
	private int economicScore = 100;
	private int socialScore = 0;
	
	public static Finder<String, Player> find = new Finder<String, Player>(
			String.class, Player.class);

	public static List<Player> getAll() {		
		return find.all();				
	}
	
	public static List<Player> getAllEconRanked() {		
		List<Player> rankedPlayers = find.all();
		Collections.sort(rankedPlayers, new economicComparator());
		return rankedPlayers;
	}
	
	public static List<Player> getAllSocialRanked() {		
		List<Player> rankedPlayers = find.all();
		Collections.sort(rankedPlayers, new socialComparator());
		return rankedPlayers;
	}
	
	public static Player authenticate(String name, String password) {
		return find.where().eq("name", name).eq("password", password)
				.findUnique();
	}

	public static void create(Player player) {
		player.game = Game.getAll().get(0);
		player.save();
	}

	public void socialScore(int val){
		socialScore += val;
		this.save();
	}	
			
	public void econScore(int val){
		System.out.println("ECONOMIC SCORE: " + val);
		economicScore += val;
		this.save();
	}
	
	public int getEconomicScore(){
		return economicScore;
	}
	
	public int getSocialScore(){
		return socialScore;
	}
	
	public void restart(){
		this.economicScore = 100;
		this.socialScore = 0;
		this.matchup = null;
		this.save();
	}
	
	static class socialComparator implements Comparator<Player> {
	    @Override
	    public int compare(Player p1, Player p2) {
	    	Integer p1socialscore = p1.socialScore;
	    	Integer p2socialscore = p2.socialScore;
	    	return p2socialscore.compareTo(p1socialscore);	        
	    }
	}
	
	static class economicComparator implements Comparator<Player> {
	    @Override
	    public int compare(Player p1, Player p2) {
	    	Integer p1economicscore = p1.economicScore;
	    	Integer p2economicscore = p2.economicScore;
	    	return p2economicscore.compareTo(p1economicscore);	        
	    }
	}
	
}
