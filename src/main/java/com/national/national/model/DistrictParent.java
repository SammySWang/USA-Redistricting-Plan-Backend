package com.national.national.model;

import lombok.Data;

import java.util.*;
@Data
public class DistrictParent {

    private String title;
    private String key;
    private List<DistrictChild> dataSource;

}
