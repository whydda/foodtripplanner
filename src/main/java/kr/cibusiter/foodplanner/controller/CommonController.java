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

    @GetMapping(value = "/pub/portfolio/1")
    public String portfolio1(){
        return "/tl/portfolio-1-col.html";
    }

    @GetMapping(value = "/pub/portfolio/2")
    public String portfolio2(){
        return "/tl/portfolio-2-col.html";
    }

    @GetMapping(value = "/pub/portfolio/3")
    public String portfolio3(){
        return "/tl/portfolio-3-col.html";
    }

    @GetMapping(value = "/pub/portfolio/4")
    public String portfolio4(){
        return "/tl/portfolio-4-col.html";
    }

    @GetMapping(value = "/pub/portfolio/item")
    public String portfolioItem(){
        return "/tl/portfolio-item.html";
    }

    @GetMapping(value = "/pub/blog/home/1")
    public String blogHome1(){
        return "/tl/blog-home-1.html";
    }

    @GetMapping(value = "/pub/blog/home/2")
    public String blogHome2(){
        return "/tl/blog-home-2.html";
    }

    @GetMapping(value = "/pub/blog/post")
    public String blogPost(){
        return "/tl/blog-post.html";
    }

    @GetMapping(value = "/pub/fullwidth")
    public String fullwidth(){
        return "/tl/full-width.html";
    }

    @GetMapping(value = "/pub/sidebar")
    public String sidebar(){
        return "/tl/portfolio-item.html";
    }

    @GetMapping(value = "/pub/faq")
    public String faq(){
        return "/tl/faq.html";
    }

    @GetMapping(value = "/errors/error")
    public String exception404(){
        return "/tl/404.html";
    }

    @GetMapping(value = "/pub/pricing")
    public String pricing(){
        return "/tl/pricing.html";
    }
}
