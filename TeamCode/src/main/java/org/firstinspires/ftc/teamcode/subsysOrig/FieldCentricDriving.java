package org.firstinspires.ftc.teamcode.subsysOrig;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class FieldCentricDriving {

    DcMotor rearLeft;
    DcMotor rearRight;
    DcMotor frontLeft;
    DcMotor frontRight;

    boolean autoDrive = false;

    double yaw = 0d;
    double x = 0d;
    double y = 0d;

    double kp = 0.00;
    double ki = 0.00;
    double kd = 0.00;

    Telemetry telemetryDrive;

    FancyPID pid = new FancyPID();
    void init(HardwareMap hardwareMap, Telemetry telemetry){

        telemetryDrive = telemetry;
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");

        rearLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        rearRight.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);

        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        pid.setCoefficients(kp,ki,kd);
        pid.init();
    }

    void drive(double leftX, double leftY, double rightX){
        double x = leftX;
        double y = leftY;
        double rx = rightX;

        double s = Math.sin(yaw);
        double c = Math.cos(yaw);
        double xRotated = x * c + y * s;
        double yRotated = y * c - x * s;

        double rl = yRotated - xRotated - rx;
        double rr = yRotated + xRotated + rx;
        double fl = yRotated + xRotated - rx;
        double fr = yRotated - xRotated + rx;

        double denominator = findMaxAbs(1d, rl, rr, fl, fr);

        rearLeft.setPower(rl/denominator);
        rearRight.setPower(rr/denominator);
        frontLeft.setPower(fl/denominator);
        frontRight.setPower(fr/denominator);

        telemetryDrive.addData("rl: ", rl);
    }

    void simpleHDrive(double leftStick, double rightStick){
        rearLeft.setPower(leftStick - rightStick);
        rearRight.setPower(leftStick + rightStick);
        frontLeft.setPower(leftStick - rightStick);
        frontRight.setPower(leftStick + rightStick);
    }

    public double findMaxAbs(double a,double b, double c, double d, double e){

        b = Math.abs(b);
        c = Math.abs(c);
        d = Math.abs(d);
        e = Math.abs(e);

        double ret = a;
        ret = Math.max(ret, b);
        ret = Math.max(ret, c);
        ret = Math.max(ret, d);
        ret = Math.max(ret, e);

        return ret;
    }

    void setMotorPowers(double leftStickX, double leftStickY, double rightStickX){
        if (!autoDrive){
            drive(leftStickX,leftStickY,rightStickX);
            pid.init(); // i dont want integral windup or weird stuff so i reset it
            return;
        }
        double displacementFromHiveX = x - 72d;
        double displacementFromHiveY = y - 72d;
        double angleToHive = Math.atan2(displacementFromHiveY,displacementFromHiveX);
        pid.setTarget(angleToHive);
        pid.update(yaw);
        double rotationalVelocity = pid.getVelocity();

        drive(leftStickX,leftStickY,rotationalVelocity);
    }

    void updatePosition(Pose2D position){
        yaw = position.getHeading(AngleUnit.RADIANS);
        x = position.getX(DistanceUnit.INCH);
        y = position.getY(DistanceUnit.INCH);
    }


}
