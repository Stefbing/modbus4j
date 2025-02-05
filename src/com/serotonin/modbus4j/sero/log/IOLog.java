/**
 * Copyright (C) 2014 Infinite Automation Software and Serotonin Software. All rights reserved.
 * @author Terry Packer, Matthew Lohbihler
 */
package com.serotonin.modbus4j.sero.log;

import java.io.File;

/**
 * <p>IOLog class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class IOLog extends BaseIOLog{
    //private static final Log LOG = LogFactory.getLog(IOLog.class);
    private static final int MAX_FILESIZE = 1024 * 1024 * 10;
    //    private static final int MAX_FILESIZE = 1000;

    /**
     * <p>Constructor for IOLog.</p>
     *
     * @param filename a {@link java.lang.String} object.
     */
    public IOLog(String filename) {
    	super(new File(filename));
    }

    /** {@inheritDoc} */
    @Override
    protected void sizeCheck() {
        // Check if the file should be rolled.
        if (file.length() > MAX_FILESIZE) {
            out.close();

            file.renameTo(new File(file.getAbsolutePath() + "_" + System.currentTimeMillis() / 1000));
            createOut();
        }
    }
    //
    //    public static void main(String[] args) {
    //        byte[] b = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    //
    //        IOLog log = new IOLog("iotest");
    //        log.log("test");
    //        log.log("testtest");
    //
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //
    //        log.log("testtesttesttesttesttesttesttesttesttest");
    //    }
}
