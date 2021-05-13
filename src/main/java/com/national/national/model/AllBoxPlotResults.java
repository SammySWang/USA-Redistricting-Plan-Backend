package com.national.national.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class AllBoxPlotResults {

    private List<DistrictParent> districtParentList;
    private List<MinorityData> minorityDataList;
    private HashMap<Integer, ArrayList<Integer>> box;
}
