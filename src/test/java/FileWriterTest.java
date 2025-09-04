import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileWriterTest {

    private FileWriter fileWriter;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        fileWriter = new FileWriter();
    }

    @Test
    void writeFileShouldCreateNewFileWithData() throws Exception {
        List<String> testData = List.of("line1", "line2", "line3");

        fileWriter.writeFile("test.txt", "prefix_", tempDir, false, testData);

        Path expectedFile = tempDir.resolve("prefix_test.txt");
        assertAll(
                () -> assertTrue(Files.exists(expectedFile)),
                () -> assertEquals(testData, Files.readAllLines(expectedFile))
        );
    }

    @Test
    void writeFileShouldAppendToExistingFile() throws Exception {
        Path existingFile = tempDir.resolve("prefix_test.txt");
        Files.write(existingFile, List.of("existing_line"));

        fileWriter.writeFile("test.txt", "prefix_", tempDir, true, List.of("new_line"));

        assertEquals(List.of("existing_line", "new_line"), Files.readAllLines(existingFile));
    }

    @Test
    void writeFileShouldOverwriteExistingFileWhenAppendDisabled() throws Exception {
        Path existingFile = tempDir.resolve("prefix_test.txt");
        Files.write(existingFile, List.of("old_content"));

        fileWriter.writeFile("test.txt", "prefix_", tempDir, false,
                List.of("new_content"));

        assertEquals(List.of("new_content"), Files.readAllLines(existingFile));
    }

    @Test
    void writeFileShouldSkipEmptyData() throws Exception {
        fileWriter.writeFile("test.txt", "prefix_", tempDir, false, List.of());

        assertFalse(Files.exists(tempDir.resolve("prefix_test.txt")));
    }
}