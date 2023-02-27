package co.atlascomputing.atfingerprint;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import co.atlascomputing.atfingerprint.wrappers.MantraMorfinAuthScannerWrapper;

public class MantraMorfinAuthScannerWrapperTest {

    @Test
    public void isSupportedScanner_ReturnsFalse() {

        assertFalse(MantraMorfinAuthScannerWrapper.isSupportedScanner(0, 0)); //invalid vendor, product id
        assertFalse(MantraMorfinAuthScannerWrapper.isSupportedScanner(100, 10000)); // not this vendor's product


        // valid product ids, invalid vendor
        assertFalse(MantraMorfinAuthScannerWrapper.isSupportedScanner(0, 34323));
        assertFalse(MantraMorfinAuthScannerWrapper.isSupportedScanner(0, 4101));
        assertFalse(MantraMorfinAuthScannerWrapper.isSupportedScanner(0, 4102));

    }

    @Test
    public void isSupportedScanner_ReturnsTrue() {

        // valid product ids and vendors
        assertTrue(MantraMorfinAuthScannerWrapper.isSupportedScanner(11279, 4352));
        assertTrue(MantraMorfinAuthScannerWrapper.isSupportedScanner(11279, 4619));
        assertTrue(MantraMorfinAuthScannerWrapper.isSupportedScanner(11279, 4621));

    }
}
