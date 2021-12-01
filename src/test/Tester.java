package test;

import test.*;
  
  // -- NOTE (Elias): This testing framework is meant to be run in terminal.
  // !! NOTE (Elias): The testing framework was made for my own system which runs on linux, 
  //                  there might be unforseen differences with a windows or macos setup.

public class Tester {
  
  private String[] argv;
  private int argc;
  
  boolean printPriority;

  public Tester (String args[]) {
    argv = args;
    argc = args.length; 
    printPriority = true;
  }

  // OPTTODO (Elias): This way of checking command line arguments is far from good code imo :( .
  //                  If there is time left, clean this up.
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
    t.runTests();
  }

  private void testOptimalTree () {
    OptimalTreeTest t = new OptimalTreeTest(printPriority);
    t.runTests();
  }

  private void testMyTree () {
    MyTreeTest t = new MyTreeTest(printPriority);
    t.runTests();
  }

}
