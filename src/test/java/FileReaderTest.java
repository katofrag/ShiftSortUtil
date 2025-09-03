import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileReaderTest {

    @Test
    void processInputFiles_shouldParseAllDataTypesCorrectly(@TempDir Path tempDir) throws Exception {
        FileReader fileReader = new FileReader();
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, List.of("123", "45.67", "hello", "-100", "3.14", "world"));

        fileReader.processInputFiles(testFile);

        assertAll(
                () -> assertEquals(List.of(123L, -100L), fileReader.getIntegers()),
                () -> assertEquals(List.of(45.67, 3.14), fileReader.getFloats()),
                () -> assertEquals(List.of("hello", "world"), fileReader.getStrings())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"test.dat", "test.xml", "file"})
    void processInputFiles_shouldIgnoreNonTxtFiles(String filename, @TempDir Path tempDir) throws Exception {
        FileReader fileReader = new FileReader();
        Path testFile = tempDir.resolve(filename);
        Files.write(testFile, List.of("123", "test"));

        fileReader.processInputFiles(testFile);

        assertAll(
                () -> assertTrue(fileReader.getIntegers().isEmpty()),
                () -> assertTrue(fileReader.getFloats().isEmpty()),
                () -> assertTrue(fileReader.getStrings().isEmpty())
        );
    }

    @Test
    void processInputFiles_shouldHandleNonExistentFileGracefully(@TempDir Path tempDir) {
        FileReader fileReader = new FileReader();
        Path nonExistentFile = tempDir.resolve("nonexistent.txt");

        assertDoesNotThrow(() -> fileReader.processInputFiles(nonExistentFile));
    }

    @Test
    void processInputFiles_shouldSkipEmptyAndWhitespaceLines(@TempDir Path tempDir) throws Exception {
        FileReader fileReader = new FileReader();
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, List.of("", "   ", "\t", "valid", "  42  "));

        fileReader.processInputFiles(testFile);

        assertAll(
                () -> assertEquals(List.of(42L), fileReader.getIntegers()),
                () -> assertTrue(fileReader.getFloats().isEmpty()),
                () -> assertEquals(List.of("valid"), fileReader.getStrings())
        );
    }
}