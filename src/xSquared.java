public class xSquared implements IProblem{
    public double fitness (Individual individuo) {
        int n = individuo.getChromossoma().length();
        int result = 0;
        for (int i=0; i < n; i++) {
            //if (this.individuo[i].getChromossoma().charAt(i) == '1') {
            if (individuo.getChromossoma().charAt(i) == '1') {
                result+=Math.pow(2,n-1-i);
            }
        }
        return Math.pow(result,2);
    }
}
