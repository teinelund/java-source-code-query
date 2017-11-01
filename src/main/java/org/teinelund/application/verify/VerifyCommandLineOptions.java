package org.teinelund.application.verify;

import org.apache.commons.lang3.StringUtils;
import org.teinelund.application.ApplicationException;
import org.teinelund.application.commandline.CommandLineOptions;
import org.teinelund.application.commandline.OptionType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class VerifyCommandLineOptions implements Verify {

    private CommandLineOptions options;
    private Verify verify;

    public VerifyCommandLineOptions(CommandLineOptions options, Verify verify) {
        this.options = options;
        this.verify = verify;
    }

    @Override
    public void verify() {
        Set<OptionType> optionTypes = options.getOptionTypes();
        if (optionTypes.contains(OptionType.HELP) || optionTypes.contains(OptionType.VERSION))
            return;
        if (!optionTypes.contains(OptionType.PROJECT)) {
            throw new ApplicationException("Options --" + CommandLineOptions.OPTION_PROJECT_PATH_LONG + " is mandatory. " + CommandLineOptions.HELP_TEXT);
        }
        if (!optionTypes.contains(OptionType.BOUNDS) && !optionTypes.contains(OptionType.INBOUND) && !optionTypes.contains(OptionType.OUTBOUND)) {
            throw new ApplicationException("No query options found. " + CommandLineOptions.HELP_TEXT);
        }
        if (optionTypes.contains(OptionType.BOUNDS) && (optionTypes.contains(OptionType.INBOUND) || optionTypes.contains(OptionType.OUTBOUND))) {
            throw new ApplicationException("Option --" + CommandLineOptions.OPTION_BOUNDS_LONG + " may not be used with --"
                    + CommandLineOptions.OPTION_INBOUND_LONG + ", or --" + CommandLineOptions.OPTION_OUTBOUND_LONG + ". " + CommandLineOptions.HELP_TEXT);
        }
        if (optionTypes.contains(OptionType.PROJECT)) {
            if (options.getCommandLineOptionValue(OptionType.PROJECT).isEmpty()) {
                throw new ApplicationException("Option --" + CommandLineOptions.OPTION_PROJECT_PATH_LONG + " must contain a path." + CommandLineOptions.HELP_TEXT);
            }
            else {
                for (String pathName : options.getCommandLineOptionValue(OptionType.PROJECT)) {
                    if (StringUtils.isBlank(pathName))
                        throw new ApplicationException("Option --" + CommandLineOptions.OPTION_PROJECT_PATH_LONG + " must contain a path. " + CommandLineOptions.HELP_TEXT);
                }
                for (String pathName : options.getCommandLineOptionValue(OptionType.PROJECT)) {
                    Path path = Paths.get(pathName);
                    if (!Files.exists(path))
                        throw new ApplicationException("Path (for option --" + CommandLineOptions.OPTION_PROJECT_PATH_LONG + ") does not exist. Check spelling. " + CommandLineOptions.HELP_TEXT);
                    if (!Files.isDirectory(path))
                        throw new ApplicationException("Path (for option --" + CommandLineOptions.OPTION_PROJECT_PATH_LONG + ") is not a directory. " + CommandLineOptions.HELP_TEXT);
                }
            }
        }
        if (this.verify != null)
            this.verify.verify();
    }
}
