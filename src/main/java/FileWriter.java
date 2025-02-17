import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;


public class FileWriter {
    public <T> void writeFile(String fileName, String prefix, Path outputPath, boolean aOptionEnable, List<T> data) {
        if (!data.isEmpty()) {

            Path path = Paths.get(outputPath.toString(), prefix + fileName);
            StandardOpenOption option = aOptionEnable ? StandardOpenOption.APPEND : StandardOpenOption.CREATE;

            try {
                if (Files.exists(path) && !aOptionEnable) {
                    option = StandardOpenOption.TRUNCATE_EXISTING;
                }

                List<String> lines = data.stream()
                        .map(Object::toString)
                        .toList();

                Files.write(path, lines, option);
            } catch (IOException e) {
                System.out.println("Error writing to file " + path + ": " + e.getMessage());
            }
        }
    }
}

