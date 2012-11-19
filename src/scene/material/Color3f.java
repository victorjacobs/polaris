package scene.material;

import scene.data.Tuple3f;

import java.awt.Color;
import java.io.Serializable;

public class Color3f extends Tuple3f implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8091034297331218912L;


	/**
     * Constructs and initializes a Color3f to (0.0, 0.0, 0.0).
     */

    public Color3f()
    {
    }


    /**
     * Constructs and initializes a Color3f from the three xyz values.
     * @param r
     * @param g
     * @param b
     */

    public Color3f(float r, float g, float b)
    {
    	// Ensure that r, g and b are NOT higher than 1
        super((r > 1) ? 1 : r, (g > 1) ? 1 : g, (b > 1) ? 1 : b);
    }


    /**
     * Constructs and initializes a Color3f from the specified Color3f. 
     * @param c
     */

    public Color3f(Color3f c)
    {
        super(c);
    }


    /**
     * Constructs and initializes a Color3f from the specified Tuple3f.
     * @param t
     */

    public Color3f(Tuple3f t)
    {
        super(t);
    }


    /**
     * Constructs and initializes a Color3f from the array of length 3.
     * @param c
     */

    public Color3f(float c[])
    {
        super(c);
    }


    /**
     * Constructs and initializes a Color3f from the specified AWT Color object.
     * @param c
     */

    public Color3f(Color c)
    {
        super((float)c.getRed() / 255F, (float)c.getGreen() / 255F, (float)c.getBlue() / 255F);
    }


    /**
     * Sets the r,g,b values of this Color3f object to those of the specified AWT Color object.
     * @param c
     */

    public final void set(Color c)
    {
        x = (float)c.getRed() / 255F;
        y = (float)c.getGreen() / 255F;
        z = (float)c.getBlue() / 255F;
    }


    /**
     * Returns a new AWT color object initialized with the r,g,b values of this Color3f object.
     * @return
     */

    public final Color get()
    {
        int i = Math.round(x * 255F);
        int j = Math.round(y * 255F);
        int k = Math.round(z * 255F);
        return new Color(i, j, k);
    }
    
	public float getRed() {
		return x;
	}

	public float getGreen() {
		return y;
	}

	public float getBlue() {
		return z;
	}

	public Color3f sum(Color3f other) {
		return new Color3f(other.x + x, other.y + y, other.z + z);
	}
}
