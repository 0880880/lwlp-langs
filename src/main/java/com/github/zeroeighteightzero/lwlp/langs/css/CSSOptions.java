package com.github.zeroeighteightzero.lwlp.langs.css;

public class CSSOptions {

    public boolean convertColorFunctions = true;
    public boolean convertSingleUnitMath = true;
    public boolean createVariables = true;
    public boolean printAST = false;

    public void set(CSSOptions from) {
        convertColorFunctions = from.convertColorFunctions;
        convertSingleUnitMath = from.convertSingleUnitMath;
        printAST = from.printAST;
        createVariables = from.createVariables;
    }

}
