package entities;

import org.joml.Vector3f;

import java.awt.*;

/**
 * Created by Carter Milch on 5/2/2016.
 */
public class LightSource {

    private Vector3f position;
    private Vector3f color;
    private Vector3f attenuation = new Vector3f(1.0f, 0.0f, 0.0f);

    public LightSource(Vector3f position, Vector3f color) {
        this.color = color;
        this.position = position;
    }

    public LightSource(Vector3f position, Vector3f color, Vector3f attenuation) {
        this.position = position;
        this.color = color;
        this.attenuation = attenuation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getColor() {
        return color;
    }

    public Color getRGBColor(){
        return new Color(color.x * 100, color.y * 100, color.z * 100);
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }
}
