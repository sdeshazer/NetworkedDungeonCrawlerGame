package Project2;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;

/**
 *
 *
 * Description:
 *
 * Controls:
 *
 * States:
 *
 * Graphical Asset Credits:
 *
 * Sound Credits:
 *
 * @author
 *
 */

public class DungeonGame extends StateBasedGame {
  // States
  public static final int STARTUPSTATE = 0;
  public static final int TESTSTATE = 1;
  public static final int LEVEL1 = 2;
  //public static final int LEVEL2 = 5;
  public static final int MENUSTATE = 3;
  public static final int DUMMYSTATE = 4;
  public static final int GAMEOVER = 6;
  public static final int TRANSITION = 7;
  public static final int WIN = 8;
  // Important parameters
  public static final int SCALE = 1;

  /*** ASSET PATHS ***/
  // Player
  public static final String PLAYER_RANGEDIDLELEFT_RSC = "Project2/Assets/player/playerRangedIdleLeft.png";
  public static final String PLAYER_RANGEDIDLERIGHT_RSC = "Project2/Assets/player/playerRangedIdleRight.png";
  public static final String PLAYER_RANGEDMOVELEFT_RSC = "Project2/Assets/player/playerRangedMoveLeft.png";
  public static final String PLAYER_RANGEDMOVERIGHT_RSC = "Project2/Assets/player/playerRangedMoveRight.png";
  public static final String PLAYER_RANGEDBOW1_RSC = "Project2/Assets/player/playerRangedBow1.png";
  public static final String PLAYER_RANGEDARROW1_RSC = "Project2/Assets/player/playerRangedArrow1.png";

  public static final String PLAYER_MELEEIDLELEFT_RSC = "Project2/Assets/player/playerMeleeIdleLeft.png";
  public static final String PLAYER_MELEEIDLERIGHT_RSC = "Project2/Assets/player/playerMeleeIdleRight.png";
  public static final String PLAYER_MELEEMOVELEFT_RSC = "Project2/Assets/player/playerMeleeMoveLeft.png";
  public static final String PLAYER_MELEEMOVERIGHT_RSC = "Project2/Assets/player/playerMeleeMoveRight.png";
  public static final String PLAYER_MELEESWORD1_RSC = "Project2/Assets/player/playerMeleeSword1.png";
  public static final String PLAYER_MELEESLASH_RSC = "Project2/Assets/player/slash.png";

  // Enemy
  public static final String ENEMY_MELEEIDLELEFT_RSC = "Project2/Assets/enemy/enemyMeleeIdleLeft.png";
  public static final String ENEMY_MELEEIDLERIGHT_RSC = "Project2/Assets/enemy/enemyMeleeIdleRight.png";
  public static final String ENEMY_MELEEMOVELEFT_RSC = "Project2/Assets/enemy/enemyMeleeMoveLeft.png";
  public static final String ENEMY_MELEEMOVERIGHT_RSC = "Project2/Assets/enemy/enemyMeleeMoveRight.png";

  public static final String ENEMY_RANGEDIDLELEFT_RSC = "Project2/Assets/enemy/enemyRangedIdleLeft.png";
  public static final String ENEMY_RANGEDIDLERIGHT_RSC = "Project2/Assets/enemy/enemyRangedIdleRight.png";
  public static final String ENEMY_RANGEDMOVELEFT_RSC = "Project2/Assets/enemy/enemyRangedMoveLeft.png";
  public static final String ENEMY_RANGEDMOVERIGHT_RSC = "Project2/Assets/enemy/enemyRangedMoveRight.png";
  public static final String ENEMY_RANGEDPROJECTILE_RSC = "Project2/Assets/enemy/enemyRangedProjectile.png";

