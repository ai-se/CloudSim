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
    # os.chdir(working_directory)
    # print os.getcwd()
    # os.system(command)
    p = subprocess.Popen(command, stdout=subprocess.PIPE, shell=True, cwd=working_directory)
    p.wait()
    content  = [line for line in p.stdout]
    return ",".join(cmd[4:] + content[-1].strip().split(', ')) + "\n"


def generate_configuration():
    """
    Generates all the possible configurations and stores it in the configs.p
    :return: None
    """
    allocation_policy = [1, 2, 3, 4, 5]
    selection_policy = [1, 2, 3, 4]
    vm_types = [1, 2, 3, 4]
    host_types = [1, 2, 3, 4]
    no_hosts = [10, 20, 30, 40, 50, 60, 70, 80, 90, 100]
    no_vms = [20, 40, 60, 80, 100, 120, 140, 160, 180, 200]

    collector_list = []
    for alloc_policy in allocation_policy:
        for select_policy in selection_policy:
            for vm_type in vm_types:
                for host_type in host_types:
                    for no_host in no_hosts:
                        for no_vm in no_vms:
                            if no_vm > no_host: continue
                            collector_list.append([alloc_policy, select_policy, no_host, no_vm, vm_type, host_type])

    import pickle
    pickle.dump(collector_list, open('Data/configs.p', 'w'))
    print "Done ", len(collector_list)


def append_data(line, filename="Data/collector.txt"):
    """
    :param line: new line to be added
    :param filename: file where new line is appended
    :return: None
    """
    with open(filename, "a") as myfile: myfile.write(line)


def wrapper_run_configurations():
    import pickle
    configs = pickle.load(open('Data/configs.p', 'r'))
    for i, config in enumerate(configs):
        try:
            print i, config
            return_line = run_configuration(config)
            if return_line.count(',') == 16:
                # valid configuration
                append_data(return_line)
            else:
                # invalid configuration
                append_data(return_line, "./Data/invalid_collector.txt")
        except:
            print config, " caused issues"
            continue

if __name__ == "__main__":
    # print run_configuration([1, 1, 6, 12, 4, 4])
    # generate_configuration()
    wrapper_run_configurations()