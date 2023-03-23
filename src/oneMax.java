public class oneMax implements IProblem{
    public double fitness(Individual individuo) {
        assert (Integer.parseInt(individuo.getChromossoma()) >= 1000);
        int result = 0;
        for (int i=0; i < individuo.getChromossoma().length(); i++) {
            //if (this.individuo[i].getChromossoma().charAt(i) == '1') {
            if (individuo.getChromossoma().charAt(i) == '1') {
                result++;
            }
        }
        return result;
    }
}
