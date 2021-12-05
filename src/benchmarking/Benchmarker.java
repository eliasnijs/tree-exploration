package benchmarking;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Collections;

import opgave.samplers.Sampler;
import opgave.samplers.ZipfSampler;
import opgave.SearchTree;

import oplossing.SemiSplayTree;
import oplossing.MyTree;
import oplossing.OptimalTree;
import oplossing.BinarySearchTree;
import oplossing.SearchTreeB;

import helpers.TreePrinter;

public class Benchmarker {

  private static class Result {
    public double time;
  }

  private static class Operation {
    public int operation;  // !! 0 = add, 1 = remove, 2 = search
    public int value;
  }

  private static class Tree {
    public String name;
    public SearchTreeB<Integer> tree;
  }

  private static class Test {
    public String name;
    public Operation[] operations;
    public Result[] results;
  }
  
  private final static int SAMPLE_COUNT = 10;
  
  private final Random random = new Random();
  private final List<Tree> trees;
  private Sampler prepareSampler;

  private Sampler sampler;
  private int amount;
  private int upperbound;
  private double exponent;

  public Benchmarker () {
    // System.out.println("ALGORITHMS AND DATASTRUCTURES 2 -- PROJECT: BENCHMARKS");
    trees = new ArrayList<>();
   
    // NOTE (Elias): Add the trees to benchmark here
    Tree semisplay = new Tree();
    semisplay.name = "Semisplay";
    semisplay.tree = new SemiSplayTree<Integer>();

    Tree mytree = new Tree();
    mytree.name = "MyTree";
    mytree.tree = new MyTree<Integer>();

    Tree bst = new Tree();
    bst.name = "Simple";
    bst.tree = new BinarySearchTree<Integer>();
    
    trees.add(semisplay);
    trees.add(mytree);
    trees.add(bst);
  }
  
  public void print (Test[] tests) {
    System.out.println("Test parameters:" + " amount=" + amount + ", upperbound=" + upperbound + ", exponent=" + exponent);
    System.out.print("Time(ms)                     ");
    for (int i=0; i<trees.size(); ++i) {
      System.out.print(String.format("| %-11s ", trees.get(i).name));
    } System.out.println();
    System.out.print("-----------------------------");
    for (int i=0; i<trees.size(); ++i) {
      System.out.print("|-------------");
    } System.out.println();
    for (int i=0; i<tests.length; ++i) {
      System.out.print(String.format("%-29s", tests[i].name));
      for (int j=0; j<tests[i].results.length; j++) {
        System.out.print(String.format("| %-9.2f   ", tests[i].results[j].time));
      } System.out.println();
    }
    System.out.println();
  }

  public void csvprint (Test[] tests) {
    System.out.println(
        String.format("%3.2f,%4.2f,%4.2f", exponent, tests[0].results[0].time, tests[0].results[1].time)
        );
  }
    
  // Add                      
  // Remove                   
  // Search                   
  // Add    and remove (order)
  // Add    and search (order)
  // remove and add    (order)
  // remove and search (order)
  // search and add    (order)
  // search and remove (order)
  // Add    and remove (mix)   
  // Add    and search (mix)  
  // remove and search (mix)  
  // all (mix)                
  public void run (int amount, int upperbound, double exp) {
    this.amount  = amount;
    this.upperbound = upperbound;
    this.exponent = exp;
    this.sampler = new ZipfSampler(random, upperbound, exp);
    this.prepareSampler = new Sampler(random, upperbound);

    // NOTE (Elias): Make a new test here and add it to `tests`.  You can use the 3 `buildOperations` functions to make the operations or use a custom function.
    Test addTest = new Test();
    addTest.name = "Add";
    addTest.operations = buildOperations(0);

    Test removeTest = new Test();
    removeTest.name = "Remove";
    removeTest.operations = buildOperations(1);

    Test searchTest = new Test();
    searchTest.name = "Search";
    searchTest.operations = buildOperations(2);

    Test allTest = new Test();
    allTest.name = "All (mix)";
    allTest.operations = buildOperations(new int[]{0,1,2});
    
    Test allOrderTest = new Test();
    allOrderTest.name = "All (order)";
    allOrderTest.operations = buildOperations(0,2,1);
    
    Test[] tests = new Test[]{
      addTest,
      removeTest,
      searchTest,
      allTest
      };

    System.out.println();
    for (int i=0; i<tests.length; ++i) {
      System.out.println("Test " + (i+1) + "/" + tests.length + " - " + tests[i].name);
      test(tests[i]);
    } System.out.println();
    
    print(tests);
  }

