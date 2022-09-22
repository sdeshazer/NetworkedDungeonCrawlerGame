package Project2;

import jig.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.BlobbyTransition;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;

import java.util.Map;

/**
 * This state is used to transition back into Entering the playing state.
 */
public class TransitionState extends BasicGameState {

    Boolean playerWon;
    boolean player2;

    private int timer;

    @Override // TRANSITION = 7
    public int getID() {
        return DungeonGame.TRANSITION;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
      playerWon = false;
      player2 = false;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game)
    {
        // switching levels:
        timer = 1500;

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        if (timer <= 1000 &&  timer > 1) {
            // Checks previous level before updating to the next:
            if (MapUtil.levelName == LevelName.MENU)
                g.drawImage(ResourceManager.getImage(DungeonGame.MENU_L1_RSC), 250, 350);
            if (MapUtil.levelName == LevelName.ONE)
              g.drawImage(ResourceManager.getImage(DungeonGame.MENU_L2_RSC), 258, 350);
            if (MapUtil.levelName == LevelName.TWO)
              g.drawImage(ResourceManager.getImage(DungeonGame.MENU_L3_RSC), 234, 350);
            if (MapUtil.levelName == LevelName.THREE)
              g.drawImage(ResourceManager.getImage(DungeonGame.MENU_DRUM_RSC), 228, 350);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        timer -= delta;
        if (timer <= 0) {
            if (MapUtil.levelName == LevelName.MENU) {
                MapUtil.levelName = LevelName.ONE;
            } else if (MapUtil.levelName == LevelName.ONE)
                MapUtil.setLevelName(LevelName.TWO);
            else if (MapUtil.levelName == LevelName.TWO) {
                MapUtil.setLevelName(LevelName.THREE);
            } else if (MapUtil.levelName == LevelName.THREE) {
                playerWon = true;
                System.out.println("Player(s) have won the game!");

            }

            if (!playerWon) {
                if (player2)
                  game.enterState(DungeonGame.DUMMYSTATE, new FadeOutTransition(), new BlobbyTransition());
                else
                  game.enterState(DungeonGame.LEVEL1, new FadeOutTransition(), new BlobbyTransition());
            } else {
                playerWon = false;
                game.enterState(DungeonGame.WIN, new EmptyTransition(), new HorizontalSplitTransition());
            }
        }
    }

    public void set2P() {player2 = true;}
}
