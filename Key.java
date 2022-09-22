package Project2;

import jig.Entity;
import jig.ResourceManager;

import java.awt.*;
import java.util.ArrayList;

public class Key  extends Entity {

    private Coordinate worldPos;
    private TileIndex location;

    public Key(int x, int y) {
        location = new TileIndex(x,y);
        worldPos = MapUtil.convertTileToWorld(location);
        addImage(ResourceManager.getImage(DungeonGame.KEY_RSC));
    }

    // return true if the players location matches the key's location:
    public boolean playerCollision(TileIndex playerLoc) {
        return (playerLoc.x == location.x && playerLoc.y == location.y);
    }

    public TileIndex getLocation() {
        return location;
    }

    public String getData() {
      String data = "";
      data = data.concat(5 + ";");
      data = data.concat(worldPos.x + ";" + worldPos.y + ";");
      return data;
    }
}
