package entities;

import models.TexturedModel;
import org.joml.Vector3f;

/**
 * Created by Carter Milch on 4/24/2016.
 */
public class Entity {

    private TexturedModel model;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;

    public Entity(TexturedModel m, Vector3f pos, float rX, float rY, float rZ, float s){
        model = m;
        position = pos;
        rotX = rX;
        rotY = rY;
        rotZ = rZ;
        scale = s;
    }

    public TexturedModel getModel() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotX() {
        return rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public float getScale() {
        return scale;
    }
}
