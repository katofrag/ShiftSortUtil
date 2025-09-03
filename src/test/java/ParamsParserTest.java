import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;


import static org.junit.jupiter.api.Assertions.*;

class ParamsParserTest {

    @Test
    void parsedArgs_shouldParseAllOptions(@TempDir Path tempDir) throws Exception {
        Path outputDir = tempDir.resolve("output");
        Files.createDirectory(outputDir);

        ParamsParser parser = new ParamsParser();
        String[] args = {"-o", outputDir.toString(), "-p", "test_", "-a", "-s", "file.txt"};

        Params params = parser.parsedArgs(args, parser);

        assertAll(
                () -> assertEquals(outputDir, params.outPath()),
                () -> assertEquals("test_", params.prefix()),
                () -> assertTrue(params.aEnable()),
                () -> assertTrue(params.sEnable()),
                () -> assertFalse(params.fEnable())
        );
    }

    @Test
    void parsedArgs_shouldUseDefaultsWhenNoOptions() throws Exception {
        ParamsParser parser = new ParamsParser();
        String[] args = {"file.txt"};

        Params params = parser.parsedArgs(args, parser);

        assertAll(
                () -> assertEquals(Path.of("."), params.outPath()),
                () -> assertEquals("", params.prefix()),
                () -> assertFalse(params.aEnable()),
                () -> assertFalse(params.sEnable()),
                () -> assertFalse(params.fEnable())
        );
    }
}