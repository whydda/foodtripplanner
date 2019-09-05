package kr.cibusiter.foodplanner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FoodplannerApplicationTests {

    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() throws Exception {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Test
    public void encode() {
        String password = "password";

        String encPassword = passwordEncoder.encode(password);

        System.out.println(passwordEncoder.matches(password, encPassword));
    }

    @Test
    public void contextLoads() {
    }

}
