package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
//import com.pedropathing.
//import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
//import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot.Robot;
import com.pedropathing.api.PoseFactory;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Config
public class drivetrain {
    //public static PIDFCoefficients headingCoefficients = new PIDFCoefficients(1.75, 0, 0.09, 0);
    private final PoseFactory p = PoseFactory.degrees();
    public final DcMotorEx frontLeft;
    public final DcMotorEx frontRight;
    public final DcMotorEx backLeft;
    public final DcMotorEx backRight;
    public final Follower follower;
    private final Telemetry telemetry;
    //private final PIDFController headingController = new PIDFController(headingCoefficients);

    public drivetrain(Robot robot) {
        follower = Constants.create(robot.hardwareMap);
        frontLeft = robot.hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = robot.hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = robot.hardwareMap.get(DcMotorEx.class, "rearLeft");
        backRight = robot.hardwareMap.get(DcMotorEx.class, "rearRight");

        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry = robot.telemetry;
    }
    public void simpleHDrive(double leftStick, double rightStick){
        backLeft.setPower(leftStick - rightStick);
        backRight.setPower(leftStick + rightStick);
        frontLeft.setPower(leftStick - rightStick);
        frontRight.setPower(leftStick + rightStick);
    }

    public Pose getPose() {
        return follower.pose();
    }

    public void setPose(Pose pose) {
        follower.setPose(pose);
    }

    public Command followPath(Path path) {
        return follow(follower, path);
    }

    public Command periodic() {
        return infinite(() -> {
            follower.update();

            telemetry.addData("Current X", follower.pose().x());
            telemetry.addData("Current Y", follower.pose().y());
            telemetry.addData("Current Heading", follower.pose().heading());;
        });
    }
}
