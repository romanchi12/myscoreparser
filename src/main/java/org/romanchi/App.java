package org.romanchi;

import org.romanchi.myscore.services.ClientService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import java.text.ParseException;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App {
    public static void main( String[] args ) throws ParseException {
        SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(App.class);
        ApplicationContext applicationContext = applicationBuilder.run(args);
        ClientService clientService = applicationContext.getBean(ClientService.class);
        clientService.parse();
    }
}
