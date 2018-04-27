package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class CSVparser {




    static String cvsSplitBy = ",";


    public static HashMap<Integer, String[]> parse(String csvFile,int lines){
        HashMap<Integer, String[]> dataset = new HashMap<>();

        String line ="";
        int i = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
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
