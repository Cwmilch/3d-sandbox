package engineTests;

import engine.DisplayManager;
import engine.ModelLoader;
import engine.RawModel;
import engine.Renderer;
import org.lwjgl.opengl.GL;
import shaders.StaticShader;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GameLoopMain {

    public static void main(String[] args){
        Renderer renderer = new Renderer();

        DisplayManager m = new DisplayManager();
        m.init();
        GL.createCapabilities();
        long window = m.getWindow();

        ModelLoader loader = new ModelLoader();
        StaticShader shader = new StaticShader();


        float[] vertices = {
                -0.5f, 0.5f, 0f,    //v0
                -0.5f, -0.5f, 0f,   //v1
                0.5f, -0.5f, 0f,    //v2
                0.5f, 0.5f, 0f,    //v3
        };

        int[] indices = {
                0, 1, 3, //Top left triangle (v0, v1, v3)
                3, 1, 2  // Bottom right triangle
        };

        RawModel model = loader.loadToVAO(vertices, indices);

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        //Render until user has attempted to close window or press escape key
        while(!glfwWindowShouldClose(m.getWindow())){
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); //clear framebuffer

            renderer.prepareFrame();
            shader.start();
            renderer.render(model);
            shader.stop();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        shader.clearShaderCache();
        loader.wipeLists();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
