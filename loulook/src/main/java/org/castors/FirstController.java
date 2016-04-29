package org.castors;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.boot.SpringApplication.run;

/**
 * Created by jauparts on 29/04/2016.
 */
@Controller
@EnableAutoConfiguration
public class FirstController {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        run(FirstController.class, args);
    }
}
