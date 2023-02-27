import bagel.util.Point;

/**
 * Class representing a normal demon
 *
 * @author Victoria Halim
 */

public class Demon extends AbstractDemon {
    private final static String DEMON_NAME = "Demon";
    private final static String DEMON_RIGHT_IMG_SRC = "res/demon/demonRight.png";
    private final static String DEMON_LEFT_IMG_SRC = "res/demon/demonLeft.png";
    private final static String DEMON_INVINCIBLE_RIGHT_IMG_SRC = "res/demon/demonInvincibleRight.PNG";
    private final static String DEMON_INVINCIBLE_LEFT_IMG_SRC = "res/demon/demonInvincibleLeft.PNG";
    private final static String DEMON_FIRE_IMG_SRC = "res/demon/demonFire.png";

    private final static int DEMON_MAX_HEALTH = 40;
    private final static int DEMON_DAMAGE = 10;
    private final static int DEMON_ATTACK_RADIUS = 150;

    public Demon(Point coordinate, Point topLeftBound, Point bottomRightBound) {
        super(DEMON_NAME, coordinate, DEMON_RIGHT_IMG_SRC, DEMON_LEFT_IMG_SRC, DEMON_INVINCIBLE_RIGHT_IMG_SRC,
                DEMON_INVINCIBLE_LEFT_IMG_SRC, DEMON_FIRE_IMG_SRC, DEMON_ATTACK_RADIUS, DEMON_MAX_HEALTH, DEMON_DAMAGE,
                false, true, topLeftBound, bottomRightBound);
    }
}
