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

    public void increasePosition(float dx, float dy, float dz){
        position.x += dx;
        position.y += dy;
        position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz){
        rotX += dx;
        rotY += dy;
        rotZ += dz;
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getTextureXOffset(){
        return 0;
    }

    public float getTextureYOffset(){
        return 0;
    }
}
