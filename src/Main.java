import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.next());
        int l = Integer.parseInt(sc.next());
        int s = Integer.parseInt(sc.next());
        double pm = Double.parseDouble(sc.next());
        double pc = Double.parseDouble(sc.next());
        int g = Integer.parseInt(sc.next());
        sGA sga = new sGA(l, n, new oneMax());
        sga.geneticAlgorithmOnemax(s, pm, pc, g);
        sc.close();
    }
}