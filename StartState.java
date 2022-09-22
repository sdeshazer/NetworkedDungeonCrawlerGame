package Project2;
//TODO removing this state for menu?
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/***
 * Description:
 *
 * Transitions From
 *
 * Transitions To TestState
 */
public class StartState extends BasicGameState {

  private int select, timer;
  private boolean selected, arrowBlink, musicRestart;
  Animation player, melee, melee2, ranged;

  @Override
  public int getID() {
    return DungeonGame.STARTUPSTATE;
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {
    musicRestart = true;
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) {
    MapUtil.cheatMode = false;
    DungeonGame rg = (DungeonGame)game;
    container.setSoundOn(true);
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

    Input input = container.getInput();
    DungeonGame rg = (DungeonGame)game;

    rg.enterState(DungeonGame.MENUSTATE);
  }
}
