import java.nio.file.Path;

public record Params(Path outPath, String prefix,
                     boolean aEnable, boolean sEnable, boolean fEnable, Path file1, Path file2) {
}

