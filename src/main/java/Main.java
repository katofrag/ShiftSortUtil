import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            Business business = new Business();
            ParamsParser parser = new ParamsParser();
            Params params = null;
            try {
                params = parser.parsedArgs(args, parser);
                System.out.println(params);
            } catch (ParseException e) {
                logger.error(e.getMessage());
                System.exit(2);
            } catch (IOException e){
                logger.info(e.getMessage());
                System.exit(3);
            }
            business.process(params);
        } catch (Exception e) {
            System.out.println("Unexpected error occurred: " + e.getMessage());
            System.exit(4);
        }
    }
}