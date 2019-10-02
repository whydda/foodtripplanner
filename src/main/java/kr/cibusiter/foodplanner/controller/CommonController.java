package kr.cibusiter.foodplanner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by IntelliJ IDEA.
 * User: whydda
 * Date: 2019-09-05
 * Time: 오후 8:01
 */
@Controller
public class CommonController {

    @GetMapping(value = "/login/page")
    public String loginPage(){
        return "/tl/index";
    }
}
