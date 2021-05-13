package com.national.national.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class BoxPlotData {


//      0:[61589, 61589, 86245, 158189, 319246],
//      1:[163778, 163778, 163778, 163778, 212010],
//      2:[55561, 105977, 105977, 105977, 190374],
//      3:[207316, 291713, 291713, 291713, 293381],
//      4:[151884, 192563, 192563, 192563, 192563],
//      5:[64860, 64860, 64860, 64860, 64860],
//      6:[38526, 197514, 257862, 296183, 296183],
//      7:[38477, 63133, 63133, 63133, 275980]
//this is plot data: {0=[51077, 61589, 61589, 61589, 176887], 1=[73862, 163778, 163778, 163778, 316478], 2=[55561, 105977, 105977, 105977, 192237], 3=[60462, 291713, 291713, 291713, 311105], 4=[151884, 192563, 192563, 192563, 293517], 5=[64860, 64860, 64860, 64860, 64860], 6=[66704, 296183, 296183, 296183, 333903], 7=[17920, 63133, 63133, 63133, 294384]}

    private HashMap<Integer, ArrayList<Integer>> s;
}
