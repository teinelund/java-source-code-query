package org.teinelund.application.commandline;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandLineOptionsImplTest {

    private final String PROJECT_VALUE = "/User/ada/repos/orderengine";
    private final String PROJECT_VALUE_A = "/User/ada/repos/orderengine/engineUI";
    private final String PROJECT_VALUE_B1 = "/User/ada/repos/orderengine/engineDomain";
    private final String PROJECT_VALUE_B2 = "/User/ada/repos/orderengine/repository";
    private final String PROJECT_VALUE_B = PROJECT_VALUE_B1 + "," + PROJECT_VALUE_B2;
    private final String INBOUND_VALUE = "rest,jms";
    private final String OUTBOUND_VALUE = "rest,dao";

    @Test
    public void getOptionTypesWhereOptionsAreHelpAndVersionShortOpt() throws ParseException {
        // Initialize
        String args[] = {"-h", "-v"};
        CommandLineOptions options = new CommandLineOptionsImpl(args);
        final int expectedSize = 2;
        // Test
        Set<OptionType> result = options.getOptionTypes();
        // Verify
        assertTrue(result.contains(OptionType.HELP));
        assertTrue(result.contains(OptionType.VERSION));
        assertEquals(expectedSize, result.size());
    }

    @Test
    public void getOptionTypesWhereOptionsAreHelpAndVersionLongOpt() throws ParseException {
        // Initialize
        String args[] = {"--help", "--version"};
        CommandLineOptions options = new CommandLineOptionsImpl(args);
        final int expectedSize = 2;
        // Test
        Set<OptionType> result = options.getOptionTypes();
        // Verify
        assertTrue(result.contains(OptionType.HELP));
        assertTrue(result.contains(OptionType.VERSION));
        assertEquals(expectedSize, result.size());
    }

    @Test
    public void getOptionTypesWhereArgumentIsProjectShortOpt() throws ParseException {
        // Initialize
        String args[] = {"-p", PROJECT_VALUE};
        CommandLineOptions options = new CommandLineOptionsImpl(args);
        final int expectedSize = 1;
        // Test
        Set<OptionType> result = options.getOptionTypes();
        // Verify
        assertTrue(result.contains(OptionType.PROJECT));
        assertEquals(expectedSize, result.size());
    }

    @Test
    public void getOptionTypesWhereArgumentIsProjectLongOpt() throws ParseException {
        // Initialize
        String args[] = {"--project-path", PROJECT_VALUE};
        CommandLineOptions options = new CommandLineOptionsImpl(args);
        final int expectedSize = 1;
        // Test
        Set<OptionType> result = options.getOptionTypes();
        // Verify
        assertTrue(result.contains(OptionType.PROJECT));
        assertEquals(expectedSize, result.size());
    }

    @Test
    public void getCommandLineOptionValueWhereArgumentIsProjectShortOpt() throws ParseException {
        // Initialize
        String args[] = {"-p", PROJECT_VALUE};
        CommandLineOptions options = new CommandLineOptionsImpl(args);
        final int expectedSize = 1;
        // Test
        List<String> result = options.getCommandLineOptionValue(OptionType.PROJECT);
        // Verify
        assertEquals(PROJECT_VALUE, result.get(0));
        assertEquals(expectedSize, result.size());
    }

    @Test
    public void getCommandLineOptionValueWhereArgumentIsProjectLongOpt() throws ParseException {
        // Initialize
        String args[] = {"--project-path", PROJECT_VALUE};
        CommandLineOptions options = new CommandLineOptionsImpl(args);
        final int expectedSize = 1;
        // Test
        List<String> result = options.getCommandLineOptionValue(OptionType.PROJECT);
        // Verify
        assertEquals(PROJECT_VALUE, result.get(0));
        assertEquals(expectedSize, result.size());
    }

    @Test
    public void getOptionTypesWhereArgumentIsProjectShortOptAndLongOpt() throws ParseException {
        // Initialize
        String args[] = {"-p", PROJECT_VALUE_A, "--project-path", PROJECT_VALUE_B};
        CommandLineOptions options = new CommandLineOptionsImpl(args);
        final int expectedSize = 1;
        // Test
        Set<OptionType> result = options.getOptionTypes();
        // Verify
        assertTrue(result.contains(OptionType.PROJECT));
        assertEquals(expectedSize, result.size());
    }

    @Test
    public void getCommandLineOptionValueWhereArgumentIsProjectShortOptAndLongOpt() throws ParseException {
        // Initialize
        String args[] = {"-p", PROJECT_VALUE_A, "--project-path", PROJECT_VALUE_B};
        CommandLineOptions options = new CommandLineOptionsImpl(args);
        final int expectedSize = 3;
        // Test
        List<String> result = options.getCommandLineOptionValue(OptionType.PROJECT);
        // Verify
        assertEquals(expectedSize, result.size());
        assertEquals(PROJECT_VALUE_A, result.get(0));
        assertEquals(PROJECT_VALUE_B1, result.get(1));
        assertEquals(PROJECT_VALUE_B2, result.get(2));
    }

    @Test
    public void getOptionTypesWhereArgumentIsBoundsShortOpt() throws ParseException {
        // Initialize
        String args[] = {"-b"};
        CommandLineOptions options = new CommandLineOptionsImpl(args);
        final int expectedSize = 1;
        // Test
        Set<OptionType> result = options.getOptionTypes();
        // Verify
        assertTrue(result.contains(OptionType.BOUNDS));
        assertEquals(expectedSize, result.size());
    }

    @Test
    public void getOptionTypesWhereArgumentIsBoundsLongOpt() throws ParseException {
        // Initialize
        String args[] = {"--bounds"};
        CommandLineOptions options = new CommandLineOptionsImpl(args);
        final int expectedSize = 1;
        // Test
        Set<OptionType> result = options.getOptionTypes();
        // Verify
        assertTrue(result.contains(OptionType.BOUNDS));
        assertEquals(expectedSize, result.size());
    }

    @Test
    public void getOptionTypesWhereArgumentIsInboundShortOpt() throws ParseException {
        // Initialize
        String args[] = {"-i", INBOUND_VALUE};
        CommandLineOptions options = new CommandLineOptionsImpl(args);
        final int expectedSize = 1;
        // Test
        Set<OptionType> result = options.getOptionTypes();
        // Verify
        assertTrue(result.contains(OptionType.INBOUND));
        assertEquals(expectedSize, result.size());
    }

    @Test
    public void getOptionTypesWhereArgumentIsInboundLongOpt() throws ParseException {
        // Initialize
        String args[] = {"--inbound", INBOUND_VALUE};
        CommandLineOptions options = new CommandLineOptionsImpl(args);
        final int expectedSize = 1;
        // Test
        Set<OptionType> result = options.getOptionTypes();
        // Verify
        assertTrue(result.contains(OptionType.INBOUND));
        assertEquals(expectedSize, result.size());
    }

    @Test
    public void getOptionTypesWhereArgumentIsOutboundShortOpt() throws ParseException {
        // Initialize
        String args[] = {"-o", OUTBOUND_VALUE};
        CommandLineOptions options = new CommandLineOptionsImpl(args);
        final int expectedSize = 1;
        // Test
        Set<OptionType> result = options.getOptionTypes();
        // Verify
        assertTrue(result.contains(OptionType.OUTBOUND));
        assertEquals(expectedSize, result.size());
    }

    @Test
    public void getOptionTypesWhereArgumentIsOutboundLongOpt() throws ParseException {
        // Initialize
        String args[] = {"--outbound", OUTBOUND_VALUE};
        CommandLineOptions options = new CommandLineOptionsImpl(args);
        final int expectedSize = 1;
        // Test
        Set<OptionType> result = options.getOptionTypes();
        // Verify
        assertTrue(result.contains(OptionType.OUTBOUND));
        assertEquals(expectedSize, result.size());
    }


}