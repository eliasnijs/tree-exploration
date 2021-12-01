import test.Tester;
import benchmarking.Benchmarker;

public class Main {

  public static void main(String[] args) {
    // Tester t = new Tester(args);
    // t.run();
    Benchmarker b = new Benchmarker();
    b.run(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Double.parseDouble(args[2]));
  }
  
}
