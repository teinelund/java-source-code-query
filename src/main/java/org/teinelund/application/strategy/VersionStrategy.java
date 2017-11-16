package org.teinelund.application.strategy;

import org.teinelund.application.Application;

class VersionStrategy implements Strategy {

    @Override
    public void process() {
        String version = Application.class.getPackage().getImplementationVersion();
        System.out.println("Java Source Code Query, version " + version + ".");
        System.out.println("Copyright (C) 2017 Henrik Teinelund.");
    }
}