  // HUD
  public static final String HUD_GBAR_RSC = "Project2/Assets/hud/barGreen.png";
  public static final String HUD_GBARL_RSC = "Project2/Assets/hud/barGreenCapL.png";
  public static final String HUD_GBARR_RSC = "Project2/Assets/hud/barGreenCapR.png";
  public static final String HUD_RBAR_RSC = "Project2/Assets/hud/barRed.png";
  public static final String HUD_RBARL_RSC = "Project2/Assets/hud/barRedCapL.png";
  public static final String HUD_RBARR_RSC = "Project2/Assets/hud/barRedCapR.png";
  public static final String HUD_PBAR_RSC = "Project2/Assets/hud/barPurple.png";
  public static final String HUD_PBARL_RSC = "Project2/Assets/hud/barPurpleCapL.png";
  public static final String HUD_PBARR_RSC = "Project2/Assets/hud/barPurpleCapR.png";
  public static final String HUD_WBAR_RSC = "Project2/Assets/hud/barWhite.png";
  public static final String HUD_WBARL_RSC = "Project2/Assets/hud/barWhiteCapL.png";
  public static final String HUD_WBARR_RSC = "Project2/Assets/hud/barWhiteCapR.png";

  public static final String HUD_PARCHMENTMELEE_RSC = "Project2/Assets/hud/parchmentHUDMelee.png";
  public static final String HUD_PARCHMENTRANGED_RSC = "Project2/Assets/hud/parchmentHUDRanged.png";
  public static final String HUD_P1_RSC = "Project2/Assets/hud/p1.png";
  public static final String HUD_P2_RSC = "Project2/Assets/hud/p2.png";
  public static final String HUD_DIVIDER_RSC = "Project2/Assets/hud/hudDivider.png";
  public static final String HUD_BG_RSC = "Project2/Assets/hud/hudBG.png";

  // Menu
  public static final String MENU_1P_RSC = "Project2/Assets/menu/1player.png";
  public static final String MENU_2P_RSC = "Project2/Assets/menu/2player.png";
  public static final String MENU_BACK_RSC = "Project2/Assets/menu/back.png";
  public static final String MENU_CHARACTERSELECT_RSC = "Project2/Assets/menu/characterSelect.png";
  public static final String MENU_HOWTOPLAY_RSC = "Project2/Assets/menu/howToPlay.png";
  public static final String MENU_JOIN_RSC = "Project2/Assets/menu/join.png";
  public static final String MENU_MELEEICON_RSC = "Project2/Assets/menu/meleeIcon.png";
  public static final String MENU_PLAY_RSC = "Project2/Assets/menu/play.png";
  public static final String MENU_RANGEDICON_RSC = "Project2/Assets/menu/rangedIcon.png";
  public static final String MENU_SELECTOR_RSC = "Project2/Assets/menu/selector.png";
  public static final String MENU_TITLE_RSC = "Project2/Assets/menu/titleCard.png";
  public static final String MENU_WAIT_RSC = "Project2/Assets/menu/waiting.png";
  public static final String MENU_FOUND_RSC = "Project2/Assets/menu/found.png";
  public static final String MENU_ERROR_RSC = "Project2/Assets/menu/error.png";
  public static final String MENU_WAITHOST_RSC = "Project2/Assets/menu/waitinghost.png";
  public static final String MENU_DRUM_RSC = "Project2/Assets/menu/drum.png";
  public static final String MENU_GAMEOVERBG_RSC = "Project2/Assets/menu/gameoverbg.png";
  public static final String MENU_HOWTOBG_RSC = "Project2/Assets/menu/howToPlayBG.png";
  public static final String MENU_L1_RSC = "Project2/Assets/menu/level1.png";
  public static final String MENU_L2_RSC = "Project2/Assets/menu/level2.png";
  public static final String MENU_L3_RSC = "Project2/Assets/menu/level3.png";
  public static final String MENU_WINBG_RSC = "Project2/Assets/menu/winbg.png";

  // Other
  public static final String POWERUP_HEALTHPOTION_RSC = "Project2/Assets/powerups/healthPotion.png";
  public static final String PLAYER_ARROWTEST_RSC = "Project2/Assets/arrow.png";
  public static final String PLAYER_PROJECTILE_RSC = "Project2/Assets/projectile.png";
  public static final String POWERUP_SELFREVIVE_RSC = "Project2/Assets/powerups/watermelon.png";
  public static final String POWERUP_INVINCIBILITY_RSC = "Project2/Assets/powerups/shield_02.png";
  public static final String POWERUP_DOUBLESTRENGTH_RSC = "Project2/Assets/powerups/glowing_dust.png";
  public static final String KEY_RSC = "Project2/Assets/key.png";

