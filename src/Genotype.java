import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genotype {
    private List<int[]> genotype;
    private int[] allele1;
    private int[] allele2;
    private Random random = new Random();
    
    public Genotype(int[] allele1, int[] allele2) {
        List<int[]> genotype = new ArrayList<>();
        this.allele1 = allele1;
        this.allele2 = allele2;
        genotype.add(allele1);
        genotype.add(allele2);
        this.genotype = genotype;
    }

    public ArrayList<int[]> getChildGenotype(double mutation_probability) {
        ArrayList<int[]> child_genotype = new ArrayList<>();
        int allele_length = allele1.length;
        int[] child_allele1 = new int[allele_length];
        int[] child_allele2 = new int[allele_length];
        for (int i = 0; i < allele_length; i++) {
            double randomValue1 = random.nextDouble();
            if (randomValue1 < mutation_probability) {
                child_allele1[i] = allele1[i]^1;
            } else {
                child_allele1[i] = allele1[i];
            }
            double randomValue2 = random.nextDouble();
            if (randomValue2 < mutation_probability) {
                child_allele2[i] = allele2[i]^1;
            } else {
                child_allele2[i] = allele2[i];
            }
        }
        child_genotype.add(child_allele1);
        child_genotype.add(child_allele2);
        return child_genotype;
    }

    public boolean spreadOrNot() {
        double proba = getSpreadProba();
        // if (random.nextDouble() < proba) {
        //     return true;
        // }
        if (proba > 0.5) {
            return true;
        }
        return false;
    }

    public double getSpreadProba() {
        double allele_length = allele1.length;
        double sum_of_ones = 0;
        for (int i = 0; i < allele_length; i++) {
            sum_of_ones += allele1[i];
            sum_of_ones += allele2[i];
        }
        double proba = sum_of_ones / (allele_length*2);

        return proba;
    }

    public List<int[]> getGenotype() {
        return this.genotype;
    } 

    public int[] getAllele1() {
        int[] allele1 = this.genotype.get(0);
        return allele1;
    }

    public int[] getAllele2() {
        int[] allele2 = this.genotype.get(1);
        return allele2;
    }
}
