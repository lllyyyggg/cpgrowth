package refine;

import java.io.*;
import java.util.Map;
import java.util.function.Function;

public class FunctorFactory {
    public static Function<String, BufferedReader> getBufferReaderGetter() {
        Function<String, BufferedReader> bufferedReaderGetter = source -> {
            try {
                return new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(source)));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(source + " not found !!!");
            }
        };
        return bufferedReaderGetter;
    }

    public static Function<String, BufferedWriter> getBufferWriterGetter() {
        Function<String, BufferedWriter> bufferedWriterGetter = dest -> {
            try {
                return new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(dest)));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(dest + " not found !!!");
            }
        };
        return bufferedWriterGetter;
    }

    public static Function<String, String> trim() {
        Function<String, String> trim = String::trim;
        return trim;
    }

    //T Map, R Map
    public static Function<Map<String, Integer>, Function<Map<String, Integer>, Map<String, Integer>>> getMapMerger() {
        Function<Map<String, Integer>, Function<Map<String, Integer>, Map<String, Integer>>>
                mapMerger = m -> m2 -> {
            Map<String, Integer> maxMap = m.size() > m2.size() ? m : m2;
            Map<String, Integer> minMap = maxMap == m ? m2 : m;
            for (Map.Entry<String, Integer> entry : minMap.entrySet()) {
                Integer value = maxMap.getOrDefault(entry.getKey(), null);
                if (null == value) {
                    maxMap.put(entry.getKey(), 1);
                } else {
                    maxMap.put(entry.getKey(), value + entry.getValue());
                }
            }
            return maxMap;
        };
        return mapMerger;
    }
}
