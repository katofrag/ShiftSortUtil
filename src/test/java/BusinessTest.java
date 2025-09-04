import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BusinessTest {

    private Business business;
    private Path outputDir;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        business = new Business();
        outputDir = tempDir.resolve("output");
    }

    @Test
    void processShouldProcessFilesAndCreateOutput() throws Exception {
        Path inputFile = tempDir.resolve("input.txt");
        Files.write(inputFile, List.of("100", "45.5", "hello"));
        Files.createDirectory(outputDir);

        Params params = new Params(outputDir, "", false, false, false, List.of(inputFile));

        assertDoesNotThrow(() -> business.process(params));

        assertAll(
                () -> assertTrue(Files.exists(outputDir.resolve("integers.txt"))),
                () -> assertTrue(Files.exists(outputDir.resolve("floats.txt"))),
                () -> assertTrue(Files.exists(outputDir.resolve("strings.txt")))
        );
    }

    @Test
    void processShouldHandleMultipleFiles() throws Exception {
        Path file1 = tempDir.resolve("file1.txt");
        Path file2 = tempDir.resolve("file2.txt");
        Files.write(file1, List.of("100"));
        Files.write(file2, List.of("200"));
        Files.createDirectory(outputDir);

        Params params = new Params(outputDir, "", false, false, false, List.of(file1, file2));

        assertDoesNotThrow(() -> business.process(params));

        assertAll(
                () -> assertTrue(Files.exists(outputDir.resolve("integers.txt"))),
                () -> assertTrue(Files.readAllLines(outputDir.resolve("integers.txt"))
                        .containsAll(List.of("100", "200")))
        );
    }
}