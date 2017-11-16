package org.teinelund.application.controller;

import org.teinelund.application.commandline.CommandLineOptions;

public class ControllerFactoryImpl implements ControllerFactory {
    @Override
    public Controller getController(CommandLineOptions options) {
        return new ControllerImpl(options);
    }
}
