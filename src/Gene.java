import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gene {
    public Allele allele1;
    private Allele allele2;
    private int allele_length;
    private Random random = new Random();

    public Gene(Allele allele1, Allele allele2) {
        this.allele1 = allele1;
        this.allele2 = allele2;
        this.allele_length = allele1.getLength();
    }

    public Allele getAllele1() {
        return allele1;
    }

    public Allele getAllele2() {
        return allele2;
    }

    public int getAlleleLength() {
        return allele_length;
    }

    public double getProba() {
        double sum_of_ones = 0;
        for (int i = 0; i < allele_length; i++) {
            if (random.nextDouble() < 0.5) {
                sum_of_ones += allele1.getI(i);
            } else {
                sum_of_ones += allele2.getI(i);
            }
        }
        double proba = sum_of_ones / allele_length;
        return proba;
    }

    public Gene getMutatedGene(double mutation_probability) {
        int[] mutated_allele1 = new int[allele_length];
        int[] mutated_allele2 = new int[allele_length];
        for (int i = 0; i < allele_length; i ++) {
            if (random.nextDouble() < mutation_probability) {
                mutated_allele1[i] = allele1.getI(i)^1;
            } else {
                mutated_allele1[i]  = allele1.getI(i);
            }            
            if (random.nextDouble() < mutation_probability) {
                mutated_allele2[i] = allele2.getI(i)^1;
            } else {
                mutated_allele2[i]  = allele2.getI(i);
            }
        }
        Allele all1 = new Allele(mutated_allele1);
        Allele all2 = new Allele(mutated_allele2);
        Gene mutated_gene = new Gene(all1, all2);
        return mutated_gene;
    }
}
