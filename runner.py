from __future__ import division
import subprocess, os



ALLOCATION_POLICY = {
            "1": "iqr",
            "2": "lr",
            "3": "lrr",
            "4": "mad",
            "5": "thr"
         }

SELECTION_POLICY = {
            "1": "mc",
            "2": "mmt",
            "3": "mu",
            "4": "rs",
        }

VM_TYPES = 	{
    "1": [2500, 1, 870, 10000, 2500],
    "2": [2000, 1, 1740, 10000, 2500],
    "3": [1000, 1, 1740, 10000, 2500],
    "4": [500, 1, 613, 10000, 2500],
}

HOST_TYPES = {
    "1": [1860, 2, 4096, 1000000, 1000000],
    "2": [2660, 2, 4096, 1000000, 1000000],
    "3": [3000, 4, 8192, 1000000, 1000000],
    "4": [4000, 4, 8192, 1000000, 1000000],
}

PARAMETER = 1.393674679793466  # from the ase fall 2016 group || check what it means


def run_configuration(list_of_configurations):
    vm_allocation_policy = ALLOCATION_POLICY[str(list_of_configurations[0])]
    vm_selection_policy = SELECTION_POLICY[str(list_of_configurations[1])]

    number_of_hosts = str(list_of_configurations[2]) # hosts;
    number_of_vms = str(list_of_configurations[3])  #vms;

    # VM configurations: We only use 1 types of VM at a time
    vm_configuration = VM_TYPES[str(list_of_configurations[4])]
    vm_mips = str(vm_configuration[0])  #vmMips;
    vm_pes = str(vm_configuration[1])  #vmPes;
    vm_ram = str(vm_configuration[2])  #vmRam;
    vm_bw = str(vm_configuration[3])  #vmBm;
    # vm_types = vm_configuration[int(list_of_configurations[4])]  #vmTypes;

    host_configuration = HOST_TYPES[str(list_of_configurations[5])]
    host_mips = str(host_configuration[0])  #hostMips;
    host_pes = str(host_configuration[1])  #hostPes;
    host_ram = str(host_configuration[2])  #hostRam;
    host_bw = str(host_configuration[3])  #hostBw;
    host_storage = str(host_configuration[4])  #hostStorage;

    # host_types = list_of_configurations[5]  #hostType;

    command = "java -cp $CLASSPATH org/cloudbus/cloudsim/examples/power/random/ConfigRunner " \
              + vm_allocation_policy + " "\
              + vm_selection_policy + " "\
            + str(PARAMETER) + " "\
            + number_of_hosts + " "\
            + number_of_vms + " "\
            + vm_pes + " "\
            + vm_mips + " "\
            + vm_ram + " "\
            + host_pes + " "\
            + host_mips + " "\
            + host_ram + " "\
            + vm_bw + " "\
            + host_bw + " "\
            + host_storage

    working_directory = "/Users/viveknair/GIT/CloudSim/CloudSim_Project/modules/cloudsim-examples/src/main/java"
    cmd = command.split(" ")
    print command
    print cmd
    # os.chdir(working_directory)
    # print os.getcwd()
    # os.system(command)
    p = subprocess.Popen(command, stdout=subprocess.PIPE, shell=True, cwd=working_directory)
    p.wait()
    content  = [line for line in p.stdout]
    return map(float, content[-1].split(', '))

if __name__ == "__main__":
    print run_configuration([1, 1, 6, 12, 4, 4])
