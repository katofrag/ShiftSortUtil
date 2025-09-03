import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileWriterTest {

    @Test
    void writeFile_shouldCreateNewFileWithData(@TempDir Path tempDir) throws Exception {
        FileWriter fileWriter = new FileWriter();
        List<String> testData = List.of("line1", "line2", "line3");

        fileWriter.writeFile("test.txt", "prefix_", tempDir, false, testData);

        Path expectedFile = tempDir.resolve("prefix_test.txt");
        assertAll(
                () -> assertTrue(Files.exists(expectedFile)),
                () -> assertEquals(testData, Files.readAllLines(expectedFile))
        );
    }

    @Test
    void writeFile_shouldAppendToExistingFile(@TempDir Path tempDir) throws Exception {
        FileWriter fileWriter = new FileWriter();
        Path existingFile = tempDir.resolve("prefix_test.txt");
        Files.write(existingFile, List.of("existing_line"));

        fileWriter.writeFile("test.txt", "prefix_", tempDir, true, List.of("new_line"));

        assertEquals(List.of("existing_line", "new_line"), Files.readAllLines(existingFile));
    }

    @Test
    void writeFile_shouldOverwriteExistingFileWhenAppendDisabled(@TempDir Path tempDir) throws Exception {
        FileWriter fileWriter = new FileWriter();
        Path existingFile = tempDir.resolve("prefix_test.txt");
        Files.write(existingFile, List.of("old_content"));

        fileWriter.writeFile("test.txt", "prefix_", tempDir, false, List.of("new_content"));

        assertEquals(List.of("new_content"), Files.readAllLines(existingFile));
    }

    @Test
    void writeFile_shouldSkipEmptyData(@TempDir Path tempDir) throws Exception {
        FileWriter fileWriter = new FileWriter();

        fileWriter.writeFile("test.txt", "prefix_", tempDir, false, List.of());

        assertFalse(Files.exists(tempDir.resolve("prefix_test.txt")));
    }
}