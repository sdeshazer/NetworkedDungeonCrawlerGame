package Project2;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;

import java.util.ArrayList;
import java.util.LinkedList;

/***
 * Enitity class for reprsenting Enemies.
 *
 *  Methods:
 *    moveUp()
 *    moveDown()
 *    moveLeft()
 *    moveRight()
 *    stop()
 *    fire()
 *
 *  Important Fields:
 *    id: differentiates types of enemies
 *      1: Melee enemy, attacks from melee distance and moves directly toward the player.
 *      2: Ranged enemy, attacks the player from range, only fires if they have line of sight on the player, and
 *      other wise moves towards them.
 */


public class Enemy extends Entity{

  private Vector velocity;
  private float speed;
  public int id;
  private int health, sleeptimer, damage;
  private boolean isDead, sleep, faceRight, active;
  private double targetAngle;
  private Animation moveLeft, moveRight, idleLeft, idleRight, current;
  public Coordinate worldPos;

  //assigning id's for tiles with a name to avoid confusion:
  private final int floorTile = 0;
  private final int wallTile = 1;
  private final int wallTileWithTorch = 2;
  private final int exitDoorGoal = 6;

  /***
   * Constructor, prepares default stats and Images/anmiations
   * @param x
   *  x Tile Coordinate to spawn the player in
   * @param y
   *  y Tile coordinate to spawn the player in
   */
  public Enemy(final int x, final int y, int newid) {
    TileIndex tileIndex = new TileIndex(x,y);
    worldPos = MapUtil.convertTileToWorld(tileIndex);
    // addImageWithBoundingBox(ResourceManager.getImage(DungeonGame.PLAYER_ARROWTEST_RSC));
    velocity = new Vector(0,0);
    speed = 0.15f;
    id = newid;
    isDead = sleep = faceRight = active = false;
    sleeptimer = 0;
    // Set animations and attributes based on type:
    //  Melee
    if(id == 1) {
      health = 10;
      damage = 2;
      moveLeft = new Animation(ResourceManager.getSpriteSheet(
              DungeonGame.ENEMY_MELEEMOVELEFT_RSC, 32, 32), 0, 0, 3, 0,
              true, 100, true);
      moveRight = new Animation(ResourceManager.getSpriteSheet(
              DungeonGame.ENEMY_MELEEMOVERIGHT_RSC, 32, 32), 0, 0, 3, 0,
              true, 100, true);
      idleLeft = new Animation(ResourceManager.getSpriteSheet(
              DungeonGame.ENEMY_MELEEIDLELEFT_RSC, 32, 32), 0, 0, 3, 0,
              true, 100, true);
      idleRight = new Animation(ResourceManager.getSpriteSheet(
              DungeonGame.ENEMY_MELEEIDLERIGHT_RSC, 32, 32), 0, 0, 3, 0,
              true, 100, true);
    }
    //  Ranged
    else if(id == 2) {
      health = 10;
      damage = 2;
      moveLeft = new Animation(ResourceManager.getSpriteSheet(
              DungeonGame.ENEMY_RANGEDMOVELEFT_RSC, 32, 32), 0, 0, 3, 0,
              true, 100, true);
      moveRight = new Animation(ResourceManager.getSpriteSheet(
              DungeonGame.ENEMY_RANGEDMOVERIGHT_RSC, 32, 32), 0, 0, 3, 0,
              true, 100, true);
      idleLeft = new Animation(ResourceManager.getSpriteSheet(
              DungeonGame.ENEMY_RANGEDIDLELEFT_RSC, 32, 32), 0, 0, 3, 0,
              true, 100, true);
      idleRight = new Animation(ResourceManager.getSpriteSheet(
              DungeonGame.ENEMY_RANGEDIDLERIGHT_RSC, 32, 32), 0, 0, 3, 0,
              true, 100, true);
    }
    //  Invalid id, so we need to kill it immediately
    else {
      isDead = true;
      health = 0;
    }
    addAnimation(idleLeft);
    current = idleLeft;
  }

  public void setWorldPos(TileIndex tileIndex) {
    worldPos = MapUtil.convertTileToWorld(tileIndex);
  }

