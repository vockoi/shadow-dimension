import bagel.util.Point;

/**
 * Class representing a wall game object
 *
 * @Victoria Halim
 */

public class Wall extends Obstacle {
    private final static String WALL_NAME = "Wall";
    private final static String WALL_IMG_SRC = "res/wall.png";
    private final static int WALL_DAMAGE = 0;

    public Wall(Point coordinate) {
        super(WALL_NAME, coordinate, WALL_IMG_SRC, WALL_DAMAGE);
    }
}