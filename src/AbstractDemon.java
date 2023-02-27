import bagel.*;
import bagel.util.Point;

import java.util.Random;

/**
 * Class representing an abstract demon enemy
 *
 * @author Victoria Halim
 */

public class AbstractDemon extends Character {
    // demon images
    private final Image ABS_DEMON_INVINCIBLE_LEFT;
    private final Image ABS_DEMON_INVINCIBLE_RIGHT;

    // for demon's attack mechanism
    private Direction attackDirection;
    private Point fireShootPoint;
    private Fire demonFire;

    // demon movement settings
    private final boolean IS_AGGRESSIVE;
    private final double INITIAL_MOVEMENT_SPEED;
    private final static double MIN_MOVEMENT_SPEED = 0.2;
    private final static double MAX_MOVEMENT_SPEED = 0.7;
    private MovementDirection moveDirection = MovementDirection.RIGHT;
    private final static double CHANGE_PER_TIMESCALE = 0.5;
    private double currMovementSpeed;

    // demon health bar settings
    private final Font ABS_DEMON_FONT_HEALTH = new Font("res/frostbite.ttf", 15);

    // initializing player detection variables
    private Circle attackRange;
    private boolean playerInAttackRange = false;

    public AbstractDemon(String name, Point coordinate, String demonRightImgSrc, String demonLeftImgSrc,
                         String demonInvincibleRightImgSrc, String demonInvincibleLeftImgSrc, String fireImgSrc,
                         int attackRadius, int maxHealth, int damage, boolean isAggressive, boolean setRandomAggressive,
                         Point topLeftBound, Point bottomRightBound) {
        super(name, coordinate, demonRightImgSrc, demonLeftImgSrc, maxHealth, damage, topLeftBound, bottomRightBound);

        Random random = new Random();

        // if the demon's aggressiveness is random, generate a random boolean
        if (setRandomAggressive) {
            isAggressive = random.nextBoolean();
        }

        this.IS_AGGRESSIVE = isAggressive;

        // if demon is an aggressive type, it should have a random non-zero movement speed and initial direction
        if (this.IS_AGGRESSIVE) {
            INITIAL_MOVEMENT_SPEED = MIN_MOVEMENT_SPEED + (MAX_MOVEMENT_SPEED * random.nextDouble());
            moveDirection = generateRandomMovementDirection();
        } else {
            INITIAL_MOVEMENT_SPEED = 0;
        }

        currMovementSpeed = INITIAL_MOVEMENT_SPEED;

        this.ABS_DEMON_INVINCIBLE_RIGHT = new Image(demonInvincibleRightImgSrc);
        this.ABS_DEMON_INVINCIBLE_LEFT = new Image(demonInvincibleLeftImgSrc);
        demonFire = new Fire(this, name, coordinate, fireImgSrc, damage);

        double attackRangeCentreX = this.getCoordinate().x + this.getDEFAULT_IMAGE().getWidth()/2;
        double attackRangeCentreY = this.getCoordinate().y + this.getDEFAULT_IMAGE().getWidth()/2;
        Point attackRangeCentre = new Point(attackRangeCentreX, attackRangeCentreY);
        attackRange = new Circle(attackRangeCentre, attackRadius);
    }


    /**
     * Enumeration of the 4 possible directions a demon can move towards
     */
    private enum MovementDirection {
        UP, DOWN, LEFT, RIGHT;
    }

    /**
     * Generates a random movement direction
     *
     * @return   a random MovementDirection
     */
    private MovementDirection generateRandomMovementDirection() {
        Random random = new Random();
        MovementDirection[] directionValues = MovementDirection.values();
        int randomNum = random.nextInt(directionValues.length);

        return directionValues[randomNum];
    }

    /**
     * Renders a Demon's image (right or left) depending on if it has most recently moved right or left. If it
     * has most recently moved left, render the image facing left. If the demon is invincible, generates the
     * invincible versions of the right and left images.
     */
    @Override
    public void renderImage() {
        if (isInvincible()) {
            if (isLastMovedRight()) {
                ABS_DEMON_INVINCIBLE_RIGHT.drawFromTopLeft(this.getCoordinate().x, this.getCoordinate().y);
            } else {
                ABS_DEMON_INVINCIBLE_LEFT.drawFromTopLeft(this.getCoordinate().x, this.getCoordinate().y);
            }
        } else {
            super.renderImage();
        }

        if (isAttacking()) {
            demonFire.shoot(attackDirection, fireShootPoint);
        }

        // renders demon's health bar
        Point healthPointCoordinate = new Point(getCoordinate().x, getCoordinate().y - 6);
        drawHealthBar(healthPointCoordinate, ABS_DEMON_FONT_HEALTH);
    }

    /**
     *  Updates the demon's status, movement if it's aggressive, and its attack state depending on player location.
     *
     * @param player  the player the demon check its status against
     */
    public void update(Player player) {
        updateInvincible();

        // moves demon if it is an aggressive type
        if (IS_AGGRESSIVE) {
            move();
        }

        // attack detection and attack trigger
        if (inAttackRange(player)) {
            attack(player);
        } else {
            setAttacking(false);
            this.demonFire.setActive(false);
        }
    }

