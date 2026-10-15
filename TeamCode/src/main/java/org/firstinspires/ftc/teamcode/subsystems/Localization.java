package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLFieldMap;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class Localization {


    private Limelight3A limelight;
    private Pose3D botpose;

    LLFieldMap fieldMap = new LLFieldMap();

    AprilTagProcessor aprilTagProcessor;

    // 2. Pass it to the VisionPortal along with your webcam
    // VisionPortal visionPortal;

    Telemetry telemetry;

    public Localization(Robot robot){
        telemetry = robot.telemetry;
        limelight = robot.hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    public void updateLimelightPosition() {
        LLResult result = limelight.getLatestResult();
        if (result != null) {
            if (result.isValid()) {
                botpose = result.getBotpose();
            }
        }

    }

    public Pose2D getLimelightPosition() {
        Position position = botpose.getPosition();
        Pose2D pose2D;
        //TODO: vv FIX THIS I DONT THINK THAT IT IS CORRECT vv
        pose2D = new Pose2D(DistanceUnit.INCH, position.x, position.y, AngleUnit.DEGREES, botpose.getOrientation().getYaw());
        return pose2D;
    }

    private Pose3D tagPoseForTesting = new Pose3D(new Position(),new YawPitchRollAngles(AngleUnit.RADIANS,0,0,0,0));
    public Pose3D tagPoseTest(){
        LLResult result = limelight.getLatestResult();
        if (!result.isValid()){
            return tagPoseForTesting;
        }
        for (LLResultTypes.FiducialResult tag : result.getFiducialResults()){
            tagPoseForTesting = tag.getTargetPoseRobotSpace();
            telemetry.addData("new tag: ", tag.getFiducialId());
            telemetry.addData("robo: ", tag.getRobotPoseFieldSpace());
        }
        return tagPoseForTesting;
    }

    public boolean seesTag(boolean onRed){
        LLResult result = limelight.getLatestResult();
        if (!result.isValid()){
            return false;
        }

        List<LLResultTypes.FiducialResult> tags = result.getFiducialResults();
//        int[] redTags = {30,31,32,33,34,35,36,37};
//        int[] blueTags = {38,39,40,41,42,43,44,45};
        boolean onSide = false;

        for (LLResultTypes.FiducialResult tag : tags){
            if (tag.getFiducialId() < 38 == onRed) { //if both are true youre good and if both are false than youre also good (XNOR)
                onSide = true;
                break;
            }
        }

        return onSide;
    }
}

