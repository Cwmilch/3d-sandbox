package shaders;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

/**
 * Created by Carter Milch on 4/23/2016.
 */
public abstract class AbstractShader {

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public AbstractShader(String vertexFile, String fragmentFile){
        vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttribute();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniVariableLocations();
    }

    protected abstract void getAllUniVariableLocations();

    protected int getUniVariableLocation(String varName){
        return GL20.glGetUniformLocation(programID, varName);
    }

    public void start(){
        GL20.glUseProgram(programID);
    }

    public void stop(){
        GL20.glUseProgram(0);
    }

    public void clearShaderCache(){
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    protected abstract void bindAttribute();

    protected void bindAttribute(int attribute, String variableName){
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    protected void setIntValue(int location, int value){
        GL20.glUniform1i(location, value);
    }

    protected void setFloatValue(int location, float value){
        GL20.glUniform1f(location, value);
    }

    protected void setVectorValue(int location, Vector3f vector){
        GL20.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void setVectorValue(int location, Vector4f vector){
        GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
    }

    protected void setBooleanValue(int location, boolean value){
        GL20.glUniform1f(location, value ? 1.0f : 0.0f);
    }

    protected void setMatrixValue(int location, Matrix4f matrix){
        matrix.get(matrixBuffer);
        GL20.glUniformMatrix4fv(location, false, matrixBuffer);
    }

    public void set2DVectorValue(int location, Vector2f value) {
        GL20.glUniform2f(location, value.x, value.y);
    }

    private static int loadShader(String file, int type){
        StringBuilder shaderSource = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null){
                shaderSource.append(line).append("\n");
            }
            br.close();
        } catch (IOException e) {
            System.err.println("Unable to read GLSL shader file.");
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader.");
        }
        return shaderID;
    }
}
