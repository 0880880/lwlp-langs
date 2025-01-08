package com.github.zeroeighteightzero.lwlp.langs.css;

public class CSSDeclaration {

    public String property;
    public CSSValue[] values;
    public boolean important;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(property).append(": ");
        for (int i = 0; i < values.length; i++) {
            if (i > 0)
                sb.append(',');
            sb.append(values[i]);
        }
        return sb.toString();
    }

    public boolean isImportant() {
        return important;
    }

}
