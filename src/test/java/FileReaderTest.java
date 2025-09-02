import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class FileReaderTest {

    @InjectMocks
    private FileReader fileReader;

    private final Path existingTxtPath = Path.of("existing_file.txt");
    private final Path nonExistingPath = Path.of("ghost.txt");
    private final Path nonTxtPath = Path.of("image.png");

    @Test
    void processInputFilesShouldNotModifyCollectionsWhenFileDoesNotExist() throws IOException {
        // Arrange
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.exists(nonExistingPath)).thenReturn(false);

            int initialIntSize = fileReader.getIntegers().size();
            int initialFloatSize = fileReader.getFloats().size();
            int initialStringSize = fileReader.getStrings().size();

            // Act
            fileReader.processInputFiles(nonExistingPath);

            // Assert
            assertAll("Collections should remain unchanged",
                    () -> assertEquals(initialIntSize, fileReader.getIntegers().size()),
                    () -> assertEquals(initialFloatSize, fileReader.getFloats().size()),
                    () -> assertEquals(initialStringSize, fileReader.getStrings().size())
            );
        }
    }

    @Test
    void processInputFilesShouldNotModifyCollectionsWhenFileIsNotTxt() throws IOException {
        // Arrange
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.exists(nonTxtPath)).thenReturn(true);

            int initialIntSize = fileReader.getIntegers().size();
            int initialFloatSize = fileReader.getFloats().size();
            int initialStringSize = fileReader.getStrings().size();

            // Act
            fileReader.processInputFiles(nonTxtPath);

            // Assert
            assertAll("Collections should remain unchanged for non-TXT file",
                    () -> assertEquals(initialIntSize, fileReader.getIntegers().size()),
                    () -> assertEquals(initialFloatSize, fileReader.getFloats().size()),
                    () -> assertEquals(initialStringSize, fileReader.getStrings().size())
            );
        }
    }

    @Test
    void processInputFilesShouldParseLinesCorrectlyWhenFileIsValid() throws IOException {
        // Arrange
        List<String> fileContent = List.of(
                "42",           // -> Long
                "3.14",         // -> Double
                "hello world",  // -> String
                "-100",         // -> Long
                "0.0",          // -> Double
                "",             // -> Ignored (
                "  123  "       // -> Long (тримминг работает)
        );

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.exists(existingTxtPath)).thenReturn(true);
            filesMock.when(() -> Files.lines(existingTxtPath)).thenReturn(fileContent.stream());

            // Act
            fileReader.processInputFiles(existingTxtPath);

            // Assert
            assertAll("Data should be parsed correctly from valid file",
                    () -> assertEquals(List.of(42L, -100L, 123L), fileReader.getIntegers()),
                    () -> assertEquals(List.of(3.14, 0.0), fileReader.getFloats()),
                    () -> assertEquals(List.of("hello world"), fileReader.getStrings())
            );
        }
    }

    @Test
    void processInputFilesShouldAccumulateDataWhenMultipleFilesProcessed() throws IOException {
        // Arrange
        Path firstFile = Path.of("first.txt");
        Path secondFile = Path.of("second.txt");

        List<String> firstFileContent = List.of("1", "2.0", "text1");
        List<String> secondFileContent = List.of("3", "4.0", "text2");

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.exists(firstFile)).thenReturn(true);
            filesMock.when(() -> Files.exists(secondFile)).thenReturn(true);
            filesMock.when(() -> Files.lines(firstFile)).thenReturn(firstFileContent.stream());
            filesMock.when(() -> Files.lines(secondFile)).thenReturn(secondFileContent.stream());

            // Act
            fileReader.processInputFiles(firstFile);
            fileReader.processInputFiles(secondFile);

            // Assert
            assertAll("Data should be accumulated from multiple files",
                    () -> assertEquals(List.of(1L, 3L), fileReader.getIntegers()),
                    () -> assertEquals(List.of(2.0, 4.0), fileReader.getFloats()),
                    () -> assertEquals(List.of("text1", "text2"), fileReader.getStrings())
            );
        }
    }

    @Test
    void processInputFilesShouldHandleEdgeCasesCorrectly() throws IOException {
        // Arrange
        Path edgeCaseFile = Path.of("edge.txt");
        List<String> edgeCases = List.of(
                "999999999999999",  // Very large integer -> Long
                "3.1415926535",     // Double with many decimals
                "123.0",            // Double (even though it looks like integer)
                "0",                // Long
                "0.0",              // Double
                "-42",              // Long
                "   spaced  ",      // String (after trim)
                "",                 // Empty -> Ignored
                "   ",              // Whitespace -> Ignored
                "123abc"            // Not a number -> String
        );

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.exists(edgeCaseFile)).thenReturn(true);
            filesMock.when(() -> Files.lines(edgeCaseFile)).thenReturn(edgeCases.stream());

            // Act
            fileReader.processInputFiles(edgeCaseFile);

            // Assert
            assertAll("Edge cases should be handled correctly",
                    () -> assertEquals(List.of(999999999999999L, 0L, -42L), fileReader.getIntegers()),
                    () -> assertEquals(List.of(3.1415926535, 123.0, 0.0), fileReader.getFloats()),
                    () -> assertEquals(List.of("spaced", "123abc"), fileReader.getStrings())
            );
        }
    }

    @Test
    void processInputFilesShouldNotModifyCollectionsWhenFileIsEmpty() throws IOException {
        // Arrange
        Path emptyFile = Path.of("empty.txt");
        List<String> emptyContent = List.of();

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.exists(emptyFile)).thenReturn(true);
            filesMock.when(() -> Files.lines(emptyFile)).thenReturn(emptyContent.stream());

            int initialIntSize = fileReader.getIntegers().size();
            int initialFloatSize = fileReader.getFloats().size();
            int initialStringSize = fileReader.getStrings().size();

            // Act
            fileReader.processInputFiles(emptyFile);

            // Assert
            assertAll("Collections should remain unchanged for empty file",
                    () -> assertEquals(initialIntSize, fileReader.getIntegers().size()),
                    () -> assertEquals(initialFloatSize, fileReader.getFloats().size()),
                    () -> assertEquals(initialStringSize, fileReader.getStrings().size())
            );
        }
    }
}