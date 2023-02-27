import bagel.util.Point;
import bagel.*;

/**
 * Class representing a game character
 *
 * @author Victoria Halim
 */

public abstract class Character extends GameObject {
    private final int MAX_HEALTH;
    private int health;

    // for visualization of a character's health status
    DrawOptions fontHealthColor = new DrawOptions();
    private final DrawOptions GREEN_HEALTH_FONT = new DrawOptions().setBlendColour(0, 0.8, 0.2);
    private final DrawOptions ORANGE_HEALTH_FONT = new DrawOptions().setBlendColour(0.9, 0.6, 0);
    private final DrawOptions RED_HEALTH_FONT = new DrawOptions().setBlendColour(1, 0, 0);
    private final static int GREEN_HEALTH_BOUND = 100;
    private final static int ORANGE_HEALTH_BOUND = 65;
    private static final int RED_HEALTH_BOUND = 35;

    // character movement bounds
    private final Point topLeftBound;
    private final Point bottomRightBound;

    // character left moving image
    private final Image CHARACTER_LEFT;

    // flags to indicate character states
    private boolean isAttacking = false;
    private boolean isInvincible = false;
    private Timer invincibleCooldown = new Timer(3000);

    // true if character has most recently moved right instead of left, and false otherwise
    private boolean lastMovedRight = true;

    public Character(String name, Point coordinate, String defaultImageSrc, String characterLeftSrc, int maxHealth,
                     int damage, Point topLeftBound, Point bottomRightBound) {
        super(name, coordinate, defaultImageSrc, damage);
        this.CHARACTER_LEFT = new Image(characterLeftSrc);
        this.MAX_HEALTH = maxHealth;
        this.health = MAX_HEALTH;
        this.topLeftBound = topLeftBound;
        this.bottomRightBound = bottomRightBound;
    }

    /**
     * Renders a Character's image (right or left) depending on if it has most recently moved right or left. If it
     * has most recently moved left, render the image facing left.
     */
    @Override
    public void renderImage() {
        if (lastMovedRight) {
            super.renderImage();
        } else {
            CHARACTER_LEFT.drawFromTopLeft(getCoordinate().x, getCoordinate().y);
        }
    }

    /**
     * Draws a character's health bar
     *
     * @param healthPointCoordinate  coordinate at which the health bar would be rendered
     * @param healthFont             font to draw the health bar with
     */
    public void drawHealthBar(Point healthPointCoordinate, Font healthFont) {
        // health bar color logic
        int healthPercentage = (int) ((double) health/MAX_HEALTH*100);

        if (healthPercentage < RED_HEALTH_BOUND) {
            fontHealthColor = RED_HEALTH_FONT;
        } else if (healthPercentage < ORANGE_HEALTH_BOUND) {
            fontHealthColor = ORANGE_HEALTH_FONT;
        } else if (healthPercentage <= GREEN_HEALTH_BOUND) {
            fontHealthColor = GREEN_HEALTH_FONT;
        }

        healthFont.drawString((int)((double) health/MAX_HEALTH*100) + "%", healthPointCoordinate.x,
                healthPointCoordinate.y, fontHealthColor);
    }

    /**
     * If a character's new coordinates would be out of the given bounds, return true.
     *
     * @param newPoint  new coordinate the character would like to move to
     * @return          whether newPoint is out of the character's movement bounds
     */
    public boolean outOfBounds(Point newPoint) {
        return (newPoint.x < topLeftBound.x || newPoint.x > bottomRightBound.x
                || newPoint.y < topLeftBound.y || newPoint.y > bottomRightBound.y);
    }

    /**
     * Character 'reverse move' behavior when it collides with a stationary game object
     */
    public abstract void reverseMove();

    /**
     * Update character health to reflect damage by a game object. If the character is currently invincible, no
     * damage is dealt. If the character is attacked by another character, triggers invincible status in the current
     * character in addition to dealing damage. Prints log detailing damage dealt on command line.
     *
     * @param gameObject  game object the character is being dmamaged by
     */
    public void damagedBy(GameObject gameObject) {
        if (this.isInvincible) {
            return;
        } else if (gameObject instanceof Character)  {
            this.isInvincible = true;
            invincibleCooldown.startTimer();
        }

        // cap minimum health at 0
        health -= gameObject.getDamage();
        if (health < 0) {
            health = 0;
        }

        System.out.println(gameObject.getName() + " inflicts " + gameObject.getDamage() + " damage points on " +
                this.getName() + ". " + this.getName() + "'s current health: " + health + "/" + MAX_HEALTH);

        if (health == 0) {
            this.setKilled(true);
        }
    }

    /**
     * Update character's invincible cooldown timer if it is currently in invincible status
     */
    public void updateInvincible() {
        if (isInvincible) {
            if (!invincibleCooldown.incrementFrame()) {
                // character goes back to normal status once timer is done
                isInvincible = false;
            }
        }
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public boolean isLastMovedRight() {
        return lastMovedRight;
    }

    public void setLastMovedRight(boolean lastMovedRight) {
        this.lastMovedRight = lastMovedRight;
    }
}