  /**
   * This method is to be called before every enemy update. Contains all behaviors.
   * @param tilemap
   *  Tilemap representing the level the enemy is in
   * @param path1
   *  Vertex map from getDijkstras() which helps the enemy determine the path it wants to take. Pathing towards player 1
   * @param player1
   *  Player object representing player 1.
   */
  public void makeMove(Tile[][] tilemap, Vertex[][] path1, Player player1,
                       ArrayList<Projectile> projectileList, int delta) {
    TileIndex location = MapUtil.convertWorldToTile(worldPos);
    // First Check if the enemy was ever activated (by being in range of the player)
    if(!active) {
      // 240 is the range since thats about 16 * sqrt(2) * 10 or right past the edge of the screen (tile cost = 10)
      if (path1[location.x][location.y].getDistance() <= 240) {
        active = true;
        return;
      }
      return;
    }
    // Now check if were currently asleep due to actions such as attacking.
    if(sleep) {
      sleeptimer -= delta;
      if(sleeptimer <= 0)
        sleep = false;
      return;
    }

    // Get location and retrieve direction from the vertex map.
    TileIndex playerLocation = MapUtil.convertWorldToTile(player1.worldPos);
    int direction = path1[location.x][location.y].getDirection();

    // Attempt attacks depending on enemy type:
    //  Melee
    if(id == 1) {
      if(playerLocation.x == location.x && playerLocation.y == location.y) {
        player1.damage(damage);
        sleep = true;
        sleeptimer = 500;
        stop();
        return;
      }
    }

    //  Ranged
    if(id == 2) {
      // Check if the enemy has a clear line of sight on the player.
      if(lineOfSight(player1, tilemap)) {
        // targetAngle is set when lineOfSight() is called. It's an object property so we just need to access it.
        Projectile newProjectile = new Projectile(getX(), getY(), 2, targetAngle, damage);
        newProjectile.worldPos = new Coordinate(worldPos.x, worldPos.y);
        projectileList.add(newProjectile);
        sleep = true;
        sleeptimer = 1000;
        stop();
        return;
      }
    }

    // If attacks are unavailable, the enemy will just move.
    switch (direction) {
      case 1:  moveDownLeft();
        break;
      case 2:  moveDown();
        break;
      case 3:  moveDownRight();
        break;
      case 4:  moveLeft();
        break;
      case 6:  moveRight();
        break;
      case 7:  moveUpLeft();
        break;
      case 8:  moveUp();
        break;
      case 9:  moveUpRight();
        break;
      default: stop();
        break;
    }
  }

  /**
   * This method is to be called before every enemy update when in twoplayer mode. Contains all behaviors.
   * @param tilemap
   *  Tilemap representing the level the enemy is in
   * @param path1
   *  Vertex map from getDijkstras() which helps the enemy determine the path it wants to take. Pathing towards player 1
   * @param player1
   *  Player object representing player 1.
   */
  public void makeMove2P(Tile[][] tilemap, Vertex[][] path1, Player player1, Vertex[][] path2, Player player2,
                       ArrayList<Projectile> projectileList, int delta) {
    TileIndex location = MapUtil.convertWorldToTile(worldPos);
    // First Check if the enemy was ever activated (by being in range of the player)
    if (!active) {
      // 240 is the range since thats about 16 * sqrt(2) * 10 or right past the edge of the screen (tile cost = 10)
      if (path1[location.x][location.y].getDistance() <= 240 || path2[location.x][location.y].getDistance() <= 240) {
        active = true;
        return;
      }
      return;
    }
    // Now check if were currently asleep due to actions such as attacking.
    if (sleep) {
      sleeptimer -= delta;
      if (sleeptimer <= 0)
        sleep = false;
      return;
    }

    // Decide which player is closer
    Player closestPlayer;
    int direction;
    if (path1[location.x][location.y].getDistance() < path2[location.x][location.y].getDistance()) {
      direction = path1[location.x][location.y].getDirection();
      closestPlayer = player1;
    }
    else {
      direction = path2[location.x][location.y].getDirection();
      closestPlayer = player2;
    }
    // Get location and retrieve direction from the vertex map.
    TileIndex playerLocation = MapUtil.convertWorldToTile(closestPlayer.worldPos);


    // Attempt attacks depending on enemy type:
    //  Melee
    if(id == 1) {
      if(playerLocation.x == location.x && playerLocation.y == location.y) {
        closestPlayer.damage(damage);
        sleep = true;
        sleeptimer = 500;
        stop();
        return;
      }
    }

    //  Ranged
    if(id == 2) {
      // Check if the enemy has a clear line of sight on the player.
      if(lineOfSight(closestPlayer, tilemap)) {
        // targetAngle is set when lineOfSight() is called. It's an object property so we just need to access it.
        Projectile newProjectile = new Projectile(getX(), getY(), 2, targetAngle, damage);
        newProjectile.worldPos = new Coordinate(worldPos.x, worldPos.y);
        projectileList.add(newProjectile);
        sleep = true;
        sleeptimer = 1000;
        stop();
        return;
      }
    }

    // If attacks are unavailable, the enemy will just move.
    switch (direction) {
      case 1:  moveDownLeft();
        break;
      case 2:  moveDown();
        break;
      case 3:  moveDownRight();
        break;
      case 4:  moveLeft();
        break;
      case 6:  moveRight();
        break;
      case 7:  moveUpLeft();
        break;
      case 8:  moveUp();
        break;
      case 9:  moveUpRight();
        break;
      default: stop();
        break;
    }
  }

