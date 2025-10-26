package hu.hunjam25.dlhc;

public record Vec2(float x, float y) {

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vec2 add(Vec2 v) {
        return new Vec2(x + v.x, y + v.y);
    }

    public Vec2 sub(Vec2 v) {
        return new Vec2(x - v.x, y - v.y);
    }

    public Vec2 mul(float f) {
        return new Vec2(x * f, y * f);
    }

    public Vec2 div(float f) {
        return new Vec2(x / f, y / f);
    }

    public float dist(Vec2 v) {
        return sub(v).length();
    }

    public Vec2 norm() {
        return div(length());
    }

    public Vec2 neg() {
        return new Vec2(-x, -y);
    }
}
