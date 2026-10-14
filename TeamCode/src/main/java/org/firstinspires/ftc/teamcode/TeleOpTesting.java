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
    FieldCentricDriving drive;
    pedroOdom pedroOdom_;

    Telemetry t2;
    FtcDashboard dash;

    Telemetry telemetry;
    Limelight limelight;

    double gpLeft = 0;
    double gpRight = 0;
    double xpos = 0;
    double ypos = 0;

    double x_start_pos = 0;
    double y_start_pos = 0;
    Pose2D pos = new Pose2D(DistanceUnit.INCH, 0.0, 0.0, AngleUnit.DEGREES, 0.0);
    Pose2d newpos = new Pose2d(0.0, 0.0, 0.0);

    boolean aprilSeen = false;
    double aprilRoll = 0.0f;

    @Override
    public void init() {
        shooterPollen = new Shooter();
        shooterNectar = new Shooter();
        Intake = new Intake();
        drive = new FieldCentricDriving();
        pedroOdom_ = new pedroOdom();
        limelight = new Limelight(this);

        /*
        shooterPollen.init(hardwareMap, "flickerP", "flywheelP");
        shooterNectar.init(hardwareMap, "flickerN", "flywheelN");
        */

        drive.init(hardwareMap, telemetry);
        pedroOdom_.init(hardwareMap, telemetry);

        dash = FtcDashboard.getInstance();
        t2 = dash.getTelemetry();
        limelight.init();
    }

    @Override
    public void loop() {

        pedroOdom_.loop();
        double[] position_array = pedroOdom_.getPosition(); // x, y, heading
        pos = new Pose2D(DistanceUnit.INCH, position_array[0], position_array[1], AngleUnit.DEGREES, position_array[2]);
        xpos = pos.getX(DistanceUnit.INCH) + x_start_pos;
        ypos = pos.getY(DistanceUnit.INCH) + y_start_pos;

        newpos = new Pose2d(xpos, ypos, 0.0);

        gpLeft = Math.abs(gamepad1.left_stick_y) > 0.1 ? gamepad1.left_stick_y : 0;
        gpRight = Math.abs(gamepad1.right_stick_x) > 0.1 ? gamepad1.right_stick_x : 0;

        drive.simpleHDrive(-gpLeft, -gpRight);

        aprilSeen = limelight.seesTag(false);
        //aprilRoll = limelight.absValAprilTagRoll(0); // CHANGE THIS LATER FOR DIFFERENT APRILTAGS!!!!!!!!!!!!

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
        if (aprilSeen) {
            t2.addLine("limelight IS VISIBLE!!!!!!!!!!!!!\n\n");
        } else {
            t2.addLine("limelight IS NOT VISIBLE!!!!!!!!!!!!!\n\n");
        }

//        if (aprilSeen && aprilRoll < 90) {
//            t2.addLine(String.format("we CAN score here!!!!!!!!!!!!! %d\n\n", aprilRoll));
//        } else if (aprilSeen) {
//            t2.addLine(String.format("we CAN NOT score here!!!!!!!!!!!!! %d\n\n", aprilRoll));
//        }

        //draw robots location on field
        packet.fieldOverlay().setStroke("#3F51B5");
        Drawing.drawRobot(packet.fieldOverlay(), newpos);
        packet.fieldOverlay().setStroke("#FF51B5");
        t2.update();
    }
}


















