package com.national.national.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.national.national.handler.JobHandler;
import com.national.national.model.District;
import com.national.national.model.DistrictPlan;
import com.national.national.model.Job;
import com.national.national.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import static com.national.national.model.Job.filtered;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping(path="api/")
public class JobController {
    @Autowired
    private JobRepository jobrepo;

    @Autowired
    private JobHandler jobHandler;
    private final Gson gson = new Gson();
//    public static HashMap<String, ArrayList<DistrictPlan>> districtPlans = new HashMap<>();//job 1
//    public static HashMap<String, ArrayList<District>> enacted = new HashMap<>();//job 1


    @GetMapping(path="job/{id}")
    public ResponseEntity getJob(@PathVariable int id) {
        Job job = jobrepo.findById(id).get();
        System.out.println("Find job " + gson.toJson(job, Job.class));
        return new ResponseEntity(gson.toJson(job, Job.class), HttpStatus.OK);
    }

    @GetMapping(path="{state}/job")
    public int getStateJobs(@PathVariable String state){
        return jobHandler.getStateJobs(state);
        //return new ResponseEntity(gson.toJson(job,Job.class))
    }

    //do we need this?
    @PostMapping(path="job")
    public ResponseEntity createJob(@RequestBody String jobJson) throws IOException {
        System.out.println("Receive job " + jobJson);
        Job job = gson.fromJson(jobJson, Job.class);
        jobrepo.save(job);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    //more implementation
    @PostMapping(path="job/{id}/filter")
    public int filter(@PathVariable Long id, @RequestBody String constraints) {
        System.out.println("Receive job " + constraints);
        JsonObject cons = gson.fromJson(constraints, JsonObject.class);
        Job job = new Job("MD");
        int remainingDistricts = job.filtering(cons);
        job = gson.fromJson(constraints,Job.class);
        job.setRemainingDistricting(remainingDistricts);
        jobrepo.save(job);
        return remainingDistricts;
        //return new ResponseEntity(remainingDistricts,HttpStatus.OK);
    }

    @GetMapping(path="job/{state}/{id}/constraints")
    public ArrayList<String> getPossibleConstraints(@PathVariable String state, @PathVariable int id) throws FileNotFoundException {
        //String json = jobHandler.getPossibleConstraints(state,id);
        if(state.equals("Maryland")){
            state = "MD";
        }else if(state.equals("Virginia")){
            state = "VA";
        }
        System.out.println("get possible constraints: "+state+" "+id);
        return jobHandler.getPossibleConstraints(state,id);
    }

    @PostMapping(path="job/{state}/{id}/weights")
    public ArrayList<Double> calculateWeights(@PathVariable String state, @PathVariable int id,@RequestBody String weights){
        //ArrayList<Integer> topTenDistrictings = new ArrayList<>();
        //Job job = jobrepo.findById(id).get();
        JsonObject weight = gson.fromJson(weights, JsonObject.class);
        // System.out.println("Recevied weights: "+weight);
        //System.out.println("cal weights"+state+" "+id);
        return jobHandler.calculateWeights(state,id,weight);
        //return topTenDistrictings;
    }

    @GetMapping(path="job/{districtings}/plot")
    public ArrayList<Double> getBoxPlot(@PathVariable int districtings){
        System.out.println("retrieving correct districting indx: "+districtings);
        Job x = new Job("MD");
        return x.getBoxAndWhiskerPlot(districtings);
    }



//    @PostMapping(path="job/{id}/filter")
//    public ResponseEntity filter(@PathVariable Long id, @RequestBody String constraints) {
//        //Job job = gson.fromJson(constraints,Job.class);
//        //jobrepo.save(job);
//        System.out.println("Received Constraints: "+constraints);
//        //Job job = jobrepo.findById(id).get();
//        //System.out.println("find job for filter: "+job);
//        //JsonObject cons = gson.toJson(job,JsonObject.class);
//        JsonObject cons = gson.fromJson(constraints, JsonObject.class);
//        //job.filtered((cons));
//        //int dev = cons.get("dev").getAsInt();
//        //String MM = "BVAP";
//        //System.out.println(job);
//        //int MM_limit = 3;
////        Job job = jobrepo.findById(id).get();
//        Job job = new Job("MD");
//        int remainingDistricts = job.filtered(cons);
//        //4084
//        //job = gson.fromJson(constraints,Job.class);
//        //System.out.println(job.setRemainingDistricting(remainingDistricts));
//        //job.setRemainingDistricting(remainingDistricts);
//        //jobrepo.save(job);
//        System.out.println(remainingDistricts);
//        return new ResponseEntity(HttpStatus.OK);
//    }
//

}
