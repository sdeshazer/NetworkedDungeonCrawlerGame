package Project2;

import jig.Entity;
import jig.ResourceManager;

/***
 * Dummy Object for use in the dummy client. All object lists which are constructed each update and have no animations
 * can be represented as a DummyObject since processing and animation tracking isn't necessary.
 * Current type: in format (Type, id)
 *  1: Projectile
 *      1:Ranged Player
 *      2:Enemy
 *      3:Melee Slash
 *  2: Powerup
 *      1:Health Potion.
 */
public class DummyObject extends Entity {
    Coordinate worldPos;

    public DummyObject(int type, int id, final float x, final float y, double angle) {
        super(x,y);
        // If projectile
        if(type == 1) {
            // If ranged player projectile
            if(id == 1) {
                addImageWithBoundingBox(ResourceManager.getImage(DungeonGame.PLAYER_RANGEDARROW1_RSC));
            }
            // If enemy Projectile
            else if(id == 2) {
                addImageWithBoundingBox(ResourceManager.getImage(DungeonGame.PLAYER_PROJECTILE_RSC));
            }
            // If melee player "projectile"
            else if(id == 3) {
                addImageWithBoundingBox(ResourceManager.getImage(DungeonGame.PLAYER_MELEESLASH_RSC));
            }
        }
        // If its a power up
        else if(type == 2) {
            if(id == 1)
                addImage(ResourceManager.getImage(DungeonGame.POWERUP_HEALTHPOTION_RSC));
            else if(id == 2)
              addImage(ResourceManager.getImage(DungeonGame.POWERUP_SELFREVIVE_RSC));
            else if(id == 3)
              addImage(ResourceManager.getImage(DungeonGame.POWERUP_INVINCIBILITY_RSC));
            else if(id == 4)
              addImage(ResourceManager.getImage(DungeonGame.POWERUP_DOUBLESTRENGTH_RSC));
            else if(id == 5)
              addImage(ResourceManager.getImage(DungeonGame.KEY_RSC));
        }
        worldPos = new Coordinate(x,y);
        setRotation(angle);
    }
}
