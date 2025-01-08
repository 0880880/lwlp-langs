package com.github.zeroeighteightzero.lwlp.langs.css;

public class CSSRule {

    public CSSSelector[] selectors;
    public CSSDeclaration[] declarations;

    @Override
    public String toString() {
        return toString(false);
    }

    private String selectorsToString(StringBuilder sb) {
        if (selectors.length == 0) return "";
        for (int i = 0; i < selectors.length; i++) {
            if (i > 0)
                sb.append(',');
            sb.append(selectors[i]);
        }
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

    public CSSSelector[] getSelectors() {
        return selectors;
    }

    public CSSDeclaration[] getDeclarations() {
        return declarations;
    }

}
