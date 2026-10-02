package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.ftccommon.internal.manualcontrol.parameters.ImuParameters;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

public class LocalizationFuser {

    Pose2D roboPose;
    HardwareMap hardwareMap;
    Telemetry telemetry;
    ElapsedTime elapsedTime;
    Deadwheels deadwheels;
    Limelight limelight;

    public LocalizationFuser(HardwareMap hardwareMap, Telemetry telemetry){
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
        this.deadwheels = new Deadwheels(hardwareMap, telemetry);
        this.limelight = new Limelight(hardwareMap, telemetry);
        this.roboPose = new Pose2D(DistanceUnit.INCH, 0,0, AngleUnit.RADIANS, 0);
    }

    void init(HardwareMap hardwareMap){
        elapsedTime.reset();
        deadwheels.init(0d,0d);
        limelight.init(0); // MAKE SURE THIS LIMELIGHT PIPELINE IS CORRECT WHEN WE GET THE FIELDMAP!!!!!!
    }

    void update(){
        limelight.updateLimelightPosition();
        deadwheels.updateDeadwheelPosition();
        if (elapsedTime.seconds() > 3 && limelight.limelightVisible()) {
            roboPose = limelight.getLimelightPosition();
            deadwheels.resetPosition(roboPose);
            elapsedTime.reset();
            return;
        }
        roboPose = deadwheels.getDeadwheelPosition();
    }

    Pose2D getPosition(){
        return roboPose;
    }
}

