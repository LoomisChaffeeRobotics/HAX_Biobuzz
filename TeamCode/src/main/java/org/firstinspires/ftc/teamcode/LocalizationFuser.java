package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.ftccommon.internal.manualcontrol.parameters.ImuParameters;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LocalizationFuser {

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
    }

    void init(HardwareMap hardwareMap){
        elapsedTime.reset();
        deadwheels.init()
    }

    void update(){
        if (elapsedTime.seconds() > 3){

        }
    }
}
