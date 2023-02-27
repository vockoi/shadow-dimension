import bagel.util.Point;
import java.lang.Math;

/**
 * Class representing a circle
 *
 * @author Victoria Halim
 */

public class Circle {
    private Point centre;
    private final double RADIUS;

    public Circle(Point centre, double radius) {
        this.centre = centre;
        this.RADIUS = radius;
    }

    /**
     * Returns true if a specified point lies in this circle, and false otherwise
     *
     * @param point  point to check with if it lies in this circle
     * @return       whether point lies in this circle
     */
    public boolean pointInCircle(Point point) {
        return Math.pow((point.x - centre.x), 2) + Math.pow((point.y - centre.y), 2) < Math.pow(RADIUS, 2);
    }

    public void setCentre(Point centre) {
        this.centre = centre;
    }
}
