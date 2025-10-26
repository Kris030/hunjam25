package hu.hunjam25.dlhc;

public class Utils {
    // source: raylib easings
    public static float EaseOut(float t, float b, float c, float d) {
        if ((t /= d) < (1.0f / 2.75f)) {
            return (c * (7.5625f * t * t) + b);
        } else if (t < (2.0f / 2.75f)) {
            float postFix = t -= (1.5f / 2.75f);
            return (c * (7.5625f * (postFix) * t + 0.75f) + b);
        } else if (t < (2.5 / 2.75)) {
            float postFix = t -= (2.25f / 2.75f);
            return (c * (7.5625f * (postFix) * t + 0.9375f) + b);
        } else {
            float postFix = t -= (2.625f / 2.75f);
            return (c * (7.5625f * (postFix) * t + 0.984375f) + b);
        }
    }

    public static float EaseIn(float t, float b, float c, float d) {
        return (c * t * t + b);
    }

//    public static float EaseOut(float t, float b, float c, float d) {
//        if (t == 0.0f) return b;
//        if ((t /= d) == 1.0f) return (b + c);
//
//        float p = d * 0.3f;
//        float a = c;
//        float s = p / 4.0f;
//
//        return (float) (a * Math.pow(2.0f, -10.0f * t) * Math.sin((t * d - s) * (2.0f * Math.PI) / p) + c + b);
//    }

    public static float interpolateExp(float dt, float start, float end) {
        var weight = (float) Math.exp(-dt * 100.0f);
        var iweight = 1.0f - weight;
        return start * weight + iweight * end;
    }
}
