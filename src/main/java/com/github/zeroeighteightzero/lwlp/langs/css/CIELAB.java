package com.github.zeroeighteightzero.lwlp.langs.css;

public class CIELAB {
    // Constants for CIELAB conversion
    private static final float EPSILON = 0.008856f;
    private static final float KAPPA = 903.3f;

    // sRGB D65 conversion matrix (XYZ to RGB)
    private static final float[][] XYZ_TO_RGB = {
        { 3.2404542f, -1.5371385f, -0.4985314f },
        {-0.9692660f,  1.8760108f,  0.0415560f },
        { 0.0556434f, -0.2040259f,  1.0572252f }
    };

    private static float labFInverse(float t) {
        float t3 = t * t * t;
        if (t3 > EPSILON) {
            return t3;
        }
        return (116 * t - 16) / KAPPA;
    }

    private static float[] labToXYZ(float l, float a, float b, float xn, float yn, float zn) {
        // Ensure L is in valid range [0, 100]
        l = Math.max(0, Math.min(100, l * 100f));
        
        float fy = (l + 16) / 116;
        float fx = fy + ((a * 255 - 127) / 500);
        float fz = fy - ((b * 255 - 127) / 200);

        float x = xn * labFInverse(fx);
        float y = yn * labFInverse(fy);
        float z = zn * labFInverse(fz);

        return new float[]{x, y, z};
    }

    private static float[] xyzToRGB(float x, float y, float z) {
        float r = XYZ_TO_RGB[0][0] * x + XYZ_TO_RGB[0][1] * y + XYZ_TO_RGB[0][2] * z;
        float g = XYZ_TO_RGB[1][0] * x + XYZ_TO_RGB[1][1] * y + XYZ_TO_RGB[1][2] * z;
        float b = XYZ_TO_RGB[2][0] * x + XYZ_TO_RGB[2][1] * y + XYZ_TO_RGB[2][2] * z;
        
        return new float[]{r, g, b};
    }

    private static float gammaCorrect(float linear) {
        if (linear <= 0.0031308f) {
            return 12.92f * linear;
        }
        return 1.055f * (float) Math.pow(linear, 1.0f / 2.4f) - 0.055f;
    }

    private static float clamp(float value) {
        return Math.max(0, Math.min(1, value));
    }

    public static float red(float l, float a, float b, float illuminantX, float illuminantY, float illuminantZ) {
        float[] xyz = labToXYZ(l, a, b, illuminantX, illuminantY, illuminantZ);
        float[] rgb = xyzToRGB(xyz[0], xyz[1], xyz[2]);
        return clamp(gammaCorrect(rgb[0]));
    }

    public static float green(float l, float a, float b, float illuminantX, float illuminantY, float illuminantZ) {
        float[] xyz = labToXYZ(l, a, b, illuminantX, illuminantY, illuminantZ);
        float[] rgb = xyzToRGB(xyz[0], xyz[1], xyz[2]);
        return clamp(gammaCorrect(rgb[1]));
    }

    public static float blue(float l, float a, float b, float illuminantX, float illuminantY, float illuminantZ) {
        float[] xyz = labToXYZ(l, a, b, illuminantX, illuminantY, illuminantZ);
        float[] rgb = xyzToRGB(xyz[0], xyz[1], xyz[2]);
        return clamp(gammaCorrect(rgb[2]));
    }

    public static float red(float l, float a, float b, CIEIlluminant illuminant) {
        return red(l, a, b, illuminant.getX(), illuminant.getY(), illuminant.getZ());
    }

    public static float green(float l, float a, float b, CIEIlluminant illuminant) {
        return green(l, a, b, illuminant.getX(), illuminant.getY(), illuminant.getZ());
    }

    public static float blue(float l, float a, float b, CIEIlluminant illuminant) {
        return blue(l, a, b, illuminant.getX(), illuminant.getY(), illuminant.getZ());
    }

    public static void main(String[] args) {
        // Test case 1: Pure white (L=100, a=0, b=0)
        System.out.println("Test 1 - White (L=100, a=0, b=0):");
        int r1 = (int)(red(1f, 1/2f, 1/2f, CIEIlluminant.D50) * 255);
        int g1 = (int)(green(1f, 1/2f, 1/2f, CIEIlluminant.D50) * 255);
        int b1 = (int)(blue(1f, 1/2f, 1/2f, CIEIlluminant.D50) * 255);
        System.out.printf("RGB: (%d, %d, %d)%n", r1, g1, b1);

        // Test case 2: Pure black (L=0, a=0, b=0)
        System.out.println("\nTest 2 - Black (L=0, a=0, b=0):");
        int r2 = (int)(red(0, 1/2f, 1/2f, CIEIlluminant.D50) * 255);
        int g2 = (int)(green(0, 1/2f, 1/2f, CIEIlluminant.D50) * 255);
        int b2 = (int)(blue(0, 1/2f, 1/2f, CIEIlluminant.D50) * 255);
        System.out.printf("RGB: (%d, %d, %d)%n", r2, g2, b2);

        // Test case 3: Red-ish color (L=50, a=80, b=67)
        System.out.println("\nTest 3 - Red-ish (L=50, a=80, b=67):");
        int r3 = (int)(red(50 / 100f, (80 + 128) / 255f, (67 + 128) / 255f, CIEIlluminant.D50) * 255);
        int g3 = (int)(green(50 / 100f, (80 + 128) / 255f, (67 + 128) / 255f, CIEIlluminant.D50) * 255);
        int b3 = (int)(blue(50 / 100f, (80 + 128) / 255f, (67 + 128) / 255f, CIEIlluminant.D50) * 255);
        System.out.printf("RGB: (%d, %d, %d)%n", r3, g3, b3);

        // Test case 4: Blue-ish color (L=50, a=-15, b=-65)
        System.out.println("\nTest 4 - Blue-ish (L=50, a=-15, b=-65):");
        int r4 = (int)(red(50 / 100f, (-15 + 128) / 255f, (-65 + 128) / 255f, CIEIlluminant.D50) * 255);
        int g4 = (int)(green(50 / 100f, (-15 + 128) / 255f, (-65 + 128) / 255f, CIEIlluminant.D50) * 255);
        int b4 = (int)(blue(50 / 100f, (-15 + 128) / 255f, (-65 + 128) / 255f, CIEIlluminant.D50) * 255);
        System.out.printf("RGB: (%d, %d, %d)%n", r4, g4, b4);
    }
}
