package com.github.zeroeighteightzero.lwlp.langs.css;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CSSValueTest {

    @Test
    void testIdentifierValue() {
        CSSValue value = new CSSValue();
        value.type = CSSType.IDENTIFIER;
        value.identifier = "auto";
        assertEquals("auto", value.toString());
    }

    @Test
    void testStringValue() {
        CSSValue value = new CSSValue();
        value.type = CSSType.STRING;
        value.string = "Hello World";
        assertEquals("Hello World", value.toString());
    }

    @Test
    void testFunctionValue() {
        CSSValue arg1 = new CSSValue();
        arg1.type = CSSType.IDENTIFIER;
        arg1.identifier = "red";

        CSSValue arg2 = new CSSValue();
        arg2.type = CSSType.NUMBER_UNIT;
        arg2.numberUnit = new CSSNumberUnit(50, CSSUnit.PERCENT);

        CSSValue value = new CSSValue();
        value.type = CSSType.FUNCTION;
        value.functionName = "rgba";
        value.functionArguments = new CSSValue[]{arg1, arg2};
        
        assertEquals("rgba(red,50%)", value.toString());
    }

    @Test
    void testNumberUnitValue() {
        CSSValue value = new CSSValue();
        value.type = CSSType.NUMBER_UNIT;
        value.numberUnit = new CSSNumberUnit(100, CSSUnit.PX);
        assertEquals("100px", value.toString());
    }
}
