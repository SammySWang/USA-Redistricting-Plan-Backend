package com.national.national.model;

import com.google.gson.JsonObject;
import com.national.national.handler.JobHandler;
import com.national.national.repository.JobRepository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import static com.national.national.model.DistrictPlan.mm;

@Entity
public class Job {

    @Id
    //@generateValue(strategy=Generationtype.Auto)
    private long id;
    //@Column(name="state")
    private String state;
    private String populationType;
    private double populationDifference;
    private String compactnessType;
    private double compactness;
    private String majorityMinorityType;
    private int majorityMinorityDistricts;
    private double majorityMinorityPercentage;
    private int remainingDistricting;

    //private List<String> incumbents = new List<>();
    // private String districtPlanPath;

    @Transient
    private final ArrayList<District> enacted;

    @Transient
    public static ArrayList<DistrictPlan> filtered = new ArrayList<>();

    @Transient
    private final ArrayList<DistrictPlan> districtPlans;

    public Job(){
       this("MD");
    }

    public Job(String state) {
        this.state = state;
        this.districtPlans = JobHandler.districtPlans.get(state);
        this.enacted = JobHandler.enacted.get(state);
        //this.filtered = new ArrayList<>();
        //this.filtered = this.filtered;
        //this.calDev();
    }

    public String getMajorityMinorityType() {
        return majorityMinorityType;
    }

    public double getPopulationEquality() {
        return populationDifference;
    }

    public void setRemainingDistricting(int remainingDistricting) {
        this.remainingDistricting = remainingDistricting;
    }

    public int getRemainingDistricting() {
        return remainingDistricting;
    }

    public ArrayList<DistrictPlan> getFiltered() {
        return filtered;
    }

    public int filtering(JsonObject cons) {
        //this.filtered = new ArrayList<>();
        //System.out.println("Im inside filtered");
        //System.out.println(cons);

        //*****this will clear remaining districting to 0 when selecting different states
        this.filtered.clear();
        //*****consider better implementation

        for (DistrictPlan plan : this.districtPlans) {
            // Majority minority type to be implement
            // compactness to be implement
            // incumbents to be implement
            if(plan.popdiff < cons.get("populationDifference").getAsDouble()/100) {
                //plan.popdiff
                this.filtered.add(plan);
            }
        }
        //System.out.println(filtered.get(0));
        return this.filtered.size();
    }

}
