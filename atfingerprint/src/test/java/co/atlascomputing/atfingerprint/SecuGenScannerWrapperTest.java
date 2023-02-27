package co.atlascomputing.atfingerprint;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import co.atlascomputing.atfingerprint.wrappers.SecuGenScannerWrapper;

public class SecuGenScannerWrapperTest {

    @Test
    public void isSupportedScanner_ReturnsFalse() {

        assertFalse(SecuGenScannerWrapper.isSupportedScanner(0,0)); //invalid vendor, product id
        assertFalse(SecuGenScannerWrapper.isSupportedScanner(100,10000)); // not this vendor's product

    }
    @Test
    public void isSupportedScanner_ReturnsTrue() {

        // valid product ids
        assertTrue(SecuGenScannerWrapper.isSupportedScanner(0,4096));
        assertTrue(SecuGenScannerWrapper.isSupportedScanner(0,8192));
        assertTrue(SecuGenScannerWrapper.isSupportedScanner(0,8704));
        assertTrue(SecuGenScannerWrapper.isSupportedScanner(0,8890));
        assertTrue(SecuGenScannerWrapper.isSupportedScanner(0,8705));
        assertTrue(SecuGenScannerWrapper.isSupportedScanner(0,8961));
        assertTrue(SecuGenScannerWrapper.isSupportedScanner(0,8707));
        assertTrue(SecuGenScannerWrapper.isSupportedScanner(0,8768));
        assertTrue(SecuGenScannerWrapper.isSupportedScanner(0,8736));
        assertTrue(SecuGenScannerWrapper.isSupportedScanner(0,9056));
        assertTrue(SecuGenScannerWrapper.isSupportedScanner(0,9232));
        assertTrue(SecuGenScannerWrapper.isSupportedScanner(0,9472));
    }
}
