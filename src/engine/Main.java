package engine;

import engine.*;
import entities.Camera;
import entities.Entity;
import entities.LightSource;
import models.TexturedModel;
import models.RawModel;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import terrain.Terrain;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {

    private static DisplayManager m;

    public static void main(String[] args){
        m = new DisplayManager();
        m.init();
        GL.createCapabilities();
        long window = m.getWindow();

        ModelLoader loader = new ModelLoader();

        RawModel mainRaw = OBJLoader.loadOBJModel("cylinder", loader);
        RawModel lamp = OBJLoader.loadOBJModel("lamp", loader);

        ModelTexture blank = new ModelTexture(loader.loadTexture("res/stone.png"));
        ModelTexture lampTexture = new ModelTexture(loader.loadTexture("res/lamp.png"));

        TexturedModel texturedModel = new TexturedModel(mainRaw, blank);

        List<Entity> entities = new ArrayList<>();

        Entity main = new Entity(texturedModel, new Vector3f(10, 2, -30), 0, 0, 0, 1);
        Entity lampA = new Entity(new TexturedModel(lamp, lampTexture), new Vector3f(5, 0, -30), 0, 0, 0, 1);
        lampA.getModel().getTexture().setUseFakeLighting(true);
        entities.add(main);
        entities.add(lampA);


        List<LightSource> lights = new ArrayList<>();
        LightSource sun = new LightSource(new Vector3f(0, 40, 5), new Vector3f(0.75f, 0.65f, 0.65f));
        LightSource light = new LightSource(new Vector3f(5, 14, -30), new Vector3f(0, 1, 0));
        lights.add(sun);
        lights.add(light);

        Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("res/terrainStone.png")));
        Terrain terrain1 = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("res/terrainStone.png")));

        Camera camera = new Camera();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        MasterRenderer renderer = new MasterRenderer(loader);
        //Render until user has attempted to close window or press escape key
        while(!glfwWindowShouldClose(m.getWindow())){
            DisplayManager.update();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); //clear framebuffer
            //main.increaseRotation(0, 1, 0);
            camera.move();

            //TODO render all in Master method
            renderer.processTerrains(terrain);
            renderer.processTerrains(terrain1);
            //has to be called for all entities
            entities.forEach(renderer::processEntity);

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
    }

    public static long getWindow(){
        return m.getWindow();
    }

    public static DisplayManager getDisplayManager(){
        return m;
    }
}
