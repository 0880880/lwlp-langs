package com.github.zeroeighteightzero.lwlp.langs.css;

public class CSSOptions {

    public boolean convertColorFunctions = true;
    public boolean convertSingleUnitMath = true;

    public void set(CSSOptions from) {
        convertColorFunctions = from.convertColorFunctions;
        convertSingleUnitMath = from.convertSingleUnitMath;
    }

}