  // Map - LEVEL 1:
  public static final String MAP_WALL_RSC = "Project2/Assets/level1/wall.png";
  public static final String MAP_FLOOR_RSC = "Project2/Assets/level1/floor.png";
  public static final String MAP_WALL_WITH_TORCH_RSC = "Project2/Assets/level1/wall_torch.png";
  public static final String MAP_DOOR_RSC = "Project2/Assets/level1/door.png";

  // Map - LEVEL 2:
  public static final String MAP2_WALL_RSC = "Project2/Assets/level2/wall2.png";
  public static final String MAP2_FLOOR_RSC = "Project2/Assets/level2/floor2.png";
  public static final String MAP2_WALL_WITH_TORCH_RSC = "Project2/Assets/level2/wall2_torch.png";
  public static final String MAP2_DOOR_RSC = "Project2/Assets/level2/door2.png";

  // Map - LEVEL 3:
  public static final String MAP3_WALL_RSC = "Project2/Assets/level3/wall3.png";
  public static final String MAP3_FLOOR_RSC = "Project2/Assets/level3/floor3.png";
  public static final String MAP3_LAVA_RSC = "Project2/Assets/level3/lava.png";
  public static final String MAP3_DOOR_RSC = "Project2/Assets/level3/door3.png";


  // Parameters
  public final int ScreenWidth;
  public final int ScreenHeight;

  public static Client client;
  /**
   * Create a new state based game
   *
   * @param title The name of the game
   */
  public DungeonGame(String title, int width, int height) throws IOException {
    super(title);
    ScreenHeight = height;
    ScreenWidth = width;
    Entity.setCoarseGrainedCollisionBoundary(Entity.CIRCLE);
  }

