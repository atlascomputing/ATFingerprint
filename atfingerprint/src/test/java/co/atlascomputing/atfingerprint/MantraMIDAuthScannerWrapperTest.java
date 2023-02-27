package co.atlascomputing.atfingerprint;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import co.atlascomputing.atfingerprint.wrappers.MantraMIDAuthScannerWrapper;

public class MantraMIDAuthScannerWrapperTest {

    @Test
    public void isSupportedScanner_ReturnsFalse() {

        assertFalse(MantraMIDAuthScannerWrapper.isSupportedScanner(0, 0)); //invalid vendor, product id
        assertFalse(MantraMIDAuthScannerWrapper.isSupportedScanner(100, 10000)); // not this vendor's product


        // valid product ids, invalid vendor
        assertFalse(MantraMIDAuthScannerWrapper.isSupportedScanner(0, 4352));
        assertFalse(MantraMIDAuthScannerWrapper.isSupportedScanner(0, 4355));
        assertFalse(MantraMIDAuthScannerWrapper.isSupportedScanner(0, 4610));
        assertFalse(MantraMIDAuthScannerWrapper.isSupportedScanner(0, 4611));

    }

    @Test
    public void isSupportedScanner_ReturnsTrue() {

        // valid product ids and vendors
        assertTrue(MantraMIDAuthScannerWrapper.isSupportedScanner(11279, 4352));
        assertTrue(MantraMIDAuthScannerWrapper.isSupportedScanner(11279, 4355));
        assertTrue(MantraMIDAuthScannerWrapper.isSupportedScanner(11279, 4610));
        assertTrue(MantraMIDAuthScannerWrapper.isSupportedScanner(11279, 4611));

    }
}
