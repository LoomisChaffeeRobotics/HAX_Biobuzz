package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
    DcMotor intake;
    Servo hinge;
    private boolean hingeDown = false;
    private double intakeSpeed = 1;
    private double hingeUpPos = 0.5;
    private double hingeDownPos = 0.3;

    public void init(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotor.class, "intake");
        hinge = hardwareMap.get(Servo.class, "hinge");
    }

    void spinIn(){
        intake.setPower(intakeSpeed);
    }
    void spinOut(){
        intake.setPower(-intakeSpeed);
    }
    void stopSpin(){
        intake.setPower(0);
    }
    void toggleHinge() {
        if (hingeDown) {
            hingeDown = false;
            hinge.setPosition(hingeUpPos);
        }
        else{
            hingeDown = true;
            hinge.setPosition(hingeDownPos);
        }
    }


}