  @Override
  public void initStatesList(GameContainer container) throws SlickException {
    // Load states
    addState(new StartState());
    //addState(new TestState());
    addState(new TransitionState());
    addState(new Level1());
    addState(new MenuState());
    addState(new DummyState());
    addState(new GameOver());
    addState(new WinState());
    /*** RESOURCE LOADING ***/
    // Player
    ResourceManager.loadImage(PLAYER_RANGEDARROW1_RSC);
    ResourceManager.loadImage(PLAYER_RANGEDBOW1_RSC);
    ResourceManager.loadImage(PLAYER_RANGEDIDLELEFT_RSC);
    ResourceManager.loadImage(PLAYER_RANGEDIDLERIGHT_RSC);
    ResourceManager.loadImage(PLAYER_RANGEDMOVELEFT_RSC);
    ResourceManager.loadImage(PLAYER_RANGEDMOVERIGHT_RSC);

    ResourceManager.loadImage(PLAYER_MELEEIDLELEFT_RSC);
    ResourceManager.loadImage(PLAYER_MELEEIDLERIGHT_RSC);
    ResourceManager.loadImage(PLAYER_MELEEMOVELEFT_RSC);
    ResourceManager.loadImage(PLAYER_MELEEMOVERIGHT_RSC);
    ResourceManager.loadImage(PLAYER_MELEESLASH_RSC);
    ResourceManager.loadImage(PLAYER_MELEESWORD1_RSC);

    // Enemy
    ResourceManager.loadImage(ENEMY_MELEEIDLELEFT_RSC);
    ResourceManager.loadImage(ENEMY_MELEEIDLERIGHT_RSC);
    ResourceManager.loadImage(ENEMY_MELEEMOVELEFT_RSC);
    ResourceManager.loadImage(ENEMY_MELEEMOVERIGHT_RSC);

    ResourceManager.loadImage(ENEMY_RANGEDIDLELEFT_RSC);
    ResourceManager.loadImage(ENEMY_RANGEDIDLERIGHT_RSC);
    ResourceManager.loadImage(ENEMY_RANGEDMOVELEFT_RSC);
    ResourceManager.loadImage(ENEMY_RANGEDMOVERIGHT_RSC);
    ResourceManager.loadImage(ENEMY_RANGEDPROJECTILE_RSC);

    // HUD
    ResourceManager.loadImage(HUD_GBAR_RSC);
    ResourceManager.loadImage(HUD_GBARL_RSC);
    ResourceManager.loadImage(HUD_GBARR_RSC);
    ResourceManager.loadImage(HUD_RBAR_RSC);
    ResourceManager.loadImage(HUD_RBARL_RSC);
    ResourceManager.loadImage(HUD_RBARR_RSC);
    ResourceManager.loadImage(HUD_PBAR_RSC);
    ResourceManager.loadImage(HUD_PBARL_RSC);
    ResourceManager.loadImage(HUD_PBARR_RSC);
    ResourceManager.loadImage(HUD_WBAR_RSC);
    ResourceManager.loadImage(HUD_WBARL_RSC);
    ResourceManager.loadImage(HUD_WBARR_RSC);
    ResourceManager.loadImage(HUD_BG_RSC);

    ResourceManager.loadImage(HUD_PARCHMENTMELEE_RSC);
    ResourceManager.loadImage(HUD_PARCHMENTRANGED_RSC);
    ResourceManager.loadImage(HUD_P1_RSC);
    ResourceManager.loadImage(HUD_P2_RSC);
    ResourceManager.loadImage(HUD_DIVIDER_RSC);

    // Menu
    ResourceManager.loadImage(MENU_1P_RSC);
    ResourceManager.loadImage(MENU_2P_RSC);
    ResourceManager.loadImage(MENU_BACK_RSC);
    ResourceManager.loadImage(MENU_CHARACTERSELECT_RSC);
    ResourceManager.loadImage(MENU_HOWTOPLAY_RSC);
    ResourceManager.loadImage(MENU_MELEEICON_RSC);
    ResourceManager.loadImage(MENU_PLAY_RSC);
    ResourceManager.loadImage(MENU_RANGEDICON_RSC);
    ResourceManager.loadImage(MENU_TITLE_RSC);
    ResourceManager.loadImage(MENU_SELECTOR_RSC);
    ResourceManager.loadImage(MENU_JOIN_RSC);
    ResourceManager.loadImage(MENU_WAIT_RSC);
    ResourceManager.loadImage(MENU_FOUND_RSC);
    ResourceManager.loadImage(MENU_ERROR_RSC);
    ResourceManager.loadImage(MENU_WAITHOST_RSC);
    ResourceManager.loadImage(MENU_DRUM_RSC);
    ResourceManager.loadImage(MENU_GAMEOVERBG_RSC);
    ResourceManager.loadImage(MENU_HOWTOBG_RSC);
    ResourceManager.loadImage(MENU_L1_RSC);
    ResourceManager.loadImage(MENU_L2_RSC);
    ResourceManager.loadImage(MENU_L3_RSC);
    ResourceManager.loadImage(MENU_WINBG_RSC);

    // Other
    ResourceManager.loadImage(POWERUP_HEALTHPOTION_RSC);
    ResourceManager.loadImage(PLAYER_ARROWTEST_RSC);
    ResourceManager.loadImage(PLAYER_PROJECTILE_RSC);
    ResourceManager.loadImage(POWERUP_SELFREVIVE_RSC);
    ResourceManager.loadImage(POWERUP_INVINCIBILITY_RSC);
    ResourceManager.loadImage(POWERUP_DOUBLESTRENGTH_RSC);

    // Map - level 1
    ResourceManager.loadImage(MAP_FLOOR_RSC);
    ResourceManager.loadImage(MAP_WALL_RSC);
    ResourceManager.loadImage(MAP_WALL_WITH_TORCH_RSC);
    ResourceManager.loadImage(MAP_DOOR_RSC);

    // Map - level 2
    ResourceManager.loadImage(MAP2_FLOOR_RSC);
    ResourceManager.loadImage(MAP2_WALL_RSC);
    ResourceManager.loadImage(MAP2_WALL_WITH_TORCH_RSC);
    ResourceManager.loadImage(MAP2_DOOR_RSC);

    // Map - level 3
    ResourceManager.loadImage(MAP3_FLOOR_RSC);
    ResourceManager.loadImage(MAP3_WALL_RSC);
    ResourceManager.loadImage(MAP3_LAVA_RSC);
    ResourceManager.loadImage(MAP3_DOOR_RSC);

    //KEY
    ResourceManager.loadImage(KEY_RSC);

  }

