import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileWriterTest {

    private FileWriter fileWriter;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        fileWriter = new FileWriter();
    }

    @Test
    void writeFile_ShouldCreateNewFile_WhenAppendDisabledAndFileNotExists() throws IOException {
        // Arrange
        String fileName = "test.txt";
        String prefix = "prefix_";
        List<String> data = List.of("line1", "line2", "line3");
        Path expectedFilePath = tempDir.resolve(prefix + fileName);

        assertFalse(Files.exists(expectedFilePath));

        // Act
        fileWriter.writeFile(fileName, prefix, tempDir, false, data);

        // Assert
        assertAll("New file should be created with correct content",
                () -> assertTrue(Files.exists(expectedFilePath), "File should be created"),
                () -> assertEquals(data, Files.readAllLines(expectedFilePath), "File content should match")
        );
    }

    @Test
    void writeFile_ShouldOverwriteFile_WhenAppendDisabledAndFileExists() throws IOException {
        // Arrange
        String fileName = "test.txt";
        String prefix = "prefix_";
        List<String> initialData = List.of("old_line1", "old_line2");
        List<String> newData = List.of("new_line1", "new_line2");
        Path expectedFilePath = tempDir.resolve(prefix + fileName);

        Files.write(expectedFilePath, initialData);

        // Act
        fileWriter.writeFile(fileName, prefix, tempDir, false, newData);

        // Assert
        assertAll("Existing file should be overwritten with new content",
                () -> assertTrue(Files.exists(expectedFilePath), "File should still exist"),
                () -> assertEquals(newData, Files.readAllLines(expectedFilePath),
                        "File should contain new content")
        );
    }

    @Test
    void writeFile_ShouldAppendToFile_WhenAppendEnabledAndFileExists() throws IOException {
        // Arrange
        String fileName = "test.txt";
        String prefix = "prefix_";
        List<String> initialData = List.of("line1", "line2");
        List<String> appendData = List.of("line3", "line4");
        List<String> expectedData = List.of("line1", "line2", "line3", "line4");
        Path expectedFilePath = tempDir.resolve(prefix + fileName);

        Files.write(expectedFilePath, initialData);

        // Act
        fileWriter.writeFile(fileName, prefix, tempDir, true, appendData);

        // Assert
        assertAll("Data should be appended to existing file",
                () -> assertTrue(Files.exists(expectedFilePath), "File should still exist"),
                () -> assertEquals(expectedData, Files.readAllLines(expectedFilePath),
                        "File should contain both old and new content")
        );
    }

    @Test
    void writeFile_ShouldCreateNewFile_WhenAppendEnabledButFileNotExists() throws IOException {
        // Arrange
        String fileName = "test.txt";
        String prefix = "prefix_";
        List<String> data = List.of("line1", "line2");
        Path expectedFilePath = tempDir.resolve(prefix + fileName);

        assertFalse(Files.exists(expectedFilePath));

        // Act
        fileWriter.writeFile(fileName, prefix, tempDir, true, data);

        // Assert
        assertAll("New file should be created even with append option",
                () -> assertTrue(Files.exists(expectedFilePath), "File should be created"),
                () -> assertEquals(data, Files.readAllLines(expectedFilePath), "File content should match")
        );
    }


    @Test
    void writeFile_ShouldDoNothing_WhenDataIsEmpty() throws IOException {
        // Arrange
        String fileName = "test.txt";
        String prefix = "prefix_";
        List<String> emptyData = List.of();
        Path expectedFilePath = tempDir.resolve(prefix + fileName);

        // Act
        fileWriter.writeFile(fileName, prefix, tempDir, false, emptyData);

        // Assert
        assertFalse(Files.exists(expectedFilePath), "File should not be created for empty data");
    }

    @Test
    void writeFile_ShouldThrowIOException_WhenWriteFails() {
        // Arrange
        String fileName = "test.txt";
        String prefix = "prefix_";
        List<String> data = List.of("line1", "line2");
        Path expectedFilePath = tempDir.resolve(prefix + fileName);

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            // Мокируем Files.write чтобы бросить исключение
            filesMock.when(() -> Files.write(any(Path.class), anyList(), any()))
                    .thenThrow(new IOException("Write failed"));

            // Act & Assert
            IOException thrownException = assertThrows(IOException.class,
                    () -> fileWriter.writeFile(fileName, prefix, tempDir, false, data));

            assertTrue(thrownException.getMessage().contains("Write failed"));
        }
    }


    @Test
    void writeFile_ShouldCreateFileInCorrectLocation_WithPrefix() throws IOException {
        // Arrange
        String fileName = "data.txt";
        String prefix = "output_";
        List<String> data = List.of("test data");
        Path subDirectory = tempDir.resolve("subdir");
        Files.createDirectories(subDirectory); // Создаем поддиректорию

        Path expectedFilePath = subDirectory.resolve(prefix + fileName);

        // Act
        fileWriter.writeFile(fileName, prefix, subDirectory, false, data);

        // Assert
        assertAll("File should be created in correct location with correct name",
                () -> assertTrue(Files.exists(expectedFilePath), "File should exist at correct path"),
                () -> assertEquals(prefix + fileName, expectedFilePath.getFileName().toString(),
                        "File name should include prefix")
        );
    }


    @Test
    void writeFile_ShouldHandleDifferentDataTypes() throws IOException {
        // Arrange
        String fileName = "test.txt";
        String prefix = "prefix_";

        // Тестируем с Integer
        List<Integer> intData = List.of(1, 2, 3);
        Path intFilePath = tempDir.resolve(prefix + "integers.txt");

        // Тестируем с Double
        List<Double> doubleData = List.of(1.1, 2.2, 3.3);
        Path doubleFilePath = tempDir.resolve(prefix + "doubles.txt");

        // Тестируем с пользовательским объектом
        record Person(String name, int age) {}
        List<Person> personData = List.of(new Person("Alice", 25), new Person("Bob", 30));
        Path personFilePath = tempDir.resolve(prefix + "persons.txt");

        // Act & Assert для Integer
        fileWriter.writeFile("integers.txt", prefix, tempDir, false, intData);
        assertEquals(List.of("1", "2", "3"), Files.readAllLines(intFilePath));

        // Act & Assert для Double
        fileWriter.writeFile("doubles.txt", prefix, tempDir, false, doubleData);
        assertEquals(List.of("1.1", "2.2", "3.3"), Files.readAllLines(doubleFilePath));

        // Act & Assert для пользовательского объекта
        fileWriter.writeFile("persons.txt", prefix, tempDir, false, personData);
        assertEquals(List.of("Person[name=Alice, age=25]", "Person[name=Bob, age=30]"),
                Files.readAllLines(personFilePath));
    }

    @Test
    void writeFile_ShouldHandleMultipleCallsCorrectly() throws IOException {
        // Arrange
        String fileName = "multi.txt";
        String prefix = "";
        Path filePath = tempDir.resolve(fileName);

        List<String> firstData = List.of("first");
        List<String> secondData = List.of("second");
        List<String> thirdData = List.of("third");

        // Act
        fileWriter.writeFile(fileName, prefix, tempDir, false, firstData);
        fileWriter.writeFile(fileName, prefix, tempDir, false, secondData);
        fileWriter.writeFile(fileName, prefix, tempDir, true, thirdData);

        // Assert
        assertEquals(List.of("second", "third"), Files.readAllLines(filePath));
    }
}