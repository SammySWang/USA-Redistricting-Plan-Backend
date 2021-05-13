package com.national.national.handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.national.national.model.District;
import com.national.national.model.DistrictPlan;
import com.national.national.model.Job;
import com.national.national.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.national.national.model.Job.filtered;

//import static com.national.national.model.Job.filtered;

@Service
public class JobHandler {

    @Autowired
    JobRepository jobrepo;
    private final int MAX_JOBS = 4;
    public static HashMap<String, ArrayList<DistrictPlan>> districtPlans = new HashMap<>();//job 1
    public static HashMap<String, ArrayList<District>> enacted = new HashMap<>();//job 1

    public int getStateJobs(String state){
        int numberStateJobs = 0;

        if(state.equals("Maryland")){
            state = "MD";
        }else if(state.equals("Virginia")){
            state = "VA";
        }else{
            state = "WA";
        }
        for(int i = 1; i < MAX_JOBS; i++) {
            String path = "src/main/resources/plans/" + state + "_plans_"+i+".json";
            try {
                FileReader validFile = new FileReader(path);
                if (validFile != null) {
                    //System.out.println("path " + i + ": " + path);
                    numberStateJobs += 1;
                }
            }catch(Exception e){
                System.out.println("missing files from get state jobs: "+e);
            }
        }
        return numberStateJobs;

    }

    public ArrayList<String> getPossibleConstraints(String state, int id) throws FileNotFoundException {

        ArrayList<String> container = new ArrayList();
        String path = "src/main/resources/plans/" + state + "/" + state + "_plans_"+id+".json";
        JsonObject checkForConstraints = new Gson().fromJson(new FileReader(path), JsonObject.class);
        JsonElement planArray = checkForConstraints.getAsJsonObject().getAsJsonArray("plans").get(0);
        //{"districts":[{"districtNumber":1,"population":726108,"white":605023,"bl

        JsonArray districtArray = planArray.getAsJsonObject().getAsJsonArray("districts");
        //[{"districtNumber":1,"population":726108,"white":605023,"

        JsonElement district = districtArray.get(0);
        //{"districtNumber":1,"population":726108,"white":605023,

        JsonElement population  = district.getAsJsonObject().get("population");
        JsonElement vap = district.getAsJsonObject().get("vap");
        JsonElement cvap = district.getAsJsonObject().get("cvap");
        JsonElement black = district.getAsJsonObject().get("black");
        JsonElement asian = district.getAsJsonObject().get("asian");
        JsonElement hispanic = district.getAsJsonObject().get("hisp");
        if(population != null){container.add("population");}
        if(vap != null){container.add("vap");}
        if(cvap != null){container.add("cvap");}
        if(black !=null){container.add("black");}
        if(asian !=null){container.add("asian");}
        if(hispanic !=null){container.add("hispanic");}
        System.out.println(container);
        return container;
    }

    public ArrayList<Double> calculateWeights(String state, int id, JsonObject weight){
        //Job job = jobrepo.findById(id); get its remaining districting therefore remaining disctring
        //must be save in server
        ArrayList<Double> remainingDistricting = new ArrayList<>();
        ArrayList<Double> topTenDistricting = new ArrayList<>();
        //Job job = new Job("MD");
        Job job = new Job("MD");
        //System.out.println(filtered);
        for(int i = 0; i < filtered.size(); i++){
            //DistrictPlan x = filtered.get(i);
            //System.out.println("check correct equality: "+weight.get("populationEquality").getAsDouble());
            //System.out.println("check correct popdiff: "+filtered.get(i).popdiff);
            double userInputPopulationEquality = weight.get("populationEquality").getAsDouble();
            double userInputDeviationEnacted = weight.get("deviationFromEnacted").getAsDouble();
            double userInputWeightCompactness = weight.get("weightCompactness").getAsDouble();
            double populationEqualityScore = userInputPopulationEquality * filtered.get(i).popdiff;
            double deviationFromEnactedScore = userInputDeviationEnacted * filtered.get(i).deviationFromAverageEnacted;
            double compactnessScore = userInputWeightCompactness * filtered.get(i).graphCompactness;
//            System.out.println("   population equality: "+populationEqualityScore);
//            System.out.println("deviation From enacted: "+ deviationFromEnactedScore);
//            System.out.println("     compactness score: "+ compactnessScore);

            //System.out.println(populationEqualityScore + deviationFromEnactedScore + compactnessScore);
            //System.out.println("After implemented: ");
            //System.out.println((populationEqualityScore + deviationFromEnactedScore + compactnessScore)/(userInputPopulationEquality+userInputDeviationEnacted+userInputWeightCompactness));
            //System.out.println("--------------------------");
            double objectiveFunctionScore = (populationEqualityScore + deviationFromEnactedScore + compactnessScore);
            //double objectiveFunctionScore = (populationEqualityScore + deviationFromEnactedScore + compactnessScore)/(userInputPopulationEquality+userInputDeviationEnacted+userInputWeightCompactness);
            remainingDistricting.add(objectiveFunctionScore);
        }
        //now sort it
        ArrayList<Double> unsort = new ArrayList<>(remainingDistricting);
        Collections.sort(remainingDistricting);
        //System.out.println("unsort: "+unsort);
        //System.out.println("after sort: "+remainingDistricting);
        //now take the top 10 highest
        //System.out.println("size: "+remainingDistricting.size());
        //System.out.println("should only be ten: "+topTenDistricting.subList(topTenDistricting.size()-10, topTenDistricting.size()));

        //CAUTION: possible index out of bound
        // what if the remaining is less than 10
        for(int i = 0; i < 10 ; i++){
            //topTenDistricting.add(remainingDistricting.get(remainingDistricting.size()-10+i));
            //in reverse order districting1 - 10
            topTenDistricting.add(remainingDistricting.get(remainingDistricting.size()-1-i));
        }
        //double first = topTenDistricting.get(0);
        ArrayList<Integer> topTenDistrictingIndex = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            topTenDistrictingIndex.add(unsort.indexOf(topTenDistricting.get(i)));
        }
        int top1 = unsort.indexOf(topTenDistricting.get(0));
        int top2 = unsort.indexOf(topTenDistricting.get(1));
        System.out.println("top 1 position: "+top1);
        System.out.println("top 2 position: "+top2);
        System.out.println("All top 10 index: "+topTenDistrictingIndex);
        System.out.println("top 10: "+topTenDistricting);

