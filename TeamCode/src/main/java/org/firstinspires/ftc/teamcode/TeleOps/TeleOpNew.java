package org.firstinspires.ftc.teamcode.TeleOps;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.autoMode.autoParts.Memory;
import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

@TeleOp(name = "TeleOp", group = "Competition")
@Config
public class TeleOpNew extends RobotOpMode {
    double gpLeft = 0;
    double gpRight = 0;
    Pose curPose;
    private boolean fieldCentric = true;

    @Override
    public void init(){
        super.init();
        curPose = Memory.endPose;

    }


    @Override
    public void start(){
        robot.shooterN.stopShoot();
        robot.shooterP.stopShoot();
        robot.intake.off().schedule();
    }

    @Override
    public void loop(){
        if (gamepad1.dpadLeftWasPressed()) {
            fieldCentric = !fieldCentric;
        }

        if (fieldCentric) {
            robot.drive.pePaDrive(
                    gamepad1.left_stick_y,
                    gamepad1.left_stick_x,
                    gamepad1.right_stick_x
            );
        } else {
            robot.drive.simpleHDrive(gamepad1.left_stick_y, gamepad1.right_stick_x);
        }
        telemetry.addData("Drive mode", fieldCentric ? "Field-centric" : "Simple H drive");

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

        super.loop();
    }
}
