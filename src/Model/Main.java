package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;



public class Main {


    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        HashMap<Integer, String[]> dataset = CSVparser.parse("ratings.csv", 300000);
        DataFrame df = new DataFrame(dataset);
        item2item t = new item2item(df);
        System.out.println("running time:" + (System.currentTimeMillis() - start) / 1000);

    }
}
