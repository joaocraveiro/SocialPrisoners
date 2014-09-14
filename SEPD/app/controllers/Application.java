package controllers;

import static play.data.Form.form;
import models.Game;
import models.Matchup;
import models.Player;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
  
	@Security.Authenticated(Secured.class)
    public static Result index() {
    	Game game = Game.getAll().get(0);
    	if (game.round == Game.ROUNDLIMIT){
			 return redirect(routes.Application.end());				    
		}
    	else {
    		game.update();
    		Player user = Player.find.byId(request().username());
    		Matchup matchup = game.getMatchup(user);
    		return ok(views.html.index.render(user,matchup,game,Player.getAllSocialRanked()));
    	}
    }
		
	// TODO: MORE SECURITY USING THIS
	@Security.Authenticated(Secured.class)
    public static Result restart() {
    	Game game = Game.getAll().get(0);    	
    	game.restart();    	
    	return redirect(routes.Application.index());	
    }
	
	@Security.Authenticated(Secured.class)
    public static Result end() {
    	Game game = Game.getAll().get(0);    	
    	Player user = Player.find.byId(request().username());    
    	return ok(views.html.end.render(user,game,Player.getAllEconRanked(),Player.getAllSocialRanked()));
    }
    
    public static Result login() {
		return ok(views.html.login.render(form(Login.class)));
	}
	
	public static Result logout() {
	    session().clear();
	    flash("success", "You've been logged out");
	    return redirect(
	        routes.Application.login()
	    );
	}

	public static Result authenticate() {
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(login.render(loginForm));
		} else {
			session().clear();
			session("name", loginForm.get().name);
			return redirect(routes.Application.index());
		}
	}
  

public static class Login {

	public String name;
	public String password;

	public String validate() {
		if (Player.authenticate(name, password) == null) {
			return "Invalid user or password";
		}
		return null;
	}
}

}

