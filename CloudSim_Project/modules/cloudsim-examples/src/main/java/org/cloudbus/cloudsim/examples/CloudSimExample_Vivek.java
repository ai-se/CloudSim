/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.examples;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.UtilizationModelNull;
import org.cloudbus.cloudsim.UtilizationModelStochastic;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.VmStateHistoryEntry;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.power.Constants;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.util.MathUtil;


/**
 * A simple example showing how to create
 * a datacenter with two hosts and run two
 * cloudlets on it. The cloudlets run in
 * VMs with different MIPS requirements.
 * The cloudlets will take different time
 * to complete the execution depending on
 * the requested VM performance.
 */
public class CloudSimExample_Vivek {
	public static final String ENERGY_CONSUMPTION = "energy";
	public static final String AVERAGE_SLA = "sla";
	public static final String MEAN_HOST_SHUTDOWN_TIME = "shutdown";
	public static final String OVERALL_SLA = "overall_sla";
	public final static double SIMULATION_LIMIT = 6 * 60 * 60;
	public static int CLOUDLET_LENGTH	= 2500 * (int) SIMULATION_LIMIT;
	public static int CLOUDLET_PES	= 1;

	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;

	/** The vmlist. */
	private static List<Vm> vmlist;

	/**
	 * Creates main() to run this example
	 */
	public static void main(String[] args) {

		Log.printLine("Starting CloudSimExample_Vivek...");
		
		int[] list_of_hostType = {1, 2, 3, 1, 2, 3, 1, 2, 3, 4, 2, 3, 1, 2,3, 4};
		int[] list_of_vmType = {1, 2, 3, 1, 2, 3, 1, 2, 3, 4, 2, 3, 1, 2,3, 4};
		int number_of_cloudlets = 25;

		try {
			// First step: Initialize the CloudSim package. It should be called
			// before creating any entities.
			int num_user = 1;   // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false;  // mean trace events

			// Initialize the CloudSim library
			CloudSim.init(num_user, calendar, trace_flag);

			// Second step: Create Datacenters
			//Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
			@SuppressWarnings("unused")
			Datacenter datacenter0 = createDatacenter("Datacenter_0", list_of_hostType.length, list_of_hostType);

			//Third step: Create Broker
			DatacenterBroker broker = createBroker();
			int brokerId = broker.getId();

			//Fourth step: Create one virtual machine
			vmlist = new ArrayList<Vm>();
			
			//add the VMs to the vmList
			vmlist = createVmList(brokerId, list_of_vmType.length, list_of_vmType );

			//submit vm list to the broker
			broker.submitVmList(vmlist);


			//Fifth step: Create two Cloudlets
			cloudletList = createCloudletList(brokerId, number_of_cloudlets);
			

			//submit cloudlet list to the broker
			broker.submitCloudletList(cloudletList);

//			CloudSim.terminateSimulation(SIMULATION_LIMIT);
			// Sixth step: Starts the simulation
			CloudSim.startSimulation();


			// Final step: Print results when simulation is over
			List<Cloudlet> newList = broker.getCloudletReceivedList();

			CloudSim.stopSimulation();

			Map<String, Double> objectiveScores = new HashMap<String,Double>();
			Map<String, Double> slaMetrics = getSlaMetrics(vmlist);
			double slaAverage = slaMetrics.get("average");
			objectiveScores.put(AVERAGE_SLA, slaAverage);
			
			double slaOverall = slaMetrics.get("overall");
			objectiveScores.put(OVERALL_SLA, slaOverall);
        	printCloudletList(newList);
			System.out.println("slaAverage: " + slaAverage);
			System.out.println("slaOverall: " + slaOverall);
			


			Log.printLine("CloudSimExample_Vivek finished!");
		}
		catch (Exception e) {
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
		}
	}
	
	private static List<Cloudlet> createCloudletList(int brokerId, int number_of_cloudlets){
		//Cloudlet properties
		long length = CLOUDLET_LENGTH;
		long fileSize = 300;
		long outputSize = 300;
		int pesNumber = CLOUDLET_PES;
		int seed = 200;
		UtilizationModel utilizationModelNull = new UtilizationModelNull();
		List<Cloudlet> cloudletList = new ArrayList<Cloudlet>();
		
		for(int i=0; i < number_of_cloudlets; i++){
			Cloudlet temp = new Cloudlet(i, length, pesNumber, fileSize, outputSize, new UtilizationModelStochastic(seed * i), utilizationModelNull, utilizationModelNull);
			temp.setUserId(brokerId);
			cloudletList.add(temp);
		}
		return cloudletList;
	}

