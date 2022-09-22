package Project2;

/**
 * Simple class for use in pathfinding. Represents a node in a graph for use in dijkstras.
 *
 * Has 3 parameters:
 *  Cost: The cost of moving into this tile
 *  Distance: The shortest path distance in this node to the desired location (usually the player)
 *  Direction: The direction an entity needs to move in to follow the shortest path to the desired location.
 */
public class Vertex {
  private int cost, direction;
  private double distance;

  public Vertex(int newCost) {
    cost = newCost;
    distance = 10000;
    direction = 0;
  }

  public void setCost(int newCost) {cost = newCost;}

  public void setDirection(int newDirection) {direction = newDirection;}

  public void setDistance(double newDistance) {distance = newDistance;}

  public int getCost() {return cost;}

  public double getDistance() {return distance;}

  public int getDirection() {return direction;}
}
