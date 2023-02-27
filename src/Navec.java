import bagel.util.Point;

/**
 * Class representing the 'Navec' boss demon
 *
 * @author Victoria Halim
 */

public class Navec extends AbstractDemon {
    private final static String NAVEC_NAME = "Navec";
    private final static String NAVEC_RIGHT_IMG_SRC = "res/navec/navecRight.png";
    private final static String NAVEC_LEFT_IMG_SRC = "res/navec/navecLeft.png";
    private final static String NAVEC_INVINCIBLE_RIGHT_IMG_SRC = "res/navec/navecInvincibleRight.PNG";
    private final static String NAVEC_INVINCIBLE_LEFT_IMG_SRC = "res/navec/navecInvincibleLeft.PNG";
    private final static String NAVEC_FIRE_IMG_SRC = "res/navec/navecFire.png";

    private final static int NAVEC_ATTACK_RADIUS = 200;
    private final static int NAVEC_MAX_HEALTH = 80;
    private final static int NAVEC_DAMAGE = 20;

    public Navec(Point coordinate, Point topLeftBound, Point bottomRightBound) {
        super(NAVEC_NAME, coordinate, NAVEC_RIGHT_IMG_SRC, NAVEC_LEFT_IMG_SRC, NAVEC_INVINCIBLE_RIGHT_IMG_SRC,
                NAVEC_INVINCIBLE_LEFT_IMG_SRC, NAVEC_FIRE_IMG_SRC, NAVEC_ATTACK_RADIUS, NAVEC_MAX_HEALTH, NAVEC_DAMAGE,
                true, false, topLeftBound, bottomRightBound);
    }
}
