import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileReaderTest {

    private FileReader fileReader;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        fileReader = new FileReader();
    }

    @Test
    void processInputFilesShouldParseAllDataTypesCorrectly() throws Exception {
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
    void processInputFilesShouldIgnoreNonTxtFiles(String filename) throws Exception {
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
    void processInputFilesShouldHandleNonExistentFileGracefully() {
        Path nonExistentFile = tempDir.resolve("test.txt");

        assertDoesNotThrow(() -> fileReader.processInputFiles(nonExistentFile));
    }

    @Test
    void processInputFilesShouldSkipEmptyAndWhitespaceLines() throws Exception {
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