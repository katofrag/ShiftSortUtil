import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileReader {
    List<String> strings = new ArrayList<>();
    List<Integer> integers = new ArrayList<>();
    List<Double> doubles = new ArrayList<>();

    public void processInputFiles(Path path) throws IOException {
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(this::parseLine);
        }
    }
    private void parseLine(String line) {
        line = line.trim();
        try {
            integers.add(Integer.parseInt(line));
        } catch (NumberFormatException e) {
            try {
                doubles.add(Double.parseDouble(line));
            } catch (NumberFormatException ex) {
                if(!line.isEmpty())
                    strings.add(line);
            }
        }
    }
}