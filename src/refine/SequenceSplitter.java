package refine;

// TESTED PASS
public class SequenceSplitter {
    public static String[] split(String sequence, String delim) {
        if (null != sequence) {
            return sequence.trim().split(delim);
        } else {
            return null;
        }
    }

    public static String[] split(String sequence) {
        return split(sequence, " ");
    }
}
