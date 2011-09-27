package com.jetbootlabs.gordonflash;

import org.codehaus.groovy.grails.web.servlet.GrailsFlashScope;

public class GordonFlashScope extends GrailsFlashScope {

    @Override
    public void next() {
        // Does nothing, instead nextThatWorks() does the work to allow us to delay clearing flash scope until
        // the end of processing a request
    }

    public void nextThatWorks() {
        super.next();
    }

}
