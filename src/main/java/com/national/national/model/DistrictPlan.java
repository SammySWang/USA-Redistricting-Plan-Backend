package com.national.national.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class DistrictPlan {

    public String state;
    public static HashMap<String, DistrictPlan> enacts;
    public ArrayList<District> districts;
    public HashMap<District.MM, Integer> mm;
    public double popdiff;
    public double deviationFromAverageEnacted;
    public double graphCompactness;
    //public static ArrayList<Double> popdiffArrayList = new ArrayList<>();
    public double deviation;

    public DistrictPlan(JsonElement districtsJson) {
        this.districts = new ArrayList<>();
        this.mm = new HashMap<>();
        //this.popdiffArrayList = new ArrayList<Double>();
        ArrayList<Integer> vap = new ArrayList<>();
        //ArrayList<Double> deviationFromAverageEnacted = new ArrayList<>();
        double devFromAvgEnactedForAllDistrict = 0;
        double graphCompactnessForAllDistrict = 0;
        ArrayList<Double> graphCompactness = new ArrayList<>();
        JsonArray districtArray = districtsJson.getAsJsonObject().getAsJsonArray("districts");
        for(JsonElement district: districtArray) {
            JsonObject districtObject = district.getAsJsonObject();
            JsonArray precinctArray = districtObject.getAsJsonArray("precincts");
            ArrayList<Integer> precincts = new ArrayList<>();
            if (precinctArray != null) {
                for (int i=0;i<precinctArray.size();i++){
                    precincts.add(precinctArray.get(i).getAsInt());
                }
            }
            districts.add(new District(
                    districtObject.get("renumber").getAsInt(),
                    districtObject.get("vap").getAsInt(),
                    districtObject.get("hvap").getAsInt(),
                    districtObject.get("wvap").getAsInt(),
                    districtObject.get("bvap").getAsInt(),
                    districtObject.get("asianvap").getAsInt(),
                    precincts));
            vap.add(districtObject.get("vap").getAsInt());
           // deviationFromAverageEnacted.add(districtObject.get("areadevs").getAsDouble());
            devFromAvgEnactedForAllDistrict += districtObject.get("areadevs").getAsDouble();
            graphCompactnessForAllDistrict += districtObject.get("gc").getAsDouble();
        }
        long sum = 0;
        for(int pop: vap) {
            sum += pop;
        }
        //int i = 0;
        //System.out.println("vap size: "+vap.size());
        this.graphCompactness = graphCompactnessForAllDistrict/vap.size();
        this.deviationFromAverageEnacted = devFromAvgEnactedForAllDistrict;
        this.popdiff = (Collections.max(vap) - Collections.min(vap)) / (1.0 * sum / vap.size());
        //System.out.println("1 - popdiff" + (1 - this.popdiff));

        //System.out.println("population difference: "+this.popdiff);
        //System.out.println("graph compactness: "+this.graphCompactness);
        //System.out.println("graph compactness divided by total districts: "+this.graphCompactness/vap.size());
        //System.out.println("deviation from average enacted in districtings: "+this.deviationFromAverageEnacted);
        //this.popdiffArrayList.add(popdiff);
        //System.out.println(this.districts.size());
        //System.out.println(this.popdiffArrayList.size());
        //System.out.println(this.popdiff);
        this.districts.sort(Comparator.comparingInt(o->o.districtNum));

        findMM();
    }

    public void findMM() {
        int count = 0;
        for(District d: this.districts) {
            if (d.mm.HVAP == District.MM.HVAP) {
                //never got in
                if(!this.mm.containsKey(District.MM.HVAP)) {
                    //System.out.println("not contain");
                    this.mm.put(District.MM.HVAP, 0);
                }
                else {
                    count+=1;
                    //System.out.println("what is that: "+this.mm.get(District.MM.HVAP));
                    this.mm.put(District.MM.HVAP, this.mm.get(District.MM.HVAP));
                }
            }
        }
        //System.out.println(this.mm.values());
        //System.out.println("mm count: "+count);
    }

    public void calDeviation(ArrayList<District> enacted) {
        double diff = 0;
        for(int i = 0; i < enacted.size(); i++) {
            diff += Math.pow(enacted.get(i).VAP - districts.get(i).VAP, 2);

        }
        this.deviation = Math.pow(diff / enacted.size(), 0.5);
    }
}
