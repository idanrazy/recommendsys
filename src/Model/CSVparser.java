package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CSVparser {


    static String cvsSplitBy = ",";


    public static HashMap<Integer, String[]> parse(int lines){
        HashMap<Integer, String[]> dataset = new HashMap<>();

        String line ="";
        int i = 0;
        try {
            InputStream is = CSVparser.class.getResourceAsStream("ratings.csv");
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);
//            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine() ) != null && i < lines ) {

                // use comma as separator
                String[] row = line.split(cvsSplitBy);
                dataset.put(i,row);
                i++;
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return  dataset;
    }
    public static void config(String cvsSplitBy){

        CSVparser.cvsSplitBy = cvsSplitBy;

    }


}
