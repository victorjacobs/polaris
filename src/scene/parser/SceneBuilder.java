package scene.parser;

import org.xml.sax.InputSource;
import scene.Camera;
import scene.Scene;
import scene.geometry.Point3f;
import scene.geometry.Surface;
import scene.geometry.Vector3f;
import scene.lighting.Light;
import scene.material.Color3f;
import scene.material.Material;
import scene.material.TexCoord2f;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
  * Class used to build a scene from a given sdl file.
  * Implements the ParserHandler interface (these methods
  * need to be filled in by you).
  * 
  * Note that this class keeps the absolute path to the
  * directory where the sdl file was found.  If you put your
  * textures in the same directory, you can use this path
  * to construct an absolute file name for each texture.
  * You will probably need absolute file names when loading
  * the texture.
  */
public class SceneBuilder implements ParserHandler
{

    // the scene being built
    private Scene scene = null;

    private Scene getScene() { return scene; }

    // the path to the xml directory
    // this path can be used to put in front of the texture file name
    // to load the textures
    private String path = null;

    public String getPath() { return path; }


    /**
     * Load a scene.
     * @param filename The name of the file that contains the scene.
     * @return The scene, or null if something went wrong.
     * @throws FileNotFoundException The file could not be found.
     */
    public Scene loadScene(String filename) throws FileNotFoundException
    {
        // create file and file input stream
        File file = new File(filename);
        FileInputStream fileInputStream = new FileInputStream(file);

        // set the system id so that the dtd can be a relative path
        // the first 2 lines of your sdl file should always be
        //    <?xml version='1.0' encoding='utf-8'?>
        //    <!DOCTYPE Sdl SYSTEM "sdl.dtd">
        // and sdl.dtd should be in the same directory as the dtd
        // if you experience dtd problems, commend the doctype declaration
        //    <!-- <!DOCTYPE Sdl SYSTEM "sdl.dtd"> -->
        // and disable validation (see further)
        // although this is in general not a good idea

        InputSource inputSource = new InputSource(fileInputStream);
        String parentPath = file.getParentFile().getAbsolutePath() + "/";
        path = file.getParentFile().getAbsolutePath() + "/";
        inputSource.setSystemId("file:///" + file.getParentFile().getAbsolutePath() + "/");



        // create the new scene
        scene = new Scene();

        // create the parser and parse the input file
        Parser parser = new Parser();
        parser.setHandler(this);

        // if the output bothers you, set echo to false
        // also, if loading a large file (with lots of triangles), set echo to false
        // you should leave validate to true
        // if the docuement is not validated, the parser will not detect syntax errors
        if (parser.parse(inputSource, /* validate */ true, /* echo */ true) == false)
        {
            scene = null;
        }

        // return the scene
        return scene;
    }

	/*
	 * START OF OWN IMPLEMENTATION
	 */

	private Map<String, Camera> cameras;
	private Map<String, Light> lights;
	private Map<String, Surface> surfaces;
	private Map<String, Material> materials;

    // Start file
    public void startSdl() throws Exception
    {
		System.out.println("Start parsing SDL");
		cameras = new HashMap<String, Camera>();
		lights = new HashMap<String, Light>();
		surfaces = new HashMap<String, Surface>();
		materials = new HashMap<String, Material>();
    }

    public void endSdl() throws Exception
    {
		System.out.println("Finished parsing SDL");
    }

    
    // Start cameras
    public void startCameras() throws Exception
    {
		System.out.println("Start parsing cameras");
    }

    public void startCamera(Point3f position, Vector3f direction, Vector3f up, float fovy, String name) throws Exception
    {
		//cameras.put(name, new Camera(position, direction, up, , fovy));
    }

    public void endCamera() throws Exception
    {
    }

	public void endCameras() throws Exception
	{
		System.out.println("Finished parsing cameras");
	}
    
    
    // Lighting
    public void startLights() throws Exception
    {
    }

    public void endLights() throws Exception
    {
    }

    public void startDirectionalLight(Vector3f direction, float intensity, Color3f color, String name) throws Exception
    {

    }

    public void endDirectionalLight() throws Exception
    {
    }

    public void startPointLight(Point3f position, float intensity, Color3f color, String name) throws Exception
    {

    }

    public void endPointLight() throws Exception
    {
    }

    public void startSpotLight(Point3f position, Vector3f direction, float angle, float intensity, Color3f color, String name) throws Exception
    {

    }

    public void endSpotLight() throws Exception
    {
    }

    
    // Geometry
    public void startGeometry() throws Exception
    {
    }

    public void endGeometry() throws Exception
    {
    }

    public void startSphere(float radius, String name) throws Exception
    {

    }

    public void endSphere() throws Exception
    {
    }

    public void startCylinder(float radius, float height, boolean capped, String name) throws Exception
    {
    }

    public void endCylinder() throws Exception
    {
    }

    public void startCone(float radius, float height, boolean capped, String name) throws Exception
    {

    }

    public void endCone() throws Exception
    {
    }

    public void startTorus(float innerRadius, float outerRadius, String name) throws Exception
    {
    }

    public void endTorus() throws Exception
    {
    }

    public void startTeapot(float size, String name) throws Exception
    {

    }

    public void endTeapot() throws Exception
    {
    }

    public void startIndexedTriangleSet(Point3f [] coordinates, Vector3f [] normals, TexCoord2f [] textureCoordinates, int [] coordinateIndices, int [] normalIndices, int [] textureCoordinateIndices, String name) throws Exception
    {
    }

    public void endIndexedTriangleSet() throws Exception
    {
    }

    
    // Textures
    public void startTextures() throws Exception
    {
    }

    public void endTextures() throws Exception
    {
    }

    public void startTexture(String src, String name) throws Exception
    {
    }

    public void endTexture() throws Exception
    {
    }
    
    
    // Materials
    public void startMaterials() throws Exception
    {
    }

    public void endMaterials() throws Exception
    {
    }

    public void startDiffuseMaterial(Color3f color, String name) throws Exception
    {

    }

    public void endDiffuseMaterial() throws Exception
    {
    }

    public void startPhongMaterial(Color3f color, float shininess, String name) throws Exception
    {

    }

    public void endPhongMaterial() throws Exception
    {
    }

    public void startLinearCombinedMaterial(String material1Name, float weight1, String material2Name, float weight2, String name) throws Exception
    {
    }

    public void endLinearCombinedMaterial() throws Exception
    {
    }

    
    // Start scene
    public void startScene(String cameraName, String [] lightNames, Color3f background) throws Exception
    {
    }

    public void endScene() throws Exception
    {
    }

    public void startShape(String geometryName, String materialName, String textureName) throws Exception
    {
    }

    public void endShape() throws Exception
    {
    }

    
    // Transforms
    public void startRotate(Vector3f axis, float angle) throws Exception
    {
    }

    public void endRotate() throws Exception
    {
    }

    public void startTranslate(Vector3f vector) throws Exception
    {
    }

    public void endTranslate() throws Exception
    {
    }

    public void startScale(Vector3f scale) throws Exception
    {
    }

    public void endScale() throws Exception
    {
    }

}
