package org.teinelund.application.controller;

import org.teinelund.application.commandline.CommandLineOptions;

public interface ControllerFactory {
    public Controller getController(CommandLineOptions options);
}