  public void test (Test test) {
    Result results[] = new Result[trees.size()];
    for (int i=0; i<results.length; ++i) {
      results[i] = new Result(); 
      results[i].time = 0;
    }
    for (int s=0; s<=SAMPLE_COUNT; ++s) {
      resetTrees(); 
      prepareTrees();
      System.out.print("\r\033[K[ ] running test (" + s + "/" + SAMPLE_COUNT + ")");
      for (int i=0; i<trees.size(); ++i) {
        Result r = runOperationsOnTree(trees.get(i).tree, test.operations);
        results[i].time += (r.time/SAMPLE_COUNT);
      }
    }
    System.out.println("\r\033[K[x] running test (" + SAMPLE_COUNT + "/" + SAMPLE_COUNT + ")");
    System.out.println();
    test.results = results;
  }

  public Result runOperationsOnTree (SearchTreeB<Integer> tree, Operation operations[]) {
    Result result = new Result(); 
    result.time = 0;
    long st = System.currentTimeMillis();
    for (Operation o : operations) {
      if (o.operation == 0) { 
        tree.add(o.value);
      } else if (o.operation == 1) {
        tree.remove(o.value);
      } else if (o.operation == 2) {
        tree.search(o.value);
      } 
    }
    long et = System.currentTimeMillis();
    result.time += et-st;
    return result;
  }
  
  public void resetTrees () {
    for (Tree t : trees)
      t.tree.clear();
  }

  private void prepareTrees () {
    List<Integer> initsamples = prepareSampler.sample(amount);
    for (Tree t : trees) { 
      for (int sample : initsamples) {
        t.tree.add(sample);
      }
    }
  }

  // Generates `amount` operations of type a
  private Operation[] buildOperations (int a) {
    HashSet<Integer> controle = new HashSet<>();
    Operation operations[] = new Operation[amount]; 
    List<Integer> samples = sampler.sample(amount);
    for (int i = 0; i < amount; ++i) {
      Operation operation = new Operation(); 
      operation.value = samples.get(i);
      operation.operation = a;
      operations[i] = operation;
      controle.add(samples.get(i));
    }
    System.out.println("Generated " + ((double) controle.size()/(double) samples.size()*100) + "% unique samples");
    return operations;
  }
 
  // Generates `amount` operations.
  // First all `amount`/2 `a1` operations and then
  // `amount`/2 `a2` operations
  private Operation[] buildOperations (int a1, int a2) {
    Operation operations[] = new Operation[amount];
    List<Integer> samples = sampler.sample(amount);
    for (int i = 0; i < amount/2; ++i) {
      Operation operation = new Operation(); 
      operation.value = samples.get(i);
      operation.operation = a1;
      operations[i] = operation;
    }
    for (int i = amount/2; i < amount; ++i) {
      Operation operation = new Operation(); 
      operation.value = samples.get(i);
      operation.operation = a2;
      operations[i] = operation;
    }
    return operations;
  }

  private Operation[] buildOperations (int a1, int a2, int a3) {
    Operation operations[] = new Operation[amount];
    List<Integer> samples = sampler.sample(amount);
    for (int i = 0; i < amount/3; ++i) {
      Operation operation = new Operation(); 
      operation.value = samples.get(i);
      operation.operation = a1;
      operations[i] = operation;
    }
    for (int i = amount/3; i < (amount/3)*2; ++i) {
      Operation operation = new Operation(); 
      operation.value = samples.get(i);
      operation.operation = a2;
      operations[i] = operation;
    }
    for (int i = (amount/3)*2; i < amount; ++i) {
      Operation operation = new Operation(); 
      operation.value = samples.get(i);
      operation.operation = a3;
      operations[i] = operation;
    }
    return operations;
  }
 
  // Generates `amount` random operations
  // Which operations to choose from is the parameter `a`
  private Operation[] buildOperations (int a[]) {
    Operation operations[] = new Operation[amount];
    List<Integer> samples = sampler.sample(amount);
    Random random = new Random();
    for (int i = 0; i < amount; ++i) {
      Operation operation = new Operation(); 
      operation.value = samples.get(i);
      int index = random.nextInt(a.length);
      operation.operation = a[index];
      operations[i] = operation;
    }
    return operations;
  }
  
}
