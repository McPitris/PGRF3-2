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

    private double oldMx, oldMy;
    private boolean mousePressed;

    private int shaderProgramViewer, shaderProgramLight;
    private OGLBuffers buffers;
    private OGLRenderTarget renderTarget;

    private Camera camera, cameraLight;
    private Mat4 projection;
    private int locView, locProjection, locTime, locTimeLight, locSolid, locLightPosition, locEyePosition, locLightVP, locTransform, locLight;


    private float time = 1;
    private float timeDirection = 0.005f;

    private float [] translate =  {0.0f, 0.0f, 0.0f};
    private float translateSpeed = 0.1f;

    private int locViewLight, locProjectionLight, locSolidLight;
    private OGLTexture2D mosaicTexture, globeTexture;
    private OGLTexture.Viewer viewer;
    private static List<Integer> VIEW_TYPES = Arrays.asList(GL_LINE, GL_POINT, GL_FILL);
    private int test = GL_FILL;

    private boolean perspective = true;
    private boolean animateObj = false;
    private boolean ambientBool = true, diffuseBool = true, specularBool = true;
    private boolean texture = true;
    private boolean cameraMove = true;

    private int showedObject = 0;
    private int [] axis = new int[3];



    @Override
    public void init() {
        OGLUtils.printOGLparameters();
        OGLUtils.printJAVAparameters();
        OGLUtils.printLWJLparameters();
        OGLUtils.shaderCheck();

        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        shaderProgramViewer = ShaderUtils.loadProgram("/start");
        shaderProgramLight = ShaderUtils.loadProgram("/light");

        locView = glGetUniformLocation(shaderProgramViewer, "view");
        locProjection = glGetUniformLocation(shaderProgramViewer, "projection");
        locSolid = glGetUniformLocation(shaderProgramViewer, "solid");
        locLightPosition = glGetUniformLocation(shaderProgramViewer, "lightPosition");
        locEyePosition = glGetUniformLocation(shaderProgramViewer, "eyePosition");
        locLightVP = glGetUniformLocation(shaderProgramViewer, "lightVP");

        locViewLight = glGetUniformLocation(shaderProgramLight, "view");
        locProjectionLight = glGetUniformLocation(shaderProgramLight,"projection");
        locSolidLight = glGetUniformLocation(shaderProgramLight, "solid");

        locTransform =  glGetUniformLocation(shaderProgramViewer, "transform");
        locTime = glGetUniformLocation(shaderProgramViewer, "time");
        locTimeLight = glGetUniformLocation(shaderProgramViewer, "time");


//        view = new Mat4ViewRH();
//        camera = new Camera(
//                new Vec3D(6, 6, 5),
//                5 / 4.0 * Math.PI,
//                -1 / 5.0 * Math.PI,
//                1.0,
//                true
//        );
        camera = new Camera()
                .withPosition(new Vec3D(-3, 3, 3))
                .withAzimuth(-1 / 4.0 * Math.PI)
                .withZenith(-1.3 / 5.0 * Math.PI);

        projection = new Mat4PerspRH(Math.PI / 3, 600 / 800f, 1.0, 20.0);

        buffers = GridFactory.createGrid(100, 100);
        renderTarget = new OGLRenderTarget(1024, 1024);
        viewer = new OGLTexture2D.Viewer();

        cameraLight = new Camera()
                .withPosition(new Vec3D(5, 5, 5))
                .withAzimuth(5 / 4f * Math.PI)
                .withZenith(-1 / 5f * Math.PI);

        textRenderer = new OGLTextRenderer(width, height);

        try {
            mosaicTexture = new OGLTexture2D("textures/mosaic.jpg");
            globeTexture = new OGLTexture2D("textures/globe.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void display() {
        glEnable(GL_DEPTH_TEST); // zapnout z-buffer (kvůli TextRendereru)
    glPolygonMode(GL_FRONT_AND_BACK, test);

        modifyObject();
        renderFromLight();
        renderFromViewer();


        viewer.view(renderTarget.getColorTexture(), -1, -1, 0.7);
        viewer.view(renderTarget.getDepthTexture(), -1, -0.3, 0.7);

        textRenderer.addStr2D(20, 20, "PGRF");
        textRenderer.addStr2D(20,40,"Light-Sphere = x: "+axis[0]+", y: "+axis[1]+", z: "+axis[2]);
    }

    private void modifyObject() {
       if(animateObj){
               time += timeDirection;
       }else if(!animateObj && time > 1){
           time -= timeDirection;
       }
       else{
           time = 1;
       }
    }

    private int boolToInt(boolean bool){
       // System.out.println(bool ? 1 : 0);
        return bool ? 1 : 0;
    }

    private void renderFromLight() {
        glUseProgram(shaderProgramLight);
        renderTarget.bind();

        glClearColor(0.5f, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glUniformMatrix4fv(locViewLight, false, cameraLight.getViewMatrix().floatArray());
        glUniformMatrix4fv(locProjectionLight, false, projection.floatArray());






        glUniform1f(locTimeLight, time);
        switch(showedObject){
            case 1:
                glUniform1i(locSolidLight, 1);
                buffers.draw(GL_TRIANGLES, shaderProgramLight);
                break;
            case 2:
                glUniform1i(locSolidLight, 3);
                buffers.draw(GL_TRIANGLES, shaderProgramLight);
                break;
            case 3:
                glUniform1i(locSolidLight, 4);
                buffers.draw(GL_TRIANGLES, shaderProgramLight);
                break;
            case 4:
                glUniform1i(locSolidLight, 5);
                buffers.draw(GL_TRIANGLES, shaderProgramLight);
                break;
            default:
                glUniform1i(locSolidLight, 1);
                buffers.draw(GL_TRIANGLES, shaderProgramLight);
                glUniform1i(locSolidLight, 0);
                buffers.draw(GL_TRIANGLES, shaderProgramLight);
                glUniform1i(locSolidLight, 3);
                buffers.draw(GL_TRIANGLES, shaderProgramLight);
                glUniform1i(locSolidLight, 4);
                buffers.draw(GL_TRIANGLES, shaderProgramLight);
                glUniform1i(locSolidLight, 5);
                buffers.draw(GL_TRIANGLES, shaderProgramLight);
                break;
        }
    }

    private void renderFromViewer() {

        // systém souřadnic: https://en.wikipedia.org/wiki/Spherical_coordinate_system
        glUseProgram(shaderProgramViewer);

        // výchozí framebuffer - render to obrazovky
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        // nutno opravit viewport, protože render target si nastavuje vlastní
        glViewport(0, 0, width, height);

        glClearColor(0, 0.5f, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        renderTarget.getDepthTexture().bind(shaderProgramViewer, "depthTexture", 1);
        mosaicTexture.bind(shaderProgramViewer, "mosaic", 0);

        Vec3D lightPosition = cameraLight.getPosition();
        axis[0] = (int)(lightPosition.getX() - translate[0]);
        axis[1] =  (int)(lightPosition.getY() - translate[1]);
        axis[2] =  (int)(lightPosition.getZ() - translate[2]);


//        cameraLight = cameraLight.up(0.05);
        glUniform3fv(locLightPosition, ToFloatArray.convert(cameraLight.getPosition()));
        glUniform3fv(locEyePosition, ToFloatArray.convert(camera.getEye()));

        glUniformMatrix4fv(locView, false, camera.getViewMatrix().floatArray());
        glUniformMatrix4fv(locProjection, false, projection.floatArray());
        glUniformMatrix4fv(locLightVP, false, cameraLight.getViewMatrix().mul(projection).floatArray());

        glUniform1i(glGetUniformLocation(shaderProgramViewer, "ambientInt"), boolToInt(ambientBool));
        glUniform1i(glGetUniformLocation(shaderProgramViewer, "diffuseInt"), boolToInt(diffuseBool));
        glUniform1i(glGetUniformLocation(shaderProgramViewer, "specularInt"), boolToInt(specularBool));

        glUniform1i(glGetUniformLocation(shaderProgramViewer, "textureEnabled"), boolToInt(texture));
       // System.out.println(glGetUniformLocation(shaderProgramViewer, "ambientBool"));


        switch(showedObject){
            case 1:
                glUniform1i(locSolid, 1);
                buffers.draw(GL_TRIANGLES, shaderProgramViewer);
                break;
            case 2:
                glUniform1i(locSolid, 3);
                buffers.draw(GL_TRIANGLES, shaderProgramViewer);
                break;
            case 3:
                glUniform1i(locSolid, 4);
                buffers.draw(GL_TRIANGLES, shaderProgramViewer);
                break;
            case 4:
                glUniform1i(locSolid, 5);
                buffers.draw(GL_TRIANGLES, shaderProgramViewer);
                break;
            default:
                glUniform1i(locSolid, 1);
                buffers.draw(GL_TRIANGLES, shaderProgramViewer);
                glUniform1i(locSolid, 3);
                buffers.draw(GL_TRIANGLES, shaderProgramViewer);
                glUniform1i(locSolid, 4);
                buffers.draw(GL_TRIANGLES, shaderProgramViewer);
                glUniform1i(locSolid, 5);
                buffers.draw(GL_TRIANGLES, shaderProgramViewer);
                glUniform1i(locSolid, 6);
                buffers.draw(GL_TRIANGLES, shaderProgramViewer);
                break;

        }
        glUniform1i(locSolid, 0);
        glUniform1f(locTime, time);
        buffers.draw(GL_TRIANGLES, shaderProgramViewer);
        glUniform3fv(locTransform, translate);
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
            if (mousePressed) {
                camera = camera.addAzimuth(Math.PI * (oldMx - x) / LwjglWindow.WIDTH);
                camera = camera.addZenith(Math.PI * (oldMy - y) / LwjglWindow.HEIGHT);
                oldMx = x;
                oldMy = y;
            }
        }
    };

    private final GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int button, int action, int mods) {
            if (button == GLFW_MOUSE_BUTTON_LEFT) {
                double[] xPos = new double[1];
                double[] yPos = new double[1];
                glfwGetCursorPos(window, xPos, yPos);
                oldMx = xPos[0];
                oldMy = yPos[0];
                mousePressed = (action == GLFW_PRESS);
            }
        }
    };

    private final GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (action == GLFW_PRESS || action == GLFW_REPEAT) {
                switch (key) {
                    case GLFW_KEY_A:
                        if(cameraMove){
                            camera = camera.left(0.1);
                        }else{
                            cameraLight = cameraLight.left(0.1);
                        }

                        break;
                    case GLFW_KEY_D:
                        if(cameraMove){
                            camera = camera.right(0.1);
                        }else{
                            cameraLight = cameraLight.right(0.1);
                        }
                        break;
                    case GLFW_KEY_W:
                        if(cameraMove){
                            camera = camera.forward(0.1);
                        }else{
                            cameraLight = cameraLight.forward(0.1);
                        }
                        break;
                    case GLFW_KEY_S:
                        if(cameraMove){
                            camera = camera.backward(0.1);
                        }else{
                            cameraLight = cameraLight.backward(0.1);
                        }
                        break;
                    case GLFW_KEY_R:
                        if(cameraMove){
                            camera = camera.up(0.1);
                        }else{
                            cameraLight = cameraLight.up(0.1);
                        }
                        break;
                    case GLFW_KEY_F:
                        if(cameraMove){
                            camera = camera.down(0.1);
                        }else{
                            cameraLight = cameraLight.down(0.1);
                        };
                        break;
                    case GLFW_KEY_LEFT_SHIFT:
                        cameraMove = !cameraMove;
                        break;
                    case GLFW_KEY_T:
                        if(test == VIEW_TYPES.get(0)){
                            test = VIEW_TYPES.get(1);
                        }
                        else if(test == VIEW_TYPES.get(1)){
                            test = VIEW_TYPES.get(2);
                        }
                        else{
                            test = VIEW_TYPES.get(0);
                        }
                        break;
                    case GLFW_KEY_LEFT:
                        translate[0] += translateSpeed;
                        break;

                    case GLFW_KEY_RIGHT:
                        translate[0] -= translateSpeed;
                        break;
                    case GLFW_KEY_UP:
                        translate[1] -= translateSpeed;
                        break;
                    case GLFW_KEY_DOWN:
                            translate[1] += translateSpeed;
                        break;
                    case GLFW_KEY_KP_ADD:
                        translate[2] += translateSpeed;
                        break;
                    case GLFW_KEY_KP_SUBTRACT:
                        translate[2] -= translateSpeed;
                        break;
                    case GLFW_KEY_P:
                        projection =  perspective ? new Mat4OrthoRH(width/70, height/50, 1.0, 20.0) : new Mat4PerspRH(Math.PI / 3, 600 / 800f, 1.0, 20.0);
                        perspective = !perspective;
                        break;
                    case GLFW_KEY_M:
                        //showedObject++;
                        if(showedObject==4){
                            showedObject=0;
                        }else{
                            showedObject++;
                        }
                        break;
                    case GLFW_KEY_X:
                        if(animateObj){
                            timeDirection = 0.05f;
                            animateObj = !animateObj;
                        }else{
                            animateObj = !animateObj;
                            timeDirection = 0.01f;
                        }
                        break;
                    case GLFW_KEY_F1:
                        ambientBool = !ambientBool;
                        break;
                    case GLFW_KEY_F2:
                        diffuseBool = !diffuseBool;
                        break;
                    case GLFW_KEY_F3:
                        specularBool = !specularBool;
                        break;
                    case GLFW_KEY_C:
                        texture = !texture;
                        break;
                }


            }
        }
    };
}
