package gui;

/**
 * Created by Carter Milch on 5/9/2016.
 */
public enum Texture {
    BLANK("White"),
    STONE("Stone"),
    WATER("Water"),
    BRICK("Brick"),
    GRASS("Grass");

    private String name;

    Texture(String name){
        this.name = name;
    }

    public static Texture getTexture(int i){
        switch(i){
            case 1:
                return STONE;
            case 2:
                return WATER;
            case 3:
                return BRICK;
            case 4:
                return GRASS;
        }
        return BLANK;
    }

    private String getName(){
        return name;
    }

    public static String getName(int i){
        return getTexture(i).getName();
    }

    public static int getID(Texture t){
        switch(t){
            case STONE:
                return 1;
            case WATER:
                return 2;
            case BRICK:
                return 3;
            case GRASS:
                return 4;
        }
        return 0;
    }
}
