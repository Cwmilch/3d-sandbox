package engine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carter Milch on 4/23/2016.
 */
public class ModelLoader {

    private List<Integer> vaoList = new ArrayList<>();
    private List<Integer> vboList = new ArrayList<>();

    public RawModel loadToVAO(float[] positions, int[] indices){
        int vaoID = createVAO();
        bindIndexBuffer(indices);
        storeVBO(0, positions);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    public void wipeLists(){
        vaoList.forEach(GL30::glDeleteVertexArrays);
        vboList.forEach(GL15::glDeleteBuffers);
    }

    private int createVAO(){
        int vaoID = GL30.glGenVertexArrays();
        vaoList.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeVBO(int attributeNumber, float[] data){
        int vboID = GL15.glGenBuffers();
        vboList.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeAsFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); //unbind current VBO
    }

    private void unbindVAO(){
        GL30.glBindVertexArray(0);
    }

    private void bindIndexBuffer(int[] indices){
        int vboID = GL15.glGenBuffers();
        vboList.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeAsIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeAsIntBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeAsFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip(); //Switches buffer mode from write to read
        return buffer;
    }
}
