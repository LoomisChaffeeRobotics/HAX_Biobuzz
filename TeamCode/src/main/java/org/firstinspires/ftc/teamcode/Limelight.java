package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

public class Limelight {
    private Limelight3A limelight;
    private Pose3D botpose;

    Telemetry telemetry;

    public void init(int limelightPipeline, int telemetryFrequency, Telemetry telemetryInput) { // Set telemetryFrequency to 11ms for most things
        telemetry = telemetryInput;
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start(); // This tells Limelight to start looking!
        limelight.pipelineSwitch(limelightPipeline);
        telemetry.setMsTransmissionInterval(telemetryFrequency);
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

    public Pose3D getLimelightPosition() {
        return botpose;
    }
}
