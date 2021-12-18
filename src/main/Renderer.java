package main;

import lwjglutils.*;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import transforms.*;


import java.util.ArrayList;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

/**
 * @author PGRF FIM UHK
 * @version 2.0
 * @since 2019-09-02
 */
public class Renderer extends AbstractRenderer {


    private int shaderProgramDith;
    private OGLBuffers buffers;

    private int locColorMode, locDitherMode, locBayerMatrix, locTresHold, locNoiseConst;

    private OGLTexture2D texture;

    private int ditherMode = 0;
    private int colorMode = 0;
    private int bayerMatrix = 0;
    private float tresHold = 0.4f;
    private float noiseConst = 0.5f;

    ArrayList<OGLTexture2D> textures = new ArrayList<>();

    private int choosedTexture = 0;
    private int switchTexture = 0;
    private boolean uploadImage = false, showLabels = true;

    // private OGLTexture.Viewer viewer;
    //private static List<Integer> VIEW_TYPES = Arrays.asList(GL_LINE, GL_POINT, GL_FILL);

    private String getLabel(String type) {
        String label = null;
        if (Objects.equals(type, "ditherMode")) {
            switch (ditherMode) {
                case 0 -> {
                    label = "Random dithering";
                }
                case 1 -> {
                    label = "Ordered dithering";
                }
                case 2 -> {
                    label = "Treshold dithering";
                }
            }
        }
        if (Objects.equals(type, "colorMode")) {
            switch (colorMode) {
                case 0 -> {
                    label = "Original image";
                }
                case 1 -> {
                    label = "Color dithering";
                }
                case 2 -> {
                    label = "Grayscale dithering";
                }
            }
        }
        if (Objects.equals(type, "bayerMatrix")) {
            switch (bayerMatrix) {
                case 0 -> {
                    label = "2x2";
                }
                case 1 -> {
                    label = "4x4";
                }
                case 2 -> {
                    label = "8x8";
                }
            }
        }
        if(Objects.equals(type, "treshold")){
            label = (int)(tresHold*100)+"%";
        }
        if(Objects.equals(type, "noiseConst")){
            label = (int)(noiseConst*100)+"%";
        }
        return label;

    }

    @Override
    public void init() {
        OGLUtils.printOGLparameters();
        OGLUtils.printJAVAparameters();
        OGLUtils.printLWJLparameters();
        OGLUtils.shaderCheck();

        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        buffers = Quad.getQuad();
        // shaderProgramDith = ShaderUtils.loadProgram("/dither");
        shaderProgramDith = ShaderUtils.loadProgram("/dither");
//        textures.add("andrea1.jpg");
//        textures.add("andrea2.jpg");
//        textures.add("covid.jpg");
//        textures.add("kate.jpg");
//        textures.add("monica.jpg");



//        FileControler.setDefaultCountOfImages(textures.size());
        textures.addAll(FileControler.getAllImages());
        System.out.println(FileControler.getAllImages());

        texture = textures.get(0);


        locColorMode = glGetUniformLocation(shaderProgramDith, "colorMode");
        locDitherMode = glGetUniformLocation(shaderProgramDith, "ditherMode");
        locBayerMatrix = glGetUniformLocation(shaderProgramDith, "bayerMatrix");
        locTresHold = glGetUniformLocation(shaderProgramDith, "tresHold");
        locNoiseConst = glGetUniformLocation(shaderProgramDith, "noiseConst");

//        Mat4 projection = new Mat4PerspRH(Math.PI / 3, 600 / 800f, 1.0, 20.0);


        textRenderer = new OGLTextRenderer(width, height);
    }

    @Override
    public void display() {
        glEnable(GL_DEPTH_TEST); // zapnout z-buffer (kvůli TextRendereru)
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        render();
        if(showLabels){
            textRenderer.addStr2D(20, 20, "PGRF3 - 2");
            textRenderer.addStr2D(20, 40, "Dither mode: " + getLabel("ditherMode"));
            textRenderer.addStr2D(20, 60, "Color mode: " + getLabel("colorMode"));
            if(ditherMode == 0){
                textRenderer.addStr2D(20, 80, "Random noise: " + getLabel("noiseConst"));
            }
            if (ditherMode == 1) {
                textRenderer.addStr2D(20, 80, "Matrix mode: " + getLabel("bayerMatrix"));
            }
            if(ditherMode == 2){
                textRenderer.addStr2D(20, 80, "Treashold: " + getLabel("treshold"));
            }
        }


    }


