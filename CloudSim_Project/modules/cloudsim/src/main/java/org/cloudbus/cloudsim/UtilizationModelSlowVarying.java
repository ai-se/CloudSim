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
public class UtilizationModelSlowVarying implements UtilizationModel {

	/** The scheduling interval. */
	private double schedulingInterval;

	/** The data (5 min * 288 = 24 hours). */
	private final double[] data = {1,1.4,1.8,2.2,2.6,3,3.4,3.8,4.2,4.6,5,5.4,5.8,6.2,6.6,7,7.4,7.8,8.2,8.6,
			9,9.4,9.8,10.2,10.6,11,11.4,11.8,12.2,12.6,13,13.4,13.8,14.2,14.6,15,15.4,15.8,16.2,16.6,17,17.4,
			17.8,18.2,18.6,19,19.4,19.8,20.2,20.6,21,21.4,21.8,22.2,22.6,23,23.4,23.8,24.2,24.6,25,25.4,25.8,
			26.2,26.6,27,27.4,27.8,28.2,28.6,29,29.4,29.8,30.2,30.6,31,31.4,31.8,32.2,32.6,33,33.4,33.8,34.2,
			34.6,35,35.4,35.8,36.2,36.6,37,37.4,37.8,38.2,38.6,39,39.4,39.8,40.2,40.6,41,41.4,41.8,42.2,42.6,
			43,43.4,43.8,44.2,44.6,45,45.4,45.8,46.2,46.6,47,47.4,47.8,48.2,48.6,49,49.4,49.8,50.2,50.6,51,
			51.4,51.8,52.2,52.6,53,53.4,53.8,54.2,54.6,55,55.4,55.8,56.2,56.6,57,57.4,57.8,58.2,58.6,59,59.4,
			59.8,60.2,60.6,61,61.4,61.8,62.2,62.6,63,63.4,63.8,64.2,64.6,65,65.4,65.8,66.2,66.6,67,67.4,67.8,
			68.2,68.6,69,69.4,69.8,70.2,70.6,71,71.4,71.8,72.2,72.6,73,73.4,73.8,74.2,74.6,75,75.4,75.8,76.2,
			76.6,77,77.4,77,76.6,76.2,75.8,75.4,75,74.6,74.2,73.8,73.4,73,72.6,72.2,71.8,71.4,71,70.6,70.2,
			69.8,69.4,69,68.6,68.2,67.8,67.4,67,66.6,66.2,65.8,65.4,65,64.6,64.2,63.8,63.4,63,62.6,62.2,61.8,
			61.4,61,60.6,60.2,59.8,59.4,59,58.6,58.2,57.8,57.4,57,56.6,56.2,55.8,55.4,55,54.6,54.2,53.8,53.4,
			53,52.6,52.2,51.8,51.4,51,50.6,50.2,49.8,49.4,49,48.6,48.2,47.8,47.4,47,46.6,46.2,45.8,45.4,45,44.6,
			44.2,43.8,43.4,43,42.6,42.2,41.8,41.4,41,40.6,40.2,39.8,39.4,39,38.6,38.2,37.8,37.4,37,36.6,36.2,
			35.8,35.4,35,34.6,34.2,33.8,33.4,33,32.6,32.2,31.8,31.4,31,30.6,30.2,29.8,29.4,29,28.6,28.2,27.8,
			27.4,27,26.6,26.2,25.8,25.4,25,24.6,24.2,23.8,23.4,23,22.6,22.2,21.8,21.4,21,20.6,20.2,19.8,19.4,
			19,18.6,18.2,17.8,17.4,17,16.6,16.2,15.8,15.4,15,14.6,14.2,13.8,13.4,13,12.6,12.2,11.8,11.4,11,
			10.6,10.2,9.8,9.4,9,8.6,8.2,7.8,7.4,7,6.6,6.2,5.8,5.4,5,4.6,4.2,3.8,3.4,3,2.6,2.2,1.8,1.4,1,0.6};

	/**
	 * Instantiates a new utilization model stochastic.
	 */
	public UtilizationModelSlowVarying(double schedulingInterval) {
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

