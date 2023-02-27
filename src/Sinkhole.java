import bagel.util.Point;

/**
 * Class representing sinkhole game object
 *
 * @author Victoria Halim
 */

public class Sinkhole extends Obstacle {
    private final static String SINKHOLE_NAME = "Sinkhole";
    private final static String SINKHOLE_IMG_SRC = "res/sinkhole.png";
    private final static int SINKHOLE_DAMAGE = 30;

    public Sinkhole(Point coordinate) {
        super(SINKHOLE_NAME, coordinate, SINKHOLE_IMG_SRC, SINKHOLE_DAMAGE);
    }

    /**
     * 'Bounces a character back' to its original coordinates if it collides with the sinkhole. If the character is a
     * player, deals damage to the player. Sinkhole disappears after a collision with a player.
     *
     * @param character  character the sinkhole is performing a collision action against
     */

    @Override
    public void collisionAction(Character character) {
        super.collisionAction(character);

        if (character instanceof Player) {
            Player player = (Player) character;
            player.damagedBy(this);
            setKilled(true);
        }
    }
}
