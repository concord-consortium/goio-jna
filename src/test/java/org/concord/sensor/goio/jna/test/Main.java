package org.concord.sensor.goio.jna.test;

import java.io.IOException;

import org.concord.sensor.goio.jna.*;
//import org.concord.sensor.goio.jna.GoIOInterface.GoIOSensor;

import com.sun.jna.Pointer;

//Test GoIO
public class Main {
	
	

	public static void main(String[] args) throws IOException {
		
		boolean sweet = false;
		boolean isthere = false;
		GoIOInterface goIOInterface;
		
		goIOInterface = new GoIOInterface();
		
		System.out.println("start main");
		
		sweet = goIOInterface.init();
		

		GoIOInterface.GoIOSensor sensor 
		= goIOInterface.mkSensor();
		
		if(!sweet)
		{
			System.out.println("goIOInterface.init() failed --bye");
			return;
		}


		isthere = goIOInterface.isGolinkAttached();
		System.out.println("Is golink there: "+isthere);		
	
		isthere = goIOInterface.getDeviceName(sensor);
		System.out.println("Got device name: "+isthere);
		
		goIOInterface.sensorOpen(sensor);

		sweet = goIOInterface.sensorSetMeasurementPeriod(sensor,0.040, GoIOLibrary.SKIP_TIMEOUT_MS_DEFAULT);
		System.out.println("sensorSetMeasurementPeriod: "+sweet);
		
		//FIX

		Pointer pParams = null;
		Pointer pRespBuf =null;
		int []pnRespBytes = null;
		
		sweet = goIOInterface.sensorSendCmd(sensor,
									GoIOLibrary.SKIP_CMD_ID_START_MEASUREMENTS, 							 
									pParams, 
									0, //null,
									pRespBuf, //null, 
									pnRespBytes,
									GoIOLibrary.SKIP_TIMEOUT_MS_DEFAULT
									);
		
		System.out.println("sensorSendCmd: "+sweet);
		
		//skulk for ~a sec
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("Bad sleep");
		}
		
		int MAX_NUM_MEASUREMENTS = 100;
		int [] pMeasurementsBuf = new int[MAX_NUM_MEASUREMENTS];
		
		int ret = goIOInterface.sensorReadRawMeasuements(sensor, pMeasurementsBuf, MAX_NUM_MEASUREMENTS);
		
		System.out.println("sensorReadRawMeasuements: number of bytes read: "+ret);
		
		//print the array
		int i = 0;
		 
		for(i=0;i<ret;i++) //array.length;i++)
		{
			System.out.println("> "+pMeasurementsBuf[i]);
		}
		
		//end
		goIOInterface.cleanup();
		
		System.out.println("end  main");
	};//end main

}