    /**
     *  Returns true if player is in the demon's attack range, and false otherwise.
     *
     * @param player  player the demon check its attack range with
     * @return        whether the player is in demon's attack range
     */
    private boolean inAttackRange(Player player) {
        return attackRange.pointInCircle(player.getCoordinate());
    }

    /**
     *  Returns direction from demon at which player is closest to.
     *
     * @param player  player the demon check its closest attack direction with
     * @return        closest direction player is to demon
     */
    private Direction closestAttackDirection(Player player) {
        Point playerPoint = player.getCoordinate();
        Point demonPoint = this.getCoordinate();
        if (playerPoint.x <= demonPoint.x && playerPoint.y <= demonPoint.y) {
            return Direction.TOP_LEFT;
        } else if (playerPoint.x <= demonPoint.x && playerPoint.y > demonPoint.y) {
            return Direction.BOTTOM_LEFT;
        } else if (playerPoint.x > demonPoint.x && playerPoint.y <= demonPoint.y) {
            return Direction.TOP_RIGHT;
        } else {
            return Direction.BOTTOM_RIGHT;
        }
    }

    /**
     *  Finds point at which the demon would shoot its fire i.e. which point Bagel would render the fire image at.
     *
     * @param fireDirection  direction the demon wants to shoot its fire at
     */
    private void findShootPoint(Direction fireDirection) {
        double shootPointX = this.getCoordinate().x;
        double shootPointY = this.getCoordinate().y;

        if (fireDirection == Direction.TOP_LEFT) {
            shootPointX = shootPointX - this.demonFire.getDEFAULT_IMAGE().getWidth();
            shootPointY = shootPointY - this.demonFire.getDEFAULT_IMAGE().getHeight();
        } else if (fireDirection == Direction.BOTTOM_LEFT) {
            shootPointX = shootPointX - this.demonFire.getDEFAULT_IMAGE().getWidth();
            shootPointY = shootPointY + this.getDEFAULT_IMAGE().getHeight();
        } else if (fireDirection == Direction.TOP_RIGHT) {
            shootPointX = shootPointX + this.getDEFAULT_IMAGE().getWidth();
            shootPointY = shootPointY - this.demonFire.getDEFAULT_IMAGE().getHeight();
        } else if (fireDirection == Direction.BOTTOM_RIGHT) {
            shootPointX = shootPointX + this.getDEFAULT_IMAGE().getWidth();
            shootPointY = shootPointY + this.getDEFAULT_IMAGE().getHeight();
        }

        this.fireShootPoint = new Point(shootPointX, shootPointY);
    }

    /**
     *  Attacks the player by shooting fire. Damages the player if the player collides with the fire.
     *
     * @param  player the demon is attacking
     */
    private void attack(Player player) {
        setAttacking(true);

        // determining where to render the fire image
        this.attackDirection = closestAttackDirection(player);
        findShootPoint(attackDirection);

        if (this.demonFire.collideWith(player)) {
            this.demonFire.collisionAction(player);
        }
    }

    /**
     * Updates the demon's coordinates by moving it according to its movement type. Reverses move direction if new
     * coordinate is out of bounds.
     */
    private void move() {
        double newCoordinateX = this.getCoordinate().x;
        double newCoordinateY = this.getCoordinate().y;

        if (moveDirection == MovementDirection.UP) {
            newCoordinateY -= currMovementSpeed;
        } else if (moveDirection == MovementDirection.DOWN) {
            newCoordinateY += currMovementSpeed;
        } else if (moveDirection == MovementDirection.LEFT) {
            newCoordinateX -= currMovementSpeed;
            setLastMovedRight(false);
        } else {
            newCoordinateX += currMovementSpeed;
            setLastMovedRight(true);
        }

        Point newCoordinate = new Point(newCoordinateX, newCoordinateY);

        if (outOfBounds(newCoordinate)) {
            reverseMove();
        } else {
            this.setCoordinate(newCoordinate);
            attackRange.setCentre(newCoordinate);
        }
    }

    /**
     *  Reverses the demon's movement direction
     */
    public void reverseMove() {
        if (moveDirection == MovementDirection.UP) {
            moveDirection = MovementDirection.DOWN;
        } else if (moveDirection == MovementDirection.DOWN) {
            moveDirection = MovementDirection.UP;
        } else if (moveDirection == MovementDirection.LEFT) {
            moveDirection = MovementDirection.RIGHT;
        } else {
            moveDirection = MovementDirection.LEFT;
        }
    }

    /**
     *  Changes demon's movement speed based on the timescale
     *
     * @param timescale  timescale the demon would like to adjust its movement speed with
     */
    public void changeMovementSpeed(double timescale) {
        currMovementSpeed = INITIAL_MOVEMENT_SPEED * Math.pow(1 + CHANGE_PER_TIMESCALE, timescale);
    }
}
