public class Pheromone extends Element {
    private double life_span;
    private double radius_effect;

    public Pheromone(double x, double y, double life_span, double radius_effect) {
        super(x, y);
        this.life_span = life_span;
        this.radius_effect = radius_effect;
    }

    public double getLifeSpan() {
        return this.life_span;
    }

    public double getRadiusEffect() {
        return this.radius_effect;
    }
}
