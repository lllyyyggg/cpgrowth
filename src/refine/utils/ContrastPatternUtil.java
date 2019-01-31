package refine.utils;

public class ContrastPatternUtil {
    public static boolean isContrastPatter(int c1, int c2, int n1, int n2, Double alpha, Double beta) {
        return (c1 > alpha * n1 && c2 <= beta * n2) || (c2 > alpha * n2 && c1 <= beta * n1);
    }
    public static boolean canPrune(int c1, int c2, int n1, int n2, double alpha) {
        return !(c1 > alpha * n1 || c2 > alpha * n2);
    }
}
