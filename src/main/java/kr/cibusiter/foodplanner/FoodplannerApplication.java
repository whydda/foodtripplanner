package kr.cibusiter.foodplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class FoodplannerApplication {
    public static void main(String[] args)
    {
        SpringApplication api = new SpringApplication(FoodplannerApplication.class);
        api.addListeners(new ApplicationPidFileWriter());
        api.run(args);

    }

}
