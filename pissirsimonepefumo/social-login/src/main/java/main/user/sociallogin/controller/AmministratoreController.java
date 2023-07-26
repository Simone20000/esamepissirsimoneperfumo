 package main.user.sociallogin.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class AmministratoreController {

    @RequestMapping("/secured")
	public String amministratore() {
		System.out.println("\n\n\n\n\n errore");
		return "amministratore";
	}
    
}
