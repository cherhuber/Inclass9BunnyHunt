package bunnyhunt;
import java.util.Random;

public class Animal {
    private boolean haveSeenEnemy;
    private boolean canSeeEnemyNow;
    private int distanceToEnemy;
    private int directionToEnemy;
    private int currentDirection;
    // instance variables -- every animal has a location   
    private Model model;
    /** The row of the field containing this animal (do not modify!) */
    int row;
    /** The column of the field containing this animal (do not modify!) */
    int column;
    static Random randomNumberGenerator = new Random();
    
    /**
     * Constructor for objects of class Animal.
     *
     * @param model  the model (needed to use Model.look and Model.move methods
     * @param row  the row of the field in which to place this animal
     * @param column  the column of the field in which to place this animal
     */
    public Animal(Model model, int row, int column) {
        this.model = model;
        this.row = row;
        this.column = column;
        haveSeenEnemy = false;
        canSeeEnemyNow = false;
    }
    
    /**
     * Utility method to choose a random integer from min
     * to max, inclusive.
     *
     * @param min  the minimum value to be returned
     * @param max  the maximum value to be returned
     * @return a random integer N, where min &lt;= N &lt;= max
     */
    static int random(int min, int max) {
        return Model.random(min, max);
    }

    /**
     * Finds the first visible thing in the given direction, starting
     * from this animal's current position.
     *
     * @param direction the direction in which to look
     * @return the object seen
     */
    int look(int direction) {
        return model.look(row, column, direction);
    }
    
    /**
     * Finds the distance to the first visible thing in the given
     * direction, starting from this animal's current position.
     *
     * @param direction the direction in which to look
     * @return the distance to the object seen
     */
    int distance(int direction) {
        return model.distance(row, column, direction);
    }
    
    /**
     * Determines whether this animal can move one step in the
     * given direction. An animal cannot move off the edge of
     * the array or onto a bush, but it <i>can</i> move onto
     * another animal.<p>
     * If we extend this program to allow multiple bunnys or
     * multiple wolfes, we should disallow an animal moving onto
     * another animal of the same type.
     *
     * @param direction the direction in which to consider moving
     * @return true if the move is possible
     */
    boolean canMove(int direction) {
        if (direction == Model.STAY) return true;
        if (distance(direction) > 1) return true;
        int object = look(direction);
        if (object == Model.EDGE || object == Model.BUSH) return false;
        return true;
    }
    
    /**
     * Decides what direction to move next.
     *
     * @return the desired direction
     */
//    int decideMove() {
//        return Model.STAY;
//    }
 //added 4/13   
    int decideMove(int enemy) {
    
        // look all around for bunny
        canSeeEnemyNow = false;
        for (int i = Model.MIN_DIRECTION; i <= Model.MAX_DIRECTION; i++) {
            if (look(i) == enemy) {
                canSeeEnemyNow = haveSeenEnemy = true;
                if(enemy == 1){
                    directionToEnemy = i;
                }else{
                    directionToEnemy = Model.turn(i, 2);
                }
                    
                distanceToEnemy = distance(i);
            }
        }
        
        // if bunny/wolf has been seen recently (not necessarily this time),
        // move toward its last known position
        if (haveSeenEnemy) {
            if (distanceToEnemy > 0) {
                if(enemy == 1){//if wolf go towards enemy
                    distanceToEnemy--;
                }else{//if bunny move away
                    distanceToEnemy++;
                }
                return directionToEnemy;
            }
            else { // bunny was here--where did it go?
                haveSeenEnemy
                        = false;
                currentDirection = Model.random(Model.MIN_DIRECTION,
                                                Model.MAX_DIRECTION);
            }
        }
        
        // either haven't seen bunny, or lost track of bunny
        // continue with current direction, maybe dodging bushes
        if (canMove(currentDirection))
            return currentDirection;
        else if (canMove(Model.turn(currentDirection, 1)))
            return Model.turn(currentDirection, 1);
        else if (canMove(Model.turn(currentDirection, -1)))
            return Model.turn(currentDirection, -1);
        else {
            currentDirection = Model.random(Model.MIN_DIRECTION,
                                            Model.MAX_DIRECTION);
            for (int i = 0; i < 8; i++) {
                if (canMove(currentDirection))
                    return currentDirection;
                else
                    currentDirection = Model.turn(currentDirection, 1);
            }
        }
        // stuck! cannot move
        return Model.STAY;
    }
}

