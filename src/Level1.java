/**
 * Level 1 of ShadowDimension
 *
 * @Victoria Halim
 */

public class Level1 extends Level {
    private final static String WORLD_FILE_PATH = "res/level1.csv";
    private final static String BACKGROUND_IMAGE_SRC = "res/background1.png";

    public Level1() {
        super(BACKGROUND_IMAGE_SRC, WORLD_FILE_PATH);
    }

    /**
     * Return true if player has won the level by killing navec
     *
     * @return  whether player has won level 1
     */
    public boolean isLevelWon() {
        return this.isNavecKilled();
    }
}
