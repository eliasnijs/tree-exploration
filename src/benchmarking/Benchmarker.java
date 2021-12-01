package benchmarking;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import opgave.samplers.Sampler;
import opgave.samplers.ZipfSampler;
import opgave.SearchTree;

import oplossing.SemiSplayTree;
import oplossing.MyTree;
import oplossing.OptimalTree;
import oplossing.BinarySearchTree;

public class Benchmarker {

  private static class Result {
    public long time;
    // NOTE (Elias): other data to collect needs to be added here
    public String toString () {
      return "" + time;
    }
  }

  private static class Operation {
    public int operation; 
    public int value;
  }

  public Benchmarker () {
    System.out.println("   ALGORITHMS AND DATASTRUCTURES 2 -- PROJECT: BENCHMARKS");
  }
  
  public void run (int amount, int upperbound, double exp) {
    test(amount, upperbound, exp);
  }

  public void print (String name, Result[][] r) {
    System.out.println("\n   " + name);
    System.out.println("   Time(ms)                     | Semisplay   | MyTree      | Simple ");
    System.out.println("  ------------------------------|-------------|-------------|-------------");
    System.out.println(String.format("   Add                          | %-9d   | %-9d   | %-9d   ", r[ 0][0].time, r[ 0][1].time, r[ 0][2].time));
    // System.out.println(String.format("   Remove                       | %-9d   | %-9d   | %-9d   ", r[ 1][0].time, r[ 1][1].time, r[ 1][2].time));
    // System.out.println(String.format("   Search                       | %-9d   | %-9d   | %-9d   ", r[ 2][0].time, r[ 2][1].time, r[ 2][2].time));
    // System.out.println(String.format("   Add    and remove (order)    | %-9d   | %-9d   | %-9d   ", r[ 3][0].time, r[ 3][1].time, r[ 3][2].time));
    // System.out.println(String.format("   Add    and search (order)    | %-9d   | %-9d   | %-9d   ", r[ 4][0].time, r[ 4][1].time, r[ 4][2].time));
    // System.out.println(String.format("   remove and add    (order)    | %-9d   | %-9d   | %-9d   ", r[ 5][0].time, r[ 5][1].time, r[ 5][2].time));
    // System.out.println(String.format("   remove and search (order)    | %-9d   | %-9d   | %-9d   ", r[ 6][0].time, r[ 6][1].time, r[ 6][2].time));
    // System.out.println(String.format("   search and add    (order)    | %-9d   | %-9d   | %-9d   ", r[ 7][0].time, r[ 7][1].time, r[ 7][2].time));
    // System.out.println(String.format("   search and remove (order)    | %-9d   | %-9d   | %-9d   ", r[ 8][0].time, r[ 8][1].time, r[ 8][2].time));
    // System.out.println(String.format("   Add    and remove (mix)      | %-9d   | %-9d   | %-9d   ", r[ 8][0].time, r[ 8][1].time, r[ 8][2].time));
    // System.out.println(String.format("   Add    and search (mix)      | %-9d   | %-9d   | %-9d   ", r[10][0].time, r[10][1].time, r[10][2].time));
    // System.out.println(String.format("   remove and search (mix)      | %-9d   | %-9d   | %-9d   ", r[11][0].time, r[11][1].time, r[11][2].time));
    // System.out.println(String.format("   all (mix)                    | %-9d   | %-9d   | %-9d   ", r[12][0].time, r[12][1].time, r[12][2].time));
    // System.out.println();
  }

