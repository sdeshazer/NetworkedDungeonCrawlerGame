package Project2;



import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Graphics;

import java.io.*;

/**
 * MapUtil handles rendering the map, checking for which level the game is currently in
 * where level name and some util methods are static. Attributes to the map,
 * tile, world and screen conversion are here.
 */

public class MapUtil {
    private final String TAG = "MapUtil -";
    Boolean debug = false;
    //Path to the level 60x60 array:
    private final String level1Data = "Project2/src/Project2/Data/LevelOneMap.csv";
    private final String level2Data = "Project2/src/Project2/Data/LevelTwoMap.csv";
    private final String level3Data = "Project2/src/Project2/Data/LevelThreeMap.csv";
    public static final int TILESIZE = 32;
    public static final int SCREENWIDTH = 20;
    public static final int SCREENHEIGHT = 20;
    public static final int LEVELWIDTH = 60;
    public static final int LEVELHEIGHT = 60;
    public static LevelName levelName;

    //assigning id's for tiles with a name to avoid confusion:
    private static final int floorTile = 0;
    private static final int wallTile = 1;
    private static final int wallTileWithTorch = 2;
    private static final int exitDoorGoal = 6;
    // press M to enter cheat mode while in Playing state:
    // This will allow skipping of levels by pressing Q
    // and is reset to false in the starting state
    public static boolean cheatMode;



    Tile[][] currentTileMap;
    Coordinate cameraPos = new Coordinate(0, 0);
    Coordinate maxCameraPos = new Coordinate((LEVELWIDTH * TILESIZE) - (SCREENWIDTH * TILESIZE),
            (LEVELHEIGHT * TILESIZE) - (SCREENHEIGHT * TILESIZE));


    String currentMapString;
    // returns the level name of type LevelName, can be LevelName.ONE, LevelName.START, etc.
    public LevelName getCurrentLevel(){
        return levelName;
    }
    // mostly used in changing states within dungeon game:
    public static void setLevelName(LevelName name){
        levelName = name;
    }
    // loads the map csv based off the level name:
    public void loadLevelMap() throws IOException {
        switch (levelName) {
            case MENU:
                System.out.println(TAG + " Menu state");
                break;
            case ONE:
                currentMapString = readLevelCSV(level1Data);
                break;
            case TWO:
                currentMapString = readLevelCSV(level2Data);
                break;
            case THREE:
                currentMapString = readLevelCSV(level3Data);
                break;
            case GAMEOVER:
                System.out.println(TAG + " GameOver state");
                break;
            default:
                System.out.print(TAG + "error: Level and state not found or listed ");
                break;
        }
    }

    /**
     * Screen coordinates: where objects are on camera.
     * World coordinates: where objects are a separate
     *      coordinate system relative to the entire map including off camera.
     * TileIndex: the index of the tile map, a tile that a position is on.
     */
    public Coordinate convertScreenToWorld(Coordinate screenPos){
        return new Coordinate(cameraPos.x + screenPos.x, cameraPos.y + screenPos.y);
    }

    public Coordinate convertWorldToScreen(Coordinate worldPos){
        return new Coordinate(worldPos.x - cameraPos.x, worldPos.y - cameraPos.y);
    }

    public static Coordinate convertTileToWorld(TileIndex tileIndex){
        return new Coordinate((tileIndex.x * TILESIZE) + (0.5f * TILESIZE), (tileIndex.y * TILESIZE) + (0.5f * TILESIZE));
    }

    public Coordinate convertTileToScreen(TileIndex tileIndex){
        Coordinate worldPos = convertTileToWorld(tileIndex);
        return convertWorldToScreen(worldPos);
    }

    // very handy methods for handling collision checks for a tile index:
    public Boolean hasCollision(TileIndex tileIndex){
        int tileValue = currentTileMap[tileIndex.x][tileIndex.y].getID();
        // return to true if the tile is not blank and not the door tile:
        return (tileValue == wallTile || tileValue == wallTileWithTorch);
    }

    public static Boolean hasCollision(int tileValue){
        // return to true if the tile is not blank and not the door tile:
        return (tileValue == wallTile || tileValue == wallTileWithTorch);
    }

    // converting from world to tile:
    public static TileIndex convertWorldToTile(Coordinate worldPos){
        return new TileIndex((int)Math.floor(worldPos.x / TILESIZE), (int)Math.floor(worldPos.y / TILESIZE));
    }

    // convert screen to tile if needed:
    public TileIndex convertScreenToTile(Coordinate screenPos){
        Coordinate worldPos = convertScreenToWorld(screenPos);
        return convertWorldToTile(worldPos);
    }

    // offset of the camera can be in between and partially on tiles:
    public Coordinate getCameraTileOffset() {
        Coordinate cameraTilePos = new Coordinate(cameraPos.x % TILESIZE, cameraPos.y % TILESIZE);
        return cameraTilePos;
    }
    // check if
    public Boolean isAtDoor(Player player){
       TileIndex playerLoc = player.getTileIndex();
       return (currentTileMap[playerLoc.x][playerLoc.y].getID() == exitDoorGoal);
    }

