import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Population {

    static Random generator = new Random(0);

    private ArrayList<Individual> individuo;
    private Individual individ;

    public Population () {

    }

    public Population(int l, int n) {
        this.individuo = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            this.individuo.add(new Individual(createChromossome(l)));
        }
    }

    public Population(int l, int n, IProblem fitness) {
        this.individuo = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            this.individuo.add(new Individual(createChromossome(l), fitness));
        }
    }

    public Population(double []fitness, int l, int n) {
        this.individuo = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            this.individuo.add(new Individual(createChromossome(l), fitness[i]));
        }
    }

    public Population (ArrayList<Individual> individuos) {
        this.individuo = individuos;
    }

    public Population (ArrayList<Individual> individuos, IProblem fitness) {
        this.individuo = new ArrayList<>();
        for (int i = 0; i < individuos.size(); i++) {
            this.individuo.add(new Individual(individuos.get(i).getChromossoma(), fitness));
        }
    }

    public ArrayList<Individual> getIndividuo () {
        return this.individuo;
    }

    public String createChromossome (int l) {
        String chromo = "";
        double d;
        for (int i=0; i < l; i++) {
            d = generator.nextDouble();
            if (d < 0.5) {
                chromo+='0';
            }
            else {
                chromo+='1';
            }
        }
        return chromo;
    }

    private Individual winner () {
        double d1 = generator.nextDouble();
        int competitor1 = (int) (Math.round(d1 * (this.individuo.size() - 1)));
        double d2 = generator.nextDouble();
        int competitor2 = (int) (Math.round(d2 * (this.individuo.size() - 1)));
        if (this.individuo.get(competitor1).getFitness() >= this.individuo.get(competitor2).getFitness()) {
            return this.individuo.get(competitor1);
        }
        return this.individuo.get(competitor2);
    }

    public ArrayList<Individual> tournament () {
        ArrayList<Individual> winners = new ArrayList<>();
        for (int i = 0; i < this.individuo.size(); i++) {
            winners.add(winner());
        }
        return winners;
    }

    private double cumulativeSum () {
        double total = 0;
        for (int i = 0; i < this.individuo.size(); i++) {
            total += this.individuo.get(i).getFitness();
        }
        return total;
    }

    private int rouletteWinner(double total) {
        double d = generator.nextDouble();
        double cumulativo = 0;
        for (int i = 0; i < this.individuo.size(); i++) {
            cumulativo += (this.individuo.get(i).getFitness())/total;
            if (cumulativo >= d) {
                return i;
            }

        }
        return -1;
    }

    private ArrayList<Individual> getResult (double total) {
        ArrayList<Individual> result = new ArrayList<>();
        for (int i = 0; i < this.individuo.size(); i++) {
            int index = rouletteWinner(total);
            result.add(this.individuo.get(index));
        }
        return result;
    }

    public ArrayList<Individual> roulette() {
        ArrayList<Individual> result;
        Collections.sort(this.individuo, (s1, s2) -> (int) (s2.getFitness() - s1.getFitness()));
        double total = cumulativeSum();
        result = getResult(total);
        Collections.sort(result, (s1, s2) -> (s1.getChromossoma().compareTo(s2.getChromossoma())));
        return result;
    }

    public Individual[] onePointCrossOver(Individual s1, Individual s2) {
        ArrayList<Individual> result = new ArrayList<>();
        int n = (int) (1+Math.round(generator.nextDouble() * (s1.getChromossoma().length() - 1 - 1)));
        Individual p1 = new Individual(s1.getChromossoma(), s1.getFitness());
        Individual p2 = new Individual(s2.getChromossoma(), s2.getFitness());
        result.add(new Individual(p1.onePointCrossover(n, p2)));
        result.add(new Individual(p2.onePointCrossover(n, p1)));
        return new Individual[]{result.get(0), result.get(1)};
    }

    public void uniformCrossover () {
        int n = this.individuo.get(0).getChromossoma().length();
        double d;
        for (int i = 0; i < n; i++) {
            d = generator.nextDouble();
            if (d < 0.5) {
                this.individuo.get(0).exchangeGene(i, this.individuo.get(1));
            }
        }
    }

    public void bitFlipMutation (double pm, int index) {
        int n = this.individuo.get(0).getChromossoma().length();
        double d;
        for (int i = 0; i < n; i++) {
            d = generator.nextDouble();
            if (d < pm) {
                this.individuo.get(index).exchangeGene(i);
            }
        }
    }

    public int[] randomPermutation (int n) {
        int []v = new int[n];
        double d;
        int index;
        for (int i = 0; i < n; i++) {
            v[i] = i;
        }
        for (int i = 0; i < n-1; i++) {
            d = generator.nextDouble(1 - (double)i/(n-1))+(double)i/(n-1);
            index = (int) Math.round(d*(n-1));
            int old = v[i];
            v[i] = v[index];
            v[index] = old;
        }
        return v;
    }

    public ArrayList<Individual> randomPermutationNoReplacement() {
        ArrayList<Individual> result = new ArrayList<>();
        result.addAll(this.individuo);
        int n = this.individuo.size();
        double d;
        int index;
        for (int i = 0; i < n-1; i++) {
            d = generator.nextDouble();
            index = i + (int) Math.round(d*(n-1-i));
            Individual old = result.get(i);
            result.set(i, result.get(index));
            result.set(index,old);
        }
        return result;
    }

    public Population tournamentSelecNoReplacement (int s) {
        ArrayList<Individual> result = new ArrayList<>();
        ArrayList<Individual> permutation;
        Individual best;
        for (int i = 0; i < s; i++) {
            permutation = randomPermutationNoReplacement();
            for (int j = 0; j < this.individuo.size(); j+=s) {
                best = permutation.get(j);
                for (int k = j+1; k < j+s; k++) {
                    if (best.getFitness() < permutation.get(k).getFitness()) {
                        best = permutation.get(k);
                    }
                }
                result.add(best);
            }
        }
        return new Population(result);
    }



    public double maxFitness() {
        double max = this.individuo.get(0).getFitness();
        for (int i = 1; i < this.individuo.size(); i++) {
            if (this.individuo.get(i).getFitness() > max) {
                max = this.individuo.get(i).getFitness();
            }
        }
        return max;
    }

    public double medioFitness () {
        double medio = 0;
        for (int i = 0; i < this.individuo.size(); i++) {
            medio+= this.individuo.get(i).getFitness();
        }
        return medio/this.individuo.size();
    }

    public double minFitness () {
        double min = this.individuo.get(0).getFitness();
        for (int i = 1; i < this.individuo.size(); i++) {
            if (this.individuo.get(i).getFitness() < min) {
                min = this.individuo.get(i).getFitness();
            }
        }
        return min;
    }

    public Population crossOver (double pc, IProblem fitness) {
        for (int i = 0; i < this.individuo.size(); i+=2) {
            if (generator.nextDouble() <= pc) {
                Individual []cross = onePointCrossOver(this.individuo.get(i), this.individuo.get(i+1));
                this.individuo.set(i, cross[0]);
                this.individuo.set(i+1, cross[1]);
            }
        }
        return new Population(this.individuo, fitness);
    }

    public Population bitFlip (double pm, IProblem fitness) {
        for (int i = 0; i < this.individuo.size(); i++) {
            bitFlipMutation(pm, i);
        }
        return new Population(this.individuo, fitness);
    }

    public String toString(int generation) {
        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
        unusualSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", unusualSymbols);
        return generation + ": " + df.format(maxFitness()) + " " + df.format(medioFitness()) + " " + df.format(minFitness());
    }
}
