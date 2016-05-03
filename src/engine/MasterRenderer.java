package engine;

import engineTests.GameLoopMain;
import entities.Camera;
import entities.Entity;
import entities.LightSource;
import models.TexturedModel;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrain.Terrain;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Carter Milch on 5/2/2016.
 */
public class MasterRenderer {

    private static final float FIELD_OF_VIEW = 80.0f;
    private static final float NEAR_PLANE = 0.1F;
    private static final float FAR_PLANE = 1000f;

    private static final float RED = 0.5f;
    private static final float GREEN = 0.5f;
    private static final float BLUE = 1.0f;

    private Matrix4f projMatrix;

    private StaticShader shader = new StaticShader();
    private EntityRenderer renderer;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();

    private HashMap<TexturedModel, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();

    public MasterRenderer(){
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projMatrix);
    }

    public void prepareFrame(){
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
        glClearColor(RED, GREEN, BLUE, 1);
    }

    public void render(LightSource light, Camera camera){
        prepareFrame();
        shader.start();
        shader.loadSkyColor(RED, GREEN, BLUE);
        shader.loadLight(light);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();
        terrainShader.start();
        terrainShader.loadSkyColor(RED, GREEN, BLUE);
        terrainShader.loadLight(light);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        terrains.clear();
        entities.clear();
    }

    public void processTerrains(Terrain terrain){
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
        long window = GameLoopMain.getWindow();
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
}
