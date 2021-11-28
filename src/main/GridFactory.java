package main;

import lwjglutils.OGLBuffers;

public class GridFactory {

    /**
     * @param a počet vrcholů na řádku
     * @param b počet vrcholů ve sloupci
     * @return OGLBuffers
     */

    private static float[] getVertexBuffer(int a, int b) {
        float[] vb = new float[a * b * 2];
        int index = 0;

        for (int j = 0; j < b; j++) {
            float y = j / (float) (b - 1);

            for (int i = 0; i < a; i++) {
                float x = i / (float) (a - 1);

                vb[index++] = x;
                vb[index++] = y;
            }
        }
        return vb;
    }

    public static OGLBuffers createGrid(int a, int b) {

        float[] vb = getVertexBuffer(a, b);

        int[] ib = new int[(a - 1) * (b - 1) * 2 * 3];
        int index2 = 0;
        for (int j = 0; j < b - 1; j++) {
            int offset = j * a;
            for (int i = 0; i < a - 1; i++) {
                ib[index2++] = offset + i;
                ib[index2++] = offset + i + 1;
                ib[index2++] = offset + i + a;
                ib[index2++] = offset + i + a;
                ib[index2++] = offset + i + 1;
                ib[index2++] = offset + i + a + 1;
//                System.out.println(offset + i);
//                System.out.println(offset + i + 1);
//                System.out.println(offset + i + a);
//                System.out.println(offset + i + a);
//                System.out.println(offset + i + 1);
//                System.out.println(offset + i + a + 1);
//                System.out.println("---");
            }
        }

        OGLBuffers.Attrib[] attributes = {
                new OGLBuffers.Attrib("inPosition", 2)
        };
        return new OGLBuffers(vb, attributes, ib);
    }

    public static OGLBuffers createTriangleStrip(int a, int b) {
        float[] vb = getVertexBuffer(a, b);

        int[] ib = new int[2 * a * (b - 1) + 2 * (b - 1)];

        int index = 0;
        for (int row = 0; row < b - 1; row++) {
            if (row % 2 == 0) { // even row
                    for (int col = 0; col < a; col++) {
                        ib[index++] = col + row * a;
                        ib[index++] = col + (row + 1) * a;
                }
                if (row < b - 1) {
                    ib[index++] = (row + 1) * a + (a - 1);
                    ib[index++] = (row + 1) * a + (a - 1);
                }
            } else { // odd row
                for (int col = a - 1; col >= 0; col--) {
                    ib[index++] = col + (row + 1) * a;
                    ib[index++] = col + row * a;
                }
                if (row < b - 1) {
                    ib[index++] = (row + 1) * a;
                    ib[index++] = (row + 1) * a;
                }
            }
        }

        OGLBuffers.Attrib[] atributes = {
                new OGLBuffers.Attrib("inPosition", 2)
        };

        return new OGLBuffers(vb, atributes, ib);
    }
}
