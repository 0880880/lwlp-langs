package com.github.zeroeighteightzero.lwlp.langs.css;

public class CSSNumberUnit {

    public float number;
    public CSSUnit unit = CSSUnit.NONE;

    public CSSNumberUnit(float number) {
        this.number = number;
    }

    public CSSNumberUnit(float number, CSSUnit unit) {
        this.number = number;
        this.unit = unit;
    }

    @Override
    public String toString() {
        boolean round = number % 1 == 0;
        if (unit != CSSUnit.NONE && round)
            return ((int) number) + unit.getName();
        else if (unit != CSSUnit.NONE)
            return number + unit.getName();
        if (round)
            return String.valueOf((int) number);
        return String.valueOf(number);
    }
}
