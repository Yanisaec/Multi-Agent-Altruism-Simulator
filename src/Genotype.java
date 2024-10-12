import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genotype {
    private Gene pheromone_gene;
    private Gene repelant_gene;
    private Random random = new Random();

    public Genotype(Gene pheromone_gene, Gene repelant_gene) {
    // public Genotype2(Gene pheromone_gene, Gene repelant_gene) {
        this.pheromone_gene = pheromone_gene;
        this.repelant_gene = repelant_gene;
        // this.repelant_gene = repelant_gene;
    }

    public Genotype getChildCoupleChildrenGenotype(Genotype parent1, Genotype parent2, double mutation_probability) {
        Gene parent2_phero_gene = parent2.getPheromoneGene();
        Gene parent2_repel_gene = parent2.getRepelantGene();

        Gene mutated_repel_gene1 = repelant_gene.getMutatedGene(mutation_probability);
        Gene mutated_repel_gene2 = parent2_repel_gene.getMutatedGene(mutation_probability);
        Gene children_repel_gene = getGeneFromParents(mutated_repel_gene1, mutated_repel_gene2);

        Gene mutated_phero_gene1 = pheromone_gene.getMutatedGene(mutation_probability);
        Gene mutated_phero_gene2 = parent2_phero_gene.getMutatedGene(mutation_probability);
        Gene children_phero_gene = getGeneFromParents(mutated_phero_gene1, mutated_phero_gene2);

        Genotype child_genotype2 = new Genotype(children_phero_gene, children_repel_gene);
        return child_genotype2;
    }

    public Gene getGeneFromParents(Gene gene1, Gene gene2) {
        int allele_length = pheromone_gene.getAlleleLength();
        int[] child_allele1 = new int[allele_length];
        int[] child_allele2 = new int[allele_length];

        for (int i = 0; i < allele_length; i ++) {
            if (random.nextDouble() < 0.5) {
                child_allele1[i] = gene1.getAllele1().getI(i);
            } else {
                child_allele1[i] = gene2.getAllele1().getI(i);
            }
            if (random.nextDouble() < 0.5) {
                child_allele2[i] = gene1.getAllele2().getI(i);
            } else {
                child_allele2[i] = gene2.getAllele2().getI(i);
            }
        }
        Allele all1 = new Allele(child_allele1);
        Allele all2 = new Allele(child_allele2);
        Gene child_gene = new Gene(all1, all2);
        return child_gene;
    }

    public double getSpreadPheromoneProba() {
        double proba = pheromone_gene.getProba();
        return proba;
    }

    public double getRepelantPheromoneProba() {
        double proba = repelant_gene.getProba();
        return proba;
    }

    public boolean isAPheromoneProducer() {
        double spread_proba = getSpreadPheromoneProba();
        if (spread_proba > random.nextDouble()) {
            return true;
        }
        return false;
    }

    public boolean isARepelantProducer() {
        double spread_proba = getRepelantPheromoneProba();
        if (spread_proba > random.nextDouble()) {
            return true;
        }
        return false;
    }
    public Gene getPheromoneGene() {
        return pheromone_gene;
    }

    public Gene getRepelantGene() {
        return repelant_gene;
    }
}
