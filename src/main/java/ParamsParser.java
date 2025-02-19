import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ParamsParser {

    private static final Logger log = LoggerFactory.getLogger(ParamsParser.class);

    private static final String O_OPT = "o";
    private static final String P_OPT = "p";
    private static final String A_OPT = "a";
    private static final String S_OPT = "s";
    private static final String F_OPT = "f";

    private String prefix = "";
    private Path outPath = Path.of(".");

    public CommandLine parse(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption(O_OPT, true, "Specify path for outgoing files");
        options.addOption(P_OPT, true, "Add prefix to file name");
        options.addOption(A_OPT, "Append data to existing files");
        options.addOption(S_OPT, "Display brief statistics");
        options.addOption(F_OPT, "Display detailed statistics");
        try {
            CommandLine commandLine = new DefaultParser().parse(options, args);
            if (commandLine.hasOption(O_OPT))
                if (Files.isDirectory(Path.of(commandLine.getOptionValue(O_OPT)))) {
                    outPath = Path.of(commandLine.getOptionValue(O_OPT));
                } else {
                    log.error("Directory {} not found, specify an existing directory",
                            commandLine.getOptionValue(O_OPT));
                    throw new ParseException("Directory " + commandLine.getOptionValue(O_OPT) +
                            " not found, specify an existing directory");
                }
            if (commandLine.hasOption(P_OPT)) {
                prefix = commandLine.getOptionValue(P_OPT);
            }
            if (commandLine.getArgList().size() != 2) {
                log.error("Passed arguments{}", commandLine.getArgList());
                throw new ParseException("It is necessary to pass 2 files as arguments");
            }

            return commandLine;
        } catch (ParseException e) {
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            new HelpFormatter().printHelp("app <options> file1.txt file2.txt", options);
            throw e;
        }
    }

    public Params parsedArgs(String[] args, ParamsParser parser) throws ParseException {
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