package scene.data;

import java.io.Serializable;


public class Vector3f extends Tuple3f implements Serializable, Comparable<Vector3f>
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
    	this(0, 0, 0);
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
    
    public float norm() {
    	return (float) Math.sqrt(x * x + y * y + z * z);
    }
    
    public Vector3f crossProduct(Vector3f other) {
    	float C1 = y * other.z - z * other.y;
    	float C2 = z * other.x - x * other.z;
    	float C3 = x * other.y - y * other.x;
    	return new Vector3f(C1, C2, C3);
    }
    
    public Vector3f divideBy(float divider) {
    	return new Vector3f(x / divider, y / divider, z / divider);
    }

	public Vector3f divideBy(float[] divider) {
		return new Vector3f(x / divider[0], y / divider[1], z / divider[2]);
	}
    
    public Vector3f minus(Vector3f min) {
    	return new Vector3f(x - min.x, y - min.y, z - min.z);
    }
    
    public Vector3f negate() {
    	return (new Vector3f()).minus(this);
    }
    
    public Vector3f normalize() {
    	return this.divideBy(this.norm());
    }
    
    public Vector3f multiply(float mult) {
    	return new Vector3f(mult * x, mult * y, mult * z);
    }
    
    public Vector3f sum(Vector3f other) {
    	return new Vector3f(x + other.x, y + other.y, z + other.z);
    }
    
    public float dotProduct(Vector3f other) {
    	return x * other.x + y * other.y + z * other.z;
    }
    
    public Vector3f reflectOver(Vector3f normal) {
		float projectionOnNormal = this.dotProduct(normal);

		return this.minus(normal.multiply(2 * projectionOnNormal));
    }

	public Vector3f replaceSmallestComponentWith(float replace) {
		float[] values = new float[3];
		values[0] = x;
		values[1] = y;
		values[2] = z;

		int smallest = 0;
		float newX, newY, newZ;

		if (y < values[smallest]) {
			smallest = 1;
		}
		if (z < values[smallest]) {
			smallest = 2;
		}

		newX = (smallest == 0) ? replace : x;
		newY = (smallest == 1) ? replace : y;
		newZ = (smallest == 2) ? replace : z;

		return new Vector3f(newX, newY, newZ);
	}
    
    @Override
    public boolean equals(Object o) {
    	if (this == o) return true;
    	if (!(o instanceof Vector3f)) return false;
    	
    	Vector3f other = (Vector3f) o;
    	
    	if (this.x == other.x && this.y == other.y && this.z == other.z) return true;
    	
    	return false;
    }
    
    @Override
    public String toString() {
    	return "(" + x + ", " + y + ", " + z + ")";
    }

	public float get(int i) {
		if (i == 0) return x;
		if (i == 1) return y;
		if (i == 2) return z;

		return 0;
	}

	@Override
	public int compareTo(Vector3f o) {
		if (norm() < o.norm()) return -1;
		if (norm() > o.norm()) return 1;
		return 0;
	}
}
