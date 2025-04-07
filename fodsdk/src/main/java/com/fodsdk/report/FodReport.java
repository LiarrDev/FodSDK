package com.fodsdk.report;

public class FodReport extends InternalReport {

    private FodReport() {
    }

    private static final FodReport instance = new FodReport();

    public static FodReport get() {
        return instance;
    }
}
