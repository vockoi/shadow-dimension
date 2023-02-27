import bagel.util.Point;
import bagel.Image;
import bagel.util.Rectangle;

/**
 * Abstract class representing properties of all game objects within a game
 *
 * @author Victoria Halim
 */

public abstract class GameObject {
    private final Image DEFAULT_IMAGE;
    private final String NAME;
    private final int DAMAGE;

    private Point coordinate;
    private Rectangle area;
    private boolean isKilled = false;

    public GameObject(String name, Point coordinate, String defaultImageSrc, int damage) {
        this.NAME = name;
        this.coordinate = coordinate;
        this.DEFAULT_IMAGE = new Image(defaultImageSrc);
        this.DAMAGE = damage;

        setArea(coordinate);
    }

    /**
     * Renders the game object's default image on the screen.
     */
    public void renderImage() {
        DEFAULT_IMAGE.drawFromTopLeft(coordinate.x, coordinate.y);
    }

    /**
     * Returns true if the gameObject 'collides' with another gameObject, defining collisions as rectangle overlaps.
     *
     * @param gameObject  game object which this game object is checking collisions with
     * @return            whether gameObject collides with this game object
     */
    public boolean collideWith(GameObject gameObject) {
        return area.intersects(gameObject.area);
    }

    public String getName() {
        return NAME;
    }

    public int getDamage() {
        return DAMAGE;
    }

    public Image getDEFAULT_IMAGE() {
        return DEFAULT_IMAGE;
    }

    public boolean isKilled() {
        return isKilled;
    }

    public void setKilled(boolean killed) {
        isKilled = killed;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Point coordinate) {
        this.coordinate = coordinate;
        setArea(coordinate);
    }

    private void setArea(Point coordinate) {
        double areaCoordinateX = coordinate.x + DEFAULT_IMAGE.getWidth()/2;
        double areaCoordinateY = coordinate.y + DEFAULT_IMAGE.getHeight()/2;
        Point areaCoordinate = new Point(areaCoordinateX, areaCoordinateY);
        area = DEFAULT_IMAGE.getBoundingBoxAt(areaCoordinate);
    }
}
