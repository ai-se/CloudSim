package org.cloudbus.cloudsim.examples.power.planetlab;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.examples.power.Constants;
import org.cloudbus.cloudsim.examples.power.Helper;
import org.cloudbus.cloudsim.examples.power.random.RandomConstants;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.util.MathUtil;

/**
 * This is a universal example runner that can be used for running examples from console. All the
 * parameters are specified as command line parameters.
 * 
 * If you are using any algorithms, policies or workload included in the power package please cite
 * the following paper:
 * 
 * Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
 * Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
 * Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
 * Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012
 * 
 * @author Anton Beloglazov
 * @since Jan 5, 2012
 */
public class PlanetLabRunnerConsole {
	public static final String ENERGY_CONSUMPTION = "energy";
	public static final String AVERAGE_SLA = "sla";
	public static final String MEAN_HOST_SHUTDOWN_TIME = "shutdown";
	public static final String OVERALL_SLA = "overall_sla";

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		int[] vmPes = {1, 1, 1, 1};
		int[] vmMips = {2500, 2000, 1000, 500};
		int[] vmRam = {870, 1740, 1740, 613};
		int[] hostPes = {2, 2, 2, 2};
		int[] hostMips = {5000, 5000, 5000, 5000};
		int[] hostRam = {4096, 4096, 4096, 4096};
		
		int [] vmDis = {1100, 200, 200, 200};
		int [] hostDis = {1100, 200, 200, 200};
		boolean enableOutput = false;
		boolean outputToFile = true;
		if (args[0].equals("1")) {
			enableOutput = true;
		}
		//remove: added by vivek
		enableOutput = true;
		
		String inputFolder = args[1];
		String outputFolder = args[2];
		String workload = args[3];
		String vmAllocationPolicy = args[4];
		String vmSelectionPolicy = "";
		if (args.length >= 6 && args[5] != null && !args[5].isEmpty()) {
			vmSelectionPolicy = args[5];
		}
		String parameter = "";
		if (args.length >= 7 && args[6] != null && !args[6].isEmpty()) {
			parameter = args[6];
		}
		RandomConstants.VM_DIST = vmDis;
		RandomConstants.HOST_DIST = hostDis;
		
		Constants.CLOUDLET_PES = 1;
		RandomConstants.NUMBER_OF_HOSTS = 1100;
		RandomConstants.NUMBER_OF_VMS = 800;
		RandomConstants.NUMBER_OF_CLOUDLETS = 1000;

		PlanetLabRunner runner = new PlanetLabRunner(
				enableOutput,
				outputToFile,
				inputFolder,
				outputFolder,
				workload,
				vmAllocationPolicy,
				vmSelectionPolicy,
				parameter);
		
		PowerDatacenter datacenter = runner.getmDataCenter();
		
		Map<String, Double> objectiveScores = new HashMap<String,Double>();
		Map<String, Double> slaMetrics = Helper.getSlaMetrics(runner.getVmList());
		double slaAverage = slaMetrics.get("average");
		objectiveScores.put(AVERAGE_SLA, slaAverage);
		
		double slaOverall = slaMetrics.get("overall");
		objectiveScores.put(OVERALL_SLA, slaOverall);
		
		List<Double> timeBeforeHostShutdown = Helper.getTimesBeforeHostShutdown(datacenter.getHostList());
		
		double meanTimeBeforeHostShutdown = Double.NaN;
		if (!timeBeforeHostShutdown.isEmpty()) {
			meanTimeBeforeHostShutdown = MathUtil.mean(timeBeforeHostShutdown);
		}
		objectiveScores.put(MEAN_HOST_SHUTDOWN_TIME, meanTimeBeforeHostShutdown);
		
		double energy = datacenter.getPower() / (3600 * 1000);
		
		objectiveScores.put(ENERGY_CONSUMPTION, energy);
		
		System.out.println("sasdasdas: " + objectiveScores.get(ENERGY_CONSUMPTION) + ", " + 
				objectiveScores.get(AVERAGE_SLA) + ", " + objectiveScores.get(MEAN_HOST_SHUTDOWN_TIME) + ", " + objectiveScores.get(OVERALL_SLA));
	}

}
