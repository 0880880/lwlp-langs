package com.github.zeroeighteightzero.lwlp.langs.css;

public enum CIEIlluminant {
    A(1.0985f, 1.0000f, 0.3551f),
    C(0.9807f, 1.0000f, 1.1821f),
    D50(0.9672f, 1.0000f, 1.1372f),
    D65(0.9505f, 1.0000f, 1.0890f),
    D75(0.9497f, 1.0000f, 1.1117f),
    E(1.0000f, 1.0000f, 1.0000f),
    F1(0.9919f, 1.0000f, 1.0270f),
    F2(1.0000f, 1.0000f, 1.1940f),
    F3(1.0121f, 1.0000f, 1.0756f),
    F4(1.0323f, 1.0000f, 1.0870f),
    F5(0.9972f, 1.0000f, 1.1020f),
    F6(1.0200f, 1.0000f, 1.0856f),
    F7(1.0000f, 1.0000f, 1.1940f),
    F8(1.0200f, 1.0000f, 1.0856f),
    F9(0.9972f, 1.0000f, 1.1020f),
    F11(0.9919f, 1.0000f, 1.0275f),
    F12(1.0000f, 1.0000f, 1.1945f),
    D65_10(0.9504f, 1.0000f, 1.0891f),
    D50_10(0.9674f, 1.0000f, 1.1373f),
    D75_10(0.9499f, 1.0000f, 1.1118f),
    TL84(0.9890f, 1.0000f, 1.0770f),
    HPS(0.9925f, 1.0000f, 1.0543f),
    LPS(0.9443f, 1.0000f, 1.0087f);

    private final float X;
    private final float Y;
    private final float Z;

    CIEIlluminant(float X, float Y, float Z) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    public float getX() {
        return X;
    }

    public float getY() {
        return Y;
    }

    public float getZ() {
        return Z;
    }

    public float[] getXYZ() {
        return new float[] {X, Y, Z};
    }

    @Override
    public String toString() {
        return name() + " [X=" + X + ", Y=" + Y + ", Z=" + Z + "]";
    }
}
