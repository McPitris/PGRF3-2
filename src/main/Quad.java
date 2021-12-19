package main;

import lwjglutils.OGLBuffers;
/**
 * @author Michal Petras
 * @version 1.0
 * @since 2021-12-18
 */
public class Quad {
// https://home.zcu.cz/~smolik/zpg/cviceni/cv_04.html
    private static float[] getVertexBuffer() {
        return new float[]{
                -1, -1, 0, 1,
                1, -1, 1, 1,
                1, 1, 1, 0,
                -1, 1, 0, 0
        };
    }

    public static OGLBuffers getQuad() {
        float [] vb = getVertexBuffer();
        int[] ib = {0, 1, 2, 2, 0, 3};

        OGLBuffers.Attrib[] attributes = {
                new OGLBuffers.Attrib("inPosition", 2),
                new OGLBuffers.Attrib("inTextureCoord", 2)
        };

        return new OGLBuffers(vb, attributes, ib);
    }
}
