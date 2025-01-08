package com.github.zeroeighteightzero.lwlp.langs.css;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CSSParserTest {
    private final CSS css = new CSS();

    @Test
    void testAttributeSelectors() {
        String input = "input[type=\"text\"] { border: 1px solid black; }";
        CSSStylesheet stylesheet = css.process(input);
        
        assertNotNull(stylesheet);
        assertEquals(1, stylesheet.getRules().length);
        CSSRule rule = stylesheet.getRules()[0];
        assertTrue(rule.getSelectors()[0].toString().contains("[type=\"text\"]"));
    }

    @Test
    @Disabled("Feature not yet implemented")
    void testPseudoClassWithFunctionArgument() {
        String input = "div:nth-child(2n+1) { background: yellow; }";
        CSSStylesheet stylesheet = css.process(input);
        
        assertNotNull(stylesheet);
        CSSRule rule = stylesheet.getRules()[0];
        assertTrue(rule.getSelectors()[0].toString().contains(":nth-child(2n+1)"));
    }

    @Test
    void testComplexCombinators() {
        String input = "div > p ~ span + a { color: blue; }";
        CSSStylesheet stylesheet = css.process(input);
        
        assertNotNull(stylesheet);
        CSSRule rule = stylesheet.getRules()[0];
        String selector = rule.getSelectors()[0].toString();
        assertTrue(selector.contains(">"));
        assertTrue(selector.contains("~"));
        assertTrue(selector.contains("+"));
    }

    @Test
    void testMultipleValuesWithCommas() {
        String input = ".box { background: rgb(255, 0, 0); }";
        CSSStylesheet stylesheet = css.process(input);
        
        assertNotNull(stylesheet);
        CSSRule rule = stylesheet.getRules()[0];
        CSSDeclaration decl = rule.getDeclarations()[0];
        assertEquals("background", decl.property);
        System.out.println(decl.values[0]);
        assertEquals(decl.values[0].color, new CSSColor(1, 0, 0, 1));
    }

    @Test
    void testNestedCalcExpressions() {
        String input = ".container { width: calc(100% - (20px + 2em)); }";
        CSSStylesheet stylesheet = css.process(input);
        
        assertNotNull(stylesheet);
        CSSRule rule = stylesheet.getRules()[0];
        CSSDeclaration decl = rule.getDeclarations()[0];
        assertEquals("width", decl.property);
        assertTrue(decl.values[0].toString().contains("calc"));
    }

    @Test
    void testMultipleSelectorsWithPseudoElements() {
        String input = "p::first-line, h1::before { text-transform: uppercase; }";
        CSSStylesheet stylesheet = css.process(input);
        
        assertNotNull(stylesheet);
        CSSRule rule = stylesheet.getRules()[0];
        assertEquals(2, rule.getSelectors().length);
        assertTrue(rule.getSelectors()[0].toString().contains("::first-line"));
        assertTrue(rule.getSelectors()[1].toString().contains("::before"));
    }

    @Test
    void testComplexAttributeSelectors() {
        String input = "a[href=\"https\"][target=\"_blank\"] { color: green; }"; // [href^="https"]
        CSSStylesheet stylesheet = css.process(input);
        
        assertNotNull(stylesheet);
        CSSRule rule = stylesheet.getRules()[0];
        String selector = rule.getSelectors()[0].toString();
        assertTrue(selector.contains("[href=\"https\"]")); // [href^="https"]
        assertTrue(selector.contains("[target=\"_blank\"]"));
    }

    @Test
    void testMathematicalExpressions() {
        String input = ".math { margin: calc(2 * (30px + 5%)); }";
        CSSStylesheet stylesheet = css.process(input);
        
        assertNotNull(stylesheet);
        CSSRule rule = stylesheet.getRules()[0];
        CSSDeclaration decl = rule.getDeclarations()[0];
        assertEquals("margin", decl.property);
        assertTrue(decl.values[0].toString().contains("calc"));
    }

    @Test
    void testMultiplePropertiesWithImportant() {
        String input = ".important { color: red !important; font-weight: bold !important; }";
        CSSStylesheet stylesheet = css.process(input);
        
        assertNotNull(stylesheet);
        CSSRule rule = stylesheet.getRules()[0];
        assertEquals(2, rule.getDeclarations().length);
        assertTrue(rule.getDeclarations()[0].isImportant());
        assertTrue(rule.getDeclarations()[1].isImportant());
    }

    @Test
    void testCommentsInSelectors() {
        String input = "/* Header styles */\nh1 /* Main title */ { color: black; }";
        CSSStylesheet stylesheet = css.process(input);
        
        assertNotNull(stylesheet);
        CSSRule rule = stylesheet.getRules()[0];
        assertEquals("h1", rule.getSelectors()[0].toString());
    }
}
