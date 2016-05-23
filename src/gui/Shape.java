package gui;

/**
 * Created by Carter Milch on 5/9/2016.
 */
public enum Shape {
    AIR("Air"),
    CUBE("Cube"),
    SPHERE("Sphere"),
    CYLINDER("Cylinder"),
    CONE("Cone"),
    LIGHT("Light");

    private String name;

    Shape(String name) {
        this.name = name;
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

    public static int getID(Shape s){
        switch(s){
            case CUBE:
                return 1;
            case SPHERE:
                return 2;
            case CYLINDER:
                return 3;
            case CONE:
                return 4;
            case LIGHT:
                return 5;
        }
        return 0;
    }

    @Override
    public String toString(){
        return getName();
    }
}
