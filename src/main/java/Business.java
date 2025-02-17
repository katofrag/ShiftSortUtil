import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Business {
    private final Logger logger = LoggerFactory.getLogger(Business.class);
    public void process(Params params) throws IOException {
        FileReader fileReader = new FileReader();
        FileWriter fileWriter = new FileWriter();
        Statistic statistic = new Statistic();

        fileReader.processInputFiles(params.file1());
        fileReader.processInputFiles(params.file2());

        statistic.displayStatistics(params.sEnable(), params.fEnable(), fileReader);

        fileWriter.writeFile("integers.txt", params.prefix(), params.outPath(), params.aEnable(), fileReader.integers);
        fileWriter.writeFile("floats.txt", params.prefix(), params.outPath(), params.aEnable(), fileReader.doubles);
        fileWriter.writeFile("strings.txt", params.prefix(), params.outPath(), params.aEnable(), fileReader.strings);
        logger.info("Записано в файл {}", fileReader.strings );
        
    }
}
