package org.teinelund.application.commandline;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.teinelund.application.ApplicationException;

public class CommandLineOptionsImpl implements CommandLineOptions {
    private Options cliOptions;
    private Map<OptionType, List<String>> options = new HashMap<>();
    private Map<String, OptionType> mapFromStringToOptionType = new HashMap<>();

    public CommandLineOptionsImpl(final String[] args) {
        initialize();
        createOptions();
        CommandLineParser commandLineParser = new DefaultParser();

        try {
            CommandLine commandLine = commandLineParser.parse(this.cliOptions, args);
            processCommandLineOptions(commandLine);
        } catch (ParseException e) {
            throw new ApplicationException("ParseException: " + e.getMessage());
        }
    }

    void initialize() {
        mapFromStringToOptionType.put(OPTION_HELP_SHORT, OptionType.HELP);
        mapFromStringToOptionType.put(OPTION_HELP_LONG, OptionType.HELP);
        mapFromStringToOptionType.put(OPTION_VERSION_SHORT, OptionType.VERSION);
        mapFromStringToOptionType.put(OPTION_VERSION_LONG, OptionType.VERSION);
        mapFromStringToOptionType.put(OPTION_BOUNDS_SHORT, OptionType.BOUNDS);
        mapFromStringToOptionType.put(OPTION_BOUNDS_LONG, OptionType.BOUNDS);
        mapFromStringToOptionType.put(OPTION_INBOUND_SHORT, OptionType.INBOUND);
        mapFromStringToOptionType.put(OPTION_INBOUND_LONG, OptionType.INBOUND);
        mapFromStringToOptionType.put(OPTION_OUTBOUND_SHORT, OptionType.OUTBOUND);
        mapFromStringToOptionType.put(OPTION_OUTBOUND_LONG, OptionType.OUTBOUND);
        mapFromStringToOptionType.put(OPTION_PROJECT_PATH_SHORT, OptionType.PROJECT);
        mapFromStringToOptionType.put(OPTION_PROJECT_PATH_LONG, OptionType.PROJECT);
    }

    void createOptions() {
        cliOptions = new Options();
        cliOptions.addOption(Option.builder(OPTION_PROJECT_PATH_SHORT).longOpt(OPTION_PROJECT_PATH_LONG).hasArg().argName("PATH")
                .desc("Project PATH. Example: \'-p /Users/ada/repos/orderengine\'. Mandatory.").build());
        cliOptions.addOption(Option.builder(OPTION_INBOUND_SHORT).longOpt(OPTION_INBOUND_LONG).hasArg().argName("ARGUMENT")
                .desc("Inbound ARGUMENT. Example: \'-i rest,web\'. ARGUMENT is case insensive. Optional.").build());
        cliOptions.addOption(Option.builder(OPTION_OUTBOUND_SHORT).longOpt(OPTION_OUTBOUND_LONG).hasArg().argName("ARGUMENT")
                .desc("Outbound ARGUMENT. Example: \'-o res,dao,system,log\'. ARGUMENT is case insensive. Optional.").build());
        cliOptions.addOption(Option.builder(OPTION_BOUNDS_SHORT).longOpt(OPTION_BOUNDS_LONG)
                .desc("Which inbound and outbound technologies are used. Example: \'-b\'. Optional.").build());
        cliOptions.addOption(Option.builder(OPTION_HELP_SHORT).longOpt(OPTION_HELP_LONG).desc("Prints this page.").build());
        cliOptions.addOption(Option.builder(OPTION_VERSION_SHORT).longOpt(OPTION_VERSION_LONG).desc("Show version.").build());
    }


    void processCommandLineOptions(final CommandLine commandLine) {
        Option[] optionArray = commandLine.getOptions();
        for (Option option : optionArray) {
            String optionName = "";
            if (this.mapFromStringToOptionType.containsKey(option.getOpt())) {
                optionName = option.getOpt();
            } else if (this.mapFromStringToOptionType.containsKey(option.getLongOpt())) {
                optionName = option.getLongOpt();
            } else {
                throw new ApplicationException("Application option \"" + option.getOpt() + "\"/\"" + option.getLongOpt() + "\", can't be found. THIS IS A BUG!");
            }
            if (!this.options.containsKey(this.mapFromStringToOptionType.get(optionName))) {
                this.options.put(this.mapFromStringToOptionType.get(optionName), new LinkedList<>());
            }
            if (option.getValue() != null) {
                this.options.get(this.mapFromStringToOptionType.get(optionName)).add(option.getValue());
            }
            else {
                this.options.get(this.mapFromStringToOptionType.get(optionName)).add("");
            }

        }
    }


    @Override
    public Set<OptionType> getOptionTypes() {
        return this.options.keySet();
    }

    @Override
    public List<String> getCommandLineOptionValue(OptionType optionType) {
        if (!this.options.containsKey(optionType))
            throw new ApplicationException("Optione Type " + optionType.toString() + " does not exist among the command line parameters.");
        if (this.options.get(optionType).isEmpty()) {
            return new LinkedList<>();
        }
        else {
            List<String> list = new LinkedList<>();
            for (String value : this.options.get(optionType)) {
                if (value.contains(",")) {
                    String[] splits = value.split(",");
                    list.addAll(Arrays.asList(splits));
                }
                else {
                    list.add(value);
                }
            }
            return list;
        }

    }

    @Override
    public void createHelpFormatter(String header, String footer) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(100, "java-source-code-query", header, this.cliOptions, footer, true);
    }

}