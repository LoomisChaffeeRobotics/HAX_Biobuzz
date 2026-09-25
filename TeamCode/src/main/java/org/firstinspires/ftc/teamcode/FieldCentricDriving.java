package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class FieldCentricDriving {

    DcMotor rearLeft;
    DcMotor rearRight;
    DcMotor frontLeft;
    DcMotor frontRight;

    boolean autoDrive = false;

    double yaw = 0d;
    void init(HardwareMap hardwareMap){

        rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");

        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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
            return;
        }
        Pose2D error;

    }

    void updatePosition(Pose2D position){
        yaw = position.getHeading(AngleUnit.RADIANS);


    }


}
