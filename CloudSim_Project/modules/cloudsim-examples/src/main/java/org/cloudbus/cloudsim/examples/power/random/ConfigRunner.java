package org.cloudbus.cloudsim.examples.power.random;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.examples.power.Constants;
import org.cloudbus.cloudsim.examples.power.Helper;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.util.MathUtil;

//import cloudsim.power.optimization.PolicyConstants;

public class ConfigRunner {
	
	public static final String ENERGY_CONSUMPTION = "energy";
	public static final String AVERAGE_SLA = "sla";
	public static final String MEAN_HOST_SHUTDOWN_TIME = "shutdown";

	private static boolean enableOutput;
	private static boolean outputToFile;
	private String inputFolder = "";
	private String outputFolder = "";
	private String workload = "random"; // Random workload

	public String getInputFolder() {
		return inputFolder;
	}

	public void setInputFolder(String inputFolder) {
		this.inputFolder = inputFolder;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	public String getWorkload() {
		return workload;
	}

	public void setWorkload(String workload) {
		this.workload = workload;
	}

	public ConfigRunner(boolean enableOuput, boolean outputToFile, String inputFolder, String outputFolder) {
		ConfigRunner.enableOutput = enableOuput;
		ConfigRunner.outputToFile = outputToFile;
		this.inputFolder = inputFolder;
		this.outputFolder = outputFolder;
	}

	public Map<String, Double> genericRunner(String vmAllocationPolicy, String vmSelectionPolicy, String parameter,
			int hosts, int vms, int[] vmPes, int[] vmMips, int[] vmRam, int[] hostPes, int[] hostMips, int[] hostRam,
			int hostBw, int hostStorage, int vmBm, int hostType, int vmTypes) throws IOException {
		
		String workload = "random";
		Constants.CLOUDLET_PES = 1;
		RandomConstants.NUMBER_OF_HOSTS = hosts;
		RandomConstants.NUMBER_OF_VMS = vms;
		Constants.VM_PES = vmPes;
		Constants.VM_MIPS = vmMips;
		Constants.VM_RAM = vmRam;
		Constants.HOST_PES = hostPes;
		Constants.HOST_MIPS = hostMips;
		Constants.HOST_RAM = hostRam;
		Constants.HOST_BW = hostBw;
		Constants.HOST_STORAGE = hostStorage;
		Constants.VM_BW = vmBm;
		Constants.HOST_TYPES = hostType;
		Constants.VM_TYPES = vmTypes;
		
//		System.out.println("Host :" + RandomConstants.NUMBER_OF_HOSTS);
//		System.out.println("VM : " + RandomConstants.NUMBER_OF_VMS);
//		System.out.println("Parameter : " + parameter);
//		System.out.println("vmAllocationPolicy : " + vmAllocationPolicy);
//		System.out.println("vmSelectionPolicy : " + vmSelectionPolicy)1;
//		System.out.println("vmPes : " + Arrays.toString(vmPes));
//		System.out.println("vmMips : " + Arrays.toString(vmMips));
//		System.out.println("vmRam : " + Arrays.toString(vmRam));
//		System.out.println("hostPes : " + Arrays.toString(hostPes));
//		System.out.println("hostMips : " + Arrays.toString(hostMips));
//		System.out.println("hostRam : " + Arrays.toString(hostRam));
//		System.out.println("hostBw : " + hostBw);
//		System.out.println("hostStorage : " + hostStorage);
//		System.out.println("vmBm : " + vmBm);
//		System.out.println("hostType : " + hostType);
//		System.out.println("vmTypes : " + vmTypes);
		
		long start = System.currentTimeMillis();
		RandomRunner runner = new RandomRunner(enableOutput, outputToFile, inputFolder, outputFolder, workload,
				vmAllocationPolicy, vmSelectionPolicy, parameter);
		long end = System.currentTimeMillis();
		System.out.println("\nTotal simulation time : " + (end - start) + " ms");

		PowerDatacenter datacenter = runner.getmDataCenter();
		
		Map<String, Double> objectiveScores = new HashMap<String,Double>();
		Map<String, Double> slaMetrics = Helper.getSlaMetrics(runner.getVmList());
		double slaAverage = slaMetrics.get("average");
		objectiveScores.put(AVERAGE_SLA, slaAverage);
		
		List<Double> timeBeforeHostShutdown = Helper.getTimesBeforeHostShutdown(datacenter.getHostList());
		
		double meanTimeBeforeHostShutdown = Double.NaN;
		if (!timeBeforeHostShutdown.isEmpty()) {
			meanTimeBeforeHostShutdown = MathUtil.mean(timeBeforeHostShutdown);
		}
		objectiveScores.put(MEAN_HOST_SHUTDOWN_TIME, meanTimeBeforeHostShutdown);
		
		double energy = datacenter.getPower() / (3600 * 1000);
		
		objectiveScores.put(ENERGY_CONSUMPTION, energy);
		if (objectiveScores.size() > 0)
			return objectiveScores;
		else
			return null;
	}

	/**
	 * The main method just for testing.
	 * 
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	
	public static void main(String[] args) throws IOException {
		
		int[] vmPes = {1, 1, 1, 1};
		int[] vmMips = {2500, 2000, 1000, 500};
		int[] vmRam = {870, 1740, 1740, 613};
		int[] hostPes = {2, 2, 2, 2};
		int[] hostMips = {5000, 5000, 5000, 5000};
		int[] hostRam = {4096, 4096, 4096, 4096};
		
		int [] vmDis = {15,25, 24, 16};
		int [] hostDis = {5,15, 13, 7};
		
		
		// invoke model here
		MainRunner runner = new MainRunner(false, false, "", "");
		String vmAllocationPolicy = args[0];
		String vmSelectionPolicy = args[1];
		double parameter = Double.parseDouble(args[2]);
		int hosts = Integer.parseInt(args[3]);
		int vms = Integer.parseInt(args[4]);
		int actualVmType = 4;
		int actualHostType = 4;


		int vmBw = Integer.parseInt(args[5]);
		int hostBw = Integer.parseInt(args[6]);
		int hostStorage = Integer.parseInt(args[7]);
		
//		int vm_dist_1 = Integer.parseInt(args[8]);
//		int vm_dist_2 = Integer.parseInt(args[9]);
//		int vm_dist_3 = Integer.parseInt(args[10]);
//		int vm_dist_4 = Integer.parseInt(args[11]);
		
		int [] vm_dist = {
				Integer.parseInt(args[8]),
				Integer.parseInt(args[9]),
				Integer.parseInt(args[10]),
				Integer.parseInt(args[11])	
		};
		
		int[] h_dist = {
				Integer.parseInt(args[12]),
				Integer.parseInt(args[13]),
				Integer.parseInt(args[14]),
				Integer.parseInt(args[15])	
		};

		try {
			if (vms <= 2 * hosts) {
				// Do nothing
			} else {
				vms = 2 * hosts;
			}
			Map<String, Double> objectives = runner.genericRunner(vmAllocationPolicy,
					vmSelectionPolicy, parameter + "", hosts, vms, vmPes,
					vmMips, vmRam, hostPes, hostMips, hostRam, hostBw, hostStorage, vmBw, actualHostType,
					actualVmType, vm_dist, h_dist);
		System.out.println(objectives.get(ENERGY_CONSUMPTION) + ", " + 
					objectives.get(AVERAGE_SLA) + ", " + objectives.get(MEAN_HOST_SHUTDOWN_TIME));
			
		} catch (IOException e) {
			System.out.println("Something falied in the model :");
			e.printStackTrace();
		}

		
	
	}
	

	
	

}
