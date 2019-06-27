package org.romanchi;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App {
    public static void main( String[] args ) {
        SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(App.class);
        ApplicationContext applicationContext = applicationBuilder.run(args);
    }
}
