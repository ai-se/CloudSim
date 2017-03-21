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
public class UtilizationModelSteepTriPhase implements UtilizationModel {

	/** The scheduling interval. */
	private double schedulingInterval;

	/** The data (5 min * 288 = 24 hours). */
	private final double[] data = {5,7,6,6,4,3,7,3,3,5,7,5,4,6,3,5,6,3,6,6,3,7,4,3,5,6,5,4,4,6,
			3,6,7,5,7,7,3,3,6,5,3,5,5,6,3,3,5,3,4,7,4,3,6,4,4,4,7,3,6,5,6,3,3,3,6,3,4,5,4,3,4,5,
			3,7,3,4,5,5,7,6,13,20,27,34,41,48,55,54,55,55,54,55,54,54,55,56,56,55,55,54,56,56,55,
			56,55,54,54,56,55,56,55,54,55,55,55,56,56,55,55,55,56,55,55,55,56,54,55,56,54,56,56,
			56,54,54,55,56,55,56,54,55,54,55,55,55,54,56,54,56,55,55,56,55,56,55,54,55,55,56,54,
			56,54,56,54,56,56,56,54,56,54,54,55,55,55,56,54,54,54,55,55,56,56,55,54,56,55,54,56,
			55,56,55,55,55,54,55,55,56,56,56,55,54,56,55,54,55,56,55,55,55,56,55,54,55,55,56,56,
			55,56,55,56,54,54,54,56,55,55,56,54,56,55,55,56,55,54,56,56,55,54,54,54,56,55,55,55,
			55,55,54,56,55,54,54,56,56,56,56,54,55,56,56,56,54,56,55,55,56,55,54,56,55,56,54,50,
			46,42,38,34,30,26,22,18,14,10,9,8,7,6,4,5,6,4,5,6,6,5,4,4,6,5,6,4,6,4,6,4,6,5,6,4,6,
			4,5,6,5,4,5,4,6,4,6,6,6,5,6,4,6,4,4,5,6,6,4,4,5,6,5,6,6,5,4,4,4,5,6,6,4,5,6,5,5,4,4,
			4,5,5,5,5,5,4,5,4,5,5,4,4,6,6,5,5,5,4,4,5,6,6,4,6,5,5,6,4,6,4,6,4,5};

	/**
	 * Instantiates a new utilization model stochastic.
	 */
	public UtilizationModelSteepTriPhase(double schedulingInterval) {
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

