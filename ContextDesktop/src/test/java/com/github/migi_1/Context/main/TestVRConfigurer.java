package com.github.migi_1.Context.main;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


/**
 * Tests VRConfigurer class.
 *
 */
public class TestVRConfigurer {

    private Main main;

    /**
     * Setup for the test suite.
     */
    @Before
    public void setUp() {
        main = Mockito.mock(Main.class);
    }

    /**
     * Tests if the VRSettings are configured correctly.
     */
    @Test
    public void configureVRTest() {
        VRConfigurer.configureVR(main);
        Mockito.verify(main, Mockito.times(8)).preconfigureVRApp(Mockito.any(), Mockito.anyBoolean());
        Mockito.verify(main).preconfigureFrustrumNearFar(Mockito.anyFloat(), Mockito.anyFloat());
    }

}
