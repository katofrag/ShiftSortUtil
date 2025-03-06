import java.nio.file.Path;
import java.util.List;

public record Params(Path outPath, String prefix,
                     boolean aEnable, boolean sEnable, boolean fEnable, List<Path> files) {
}

