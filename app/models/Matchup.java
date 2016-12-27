package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import com.avaje.ebean.Model;

@Entity
public class Matchup extends Model {

	static final long serialVersionUID = 3L;	
	
	@Required
	@Id
	public int id;	
	
	@OneToOne
	public Player player1;
	
	@OneToOne
	public Player player2;
	
	@ManyToOne
	public Game game;
	
	public int round;
	
	public int p1choice1=0;
	public int p2choice1=0;
	
	public int p1choice2=0;
	public int p2choice2=0;
		
	public static void create(Matchup matchup){
		matchup.save();
	}
	
	public static Finder<String, Matchup> find = new Finder<String, Matchup>(
			String.class, Matchup.class);

	public static List<Matchup> getAll() {		
		return find.all();				
	}
	
	

	
}
