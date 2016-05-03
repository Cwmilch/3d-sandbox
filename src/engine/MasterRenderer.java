package engine;

import entities.Camera;
import entities.Entity;
import entities.LightSource;
import models.TexturedModel;
import shaders.StaticShader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Carter Milch on 5/2/2016.
 */
public class MasterRenderer {

    private StaticShader shader = new StaticShader();
    private Renderer renderer = new Renderer(shader);

    private HashMap<TexturedModel, List<Entity>> entities = new HashMap<>();

    public void render(LightSource light, Camera camera){
        renderer.prepareFrame();
        shader.start();
        shader.loadLight(light);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();
        entities.clear();
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

    public void cleanUp(){
        shader.clearShaderCache();
    }
}
