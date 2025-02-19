import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileReader {
    private static final Logger log = LoggerFactory.getLogger(FileReader.class);
    private final List<String> strings = new ArrayList<>();
    private final List<Long> integers = new ArrayList<>();
    private final List<Double> floats = new ArrayList<>();
    private int countNoValidFile = 0;

    public void processInputFiles(Path path) throws IOException {
        if (!Files.exists(path)) {
            log.error("Error: File does not exist at the specified path: {}", path);
            System.out.println("Error: File does not exist at the specified path: " + path);
            countNoValidFile++;
            return;
        }
        if (!path.toString().endsWith(".txt")) {
            log.error("Error: Invalid file format. Expected \"filename.txt\", accepted file: {}", path);
            System.out.println("Error: Invalid file format. Expected \"filename.txt\", accepted file: " + path);
            countNoValidFile++;
            return;
        }
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(this::parseLine);
        } catch (IOException e) {
            log.error("Error while reading file: {}", path);
            throw new IOException("Error while reading file: " + path);
        }
        log.info("File {} read and data committed", path);
    }

    private void parseLine(String line) {
        line = line.trim();
        try {
            integers.add(Long.parseLong(line));
        } catch (NumberFormatException e) {
            try {
                floats.add(Double.parseDouble(line));
            } catch (NumberFormatException ex) {
                if (!line.isEmpty())
                    strings.add(line);
            }
        }
    }

    public List<String> getStrings() {
        return strings;
    }

    public List<Long> getIntegers() {
        return integers;
    }

    public List<Double> getFloats() {
        return floats;
    }

    public int getCountNoValidFile() {
        return countNoValidFile;
    }
}
