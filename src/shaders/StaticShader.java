package shaders;

import entities.Camera;
import entities.LightSource;
import org.joml.Matrix4f;
import utils.MathUtils;

/**
 * Created by Carter Milch on 4/23/2016.
 */
public class StaticShader extends AbstractShader{

    private static final String VERTEX_FILE = "src/shaders/VertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/FragmentShader.txt";

    private int transMatrixLocation;
    private int projMatrixLocation;
    private int viewMatrixLocation;
    private int lightPosLocation;
    private int lightColorLocation;
    private int shineDamperLocation;
    private int reflectivityLocation;


    public StaticShader(){
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttribute(){
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniVariableLocations(){
        transMatrixLocation = super.getUniVariableLocation("transformationMatrix");
        projMatrixLocation = super.getUniVariableLocation("projectionMatrix");
        viewMatrixLocation = super.getUniVariableLocation("viewMatrix");
        lightPosLocation = super.getUniVariableLocation("lightPos");
        lightColorLocation = super.getUniVariableLocation("lightColor");
        shineDamperLocation = super.getUniVariableLocation("shineDamper");
        reflectivityLocation = super.getUniVariableLocation("reflectivity");
    }

    public void loadShineVariables(float damper, float reflectivity){
        super.setFloatValue(shineDamperLocation, damper);
        super.setFloatValue(reflectivityLocation, reflectivity);
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.setMatrixValue(transMatrixLocation, matrix);
    }

    public void loadLight(LightSource lightSource){
        super.setVectorValue(lightPosLocation, lightSource.getPosition());
        super.setVectorValue(lightColorLocation, lightSource.getColor());
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = MathUtils.createViewMatrix(camera);
        super.setMatrixValue(viewMatrixLocation, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.setMatrixValue(projMatrixLocation, matrix);
    }
}
