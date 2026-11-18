package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.Robot;

public class Indicator {
    Servo indicator;
    Telemetry telemetry;

    public Indicator(Robot robot){
        indicator = robot.hardwareMap.get(Servo.class, "indicator");
        telemetry = robot.telemetry;
    }

    public void update(boolean launchersSpunUp){
        if (launchersSpunUp){
            indicator.setPosition(0.45); //green
        } else {
            indicator.setPosition(0.3); //red
        }
        // i got colors from https://www.gobilda.com/rgb-indicator-light-pwm-controlled/?srsltid=AYCX9vJTpuOqimrSJ6L6K3P8KiKrlCjqk-seiX4eH2CaAKcxnE4sWzDT
    }
}
