package Project2;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;


import java.util.ArrayList;
import java.util.LinkedList;


/***
 * Enitity class for reprsenting projectiles. Tied to an ID:
 *  ID = 1: Ranged Player projectile
 *  ID = 2: Ranged Enemy projectile
 * Methods:
 *
 */

public class Projectile extends Entity {
  private float speed;
  private Vector velocity;
  private boolean removeMe;
  private int slashTimer;
  private double rotationAngle;
  int id, damage;
  Coordinate worldPos;

  /***
   * Constructor
   * @param x
   *  x location to spawn the projectile
   * @param y
   *  y location to spawn the projectile
   * @param type
   *  projectile type:
   *    type 1: Ranged Player projectile
   * @param angle
   *  Angle at which the projectile is fire in.
   */
  public Projectile (final float x, final float y, int type, double angle, int damageAmount) {
    super(x,y);
    id = type;
    damage = damageAmount;
    rotationAngle = angle;
    // If ranged player projectile
    if(id == 1) {
      speed = 1f;
      addImageWithBoundingBox(ResourceManager.getImage(DungeonGame.PLAYER_RANGEDARROW1_RSC));
    }
    // If enemy Projectile
    else if(id == 2) {
      speed = 0.5f;
      addImageWithBoundingBox(ResourceManager.getImage(DungeonGame.PLAYER_PROJECTILE_RSC));
    }
    // If melee player "projectile"
    else if(id == 3) {
      speed = 0.3f;
      slashTimer = 250;
      addImageWithBoundingBox(ResourceManager.getImage(DungeonGame.PLAYER_MELEESLASH_RSC));
    }

    velocity = new Vector(speed,0);
    velocity = velocity.setRotation(angle);
    setRotation(angle);
    removeMe = false;
  }


  /**
   * This function is for collision checking the projectile, should be called every update.
   * Marks the removeMe field if the projectile needs to be removed.
   * @param tilemap
   *  The tilemap representing the layout of the map.
   */
  public void collisionCheck(Tile[][] tilemap, ArrayList<Enemy> enemyList, Player player) {
    TileIndex location = MapUtil.convertWorldToTile(worldPos);
    if(location.x < 0 || location.y < 0 || location.x >= 60 || location.y >=60){
      removeMe = true;
      return;
    }
    // Always do a wall check
    if(tilemap[location.x][location.y].getID() == 1) {
      removeMe = true;
    }
    // If we're a player projectile, do an enemy check
    if(id == 1 || id == 3) {
      for(Enemy enemy : enemyList) {
        TileIndex enemyLocation = enemy.getLocation();
        if(enemyLocation.x == location.x && enemyLocation.y == location.y && !removeMe) {
          removeMe = true;
          enemy.damage(damage);
          break;
        }
      }
    }
    // If we're an enemy projectile, do a player check
    if(id == 2) {
      TileIndex playerLocation = player.getTileIndex();
      if(playerLocation.x == location.x && playerLocation.y == location.y) {
        player.damage(damage);
        removeMe = true;
      }
    }
  }

  /**
   * This function is for getting the coordinate of the player.
   * @return
   * A Coordinate object with an x and y field representing the location in the tilemap the player currently exists in.
   */
  public TileIndex getLocation() {
    return MapUtil.convertWorldToTile(worldPos);
  }


  public void update(final int delta) {
    // Melee projectiles slashes are timed, and go away after an amount of time.
    if(id == 3) {
      slashTimer -= delta;
      if(slashTimer <= 0) {
        removeMe = true;
      }
    }
    Vector movement = velocity.scale(delta);
    worldPos.x += movement.getX();
    worldPos.y += movement.getY();
  }

  public boolean needsRemove() {return removeMe;}

  public int getID() { return id;}

  /***
   * This function builds a string to send to the dummy client second player during online play. The string is in
   * the format: 'PROJECTILETYPE;PROJECTILEXPOS;PROJECTILEYPOS;PROJECTILEANGLE;'
   * @return
   * String to send across for 2P game.
   */
  public String getData() {
    String data = "";
    // Identifier for what projectile this is
    data = data.concat(id + ";");
    // Positional data
    data = data.concat(worldPos.x + ";" + worldPos.y + ";");
    // Angle of projectile
    data = data.concat(rotationAngle + ";");
    return data;
  }
}
