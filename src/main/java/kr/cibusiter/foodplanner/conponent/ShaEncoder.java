package kr.cibusiter.foodplanner.conponent;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ShaEncoder {

 private PasswordEncoder shaPasswordEncoder;

 public String encoding(String str){
  return shaPasswordEncoder.encode(str);
 }
}
