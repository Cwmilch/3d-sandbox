package gui;

/**
 * Created by Carter Milch on 5/9/2016.
 */
public enum Texture {
    BLANK("White", 0),
    STONE("Stone", 1),
    WATER("Water", 2),
    BRICK("Brick", 3),
    GRASS("Grass", 4);

    private String name;
    private int id;

    Texture(String name, int id){
        this.name = name;
        this.id = id;
    }

    public static Texture getTexture(int i){
        switch(i){
            case 0:
                return BLANK;
            case 1:
                return STONE;
            case 2:
                return WATER;
            case 3:
                return BRICK;
            case 4:
                return GRASS;
        }
        return null;
    }

    private String getName(){
        return name;
    }
    public int getID(){
        return id;
    }

    public static String getName(int i){
        Texture t = getTexture(i);
        assert t != null;
        return t.getName();
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
