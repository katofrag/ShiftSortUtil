import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParamsParserTest {

    private ParamsParser parser;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        parser = new ParamsParser();
    }

    @Test
    void parseShouldParseAllOptionsCorrectly() throws Exception {
        String[] args = {
                "-o", tempDir.toString(),
                "-p", "prefix_",
                "-a",
                "-s",
                "-f",
                "file1.txt",
                "file2.txt"
        };

        var cmd = parser.parse(args);

        assertAll(
                () -> assertTrue(cmd.hasOption("o")),
                () -> assertEquals(tempDir.toString(), cmd.getOptionValue("o")),
                () -> assertTrue(cmd.hasOption("p")),
                () -> assertEquals("prefix_", cmd.getOptionValue("p")),
                () -> assertTrue(cmd.hasOption("a")),
                () -> assertTrue(cmd.hasOption("s")),
                () -> assertTrue(cmd.hasOption("f")),
                () -> assertEquals(List.of("file1.txt", "file2.txt"), cmd.getArgList())
        );
    }

    @Test
    void parseShouldThrowIfOutputDirDoesNotExist() {
        String[] args = {"-o", "non_existing_dir_123", "file.txt"};

        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(args));

        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void parseShouldThrowIfNoFilesProvided() {
        String[] args = {"-o", tempDir.toString()};

        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(args));

        assertTrue(ex.getMessage().contains("It is necessary to pass files"));
    }

    @Test
    void parsedArgsShouldReturnParamsWithCorrectValues() throws Exception {
        String[] args = {
                "-o", tempDir.toString(),
                "-p", "prefix_",
                "-a",
                "-s",
                "-f",
                "file1.txt",
                "file2.txt"
        };

        Params params = parser.parsedArgs(args, parser);

        assertAll(
                () -> assertEquals(tempDir, params.outPath()),
                () -> assertEquals("prefix_", params.prefix()),
                () -> assertTrue(params.aEnable()),
                () -> assertTrue(params.sEnable()),
                () -> assertTrue(params.fEnable()),
                () -> assertEquals(List.of(Path.of("file1.txt"), Path.of("file2.txt")), params.files())
        );
    }

    @Test
    void parsedArgsShouldUseDefaultsWhenOptionsMissing() throws Exception {
        String[] args = {"file1.txt"};

        Params params = parser.parsedArgs(args, parser);

        assertAll(
                () -> assertEquals(Path.of("."), params.outPath()),
                () -> assertEquals("", params.prefix()),
                () -> assertFalse(params.aEnable()),
                () -> assertFalse(params.sEnable()),
                () -> assertFalse(params.fEnable()),
                () -> assertEquals(List.of(Path.of("file1.txt")), params.files())
        );
    }
}