package main;

import lwjglutils.OGLBuffers;

public class Quad {
// https://home.zcu.cz/~smolik/zpg/cviceni/cv_04.html
    private static float[] getVertexBuffer() {
        float[] vb = {
                -1, -1, 0, 0, // textura je opačně TODO: nutno přepsat a otočit texturu správně
                1, -1, 1, 0,
                1, 1, 1, 1,
                -1, 1, 0, 1
        };
        return vb;
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
