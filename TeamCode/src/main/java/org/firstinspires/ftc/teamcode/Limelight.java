package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

public class Limelight{
    private Limelight3A limelight;
    private Pose3D botpose;

    Telemetry telemetry;
    HardwareMap hardwareMap;

    public Limelight(HardwareMap hardwareMap, Telemetry telemetry){
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }

    public void init(int limelightPipeline) { // Set telemetryFrequency to 11ms for most things
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start(); // This tells Limelight to start looking!
        limelight.pipelineSwitch(limelightPipeline);
        limelight.start();
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

    public boolean isReady(){
        return false; //TODO: fill this w/ 'is april tag currently being seen'
    }

    public Pose2D getLimelightPosition() {
        Position position = botpose.getPosition();
        Pose2D pose2D;
        //TODO: vv FIX THIS I DONT THINK THAT IT IS CORRECT vv
        pose2D = new Pose2D(DistanceUnit.INCH, position.x, position.y, AngleUnit.RADIANS, botpose.getOrientation().getYaw());
        return pose2D;
    }
}
