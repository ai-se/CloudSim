# CloudSim
Project to collect CloudSim configurations and corresponding performance values

- add 
```
export CLASSPATH=${CLASSPATH}:/Users/viveknair/Research/CloudSim/Software/cloudsim-3.0.3/jars/*:/Users/viveknair/Research/CloudSim/Software/cloudsim-3.0.3/examples
```
to ~/.bash_rc

### How to extract the classpath from eclipse: 
- Run your application and go to your Debug perspective
- In the process viewer, there should be an entry for the app you've just executed
- Right-click the row that mentions java.exe or javaw.exe
- Select Properties
- In the dialog that pops up you'll see the Command Line which includes all classpath entries and arguments

Commandline:
java -Dfile.encoding=UTF-8 -classpath "/Users/viveknair/GIT/CloudSim/CloudSim_Project/bin:/Users/viveknair/Research/CloudSim/Software/commons-math3-3.6.1/commons-math3-3.6.1.jar:/Users/viveknair/GIT/fss16dst/project/dependency/easymock-3.0 2.jar:/Users/viveknair/Research/CloudSim/Software/eclipse/plugins/org.junit_4.11.0.v201303080030/junit.jar:/Users/viveknair/Research/CloudSim/Software/eclipse/plugins/org.hamcrest.core_1.3.0.v201303031735.jar:/Users/viveknair/GIT/fss16dst/project/dependency/opencsv-3.3.jar" org.cloudbus.cloudsim.examples.power.random.ConfigRunner iqr mc 1.39367467979 160 200 10000 1000000 1000000 200 0 0 0 160 0 0 0 1

java -Dfile.encoding=UTF-8 -classpath "/Users/viveknair/tmp/CloudSimVCL/CloudSim_Project/bin/":"/Users/viveknair/tmp/CloudSimVCL/dependency/easymock-3.0 2.jar":"~/tmp/CloudSimVCL/dependency/junit.jar":"~/tmp/CloudSimVCL/dependency/opencsv-3.3.jar":"~/tmp/CloudSimVCL/dependency/org.hamcrest.core_1.3.0.v201303031735.jar": org/cloudbus/cloudsim/examples/power/random/ConfigRunner iqr mc 1.39367467979 160 200 10000 1000000 1000000 200 0 0 0 160 0 0 0 1