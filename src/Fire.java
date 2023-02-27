import bagel.DrawOptions;
import bagel.util.Point;

/**
 * Class representing a demon's fire game object
 *
 * @author Victoria Halim
 */

public class Fire extends GameObject implements Collideable {
    private final AbstractDemon DEMON_OWNER;

    // fire image rotation settings
    private DrawOptions TOP_RIGHT_ROTATION = new DrawOptions().setRotation(2*Math.PI/4);
    private DrawOptions BOTTOM_RIGHT_ROTATION = new DrawOptions().setRotation(4*Math.PI/4);
    private DrawOptions BOTTOM_LEFT_ROTATION = new DrawOptions().setRotation(6*Math.PI/4);

    // flag to indicate whether to render the fire image
    private boolean isActive = true;

    public Fire(AbstractDemon demonOwner, String ownerName, Point coordinate, String fireImgSrc, int damage) {
        super(ownerName, coordinate, fireImgSrc, damage);
        this.DEMON_OWNER = demonOwner;
    }

    /**
     * Renders the demon's fire image at coordinate fireShootPoint, with the fire pointed towards attackDirection.
     * 'Activates' the fire such that it could damage a player.
     *
     * @param attackDirection  direction relative to demon which fire is shot at
     * @param fireShootPoint   coordinate to render fire image at
     */
    public void shoot(Direction attackDirection, Point fireShootPoint) {
        this.isActive = true;
        this.setCoordinate(fireShootPoint);

        if (attackDirection == Direction.TOP_LEFT) {
            super.renderImage();
        } else if (attackDirection == Direction.BOTTOM_LEFT) {
            getDEFAULT_IMAGE().drawFromTopLeft(fireShootPoint.x, fireShootPoint.y, BOTTOM_LEFT_ROTATION);
        } else if (attackDirection == Direction.BOTTOM_RIGHT) {
            getDEFAULT_IMAGE().drawFromTopLeft(fireShootPoint.x, fireShootPoint.y, BOTTOM_RIGHT_ROTATION);
        } else {
            getDEFAULT_IMAGE().drawFromTopLeft(fireShootPoint.x, fireShootPoint.y, TOP_RIGHT_ROTATION);
        }
    }

    /**
     * Damages the character if they are a player, if they collide with active fire.
     *
     * @param character  character to carry out collision action with
     */
    public void collisionAction(Character character) {
        if (this.isActive && character instanceof Player) {
            Player player = (Player) character;
            player.damagedBy(DEMON_OWNER);
        }
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
