public class Repelant extends Element {
    private double life_span;
    private double radius_effect;
    private double age;

    public Repelant(double x, double y, double life_span, double radius_effect, double height, double width) {
        super(x, y, height, width);
        this.life_span = life_span;
        this.radius_effect = radius_effect;
        this.age = 0;
    }

    public double getLifeSpan() {
        return this.life_span;
    }

    public double getRadiusEffect() {
        return this.radius_effect;
    }

    public boolean update() {
        this.age++;
        if (this.age > this.life_span) {
            return true;
        }
        return false;
    }
}
