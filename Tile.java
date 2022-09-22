package Project2;

/***
 * Tile class for building a tilemap. Each tile type has an id and a associated cost.
 * Current ids:
 *  0: Floor tile
 *  1: Wall tile
 */
public class Tile {
  private int id, cost;

  /***
   * Constructor,
   * @param newID
   *  ID of the tile to be built
   */
  public Tile(int newID) {
    id = newID;
    /*** FLoor tile ***/
    if (id == 0) {
      cost = 10;
    }
    /*** Wall tile ***/
    if(id == 1 || id == 2) {
      cost = 100000;
    }
  }

  public int getID() { return id;}

  public int getCost() { return cost;}

  public void setCost(int newcost) { cost = newcost;}

  /***
   * Method for building copies of a tilemap.
   * @return
   *  Copy of a this tile that is a new object.
   */
  Tile getCopy() {
    Tile tile = new Tile(this.id);
    return tile;
  }
}
