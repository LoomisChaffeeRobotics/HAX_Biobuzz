package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@TeleOp
public class TeleOpTesting extends OpMode {

    FieldCentricDriving Drive;

    Intake Intake;

    Shooter shooterPollen;
    Shooter shooterNectar;
    DcMotorEx flyWheelPollen;
    DcMotorEx flyWheelNectar;
    DcMotor  flickerPollen;
    DcMotor flickerNectar;
    FieldCentricDriving drive;
    pedroOdom deadwheels;

    Telemetry t2;
    FtcDashboard dash;

    Telemetry telemetry;

    double gpLeft = 0;
    double gpRight = 0;
    double xpos = 0;
    double ypos = 0;

    double x_start_pos = 0;
    double y_start_pos = 0;
    Pose2D pos = new Pose2D(DistanceUnit.CM, 0.0, 0.0, AngleUnit.DEGREES, 0.0);
    Pose2d newpos = new Pose2d(0.0, 0.0, 0.0);

    @Override
    public void init() {
        shooterPollen = new Shooter();
        shooterNectar = new Shooter();
        Intake = new Intake();
        drive = new FieldCentricDriving();
        deadwheels = new pedroOdom();

        /*
        shooterPollen.init(hardwareMap, "flickerP", "flywheelP");
        shooterNectar.init(hardwareMap, "flickerN", "flywheelN");
        */

        drive.init(hardwareMap, telemetry);
        deadwheels.init(hardwareMap, telemetry, 10.5, 11.1);

        dash = FtcDashboard.getInstance();
        t2 = dash.getTelemetry();
    }

    @Override
    public void loop() {

        deadwheels.updateDeadwheelPosition();
        pos = deadwheels.getDeadwheelPosition();
        xpos = pos.getX(DistanceUnit.CM) + x_start_pos;
        ypos = pos.getY(DistanceUnit.CM) + y_start_pos;

        newpos = new Pose2d(xpos, ypos, 0.0);

        gpLeft = Math.abs(gamepad1.left_stick_y) > 0.1 ? gamepad1.left_stick_y : 0;
        gpRight = Math.abs(gamepad1.right_stick_x) > 0.1 ? gamepad1.right_stick_x : 0;

        drive.simpleHDrive(gpLeft, gpRight);

        /*
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

         */
        TelemetryPacket packet = new TelemetryPacket();
        FtcDashboard.getInstance().sendTelemetryPacket(packet);
        t2.addData("xPos", xpos);
        t2.addData("yPos", ypos);
        t2.addData("Heading", pos.getHeading(AngleUnit.DEGREES));
        //draw robots location on field
        packet.fieldOverlay().setStroke("#3F51B5");
        Drawing.drawRobot(packet.fieldOverlay(), newpos);
        packet.fieldOverlay().setStroke("#FF51B5");
        t2.update();
    }
}


















