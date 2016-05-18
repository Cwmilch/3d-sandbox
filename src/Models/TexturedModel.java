package models;

import textures.ModelTexture;

/**
 * Created by Carter Milch on 4/24/2016.
 */
public class TexturedModel {

    private RawModel rawModel;
    private ModelTexture texture;

    private int height = 0;
    private int width = 0;

    public TexturedModel(RawModel model, ModelTexture texture){
        rawModel = model;
        this.texture = texture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public ModelTexture getTexture() {
        return texture;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void setWidth(int width){
        this.width = width;
    }
}
