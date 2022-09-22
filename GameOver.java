package Project2;

import jig.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class GameOver extends BasicGameState {
    private int select;

    @Override
    public int getID() { // state id = 6
        return DungeonGame.GAMEOVER;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {

    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
      container.getInput().clearKeyPressedRecord();
      MapUtil.setLevelName(LevelName.GAMEOVER);
      select = 0;
      // If we were playing in 2 player mode we should disconnect at this point.
      DungeonGame dg = (DungeonGame) game;
      if(((Level1)game.getState(DungeonGame.LEVEL1)).twoPlayer) {
        DungeonGame.client.disconnect();
      }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
      g.drawImage(ResourceManager.getImage(DungeonGame.MENU_GAMEOVERBG_RSC),0,0);

      if(select == 1)
        g.drawImage(ResourceManager.getImage(DungeonGame.MENU_SELECTOR_RSC), 256, 532);
      else if(select == 2)
        g.drawImage(ResourceManager.getImage(DungeonGame.MENU_SELECTOR_RSC), 602, 532);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
      Input input = container.getInput();
      // Get mouse position
      float mousex = input.getMouseX();
      float mousey = input.getMouseY();
      // Get selection status
      if(50 < mousex && mousex < 246)
        if(530 < mousey && mousey < 598)
          select = 1;
      if(396 < mousex && mousex < 590)
        if(530 < mousey && mousey < 598)
          select = 2;
      else
        select = 0;

      if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
        if(select == 1)
          game.enterState(DungeonGame.STARTUPSTATE, new FadeOutTransition(), new FadeInTransition());
        else if(select == 2)
          container.exit();
      }
    }
}
