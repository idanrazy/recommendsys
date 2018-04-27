package Model;

import java.util.*;
import java.util.Map.Entry;


public class DataFrame {

    public Map<String,Integer> columns;
    public HashMap<Integer,String[]> dataset;
    public int size;

    public DataFrame(HashMap<Integer,String[]> dataset){
        columns = new HashMap<>();
        this.dataset = dataset;
        size = dataset.size();
        String[] col = dataset.get(0);
        for (int i = 0 ; i <col.length;i++){
            columns.put(col[i],i);
        }

    }

    public String[] getrow(int index){
     return dataset.get(index);

    }
    public String[] getcolumns(){
        String[] cols = new String[columns.keySet().size()];
        columns.keySet().toArray(cols);
        return cols;
    }


    public ArrayList select(String column,String value){
        ArrayList<String[]> mylist = new ArrayList<String[]>();
        for(int i = 1; i < dataset.size();i++){
            if(dataset.get(i)[columns.get(column)].equals(value)){
                mylist.add(dataset.get(i));
            }
        }
        return mylist;
    }

}
