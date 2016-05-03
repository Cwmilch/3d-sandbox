package engineTests;

import engine.*;
import entities.Camera;
import entities.Entity;
import entities.LightSource;
import models.TexturedModel;
import models.RawModel;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import shaders.StaticShader;
import textures.ModelTexture;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GameLoopMain {

    private static DisplayManager m;

    public static void main(String[] args){
        m = new DisplayManager();
        m.init();
        GL.createCapabilities();
        long window = m.getWindow();

        ModelLoader loader = new ModelLoader();

        RawModel model = OBJLoader.loadOBJModel("sphere", loader);
        ModelTexture blank = new ModelTexture(loader.loadTexture("res/water.png"));

        TexturedModel texturedModel = new TexturedModel(model, blank);
        /*ModelTexture texture = texturedModel.getTexture();
        texture.setShineDamper(15.0f);
        texture.setReflectivity(5.0f);*/


        Entity entity = new Entity(texturedModel, new Vector3f(0, -3, -30), 0, 0, 0, 1);
        LightSource light = new LightSource(new Vector3f(0, 0, -25), new Vector3f(1, 1, 1));

        Camera camera = new Camera();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        MasterRenderer renderer = new MasterRenderer();
        //Render until user has attempted to close window or press escape key
        while(!glfwWindowShouldClose(m.getWindow())){
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); //clear framebuffer
            entity.increaseRotation(0, 1, 0);
            camera.move();
            renderer.processEntity(entity); //has to be called for all entities
            renderer.render(light, camera);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        renderer.cleanUp();
        loader.wipeLists();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static long getWindow(){
        return m.getWindow();
    }

    public static DisplayManager getDisplayManager(){
        return m;
    }
}
