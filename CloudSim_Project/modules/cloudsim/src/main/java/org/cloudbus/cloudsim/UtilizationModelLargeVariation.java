/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim;


/**
 * Implements a model, according to which a Cloudlet generates
 * random resource utilization every time frame.
 * 
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 2.0
 * @todo This class is the only one that stores the utilization history and
 * make use of the time attribute at the {@link #getUtilization(double) } method.
 * Check the other classes to implement the same behavior
 * (that can be placed in the super class)
 */
public class UtilizationModelLargeVariation implements UtilizationModel {

	/** The scheduling interval. */
	private double schedulingInterval;

	/** The data (5 min * 288 = 24 hours). */
	private final double[] data = {10,13,18,18,21,25,29,28,30,31,32,34,36,38,36,34,39,41,40,39,39,
			40,42,46,51,53,52,49,51,51,53,50,55,54,51,49,46,50,54,54,56,55,54,57,76,63,63,60,63,61,
			59,62,57,56,56,52,48,49,52,51,50,52,55,56,51,50,53,54,52,50,51,50,48,51,52,47,50,47,46,
			47,49,46,45,43,39,42,40,39,42,44,39,37,40,39,38,33,32,28,31,32,32,33,31,33,34,36,35,34,
			33,33,31,32,34,33,33,31,31,31,29,27,27,29,29,28,29,29,29,31,32,33,34,32,34,34,33,34,35,
			35,33,34,35,37,36,38,36,34,33,32,31,29,33,35,38,40,43,44,44,44,46,51,54,57,58,61,63,65,
			69,74,75,77,79,78,76,78,76,75,76,75,74,75,76,75,75,74,78,79,75,74,75,78,76,73,75,76,75,
			78,78,76,75,76,74,75,77,74,75,76,75,74,75,76,74,78,78,76,75,77,75,74,76,78,75,73,75,74,
			77,76,74,75,74,75,76,75,78,76,75,74,71,69,66,64,61,59,56,54,52,49,47,46,45,42,41,39,38,
			36,33,31,28,27,25,23,22,21,18,15,14,12,11,10,15,16,18,13,21,23,15,16,15,14,16,23,24,12,
			24,23,18,25,16,18,16,14,16,17,18,19,18,20,22,23,21,24,23,22,24,23,24,23,22,25,26,25,27,
			28,28,28,26,27,25,26,23,26,28,29,30,33,36,39,38,39,38,40,42,41,43,42,41,42,40,38,36,33,
			32,30,28,28,27,29,28,29,30,31,32,31,32,30,32,31,30,31,32,32,33,34,32,35,34,34,36,37,36,
			37,35,30,31,34,35,37,39,40,41,44,46,49,50,52};

	/**
	 * Instantiates a new utilization model stochastic.
	 */
	public UtilizationModelLargeVariation(double schedulingInterval) {
		setSchedulingInterval(schedulingInterval);
	}
	
	/**
	 * Gets the scheduling interval.
	 * 
	 * @return the scheduling interval
	 */
	public double getSchedulingInterval() {
		return schedulingInterval;
	}
	
	/**
	 * Sets the scheduling interval.
	 * 
	 * @param schedulingInterval the new scheduling interval
	 */
	public void setSchedulingInterval(double schedulingInterval) {
		this.schedulingInterval = schedulingInterval;
	}


	@Override
	public double getUtilization(double time) {
		if (time % getSchedulingInterval() == 0) {
			return data[(int) time / (int) getSchedulingInterval()];
		}
		int time1 = (int) Math.floor(time / getSchedulingInterval());
		int time2 = (int) Math.ceil(time / getSchedulingInterval());
		double utilization1 = data[time1];
		double utilization2 = data[time2];
		double delta = (utilization2 - utilization1) / ((time2 - time1) * getSchedulingInterval());
		double utilization = utilization1 + delta * (time - time1 * getSchedulingInterval());
		return utilization;

	}
}

