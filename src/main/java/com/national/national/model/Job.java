package com.national.national.model;

import com.google.common.math.Quantiles;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.national.national.handler.JobHandler;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.*;

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
        System.out.println("Start filtering...");
        //can i say job.districtPlans? if I moved this function to jobHandler
        for (DistrictPlan plan : this.districtPlans) {
            // Majority minority type to be implement
            // compactness to be implement
            // incumbents to be implement
            //System.out.println("plan graph:"+plan.graphCompactness);

            if(plan.graphCompactness > cons.get("compactness").getAsDouble() && plan.popdiff < cons.get("populationDifference").getAsDouble()/100) {

                this.filtered.add(plan);
            }
        }
        System.out.println("Remaining Districtings: "+this.filtered.size());
        //System.out.println(filtered.get(0));
        return this.filtered.size();
    }

    ////////////////////////////5/10 changes

    public ArrayList<Double> getBoxAndWhiskerPlot(int districting) {
        //this should be districting use selects, top 10 districting 1
        //i should save top 10 to the database too
        //this will makes get by state and id easier.
        int districtNum = this.filtered.get(districting).districts.size();
        String x = "";
        for(int i =0 ;i < districtNum; i++){
            x+=this.filtered.get(districting).districts.get(i).precincts;
        }
        System.out.println(x);
        System.out.println("Getting Box plot...");
        ArrayList<ArrayList<Integer>> plotData = new ArrayList<>();
        HashMap<Integer, ArrayList<Integer>> plot = new HashMap<>();
        for(int i = 0; i < districtNum; i++) {
            plotData.add(new ArrayList<>());
        }
        int cur;//what is this
        for(DistrictPlan plan: this.filtered) {
            cur = 0;
            for(District district: plan.districts) {
                int pop = 0;
                //int total = 0;
                switch ("bvap") {
                    case "bvap":
                        pop = district.BVAP;
                        //total += district.VAP;
                        break;
                    case "wvap":
                        pop = district.WVAP;
                        break;
                    case "hvap":
                        pop = district.HVAP;
                        break;
                    default:
                        break;
                }
                plotData.get(cur).add(pop);

//                plotData.get(cur).add(pop/total);
//                System.out.println("pop: "+pop);
//                System.out.println("total: "+total);
//                System.out.println("pop/total: "+Double.valueOf(pop)/Double.valueOf(total));
//                System.out.println("--------------------------");
                cur ++;
            }
        }
        System.out.println("Finish adding pop data. Check " + plotData.get(0).size());
        cur = 0;

        //System.out.println("total");
        for(ArrayList<Integer> id: plotData) {
            int min = Collections.min(id);
            int max = Collections.max(id);
            int q1 = this.filtered.size() / 4;
            //int q1 = id.size() /4;
            int q2 = q1 * 2;
            int q3 = q1 * 3;
            Collections.sort(id);//why sort?
            plot.put(cur, new ArrayList<>(Arrays.asList(min, id.get(q1), id.get(q2), id.get(q3), max)));
            cur ++;
        }
        System.out.println("Plot is ready, check " + plot.size());
        System.out.println("this is plot data: "+plot);
        System.out.println("Now checking districts info...");
        //return new Gson().toJson(plot);
        //ArrayList<> districtData = new ArrayList();
        //HashMap<String,String> ww = new HashMap();

        List<List> listOfMixedTypes = new ArrayList<List>();
        for(int i = 0; i < districtNum;i++){
            //ww.put("title","district"+(i+1));
            System.out.println("district :"+(i+1));
            System.out.println("voting population: "+this.filtered.get(districting).districts.get(i).VAP);
            System.out.println("BVAP: "+this.filtered.get(districting).districts.get(i).BVAP);
            System.out.println("HVAP: "+this.filtered.get(districting).districts.get(i).HVAP);
            System.out.println();
            //listOfMixedTypes.add()

        }
        //System.out.println(ww);
        System.out.println("-------------------------------");

        return new ArrayList<Double>();
    }

}
