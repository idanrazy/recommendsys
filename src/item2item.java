import java.util.*;
import java.util.Map.Entry;

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
    public Double predict(Entry<String,Double>[] user_rank ,String item,String user){

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

        String max1 = Collections.max(cosimtotal.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();;
        double max1sim = cosimtotal.get(max1);
        cosimtotal.remove(max1);
        String max2 = Collections.max(cosimtotal.entrySet(), (entry1, entry2) -> (int) (entry1.getValue() - entry2.getValue())).getKey();
        double max2sim = cosimtotal.get(max2);
        cosimtotal.remove(max2);
        double rank1=0;
        double rank2=0;

        for(int i = 0 ; i <user_rank.length;i++){

            if(user_rank[i].getKey().equals(max1))
            {
                rank1 = user_rank[i].getValue();
            }
            if(user_rank[i].getKey().equals(max2))
            {
                rank2 = user_rank[i].getValue();
            }
        }

        return (Double)(rank1*max1sim + rank2*max2sim)/(max1sim+max2sim);
    }
}
