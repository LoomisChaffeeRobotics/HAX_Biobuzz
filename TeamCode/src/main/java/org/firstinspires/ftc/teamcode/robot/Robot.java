package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.LocalizationFuser;
import org.firstinspires.ftc.teamcode.Shooter;

public class Robot {
    public final HardwareMap hardwareMap;
    public final Telemetry telemetry;
    public final LocalizationFuser localization;
    Shooter shooterPollen;

    public Robot(OpMode opMode){
        this.hardwareMap = opMode.hardwareMap;
        this.telemetry = opMode.telemetry;
        this.localization = new LocalizationFuser(hardwareMap, telemetry);
        this.shooterPollen = new Shooter();
    }

}
