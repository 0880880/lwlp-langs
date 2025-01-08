package com.github.zeroeighteightzero.lwlp.langs.css;

import java.util.HashMap;

public class CSSMath {

    public HashMap<CSSUnit, CSSNumberUnit> values = new HashMap<>();

    public void add(CSSNumberUnit numberUnit) {
        values.put(numberUnit.unit, numberUnit);
    }

    public void add(float number, CSSUnit unit) {
        add(new CSSNumberUnit(number, unit));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (CSSUnit unit : values.keySet()) {
            float num = values.get(unit).number;
            if (i > 0) {
                boolean sign = num < 0;
                sb.append(' ');
                if (sign)
                    sb.append('-');
                else
                    sb.append('+');
                sb.append(' ');
                Number normalizedNum = num * (sign ? -1 : 1);
                if (num % 1f == 0f) normalizedNum = (int) normalizedNum.floatValue();
                sb.append(normalizedNum).append(unit.getName());
            } else {
                sb.append(values.get(unit));
            }
            i++;
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CSSMath math = (CSSMath) o;
        if (math.values.size() != values.size()) return false;
        for (CSSUnit unit : math.values.keySet()) {
            if (!values.containsKey(unit) && values.get(unit).number != math.values.get(unit).number)
                return false;
        }
        for (CSSUnit unit : values.keySet()) {
            if (!math.values.containsKey(unit) && values.get(unit).number != math.values.get(unit).number)
                return false;
        }
        return true;
    }

}
