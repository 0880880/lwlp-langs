package com.github.zeroeighteightzero.lwlp.langs.css;

import com.github.tommyettinger.colorful.pure.cielab.ColorTools;
import com.github.tommyettinger.digital.TrigTools;

public class CSSColor {

    public float r, g, b, a = 1;

    public boolean currentColor;

    public CSSColor fromRGB(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        return this;
    }

    public CSSColor fromRGB(float r, float g, float b, float alpha) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = alpha;
        return this;
    }

    public CSSColor fromLAB(float l, float a, float b, float alpha) {
        float encoded = ColorTools.cielab(l, a, b, alpha);
        this.r = ColorTools.red(encoded);
        this.g = ColorTools.green(encoded);
        this.b = ColorTools.blue(encoded);
        this.a = alpha;
        return this;
    }

    public CSSColor fromLAB(float l, float a, float b) {
        return fromLAB(l, a, b, 1);
    }

    public CSSColor fromLCH(float l, float c, float h, float alpha) {
        float hRad = h * TrigTools.PI2;
        fromLAB(l, c * TrigTools.cos(hRad), TrigTools.sin(hRad), alpha);
        return this;
    }

    public CSSColor fromLCH(float l, float c, float h) {
        return fromLCH(l, c, h, 1);
    }

    public CSSColor fromHSV(float h, float s, float v, float alpha) {
        int i = (int) (h * 6);
        float f = h * 6 - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);
        i = i % 6;

        switch (i) {
            case 0: r = v; g = t; b = p; break;
            case 1: r = q; g = v; b = p; break;
            case 2: r = p; g = v; b = t; break;
            case 3: r = p; g = q; b = v; break;
            case 4: r = t; g = p; b = v; break;
            case 5: r = v; g = p; b = q; break;
        }
        this.a = alpha;
        return this;
    }

    public CSSColor fromHSV(float h, float s, float v) {
        return fromHSV(h, s, v, 1);
    }

    public CSSColor fromHSL(float h, float s, float l, float alpha) {
        float v = l + s * Math.min(l, 1-l);
        float saturation;
        if (v <= 0)
            saturation = 0;
        else
            saturation = 2 * (v-l)/v;
        fromHSV(h, saturation, v, alpha);
        return this;
    }

    public CSSColor fromHSL(float h, float s, float l) {
        return fromHWB(h, s, l, 1);
    }

    public CSSColor fromHWB(float h, float w, float b, float alpha) {
        float v = 1 - w - b;
        fromHSV(h, v == 0 ? (v - Math.max(w, b)) / v : 0, v, alpha);
        return this;
    }

    public CSSColor fromHWB(float h, float w, float b) {
        return fromHWB(h, w, b, 1);
    }

    public CSSColor fromHex(String hex) {
        if ((hex.length() ^ 5) * (hex.length() ^ 9) != 0 || hex.charAt(0) != '#') {
            throw new IllegalArgumentException("Invalid hex format. Use #RGBA or #RRGGBBAA.");
        }

        int r, g, b, a;
        if (hex.length() == 5) {
            int rgba = Integer.parseInt(hex.substring(1), 16);
            r = ((rgba >> 12) & 0xF) * 0x11;
            g = ((rgba >> 8) & 0xF) * 0x11;
            b = ((rgba >> 4) & 0xF) * 0x11;
            a = (rgba & 0xF) * 0x11;
        } else {
            long rgba = Long.parseLong(hex.substring(1), 16);
            r = (int) ((rgba >> 24) & 0xFF);
            g = (int) ((rgba >> 16) & 0xFF);
            b = (int) ((rgba >> 8) & 0xFF);
            a = (int) (rgba & 0xFF);
        }

        final float scale = 1 / 255.0f;
        this.r = r * scale;
        this.g = g * scale;
        this.b = b * scale;
        this.a = a * scale;
        return this;
    }


    public CSSColor fromColor(CSSColor other) {
        this.r = other.r;
        this.g = other.g;
        this.b = other.b;
        this.a = other.a;
        return this;
    }

    public CSSColor fromName(String name) {
        CSSColor from = CSSColors.get(name);
        if (from == null) return this;
        fromColor(CSSColors.get(name));
        return this;
    }

    public CSSColor() {}

    public CSSColor(String hex) {
        fromHex(hex);
    }

    public CSSColor(float r, float g, float b) {
        fromRGB(r, g, b);
    }

    public CSSColor(float r, float g, float b, float alpha) {
        fromRGB(r, g, b, alpha);
    }

    @Override
    public String toString() {
        if (currentColor)
            return "currentColor";
        return "(" + r + "," + g + "," + b + "," + a + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CSSColor cssColor = (CSSColor) o;
        return Float.compare(r, cssColor.r) == 0 && Float.compare(g, cssColor.g) == 0 && Float.compare(b, cssColor.b) == 0 && Float.compare(a, cssColor.a) == 0 && currentColor == cssColor.currentColor;
    }

}
