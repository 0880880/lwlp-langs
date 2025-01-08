package com.github.zeroeighteightzero.lwlp.langs.css;

public enum CSSUnit {

    NONE(null),
    PX("px"),
    PT("pt"),
    PC("pc"),
    CM("cm"),
    MM("mm"),
    Q("Q"),
    IN("in"),

    EM("em"),
    REM("rem"),
    EX("ex"),
    CH("ch"),
    PERCENT("%"),
    VW("vw"),
    VH("vh"),
    VMIN("vmin"),
    VMAX("vmax"),

    S("s"),
    MS("ms"),

    DEG("deg"),
    RAD("rad"),
    GRAD("grad"),
    TURN("turn"),

    N("n");

    private final String name;

    CSSUnit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static CSSUnit fromString(String text) {
        for (CSSUnit unit : CSSUnit.values()) {
            if (unit.getName() == null) continue;
            if (unit.getName().equalsIgnoreCase(text)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("No enum constant with text " + text);
    }

}