  public static void main(String[] args) {
    AppGameContainer app;
    // Open the game
    try {
      app = new AppGameContainer(new DungeonGame("Project2", 1000,1000));
      app.setDisplayMode(640,736, false);
      app.setVSync(true);
      app.setTargetFrameRate(60);
      app.start();
    } catch(SlickException | IOException e) {
      e.printStackTrace();
    }
  }

  /***
   * This function builds a tilemap from a given string which represents the level layout. The string is one long
   * uninterrupted string of 100 characters to build a 10x10 map. The String consists of integers which are IDs for
   * what type each space is. Specifically only works for 100 character strings to build 10x10s. DO NOT GIVE OTHER
   * STINGS.
   * Note: This method needs to be modified if different sized maps are made.
   * @param map
   *  The string for building the level
   * @return
   *  A finished tile map (2D tile array) of constructed tiles
   */
  public static Tile[][] getTileMap(String map, int width, int height) {
    // Check if an invalid string to build was given.
    if(width * height != map.length()) {
      System.out.println("String length did not match map dimensions");
      return null;
    }
    Tile tileMap[][] = new Tile[width][height];
    char tempMap[] = map.toCharArray();
    int x = 0, y = 0, i = 0;
    for(char current : tempMap) {
      if(x == height) {
        x = 0;
        y++;
      }
      tileMap[x][y] = new Tile(Character.getNumericValue(tempMap[i]));
      i++;
      x++;
    }
    return tileMap;
  }


