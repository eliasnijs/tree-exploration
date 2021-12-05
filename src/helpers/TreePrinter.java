package helpers;

import java.util.ArrayList;

import opgave.SearchTree;
import opgave.Node;

public class TreePrinter {

  private static class NodeContext {
    public int x; // x coordinate
    public int y; // y coordinate
    public Node node;
  }

  public void print (SearchTree t) {
    print(t.root());
  } 

  // OPTTODO (Elias): display `/` and `\`
  public void print (Node n) {
    NodeContext rootData = new NodeContext();
    rootData.x = 0;
    rootData.y = 0;
    rootData.node = n;

    ArrayList<NodeContext> collection = new ArrayList<>();

    printTreeHelper(rootData, collection);

    int width  = collection.size()*2; 
    int height = 0;
    for (NodeContext nodeData : collection) {
      height = Math.max(height, nodeData.y + 1);
    }
    int offset = collection.get(0).x * -1;
    String[][] grid = new String[height][width];

    for (int i=0; i<collection.size(); ++i) {
      NodeContext nd = collection.get(i);
      int move = 0;
      for (int j=i+1; j<collection.size(); ++j) {
        int t = (collection.get(j).x > nd.x)? 0 : (Math.abs(collection.get(j).x - nd.x) + 1);
        move = Math.max(t, move);
      }
      for (int j=i+1; j<collection.size(); ++j) {
        collection.get(j).x += move;
      }
      grid[nd.y][nd.x + offset] = nd.node.getValue().toString();
      while (grid[nd.y][nd.x + offset].length() < 3) {
        grid[nd.y][nd.x + offset] = " " + grid[nd.y][nd.x + offset];
      }
    }
    System.out.print("\n");
    for (String[] row : grid) { for (String cell : row) {
      System.out.print((cell == null)? "    ": " " + cell + " ");
    } System.out.print("\n"); 
    }
  }

  // -- NOTE (Elias): Order of operation will ensure all NodeContexts are collected in the right order (i.e. left -> middle -> right)
  private void printTreeHelper (NodeContext nd, ArrayList<NodeContext> collection) {
    Node l = nd.node.getLeft();
    if (l != null) {
      NodeContext lnd = new NodeContext();
      lnd.x = nd.x - 1;
      lnd.y = nd.y + 1;
      lnd.node = l;
      printTreeHelper(lnd, collection);
    }
    collection.add(nd);
    Node r = nd.node.getRight();
    if (r != null) {
      NodeContext rnd = new NodeContext();
      rnd.x = nd.x + 1;
      rnd.y = nd.y + 1;
      rnd.node = r;
      printTreeHelper(rnd, collection);
    }
  }

}
