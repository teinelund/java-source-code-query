package org.teinelund.application.strategy;

import org.teinelund.application.commandline.CommandLineOptions;

import java.util.List;

public abstract class AbstractStrategy implements Strategy {

    private CommandLineOptions options;

    public AbstractStrategy(CommandLineOptions options) {
        this.options = options;
    }


    @Override
    public void process() {

    }

    abstract void processSourceCode(List<String> sourceCodeList);



}
