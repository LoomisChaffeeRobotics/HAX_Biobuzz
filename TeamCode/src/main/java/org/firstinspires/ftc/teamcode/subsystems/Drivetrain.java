package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
//import com.pedropathing.
//import com.pedropathing.control.PIDFController;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;


import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
//import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.subsysOrig.FancyPID;

import com.pedropathing.api.PoseFactory;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.pedropathing.follower.ManualDrive;

@Config
public class Drivetrain {
    //public static PIDFCoefficients headingCoefficients = new PIDFCoefficients(1.75, 0, 0.09, 0);
    private final PoseFactory p = PoseFactory.degrees();
//    public final DcMotorEx frontLeft;
//    public final DcMotorEx frontRight;
//    public final DcMotorEx backLeft;
//    public final DcMotorEx backRight;
    public final Follower follower;
    private final Telemetry telemetry;
    public final Limelight limelight;
    private FancyPID pid;
    Pose target = p.of(0,0,0);
    Limelight.TagSet tagSet;


    private boolean isRed = true;
    private static double TURN_KP;
    private static double TURN_KI;
    private static double TURN_KD;
    //private final PIDFController headingController = new PIDFController(headingCoefficients);

    public Drivetrain(Robot robot) {
        follower = Constants.create(robot.hardwareMap);
        pid = new FancyPID();
        telemetry = robot.telemetry;
        limelight = new Limelight(robot);
    }

    public boolean llSeesTag(boolean onRed){
        return limelight.seesTag(onRed);
    }

    public void pePaDriveAuto(double lefty, double leftx, double rightx, boolean manual) {
        if (!manual){
            pePaDrive(lefty,leftx,rightx);
            return;
        }

        pid.setTarget(0);
        pid.setCoefficients(TURN_KP,TURN_KI,TURN_KD);
        pid.update(limelight.turnError(tagSet));

        double simulatedRX = pid.getVelocity();

        pePaDrive(lefty,leftx,simulatedRX);
    }
    public void pePaDrive(double lefty, double leftx, double rightx) {
        DrivePowers powers = ManualDrive.fieldCentric(
                -lefty,
                leftx,
                rightx,
                follower.pose().heading()
        );
        follower.manual(powers);
        follower.update();
    }

    public double getDistanceFromHive(){
        return follower.pose().distance(target);
    }

    public void setIsRed(boolean isRed){
        this.isRed = isRed;
    }
    public Pose getPose() {
        return follower.pose();
    }

    public void setPose(Pose pose) {
        follower.setPose(pose);
    }

    public void setHeading(double heading){
        follower.setHeading(heading);
    }

    public Command followPath(Path path) {
        return follow(follower, path);
    }

    public Command periodic() {
        return infinite(() -> {
            follower.update();

            boolean yIsOver70 = (follower.pose().y()>70.75);
            if (isRed && yIsOver70){
                target = p.of(57.5,90.5,0);
                tagSet = Limelight.TagSet.RED_BACK;
            } else if(isRed) {
                target = p.of(57.5,51,0);
                tagSet = Limelight.TagSet.RED_FRONT;
            } else if(yIsOver70){
                target = p.of(84,90.5,0);
                tagSet = Limelight.TagSet.BLUE_FRONT;
            } else {
                target = p.of(84,51,0);
                tagSet = Limelight.TagSet.BLUE_BACK;
            }

            telemetry.addData("Current X", follower.pose().x());
            telemetry.addData("Current Y", follower.pose().y());
            telemetry.addData("Current Heading", follower.pose().heading());;
        });
    }

}
