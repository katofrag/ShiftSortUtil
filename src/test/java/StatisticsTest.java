import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatisticsTest {

    private Statistics statistics;
    private FileReader fileReader;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        statistics = new Statistics();
        fileReader = mock(FileReader.class);
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void displayStatistics_shouldShowShortStatsWhenSEnabled() {
        when(fileReader.getStrings()).thenReturn(List.of("a", "bb"));
        when(fileReader.getIntegers()).thenReturn(List.of(1L, 2L));
        when(fileReader.getFloats()).thenReturn(List.of(1.1));

        statistics.displayStatistics(true, false, fileReader);

        String output = outputStream.toString();
        assertAll(
                () -> assertTrue(output.contains("Strings count: 2")),
                () -> assertTrue(output.contains("Integers count: 2")),
                () -> assertTrue(output.contains("Floats count: 1"))
        );
    }

    @Test
    void displayStatistics_shouldShowDetailedStatsWhenFEnabled() {
        when(fileReader.getIntegers()).thenReturn(List.of(5L, 15L));

        statistics.displayStatistics(false, true, fileReader);

        String output = outputStream.toString();
        assertTrue(output.contains("Detailed statistics for integers"));
    }

    @Test
    void displayStatistics_shouldShowNothingWhenBothDisabled() {
        when(fileReader.getStrings()).thenReturn(List.of("test"));

        statistics.displayStatistics(false, false, fileReader);

        assertEquals("", outputStream.toString());
    }

    @Test
    void displayStatistics_shouldHandleEmptyDataGracefully() {
        when(fileReader.getStrings()).thenReturn(List.of());
        when(fileReader.getIntegers()).thenReturn(List.of());
        when(fileReader.getFloats()).thenReturn(List.of());

        assertDoesNotThrow(() -> statistics.displayStatistics(true, true, fileReader));
    }
}