    private int boolToInt(boolean bool) {
        return bool ? 1 : 0;
    }

    private void render() {
        // výchozí framebuffer - render to obrazovky
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        // nutno opravit viewport, protože render target si nastavuje vlastní
        glViewport(0, 0, width, height);

        glClearColor(0, 0.5f, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glUseProgram(shaderProgramDith);

        if (choosedTexture != switchTexture) {

            System.out.println("Chi zobrazit: "+ choosedTexture);
                texture = textures.get(choosedTexture);
                switchTexture = choosedTexture;
        }

        if(uploadImage){
            OGLTexture2D existingTexture = FileControler.loadImage();
            if(existingTexture != null)texture = existingTexture;
            uploadImage = false;
        }

        texture.bind(shaderProgramDith, "mosaic", 0);
        buffers.draw(GL_TRIANGLES, shaderProgramDith);

        glUniform1i(locColorMode, colorMode);
        glUniform1i(locDitherMode, ditherMode);
        glUniform1i(locBayerMatrix, bayerMatrix);
        glUniform1f(locTresHold, tresHold);
        glUniform1f(locNoiseConst, noiseConst);
    }

    @Override
    public GLFWCursorPosCallback getCursorCallback() {
        return cursorPosCallback;
    }

    @Override
    public GLFWMouseButtonCallback getMouseCallback() {
        return mouseButtonCallback;
    }

    @Override
    public GLFWKeyCallback getKeyCallback() {
        return keyCallback;
    }

    private final GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
        @Override
        public void invoke(long window, double x, double y) {

        }
    };

    private final GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int button, int action, int mods) {
            if (button == GLFW_MOUSE_BUTTON_LEFT) {

            }
        }
    };

    private final GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (action == GLFW_PRESS || action == GLFW_REPEAT) {
                switch (key) {
                    // přepínání ditheringu
                    case GLFW_KEY_X -> {
                        if (ditherMode == 2) {
                            ditherMode = 0;
                        } else {
                            ditherMode++;
                            bayerMatrix = 0;
                            tresHold = 0.4f;
                            noiseConst = 0.5f;

                        }
                    }
                    //  přepínání fotky/brevného ditheringu/ grayscale ditheringu v daném ditherModu
                    case GLFW_KEY_C -> {
                        if (colorMode == 2) {
                            colorMode = 0;
                        } else {
                            colorMode++;
                        }

                    }
                    // změna matice při použití ordered dithering (zvětšení matice) (max 8x8)
                    case GLFW_KEY_UP -> {
                        if (noiseConst < 1 && ditherMode == 0) {
                            noiseConst = noiseConst + 0.1f;
                        }
                        if (bayerMatrix < 2 && ditherMode == 1) {
                            bayerMatrix++;
                        }
                        if (tresHold < 1 && ditherMode == 2) {
                            tresHold = tresHold + 0.1f;
                        }
                    }
                    // změna matice při použití ordered dithering (zmenšení matice) (min 2x2)
                    case GLFW_KEY_DOWN -> {
                        if(noiseConst > 0.1f && ditherMode == 0){
                            noiseConst = noiseConst - 0.1f;
                        }
                        if (bayerMatrix > 0 && ditherMode == 1) {
                            bayerMatrix--;
                        }
                        if(tresHold > 0.1f && ditherMode == 2){
                            tresHold = tresHold - 0.1f;
                        }
                    }
                    // další image/textura
                    case GLFW_KEY_RIGHT -> {
                        if (choosedTexture != textures.size() - 1) {
                            choosedTexture++;
                        }
                    }
                    // předchozí image/textura
                    case GLFW_KEY_LEFT -> {
                        if (choosedTexture != 0) {
                            choosedTexture--;
                        }
                    }
                    // upload textury
                    case GLFW_KEY_U -> {
                        uploadImage = true;
                    }
                    // show/hide labels
                    case GLFW_KEY_H -> {
                        showLabels = !showLabels;
                    }

                }
            }
        }
    };
}
