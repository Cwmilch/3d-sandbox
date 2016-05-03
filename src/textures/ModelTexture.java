package textures;

import engine.ModelLoader;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.nio.ByteBuffer;

/**
 * Created by Carter Milch on 4/24/2016.
 */
public class ModelTexture {

    private PNGDecoder textureDecoder;

    private float shineDamper = 1.0f;

    private float reflectivity = 0.0f;

    public ModelTexture(PNGDecoder decoder){
        this.textureDecoder = decoder;
    }

    public PNGDecoder getTextureDecoder() {
        return textureDecoder;
    }

    public ByteBuffer getTextureBuffer(){
        return ModelLoader.getDecoderBuffer(textureDecoder);
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

}
