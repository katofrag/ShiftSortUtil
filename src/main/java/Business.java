import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Business {
    private static final Logger log = LoggerFactory.getLogger(Business.class);
    private final String integers = "integers.txt";
    private final String floats = "floats.txt";
    private final String strings = "strings.txt";

    public void process(Params params) throws IOException, ParseException {
        FileReader fileReader = new FileReader();
        FileWriter fileWriter = new FileWriter();
        Statistic statistic = new Statistic();

        try {
            if (params.aEnable()) {
                validateFileExistence(params);
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            throw e;
        }

        for (Path path : params.files()){
            fileReader.processInputFiles(path);
        }

        statistic.displayStatistics(params.sEnable(), params.fEnable(), fileReader);

        fileWriter.writeFile(integers, params.prefix(), params.outPath(), params.aEnable(), fileReader.getIntegers());
        fileWriter.writeFile(floats, params.prefix(), params.outPath(), params.aEnable(), fileReader.getFloats());
        fileWriter.writeFile(strings, params.prefix(), params.outPath(), params.aEnable(), fileReader.getStrings());
    }

    private void validateFileExistence(Params params) throws ParseException {
        Path[] paths = {
                Paths.get(params.outPath().toString(), params.prefix() + integers),
                Paths.get(params.outPath().toString(), params.prefix() + floats),
                Paths.get(params.outPath().toString(), params.prefix() + strings)
        };

        for (Path path : paths) {
            if (!Files.exists(path)) {
                log.error("For append to the file, it must exist. File not found: {}", path);
                throw new ParseException("For append to the file, it must exist. File not found: " + path);
            }
        }
    }
}
