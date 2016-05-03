package shaders;

import entities.Camera;
import entities.LightSource;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import utils.MathUtils;

/**
 * Created by Carter Milch on 5/2/2016.
 */
@SuppressWarnings("Duplicates")
public class TerrainShader extends AbstractShader{

    private static final String VERTEX_FILE = "src/shaders/TerrainVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/TerrainFragmentShader.txt";

    private int transMatrixLocation;
    private int projMatrixLocation;
    private int viewMatrixLocation;
    private int lightPosLocation;
    private int lightColorLocation;
    private int shineDamperLocation;
    private int reflectivityLocation;
    private int skyColorLocation;


    public TerrainShader(){
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
        skyColorLocation = super.getUniVariableLocation("skyColor");
    }

    public void loadSkyColor(float r, float g, float b){
        super.setVectorValue(skyColorLocation, new Vector3f(r, g, b));
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
