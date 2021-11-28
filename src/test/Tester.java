package test;

import test.*;

public class Tester {

  private String[] argv;
  private int argc;
  
  boolean printPriority;

  public Tester (String args[]) {
    argv = args;
    argc = args.length; 
    printPriority = true;
  }

  public void run () {
    if (argc > 0) {
      switch (argv[0]) {
        case "1" :
          printPriority = true;
          System.out.println("\n   (printing all tests)");
          break;          
        case "0" :
          printPriority = false;
          System.out.println("\n   (printing only `FAILED` tests)");
          break;          
        case "s": 
          testSemisplayTree();
          return;
        case "o": 
          testOptimalTree();
          return;
        case "m": 
          testMyTree();
          return;
        default:
          System.out.println("   invalid `print priority` argument! quitting program...");
          return;
      }
    } 

    if (argc > 1) {
      switch (argv[1]) {
        case "s": 
          testSemisplayTree();
          return;
        case "o": 
          testOptimalTree();
          return;
        case "m": 
          testMyTree();
          return;
        default:
          System.out.println("   invalid `specific test` argument! quitting program...");
          return;
      }
    }
    testSemisplayTree();
    testOptimalTree();
    testMyTree(); 
  }

  private void testSemisplayTree () {
    SemiSplayTreeTest t = new SemiSplayTreeTest(printPriority);
    t.run();
  }

  private void testOptimalTree () {
    OptimalTreeTest t = new OptimalTreeTest(printPriority);
    t.run();
  }

  private void testMyTree () {
    MyTreeTest t = new MyTreeTest(printPriority);
    t.run();
  }

}
