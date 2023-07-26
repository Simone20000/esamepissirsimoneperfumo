package main.user.sociallogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import main.user.sociallogin.manager.ParkingManager;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println("funziona su https://localhost:8000");
        ParkingManager ParkingManager=new ParkingManager();

    }
	

}
