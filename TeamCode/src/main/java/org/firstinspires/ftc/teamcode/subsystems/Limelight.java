package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.Robot;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Limelight {
    private Limelight3A limelight;

    public LLResult result;
    private Telemetry telemetry;

    public enum TagSet {RED_FRONT, RED_BACK, BLUE_FRONT, BLUE_BACK, NO_TAGS};


    public Limelight (Robot robot){
        limelight = robot.hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
        telemetry = robot.telemetry;
    }

    public void update(){
        result = limelight.getLatestResult();
    }
    public boolean seesTag(boolean onRed){
        LLResult result = limelight.getLatestResult();
        if (!result.isValid()){
            return false;
        }

        List<LLResultTypes.FiducialResult> tags = result.getFiducialResults();
//        int[] redTags = {30,31,32,33,  34,35,36,37};
//        int[] blueTags = {38,39,40,41,  42,43,44,45};
        boolean onSide = false;

        for (LLResultTypes.FiducialResult tag : tags){
            if (tag.getFiducialId() < 38 == onRed) { //if both are true youre good and if both are false than youre also good (XNOR)
                onSide = true;
                break;
            }
        }

        return onSide;
    }
    public List<Integer> tagsSeen(boolean onRed){
        LLResult result = limelight.getLatestResult();
        if (!result.isValid()){
            return new ArrayList<>();
        }

        List<LLResultTypes.FiducialResult> tags = result.getFiducialResults();
        List<Integer> seen = new ArrayList<>();
        for (LLResultTypes.FiducialResult tag : tags){
            seen.add(tag.getFiducialId());
        }
        return seen;
    }

    public double turnError(TagSet tagSet){
        int[] tags = getTags(tagSet);
        double[][] tagPositions = pixelList(tags);
        List<Integer> activeTags = tagsActive(tagPositions);
        double[] center = center(tagPositions,activeTags);
        return center[0];
    }
    public double turnErrorWithTelemetry(TagSet tagSet){
        int[] tags = getTags(tagSet);
        telemetry.addData("tags Seen: ", tags);
        double[][] tagPositions = pixelList(tags);
        telemetry.addData("tag Positions: ", tagPositions);
        List<Integer> activeTags = tagsActive(tagPositions);
        telemetry.addData("active tags: ", activeTags);
        double[] center = center(tagPositions,activeTags);
        telemetry.addData("center: ", center);
        return center[0];
    }

    private List<Integer> tagsActive(double[][] tagPositions){
        List<Integer> active = new ArrayList<>();
        for(int i = 0; i < tagPositions.length; i++){
            if (tagPositions[i][2] != 0.0){
                active.add(i);
            }
        }
        return active;
    }

    private double[] center (double[][] tagPositions, List<Integer> active){

        double[] average;
        double[] predicted2Displacement;
        if (active == List.of(0,1,2,3)){
            double averageX = (tagPositions[0][0]+tagPositions[1][0]+tagPositions[2][0]+tagPositions[3][0])/4;
            double averageY = (tagPositions[0][1]+tagPositions[1][1]+tagPositions[2][1]+tagPositions[3][1])/4;
            double averageD = (tagPositions[0][2]+tagPositions[1][2]+tagPositions[2][2]+tagPositions[3][2])/4;
            average = new double[]{averageX, averageY, averageD};
            predicted2Displacement = new double[]{tagPositions[2][0] - averageX, tagPositions[2][1] - averageY};}
        else if(active == List.of(0,1,2)){
            double averageX = (tagPositions[1][0]+tagPositions[2][0])/2;
            double averageY = (tagPositions[1][1]+tagPositions[2][1])/2;
            double averageD = (tagPositions[1][2]+tagPositions[2][2])/2;
            average = new double[]{averageX, averageY, averageD};
            predicted2Displacement = new double[]{tagPositions[2][0] - averageX, tagPositions[2][1] - averageY};
        }
        else if(active == List.of(1,2,3)){
            double averageX = (tagPositions[1][0]+tagPositions[2][0])/2;
            double averageY = (tagPositions[1][1]+tagPositions[2][1])/2;
            double averageD = (tagPositions[1][2]+tagPositions[2][2])/2;
            average = new double[]{averageX, averageY, averageD};
            predicted2Displacement = new double[]{tagPositions[2][0] - averageX, tagPositions[2][1] - averageY};
        }
        else if(active == List.of(1,2)){
            double averageX = (tagPositions[1][0]+tagPositions[2][0])/2;
            double averageY = (tagPositions[1][1]+tagPositions[2][1])/2;
            double averageD = (tagPositions[1][2]+tagPositions[2][2])/2;
            average = new double[]{averageX, averageY, averageD};
            predicted2Displacement = new double[]{tagPositions[2][0] - averageX, tagPositions[2][1] - averageY};
        }
        else if(active == List.of(0,2)){
            double averageX = (3*tagPositions[0][0]+6.5*tagPositions[2][0])/9.5;
            double averageY = (3*tagPositions[0][1]+6.5*tagPositions[2][1])/9.5;
            double averageD = (3*tagPositions[0][2]+6.5*tagPositions[2][2])/9.5;
            average = new double[]{averageX, averageY, averageD};
            predicted2Displacement = new double[]{tagPositions[2][0] - averageX, tagPositions[2][1] - averageY};
        }
        else if(active == List.of(1,3)){
            double averageX = (6.5*tagPositions[1][0]+3*tagPositions[3][0])/9.5;
            double averageY = (6.5*tagPositions[1][1]+3*tagPositions[3][1])/9.5;
            double averageD = (6.5*tagPositions[1][2]+3*tagPositions[3][2])/9.5;
            average = new double[]{averageX, averageY, averageD};
            double p2dx = (3 * tagPositions[3][0] + 3.5 * averageX)/6.5;
            double p2dy = (3 * tagPositions[3][1] + 3.5 * averageY)/6.5;
            predicted2Displacement = new double[]{p2dx,p2dy};
        }
        else if(active == List.of(0,1)){
            double averageX = (-3*tagPositions[0][0]+6.5*tagPositions[1][0])/3.5;
            double averageY = (-3*tagPositions[0][1]+6.5*tagPositions[1][1])/3.5;
            double averageD = (-3*tagPositions[0][2]+6.5*tagPositions[1][2])/3.5;
            average = new double[]{averageX, averageY, averageD};
            double p2dx = (-3 * tagPositions[0][0] + 9.5 * averageX)/6.5;
            double p2dy = (-3 * tagPositions[0][1] + 9.5 * averageY)/6.5;
            predicted2Displacement = new double[]{p2dx,p2dy};
        }
        else if(active == List.of(2,3)){
            double averageX = (6.5*tagPositions[2][0]-3*tagPositions[3][0])/3.5;
            double averageY = (6.5*tagPositions[2][1]-3*tagPositions[3][1])/3.5;
            double averageD = (6.5*tagPositions[2][2]-3*tagPositions[3][2])/3.5;
            average = new double[]{averageX, averageY, averageD};
            predicted2Displacement = new double[]{tagPositions[2][0] - averageX, tagPositions[2][1] - averageY};
        }
        else if(active == List.of(0,2,3)){
            double averageX = (tagPositions[0][0]+tagPositions[3][0])/2;
            double averageY = (tagPositions[0][1]+tagPositions[3][1])/2;
            double averageD = (tagPositions[0][2]+tagPositions[3][2])/2;
            average = new double[]{averageX, averageY, averageD};
            predicted2Displacement = new double[]{tagPositions[2][0] - averageX, tagPositions[2][1] - averageY};
        }
        else if(active.contains(0)&& active.contains(3)){ // both 0,3 and 0,1,3 are included
            double averageX = (tagPositions[0][0]+tagPositions[3][0])/2;
            double averageY = (tagPositions[0][1]+tagPositions[3][1])/2;
            double averageD = (tagPositions[0][2]+tagPositions[3][2])/2;
            average = new double[]{averageX, averageY, averageD};
            double p2dx = (3 * tagPositions[3][0] + 3.5 * averageX)/6.5;
            double p2dy = (3 * tagPositions[3][1] + 3.5 * averageY)/6.5;
            predicted2Displacement = new double[]{p2dx,p2dy};
        }
        else{ //only 1 or 0 tags seen
            return new double[]{0.0,0.0,0.0};
        }

        //rotation and scaling of p2 around average
        return new double[]{
                -2*predicted2Displacement[1]+average[0]+average[1],
                2*predicted2Displacement[0]-average[0]+average[1],
                average[2]
        };
    }
    private double[][] pixelList(int[] tags){

        double[][] tagPositions = new double[][]{{0.0,0.0,0.0},{0.0,0.0,0.0},{0.0,0.0,0.0},{0.0,0.0,0.0}};

        for(LLResultTypes.FiducialResult tag : result.getFiducialResults()){
            int index = includes(tags,tag.getFiducialId());
            if (index == -1){
                continue;
            }
            tagPositions[index] = new double[]{
                    tag.getTargetXPixels(),
                    tag.getTargetYPixels(),
                    tag.getTargetPoseRobotSpace().getPosition().x
            };
        }

        return tagPositions;
    }


    private int[] getTags(TagSet tagSet){
        switch (tagSet) {
            case RED_FRONT:
                return new int[]{34, 35, 36, 37};
            case RED_BACK:
                return new int[]{30, 31, 32, 33};
            case BLUE_FRONT:
                return new int[]{42, 43, 44, 45};
            case BLUE_BACK:
                return new int[]{38, 39, 40, 41};
        }
        return new int[]{};

    }

    private int includes(int[] array, int thing){
        for (int i = 0; i < array.length; i++){
            if(array[i] == thing) {
                return i;
            }
        }
        return -1;
    }

}
