package shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Carter Milch on 4/23/2016.
 */
public abstract class AbstractShader {

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

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
