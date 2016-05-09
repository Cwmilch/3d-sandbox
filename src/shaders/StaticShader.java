package shaders;

import entities.Camera;
import entities.LightSource;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import utils.MathUtils;

import java.util.List;

/**
 * Created by Carter Milch on 4/23/2016.
 */
@SuppressWarnings("Duplicates")
public class StaticShader extends AbstractShader{

    private static final int MAX_LIGHTS = 6;

    private static final String VERTEX_FILE = "src/shaders/VertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/FragmentShader.txt";

    private int transMatrixLocation;
    private int projMatrixLocation;
    private int viewMatrixLocation;
    private int shineDamperLocation;
    private int reflectivityLocation;
    private int useFakeLightingLocation;
    private int skyColorLocation;
    private int[] lightPosLocations;
    private int[] lightColorLocations;
    private int[] attenuationLocations;


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
        shineDamperLocation = super.getUniVariableLocation("shineDamper");
        reflectivityLocation = super.getUniVariableLocation("reflectivity");
        useFakeLightingLocation = super.getUniVariableLocation("useFakeLighting");
        skyColorLocation = super.getUniVariableLocation("skyColor");

        lightPosLocations = new int[MAX_LIGHTS];
        lightColorLocations = new int[MAX_LIGHTS];
        attenuationLocations = new int[MAX_LIGHTS];
        for(int i = 0; i < MAX_LIGHTS; i++){
            lightPosLocations[i] = super.getUniVariableLocation("lightPos[" + i + "]");
            lightColorLocations[i] = super.getUniVariableLocation("lightColor[" + i + "]");
            attenuationLocations[i] = super.getUniVariableLocation("attenuation[" + i + "]");
        }
    }

    public void loadFakeLighting(boolean fake){
        super.setBooleanValue(useFakeLightingLocation, fake);
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

    public void loadLights(List<LightSource> sources){
        for(int i = 0; i < MAX_LIGHTS; i++){
            if(i < sources.size()){
                super.setVectorValue(lightPosLocations[i], sources.get(i).getPosition());
                super.setVectorValue(lightColorLocations[i], sources.get(i).getColor());
                super.setVectorValue(attenuationLocations[i], sources.get(i).getAttenuation());
            }else{
                super.setVectorValue(lightPosLocations[i], new Vector3f(0, 0, 0));
                super.setVectorValue(lightColorLocations[i], new Vector3f(0, 0, 0));
                super.setVectorValue(attenuationLocations[i], new Vector3f(1, 0, 0));
            }
        }
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = MathUtils.createViewMatrix(camera);
        super.setMatrixValue(viewMatrixLocation, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.setMatrixValue(projMatrixLocation, matrix);
    }
}
