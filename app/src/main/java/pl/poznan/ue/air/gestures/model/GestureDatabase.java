package pl.poznan.ue.air.gestures.model;

import java.util.ArrayList;


/**
 * Created by Hubert Bossy on 2017-11-15.
 */

public class GestureDatabase {

    public GestureDatabase(ArrayList<Gesture> data) {
        this.data = data;
    }

    public GestureDatabase() {
        this.data =  new ArrayList<>(2);
    }

    public ArrayList<Gesture> getAll() {
        return data;
    }

    private ArrayList<Gesture> data;

    public Gesture getAt(int pos){
        return data.get(pos);
    }

    public void put(Gesture obj){
        data.add(obj);
    }

    public void update(int position, Gesture obj){
        data.set(position, obj);
    }

    public void remove(int pos) {
        data.remove(pos);
    }

    public String[] getAllTitles(){
        String[] titles = new String[data.size()];
        for(int i = 0; i < data.size(); ++i){
            titles[i] = data.get(i).getTitle();
        }
        return titles;
    }
    // TODO: 2017-11-18  save database to JSON file
}
