package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class HingeValueFinder extends OpMode {
    Servo hinge;
    double position = .5;
    @Override
    public void init() {
        hinge = hardwareMap.get(Servo.class, "hinge");
    }

    @Override
    public void loop() {
        position += (gamepad1.aWasPressed()? 0.05: 0) + (gamepad1.yWasPressed()? -00.05: 0) + (gamepad1.dpadUpWasPressed()? 0.01:0)+(gamepad1.dpadDownWasPressed()? -0.01:0);
        hinge.setPosition(position);
        telemetry.addData("hing position", position);
    }
}
