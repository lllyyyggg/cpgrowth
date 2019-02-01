package com.lanyage.version.two;

import com.lanyage.version.two.utils.Launcher;
public class Main {
    public static void main(String[] args) {
        try {
            Launcher.main(new String[]{
                    "/Users/lanyage/git/cpgrowth/resources/DATASET_I",
                    "/Users/lanyage/git/cpgrowth/resources/DATASET_II",
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
