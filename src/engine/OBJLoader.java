package engine;

import models.RawModel;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carter Milch on 4/29/2016.
 */
public class OBJLoader {

    public static RawModel loadOBJModel(String fileName, ModelLoader loader) {
        FileReader fr = null;
        try {
            fr = new FileReader(new File("res/" + fileName + ".obj"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert fr != null;
        BufferedReader br = new BufferedReader(fr);
        String line;

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textureCoords = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        float[] textureArray = null;
        float[] normalsArray = null;
        float[] verticesArray;
        int[] indicesArray;

        try {
            while (true) {
                line = br.readLine();
                String[] current = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(toFloat(current[1]), toFloat(current[2]), toFloat(current[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(toFloat(current[1]), toFloat(current[2]));
                    textureCoords.add(texture);
                } else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(toFloat(current[1]), toFloat(current[2]), toFloat(current[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    textureArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }

            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = br.readLine();
                    continue;
                }
                String[] current = line.split(" ");
                String[] vertexA = current[1].split("/");
                String[] vertexB = current[2].split("/");
                String[] vertexC = current[3].split("/");

                processVertex(vertexA, indices, textureCoords, normals, textureArray, normalsArray);
                processVertex(vertexB, indices, textureCoords, normals, textureArray, normalsArray);
                processVertex(vertexC, indices, textureCoords, normals, textureArray, normalsArray);
                line = br.readLine();
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size() * 3];

        int vertexIndex = 0;
        for(Vector3f vertex : vertices){
            verticesArray[vertexIndex++] = vertex.x;
            verticesArray[vertexIndex++] = vertex.y;
            verticesArray[vertexIndex++] = vertex.z;
        }

        indicesArray = indices.stream().mapToInt(Integer::new).toArray();
        return loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);
    }

    private static void processVertex(String[] data, List<Integer> indices, List<Vector2f> textureCoords,
                                      List<Vector3f> normals, float[] textureArray, float[] normalArray) {
        int currentVertex = Integer.parseInt(data[0]) - 1;
        indices.add(currentVertex);
        Vector2f currentTex = textureCoords.get(Integer.parseInt(data[1]) - 1);
        textureArray[currentVertex * 2] = currentTex.x;
        textureArray[currentVertex * 2 + 1] = 1 - currentTex.y;
        Vector3f currentNorm = normals.get(Integer.parseInt(data[2]) - 1);
        normalArray[currentVertex * 3] = currentNorm.x;
        normalArray[currentVertex * 3 + 1] = currentNorm.y;
        normalArray[currentVertex * 3 + 2] = currentNorm.z;
    }

    private static float toFloat(String s) {
        return Float.parseFloat(s);
    }
}
