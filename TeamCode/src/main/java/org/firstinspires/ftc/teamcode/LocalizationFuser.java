package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.ftccommon.internal.manualcontrol.parameters.ImuParameters;

public class LocalizationFuser {

    IMU imu;
    ElapsedTime elapsedTime;
    void init(HardwareMap hardwareMap){
        imu = hardwareMap.get(IMU.class, "imu");
        ImuParameters params = new ImuParameters(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        );

    }
}
