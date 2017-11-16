package org.teinelund.application;

import org.teinelund.application.commandline.CommandLineOptions;
import org.teinelund.application.commandline.CommandLineOptionsImpl;
import org.teinelund.application.controller.Controller;
import org.teinelund.application.controller.ControllerFactory;
import org.teinelund.application.controller.ControllerFactoryImpl;
import org.teinelund.application.verify.VerifyCommandLineOptions;
import org.teinelund.application.verify.Verify;

public class Application
{
    public static void main( String[] args )
    {
        CommandLineOptions options = new CommandLineOptionsImpl(args);
        Verify verifyOptions = new VerifyCommandLineOptions(options, null);
        verifyOptions.verify();
        ControllerFactory controllerFactory = new ControllerFactoryImpl();
        Controller controller = controllerFactory.getController(options);
        controller.selectStrategy();
    }
}
