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
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.teinelund.application.ApplicationException;

public class CommandLineOptionsImpl implements CommandLineOptions {

    private final String OPTION_HELP_SHORT = "h";
    private final String OPTION_HELP_LONG = "help";
    private final String OPTION_VERSION_SHORT = "v";
    private final String OPTION_VERSION_LONG = "version";
    private final String OPTION_BOUNDS_SHORT = "b";
    private final String OPTION_BOUNDS_LONG = "bounds";
    private final String OPTION_INBOUND_SHORT = "i";
    private final String OPTION_INBOUND_LONG = "inbound";
    private final String OPTION_OUTBOUND_SHORT = "o";
    private final String OPTION_OUTBOUND_LONG = "outbound";
    private final String OPTION_PROJECT_PATH_SHORT = "p";
    private final String OPTION_PROJECT_PATH_LONG = "project-path";
    private Map<OptionType, List<String>> options = new HashMap<>();
    private Map<String, OptionType> mapFromStringToOptionType = new HashMap<>();

    public CommandLineOptionsImpl(final String[] args) throws ParseException {
        initialize();
        Options options = createOptions();
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine commandLine = commandLineParser.parse(options, args);
        processCommandLineOptions(commandLine);
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

    Options createOptions() {
        Options options = new Options();
        options.addOption(Option.builder(OPTION_PROJECT_PATH_SHORT).longOpt(OPTION_PROJECT_PATH_LONG).hasArg().argName("PATH")
                .desc("Project PATH. Example: \'-p /Users/ada/repos/orderengine\'. Mandatory.").build());
        options.addOption(Option.builder(OPTION_INBOUND_SHORT).longOpt(OPTION_INBOUND_LONG).hasArg().argName("ARGUMENT")
                .desc("Inbound ARGUMENT. Example: \'-i rest,web\'. ARGUMENT is case insensive. Optional.").build());
        options.addOption(Option.builder(OPTION_OUTBOUND_SHORT).longOpt(OPTION_OUTBOUND_LONG).hasArg().argName("ARGUMENT")
                .desc("Outbound ARGUMENT. Example: \'-o res,dao,system,log\'. ARGUMENT is case insensive. Optional.").build());
        options.addOption(Option.builder(OPTION_BOUNDS_SHORT).longOpt(OPTION_BOUNDS_LONG)
                .desc("Which inbound and outbound technologies are used. Example: \'-b\'. Optional.").build());
        options.addOption(Option.builder(OPTION_HELP_SHORT).longOpt(OPTION_HELP_LONG).desc("Prints this page.").build());
        options.addOption(Option.builder(OPTION_VERSION_SHORT).longOpt(OPTION_VERSION_LONG).desc("Show version.").build());
        return options;
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

}