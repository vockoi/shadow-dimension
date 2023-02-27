import bagel.util.Point;

/**
 * Class representing obstacle game objects
 *
 * @author Victoria Halim
 */
public abstract class Obstacle extends GameObject implements Collideable {
    public Obstacle(String obstacleName, Point obstacleCoordinate, String obstacleImageSrc, int obstacleDamage) {
        super(obstacleName, obstacleCoordinate, obstacleImageSrc, obstacleDamage);
    }

    /**
     * 'Bounces a character back' to its original coordinates if it collides with the obstacle.
     *
     * @param character  character the obstacle is performing a collision action against
     */
    public void collisionAction(Character character) {
        character.reverseMove();
    }
}
