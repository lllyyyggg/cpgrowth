package refine;


import com.lanyage.datamining.datastructure.Item;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

// TESTED
public class ItemCountFacade {
    public static Map<String, Integer> load(String countFile) {
        Counter counter = ItemCounter.Factory.create(countFile);
        return counter.load();
    }

    public static void sortAndSaveTransaction(String countFile, String source1, String source2, String dest) {
        Map<String, Integer> itemCount = load(countFile);
        Set<Transaction> set1 = extractTransaction(itemCount, source1);
        Set<Transaction> set2 = extractTransaction(itemCount, source2);
        BufferedWriter writer = FunctorFactory.getBufferWriterGetter().apply(dest);
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.addAll(set1);
        transactionList.addAll(set2);
        Collections.sort(transactionList);
        try {
            for (Transaction transaction : transactionList) {
                String transactionString = transaction.toString();
                if (set1.contains(transaction)) {
                    writer.write(transactionString + ",1");
                }else {
                    writer.write(transactionString + ",2");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("写入异常");
        } finally {
            try {
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException("刷新异常");
            }
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException("关闭异常");
                }
            }
        }
    }

    public static Set<Transaction> extractTransaction(Map<String, Integer> itemCount, String source) {
        BufferedReader br = FunctorFactory.getBufferReaderGetter().apply(source);
        String line;
        Set<Transaction> set = new HashSet<>();
        try {
            while (null != (line = br.readLine()) && !"".equals(line = line.trim())) {
                String[] items = SequenceSplitter.split(line);
                Transaction transaction = new Transaction();
                for (String item : items) {
                    transaction.addItem(item, itemCount.get(item));
                }
                transaction.sort();
                set.add(transaction);
            }
        } catch (IOException e) {
            throw new RuntimeException("读取异常");
        } finally {
            try {
                if (null != br) {
                    br.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("关闭异常");
            }
        }
        return set;
    }

    public static int getAndSaveItemCount(String source1, String source2, String dest) {
        Counter counter1 = ItemCounter.Factory.create(source1);
        Counter counter2 = ItemCounter.Factory.create(source2);
        Map<String, Integer> m1 = counter1.getItemCountMap();
        Map<String, Integer> m2 = counter2.getItemCountMap();
        Function<Map<String, Integer>, Function<Map<String, Integer>, Map<String, Integer>>> mapMerger = FunctorFactory.getMapMerger();
        Map<String, Integer> mergedMap = mapMerger.apply(m1).apply(m2);
        Writer writer = ItemWriter.Factory.create(dest);
        List<Map.Entry<String, Integer>> list = new ArrayList<>(mergedMap.entrySet());
        Collections.sort(list, (entry1, entry2) -> {
            int valueGap = entry2.getValue().compareTo(entry1.getValue());
            if (0 == valueGap) {
                return entry1.getKey().compareTo(entry2.getKey());
            }
            return valueGap;
        });
        return writer.writeItemCounts(list);
    }

    interface Writer {
        int writeItemCounts(List<Map.Entry<String, Integer>> sortedEntry);

        void writeItemCount(String item, Integer count) throws IOException;
    }

    interface Counter {
        Map<String, Integer> getItemCountMap();

        String[] getItems(String transactionString);

        Map<String, Integer> load();
    }

    static class ItemWriter implements Writer {
        private String dest;
        private BufferedWriter bufferedWriter;

        static class Factory {
            public static ItemWriter create(String dest) {
                return new ItemWriter(dest);
            }
        }

        private ItemWriter(String dest) {
            this.dest = dest;
            this.bufferedWriter = FunctorFactory
                    .getBufferWriterGetter()
                    .apply(dest);
        }

        public int writeItemCounts(List<Map.Entry<String, Integer>> sortedEntry) {

            try {
                for (Map.Entry<String, Integer> entry : sortedEntry) {
                    // write
                    writeItemCount(entry.getKey(), entry.getValue());
                    bufferedWriter.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException("写入异常" + dest);
            } finally {
                try {
                    bufferedWriter.flush();
                } catch (IOException e) {
                    throw new RuntimeException("刷新异常");
                }
                if (null != bufferedWriter) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e) {
                        throw new RuntimeException("关闭异常");
                    }
                }
            }
            return sortedEntry.size();
        }

        @Override
        public void writeItemCount(String item, Integer count) throws IOException {
            bufferedWriter.write(item + " " + count);
        }
    }

    static class ItemCounter implements Counter {
        private String source;
        private BufferedReader br;

        private ItemCounter(String source) {
            this.source = source;
            this.br = FunctorFactory.getBufferReaderGetter().apply(source);
        }

        static class Factory {
            public static ItemCounter create(String dest) {
                return new ItemCounter(dest);
            }
        }

        public Map<String, Integer> getItemCountMap() {
            Map<String, Integer> map = new HashMap<>();
            String transactionString;
            try {
                while (null != (transactionString = br.readLine())
                        && !"".equals(transactionString = transactionString.trim())) {
                    // split
                    String[] items = getItems(transactionString);
                    for (String item : items) {
                        Integer count = map.getOrDefault(item, null);
                        if (null == count) {
                            map.put(item, 1);
                        } else {
                            map.put(item, count + 1);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("读取异常" + source);
            } finally {
                if (null != br) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        throw new RuntimeException("关闭异常");
                    }
                }
            }
            return map;
        }

        @Override
        public String[] getItems(String transactionString) {
            return SequenceSplitter.split(transactionString);
        }

        @Override
        public Map<String, Integer> load() {
            Map<String, Integer> map = new HashMap<>();
            try {
                String itemCount;
                Integer total = 0;
                while (null != (itemCount = br.readLine()) && !"".equals(itemCount = itemCount.trim())) {
                    String[] splits = SequenceSplitter.split(itemCount);
                    map.put(splits[0], Integer.valueOf(splits[1]));
                    total += Integer.valueOf(splits[1]);
                }
                System.out.println(total);
            } catch (IOException e) {
                throw new RuntimeException("读取异常");
            } finally {
                if (null != br) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        throw new RuntimeException("关闭异常");
                    }
                }
            }
            return map;
        }
    }
}
