package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class Limelight{
    private Limelight3A limelight;
    private Pose3D botpose;

    AprilTagProcessor aprilTagProcessor;

    // 2. Pass it to the VisionPortal along with your webcam
    VisionPortal visionPortal;

    OpMode opMode;

    public Limelight(OpMode opMode){
        this.opMode = opMode;
    }

    public void init() { // Set telemetryFrequency to 11ms for most things
        limelight = opMode.hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start(); // This tells Limelight to start looking!
//        limelight.pipelineSwitch(limelightPipeline);
        limelight.start();
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagOutline(true)      // Highlights the tag on the driver station screen
                .setDrawTagID(true)           // Displays the ID number on the screen
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES) // Sets your preferred units
                .build();
    }

    public void updateLimelightPosition() {
        LLResult result = limelight.getLatestResult();
        if (result != null) {
            if (result.isValid()) {
                botpose = result.getBotpose();
            }
        }

    }

    public boolean limelightVisible() {
        LLResult result = limelight.getLatestResult();
        if (result != null) {
            return true;
        } else {
            return false;
        }
    }

    public Pose2D getLimelightPosition() {
        Position position = botpose.getPosition();
        Pose2D pose2D;
        //TODO: vv FIX THIS I DONT THINK THAT IT IS CORRECT vv
        pose2D = new Pose2D(DistanceUnit.INCH, position.x, position.y, AngleUnit.DEGREES, botpose.getOrientation().getYaw());
        return pose2D;
    }

    public boolean seesTag(){
        List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();
        return(!currentDetections.isEmpty());

    }
    public double absValAprilTagRoll(int targetID) {
        List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                if (detection.id == targetID) {
                   return detection.ftcPose.roll;
                }
            }
        }

        return Double.MAX_VALUE; // Something is wrong
    }
}
