package Project2;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

/***
 * Description:
 *
 * Transitions From StartState
 *
 * Transitions To
 */

public class TestState extends BasicGameState {
  Player player;
  ArrayList<Projectile> projectileList;
  ArrayList<Enemy> enemyList;
  Tile tileMap[][];
  Vertex [][] path;
  int levelWidth, levelHeight, player1type, player2type;;
  boolean player1Dead, player2Dead, gameover, twoPlayer;

  MapUtil levelMap;

  @Override
  public int getID() {
    return DungeonGame.TESTSTATE;
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {
    player1type = player2type = 0;
    twoPlayer = false;
    MapUtil.setLevelName(LevelName.NONEORTEST);
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) {
    levelMap = new MapUtil();
    // See below method for details on construction.
    initializeLevel(1);
    if(player1type == 0)
      player1type = 1;
    player1Dead = player2Dead = gameover = false;
    projectileList = new ArrayList<Projectile>();
    enemyList = new ArrayList<Enemy>();
    enemyList.add(new Enemy(MapUtil.TILESIZE * 18, MapUtil.TILESIZE * 5, 2));
    enemyList.add(new Enemy(MapUtil.TILESIZE * 15, MapUtil.TILESIZE * 6, 1));
    player = new Player((MapUtil.TILESIZE * 4) + (0.5f * MapUtil.TILESIZE),
            (MapUtil.TILESIZE * 4) + (0.5f * MapUtil.TILESIZE), player1type);
    container.setSoundOn(true);
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {


    // Render tiles
    for(int y = 0;  y < levelHeight; y++) {
      for(int x = 0; x < levelWidth; x++) {
        Tile temp = tileMap[x][y];
        // Floor tile
        if(temp.getID() == 0) {
          g.drawImage(ResourceManager.getImage(DungeonGame.MAP_FLOOR_RSC).getScaledCopy(DungeonGame.SCALE),
                  x * MapUtil.TILESIZE, y * MapUtil.TILESIZE);
        }
        // Wall tile
        else if(temp.getID() == 1) {
          g.drawImage(ResourceManager.getImage(DungeonGame.MAP_WALL_RSC).getScaledCopy(DungeonGame.SCALE),
                  x * MapUtil.TILESIZE, y * MapUtil.TILESIZE);
        }
      }
    }

    // Render Dijkstras Overlay if needed
    /***
     * Directions are listed by a integer in the overlay corresponding to the direction on the numpad
     * 2: down, 4: left, 6: right, 8: up
     * 1: down left, 3: down right, 7: up left, 9: up right
     */
    if(true) {
      if(path != null) {
        for(int x = 0; x < levelWidth; x++) {
          for(int y = 0; y < levelHeight; y++) {
            if(path[x][y].getDistance() < 1000) {
              //g.drawString("" + path[x][y].getDistance(), (x * DungeonGame.TILESIZE) + 5, (y * DungeonGame.TILESIZE) + 20);
              g.drawString("" + path[x][y].getDirection(), (x * MapUtil.TILESIZE) + 5, (y * MapUtil.TILESIZE) + 8);
            }
          }
        }
      }
    }
    // Render projectiles on the screen
    for(Projectile p : projectileList) {
      p.render(g);
    }

    // Render Entities
    for(Enemy enemy : enemyList) {
      enemy.render(g);
    }
    player.render(g);
    player.weapon.render(g);

    // Render HUD
    if(player.getPlayerType() == 1)
      g.drawImage(ResourceManager.getImage(DungeonGame.HUD_PARCHMENTRANGED_RSC), 20, 640);
    else if(player.getPlayerType() == 2)
      g.drawImage(ResourceManager.getImage(DungeonGame.HUD_PARCHMENTMELEE_RSC), 20, 640);

    g.drawImage(ResourceManager.getImage(DungeonGame.HUD_P1_RSC), 5, 640);
    g.drawImage(ResourceManager.getImage(DungeonGame.HUD_DIVIDER_RSC), 276, 640);

    // Render Left cap of health bar
    if(player.getCurrentHealth() > 0)
      g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBARL_RSC), 152, 660);
    else
      g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBARL_RSC), 152, 660);
    // Render middle of bar
    for(int i = 1; i < player.getMaxHealth(); i++) {
      if(i <= player.getCurrentHealth())
        g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBAR_RSC), 152 + (i*6), 660);
      else
        g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBAR_RSC), 152 + (i*6), 660);
    }
    // Render Right cap of health bar
    if(player.getCurrentHealth() == player.getMaxHealth())
      g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBARR_RSC), 152 + (player.getMaxHealth() * 6), 660);
    else
      g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBARR_RSC), 152 + (player.getMaxHealth() * 6), 660);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
    Input input = container.getInput();
    DungeonGame dg = (DungeonGame)game;

    // Methods called at the start of every update for usage in the loop
    TileIndex playerloc = player.getTileIndex();
    path = DungeonGame.getDijkstras(playerloc.x,playerloc.y,levelMap, path);

    // Check if gameover occured.
    if(gameover) {
      System.out.println("Gameover");
      container.exit();
    }

    /*** CONTROLS SECTION ***/
    // Left click for attacking
    if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
      System.out.println("LClick pressed");
      Projectile newProjectile = player.fire(getPlayerMouseAngle(input));
      if(newProjectile != null)
        projectileList.add(newProjectile);
      for(Projectile p: projectileList) {
        System.out.println(p);
      }
    }

    // Check diagonals first
    // W and A for Up Left
    Direction direction = Direction.NONE;
    if(input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_A)) {
      direction = Direction.UP_LEFT;
      try {
        dg.client.dataOutputStream.writeUTF("WA;" + (playerloc.x + (-0.71f * 0.25f)) + ";" + (playerloc.y + (-0.71f * 0.25f)) +  ";" + dg.client.playerID);
        dg.client.dataOutputStream.flush();
        if(dg.client.dataInputStream.readUTF().equals("A")) {
          player.moveUpLeft();
        } else {
          System.out.println("Unable to perform action:  WA");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
      // W and D for Up Right
    else if(input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_D)) {
      direction = Direction.UP_RIGHT;
      try {
        dg.client.dataOutputStream.writeUTF("WD;" + (playerloc.x + (0.71f * 0.25f)) + ";" + (playerloc.y + (-0.71f * 0.25f)) +  ";" + dg.client.playerID);
        dg.client.dataOutputStream.flush();
        if(dg.client.dataInputStream.readUTF().equals("A")) {
          player.moveUpRight();
        } else {
          System.out.println("Unable to perform action:  WD");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    // S and A for Down Left
    else if(input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_A)) {
      direction = Direction.DOWN_LEFT;
      try {
        dg.client.dataOutputStream.writeUTF("SA;" + (playerloc.x + (-0.71f * 0.25f)) + ";" + (playerloc.y + (0.71f * 0.25f)) +  ";" + dg.client.playerID);
        dg.client.dataOutputStream.flush();
        if(dg.client.dataInputStream.readUTF().equals("A")) {
          player.moveDownLeft();
        } else {
          System.out.println("Unable to perform action:  SA");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    // S and D for Down Right
    else if(input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_D)) {
      direction = Direction.DOWN_RIGHT;
      try {
        dg.client.dataOutputStream.writeUTF("SD;" + (playerloc.x + (0.71f * 0.25f)) + ";" + (playerloc.y + (0.71f * 0.25f)) +  ";" + dg.client.playerID);
        dg.client.dataOutputStream.flush();
        if(dg.client.dataInputStream.readUTF().equals("A")) {
          player.moveDownRight();
        } else {
          System.out.println("Unable to perform action:  SD");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    // W for moving up
    else if(input.isKeyDown(Input.KEY_W)) {
      direction = Direction.UP;
      try {
        dg.client.dataOutputStream.writeUTF("W;" + playerloc.x + ";" + (playerloc.y - 0.25f )+  ";" + dg.client.playerID);
        dg.client.dataOutputStream.flush();
        if(dg.client.dataInputStream.readUTF().equals("A")) {
          player.moveUp();
        } else {
          System.out.println("Unable to perform action:  W");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    // A for moving left
    else if(input.isKeyDown(Input.KEY_A)) {
      direction = Direction.LEFT;
      try {
        dg.client.dataOutputStream.writeUTF("A;" + (playerloc.x - 0.25f) + ";" + playerloc.y +  ";" + dg.client.playerID);
        dg.client.dataOutputStream.flush();
        if(dg.client.dataInputStream.readUTF().equals("A")) {
          player.moveLeft();
        } else {
          System.out.println("Unable to perform action:  A");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    // S for moving down
    else if(input.isKeyDown(Input.KEY_S)) {
      direction = Direction.DOWN;
      try {
        dg.client.dataOutputStream.writeUTF("S;" + playerloc.x + ";" + (playerloc.y + 0.25f) +  ";" + dg.client.playerID);
        dg.client.dataOutputStream.flush();
        if(dg.client.dataInputStream.readUTF().equals("A")) {
          player.moveDown();
        } else {
          System.out.println("Unable to perform action:  S");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    // D for moving right
    else if(input.isKeyDown(Input.KEY_D)) {
      direction = Direction.RIGHT;
      try {
        dg.client.dataOutputStream.writeUTF("D;" + (playerloc.x + 0.25f) + ";" + playerloc.y +  ";" + dg.client.playerID);
        dg.client.dataOutputStream.flush();
        if(dg.client.dataInputStream.readUTF().equals("A")) {
          player.moveRight();
        } else {
          System.out.println("Unable to perform action:  D");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    if(direction != Direction.NONE && player.isMoveValid(direction, player.getVelocity().scale(delta),levelMap)){
    }
    else {
      player.stop();
    }

    // Update the player model
    player.mouseRotate(getPlayerMouseAngle(input));
    player.update(delta);

    // Now offset if were near a wall so no in the wall happens
    //player.offsetUpdate(levelMap);

    levelMap.updateCamera(player.prevMoveVelocity);

    // Update projectiles
    for(Projectile p : projectileList) {
      p.update(delta);
    }

    // Update All enemies
    for(Enemy enemy : enemyList) {
      enemy.makeMove(tileMap, path, player, projectileList, delta);
      enemy.update(delta);
      //   enemy.offsetUpdate(levelMap);
    }

    // Update the player model
    player.mouseRotate(getPlayerMouseAngle(input));
    player.update(delta);

    // Now offset if were near a wall so no in the wall happens
    // player.offsetUpdate(levelMap);

    // Update projectiles
    for(Projectile p : projectileList) {
      p.update(delta);
    }

    // Collision check for projectiles
    for(Projectile projectile : projectileList) {
      projectile.collisionCheck(tileMap, enemyList, player);
    }

    // Check if players have died.
    if(player.getCurrentHealth() <= 0) {
      gameover = true;
    }

    // Remove Projetiles that have collided with objects.
    projectileList.removeIf( (Projectile projectile) -> projectile.needsRemove());
    // Remove enemies that have died.
    enemyList.removeIf( (Enemy enemy) -> enemy.isDead());
  }

  public double getPlayerMouseAngle(Input input) {
    float mousex = input.getMouseX();
    float mousey = input.getMouseY();
    float playerx = player.getX();
    float playery = player.getY();
    Vector angleVector = new Vector(mousex - playerx, mousey - playery);
    return angleVector.getRotation();
  }

  public void setPlayerType(int id) {
    player1type = id;
  }

  public void set2Player(boolean status) {
    twoPlayer = status;
    if(player1type == 1)
      player2type = 2;
    else
      player2type = 1;
  }

  /***
   * Left this map here incase of further testing for non scrolling:
   * Internal function for construction of levels based on idd.
   * @param level
   *  level number for which to construct.
   */
  private void initializeLevel(int level) {
    levelWidth = 20;
    levelHeight = 20;
    tileMap = DungeonGame.getTileMap(
            "11111111111111111111" +
                    "10000000110000000001" +
                    "10000000110000000001" +
                    "10000000110000000001" +
                    "10000000110000000001" +
                    "10000000110000000001" +
                    "10000000110000000001" +
                    "10000011111100000001" +
                    "10000011111100000001" +
                    "10000000000000000001" +
                    "10000000000000000001" +
                    "10000011111100000001" +
                    "10000011111100000001" +
                    "10000000110000000001" +
                    "10000000110000000001" +
                    "10000011111100000001" +
                    "10000011111100000001" +
                    "10000000000000000001" +
                    "10000000000000000001" +
                    "11111111111111111111",
            levelWidth,levelHeight);
  }
}