    // scroll our camera based on independent player movement:
    public void updateCamera(Coordinate deltaMove) {
        cameraPos.x = deltaMove.x - (0.5f * SCREENWIDTH * TILESIZE);
        cameraPos.y = deltaMove.y - (0.5f * SCREENHEIGHT * TILESIZE);
        if (cameraPos.x < 0) {
            cameraPos.x = 0;
        } else if (cameraPos.x > maxCameraPos.x) {
            cameraPos.x = maxCameraPos.x;
        }
        if (cameraPos.y < 0) {
            cameraPos.y = 0;
        } else if (cameraPos.y > maxCameraPos.y) {
            cameraPos.y = maxCameraPos.y;
        }
    }

    private void renderCollisionTileObjects( Tile renderTile, float renderX, float renderY, Graphics g ){
        switch(levelName){
            case ONE -> { // render based on LEVEL ONE assets:
                if (renderTile.getID() == wallTile) {
                    g.drawImage(ResourceManager.getImage(DungeonGame.MAP_WALL_RSC).getScaledCopy(DungeonGame.SCALE),
                            renderX, renderY);
                }
                if (renderTile.getID() == wallTileWithTorch) {
                    g.drawImage(ResourceManager.getImage(DungeonGame.MAP_WALL_WITH_TORCH_RSC).getScaledCopy(DungeonGame.SCALE),
                            renderX, renderY);
                }
                if (renderTile.getID() == exitDoorGoal) {
                    g.drawImage(ResourceManager.getImage(DungeonGame.MAP_DOOR_RSC).getScaledCopy(DungeonGame.SCALE),
                            renderX, renderY);
                }

            }
            case TWO -> { // render based on LEVEL TWO assets:
                if (renderTile.getID() ==   wallTile) {
                    g.drawImage(ResourceManager.getImage(DungeonGame.MAP2_WALL_RSC).getScaledCopy(DungeonGame.SCALE),
                            renderX, renderY);
                }
                if (renderTile.getID() == wallTileWithTorch) {
                    g.drawImage(ResourceManager.getImage(DungeonGame.MAP2_WALL_WITH_TORCH_RSC).getScaledCopy(DungeonGame.SCALE),
                            renderX, renderY);
                }
                if (renderTile.getID() == exitDoorGoal) {
                    g.drawImage(ResourceManager.getImage(DungeonGame.MAP2_DOOR_RSC).getScaledCopy(DungeonGame.SCALE),
                            renderX, renderY);
                }
            }
            case THREE -> { // render based on LEVEL THREE assets:
                if (renderTile.getID() ==   wallTile) {
                    g.drawImage(ResourceManager.getImage(DungeonGame.MAP3_WALL_RSC).getScaledCopy(DungeonGame.SCALE),
                            renderX, renderY);
                }
                if (renderTile.getID() == wallTileWithTorch) {
                    g.drawImage(ResourceManager.getImage(DungeonGame.MAP3_LAVA_RSC).getScaledCopy(DungeonGame.SCALE),
                            renderX, renderY);
                }
                if (renderTile.getID() == exitDoorGoal) {
                    g.drawImage(ResourceManager.getImage(DungeonGame.MAP3_DOOR_RSC).getScaledCopy(DungeonGame.SCALE),
                            renderX, renderY);
                }
            }
        }
    }


    private void renderBlankTileObjects( Tile renderTile,  float renderX, float renderY, Graphics g){
        switch(levelName){
            case ONE -> { // render based on LEVEL ONE assets:
                if (renderTile.getID() == floorTile) {
                    g.drawImage(ResourceManager.getImage(DungeonGame.MAP_FLOOR_RSC).getScaledCopy(DungeonGame.SCALE),
                            renderX, renderY);

                }
            }
            case TWO -> { // render based on LEVEL TWO assets:
                if (renderTile.getID() == floorTile) {
                    g.drawImage(ResourceManager.getImage(DungeonGame.MAP2_FLOOR_RSC).getScaledCopy(DungeonGame.SCALE),
                            renderX, renderY);

                }

            }
            case THREE -> { // render based on LEVEL TWO assets:
                if (renderTile.getID() == floorTile) {
                    g.drawImage(ResourceManager.getImage(DungeonGame.MAP3_FLOOR_RSC).getScaledCopy(DungeonGame.SCALE),
                            renderX, renderY);

                }
            }
        }
    }


    public void renderMapByCamera(Graphics g) {
        TileIndex currentTile = convertWorldToTile(cameraPos);
        Coordinate cameraOffset = getCameraTileOffset();
        float maxWidth =  SCREENWIDTH * TILESIZE;
        float maxHeight = SCREENHEIGHT * TILESIZE;
        // -cameraOffset is the amount off screen to the left:
        for (float renderX = (-cameraOffset.x); renderX < maxWidth; renderX += TILESIZE, currentTile.x++) {
            int currentY = currentTile.y;
            for (float renderY = (-cameraOffset.y); renderY < maxHeight; renderY += TILESIZE, currentY++) {
                Tile renderTile = currentTileMap[currentTile.x][currentY];
                renderBlankTileObjects(renderTile, renderX, renderY, g);
                renderCollisionTileObjects(renderTile, renderX, renderY, g);
            }
        }
    }




    private String readLevelCSV(String MapData) throws IOException {
        File file = new File(MapData);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String map = "";
        String st;
        while ((st = br.readLine()) != null) {
            st = st.replaceAll(",", "");
            if (debug) {
                System.out.println(st);
            }
            map += st;
        }
        return map;
    }
}
