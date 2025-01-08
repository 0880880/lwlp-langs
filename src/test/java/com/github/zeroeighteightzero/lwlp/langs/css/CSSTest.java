package com.github.zeroeighteightzero.lwlp.langs.css;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CSSTest {

    private final CSS css = new CSS();

    @Test
    void testSimpleSelector() {
        String input = "div { color: red; }";
        CSSStylesheet stylesheet = css.process(input);
        
        assertNotNull(stylesheet);
        assertEquals(1, stylesheet.getRules().length);
        
        CSSRule rule = stylesheet.getRules()[0];
        assertEquals("div", rule.getSelectors()[0].toString());
        assertEquals(1, rule.getDeclarations().length);
        assertEquals("color", rule.getDeclarations()[0].property);
        assertEquals("red", rule.getDeclarations()[0].values[0].identifier);
    }
    @Test
    void testComplexSelector() {
        String input = "div.class#id:hover::before { width: 100px; height: 200px; }";
        CSSStylesheet stylesheet = css.process(input);
        
        assertNotNull(stylesheet);
        CSSRule rule = stylesheet.getRules()[0];
        assertEquals("div.class#id:hover::before", rule.selectorsToString());
        assertEquals(2, rule.getDeclarations().length);
    }

    @Test
    void testMultipleSelectors() {
        String input = "div, span, p { margin: 10px; }";
        CSSStylesheet stylesheet = css.process(input);
        
        assertNotNull(stylesheet);
        assertEquals(1, stylesheet.getRules().length);
        CSSRule rule = stylesheet.getRules()[0];
        assertEquals("div,span,p", rule.selectorsToString());
    }

    @Test
    void testCalcFunction() {
        String input = ".calculator { width: calc(100% - 20px); }";
        CSSStylesheet stylesheet = css.process(input);
        
        assertNotNull(stylesheet);
        CSSRule rule = stylesheet.getRules()[0];
        CSSDeclaration declaration = rule.getDeclarations()[0];
        System.out.println(declaration);
        assertEquals("width", declaration.property);
        System.out.println(declaration.values[0].functionArguments[0].math);
        CSSMath math = new CSSMath();
        math.add(100, CSSUnit.PERCENT);
        math.add(-20, CSSUnit.PX);
        assertEquals(math, declaration.values[0].functionArguments[0].math);
    }

    @Test
    void testPseudoElements() {
        String input = "a::before { content: '→'; }";
        CSSStylesheet stylesheet = css.process(input);
        
        assertNotNull(stylesheet);
        CSSRule rule = stylesheet.getRules()[0];
        assertTrue(rule.selectors[0].toString().contains("::before"));
    }

    @Test
    void testMultipleDeclarations() {
        String input = ".multi { color: blue; font-size: 16px; margin: 10px; padding: 5px; }";
        CSSStylesheet stylesheet = css.process(input);
        
        assertNotNull(stylesheet);
        CSSRule rule = stylesheet.getRules()[0];
        assertEquals(4, rule.getDeclarations().length);
    }

    @Test
    void testImportantFlag() {
        String input = ".important { color: red !important; }";
        CSSStylesheet stylesheet = css.process(input);
        
        assertNotNull(stylesheet);
        CSSRule rule = stylesheet.getRules()[0];
        CSSDeclaration declaration = rule.getDeclarations()[0];
        assertTrue(declaration.isImportant());
    }

    @Test
    void testInvalidCSS() {
        String input = "invalid { color: }";
        assertThrows(RuntimeException.class, () -> css.process(input));
    }
}
