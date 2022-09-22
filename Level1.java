package Project2;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

/***
 * Description:
 *
 * Transitions From StartState
 *
 * Transitions To
 */
public class Level1 extends BasicGameState {
    Player player, player2;
    ArrayList<Projectile> projectileList;
    ArrayList<Enemy> enemyList;
    ArrayList<Powerup> powerupList;
    Vertex[][] path, path2;
    MapUtil levelMap;
    Timer timer;
    int player1type, player2type;
    boolean gameover, levelComplete;
    public boolean twoPlayer;
    Key key;


    @Override
    public int getID() {
        return DungeonGame.LEVEL1;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        player1type = player2type = 0;
        levelMap = new MapUtil();
        timer = new Timer();
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        // set the state id to the level in maputil to determine which map to render:
        key = null;
        path = path2 = null;
        if (player1type == 0)
            player1type = 1;
        gameover = levelComplete = false;
        // parse the CSV map file, throw exception in case of IO error:
        try {
            levelMap.loadLevelMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // keeping the string to matrix method in DungeonGame
        levelMap.currentTileMap = DungeonGame.getTileMap(levelMap.currentMapString,
                MapUtil.LEVELWIDTH, MapUtil.LEVELWIDTH);
        projectileList = new ArrayList<Projectile>();
        powerupList = new ArrayList<Powerup>();
        player = new Player(0, 0, player1type);
        if (twoPlayer) {
            player2 = new Player(0, 0, player2type);
            player2.setWorldPos(new TileIndex(4, 5));
        }
        player.setWorldPos(new TileIndex(4, 4));
        enemyList = Enemy.buildEnemyList();
        powerupList = Powerup.buildPowerUpList();

        container.setSoundOn(true);

        // Sanity check each time we enter this state while in 2player to ensure we start at the same time.
        if(twoPlayer) {
          try {
            DungeonGame.client.dataOutputStream.writeUTF("LevelStart;1;");
            DungeonGame.client.dataOutputStream.flush();
          } catch (IOException e) { e.printStackTrace();}
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        DungeonGame dg = (DungeonGame) game;

        // Render tiles
        levelMap.renderMapByCamera(g);

        // Render projectiles on the screen
        for (Projectile p : projectileList) {
            p.render(g);
        }
        // Render enemies
        for (Enemy enemy : enemyList) {
            enemy.render(g);
        }
        // Render powerups
        for (Powerup p : powerupList) {
            p.render(g);
        }

        // Render the player
        if(!player.isDead()) {
          player.render(g);
          player.weapon.render(g);
        }

        // If were in two player
        if (twoPlayer && !player2.isDead()) {
            player2.render(g);
            player2.weapon.render(g);
        }

        // Render HUD
        g.drawImage(ResourceManager.getImage(DungeonGame.HUD_BG_RSC), 0, 640);
        if (player.getPlayerType() == 1)
            g.drawImage(ResourceManager.getImage(DungeonGame.HUD_PARCHMENTRANGED_RSC), 20, 640);
        else if (player.getPlayerType() == 2)
            g.drawImage(ResourceManager.getImage(DungeonGame.HUD_PARCHMENTMELEE_RSC), 20, 640);

        g.drawImage(ResourceManager.getImage(DungeonGame.HUD_P1_RSC), 5, 640);
        g.drawImage(ResourceManager.getImage(DungeonGame.HUD_DIVIDER_RSC), 276, 640);

        // Render Left cap of health bar
        if (player.getCurrentHealth() > 0)
            g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBARL_RSC), 152, 660);
        else
            g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBARL_RSC), 152, 660);
        // Render middle of bar
        for (int i = 1; i < player.getMaxHealth(); i++) {
            if (i <= player.getCurrentHealth())
                g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBAR_RSC), 152 + (i * 6), 660);
            else
                g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBAR_RSC), 152 + (i * 6), 660);
        }
        // Render Right cap of health bar
        if (player.getCurrentHealth() == player.getMaxHealth())
            g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBARR_RSC), 152 + (player.getMaxHealth() * 6), 660);
        else
            g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBARR_RSC), 152 + (player.getMaxHealth() * 6), 660);

        if(player.getSelfRevive()) {
            g.drawImage(ResourceManager.getImage(DungeonGame.POWERUP_SELFREVIVE_RSC).getScaledCopy(0.5f), 152, 700);
        }
        if(player.getInvincible()) {
            g.drawImage(ResourceManager.getImage(DungeonGame.POWERUP_INVINCIBILITY_RSC).getScaledCopy(0.5f), 172, 700);
        }
        if(player.getDoubleStrength()) {
            g.drawImage(ResourceManager.getImage(DungeonGame.POWERUP_DOUBLESTRENGTH_RSC).getScaledCopy(0.5f), 192, 700);
        }
        if((player.hasTheKey) && enemyList.isEmpty()){ // render the key in the hud when picked up:
            g.drawImage(ResourceManager.getImage(DungeonGame.KEY_RSC), 212, 700);
        }

        // Now if were in two player, render the second players HUD
        if (twoPlayer) {
            float player2HudOffset = 342;
            if (player2.getPlayerType() == 1)
                g.drawImage(ResourceManager.getImage(DungeonGame.HUD_PARCHMENTRANGED_RSC), 20 + player2HudOffset, 640);
            else if (player2.getPlayerType() == 2)
                g.drawImage(ResourceManager.getImage(DungeonGame.HUD_PARCHMENTMELEE_RSC), 20 + player2HudOffset, 640);

            g.drawImage(ResourceManager.getImage(DungeonGame.HUD_P2_RSC), 5 + player2HudOffset, 640);

            // Render Left cap of health bar
            if (player2.getCurrentHealth() > 0)
                g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBARL_RSC), 152 + player2HudOffset, 660);
            else
                g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBARL_RSC), 152 + player2HudOffset, 660);
            // Render middle of bar
            for (int i = 1; i < player2.getMaxHealth(); i++) {
                if (i <= player2.getCurrentHealth())
                    g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBAR_RSC), 152 + (i * 6) + player2HudOffset, 660);
                else
                    g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBAR_RSC), 152 + (i * 6) + player2HudOffset, 660);
            }
            // Render Right cap of health bar
            if (player2.getCurrentHealth() == player2.getMaxHealth()) {
                g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBARR_RSC), 152 + (player2.getMaxHealth() * 6) + player2HudOffset, 660);
            } else {
                g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBARR_RSC), 152 + (player2.getMaxHealth() * 6) + player2HudOffset, 660);
            }// render the key in player 2's HUD if they picked up the key:
            if((player2.hasTheKey) && enemyList.isEmpty()){ // render the key in the hud when picked up:
                g.drawImage(ResourceManager.getImage(DungeonGame.KEY_RSC), 212  + player2HudOffset, 700);
            }

            // Render powerups if needed
            if(player2.getSelfRevive()) {
              g.drawImage(ResourceManager.getImage(DungeonGame.POWERUP_SELFREVIVE_RSC).getScaledCopy(0.5f), 152 + player2HudOffset, 700);
            }
            if(player2.getInvincible()) {
              g.drawImage(ResourceManager.getImage(DungeonGame.POWERUP_INVINCIBILITY_RSC).getScaledCopy(0.5f), 172 + player2HudOffset, 700);
            }
            if(player2.getDoubleStrength()) {
              g.drawImage(ResourceManager.getImage(DungeonGame.POWERUP_DOUBLESTRENGTH_RSC).getScaledCopy(0.5f), 192 + player2HudOffset, 700);
            }
        }

        // rendering the key being dropped onto the map upon last enemy being killed:
        if (enemyList.isEmpty() && !player.hasTheKey) {
            if(twoPlayer){  // checking that both players do not have the key:
                if(enemyList.isEmpty() && !player2.hasTheKey){
                    g.drawImage(ResourceManager.getImage(DungeonGame.KEY_RSC), levelMap.convertTileToScreen(key.getLocation()).x - 16,
                                                                                levelMap.convertTileToScreen(key.getLocation()).y - 16);
                }
            }else{
                g.drawImage(ResourceManager.getImage(DungeonGame.KEY_RSC), levelMap.convertTileToScreen(key.getLocation()).x,
                                                                            levelMap.convertTileToScreen(key.getLocation()).y);
            }
        }

        // render a string in the corner of the screen that shows enemy count remaining:
        // if a player defeats all enemies, instruct to find the key
        // if a player has picked up the key, instruct to unlock the door
        // this logic can be refactored to be simpler later
        if(!MapUtil.cheatMode) {
            if (!enemyList.isEmpty()) {
                g.drawString("Enemies remaining: " + enemyList.size(), 440, 0);
            }
            if (enemyList.isEmpty() && (!player.hasTheKey) && (!twoPlayer)) {
                g.drawString("Find the key!", 440, 0);
            }
            if (enemyList.isEmpty() && player.hasTheKey && !twoPlayer) {
                g.drawString("Unlock the door!", 440, 0);
            }
            if (twoPlayer) {
                if (enemyList.isEmpty() && (!player.hasTheKey) && (!player2.hasTheKey)) {
                    g.drawString("Find the key!", 440, 0);
                }
                if (enemyList.isEmpty() && (player2.hasTheKey || player.hasTheKey)) {
                    g.drawString("Unlock the door!", 440, 0);
                }
            }
        }
        if(MapUtil.cheatMode){
            g.drawString("YOU ARE IN CHEAT MODE, PRESS [Q] TO SKIP", 240, 0);
        }
    }


    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        DungeonGame dg = (DungeonGame) game;
        Input input = container.getInput();
        if(input.isKeyDown(Input.KEY_M)){
            MapUtil.cheatMode = true;
        }
        if(MapUtil.cheatMode){
            if(input.isKeyDown(Input.KEY_Q)) {
                levelComplete = true;
                if (twoPlayer) {
                    try {
                        dg.client.dataOutputStream.writeUTF(get2PData());
                        dg.client.dataOutputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    game.enterState(DungeonGame.TRANSITION, new EmptyTransition(), new BlobbyTransition());
                }
                else{
                    game.enterState(DungeonGame.TRANSITION, new EmptyTransition(), new BlobbyTransition());
                }
            }
        }
        String p2data;
        String[] p2dataToken = null;
        TileIndex keyLocation = null;
        if (!enemyList.isEmpty()) {
            // only gets the location if there is one enemy left on the map:
            keyLocation = getKeyPositionWhenLastEnemyStands();
        }
        if (keyLocation != null) {
            key = new Key(keyLocation.x, keyLocation.y);
        }

        if (key != null) {
            if (key.playerCollision(player.getTileIndex()) && enemyList.isEmpty()) {
                player.hasTheKey = true;
                key = null;
            }
            if (twoPlayer && key != null) {
                if (key.playerCollision(player2.getTileIndex()) && enemyList.isEmpty()) {
                    // take the key off the map if the player grabs it:
                    // java handles the garbage collection here
                    player2.hasTheKey = true;
                    key = null;
                }
            }
        }

        TileIndex playerTile = levelMap.convertWorldToTile(player.worldPos);
        path = DungeonGame.getDijkstras(playerTile.x, playerTile.y, levelMap, path);

        // Things to do when were in 2 player mode
        if (twoPlayer) {
            // Set tile and path for p2
            TileIndex player2Tile = levelMap.convertWorldToTile(player2.worldPos);
            path2 = DungeonGame.getDijkstras(player2Tile.x, player2Tile.y, levelMap, path2);
            // Read controls for p2
            try {
                p2data = DungeonGame.client.dataInputStream.readUTF();
                p2dataToken = p2data.split(";");
            } catch (IOException e) {
                System.out.println("IOException from run() in ClientHandler");
                e.printStackTrace();
            }
        }

        /*** CONTROLS SECTION ***/
        // Left click for attacking
        if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            Projectile newProjectile = player.fire(getPlayerMouseAngle(input));
            if (newProjectile != null)
                projectileList.add(newProjectile);
        }
        // Check diagonals first
        // W and A for Up Left
        if (input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_A) && player.isMoveValid(Direction.UP_LEFT,
                player.getVelocity().scale(delta), levelMap)) {
            player.moveUpLeft();
        }
        // W and D for Up Right
        else if (input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_D) && player.isMoveValid(Direction.UP_RIGHT,
                player.getVelocity().scale(delta), levelMap)) {
            player.moveUpRight();
        }
        // S and A for Down Left
        else if (input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_A) && player.isMoveValid(Direction.DOWN_LEFT,
                player.getVelocity().scale(delta), levelMap)) {
            player.moveDownLeft();

        }
        // S and D for Down Right
        else if (input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_D) && player.isMoveValid(Direction.DOWN_RIGHT,
                player.getVelocity().scale(delta), levelMap)) {
            player.moveDownRight();
        }
        // W for moving up
        else if (input.isKeyDown(Input.KEY_W) && player.isMoveValid(Direction.UP, player.getVelocity().scale(delta),
                levelMap)) {
            player.moveUp();
        }
        // A for moving left
        else if (input.isKeyDown(Input.KEY_A) && player.isMoveValid(Direction.LEFT, player.getVelocity().scale(delta),
                levelMap)) {
            player.moveLeft();
        }
        // S for moving down
        else if (input.isKeyDown(Input.KEY_S) && player.isMoveValid(Direction.DOWN, player.getVelocity().scale(delta),
                levelMap)) {
            player.moveDown();
        }
        // D for moving right
        else if (input.isKeyDown(Input.KEY_D) && player.isMoveValid(Direction.RIGHT, player.getVelocity().scale(delta),
                levelMap)) {
            player.moveRight();
        }
        // Other wise were just gonna stop moving.
        else {
            player.stop();
        }

        /*** EXTRA CONTROLS SECTION FOR TESTING WITH TWO PLAYERS ***/
        if (twoPlayer && p2dataToken.length > 1) {
            /*** NETWORK CONTROLS ***/
            // Left click for attacking
            if (p2dataToken[0].equals("1")) {
                Projectile newProjectile = player2.fire(Double.valueOf(p2dataToken[2]));
                if (newProjectile != null) {
                    projectileList.add(newProjectile);
                }
            }
            // Check diagonals first
            // W and A for Up Left
            if (p2dataToken[1].equals("WA") && player2.isMoveValid(Direction.UP_LEFT,
                    player2.getVelocity().scale(delta), levelMap)) {
                player2.moveUpLeft();
            }
            // W and D for Up Right
            else if (p2dataToken[1].equals("WD") && player2.isMoveValid(Direction.UP_RIGHT,
                    player2.getVelocity().scale(delta), levelMap)) {
                player2.moveUpRight();
            }
            // S and A for Down Left
            else if (p2dataToken[1].equals("SA") && player2.isMoveValid(Direction.DOWN_LEFT,
                    player2.getVelocity().scale(delta), levelMap)) {
                player2.moveDownLeft();

            }
            // S and D for Down Right
            else if (p2dataToken[1].equals("SD") && player2.isMoveValid(Direction.DOWN_RIGHT,
                    player2.getVelocity().scale(delta), levelMap)) {
                player2.moveDownRight();
            }
            // W for moving up
            else if ((p2dataToken[1].equals("W") || p2dataToken[1].equals("WA") || p2dataToken[1].equals("WD")) &&
                    player2.isMoveValid(Direction.UP, player2.getVelocity().scale(delta),
                            levelMap)) {
                player2.moveUp();
            }
            // A for moving left
            else if ((p2dataToken[1].equals("A") || p2dataToken[1].equals("WA") || p2dataToken[1].equals("SA")) &&
                    player2.isMoveValid(Direction.LEFT, player2.getVelocity().scale(delta),
                            levelMap)) {
                player2.moveLeft();
            }
            // S for moving down
            else if ((p2dataToken[1].equals("S") || p2dataToken[1].equals("SD") || p2dataToken[1].equals("SA")) &&
                    player2.isMoveValid(Direction.DOWN, player2.getVelocity().scale(delta),
                            levelMap)) {
                player2.moveDown();
            }
            // D for moving right
            else if ((p2dataToken[1].equals("D") || p2dataToken[1].equals("WD") || p2dataToken[1].equals("SD")) &&
                    player2.isMoveValid(Direction.RIGHT, player2.getVelocity().scale(delta),
                            levelMap)) {
                player2.moveRight();
            }
            // Other wise were just gonna stop moving.
            else {
                player2.stop();
            }
        }

        // Update the player model
        Coordinate playerScreenPos = levelMap.convertWorldToScreen(player.worldPos);
        player.setX(playerScreenPos.x);
        player.setY(playerScreenPos.y);
        player.mouseRotate(getPlayerMouseAngle(input));
        player.update(delta);
        player.offsetUpdate(levelMap.currentTileMap);

        // Update P2 if applicable.
        if (twoPlayer) {
            Coordinate player2ScreenPos = levelMap.convertWorldToScreen(player2.worldPos);
            player2.setX(player2ScreenPos.x);
            player2.setY(player2ScreenPos.y);
            if (p2dataToken.length > 1)
                player2.mouseRotate(Double.valueOf(p2dataToken[2]));
            player2.update(delta);
            player2.offsetUpdate(levelMap.currentTileMap);
        }

        // Update the camera and follow p2 if were dead and there is a p2
        if(twoPlayer && player.isDead()) {
          levelMap.updateCamera(player2.worldPos);
        }
        else
          levelMap.updateCamera(player.worldPos);

        // Update projectiles
        for (Projectile p : projectileList) {
            p.update(delta);
            Coordinate pScreenPos = levelMap.convertWorldToScreen(p.worldPos);
            p.setX(pScreenPos.x);
            p.setY(pScreenPos.y);
        }
        // Update All enemies
        for (Enemy enemy : enemyList) {
            if (twoPlayer)
                enemy.makeMove2P(levelMap.currentTileMap, path, player, path2, player2, projectileList, delta);
            else
                enemy.makeMove(levelMap.currentTileMap, path, player, projectileList, delta);
            enemy.update(delta);
            enemy.offsetUpdate(levelMap.currentTileMap);
            Coordinate enemyScreenPos = levelMap.convertWorldToScreen(enemy.worldPos);
            enemy.setX(enemyScreenPos.x);
            enemy.setY(enemyScreenPos.y);
        }

        // Update powerups
        for (Powerup p : powerupList) {
            Coordinate screenPos = levelMap.convertWorldToScreen(p.worldPos);
            p.setX(screenPos.x);
            p.setY(screenPos.y);
        }


        // Collision check for projectiles
        for (Projectile projectile : projectileList) {
            projectile.collisionCheck(levelMap.currentTileMap, enemyList, player);
            if (twoPlayer)
                projectile.collisionCheck(levelMap.currentTileMap, enemyList, player2);
        }

        // Collision check for powerups
        for (Powerup p : powerupList) {
            p.playerCollision(player);
            if (twoPlayer)
                p.playerCollision(player2);
        }

        // Check if players have died.
        if(player.getCurrentHealth() <= 0) {
            if(player.getSelfRevive()) {
                player.maxHeal();
                player.flipSelfRevive();
            }
        }
        // Check if p2 died
        if(twoPlayer) {
            if(player2.getCurrentHealth() <= 0 ) {
                if (player2.getSelfRevive()) {
                    player2.maxHeal();
                    player2.flipSelfRevive();
                }
            }
        }

        // Check if they both died, in that case were in gameover
      if (twoPlayer && player.isDead() && player2.isDead()) {
        gameover = true;
      }

      // Check if were in single player and p1 dies, in that case were in gameover
      if(!twoPlayer && player.isDead()) {
        gameover = true;
      }

        // Send data to p2 if were in two player mode
        if (twoPlayer) {
            try {
                dg.client.dataOutputStream.writeUTF(get2PData());
                dg.client.dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Remove Projectiles that have collided with objects.
        projectileList.removeIf((Projectile projectile) -> projectile.needsRemove());
        // Remove grabbed powerups
        powerupList.removeIf((Powerup powerup) -> powerup.getRemoveMe());
        // Remove enemies that have died.
        enemyList.removeIf( (Enemy enemy) -> {
            if(enemy.isDead()) {
                // 1 in 9 chance a healing potion will spawn under the dead enemy.
                if(new Random().nextInt(9) == 0){
                    TileIndex location = enemy.getLocation();
                    powerupList.add(new Powerup(location.x, location.y, new Random().nextInt(4)+1));
                }
            }
            return enemy.isDead();
        });

        // If were in gameover, we need to go to the gameover state.
        if(gameover) {
          // Tell p2 if were in 2player
          if(twoPlayer) {
            try {
              dg.client.dataOutputStream.writeUTF(get2PData());
              dg.client.dataOutputStream.flush();
            } catch (IOException e) { e.printStackTrace(); }
          }
          game.enterState(DungeonGame.GAMEOVER, new FadeOutTransition(), new FadeInTransition());
        }

        checkIfPlayersCamExitToNextLevel(game);
    } // update


    public void setPlayerType(int id) {
        player1type = id;
    }


    public void set2Player(boolean status) {
        twoPlayer = status;
        if (player1type == 1)
            player2type = 2;
        else
            player2type = 1;
    }


    public String get2PData() {
        String data = "";
        // Step 1: get player data
        // p1 data
        data = data.concat("P1;");
        data = data.concat(player.getPlayerData());
        // p2 data
        data = data.concat("P2;");
        data = data.concat(player2.getPlayerData());
        // Step 2: build all enemy data
        data = data.concat("ENEMYLISTSTART;");
        for(Enemy e : enemyList)
            data = data.concat(e.getEnemyData());
        data = data.concat("ENEMYLISTEND;");
        // Step 3: build all projectile data
        data = data.concat("PROJLISTSTART;");
        for(Projectile p : projectileList)
            data = data.concat(p.getData());
        data = data.concat("PROJLISTEND;");
        // Step 4: build powerup data
        data = data.concat("POWERUPLISTSTART;");
        for(Powerup p : powerupList)
            data = data.concat(p.getData());
        // We're going to add the key here if applicable, since the key will be constructed as a dummy object.
        if(key != null && enemyList.isEmpty() && !player.hasTheKey && !player2.hasTheKey)
            data = data.concat(key.getData());
        data = data.concat("POWERUPLISTEND;");

        // Step 5: Send HUD information
        data = data.concat("HUDSTART;");
        // Send both players healths and max healths across
        data = data.concat(player.getCurrentHealth() + ";" + player.getMaxHealth() + ";" + player2.getCurrentHealth() + ";"
                + player2.getMaxHealth() + ";" + player.getSelfRevive() + ";" + player.getInvincible() + ";" + player.getDoubleStrength() + ";"
                + player2.getSelfRevive() + ";" + player2.getInvincible() + ";" + player2.getDoubleStrength() + ";");
        data = data.concat("HUDEND;");

        // Step 6: Send special instructions
        data = data.concat("INSTRUCTIONSSTART;");
        // Send a token if the level was completed
        if(levelComplete) {
          data = data.concat("LEVELCOMPLETE;");
        }
        // Send a token if a gameover occurs
        if(gameover) {
          data = data.concat("GAMEOVER;");
        }
        // Send a token if a player dies
        else {
          if(player.isDead()) {
            data = data.concat("PLAYER1DEAD;");
          }
          if(player2.isDead()) {
            data = data.concat("PLAYER2DEAD;");
          }
        }
        // Send a token if a player has a key
        if(player.hasTheKey) {
          data = data.concat("P1KEY;");
        }
        else if (player2.hasTheKey) {
          data = data.concat("P2KEY;");
        }
        // Put other stuff here if necessary

        data = data.concat("INSTRUCTIONSEND;");
        return data;
    }

    public double getPlayerMouseAngle(Input input) {
        float mousex = input.getMouseX();
        float mousey = input.getMouseY();
        float playerx = player.getX();
        float playery = player.getY();
        Vector angleVector = new Vector(mousex - playerx, mousey - playery);
        return angleVector.getRotation();
    }


    private TileIndex getKeyPositionWhenLastEnemyStands() {
        if (enemyList.size() == 1) {
            // if we have one enemy left, we get the location of the final enemy
            // to render the key when the enemy dies, in the enemy's location:
            return enemyList.get(0).getTileIndex();
        }
        return null;
    }


    // check if either player is at the door and also has the key to unlock it,
    // if so, the players move to the next level.
    private void checkIfPlayersCamExitToNextLevel(StateBasedGame game) {
        DungeonGame dg = (DungeonGame) game;
        if (levelMap.isAtDoor(player)) {
            if (player.hasTheKey) {
                game.enterState(DungeonGame.TRANSITION, new EmptyTransition(), new BlobbyTransition());
            }
        }
        if (twoPlayer) {
            if (levelMap.isAtDoor(player)) {
                if (player.hasTheKey) {
                  levelComplete = true;
                  try {
                    dg.client.dataOutputStream.writeUTF(get2PData());
                    dg.client.dataOutputStream.flush();
                  } catch (IOException e) { e.printStackTrace(); }
                    game.enterState(DungeonGame.TRANSITION, new EmptyTransition(), new BlobbyTransition());
                }
            }
            if (levelMap.isAtDoor(player2)) {
                if (player2.hasTheKey) {
                    levelComplete = true;
                    try {
                      dg.client.dataOutputStream.writeUTF(get2PData());
                      dg.client.dataOutputStream.flush();
                    } catch (IOException e) { e.printStackTrace(); }
                    game.enterState(DungeonGame.TRANSITION, new EmptyTransition(), new BlobbyTransition());
                }
            }
        }
    }
}
