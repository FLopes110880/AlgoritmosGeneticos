import java.util.ArrayList;

public class sGA {
    Population populacao;
    IProblem fitness;

    public sGA(int l, int n, IProblem fitness) {
        this.populacao = new Population(l, n, fitness);
        this.fitness = fitness;
    }

    public void sGAmethod (int s, double pm, double pc) {
        this.populacao = this.populacao.tournamentSelecNoReplacement(s);
        this.populacao = this.populacao.crossOver(pc, this.fitness);
        this.populacao = this.populacao.bitFlip(pm, this.fitness);
    }

    public void oneGenerationOnemax (int s, double pm, double pc) {
        System.out.println(this.populacao.toString(0));
        sGAmethod(s, pm, pc);
        System.out.println(this.populacao.toString(1));
    }

    public void geneticAlgorithmOnemax (int s, double pm, double pc, int g) {
        System.out.println(this.populacao.toString(0));
        for (int i = 0; i < g; i++) {
            sGAmethod(s, pm, pc);
            System.out.println(this.populacao.toString(i+1));
        }
    }
}
