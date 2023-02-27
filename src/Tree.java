import bagel.util.Point;

/**
 * Class representing a tree game object
 *
 * @author Victoria Halim
 */

public class Tree extends Obstacle {
    private final static String TREE_NAME = "Tree";
    private final static String TREE_IMG_SRC = "res/tree.png";
    private final static int TREE_DAMAGE = 0;

    public Tree(Point coordinate) {
        super(TREE_NAME, coordinate, TREE_IMG_SRC, TREE_DAMAGE);
    }
}