  /**
   * Function to be called when the enemy is damaged.
   * @param damage
   *  Amount of damage taken as a positive integer
   * @return
   * A boolean which is identical to to the isDead boolean.
   */
  public boolean damage(int damage) {
    health -= damage;
    if(health <= 0) {
      isDead = true;
      id = -1;
      return true;
    }
    return false;
  }

  public boolean isDead() { return isDead;}

  public void setDead() {isDead = true;}

  /**+
   * Below are the 5 movement functions for allowing the player to move around the map
   * They also set the proper animations for the enemy.
   */
  public void moveRight() {
    velocity = new Vector(speed, 0);
    faceRight = true;
    removeAnimation(current);
    addAnimation(moveRight);
    current = moveRight;
  }

  public void moveLeft() {
    velocity = new Vector(-speed, 0);
    faceRight = false;
    removeAnimation(current);
    addAnimation(moveLeft);
    current = moveLeft;
  }

  public void moveUp() {
    velocity = new Vector(0, -speed);
    removeAnimation(current);
    if(faceRight) {
      addAnimation(moveRight);
      current = moveRight;
    }
    else {
      addAnimation(moveLeft);
      current = moveLeft;
    }
  }

  public void moveDown() {
    velocity = new Vector(0, speed);
    removeAnimation(current);
    if(faceRight) {
      addAnimation(moveRight);
      current = moveRight;
    }
    else {
      addAnimation(moveLeft);
      current = moveLeft;
    }
  }

  // For the diaganol movement, speed is scaled in each direction by 1/sqrt(2) since its at a 45 degree angle.
  public void moveDownRight() {
    velocity = new Vector(0.71f * speed, 0.71f * speed);
    faceRight = true;
    removeAnimation(current);
    addAnimation(moveRight);
    current = moveRight;
  }

  public void moveDownLeft() {
    velocity = new Vector(-0.71f * speed, 0.71f * speed);
    faceRight = false;
    removeAnimation(current);
    addAnimation(moveLeft);
    current = moveLeft;
  }

  public void moveUpRight() {
    velocity = new Vector(0.71f * speed, -0.71f * speed);
    faceRight = true;
    removeAnimation(current);
    addAnimation(moveRight);
    current = moveRight;
  }

  public void moveUpLeft() {
    velocity = new Vector(-0.71f * speed, -0.71f * speed);
    faceRight = false;
    removeAnimation(current);
    addAnimation(moveLeft);
    current = moveLeft;
  }

  public void stop() {
    velocity = new Vector(0, 0);
    removeAnimation(current);
    if(faceRight) {
      addAnimation(idleRight);
      current = moveRight;
    }
    else {
      addAnimation(idleLeft);
      current = moveLeft;
    }
  }

  /**
   * This function is for getting the coordinate of the Enemy
   * @return
   * A Coordinate object with an x and y field representing the location in the tilemap the player currently exists in.
   */
  public TileIndex getLocation() {
    return MapUtil.convertWorldToTile(worldPos);
  }

