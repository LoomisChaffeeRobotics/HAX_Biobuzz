package org.firstinspires.ftc.teamcode.TeleOps;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

@TeleOp(name = "TeleOp", group = "Competition")
@Config
public class TeleOpNew extends RobotOpMode {
    double gpLeft = 0;
    double gpRight = 0;
    Pose curPose = robot.drive.getPose();

    @Override
    public void init(){
        super.init();

    }

    @Override
    public void start(){
        robot.shooterN.stopShoot();
        robot.shooterP.stopShoot();
        robot.intake.off().schedule();
    }

    @Override
    public void loop(){
        gpLeft = Math.abs(gamepad1.left_stick_y) > 0.1 ? gamepad1.left_stick_y : 0;
        gpRight = Math.abs(gamepad1.right_stick_x) > 0.1 ? gamepad1.right_stick_x : 0;

        robot.drive.simpleHDrive(gpLeft, gpRight);

        curPose = robot.drive.getPose();

        if (gamepad1.left_trigger > 0){
            robot.intake.on().schedule();
        }
        else if (gamepad1.left_bumper){
            robot.intake.reverse().schedule();
        }
        else{
            robot.intake.off().schedule();
        }

        if (gamepad1.right_bumper && gamepad1.right_trigger > 0){
            robot.shooterN.shoot();
            robot.shooterP.shoot();
        }
        else if(gamepad1.right_bumper){
            robot.shooterN.shoot();
            robot.shooterP.stopShoot();
        }
        else if(gamepad1.right_trigger > 0){
            robot.shooterP.shoot();
            robot.shooterN.stopShoot();
        }
        else{
            robot.shooterP.stopShoot();
            robot.shooterN.stopShoot();
        }

        if (gamepad1.aWasPressed()) {
            if (robot.intake.isHingeDown()) {
                robot.intake.hingeUp();
            } else {
                robot.intake.hingeDown();
            }
        }

        if (gamepad1.yWasPressed()){
            if (robot.shooterN.getSpining()){
                robot.shooterN.spinDown();
            }
            else{
                robot.shooterN.spinUp();
            }
            if (robot.shooterP.getSpining()){
                robot.shooterP.spinDown();
            }
            else{
                robot.shooterP.spinUp();
            }
        }


    }
}
