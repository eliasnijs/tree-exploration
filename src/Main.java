import test.Tester;
import benchmarking.Benchmarker;

import opgave.*;
import oplossing.*;

public class Main {

  public static void main(String[] args) {
    Tester t = new Tester(args);
    t.run();
    Benchmarker b = new Benchmarker();
    // if (args.length == 0) {
    //   System.out.println("no arguments were provided! quitting benchmark...");
    //   System.out.println("Syntax: ");
    //   System.out.println("./program <amount> <upperbound> <exponent>");
    //   System.out.println("<amount>      amount of elements to take from the sampler");
    //   System.out.println("<upperbound>  sampler generates numbers from 0 to <upperbound>");
    //   System.out.println("<exponen>     exponent for Zipf");
    // } else {
    //   b.run(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Double.parseDouble(args[2]));
    // }
    
    // exp cte
    // for (int i=0; i<=100000; i += 10000) {
    //   for (int j=1; j<=100001; j += 10000) {
    //     b.run(i, j, 1);
    //   }
    // }
    
    // upperbound cte
    // for (int i=0; i<=100000; i += 5000) {
    //   for (int j=1; j<=100; j += 5) {
    //     b.run(i, 0, j);
    //   }
    // }
    
    // amount cte
    // for (int i=1; i<=100001; i += 5000) {
    //   for (int j=1; j<=100; j += 5) {
    //     b.run(1000, i, j);
    //   }
    // }
    
    for (int j=1; j<=10; j += 1) {
      b.run(10000, 1000000, j);
    }

  }
  
}
