package textures;

import engine.ModelLoader;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.nio.ByteBuffer;

/**
 * Created by Carter Milch on 4/24/2016.
 */
public class ModelTexture {

    private PNGDecoder textureDecoder;

    public ModelTexture(PNGDecoder decoder){
        this.textureDecoder = decoder;
    }

    public PNGDecoder getTextureDecoder() {
        return textureDecoder;
    }

    public ByteBuffer getTextureBuffer(){
        return ModelLoader.getDecoderBuffer(textureDecoder);
    }
}
