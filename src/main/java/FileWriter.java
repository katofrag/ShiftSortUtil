import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;


public class FileWriter {
    private static final Logger log = LoggerFactory.getLogger(FileWriter.class);

    public <T> void writeFile(String fileName, String prefix, Path outputPath, boolean aOptionEnable, List<T> data) throws IOException {
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
                log.info("The data was written to the file: {}", path);
            } catch (IOException e) {
                log.error("Error writing to file {}", path);
                System.out.println(e.getMessage());
                throw e;
            }
        }
    }
}

