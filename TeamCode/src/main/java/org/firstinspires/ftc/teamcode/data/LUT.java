package org.firstinspires.ftc.teamcode.data;

public class LUT {
    public double[] ranges = {0.65,0.675,0.7,0.725,0.75,0.775,0.8,0.825,0.85,0.875,0.9,0.925,0.95,0.975,1,1.025,1.05,1.075,1.1,1.125,1.15,1.175,1.2,1.225,1.25,1.275,1.3,1.325,1.35,1.375,1.4,1.425,1.45,1.475,1.5,1.525,1.55,1.575,1.6};
    public double[] pollenLauncher = {2315,2270,2215,2180,2155,2135,2125,2115,2110,2100,2095,2095,2095,2095,2095,2100,2100,2105,2110,2115,2125,2130,2140,2145,2155,2160,2170,2180,2190,2200,2210,2220,2225,2235,2245,2255,2270,2280,2290};

    public double pollenValue(double distance){
        return pollenLauncher[indexOfRange(distance)];
    }
    public int indexOfRange(double distance){
        return (int) (40*distance - 26);
    }
}
