package com.github.zeroeighteightzero.lwlp.langs.css;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CSSUnitTest {

    @Test
    void testUnitFromString() {
        assertEquals(CSSUnit.PX, CSSUnit.fromString("px"));
        assertEquals(CSSUnit.EM, CSSUnit.fromString("em"));
        assertEquals(CSSUnit.PERCENT, CSSUnit.fromString("%"));
        assertEquals(CSSUnit.VW, CSSUnit.fromString("vw"));
    }

    @Test
    void testUnitToString() {
        assertEquals("px", CSSUnit.PX.toString());
        assertEquals("em", CSSUnit.EM.toString());
        assertEquals("%", CSSUnit.PERCENT.toString());
        assertEquals("vw", CSSUnit.VW.toString());
    }

    @Test
    void testNoneUnit() {
        assertNull(CSSUnit.NONE.getName());
        assertNull(CSSUnit.NONE.toString());
    }

    @Test
    void testCaseInsensitiveFromString() {
        assertEquals(CSSUnit.PX, CSSUnit.fromString("PX"));
        assertEquals(CSSUnit.EM, CSSUnit.fromString("EM"));
        assertEquals(CSSUnit.REM, CSSUnit.fromString("REM"));
    }
}
