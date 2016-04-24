package shaders;

/**
 * Created by Carter Milch on 4/23/2016.
 */
public class StaticShader extends AbstractShader{

    private static final String VERTEX_FILE = "src/shaders/VertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/FragmentShader.txt";

    public StaticShader(){
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttribute(){
        super.bindAttribute(0, "position");
    }
}
