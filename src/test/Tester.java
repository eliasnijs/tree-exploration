package test;

import test.*;

public class Tester {

  private String[] argv;
  private int argc;

  public Tester (String args[]) {
    argv = args;
    argc = args.length; 
  }

  public void run () {
    if (argc > 0) {
      switch (argv[0]) {
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
          System.out.println("   invalid argument! quitting program...");
          return;
      }
    }
    testSemisplayTree();
    testOptimalTree();
    testMyTree(); 
  }

  private void testSemisplayTree () {
    SemiSplayTreeTest t = new SemiSplayTreeTest();
    t.run();
  }

  private void testOptimalTree () {
    OptimalTreeTest t = new OptimalTreeTest();
    t.run();
  }

  private void testMyTree () {
    MyTreeTest t = new MyTreeTest();
    t.run();
  }

}
