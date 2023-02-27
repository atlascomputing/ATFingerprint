package co.atlascomputing.atfingerprint;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import co.atlascomputing.atfingerprint.wrappers.MantraMFS100ScannerWrapper;

public class MantraMFS100ScannerWrapperTest {

    @Test
    public void isSupportedScanner_ReturnsFalse() {

        assertFalse(MantraMFS100ScannerWrapper.isSupportedScanner(0, 0)); //invalid vendor, product id
        assertFalse(MantraMFS100ScannerWrapper.isSupportedScanner(100, 10000)); // not this vendor's product


        // valid product ids, invalid vendor
        assertFalse(MantraMFS100ScannerWrapper.isSupportedScanner(0, 34323));
        assertFalse(MantraMFS100ScannerWrapper.isSupportedScanner(0, 4101));
        assertFalse(MantraMFS100ScannerWrapper.isSupportedScanner(0, 4102));

    }

    @Test
    public void isSupportedScanner_ReturnsTrue() {

        // valid product ids and vendors
        assertTrue(MantraMFS100ScannerWrapper.isSupportedScanner(1204, 34323));
        assertTrue(MantraMFS100ScannerWrapper.isSupportedScanner(1204, 4101));
        assertTrue(MantraMFS100ScannerWrapper.isSupportedScanner(1204, 4102));

        assertTrue(MantraMFS100ScannerWrapper.isSupportedScanner(11279, 34323));
        assertTrue(MantraMFS100ScannerWrapper.isSupportedScanner(11279, 4101));
        assertTrue(MantraMFS100ScannerWrapper.isSupportedScanner(11279, 4102));

    }
}
