public class Pheromone extends Element {
    private int life_span;
    private int radius_effect;

    public Pheromone(int x, int y, int life_span, int radius_effect) {
        super(x, y);
        this.life_span = life_span;
        this.radius_effect = radius_effect;
    }

    public int getLifeSpan() {
        return this.life_span;
    }

    public int getRadiusEffect() {
        return this.radius_effect;
    }
}
