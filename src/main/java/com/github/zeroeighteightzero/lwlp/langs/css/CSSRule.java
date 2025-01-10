package com.github.zeroeighteightzero.lwlp.langs.css;

public class CSSRule {

    public CSSSelector selector;
    public CSSDeclaration[] declarations;

    @Override
    public String toString() {
        return toString(false);
    }

    private String selectorsToString(StringBuilder sb) {
        sb.append(selector);
        return sb.toString();
    }

    public String selectorsToString() {
        return selectorsToString(new StringBuilder());
    }

    public String toString(boolean multiline) {
        StringBuilder sb = new StringBuilder();
        selectorsToString(sb);
        sb.append('{');
        if (multiline) sb.append('\n');
        for (CSSDeclaration declaration : declarations) {
            sb.append(declaration).append(";");
            if (multiline) sb.append('\n');
        }
        sb.append('}');
        return sb.toString();
    }

    public CSSSelector getSelector() {
        return selector;
    }

    public CSSDeclaration[] getDeclarations() {
        return declarations;
    }

}
