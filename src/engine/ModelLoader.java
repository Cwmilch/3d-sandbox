package engine;

import models.RawModel;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static de.matthiasmann.twl.utils.PNGDecoder.Format;

/**
 * Created by Carter Milch on 4/23/2016.
 */
public class ModelLoader {

    private List<Integer> vaoList = new ArrayList<>();
    private List<Integer> vboList = new ArrayList<>();

    private List<PNGDecoder> decoders = new ArrayList<>();
    private static HashMap<PNGDecoder, ByteBuffer> buffers = new HashMap<>();

    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices){
        int vaoID = createVAO();
        bindIndexBuffer(indices);
        storeVBO(0, 3, positions);
        storeVBO(1, 2, textureCoords);
        storeVBO(2, 3, normals);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    public PNGDecoder loadTexture(String fileName){
        try {
            InputStream in = new FileInputStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);

            ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
            buffer.flip();

            decoders.add(decoder);
            buffers.put(decoder, buffer);
            return decoder;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void wipeLists(){
        vaoList.forEach(GL30::glDeleteVertexArrays);
        vboList.forEach(GL15::glDeleteBuffers);
        decoders.clear();
    }

    private int createVAO(){
        int vaoID = GL30.glGenVertexArrays();
        vaoList.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeVBO(int attributeNumber, int valueSize, float[] data){
        int vboID = GL15.glGenBuffers();
        vboList.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeAsFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, valueSize, GL11.GL_FLOAT, false, 0, 0);
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

    public static ByteBuffer getDecoderBuffer(PNGDecoder decoder){
        return buffers.get(decoder);
    }
}
