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
public class UtilizationModelQuickVarying implements UtilizationModel {

	/** The scheduling interval. */
	private double schedulingInterval;

	/** The data (5 min * 288 = 24 hours). */
	private final double[] data = {1,1.5,2,2.5,3,3.5,4,4.5,5,5.5,6,6.5,7,7.5,8,8.5,9,9.5,10,10.5,11,
			11.5,12,12.5,13,13.5,14,14.5,15,15.5,16,16.5,17,17.5,18,18.5,19,19.5,20,20.5,21,21.5,22,
			22.5,23,23.5,24,24.5,25,25.5,25.1,24.7,24.3,23.9,23.5,23.1,22.7,22.3,21.9,21.5,21.1,20.7,
			20.3,19.9,19.5,19.1,18.7,18.3,17.9,17.5,17.1,16.7,16.3,15.9,15.5,15.1,14.7,14.3,13.9,13.5,
			13.1,12.7,12.3,11.9,11.5,11.1,10.7,10.3,9.9,11.4,12.9,14.4,15.9,17.4,18.9,20.4,21.9,23.4,
			24.9,26.4,27.9,29.4,30.9,32.4,33.9,35.4,36.9,38.4,39.9,41.4,42.9,44.4,45.9,47.4,48.9,50.4,
			51.9,53.4,54.9,56.4,57.9,59.4,60.9,62.4,63.9,65.4,66.9,68.4,69.9,71.4,69.9,68.4,66.9,65.4,
			63.9,62.4,60.9,59.4,57.9,56.4,54.9,53.4,51.9,50.4,48.9,47.4,45.9,44.4,42.9,41.4,39.9,38.4,
			36.9,35.4,33.9,32.4,30.9,29.4,27.9,26.4,24.9,23.4,21.9,20.4,18.9,20.9,22.9,24.9,26.9,28.9,
			30.9,32.9,34.9,36.9,38.9,40.9,42.9,44.9,46.9,48.9,50.9,52.9,54.9,56.9,58.9,60.9,62.9,64.9,
			66.9,68.9,70.9,72.9,74.9,76.9,78.9,80.9,82.9,80.9,78.9,76.9,74.9,72.9,70.9,68.9,66.9,64.9,
			62.9,60.9,58.9,56.9,54.9,52.9,50.9,48.9,46.9,44.9,42.9,40.9,38.9,36.9,34.9,32.9,30.9,28.9,
			26.9,24.9,22.9,20.9,18.9,16.9,14.9,12.9,10.9,12.9,14.9,16.9,18.9,20.9,22.9,24.9,26.9,28.9,
			30.9,32.9,34.9,36.9,38.9,40.9,42.9,44.9,46.9,48.9,50.9,52.9,54.9,56.9,58.9,56.4,53.9,51.4,
			48.9,46.4,43.9,41.4,38.9,36.4,33.9,31.4,28.9,26.4,23.9,21.4,18.9,16.4,13.9,11.4,13.9,16.4,
			18.9,21.4,23.9,26.4,28.9,31.4,33.9,36.4,38.9,41.4,43.9,46.4,48.9,51.4,53.9,56.4,58.9,61.4,
			63.9,66.4,68.9,71.4,73.9,76.4,78.9,76.4,73.9,71.4,68.9,66.4,63.9,61.4,58.9,56.4,53.9,51.4,
			48.9,46.4,43.9,41.4,38.9,36.4,33.9,31.4,28.9,26.4,23.9,21.4,18.9,16.4,13.9,11.4,13.9,16.4,
			18.9,21.4,23.9,26.4,28.9,31.4,33.9,36.4,38.9,41.4,43.9,46.4,48.9,51.4,53.9,56.4,58.9,61.4,
			63.9,66.4,68.9,71.4,73.9,71.4,68.9,66.4,63.9,61.4,58.9,56.4,53.9,51.4,48.9,46.4,43.9,41.4,
			38.9,36.4,33.9,31.4,28.9,26.4,23.9,21.4,18.9,16.4,13.9,11.4,8.9,6.4,3.9,1.4};

	/**
	 * Instantiates a new utilization model stochastic.
	 */
	public UtilizationModelQuickVarying(double schedulingInterval) {
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

