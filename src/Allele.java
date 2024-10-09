public class Allele {
    private int[] allele;

    public Allele(int[] allele) {
        this.allele = allele;
    }

    public int[] getAllele() {
        return allele;
    }

    public int getLength() {
        int length = allele.length;
        return length;
    }

    public double getSum() {
        double sum = 0;
        for (int i = 0; i < getLength(); i++) {
            sum += allele[i];
        }
        return sum;
    }

    public int getI(int i) {
        return allele[i];
    }

    public void setI(int i, int value) {
        allele[i] = value;
    }
}
