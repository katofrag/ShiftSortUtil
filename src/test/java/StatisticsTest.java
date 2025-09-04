import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsTest {

    private Statistics statistics;
    private FileReader fileReader;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        statistics = new Statistics();
        fileReader = new FileReader();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void stringStatisticShouldWorkWithRealFileReaderWhenStringsExist() throws Exception {
        Path testFile = tempDir.resolve("strings.txt");
        Files.write(testFile, List.of("a", "bb", "ccc", "dd", "eeee"));

        fileReader.processInputFiles(testFile);

        statistics.displayStatistics(false, true, fileReader);

        String output = outputStream.toString();
        assertAll("String statistics should be calculated correctly with real data",
                () -> assertTrue(output.contains("Detailed statistics for strings:")),
                () -> assertTrue(output.contains("Count of recorded strings: 5")),
                () -> assertTrue(output.contains("Shortest strings: [a]")),
                () -> assertTrue(output.contains("Count of characters in shortest strings: 1")),
                () -> assertTrue(output.contains("Longest strings: [eeee]")),
                () -> assertTrue(output.contains("Count of characters in longest strings: 4"))
        );
    }

    @Test
    void intStatisticShouldWorkWithRealFileReaderWhenIntegersExist() throws Exception {
        Path testFile = tempDir.resolve("integers.txt");
        Files.write(testFile, List.of("10", "-5", "0", "25", "15"));

        fileReader.processInputFiles(testFile);

        statistics.displayStatistics(false, true, fileReader);

        String output = outputStream.toString();
        assertAll("Integer statistics should be calculated correctly with real data",
                () -> assertTrue(output.contains("Detailed statistics for integers:")),
                () -> assertTrue(output.contains("Count of recorded integers: 5")),
                () -> assertTrue(output.contains("Minimum value: -5")),
                () -> assertTrue(output.contains("Maximum value: 25")),
                () -> assertTrue(output.contains("Sum of integers: 45.0")),
                () -> assertTrue(output.contains("Arithmetic mean of integers: 9.0"))
        );
    }

    @Test
    void doubleStatisticShouldWorkWithRealFileReaderWhenFloatsExist() throws Exception {
        Path testFile = tempDir.resolve("floats.txt");
        Files.write(testFile, List.of("1.5", "2.25", "3.75", "0.5"));

        fileReader.processInputFiles(testFile);

        statistics.displayStatistics(false, true, fileReader);

        String output = outputStream.toString();
        assertAll("Double statistics should be calculated correctly with real data",
                () -> assertTrue(output.contains("Detailed statistics for floats:")),
                () -> assertTrue(output.contains("Count of recorded floats: 4")),
                () -> assertTrue(output.contains("Minimum value: 0.5")),
                () -> assertTrue(output.contains("Maximum value: 3.75")),
                () -> assertTrue(output.contains("Sum of floats numbers: 8.0")),
                () -> assertTrue(output.contains("Arithmetic mean of floats: 2.0"))
        );
    }

    @Test
    void allStatisticsShouldWorkWithRealFileReaderWhenMixedDataExists() throws Exception {
        Path testFile = tempDir.resolve("mixed.txt");
        Files.write(testFile, List.of("100", "45.67", "hello", "-50", "3.14", "world", "test"));

        fileReader.processInputFiles(testFile);

        statistics.displayStatistics(true, true, fileReader);

        String output = outputStream.toString();
        assertAll("All statistics should work correctly with real mixed data",
                () -> assertTrue(output.contains("Strings count: 3")),
                () -> assertTrue(output.contains("Integers count: 2")),
                () -> assertTrue(output.contains("Floats count: 2")),

                () -> assertTrue(output.contains("Count of recorded strings: 3")),
                () -> assertTrue(output.contains("Shortest strings: [test]")),
                () -> assertTrue(output.contains("Count of characters in shortest strings: 4")),
                () -> assertTrue(output.contains("Longest strings: [hello, world]")),
                () -> assertTrue(output.contains("Count of characters in longest strings: 5")),

                () -> assertTrue(output.contains("Count of recorded integers: 2")),
                () -> assertTrue(output.contains("Minimum value: -50")),
                () -> assertTrue(output.contains("Maximum value: 100")),
                () -> assertTrue(output.contains("Sum of integers: 50.0")),

                () -> assertTrue(output.contains("Count of recorded floats: 2")),
                () -> assertTrue(output.contains("Minimum value: 3.14")),
                () -> assertTrue(output.contains("Maximum value: 45.67")),
                () -> assertTrue(output.contains("Sum of floats numbers: 48.81"))
        );
    }

    @Test
    void statisticsShouldHandleEmptyDataWhenNoValidDataInFile() throws Exception {
        Path testFile = tempDir.resolve("empty.txt");
        Files.write(testFile, List.of("", "   ", "\t", "  "));
        fileReader.processInputFiles(testFile);

        statistics.displayStatistics(true, true, fileReader);

        assertEquals("", outputStream.toString(),
                "No statistics should be displayed for empty data");
    }

    @Test
    void numberStatisticsShouldHandleExtremeValuesCorrectly() throws Exception {
        Path testFile = tempDir.resolve("extreme.txt");
        Files.write(testFile,
                List.of("9223372036854775807", "-9223372036854775808", "1.7976931348623157E308", "4.9E-324"));
        fileReader.processInputFiles(testFile);

        statistics.displayStatistics(false, true, fileReader);

        String output = outputStream.toString();
        assertAll("Should handle extreme number values correctly",
                () -> assertTrue(output.contains("Minimum value: -9223372036854775808")),
                () -> assertTrue(output.contains("Maximum value: 9223372036854775807")),
                () -> assertTrue(output.contains("Sum of integers: 0.0")),
                () -> assertTrue(output.contains("Minimum value: 4.9E-324")),
                () -> assertTrue(output.contains("Maximum value: 1.7976931348623157E308"))
        );
    }
}