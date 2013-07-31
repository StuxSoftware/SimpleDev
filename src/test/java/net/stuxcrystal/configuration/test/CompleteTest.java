package net.stuxcrystal.configuration.test;


import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Executes all available tests.
 */
public class CompleteTest extends TestSuite {

    /**
     * Registers the test.
     */
    public CompleteTest() {
        super();
        this.addTestSuite(XMLDumpTest.class);
        this.addTestSuite(ConvertTest.class);
    }

    public static Test suite() {
        return new CompleteTest();
    }

}