        //now try to get the actual top 10 districtplan objects and try to retrieves their inside info
//        System.out.println(districtPlans.get("MD").size());//5500
//        System.out.println(districtPlans.get("MD").get(top1));//the actual plans
        //System.out.println(districtPlans.get("MD").get(top1).deviationFromAverageEnacted*0.5);
//        System.out.println(filtered.get(top1).deviationFromAverageEnacted*0.5);
//        System.out.println(filtered.get(top1).graphCompactness*0.5);
//        System.out.println(filtered.get(top1).popdiff*0.5);
        //System.out.println(districtPlans.get("MD").get(top1).districts.get(0).VAP);
        //System.out.println(districtPlans.get("MD").get(top1).districts.get(0).BVAP);
        //System.out.println(districtPlans.get("MD").get(top1));
        for(int i = 0; i < 10;i++){
            topTenDistricting.add(0.0);
        }
        for(int i = 0; i < 10;i++){
            topTenDistricting.add(filtered.get(topTenDistrictingIndex.get(i)).deviationFromAverageEnacted);
        }
        for(int i = 0; i < 10;i++){
            topTenDistricting.add(filtered.get(topTenDistrictingIndex.get(i)).popdiff);
        }
        for(int i = 0; i < 10;i++){
            topTenDistricting.add(filtered.get(topTenDistrictingIndex.get(i)).graphCompactness);
        }
        for(int i = 0; i < 10; i++){
            topTenDistricting.add((topTenDistrictingIndex.get(i)*1.0));
        }
        System.out.println(topTenDistricting);
        System.out.println("final size: "+topTenDistricting.size());
        //System.out.println(topTenDistricting.size());
        //System.out.println(topTenDistricting);
        //System.out.println("----------");
        //job.getBoxAndWhiskerPlot("bvap");
        return topTenDistricting;
    }





    //static methods
//    public static void loadEnactedPlans(String state) {
//        String path = "src/main/resources/plans/" + state + "_enacted.json";
//        ArrayList<District> enacted_districts = new ArrayList<>();
//        try {
//            JsonObject jobj = new Gson().fromJson(new FileReader(path), JsonObject.class);
//            int cur = 0;
//            for(String key: jobj.keySet()) {
//                JsonObject districtObject = jobj.get(key).getAsJsonObject();
//                JsonArray precinctArray = districtObject.getAsJsonArray("precincts");
//                ArrayList<Integer> precincts = new ArrayList<>();
//                if (precinctArray != null) {
//                    for (int i=0;i<precinctArray.size();i++){
//                        precincts.add(precinctArray.get(i).getAsInt());
//                    }
//                }
//                enacted_districts.add(new District(
//                        cur,
//                        districtObject.get("vap").getAsInt(),
//                        districtObject.get("hvap").getAsInt(),
//                        districtObject.get("wvap").getAsInt(),
//                        districtObject.get("bvap").getAsInt(),
//                        districtObject.get("asainvap").getAsInt(),
//                        precincts));
//                cur ++;
//            }
//            System.out.println(enacted_districts.size() + " districts loaded from enacted plan");
//            enacted.put(state, enacted_districts);
//        }
//        catch(Exception e) {
//            System.out.println("Error " + e);
//        }
//    }
    public static void loadPlans(String state) {
        //String path = "src/main/resources/plans/"+ state + "_plans_1.json";
        //String path = "src/main/resources/plans/"+ state + "_510_final.json";
        String path = "src/main/resources/plans/"+state +"_510_final.json";
        ArrayList<DistrictPlan> district_plans = new ArrayList<>();
        try {
            //we convert the path to jsonobject
            JsonObject jobj = new Gson().fromJson(new FileReader(path), JsonObject.class);
            //now we can get jsonobject and their array
            JsonArray arr = jobj.getAsJsonObject().getAsJsonArray("plans");
            for(JsonElement element: arr) {
                //System.out.println("ss");
                DistrictPlan plan = new DistrictPlan(element);
                district_plans.add(plan);
            }
            System.out.println("Found " + district_plans.size() + " plans...");
            districtPlans.put(state, district_plans);
        }
        catch(Exception e) {
            System.out.println("Error " + e);
        }
    }
}
