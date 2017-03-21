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
    vm_bw = str(10000)  # vmBm;


    host_bw = str(1000000)  # hostBw;
    host_storage = str(1000000)  # hostStorage;

    vm1 = str(list_of_configurations[4])
    vm2 = str(list_of_configurations[5])
    vm3 = str(list_of_configurations[6])
    vm4 = str(list_of_configurations[7])

    h1 = str(list_of_configurations[8])
    h2 = str(list_of_configurations[9])
    h3 = str(list_of_configurations[10])
    h4 = str(list_of_configurations[11])

    command = "java -Dfile.encoding=UTF-8 " \
              "-classpath \"/Users/viveknair/GIT/CloudSim/CloudSim_Project/bin:" \
              "/Users/viveknair/Research/CloudSim/Software/commons-math3-3.6.1/commons-math3-3.6.1.jar:" \
              "/Users/viveknair/GIT/fss16dst/project/dependency/easymock-3.0 2.jar:" \
              "/Users/viveknair/Research/CloudSim/Software/eclipse/plugins/org.junit_4.11.0.v201303080030/junit.jar:" \
              "/Users/viveknair/Research/CloudSim/Software/eclipse/plugins/org.hamcrest.core_1.3.0.v201303031735.jar:" \
              "/Users/viveknair/GIT/fss16dst/project/dependency/opencsv-3.3.jar\" " \
              "org.cloudbus.cloudsim.examples.power.random.ConfigRunner " \
              + vm_allocation_policy + " "\
              + vm_selection_policy + " "\
            + str(PARAMETER) + " "\
            + number_of_hosts + " "\
            + number_of_vms + " "\
            + vm_bw + " "\
            + host_bw + " "\
            + host_storage + " "\
            + vm1 + " "\
            + vm2 + " "\
            + vm3 + " "\
            + vm4 + " "\
            + h1 + " "\
            + h2 + " "\
            + h3 + " "\
            + h4 + " 1000"
    working_directory = "/Users/viveknair/GIT/CloudSim/CloudSim_Project/modules/cloudsim-examples/src/main/java"
    cmd = command.split(" ")
    # os.chdir(working_directory)
    # print os.getcwd()
    # os.system(command)

    p = subprocess.Popen(command, stdout=subprocess.PIPE, shell=True, cwd=working_directory)
    p.wait()
    content  = [line for line in p.stdout]
    return ",".join(cmd[6:] + content[-1].strip().split(', ')) + "\n"


def generate_configuration():
    """
    Generates all the possible configurations and stores it in the configs.p
    :return: None
    """
    allocation_policy = [1, 2, 3, 4, 5]
    selection_policy = [1, 2, 3, 4]
    no_hosts = [10, 50, 100, 150, 200, 250, 300, 350, 400, 450, 500]
    no_vms = [20, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000]
    vm_ratios = [
                    [0.323264002,0.409607079,0.14932131,0.117807609],
                    [0.507671124,0.288071708,0.120649132,0.083608035],
                    [0.050779864,0.292805811,0.472547349,0.183866976],
                    [0.099931575,0.224263431,0.323292402,0.352512592],
                    [0.120833939,0.134369307,0.358838949,0.385957805],
                    [0.490870023,0.317750784,0.120088985,0.071290207],
                    [0.107870084,0.061679406,0.454722762,0.375727748],
                    [0.085475861,0.276779628,0.322546254,0.315198256],
                    [0.193587375,0.310726298,0.22921138,0.266474947],
                    [0.2388495,0.295165113,0.187180292,0.278805095]
                ]

    host_ratios = [
        [0.073431799,0.177636854,0.293712423,0.455218924,],
        [0.206599345,0.312553939,0.312954293,0.167892423,],
        [0.429334137,0.031561099,0.151716582,0.387388182,],
        [0.340412702,0.283611965,0.278806876,0.097168457,],
        [0.083296691,0.464511451,0.258612985,0.193578874,],
        [0.450919647,0.187491905,0.228430031,0.133158417,],
        [0.256385386,0.058100143,0.218023394,0.467491077,],
        [0.183359774,0.23989209,0.370877838,0.205870298,],
        [0.326057704,0.093860616,0.256401766,0.323679914,],
        [0.192278961,0.364019902,0.345422047,0.09827909,],
    ]




    collector_list = []
    for alloc_policy in allocation_policy:
        for select_policy in selection_policy:
                    for no_host in no_hosts:
                        for no_vm in no_vms:
                            if no_vm > no_host: continue
                            for vm_ratio in vm_ratios:
                                vm1 = int(vm_ratio[0] * no_vm)
                                vm2 = int(vm_ratio[1] * no_vm)
                                vm3 = int(vm_ratio[2] * no_vm)
                                vm4 = int(no_vm - vm1 - vm2 - vm3)
                                assert(vm4 > 0), "Something is wrong"
                                assert(vm1 + vm2 + vm3 + vm4 == no_vm), "Something is wrong"

                                for host_ratio in host_ratios:
                                    h1 = int(host_ratio[0] * no_host)
                                    h2 = int(host_ratio[1] * no_host)
                                    h3 = int(host_ratio[2] * no_host)
                                    h4 = int(no_host - h1 - h2 - h3)
                                    assert(h4 > 0), "Something is wrong"
                                    assert(h1 + h2 + h3 + h4 == no_host), "Something is wrong"

                                    collector_list.append([alloc_policy, select_policy, no_host, no_vm, vm1, vm2, vm3, vm4, h1, h2, h3, h4])

    import pickle
    pickle.dump(collector_list, open('Data_Mixed/configs.p', 'w'))
    print "Done ", len(collector_list)


def append_data(line, filename="Data_Mixed/big_spike_collector.txt"):
    """
    :param line: new line to be added
    :param filename: file where new line is appended
    :return: None
    """
    with open(filename, "a") as myfile: myfile.write(line)


def wrapper_run_configurations():
    import pickle
    configs = pickle.load(open('./Data_Mixed/configs.p', 'r'))
    for i, config in enumerate(configs):
        # try:
            print i, config
            return_line = run_configuration(config)

            if return_line.count(',') == 20:
                # valid configuration
                append_data(return_line)
            else:
                # invalid configuration
                append_data(return_line, "./Data_Mixed/invalid_collector.txt")
        # except:
        #     print config, " caused issues"
        #     continue

if __name__ == "__main__":
    # print run_configuration([1, 1, 6, 12, 4, 4])
    # generate_configuration()
    wrapper_run_configurations()