  /**
   * Dijkstras Algorithm for a 10x10 map of Tile objects. Will build a 2D Vertex array to be used for pathfinding for
   * enemies in the game. Only works for 10x10 maps currently
   * Note: If different maps sizes are ever used, this function needs to be modified.
   */
  public static Vertex[][] getDijkstras(int sourcex, int sourcey, MapUtil levelMap, Vertex[][] oldPath) {
    int width = MapUtil.LEVELWIDTH;
    int height = MapUtil.LEVELHEIGHT;
    int leftBound, rightBound, bottomBound, topBound;
    Tile[][] tilemap = levelMap.currentTileMap;
    Vertex path[][];
    // We need to flood the whole board if the oldPath doesn't exist.
    if(oldPath == null){
      path = new Vertex[width][height];
      leftBound = bottomBound = 0;
      rightBound = width;
      topBound = height;
    }
    // Otherwise we just need to flood a 26 x 26 area around the source to reduce the amount of calls needed.
    else {
      path = oldPath;
      // Check if were near the borders when setting bounds.
      if(sourcex <= 13) {
        leftBound = 0;
        rightBound = 26;
      }
      else if(sourcex >= 47) {
        leftBound = 34;
        rightBound = 60;
      }
      else {
        leftBound = sourcex - 13;
        rightBound = sourcex + 13;
      }
      // Now set the vertical boundaries.
      if(sourcey <= 13) {
        bottomBound = 0;
        topBound = 26;
      }
      else if(sourcey >= 47) {
        bottomBound = 34;
        topBound = 60;
      }
      else {
        bottomBound = sourcey - 13;
        topBound = sourcey + 13;
      }
    }
    boolean seen[][] = new boolean[width][height];

    // Intialize the path and seen arrays
    for(int x = leftBound; x < rightBound; x++) {
      for(int y = bottomBound; y < topBound; y++) {
        path[x][y] = new Vertex(levelMap.currentTileMap[x][y].getCost());
        seen[x][y] = false;
      }
    }

    // Set the source distance to 0
    path[sourcex][sourcey].setDistance(0);

    // Keep going until all nodes are seen
    while(hasUnseenNodes(seen, rightBound, topBound, leftBound, bottomBound)) {
      // Get the node with the current shortest distance
      TileIndex current = shortestDistance(path, seen, rightBound, topBound, leftBound, bottomBound);
      int x = current.x;
      int y = current.y;
      // Mark the current node as seen.
      seen[x][y] = true;
      double compare;
      double currentDist = path[x][y].getDistance();

      // Now update tile distances that are adjacent if the distance is shorter then currently recorded.
      // Tile below
      if(y > 0) {
        compare = currentDist + path[x][y-1].getCost();
        if(path[x][y-1].getDistance() > compare) {
          path[x][y-1].setDistance(compare);
          path[x][y-1].setDirection(2);
        }
      }
      // Tile above
      if(y < height - 1) {
        compare = currentDist + path[x][y+1].getCost();
        if(path[x][y+1].getDistance() > compare) {
          path[x][y+1].setDistance(compare);
          path[x][y+1].setDirection(8);
        }
      }
      // Tile right
      if(x > 0) {
        compare = currentDist + path[x-1][y].getCost();
        if(path[x-1][y].getDistance() > compare) {
          path[x-1][y].setDistance(compare);
          path[x-1][y].setDirection(6);
        }
      }
      // Tile left
      if(x < width - 1) {
        compare = currentDist + path[x+1][y].getCost();
        if(path[x+1][y].getDistance() > compare) {
          path[x+1][y].setDistance(compare);
          path[x+1][y].setDirection(4);
        }
      }
      // Tile down right
      if(x > 0 && y > 0 && tilemap[x-1][y].getID() == 0 && tilemap[x][y-1].getID() == 0) {
          compare = currentDist + (path[x-1][y-1].getCost() * Math.sqrt(2));
          if(path[x-1][y-1].getDistance() > compare) {
            path[x-1][y-1].setDistance(compare);
            path[x-1][y-1].setDirection(3);
          }
      }
      // Tile down left
      if(x < width - 1 && y > 0 && tilemap[x+1][y].getID() == 0 && tilemap[x][y-1].getID() == 0) {
        compare = currentDist + (path[x+1][y-1].getCost() * Math.sqrt(2));
        if(path[x+1][y-1].getDistance() > compare) {
          path[x+1][y-1].setDistance(compare);
          path[x+1][y-1].setDirection(1);
        }
      }
      // Tile up right
      if(x > 0 && y < height - 1 && tilemap[x-1][y].getID() == 0 && tilemap[x][y+1].getID() == 0) {
        compare = currentDist + (path[x-1][y+1].getCost() * Math.sqrt(2));
        if(path[x-1][y+1].getDistance() > compare) {
          path[x-1][y+1].setDistance(compare);
          path[x-1][y+1].setDirection(9);
        }
      }
      // Tile up left
      if(x < width - 1 && y < height - 1 && tilemap[x+1][y].getID() == 0 && tilemap[x][y+1].getID() == 0) {
        compare = currentDist + (path[x+1][y+1].getCost() * Math.sqrt(2));
        if(path[x+1][y+1].getDistance() > compare) {
          path[x+1][y+1].setDistance(compare);
          path[x+1][y+1].setDirection(7);
        }
      }
    }
    return path;
  }

  /***
   * Sub method for getDijkstras(). Checks the seen array if there are unseen nodes.
   * @param seen
   *  2D array of booleans showing all the currently seen nodes
   * @return
   *  true if there are unseen nodes
   *  false if there aren't
   */
  private static boolean hasUnseenNodes(boolean seen[][], int width, int height, int leftBound, int bottomBound) {
    for (int x = leftBound; x < width; x++) {
      for (int y = bottomBound; y < height; y++) {
        if (!seen[x][y]) {
          return true;
        }
      }
    }
    return false;
  }

  /***
   * Sub method for getDijkstras(). Searches the vertex graph for the unseen node with the lowest distance.
   * @param graph
   *  Vertex graph being searched
   * @param seen
   *  2D boolean array showing which nodes have been marked as seen
   * @return
   *  Coordinate of the node in the vertex which has the lowest distance and is unseen.
   */
  private static TileIndex shortestDistance (Vertex graph[][], boolean seen[][], int width, int height, int leftBound, int bottomBound) {
    TileIndex shortest = new TileIndex(0,0);
    double distance = 100000000;
    // Iterate through the graph and find the right node.
    for(int x = leftBound; x < width; x++) {
      for(int y = bottomBound; y < height; y++) {
        double newDistance = graph[x][y].getDistance();
        if(newDistance < distance && !seen[x][y]) {
          distance = newDistance;
          shortest.x = x;
          shortest.y = y;
        }
      }
    }
    return shortest;
  }
}
