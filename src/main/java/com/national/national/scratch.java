package com.national.national;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.national.national.model.Job;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class scratch {

    public static void xx(){
        String BLACK = "asianvap";
        int remainingDistricting = 0;
        try {
            JsonObject jobj = new Gson().fromJson(new FileReader("src/main/resources/redistricting_results/MA_5000_2.json"), JsonObject.class);
            //System.out.println("this is from resources: "+jobj);
            JsonArray arr = jobj.getAsJsonArray("plans");
            for(JsonElement districtsArrayjson: arr){
                //System.out.println("this is districts array from json: "+districtsArrayjson);
                JsonArray districtsArray = districtsArrayjson.getAsJsonObject().getAsJsonArray("districts");
                for(JsonElement districts: districtsArray){
                    //System.out.println("this is districts" + districts);
                    //JsonObject districtsObject = districts.getAsJsonObject();
//                    if(districts.getAsJsonObject().get(BLACK))
                    int race = districts.getAsJsonObject().get(BLACK).getAsInt();
                    //System.out.println("this is user chosen: "+ race);
                    if(race > 1){
                        remainingDistricting+=1;
                    }
                }
            }
        }catch(Exception e){
            System.out.println("Error occur in getJob: "+e);
        }

        System.out.println("total remainingDistricting is: "+remainingDistricting);
    }

    public static String getPossibleConstraints(String state, int id) throws FileNotFoundException {

        ArrayList<String> container = new ArrayList();
        String path = "src/main/resources/plans/" + state + "/" + state + "_plans_"+id+".json";
        JsonObject checkForConstraints = new Gson().fromJson(new FileReader(path), JsonObject.class);
        JsonElement planArray = checkForConstraints.getAsJsonObject().getAsJsonArray("plans").get(0);
        //{"districts":[{"districtNumber":1,"population":726108,"white":605023,"bl

        JsonArray ss = planArray.getAsJsonObject().getAsJsonArray("districts");
        //[{"districtNumber":1,"population":726108,"white":605023,"

        JsonElement yy = ss.get(0);
        //{"districtNumber":1,"population":726108,"white":605023,

        JsonElement population  = yy.getAsJsonObject().get("population");
        JsonElement vap = yy.getAsJsonObject().get("vap");
        JsonElement cvap = yy.getAsJsonObject().get("cvap");
        JsonElement black = yy.getAsJsonObject().get("black");
        JsonElement asian = yy.getAsJsonObject().get("asian");
        JsonElement hispanic = yy.getAsJsonObject().get("hisp");
        if(population != null){container.add("population");}
        if(vap != null){container.add("vap");}
        if(cvap != null){container.add("cvap");}
        if(black !=null){container.add("black");}
        if(asian !=null){container.add("asian");}
        if(hispanic !=null){container.add("hispanic");}
        System.out.println(container);

        return "a";
    }

    public static void main(String[] args) throws FileNotFoundException {

        //getPossibleConstraints("MD",1);
        ArrayList<Double> xx = new ArrayList<>();
        xx.add(2.1);
        xx.add(2.2);
        xx.add(2.3);
        System.out.println(xx.indexOf(2.3));
    }
}

//    @GetMapping(path="/job/{id}")
//    public ResponseEntity getJob(@PathVariable Long id) {
//        Job job = jobrepo.findById(id).get();
//
//        String populationMMtype = "ASIANVAP";
//        System.out.println(job.getMajorityMinorityType());
//
//        System.out.println("current populationMMtype: "+populationMMtype);
//        //System.out.println("original: "+job);
//        //System.out.println("current minorityg: "+job.getMinorityG());
//        System.out.println("Find job " + gson.toJson(job, Job.class));
//
//        //parsing json
//        int remainingDistricting = 0;
//        try {
//            JsonObject jobj = new Gson().fromJson(new FileReader("src/main/resources/redistricting_results/GA_plans_1.json"), JsonObject.class);
//            //System.out.println("this is from resources: "+jobj);
//            JsonArray arr = jobj.getAsJsonArray("plans");
//            for(JsonElement districtsArrayjson: arr){
//                //System.out.println("this is districts array from json: "+districtsArrayjson);
//                JsonArray districtsArray = districtsArrayjson.getAsJsonObject().getAsJsonArray("districts");
//                for(JsonElement districts: districtsArray){
//                    //System.out.println("this is districts" + districts);
//                    //JsonObject districtsObject = districts.getAsJsonObject();
////                    if(districts.getAsJsonObject().get(BLACK))
//                    int race = districts.getAsJsonObject().get(populationMMtype).getAsInt();
//
//                    if(race > 10000){
//                        //you should save those jobs that are filtered
//                        remainingDistricting+=1;
//                    }
//                }
//            }
//        }catch(Exception e){
//            System.out.println("Error occur in getJob: "+e);
//        }
//        job.setRemainingDistricting(remainingDistricting/8);
//        System.out.println("filtering done, remaining districting: "+remainingDistricting);
//        return new ResponseEntity(gson.toJson(job, Job.class), HttpStatus.OK);
//    }
