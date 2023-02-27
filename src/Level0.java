import bagel.util.Point;

/**
 * Level 0 of ShadowDimension
 *
 * @Victoria Halim
 */

public class Level0 extends Level {
    private final static String WORLD_FILE_PATH = "res/level0.csv";
    private final static String BACKGROUND_IMAGE_SRC = "res/background0.png";

    // coordinates for player to win level 0
    private final static Point winningCoordinates = new Point(950, 670);

    public Level0() {
        super(BACKGROUND_IMAGE_SRC, WORLD_FILE_PATH);
    }

    /**
     * Return true if player has won the level by reaching winning portal coordinates
     *
     * @return  whether player has won level 0
     */
    public boolean isLevelWon() {
        return this.getPlayer().getCoordinate().x >= winningCoordinates.x &&
                this.getPlayer().getCoordinate().y >= winningCoordinates.y;
    }
}
