package engine;

import entities.Camera;
import entities.LightSource;
import gui.GridFrame;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import terrain.Terrain;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class EngineMain {

    private static DisplayManager m;

    public static void init(){
        m = new DisplayManager();
        m.init();
        GL.createCapabilities();
        long window = m.getWindow();

        ModelLoader loader = new ModelLoader();

        List<LightSource> lights = new ArrayList<>();
        LightSource sun = new LightSource(new Vector3f(0, 40, 5), new Vector3f(0.75f, 0.65f, 0.65f));
        lights.add(sun);
        lights.addAll(GridFrame.getLights());
        Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("terrainStone")));
        Terrain terrain1 = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("terrainStone")));

        Camera camera = new Camera();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        MasterRenderer renderer = new MasterRenderer(loader);
        GridFrame.frames.forEach(GridFrame::processInput);
        //Render until user has attempted to close window or press escape key
        while(!glfwWindowShouldClose(m.getWindow())){
            DisplayManager.update();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); //clear framebuffer
            camera.move();

            //TODO render all in Master method
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain1);
            //has to be called for all entities
            GridFrame.getEntities().forEach(renderer::processEntity);

            renderer.render(lights, camera);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        renderer.cleanShaders();
        loader.wipeLists();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        System.exit(0);
    }

    public static long getWindow(){
        return m.getWindow();
    }
}
