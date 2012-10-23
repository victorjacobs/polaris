
import java.io.Serializable;


public class Vector3f extends Tuple3f implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3063561618554192933L;


	/**
     * Constructs and initializes a Vector3f to (0,0,0).
     */

    public Vector3f()
    {
    }


    /**
     * Constructs and initializes a Vector3f from the specified xyz coordinates.
     * @param x
     * @param y
     * @param z
     */

    public Vector3f(float x, float y, float z)
    {
        super(x, y, z);
    }


    /**
     * Constructs and initializes a Vector3f from the specified Vector3f
     * @param v
     */

    public Vector3f(Vector3f v)
    {
        super(v);
    }


    /**
     * Constructs and initializes a Vector3f from the specified Tuple3f.
     * @param t
     */

    public Vector3f(Tuple3f t)
    {
        super(t);
    }


    /**
     * Constructs and initializes a Vector3f from the array of length 3.
     * @param v
     */

    public Vector3f(float v[])
    {
        super(v);
    }
    
}
