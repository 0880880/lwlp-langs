package com.github.zeroeighteightzero.lwlp.langs.css;

public class FunctionUtils {

    public static boolean argumentsTypeCheck(CSSValue[] arguments, CSSType... types) {
        if (types.length != 1 && types.length != arguments.length)
            return false;
        for (int i = 0; i < arguments.length; i++) {
            if ((types.length == 1 && arguments[i].type != types[0])
                    || (types.length != 1 && arguments[i].type != types[i]))
                return false;
        }
        return true;
    }

    public static boolean argumentsNumberUnitCheck(CSSValue[] arguments, CSSUnit... units) {
        if (units.length != 1 && units.length != arguments.length)
            return false;
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i].type != CSSType.NUMBER_UNIT)
                continue;
            if ((units.length == 1 && arguments[i].numberUnit.unit != units[0])
                    || (units.length != 1 && units[i] != arguments[i].numberUnit.unit))
                return false;
        }
        return true;
    }

}
