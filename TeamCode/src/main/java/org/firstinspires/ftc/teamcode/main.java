package org.firstinspires.ftc.teamcode;

import androidx.xr.runtime.Config;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp
public class main extends OpMode {

    FieldCentricDriving Drive;

    intake Intake;

    Shooter shooterPollen;
    Shooter shooterNectar;
    DcMotorEx flyWheelPollen;
    DcMotorEx flyWheelNectar;
    DcMotor  flickerPollen;
    DcMotor flickerNectar;
    FieldCentricDriving drive;

    Telemetry telemetry;

    double gpLeft = 0;
    double gpRight = 0;

    @Override
    public void init() {
        shooterPollen = new Shooter();
        shooterNectar = new Shooter();
        Intake = new intake();
        drive = new FieldCentricDriving();

        shooterPollen.init(hardwareMap, "flickerP", "flywheelP");
        shooterNectar.init(hardwareMap, "flickerN", "flywheelN");
        drive.init(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        gpLeft = Math.abs(gamepad1.left_stick_y) > 0.1 ? gamepad1.left_stick_y : 0;
        gpRight = Math.abs(gamepad1.right_stick_x) > 0.1 ? gamepad1.right_stick_y : 0;

        drive.simpleHDrive(gpLeft, gpRight);

        if (gamepad1.left_trigger > 0){
            Intake.spinIn();
        }
        else if (gamepad1.left_bumper){
            Intake.spinOut();
        }
        else{
            Intake.stopSpin();
        }

        if (gamepad1.right_bumper && gamepad1.right_trigger > 0){
            shooterNectar.shoot();
            shooterPollen.shoot();
        }
        else if(gamepad1.right_bumper){
            shooterNectar.shoot();
            shooterPollen.setBlock();
        }
        else if(gamepad1.right_trigger > 0){
            shooterPollen.shoot();
            shooterNectar.setBlock();
        }
        else{
            shooterNectar.setBlock();
            shooterPollen.setBlock();
        }

        if (gamepad1.aWasPressed()){
            Intake.toggleHinge();
        }

        if (gamepad1.yWasPressed()){
            shooterPollen.toggleSpinUp();
            shooterNectar.toggleSpinUp();
        }





        shooterNectar.update();
        shooterPollen.update();
    }
}


















