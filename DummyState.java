package Project2;

import jig.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;
import java.util.ArrayList;

import jig.Vector;
import org.newdawn.slick.state.transition.BlobbyTransition;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class DummyState extends BasicGameState {
  MapUtil levelMap;
  Player meleePlayer, rangedPlayer;
  ArrayList<Enemy> enemyList;
  ArrayList<DummyObject> dummyList;
  boolean firstData, p1SelfRevive, p1Invincible, p1DoubleStrength, p2SelfRevive, p2Invincible, p2DoubleStrength;
  boolean levelComplete, gameover, rangedPlayerDead, meleePlayerDead, p1key, p2key;
  int myId, p1Health, p1MaxHealth, p2Health, p2MaxHealth;

  @Override
  public int getID() {
    return 4;
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {
    levelMap = new MapUtil();
    MapUtil.setLevelName(LevelName.ONE);
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) {
    levelComplete = gameover = rangedPlayerDead = meleePlayerDead = p1key = p2key = false;
    p1SelfRevive = p1Invincible = p1DoubleStrength = p2SelfRevive = p2Invincible = p2DoubleStrength = false;
    p1Health = p1MaxHealth = p2Health = p2MaxHealth = 0;
    meleePlayer = rangedPlayer = null;
    enemyList = Enemy.buildEnemyList();
    dummyList = new ArrayList<DummyObject>();
    // parse the CSV map file, throw exception in case of IO error:
    firstData = true;
    try {
      levelMap.loadLevelMap();
    } catch (IOException e) {
      e.printStackTrace();
    }
    // keeping the string to matrix method in DungeonGame
    levelMap.currentTileMap  = DungeonGame.getTileMap(levelMap.currentMapString,
        MapUtil.LEVELWIDTH,MapUtil.LEVELWIDTH);

    container.setSoundOn(true);

    // Sanity Check with the other player to ensure we start at the same time
    try {
      String string = DungeonGame.client.dataInputStream.readUTF();
    } catch(IOException e) { e.printStackTrace();}

  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
    levelMap.renderMapByCamera(g);

    if(meleePlayer != null && !meleePlayerDead) {
      meleePlayer.render(g);
      meleePlayer.weapon.render(g);
    }
    if(rangedPlayer != null && !rangedPlayerDead) {
      rangedPlayer.render(g);
      rangedPlayer.weapon.render(g);
    }
    for(Enemy e : enemyList) {
      e.render(g);
    }
    for(DummyObject o : dummyList)
      o.render(g);

    // Render HUD
    float player2HudOffset = 342;
    g.drawImage(ResourceManager.getImage(DungeonGame.HUD_BG_RSC), 0, 640);
    if(myId == 2) {
      g.drawImage(ResourceManager.getImage(DungeonGame.HUD_PARCHMENTRANGED_RSC), 20, 640);
      g.drawImage(ResourceManager.getImage(DungeonGame.HUD_PARCHMENTMELEE_RSC), 20 + player2HudOffset, 640);

    }
    else if(myId == 1) {
      g.drawImage(ResourceManager.getImage(DungeonGame.HUD_PARCHMENTMELEE_RSC), 20, 640);
      g.drawImage(ResourceManager.getImage(DungeonGame.HUD_PARCHMENTRANGED_RSC), 20 + player2HudOffset, 640);
    }

    // Render Left cap of health bar
    if(p1Health > 0)
      g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBARL_RSC), 152, 660);
    else
      g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBARL_RSC), 152, 660);
    // Render middle of bar
    for(int i = 1; i < p1MaxHealth; i++) {
      if(i <= p1Health)
        g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBAR_RSC), 152 + (i*6), 660);
      else
        g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBAR_RSC), 152 + (i*6), 660);
    }
    // Render Right cap of health bar
    if(p1Health == p2MaxHealth)
      g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBARR_RSC).getScaledCopy(0.5f), 152 + (p1MaxHealth * 6), 660);
    else
      g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBARR_RSC).getScaledCopy(0.5f), 152 + (p1MaxHealth * 6), 660);

    // Powerups for P1
    if(p1SelfRevive) {
      g.drawImage(ResourceManager.getImage(DungeonGame.POWERUP_SELFREVIVE_RSC).getScaledCopy(0.5f), 152, 700);
    }
    if(p1Invincible) {
      g.drawImage(ResourceManager.getImage(DungeonGame.POWERUP_INVINCIBILITY_RSC).getScaledCopy(0.5f), 172, 700);
    }
    if(p1DoubleStrength) {
      g.drawImage(ResourceManager.getImage(DungeonGame.POWERUP_DOUBLESTRENGTH_RSC).getScaledCopy(0.5f), 192, 700);
    }
    if(p1key) {
      g.drawImage(ResourceManager.getImage(DungeonGame.KEY_RSC), 212, 700);
    }

    // Powerups for P2
    if(p2SelfRevive) {
      g.drawImage(ResourceManager.getImage(DungeonGame.POWERUP_SELFREVIVE_RSC).getScaledCopy(0.5f), 152 + player2HudOffset, 700);
    }
    if(p2Invincible) {
      g.drawImage(ResourceManager.getImage(DungeonGame.POWERUP_INVINCIBILITY_RSC).getScaledCopy(0.5f), 172 + player2HudOffset, 700);
    }
    if(p2DoubleStrength) {
      g.drawImage(ResourceManager.getImage(DungeonGame.POWERUP_DOUBLESTRENGTH_RSC).getScaledCopy(0.5f), 192 + player2HudOffset, 700);
    }
    if(p2key) {
      g.drawImage(ResourceManager.getImage(DungeonGame.KEY_RSC), 212  + player2HudOffset, 700);
    }

    // Render the second players health bar
      // Render Left cap of health bar
      if(p2Health > 0)
        g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBARL_RSC), 152 + player2HudOffset, 660);
      else
        g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBARL_RSC), 152 + player2HudOffset, 660);
      // Render middle of bar
      for(int i = 1; i < p2MaxHealth; i++) {
        if(i <= p2Health)
          g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBAR_RSC), 152 + (i*6) + player2HudOffset, 660);
        else
          g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBAR_RSC), 152 + (i*6) + player2HudOffset, 660);
      }
      // Render Right cap of health bar
      if(p2Health == p2MaxHealth)
        g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBARR_RSC), 152 + (p2MaxHealth * 6) + player2HudOffset, 660);
      else
        g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBARR_RSC), 152 + (p2MaxHealth * 6) + player2HudOffset, 660);

    g.drawImage(ResourceManager.getImage(DungeonGame.HUD_P1_RSC), 5, 640);
    g.drawImage(ResourceManager.getImage(DungeonGame.HUD_P2_RSC), 5 + player2HudOffset, 640);
    g.drawImage(ResourceManager.getImage(DungeonGame.HUD_DIVIDER_RSC), 276, 640);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
    // Important calls at the beginning of each update
    Input input = container.getInput();
    String data;
    String[] dataToken = null;
    // Read render data from client
    try {
      data = DungeonGame.client.dataInputStream.readUTF();
      dataToken = data.split(";");
    } catch (IOException e) {
      System.out.println("IOException from run() in ClientHandler");
      e.printStackTrace();
    }

    // If freak accidents happen or poorly timed synchronization reads, skip this loop
    if(dataToken.length < 4)
      return;

    // Use the data to set important fields in the game
    parseRenderData(dataToken);

    // Check special flags that could have been set
    if(levelComplete) {
      ((TransitionState)game.getState(DungeonGame.TRANSITION)).set2P();
      game.enterState(DungeonGame.TRANSITION, new EmptyTransition(), new BlobbyTransition());
    }
    if(gameover) {
      game.enterState(DungeonGame.GAMEOVER, new FadeOutTransition(), new FadeInTransition());
    }

    // Set screen positions for entities
    Coordinate playerScreenPos = levelMap.convertWorldToScreen(meleePlayer.worldPos);
    meleePlayer.setX(playerScreenPos.x);
    meleePlayer.setY(playerScreenPos.y);
    meleePlayer.weapon.update(playerScreenPos.x, playerScreenPos.y);
    Coordinate player2ScreenPos = levelMap.convertWorldToScreen(rangedPlayer.worldPos);
    rangedPlayer.setX(player2ScreenPos.x);
    rangedPlayer.setY(player2ScreenPos.y);
    rangedPlayer.weapon.update(player2ScreenPos.x, player2ScreenPos.y);

    // Now the enemies
    for(Enemy enemy : enemyList) {
      Coordinate enemyScreenPos = levelMap.convertWorldToScreen(enemy.worldPos);
      enemy.setX(enemyScreenPos.x);
      enemy.setY(enemyScreenPos.y);
    }

    // Now the dummy Objects
    for(DummyObject o: dummyList) {
      Coordinate screenPos = levelMap.convertWorldToScreen(o.worldPos);
      o.setX(screenPos.x);
      o.setY(screenPos.y);
    }

    // Set camera pos, we want to follow the other player if were dead
    if(myId == 1) {
      if(rangedPlayerDead)
        levelMap.updateCamera(meleePlayer.worldPos);
      else
        levelMap.updateCamera(rangedPlayer.worldPos);
    }

    else {
      if(meleePlayerDead)
        levelMap.updateCamera(rangedPlayer.worldPos);
      else
        levelMap.updateCamera(meleePlayer.worldPos);
    }


    /*** CONTROLS SECTION ***/
    data = "";
    // Left click for attacking: Send 1 for yes we attacked, and 0 for no we didn't
    if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
      data = data.concat("1;");
    else
      data = data.concat("0;");
    // Check diagonals first
    // W and A for Up Left
    if(input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_A)) {
      data = data.concat("WA;");
    }
    // W and D for Up Right
    else if(input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_D)) {
      data = data.concat("WD;");
    }
    // S and A for Down Left
    else if(input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_A)) {
      data = data.concat("SA;");

    }
    // S and D for Down Right
    else if(input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_D)) {
      data = data.concat("SD;");
    }
    // W for moving up
    else if(input.isKeyDown(Input.KEY_W)) {
      data = data.concat("W;");
    }
    // A for moving left
    else if(input.isKeyDown(Input.KEY_A)) {
      data = data.concat("A;");
    }
    // S for moving down
    else if(input.isKeyDown(Input.KEY_S)) {
      data = data.concat("S;");
    }
    // D for moving right
    else if(input.isKeyDown(Input.KEY_D)) {
      data = data.concat("D;");
    }
    // Other wise were just gonna stop moving.
    else {
      data = data.concat("NA;");
    }

    // Also we need to send mouse position data to p1
    float mousex = input.getMouseX();
    float mousey = input.getMouseY();
    float playerx, playery;
    if(myId == 1) {
      playerx = rangedPlayer.getX();
      playery = rangedPlayer.getY();
    }
    else {
      playerx = meleePlayer.getX();
      playery = meleePlayer.getY();
    }
    Vector angleVector = new Vector(mousex - playerx, mousey - playery);
    data = data.concat(angleVector.getRotation() + ";");


    // Write control data
    try {
      DungeonGame.client.dataOutputStream.writeUTF(data);
      DungeonGame.client.dataOutputStream.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void parseRenderData(String[] token) {
    // If this is our first time reading data, we need to construct players
    if(firstData) {
      firstData = false;
      // Check if p1 is melee or ranged
      if(Integer.valueOf(token[1]) == 1) {
        rangedPlayer = new Player(Float.valueOf(token[2]), Float.valueOf(token[3]), 1);
        meleePlayer = new Player(Float.valueOf(token[8]), Float.valueOf(token[9]), 2);
        myId = 2;
      }
      else {
        meleePlayer = new Player(Float.valueOf(token[2]), Float.valueOf(token[3]), 2);
        rangedPlayer = new Player(Float.valueOf(token[8]), Float.valueOf(token[9]), 1);
        myId = 1;
      }
    }
    // Set and get all player information
    if(myId == 1) {
      meleePlayer.worldPos.x = Float.valueOf(token[2]);
      meleePlayer.worldPos.y = Float.valueOf(token[3]);
      meleePlayer.mouseRotate(Double.valueOf(token[4]));
      meleePlayer.setAnimation(Integer.valueOf(token[5]));
      rangedPlayer.worldPos.x = Float.valueOf(token[8]);
      rangedPlayer.worldPos.y = Float.valueOf(token[9]);
      rangedPlayer.mouseRotate(Double.valueOf(token[10]));
      rangedPlayer.setAnimation(Integer.valueOf(token[11]));
    }
    else if(myId == 2) {
      meleePlayer.worldPos.x = Float.valueOf(token[8]);
      meleePlayer.worldPos.y = Float.valueOf(token[9]);
      meleePlayer.mouseRotate(Double.valueOf(token[10]));
      meleePlayer.setAnimation(Integer.valueOf(token[11]));
      rangedPlayer.worldPos.x = Float.valueOf(token[2]);
      rangedPlayer.worldPos.y = Float.valueOf(token[3]);
      rangedPlayer.mouseRotate(Double.valueOf(token[4]));
      rangedPlayer.setAnimation(Integer.valueOf(token[5]));
    }

    // Set all enemy Information
    int index;
    // First find the beginning of the enemyList
    for(index = 0; !token[index].equals("ENEMYLISTSTART"); index++);
    index++;
    for(int i = 0; !token[index].equals("ENEMYLISTEND"); index = index+4) {
      // First check if we need to remove this enemy
      if(token[index].equals("-1")) {
        enemyList.get(i).setDead();
        continue;
      }
      // Other wise we need to set the parameters given
      enemyList.get(i).worldPos.x = Float.valueOf(token[index+1]);
      enemyList.get(i).worldPos.y = Float.valueOf(token[index+2]);
      enemyList.get(i).setAnimation(Integer.valueOf(token[index+3]));
      i++;
    }
    // Remove enemies that should be removed
    enemyList.removeIf( (Enemy e) -> e.isDead());

    // We now need to rebuild the dummy list from scratch for rendering
    dummyList = new ArrayList<DummyObject>();
    // Set all projectile Information
    // The index should now be at the end of the enemy list so we need to move it two places
    index = index + 2;
    for( ; !token[index].equals("PROJLISTEND"); index = index + 4) {
      // Construct and add a DummyObject since these don't have animations.
      dummyList.add(new DummyObject(1, Integer.valueOf(token[index]), Float.valueOf(token[index+1]),
              Float.valueOf(token[index+2]), Float.valueOf(token[index+3])));
    }

    // Set all Powerup Information
    index = index + 2;
    for(; !token[index].equals("POWERUPLISTEND"); index = index + 3) {
      // Construct and add a DummyObject since these don't have animations.
      dummyList.add(new DummyObject(2, Integer.valueOf(token[index]), Float.valueOf(token[index+1]),
              Float.valueOf(token[index+2]), 0f));
    }

    // Set all HUD information
    index = index +2;
    // Order of Hud info comes in: p1.health, p1.maxhealth, p2.health, p2.maxhealth.
    p1Health = Integer.valueOf(token[index]);
    p1MaxHealth = Integer.valueOf(token[index+1]);
    p2Health = Integer.valueOf(token[index+2]);
    p2MaxHealth = Integer.valueOf(token[index+3]);
    p1SelfRevive = Boolean.parseBoolean(token[index+4]);
    p1Invincible = Boolean.parseBoolean(token[index+5]);
    p1DoubleStrength = Boolean.parseBoolean(token[index+6]);
    p2SelfRevive = Boolean.parseBoolean(token[index+7]);
    p2Invincible = Boolean.parseBoolean(token[index+8]);
    p2DoubleStrength = Boolean.parseBoolean(token[index+9]);
    // Handle special instructions
    index = index + 2;
    for(; !token[index].equals("INSTRUCTIONSEND"); index++) {
      // If we get a level complete signal
      if(token[index].equals("LEVELCOMPLETE")) {
        levelComplete = true;
      }
      // If we get a dead player signal
      if(token[index].equals("PLAYER1DEAD")) {
        if(myId == 1)
          meleePlayerDead = true;
        else
          rangedPlayerDead = true;
      }
      if(token[index].equals("PLAYER2DEAD")) {
        if(myId == 1)
          rangedPlayerDead = true;
        else
          meleePlayerDead = true;
      }
      // If we get a gameover signal
      if(token[index].equals("GAMEOVER")) {
        gameover = true;
      }
      // If we get a key signal
      if(token[index].equals("P1KEY"))
        p1key = true;
      if(token[index].equals("P2KEY"))
        p2key = true;
    }
  }
}