  /**
   * This function is for calculating the offset from the center of the tile the Enemy currently exists in.
   * @return
   * A Vector containing the x and y difference between the center of the tile and the Enemy
   */
  public TileIndex getTileIndex() {
    return MapUtil.convertWorldToTile(worldPos);
  }


  public Vector getTileOffset() {
    TileIndex location = getTileIndex();
    // The center of the tile location is (Tile * tilewidth) + 1/2 tile width, since the entity's origin is the center.
    float tilex = (location.x * MapUtil.TILESIZE) + (MapUtil.TILESIZE / 2);
    float tiley = (location.y * MapUtil.TILESIZE) + (MapUtil.TILESIZE / 2);
    float x = worldPos.x;
    float y = worldPos.y;
    // Return the offset from the center
    return new Vector(tilex - x, tiley - y);
  }


  /**
   * This function is to be called before executing an enemy move.
   * TODO Add relevant checks for determining if a move is valid. Walls checks are unneccesary since due to the
   * pathfinding algorithm, the enemy should only make valid moves.
   */
  public boolean isMoveValid(int direction, Tile[][] tilemap) {

    return true;
  }

  public void update(final int delta) {
    Vector movement = velocity.scale(delta);
    worldPos.x += movement.getX();
    worldPos.y += movement.getY();
  }


  public void offsetUpdate(Tile[][] tilemap) {
    // Check if any adjacent tiles are walls, and if were inside any of them. If so do an offset update.
    TileIndex location = getTileIndex();
    // Tile above
    if (MapUtil.hasCollision(tilemap[location.x][location.y - 1].getID()) && getTileOffset().getY() >= 0) {
      worldPos.y += getTileOffset().getY();
    }
    // Tile Below
    if (MapUtil.hasCollision(tilemap[location.x][location.y + 1].getID())  && getTileOffset().getY() <= 0) {
      worldPos.y += getTileOffset().getY();
    }
    // Tile Left
    if (MapUtil.hasCollision(tilemap[location.x - 1][location.y].getID()) && getTileOffset().getX() >= 0) {
      worldPos.x += getTileOffset().getX();
    }
    // Tile Right
    if (MapUtil.hasCollision( tilemap[location.x + 1][location.y].getID()) && getTileOffset().getX() <= 0) {
      worldPos.x += getTileOffset().getX();
    }
  }


  /***
   * Function for determining if there is a clear line between a target and the enemy.
   * @param player
   * @param tilemap
   * @return
   */
  private boolean lineOfSight(Player player, Tile[][] tilemap) {
    float enemyx = worldPos.x;
    float enemyy = worldPos.y;
    float playerx = player.worldPos.x;
    float playery = player.worldPos.y;

    // Get a Vector between the two objects and scale it to 1.
    Vector sightLine = new Vector(playerx - enemyx, playery - enemyy);
    sightLine = sightLine.unit();
    targetAngle = sightLine.getRotation();

    // Starting at the enemy, and iterating through this unit vector until we reach or pass the player,
    // Check if there is an obstacle in the way. If there is, we don't have line of sight.
    // Check which x coordinate is greater to determine which way to do the check.
    if(enemyx > playerx) {
      while(enemyx > playerx) {
        TileIndex currentLocation = MapUtil.convertWorldToTile(new Coordinate(enemyx,enemyy));
        // Check if were in a wall
        if(MapUtil.hasCollision(tilemap[currentLocation.x][currentLocation.y].getID())) {
          return false;
        }
        // If no collisions, traverse further down the line.
        enemyx += sightLine.getX();
        enemyy += sightLine.getY();
      }
    }
    // POSSIBLY ADD A CHECK IF WERE DIRECTLY VERTICAL OF THE PLAYER. THIS IS AN EDGE CASE ALTHOUGH
    else {
      while(enemyx < playerx) {
        TileIndex currentLocation = MapUtil.convertWorldToTile(new Coordinate(enemyx,enemyy));
        // Check if were in a wall
        if(MapUtil.hasCollision(tilemap[currentLocation.x][currentLocation.y].getID())) {
          return false;
        }
        // If no collisions, traverse further down the line.
        enemyx += sightLine.getX();
        enemyy += sightLine.getY();
      }
    }
    return true;
  }

