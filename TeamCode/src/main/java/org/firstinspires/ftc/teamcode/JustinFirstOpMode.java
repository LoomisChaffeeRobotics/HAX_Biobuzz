package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp
public class JustinFirstOpMode extends OpMode {
    DcMotor dylan;
    @Override
    public void init() {
        dylan = hardwareMap.get(DcMotor.class, "dylan");
    }

    @Override
    public void loop() {
        dylan.setPower(gamepad1.b? 1 : 0);
    }
}
