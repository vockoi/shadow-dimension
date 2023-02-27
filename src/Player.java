import bagel.*;
import bagel.util.Point;

/**
 * Class representing a player
 *
 * @author Victoria Halim
 */

public class Player extends Character implements Collideable {
    private final static String PLAYER_NAME = "Fae";
    private final static String PLAYER_RIGHT_IMG_SRC = "res/fae/faeRight.png";
    private final static String PLAYER_LEFT_IMG_SRC = "res/fae/faeLeft.png";
    private final Image PLAYER_ATTACK_RIGHT = new Image("res/fae/faeAttackRight.png");
    private final Image PLAYER_ATTACK_LEFT = new Image("res/fae/faeAttackLeft.png");

    // player's health bar visualization settings
    private final Font PLAYER_FONT_HEALTH = new Font("res/frostbite.ttf", 30);
    private static final Point PLAYER_HEALTH_POINT_COORDINATE = new Point(20, 25);

    private final Timer attackDuration = new Timer(1000);
    private final Timer attackCooldown = new Timer(2000);

    private final static int STEP_SIZE = 2;
    private final static int PLAYER_MAX_HEALTH = 100;
    private final static int PLAYER_DAMAGE = 20;

    // most recent key pressed from input
    private Keys recentKey;

    public Player(Point coordinate, Point topLeftBound, Point bottomRightBound) {
        super(PLAYER_NAME, coordinate, PLAYER_RIGHT_IMG_SRC, PLAYER_LEFT_IMG_SRC, PLAYER_MAX_HEALTH, PLAYER_DAMAGE,
                topLeftBound, bottomRightBound);
    }

    /**
     * Renders the Player's default image (facing right) if the player has most recently moved right
     * instead of left. Render the image facing left otherwise.
     */
    @Override
    public void renderImage() {
        if (!attackDuration.isRunning()) {
            super.renderImage();
        }
        else {
            if (isLastMovedRight()) {
                PLAYER_ATTACK_RIGHT.drawFromTopLeft(getCoordinate().x, getCoordinate().y);
            } else {
                PLAYER_ATTACK_LEFT.drawFromTopLeft(getCoordinate().x, getCoordinate().y);
            }
        }

        drawHealthBar(PLAYER_HEALTH_POINT_COORDINATE, PLAYER_FONT_HEALTH);
    }

    /**
     *  Updates player movement according to user input and update player state. If the player is in attack mode or
     *  in attack cooldown, increments the timers for each state.
     *
     * @param input  input to update player state with
     */
    public void update(Input input) {
        // have player respond to keyboard input
        if (input.isDown(Keys.RIGHT)) {
            move(Keys.RIGHT);
        } else if (input.isDown(Keys.LEFT)) {
            move(Keys.LEFT);
        } else if (input.isDown(Keys.UP)) {
            move(Keys.UP);
        } else if (input.isDown(Keys.DOWN)) {
            move(Keys.DOWN);
        } else if (input.isDown(Keys.A)) {
            attack();
        }

        updateInvincible();

        if (attackDuration.isRunning()) {
            if (attackDuration.incrementFrame() == false) {
                setAttacking(false);
                attackCooldown.startTimer();
            }
        }

        if (attackCooldown.isRunning()) {
            attackCooldown.incrementFrame();
        }
    }

    /**
     *  Places the player in attack mode (can damage enemies) if it isn't in attack cooldown.
     */
    private void attack() {
        if (attackCooldown.isRunning() || attackDuration.isRunning()) {
            return;
        }

        setAttacking(true);
        attackDuration.startTimer();
    }

    /**
     * If player is in its attack state, damages any character it collides with
     *
     * @param character  the character the player is performing a collision action with
     */
    public void collisionAction(Character character) {
        if (this.isAttacking()) {
            character.damagedBy(this);
            setAttacking(false);
        }
    }

    /**
     * Updates player's coordinates according to which direction key was pressed. Does not update if the move was
     * invalid.
     *
     * @param inputKey  user input on which direction they'd like the player to move
     */
    private void move(Keys inputKey) {
        Point oldPoint = getCoordinate();
        Point newPoint = oldPoint;

        if (inputKey == Keys.RIGHT) {
            newPoint = new Point(oldPoint.x + STEP_SIZE, oldPoint.y);
            setLastMovedRight(true);
        } else if (inputKey == Keys.LEFT) {
            newPoint = new Point(oldPoint.x - STEP_SIZE, oldPoint.y);
            setLastMovedRight(false);
        } else if (inputKey == Keys.UP) {
            newPoint = new Point(oldPoint.x, oldPoint.y - STEP_SIZE);
        } else if (inputKey == Keys.DOWN) {
            newPoint = new Point(oldPoint.x, oldPoint.y + STEP_SIZE);
        }

        recentKey = inputKey;

        // update the player's coordinates only if movement is within bounds
        if (!outOfBounds(newPoint)) {
            setCoordinate(newPoint);
        }
    }

    /**
     * Changes back player's coordinates to its previous state depending on the last key pressed.
     */
    public void reverseMove() {
        Point oldPoint = getCoordinate();
        Point newPoint = oldPoint;

        if (recentKey == Keys.RIGHT) {
            newPoint = new Point(oldPoint.x - STEP_SIZE, oldPoint.y);
        } else if (recentKey == Keys.LEFT) {
            newPoint = new Point(oldPoint.x + STEP_SIZE, oldPoint.y);
        } else if (recentKey == Keys.UP) {
            newPoint = new Point(oldPoint.x, oldPoint.y + STEP_SIZE);
        } else if (recentKey == Keys.DOWN) {
            newPoint = new Point(oldPoint.x, oldPoint.y - STEP_SIZE);
        }

        setCoordinate(newPoint);
    }
}