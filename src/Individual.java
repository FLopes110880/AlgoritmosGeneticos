import java.util.Random;

public class Individual{

    private String chromossoma;
    private double fitness;

    public Individual (String chromossoma) {
        this.chromossoma = chromossoma;
    }

    public Individual (String chromossoma, double fitness) {
        this.chromossoma = chromossoma;
        this.fitness = fitness;
    }

    public Individual (String chromossoma, IProblem fitness) {
        this.chromossoma = chromossoma;
        this.fitness = fitness.fitness(this);
    }

    public String getChromossoma() {
        return this.chromossoma;
    }

    public double getFitness() {
        return this.fitness;
    }

    public String onePointCrossover (int n, Individual parent2) {
        return this.chromossoma.substring(0, n) + parent2.chromossoma.substring(n);
    }

    public void exchangeGene (int n, Individual parent) {
        char []parent1 = this.chromossoma.toCharArray();
        char []parent2 = parent.chromossoma.toCharArray();
        char old = parent1[n];
        parent1[n] = parent2[n];
        parent2[n] = old;
        this.chromossoma = String.valueOf(parent1);
        parent.chromossoma = String.valueOf(parent2);
    }

    public void exchangeGene (int n) {
        char[] parent1 = this.chromossoma.toCharArray();
        if (parent1[n] == '1')
            parent1[n] = '0';
        else
            parent1[n] = '1';
        this.chromossoma = String.valueOf(parent1);
    }
}
