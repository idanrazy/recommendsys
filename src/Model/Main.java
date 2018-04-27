

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;



public class Main {


    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        HashMap<Integer, String[]> dataset = CSVparser.parse("./csvdata\\ratings.csv", 300000);
        DataFrame df = new DataFrame(dataset);
        String[] columns = df.getcolumns();
        ArrayList<String[]> selectedrows = df.select("userId","6");
        item2item t = new item2item(df);
        HashMap<String,Double> myranks = new HashMap<>();
        for (int i = 0; i<selectedrows.size();i++) {
            myranks.put(selectedrows.get(i)[1],Double.parseDouble(selectedrows.get(i)[2]));
        }
        Entry<String, Double>[] temp = new Entry[myranks.size()];
        myranks.entrySet().toArray(temp);
        //double rank = t.predict(temp,"1");
        List test = t.predictmovelist(df,temp);
        System.out.println("running time:" + (System.currentTimeMillis() - start) / 1000);

    }
}
