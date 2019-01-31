package refine;

import com.lanyage.datamining.enums.FilePathEnum;
public class Main {
    public static void main(String[] args) {
        try {
            Launcher.main(new String[]{
                    FilePathEnum.getPath("dataset1"),
                    FilePathEnum.getPath("dataset2"),
                    "0.6",
                    "0.05",
                    "cpnodelist"
                    //"cpgrowth"
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
