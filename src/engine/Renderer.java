package engine;

import com.sun.prism.Texture;
import engineTests.GameLoopMain;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shaders.StaticShader;
import textures.ModelTexture;
import utils.MathUtils;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Carter Milch on 4/23/2016.
 */
public class Renderer {

    private static final float FIELD_OF_VIEW = 80.0f;
    private static final float NEAR_PLANE = 0.1F;
    private static final float FAR_PLANE = 1000f;


    private Matrix4f projMatrix;
    private StaticShader shader;

    public Renderer(StaticShader shader){
        this.shader = shader;
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projMatrix);
        shader.stop();
    }

    public void prepareFrame(){
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
        glClearColor(0, 0, 0, 1);
    }

    public void render(HashMap<TexturedModel, List<Entity>> entities){
        for(TexturedModel model : entities.keySet()){
            prepareTexturedModel(model);
            List<Entity> entityList = entities.get(model);
            for(Entity entity : entityList){
                prepareEntity(entity);
                glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(TexturedModel texturedModel){
        PNGDecoder decoder = texturedModel.getTexture().getTextureDecoder();
        RawModel model = texturedModel.getRawModel();

        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        ModelTexture texture = texturedModel.getTexture();
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA,
                GL_UNSIGNED_BYTE, texturedModel.getTexture().getTextureBuffer());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

    }

    private void unbindTexturedModel(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareEntity(Entity entity){
        Matrix4f transformation = MathUtils.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformation);
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
}