  /***
   * This function builds a string to send to the dummy client second player during online play. The string is in
   * the format: 'ENEMYTYPE;ENEMYXPOS;ENEMYYPOS;CURRENTANIMATIONID;'
   * @return
   * String to send across for 2P game.
   */
  public String getEnemyData() {
    String data = "";
    // Start with an identifier of which enemy this is
    data = data.concat(id + ";");
    // Next send positional data
    data = data.concat(worldPos.x + ";" + worldPos.y + ";");
    // Now send which animation is playing
    int animID = 0;
    if(current == moveLeft)
      animID = Player.MOVELEFT;
    if(current == moveRight)
      animID = Player.MOVERIGHT;
    if(current == idleLeft)
      animID = Player.IDLELEFT;
    if(current == idleRight)
      animID = Player.IDLERIGHT;
    data = data.concat(animID + ";");

    return data;
  }

  /***
   * Special function for setting the animation of the enemy when reading render data for p2. Only use in
   * this situation
   *
   * @param id
   *  ID of the animation to play. Use the static ids from Player:
   *    Player.MOVE_LEFT, Player.MOVE_RIGHT, etc.
   */
  public void setAnimation(int id) {
    removeAnimation(current);
    if(id == Player.MOVELEFT) {
      addAnimation(moveLeft);
      current = moveLeft;
    }
    else if(id == Player.MOVERIGHT) {
      addAnimation(moveRight);
      current = moveRight;
    }
    else if(id == Player.IDLELEFT) {
      addAnimation(idleLeft);
      current = idleLeft;
    }
    else if(id == Player.IDLERIGHT) {
      addAnimation(idleRight);
      current = idleRight;
    }
  }

  /***
   * Static method to be called when setting enemies for levels. BUILD HERE WHEN SETTING ENEMIES SO THAT PLAYER 2
   * CAN ACCESS THIS METHOD AND BUILD AN IDENTICAL ENEMY LIST.
   *  Id of the level from which we build the list of enemies we can get from MapUtil.
   * @return
   *  An ArrayList of enemies to be used in either P1 state or in P2's dummy state. Each one should be using an
   *  identical list.
   */
  public static ArrayList<Enemy> buildEnemyList() {
    ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
    if(MapUtil.levelName == LevelName.ONE) {
      enemyList.add(new Enemy(9, 19, 1));
      enemyList.add(new Enemy(21, 7, 2));
      enemyList.add(new Enemy(50, 5, 2));
      enemyList.add(new Enemy(56,26, 1));
      enemyList.add(new Enemy(20,35, 1));
      enemyList.add(new Enemy(25,54, 1));
      enemyList.add(new Enemy(52,48, 2));
      enemyList.add(new Enemy(55,52, 1));
    }
    if(MapUtil.levelName == LevelName.TWO) {
      enemyList.add(new Enemy(10, 10, 1));
      enemyList.add(new Enemy(7,  37, 1));
      enemyList.add(new Enemy(27, 30, 2));
      enemyList.add(new Enemy(37, 22, 1));
      enemyList.add(new Enemy(39, 17, 2));
      enemyList.add(new Enemy(13, 40, 1));
      enemyList.add(new Enemy(54, 47, 1));
      enemyList.add(new Enemy(22, 57, 2));
      enemyList.add(new Enemy(52, 57, 2));
    }
    if(MapUtil.levelName == LevelName.THREE) {
      enemyList.add(new Enemy(17, 4, 1));
      enemyList.add(new Enemy(44, 4, 1));
      enemyList.add(new Enemy( 3,28, 1));
      enemyList.add(new Enemy( 7,28, 2));
      enemyList.add(new Enemy(26,28, 1));
      enemyList.add(new Enemy(28,38, 1));
      enemyList.add(new Enemy( 4,44, 2));
      enemyList.add(new Enemy(16,53, 1));
      enemyList.add(new Enemy(46,57, 2));
      enemyList.add(new Enemy(44,26, 2));
      enemyList.add(new Enemy(52,33, 1));
    }
    if(MapUtil.levelName == LevelName.NONEORTEST) {
      enemyList.add(new Enemy(10, 10, 2));
      enemyList.add(new Enemy(18, 18, 1));
      enemyList.add(new Enemy(26, 26, 1));
    }
    return enemyList;
  }
}
