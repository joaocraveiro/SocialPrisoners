package controllers;

import models.Game;
import models.Matchup;
import models.Player;
import play.mvc.*;

public class Choices extends Controller {
  
	
	// ECONOMIC METHODS
	
	@Security.Authenticated(Secured.class)
    public static Result defect1() {
		Game game = Game.getAll().get(0);
    	Player user = Player.find.byId(request().username());
    	Matchup matchup = game.getMatchup(user);		
    	if(matchup.player1.name.equals(user.name)){
    		matchup.p1choice1 = -1;
    		matchup.save();
    	} else {
    		matchup.p2choice1 = -1;
    		matchup.save();
    	}
    	if(matchup.p1choice1 != 0 && matchup.p2choice1 != 0){
    		econscore(matchup);
    	}
		return redirect(routes.Application.index());
    }       
	
	@Security.Authenticated(Secured.class)
    public static Result coop1() {		
		Game game = Game.getAll().get(0);
    	Player user = Player.find.byId(request().username());
    	Matchup matchup = game.getMatchup(user);
    	if(matchup.player1.name.equals(user.name)){
    		matchup.p1choice1 = 1;
    		matchup.save();
    	} else {
    		matchup.p2choice1 = 1;
    		matchup.save();
    	}	
    	if(matchup.p1choice1 != 0 && matchup.p2choice1 != 0){
    		econscore(matchup);
    	}    	
		return redirect(routes.Application.index());
    }
	
	// SOCIAL METHODS
	
	@Security.Authenticated(Secured.class)
    public static Result defect2() {
		Game game = Game.getAll().get(0);
    	Player user = Player.find.byId(request().username());
    	Matchup matchup = game.getMatchup(user);
    	if(matchup.p1choice1 != 0 && matchup.p1choice1 != 0){
    		if(matchup.player1.name.equals(user.name)){
        		matchup.p1choice2 = -1;
        		matchup.save();
        	} else {
        		matchup.p2choice2 = -1;
        		matchup.save();
        	}
    		// finish round
    		if(matchup.p1choice2 != 0 && matchup.p2choice2 != 0){
    			socialscore(matchup);    			
    			game.endround();    			
    		}
    	}    	
		return redirect(routes.Application.index());
	}
	
	
	@Security.Authenticated(Secured.class)
    public static Result coop2() {
		Game game = Game.getAll().get(0);
    	Player user = Player.find.byId(request().username());
    	Matchup matchup = game.getMatchup(user);
    	if(matchup.p1choice1 != 0 && matchup.p1choice1 != 0){
    		if(matchup.player1.name.equals(user.name)){
        		matchup.p1choice2 = 1;
        		matchup.save();
        	} else {
        		matchup.p2choice2 = 1;
        		matchup.save();
        	}
    		// finish round
    		if(matchup.p1choice2 != 0 && matchup.p2choice2 != 0){
    			socialscore(matchup);    			
    			game.endround();    			
    		}
    	}
		return redirect(routes.Application.index());
	}
	
	public static void econscore(Matchup matchup){
		
		// ECONOMIC
		if(matchup.p1choice1 == -1 && matchup.p2choice1 == -1){			
				matchup.player1.econScore(Game.ECONDEF);
				matchup.player2.econScore(Game.ECONDEF);			
		}
		if(matchup.p1choice1 == 1 && matchup.p2choice1 == -1){			
				matchup.player1.econScore(Game.ECONLOSS);
				matchup.player2.econScore(Game.ECONWIN);			
		}
		if(matchup.p1choice1 == -1 && matchup.p2choice1 == 1){			
				matchup.player1.econScore(Game.ECONWIN);
				matchup.player2.econScore(Game.ECONLOSS);			
		}
		if(matchup.p1choice1 == 1 && matchup.p2choice1 == 1){
				matchup.player1.econScore(Game.ECONCOOP);
				matchup.player2.econScore(Game.ECONCOOP);		
		}
	}
	
	public static void socialscore(Matchup matchup){
		// SOCIAL
		if(matchup.p1choice2 == -1 && matchup.p2choice2 == -1){			
			matchup.player1.socialScore(Game.SOCIALDEF);
			matchup.player2.socialScore(Game.SOCIALDEF);
		}
		if(matchup.p1choice2 == 1 && matchup.p2choice2 == -1){			
			matchup.player1.socialScore(Game.SOCIALLOSS);
			matchup.player2.socialScore(Game.SOCIALWIN);
		}
		if(matchup.p1choice2 == -1 && matchup.p2choice2 == 1){			
			matchup.player1.socialScore(Game.SOCIALWIN);
			matchup.player2.socialScore(Game.SOCIALLOSS);
		}
		if(matchup.p1choice2 == 1 && matchup.p2choice2 == 1){
			matchup.player1.socialScore(Game.SOCIALCOOP);
			matchup.player2.socialScore(Game.SOCIALCOOP);		
		}
	}
}

