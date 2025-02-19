import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            log.info("Application started");
            ParamsParser parser = new ParamsParser();
            Business business = new Business();
            Params params;
            try {
                params = parser.parsedArgs(args, parser);
                log.info("Parameters passed to our application {}", params);
                business.process(params);
            } catch (ParseException e) {
                System.exit(2);
            } catch (IOException e) {
                System.exit(3);
            }

        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            System.out.println("Unexpected error occurred: " + e.getMessage());
            System.exit(4);
        }
    }
}