  public void test (int amount, int upperbound, double exp) {
    Result results[][] = new Result[13][3];
    
    results[ 0] = singletest(upperbound, amount, exp, 0);
    results[ 1] = singletest(upperbound, amount, exp, 1);
    results[ 2] = singletest(upperbound, amount, exp, 2);
    results[ 3] =  ordertest(upperbound, amount, exp, 0, 1);
    results[ 4] =  ordertest(upperbound, amount, exp, 0, 2);
    results[ 5] =  ordertest(upperbound, amount, exp, 1, 0);
    results[ 6] =  ordertest(upperbound, amount, exp, 1, 2);
    results[ 7] =  ordertest(upperbound, amount, exp, 2, 0);
    results[ 8] =  ordertest(upperbound, amount, exp, 2, 1);
    results[ 8] =  ordertest(upperbound, amount, exp, 2, 1);
    results[ 8] =  ordertest(upperbound, amount, exp, 2, 1);
    results[ 9] = randomtest(upperbound, amount, exp, new int[]{0,1});
    results[10] = randomtest(upperbound, amount, exp, new int[]{0,2});
    results[11] = randomtest(upperbound, amount, exp, new int[]{1,2});
    results[12] = randomtest(upperbound, amount, exp, new int[]{0,1,2});
    
    // print("Test parameters: exponent=" + exp + ", amount=" + amount + ", upperbound=" + upperbound, results);
    for (Result[] r : results) {
      System.out.println("{" + exp + "-" + amount + "-" + upperbound + ":{"+ r[0] + "," + r[1] + "," + r[2] + "}}");
    }
  }

  public Result runOperationsOnTree (SearchTree<Integer> tree, Operation operations[]) {
    Result result = new Result(); 
    long samplecount = 3;
    result.time = 0;
    for (int i=0; i<samplecount; ++i) {
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
    } 
    result.time /= samplecount;
    return result;
  }

  public Result[] singletest (int upperbound, int amount, double exp, int a) {
    Operation operations[] = new Operation[amount];
    List<Integer> samples  = new ZipfSampler(new Random(), upperbound, exp).sample(amount);
    for (int i = 0; i < amount; ++i) {
      Operation operation = new Operation(); 
      operation.value = samples.get(i);
      operation.operation = a;
      operations[i] = operation;
    }
    
    SemiSplayTree<Integer> st    = new SemiSplayTree<>();
    OptimalTree<Integer> ot      = new OptimalTree<>();
    BinarySearchTree<Integer> bt = new BinarySearchTree<>();

    List<Integer> initsamples  = new Sampler(new Random(), upperbound).sample(amount);
    if (a > 0) {
      for (int sample : initsamples) {
        st.add(sample);
        ot.add(sample);
        bt.add(sample);
      } 
    }
   
    Result results[] = new Result[] { 
      runOperationsOnTree(st,operations),
      runOperationsOnTree(ot,operations),
      runOperationsOnTree(bt,operations) 
      };
    
    return results;
  }

  public Result[] ordertest (int upperbound, int amount, double exp, int a1, int a2) {
    Operation operations[] = new Operation[amount];
    List<Integer> samples  = new Sampler(new Random(), upperbound).sample(amount);
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
    
    SemiSplayTree<Integer> st    = new SemiSplayTree<>();
    OptimalTree<Integer> ot      = new OptimalTree<>();
    BinarySearchTree<Integer> bt = new BinarySearchTree<>();

    List<Integer> initsamples  = new Sampler(new Random(), upperbound).sample(amount);
    if (a1 > 0) {
      for (int sample : initsamples) {
        st.add(sample);
        ot.add(sample);
        bt.add(sample);
      } 
    }
   
    Result results[] = new Result[] { 
      runOperationsOnTree(st,operations),
      runOperationsOnTree(ot,operations),
      runOperationsOnTree(bt,operations) 
      };
    
    return results;
  }
  
  public Result[] randomtest (int upperbound, int amount, double exp, int[] a) {
    Operation operations[] = new Operation[amount];
    List<Integer> samples  = new Sampler(new Random(), upperbound).sample(amount);
    Random random = new Random();
    for (int i = 0; i < amount; ++i) {
      Operation operation = new Operation(); 
      operation.value = samples.get(i);
      int index = random.nextInt(a.length);
      operation.operation = a[index];
      operations[i] = operation;
    }
    
    SemiSplayTree<Integer> st    = new SemiSplayTree<>();
    OptimalTree<Integer> ot      = new OptimalTree<>();
    BinarySearchTree<Integer> bt = new BinarySearchTree<>();

    List<Integer> initsamples  = new Sampler(new Random(), upperbound).sample(amount);
   
    Result results[] = new Result[] { 
      runOperationsOnTree(st,operations),
      runOperationsOnTree(ot,operations),
      runOperationsOnTree(bt,operations) 
      };
    
    return results;
  }
  
}
