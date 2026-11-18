package org.firstinspires.ftc.teamcode.TeleOps;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.OpModeStorage;
import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

@TeleOp
@Config
public class TeleOpScrim extends RobotOpMode {
    Pose curPose;
    private boolean fieldCentricManual = true;

    private boolean isRed = true;
    private double rpm = 2195;

    @Override
    public void start(){
        // Restore after initialization, immediately before driver control starts.
        curPose = OpModeStorage.autonomousEndPose;
        robot.drive.setPose(curPose);
        robot.drive.follower.update();

        robot.shooterN.stopShoot();
        robot.shooterP.stopShoot();
        robot.intake.off().schedule();

        robot.drive.setIsRed(isRed); //in past games we had this be able to be changed in-game (maybe not the best option)
    }

    @Override
    public void loop(){
        update();
        if (gamepad1.options) {
            robot.drive.setHeading(0);
        }

//        if (gamepad1.dpadLeftWasPressed()){
//            fieldCentricManual = !fieldCentricManual;
//        }

        robot.drive.pePaDrive(
                gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x
        );


        if (gamepad2.left_bumper){

            robot.intake.on().schedule();
        }
        else if (gamepad2.left_trigger > 0.2){
            robot.intake.reverse().schedule();
        }
        else{
            robot.intake.off().schedule();
        }

        if (gamepad1.right_trigger > 0.2){
//            robot.shooterN.shoot();
            robot.shooterP.shoot();
        }
        else{
            robot.shooterP.stopShoot();
//            robot.shooterN.stopShoot();
        }
//
        if (gamepad2.right_bumper){
            robot.shooterP.setRPMByHand(rpm);
        } else {
            robot.shooterP.setRPMByHand(0);
        }
        if (gamepad2.yWasPressed()){
            rpm += 5;
        }
        else if (gamepad2.aWasPressed()){
            rpm -= 5;
        }
        if (gamepad2.dpadUpWasPressed()){
            robot.intake.increaseIntakePower();
        }
        else if (gamepad2.dpadDownWasPressed()){
            robot.intake.decreaseIntakePower();
        }




//        else if (gamepad1.aWasPressed()){
//
//            if (robot.shooterP.rpm ==2000){
//                robot.shooterP.setRPMByHand(0);
//            }
//            else {
//                robot.shooterP.setRPMByHand(2000);
//            }
//        }
        super.loop();
    }

    private void update(){
//        telemetry.addData("Drive mode", fieldCentricManual ? "Field-centricentric" : "Not Field-centric");
//        telemetry.addData("position: ", robot.drive.getPose());
//        telemetry.addData("Tags seen: ", robot.localization.tagsSeen(isRed));
        curPose = robot.drive.getPose();
        telemetry.addData("rpm: ", rpm);

        double distanceFromHive = robot.drive.getDistanceFromHive();
        robot.shooterP.setDistanceFromHive(distanceFromHive);
//        robot.shooterN.setDistanceFromHive(distanceFromHive);

        robot.indicator.update(robot.shooterP.getSpunUp());
    }
}
