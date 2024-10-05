import java.util.ArrayList;
import java.util.List;

public class Genotype {
    private List<int[]> genotype;
    
    public Genotype(int[] allele1, int[] allele2) {
        List<int[]> genotype = new ArrayList<>();
        genotype.add(allele1);
        genotype.add(allele2);
        this.genotype = genotype;
    }

    public List<int[]> getGenotype() {
        return this.genotype;
    } 
}
