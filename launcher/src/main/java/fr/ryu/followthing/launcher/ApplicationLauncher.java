package fr.ryu.followthing.launcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoDataAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@ComponentScan(
//        includeFilters = {
//                @ComponentScan.Filter(
//                        type = FilterType.REGEX,
//                        pattern = {
//                                "fr.ryu.followthing.launcher.ApplicationLauncher",
//                                "fr.ryu.followthing.config.*",
//                                "fr.ryu.followthing.service.*"
//                        }
//                )
//        }
        basePackages = {
                "fr.ryu.followthing.launcher",
                "fr.ryu.followthing.config.resources",
                "fr.ryu.followthing.config.bean",
                "fr.ryu.followthing.service.rest",
                "fr.ryu.followthing.service.persistence"
        }
)
@EnableAutoConfiguration(exclude = {
        MongoDataAutoConfiguration.class
})
public class ApplicationLauncher {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationLauncher.class, args);
    }
}
