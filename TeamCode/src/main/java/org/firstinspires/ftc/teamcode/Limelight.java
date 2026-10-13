package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
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
    Telemetry telemetry;
    HardwareMap hardwareMap;

    AprilTagProcessor aprilTagProcessor;

    // 2. Pass it to the VisionPortal along with your webcam
    VisionPortal visionPortal;

    public Limelight(HardwareMap hardwareMap, Telemetry telemetry){
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }

    public void init(int limelightPipeline, String limelightName, String webcamName) { // Set telemetryFrequency to 11ms for most things
        limelight = hardwareMap.get(Limelight3A.class, limelightName);
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start(); // This tells Limelight to start looking!
        limelight.pipelineSwitch(limelightPipeline);
        limelight.start();
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagOutline(true)      // Highlights the tag on the driver station screen
                .setDrawTagID(true)           // Displays the ID number on the screen
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES) // Sets your preferred units
                .build();
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, webcamName))
                .addProcessor(aprilTagProcessor) // Plugs the processor into the stream
                .build();
    }

    public void updateLimelightPosition() {
        LLResult result = limelight.getLatestResult();
        if (result != null) {
            if (result.isValid()) {
                botpose = result.getBotpose();
                telemetry.addData("tx", result.getTx());
                telemetry.addData("ty", result.getTy());
                telemetry.addData("Botpose", botpose.toString(), "\n");
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

    @SuppressLint("DefaultLocale")
    // Pray to god this works
    public double absValAprilTagRoll(int targetID) {
        List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                if (detection.id == targetID) {
                    telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                    telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                    telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (RAD)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                    return detection.ftcPose.roll;
                }
            }
        }

        return Double.MAX_VALUE; // Something is wrong
    }
}
