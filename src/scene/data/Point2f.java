package scene.data;

import java.io.Serializable;

public class Point2f extends Tuple2f implements Serializable
{
    public Point2f()
    {
    }

    public Point2f(float x, float y)
    {
        super(x, y);
    }

    public Point2f(float p[])
    {
        super(p);
    }

    public Point2f(Point2f p)
    {
        super(p);
    }

    public Point2f(Tuple2f t)
    {
        super(t);
    }

	public Point2f multiply(float mult) {
		return new Point2f(mult * x, mult * y);
	}

	public Point2f sum(Point2f other) {
		return new Point2f(x + other.x, y + other.y);
	}

}
