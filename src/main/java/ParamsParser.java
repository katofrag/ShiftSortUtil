import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ParamsParser {

    private static final Logger logger = LoggerFactory.getLogger(ParamsParser.class);

    private static final String O_OPT = "o";
    private static final String P_OPT = "p";
    private static final String A_OPT = "a";
    private static final String S_OPT = "s";
    private static final String F_OPT = "f";

    private String prefix = "";
    private Path outPath = Path.of(".");

    public CommandLine parse(String[] args) throws ParseException, IOException {
        Options options = new Options();
        options.addOption(O_OPT, true, "использовать для указания пути исходящих файлов в существующую директорию");
        options.addOption(P_OPT, true, "использовать для добавления префикса к названию исходящих файлов");
        options.addOption(A_OPT, "использовать для добавления данных в уже существующие исходящие файлы");
        options.addOption(S_OPT, "использовать для выведения краткой статистики по отфильтрованным данным");
        options.addOption(F_OPT, "использовать для выведения полной статистики по отфильтрованным данным");
        try {
            CommandLine commandLine = new DefaultParser().parse(options, args);
            if (commandLine.hasOption(O_OPT))
                if (!Files.isDirectory(Path.of(commandLine.getOptionValue(O_OPT)))) {
                    outPath = Path.of(commandLine.getOptionValue(O_OPT));
                } else
                    throw new ParseException("Directory " + commandLine.getOptionValue(O_OPT) + " not found, specify an existing directory");
            if (commandLine.hasOption(P_OPT)) {
                prefix = commandLine.getOptionValue(P_OPT);
            }
            if (commandLine.getArgList().size() != 2) {
                logger.error("222");
                logger.info("111");
                throw new ParseException("It is necessary to pass 2 files as arguments");
            }

            for (int i = 0; i < commandLine.getArgList().size(); i++) {
                if (!commandLine.getArgList().get(i).matches("(\\w+|[а-я]+)\\.txt")) {
                    throw new ParseException(
                            "The file name - " + commandLine.getArgList().get(i) + " format is incorrect, correct format \"name.txt\"");
                }
                if (!Files.exists(Path.of(commandLine.getArgList().get(i)))) {
                    throw new IOException("File " + commandLine.getArgList().get(i) + " not found");
                }
            }

            return commandLine;
        } catch (ParseException e) {
            if (args.length > 0) {
                System.out.println(e.getMessage());
            } else new HelpFormatter().printHelp("app <options> file1 file2", options);
            throw e;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public Params parsedArgs(String[] args, ParamsParser parser) throws ParseException, IOException {
        CommandLine commandLine = parser.parse(args);
        var fileName = commandLine.getArgList().iterator();
        return new Params(
                outPath,
                prefix,
                commandLine.hasOption(A_OPT),
                commandLine.hasOption(S_OPT),
                commandLine.hasOption(F_OPT),
                Paths.get(fileName.next()),
                Paths.get(fileName.next())
        );
    }
}