package com.github.zeroeighteightzero.lwlp.langs.css;

public class CSSExtendedValue {

    public CSSExtendedType type;
    public String identifier;
    public String string;
    public CSSNumberUnit numberUnit;
    public CSSSelector.CompoundSelector compoundSelector;

    public String toString() {
        switch (type) {
            case IDENTIFIER:
                return identifier;
            case STRING:
                return string;
            case NUMBER_UNIT:
                return numberUnit.toString();
            case COMPOUND_SELECTOR:
                return compoundSelector.toString();
        }
        return null;
    }

}
