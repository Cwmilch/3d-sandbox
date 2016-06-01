package gui;

/**
 * Created by Carter Milch on 5/9/2016.
 */
public enum Shape {
    AIR("Air", 0),
    CUBE("Cube", 1),
    SPHERE("Sphere", 2),
    CYLINDER("Cylinder", 3),
    CONE("Cone", 4),
    LIGHT("Light", 5);

    private String name;
    private int id;

    Shape(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static Shape getShape(int i){
        switch(i){
            case 1:
                return CUBE;
            case 2:
                return SPHERE;
            case 3:
                return CYLINDER;
            case 4:
                return CONE;
            case 5:
                return LIGHT;
        }
        return AIR;
    }

    private String getName(){
        return name;
    }

    public static String getName(int i){
        return getShape(i).getName();
    }

    public int getID(){
        return id;
    }

    @Override
    public String toString(){
        return getName();
    }
}
