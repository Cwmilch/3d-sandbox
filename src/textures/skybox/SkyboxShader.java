package textures.skybox;

import entities.Camera;

import org.joml.Matrix4f;
import shaders.AbstractShader;
import utils.MathUtils;

public class SkyboxShader extends AbstractShader{

    private static final String VERTEX_FILE = "src/textures/skybox/skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/textures/skybox/skyboxFragmentShader.txt";

    private int location_projectionMatrix;
    private int location_viewMatrix;

    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.setMatrixValue(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = MathUtils.createViewMatrix(camera);
        matrix.m30 = matrix.m31 = matrix.m32 = 0;
        super.setMatrixValue(location_viewMatrix, matrix);
    }

    @Override
    protected void getAllUniVariableLocations() {
        location_projectionMatrix = super.getUniVariableLocation("projectionMatrix");
        location_viewMatrix = super.getUniVariableLocation("viewMatrix");
    }

    @Override
    protected void bindAttribute() {
        super.bindAttribute(0, "position");
    }
}