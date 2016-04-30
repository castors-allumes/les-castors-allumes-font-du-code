package org.castors;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.boot.SpringApplication.run;

/**
 * Created by jauparts on 29/04/2016.
 */
@EnableAutoConfiguration
@ComponentScan(basePackages = {"org.castors.controller"})
public class FirstController {

    @RequestMapping("/")
    public ModelAndView home() {

        ModelAndView mav = new ModelAndView("index");

        return mav;

//        return "templates/index";
    }

    public static void main(String[] args) throws Exception {
        run(FirstController.class, args);
    }
}
