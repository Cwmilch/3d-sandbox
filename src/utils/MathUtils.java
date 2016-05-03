package utils;

import entities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by Carter Milch on 4/24/2016.
 */
public class MathUtils {

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rX, float rY, float rZ, float scale){
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(translation);
        matrix.rotate((float)Math.toRadians(rX), new Vector3f(1, 0, 0));
        matrix.rotate((float)Math.toRadians(rY), new Vector3f(0, 1, 0));
        matrix.rotate((float)Math.toRadians(rZ), new Vector3f(0, 0, 1));
        matrix.scale(new Vector3f(scale, scale, scale));
        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera){
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix);
        viewMatrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix);
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCamPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        viewMatrix.translate(negativeCamPos);
        return viewMatrix;
    }
}
