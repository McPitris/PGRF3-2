package main;

import lwjglutils.*;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import transforms.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

   // private boolean mousePressed;

    private int shaderProgramDith;
    private OGLBuffers buffers;

    private Mat4 projection;
   // private int locView, locProjection, locTime, locTimeLight, locSolid, locLightPosition, locEyePosition, locLightVP, locTransform, locLight;
private int locColorMode;

    private int locViewLight, locProjectionLight, locSolidLight;
    private OGLTexture2D texture;

    private int ditherMode = 0;
    private int colorMode = 0;

   // private OGLTexture.Viewer viewer;
    //private static List<Integer> VIEW_TYPES = Arrays.asList(GL_LINE, GL_POINT, GL_FILL);


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
        try {
            texture = new OGLTexture2D("textures/andrea1.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        locColorMode = glGetUniformLocation(shaderProgramDith, "colorMode");

        projection = new Mat4PerspRH(Math.PI / 3, 600 / 800f, 1.0, 20.0);


        textRenderer = new OGLTextRenderer(width, height);
    }

    @Override
    public void display() {
        glEnable(GL_DEPTH_TEST); // zapnout z-buffer (kvůli TextRendereru)
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        render();


        textRenderer.addStr2D(20, 20, "PGRF3 - 2");
 }


    private int boolToInt(boolean bool){
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
        texture.bind(shaderProgramDith, "mosaic", 0);
        buffers.draw(GL_TRIANGLES,shaderProgramDith);

        glUniform1i(locColorMode, colorMode);
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
                    // přepínání fotky/brevného ditheringu/ grayscale ditheringu v daném ditherModu //TODO: aktuálně pouze random dithering
                    case GLFW_KEY_C -> {
                        if(colorMode == 2){
                            colorMode=0;
                        }else{
                            colorMode++;
                        }

                    }
                }
            }
        }
    };
}
