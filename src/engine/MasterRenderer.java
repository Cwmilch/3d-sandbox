package engine;

import entities.Camera;
import entities.Entity;
import entities.LightSource;
import gui.Main;
import gui.Shape;
import gui.Texture;
import models.RawModel;
import models.TexturedModel;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import shaders.StaticShader;
import terrain.Terrain;
import textures.ModelTexture;
import textures.skybox.SkyboxRenderer;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.opengl.GL11.*;
import static engine.EngineMain.*;

/**
 * Created by Carter Milch on 5/2/2016.
 */
@SuppressWarnings("Duplicates")
public class MasterRenderer {

    public static final float FIELD_OF_VIEW = 80.0f;
    private static final float NEAR_PLANE = 0.1F;
    private static final float FAR_PLANE = 1000f;

    public static final float RED = 0.3f;
    public static final float GREEN = 0.3f;
    public static final float BLUE = 0.37f;

    private static HashMap<String, TexturedModel> modelMap = new HashMap<>();

    private Matrix4f projMatrix;

    private StaticShader shader = new StaticShader(false);
    private EntityRenderer renderer;

    private TerrainRenderer terrainRenderer;
    private StaticShader terrainShader = new StaticShader(true);

    private HashMap<TexturedModel, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();

    private SkyboxRenderer skyboxRenderer;

    public MasterRenderer(ModelLoader loader){
        setModelMap();
        enableCulling();
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projMatrix);
    }

    public static void enableCulling(){
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public static void disableCulling(){
        glDisable(GL_CULL_FACE);
    }

    public void prepareFrame(){
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
        glClearColor(RED, GREEN, BLUE, 1);
    }

    public void render(List<LightSource> lights, Camera camera){
        prepareFrame();
        shader.start();
        shader.loadSkyColor(RED, GREEN, BLUE);
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();
        terrainShader.start();
        terrainShader.loadSkyColor(RED, GREEN, BLUE);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        skyboxRenderer.render(camera);
        terrains.clear();
        entities.clear();
    }

    public void processTerrain(Terrain terrain){
        terrains.add(terrain);
    }

    public void processEntity(Entity entity){
        TexturedModel entityModel = entity.getModel();
        List<Entity> entityList = entities.get(entityModel);
        if(entityList != null){
            entityList.add(entity);
        }else{
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entities.put(entityModel, newEntityList);
        }
    }

    private void createProjectionMatrix(){
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        long window = getWindow();
        glfwGetWindowSize(window, w, h);
        int width = w.get(0);
        int height = h.get(0);

        float aspectRatio = (float) width / (float) height;
        float yScale = (float) (1.0f / Math.tan(Math.toRadians(FIELD_OF_VIEW / 2.0f))) * aspectRatio;
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        projMatrix = new Matrix4f();
        projMatrix.m00 = xScale;
        projMatrix.m11 = yScale;
        projMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength);
        projMatrix.m23 = -1;
        projMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
        projMatrix.m33 = 0;
    }

    public void cleanShaders(){
        shader.clearShaderCache();
        terrainShader.clearShaderCache();
    }

    private static TexturedModel makeTexturedModel(String model, String texture){
        ModelLoader m = Main.getLoader();
        RawModel rawModel = OBJLoader.loadOBJModel(model, m);
        ModelTexture modelTexture = new ModelTexture(m.loadTexture(texture));
        return new TexturedModel(rawModel, modelTexture);
    }

    public static TexturedModel getModel(Shape shape, Texture texture){
        String s = shape.toString().toUpperCase();
        System.out.println(s);
        String t = texture.toString().toUpperCase();
        String var = s + "_" + t;
        return modelMap.get(var);
    }

    private void setModelMap(){
        modelMap.put("CUBE_STONE", makeTexturedModel("cube", "stone"));
        modelMap.put("CUBE_WATER", makeTexturedModel("cube", "water"));
        modelMap.put("CUBE_BRICK", makeTexturedModel("cube", "brick"));
        modelMap.put("CUBE_GRASS", makeTexturedModel("cube", "grass"));

        modelMap.put("SPHERE_STONE", makeTexturedModel("sphere", "stone"));
        modelMap.put("SPHERE_WATER", makeTexturedModel("sphere", "water"));
        modelMap.put("SPHERE_BRICK", makeTexturedModel("sphere", "brick"));
        modelMap.put("SPHERE_GRASS", makeTexturedModel("sphere", "grass"));

        modelMap.put("CYLINDER_STONE", makeTexturedModel("cylinder", "stone"));
        modelMap.put("CYLINDER_WATER", makeTexturedModel("cylinder", "water"));
        modelMap.put("CYLINDER_BRICK", makeTexturedModel("cylinder", "brick"));
        modelMap.put("CYLINDER_GRASS", makeTexturedModel("cylinder", "grass"));

        modelMap.put("CONE_STONE", makeTexturedModel("cone", "stone"));
        modelMap.put("CONE_WATER", makeTexturedModel("cone", "water"));
        modelMap.put("CONE_BRICK", makeTexturedModel("cone", "brick"));
        modelMap.put("CONE_GRASS", makeTexturedModel("cone", "grass"));
    }
}
