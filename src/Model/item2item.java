package Model;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;

import static Model.CSVparser.cvsSplitBy;

public class item2item {

    public HashMap<String, HashMap<String, Double>> itemsgrades;   //build matrix for item - > user,rank
    public item2item(DataFrame df) {
        itemsgrades = new HashMap<>();
        //build user to item matrix
        for (int i = 1 ; i <df.size;i++){
            String[] temp = df.getrow(i);
            if(!itemsgrades.containsKey(temp[1])) {
                itemsgrades.put(temp[1], new HashMap<>());
                itemsgrades.get(temp[1]).put(temp[0],Double.parseDouble(temp[2]));
            }
            else{
                if(!itemsgrades.get(temp[1]).containsKey(temp[0]))
                    itemsgrades.get(temp[1]).put(temp[0],Double.parseDouble(temp[2]));
            }
        }
    }
    public Double predict(Entry<String,Double>[] user_rank ,String item){

        //the ranks of the item we want to predict
        HashMap<String,Double> item_rank = itemsgrades.get(item);
        Map<String,Double> cosimtotal = new HashMap<>();
        //calculate similarity
        for (int i =0 ; i <user_rank.length;i++){
            String item_i = user_rank[i].getKey();
            double mone = 0;
            double item1_sum = 0;
            double item2_sum = 0;
            if(!item_i.equals(item))
            {

                if(itemsgrades.containsKey(item_i)) {
                    //all users rank of item i
                    HashMap<String, Double> user_i_rank = itemsgrades.get(item_i);
                    //each user_rank
                    for (Entry<String, Double> e : item_rank.entrySet()) {
                        //if item i and item_to_rank have rank by the same users
                        if (user_i_rank.containsKey(e.getKey())) {
                            double item1 = e.getValue();
                            double item2 = user_i_rank.get(e.getKey()).doubleValue();
                            mone += item1 * item2;
                            item1_sum += Math.pow(item1, 2);
                            item2_sum += Math.pow(item2, 2);
                        }
                    }
                }
            }
            else
                continue;

            double cosim =  (mone)/((Math.pow(Math.pow(item1_sum,2),0.5))*(Math.pow(Math.pow(item2_sum,2),0.5)));
            if(!Double.isNaN(cosim))
                cosimtotal.put(item_i,cosim);
            }


        //calculate rank
        try {
            if(cosimtotal.size()>1) {
                String max1 = Collections.max(cosimtotal.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();
                double max1sim = cosimtotal.get(max1);
                cosimtotal.remove(max1);
                String max2 = Collections.max(cosimtotal.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();
                double max2sim = cosimtotal.get(max2);
                cosimtotal.remove(max2);
                double rank1 = 0;
                double rank2 = 0;


                for (int i = 0; i < user_rank.length; i++) {

                    if (user_rank[i].getKey().equals(max1)) {
                        rank1 = user_rank[i].getValue();
                    }
                    if (user_rank[i].getKey().equals(max2)) {
                        rank2 = user_rank[i].getValue();
                    }
                }


                return (Double) (rank1 * max1sim + rank2 * max2sim) / (max1sim + max2sim);
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        return Double.NaN;
    }

    public ArrayList<Pair<String,Double>> predictmovelist(DataFrame df,Entry<String,Double>[] user_rank){

        String[] list = df.uniquevlaue("movieId");
        ArrayList<Pair<String,Double>> moviesrank = new ArrayList<>();
        try {
            for (int i = 0; i < list.length; i++) {
                String item = list[i];
                //System.out.println(i + ":item " +list[i]);

                double rank = predict(user_rank, item);
                if(!Double.isNaN(rank))
                    moviesrank.add(new Pair(item, rank));
            }
            Collections.sort(moviesrank, Comparator.comparing(p -> -p.getValue()));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return moviesrank;
        }
        return moviesrank;
    }

    public static String[] getDetailsMovie(int id ) {

        String[] row = new String[3];
        String line ="";
        String idMovie;
        int i = 0;

        try {
            InputStream is = CSVparser.class.getResourceAsStream("movies.csv");
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);
//            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine() ) != null  ) {
                idMovie = line.replaceAll("\\,.*", "");

                if(isInteger(idMovie,10)){
                    int foo = Integer.parseInt( idMovie);
                    if(foo == id){

                        row[0] = new String(line.substring(0,line.indexOf(",")));
                        row[1] = new String(line.substring(line.indexOf(",")+1, line.lastIndexOf(","))).replaceAll("\"", "");
                        row[2] = new String(line.substring(line.lastIndexOf(",")+1));

                        break;
                    }
                }
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return  row;
    }
    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
}
