package refine;

public class SequenceSplitter {
    public static String[] split(String sequence, String delim) {
        if (null != sequence) return sequence.trim().split(delim);
        return null;
    }
    public static String[] split(String sequence) {
        return split(sequence, " ");
    }
}