	private static Datacenter createDatacenter(String name, int number_of_hosts, int[] list_of_hostType){
		
		int[] hostPes = {2, 2, 2, 2};
		int[] hostMips = {5000, 5000, 5000, 5000};
		int[] hostRam = {4096, 4096, 4096, 4096};

		// 1. We need to create a list to store
		//    our machine
		List<Host> hostList = new ArrayList<Host>();

		// 2. A Machine contains one or more PEs or CPUs/Cores.

		long storage = 1000000; //host storage
		int bw = 10000;
		// 3. Create PEs and add these into a list.
		for(int hostId=0; hostId < number_of_hosts; hostId++){
			List<Pe> peList = new ArrayList<Pe>();
			for(int pe=0; pe < list_of_hostType[hostId]; pe++){
				peList.add(new Pe(pe, new PeProvisionerSimple(hostMips[list_of_hostType[hostId]-1]))); // need to store Pe id and MIPS Rating
			}
			
			//4. Create Hosts with its id and list of PEs and add them to the list of machines
			hostList.add(
	    			new Host(
	    				hostId,
	    				new RamProvisionerSimple(hostRam[list_of_hostType[hostId]-1]),
	    				new BwProvisionerSimple(bw),
	    				storage,
	    				peList,
	    				new VmSchedulerTimeShared(peList)
	    			)
	    		); // This is our first machine
			
		}
		



		// 5. Create a DatacenterCharacteristics object that stores the
		//    properties of a data center: architecture, OS, list of
		//    Machines, allocation policy: time- or space-shared, time zone
		//    and its price (G$/Pe time unit).
		String arch = "x86";      // system architecture
		String os = "Linux";          // operating system
		String vmm = "Xen";
		double time_zone = 10.0;         // time zone this resource located
		double cost = 3.0;              // the cost of using processing in this resource
		double costPerMem = 0.05;		// the cost of using memory in this resource
		double costPerStorage = 0.001;	// the cost of using storage in this resource
		double costPerBw = 0.0;			// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datacenter;
	}

	//We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
	//to the specific rules of the simulated scenario
	private static DatacenterBroker createBroker(){

		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

	private static List<Vm> createVmList(int brokerId, int vmsNumber, int[] list_of_vmType){
		int[] vmPes = {1, 1, 1, 1};
		double[] vmMips = {2500, 2000, 1000, 500};
		int[] vmRam = {870, 1740, 1740, 613};
		long size = 10000; //image size (MB)
		long bw = 1000;
		String vmm = "Xen"; //VMM name

		List<Vm> vms = new ArrayList<Vm>();
		for(int vmid=0; vmid < vmsNumber; vmid++){
			System.out.println(vmid + "|" + vmid);
			vms.add(new Vm(vmid, brokerId, vmMips[list_of_vmType[vmid]-1], vmPes[list_of_vmType[vmid]-1], 
					vmRam[list_of_vmType[vmid]-1], bw, size, vmm, new CloudletSchedulerDynamicWorkload(vmMips[list_of_vmType[vmid]-1], vmPes[list_of_vmType[vmid]-1])));
		}
		return vms;
	}	
	
	/**
	 * Prints the Cloudlet objects
	 * @param list  list of Cloudlets
	 */
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
				Log.print("SUCCESS");

				Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
						indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())+
						indent + indent + dft.format(cloudlet.getFinishTime()));
			}
		}

	}
	
	public static Map<String, Double> getSlaMetrics(List<Vm> vms) {
		Map<String, Double> metrics = new HashMap<String, Double>();
		List<Double> slaViolation = new LinkedList<Double>();
		double totalAllocated = 0;
		double totalRequested = 0;
		double totalUnderAllocatedDueToMigration = 0;

		for (Vm vm : vms) {
			double vmTotalAllocated = 0;
			double vmTotalRequested = 0;
			double vmUnderAllocatedDueToMigration = 0;
			double previousTime = -1;
			double previousAllocated = 0;
			double previousRequested = 0;
			boolean previousIsInMigration = false;
			System.out.println(vm.getStateHistory().size());
			for (VmStateHistoryEntry entry : vm.getStateHistory()) {
				if (previousTime != -1) {
					double timeDiff = entry.getTime() - previousTime;
					vmTotalAllocated += previousAllocated * timeDiff;
					vmTotalRequested += previousRequested * timeDiff;

					if (previousAllocated < previousRequested) {
						slaViolation.add((previousRequested - previousAllocated) / previousRequested);
						if (previousIsInMigration) {
							vmUnderAllocatedDueToMigration += (previousRequested - previousAllocated)
									* timeDiff;
						}
					}
				}

				previousAllocated = entry.getAllocatedMips();
				previousRequested = entry.getRequestedMips();
				previousTime = entry.getTime();
				previousIsInMigration = entry.isInMigration();
				System.out.println("previousAllocated: " + previousAllocated);
				System.out.println("previousRequested: " + previousRequested);
				
			}
//			System.out.println("previousAllocated: " + previousAllocated);
//			System.out.println("previousRequested: " + previousRequested);

			totalAllocated += vmTotalAllocated;
			totalRequested += vmTotalRequested;
			totalUnderAllocatedDueToMigration += vmUnderAllocatedDueToMigration;
		}
		
		System.out.println("totalAllocated: " + totalAllocated);
		System.out.println("totalRequested: " + totalRequested);

		metrics.put("overall", (totalRequested - totalAllocated) / totalRequested);
		if (slaViolation.isEmpty()) {
			metrics.put("average", 0.);
		} else {
			metrics.put("average", MathUtil.mean(slaViolation));
		}
		metrics.put("underallocated_migration", totalUnderAllocatedDueToMigration / totalRequested);
		// metrics.put("sla_time_per_vm_with_migration", slaViolationTimePerVmWithMigration /
		// totalTime);
		// metrics.put("sla_time_per_vm_without_migration", slaViolationTimePerVmWithoutMigration /
		// totalTime);

		return metrics;
	}
}
