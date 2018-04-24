import sun.nio.cs.CharsetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class Main {


    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        HashMap<Integer, String[]> dataset = CSVparser.parse("C:\\Users\\idanr\\Desktop\\לימודים\\ג\\סמסטר ב\\הכנה לפוריקט\\ratings.csv", 30000);
        DataFrame df = new DataFrame(dataset);
        System.out.println("running time:" + (System.currentTimeMillis() - start) / 1000);
        item2item t = new item2item(df);
        HashMap<String,Double> myranks = new HashMap<>();
        myranks.put("2",3.5);
        myranks.put("32",3.5);
        myranks.put("296",4.0);
        myranks.put("50",3.5);
        Entry<String, Double>[] temp = new Entry[myranks.size()];
        myranks.entrySet().toArray(temp);
        double rank = t.predict(temp,"29","1");
        System.out.println(rank);

    }
}
