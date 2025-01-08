package com.github.zeroeighteightzero.lwlp.langs.css;

public class CSSValue {

    // NO TYPE = true

    public CSSType type;
    public String identifier;
    public String string;
    public CSSNumberUnit numberUnit;
    public CSSMath math;
    public String functionName;
    public CSSValue[] functionArguments;
    public CSSColor color;

    public String toString() {
        switch (type) {
            case IDENTIFIER:
                return identifier;
            case STRING:
                return string;
            case COLOR:
                return color.toString();
            case MATH:
                return math.toString();
            case NUMBER_UNIT:
                return numberUnit.toString();
            case FUNCTION:
                StringBuilder sb = new StringBuilder();
                sb.append(functionName).append('(');
                for (int i = 0; i < functionArguments.length; i++) {
                    if (i > 0)
                        sb.append(',');
                    sb.append(functionArguments[i]);
                }
                sb.append(')');
                return sb.toString();
        }
        return null;
    }

}
