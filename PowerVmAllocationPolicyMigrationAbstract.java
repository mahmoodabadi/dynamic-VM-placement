/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.power;
import org.cloudbus.cloudsim.examples.power.Constants;
import org.cloudbus.cloudsim.examples.power.Helper;
import org.cloudbus.cloudsim.examples.power.custom.CustomRunner;
import org.cloudbus.cloudsim.examples.power.custom.CustomRunnerAbstract;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.*;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.matching.blossom.v5.KolmogorovWeightedPerfectMatching;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.shape.mxImageShape;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.HostDynamicWorkload;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmSchedulerTimeSharedOverSubscription;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.power.lists.PowerVmList;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.util.ExecutionTimeMeasurer;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import Vector.Vectorbin;
import calavecchia.DemandRisk;
import chowdhury.MWFDVP;
import chowdhury.SWFDVP;
import guazzone.guazzoneBFD;
import lago.LagoAllocator;
import shi.AbsoluteCapacity;
import shi.PercentageUtil;
import vbproblem.src.LBMBP;
import vbproblem.src.VMMigration;
import vbproblem.src.biparti;
//import vbproblem.src.biparti;
import vbproblem.src.biparti2;
import vbproblem.src.vectorbin2;
/**
 * The class of an abstract power-aware VM allocation policy that dynamically optimizes the VM
 * allocation using migration.
 * 
 * If you are using any algorithms, policies or workload included in the power package, please cite
 * the following paper:
 * 
 * Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
 * Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
 * Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
 * Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012
 * 
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 3.0
 */
public abstract class PowerVmAllocationPolicyMigrationAbstract extends PowerVmAllocationPolicyAbstract {

	public static 	List<Map<String, Object>> migrationMapH = new LinkedList<Map<String, Object>>();
	public static 	List<Map<String, Object>> migrationMapAdd = new LinkedList<Map<String, Object>>();
	 public static List<Vm> vmsTag=new ArrayList<Vm>();
	public static int[] vmsTagArray;
		
	public static boolean tag=false;
	public 	List<Map<Vm, Integer>> old_hostForVm = new ArrayList<Map<Vm, Integer>>();
	//public Map<Vm, Host> old_hostForVm = new HashMap<Vm, Host>();
	public static List <Integer> nOfHostsInClocks=new ArrayList<Integer>();
	public static int NextFitInd=0;
	/** The vm selection policy. */
	public static PowerVmSelectionPolicy vmSelectionPolicy;

	/** The saved allocation. */
	private final List<Map<String, Object>> savedAllocation = new ArrayList<Map<String, Object>>();

	/** The utilization history. */
	private final Map<Integer, List<Double>> utilizationHistory = new HashMap<Integer, List<Double>>();

	/** The metric history. */
	private final Map<Integer, List<Double>> metricHistory = new HashMap<Integer, List<Double>>();

	/** The time history. */
	private final Map<Integer, List<Double>> timeHistory = new HashMap<Integer, List<Double>>();

	/** The execution time history vm selection. */
	private final List<Double> executionTimeHistoryVmSelection = new LinkedList<Double>();

	/** The execution time history host selection. */
	private final List<Double> executionTimeHistoryHostSelection = new LinkedList<Double>();

	/** The execution time history vm reallocation. */
	private final List<Double> executionTimeHistoryVmReallocation = new LinkedList<Double>();

	/** The execution time history total. */
	private final List<Double> executionTimeHistoryTotal = new LinkedList<Double>();

	public static List<Vm> vmList_items=new ArrayList<Vm>();
	public static List<Vm> vmListItemsDP=new ArrayList<Vm>();
	public static List<Vm> vmListItemsDP1=new ArrayList<Vm>();
	public static ArrayList<PowerHost> hostForNextFit=new ArrayList<PowerHost>();
    public static List<Double> TimeofVmMig=new ArrayList<Double>();

	public static boolean flag=false;
	static double totalItemSize=0;


	public static double itemsLimitSize=0;
	// public static PowerHost[] host;

	public static double sumOfItemSize=0;

	/**
	 * Instantiates a new power vm allocation policy migration abstract.
	 * 
	 * @param hostList the host list
	 * @param vmSelectionPolicy the vm selection policy
	 */
	public PowerVmAllocationPolicyMigrationAbstract(
			List<? extends Host> hostList,
			PowerVmSelectionPolicy vmSelectionPolicy) {
		super(hostList);
		setVmSelectionPolicy(vmSelectionPolicy);
	}


	public PowerVmAllocationPolicyMigrationAbstract(
			double[][] hostList1,
			PowerVmSelectionPolicy vmSelectionPolicy) {
		super(hostList1);
		setVmSelectionPolicy(vmSelectionPolicy);
	}



	public abstract boolean isHostOverUtilized(PowerHost host);


	/**
	 * Optimize allocation of the VMs according to current utilization.
	 * 
	 * @param vmList the vm list
	 * 
	 * @return the array list< hash map< string, object>>
	 */
	@Override
	public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> vmList) {
		ExecutionTimeMeasurer.start("optimizeAllocationTotal");

		ExecutionTimeMeasurer.start("optimizeAllocationHostSelection");

		List<PowerHostUtilizationHistory> overUtilizedHosts = getOverUtilizedHosts();
		getExecutionTimeHistoryHostSelection().add(
				ExecutionTimeMeasurer.end("optimizeAllocationHostSelection"));

		printOverUtilizedHosts(overUtilizedHosts);

		saveAllocation();

		ExecutionTimeMeasurer.start("optimizeAllocationVmSelection");


		if (CloudSim.clock()>=86000)
			System.out.println("STOP");
		
		
		List<? extends Vm> vmsToMigrate = getVmsToMigrateFromHosts(overUtilizedHosts);
		getExecutionTimeHistoryVmSelection().add(ExecutionTimeMeasurer.end("optimizeAllocationVmSelection"));

		Log.printLine("Reallocation of VMs from the over-utilized hosts:");
		ExecutionTimeMeasurer.start("optimizeAllocationVmReallocation");
		List<Map<String, Object>> migrationMap = getNewVmPlacement(vmsToMigrate, new HashSet<Host>(overUtilizedHosts));
		getExecutionTimeHistoryVmReallocation().add(
				ExecutionTimeMeasurer.end("optimizeAllocationVmReallocation"));
		Log.printLine();

		migrationMap.addAll(getMigrationMapFromUnderUtilizedHosts(overUtilizedHosts));

		restoreAllocation();

		getExecutionTimeHistoryTotal().add(ExecutionTimeMeasurer.end("optimizeAllocationTotal"));

		
		List<PowerHost> hostList1=CustomRunner.hostList1;

		int host_counts=0;
		//double sum_R=0;
		//for (int i=0;i<hostList1.size();i++)
		for (Host h:this.getHostList())
			if (h.getVmList().size()!=0) 
				host_counts++;

		nOfHostsInClocks.add(host_counts);
		
		return migrationMap;


	}



	protected List<PowerHostUtilizationHistory> getLowUtilizedHosts() {//该函数是新加的
		List<PowerHostUtilizationHistory> lowUtilizedHosts = new LinkedList<PowerHostUtilizationHistory>();
		for (PowerHostUtilizationHistory host : this.<PowerHostUtilizationHistory> getHostList()) {
			if (isHostLowUtilized(host)) {
				lowUtilizedHosts.add(host);
			}
		}
		return lowUtilizedHosts;
	}




	/**
	 * Gets the migration map from under utilized hosts.
	 * 
	 * @param overUtilizedHosts the over utilized hosts
	 * @return the migration map from under utilized hosts
	 */
	protected List<Map<String, Object>> getMigrationMapFromUnderUtilizedHosts(
			List<PowerHostUtilizationHistory> overUtilizedHosts) {
		List<Map<String, Object>> migrationMap = new LinkedList<Map<String, Object>>();
		List<PowerHost> switchedOffHosts = getSwitchedOffHosts();

		// over-utilized hosts + hosts that are selected to migrate VMs to from over-utilized hosts
		Set<PowerHost> excludedHostsForFindingUnderUtilizedHost = new HashSet<PowerHost>();
		excludedHostsForFindingUnderUtilizedHost.addAll(overUtilizedHosts);
		excludedHostsForFindingUnderUtilizedHost.addAll(switchedOffHosts);
		excludedHostsForFindingUnderUtilizedHost.addAll(extractHostListFromMigrationMap(migrationMap));

		// over-utilized + under-utilized hosts
		Set<PowerHost> excludedHostsForFindingNewVmPlacement = new HashSet<PowerHost>();
		excludedHostsForFindingNewVmPlacement.addAll(overUtilizedHosts);
		excludedHostsForFindingNewVmPlacement.addAll(switchedOffHosts);

		int numberOfHosts = getHostList().size();

		while (true) {
			if (numberOfHosts == excludedHostsForFindingUnderUtilizedHost.size()) {
				break;
			}

			PowerHost underUtilizedHost = getUnderUtilizedHost(excludedHostsForFindingUnderUtilizedHost);
			if (underUtilizedHost == null) {
				break;
			}

			Log.printLine("Under-utilized host: host #" + underUtilizedHost.getId() + "\n");

			excludedHostsForFindingUnderUtilizedHost.add(underUtilizedHost);
			excludedHostsForFindingNewVmPlacement.add(underUtilizedHost);

			List<? extends Vm> vmsToMigrateFromUnderUtilizedHost = getVmsToMigrateFromUnderUtilizedHost(underUtilizedHost);
			if (vmsToMigrateFromUnderUtilizedHost.isEmpty()) {
				continue;
			}

			Log.print("Reallocation of VMs from the under-utilized host: ");
			if (!Log.isDisabled()) {
				for (Vm vm : vmsToMigrateFromUnderUtilizedHost) {
					Log.print(vm.getId() + " ");
				}
			}
			Log.printLine();

			List<Map<String, Object>> newVmPlacement = getNewVmPlacementFromUnderUtilizedHost(
					vmsToMigrateFromUnderUtilizedHost,
					excludedHostsForFindingNewVmPlacement);

			excludedHostsForFindingUnderUtilizedHost.addAll(extractHostListFromMigrationMap(newVmPlacement));

			migrationMap.addAll(newVmPlacement);
			Log.printLine();
		}

		return migrationMap;
	}
	
	
	
	protected List<Map<String, Object>> getMigrationMapFromUnderUtilizedHosts1(
			List<PowerHostUtilizationHistory> overUtilizedHosts) {
		List<Map<String, Object>> migrationMap = new LinkedList<Map<String, Object>>();
		List<PowerHost> switchedOffHosts = getSwitchedOffHosts();

		// over-utilized hosts + hosts that are selected to migrate VMs to from over-utilized hosts
		Set<PowerHost> excludedHostsForFindingUnderUtilizedHost = new HashSet<PowerHost>();
		excludedHostsForFindingUnderUtilizedHost.addAll(overUtilizedHosts);
		excludedHostsForFindingUnderUtilizedHost.addAll(switchedOffHosts);
		excludedHostsForFindingUnderUtilizedHost.addAll(extractHostListFromMigrationMap(migrationMap));

		// over-utilized + under-utilized hosts
		Set<PowerHost> excludedHostsForFindingNewVmPlacement = new HashSet<PowerHost>();
		excludedHostsForFindingNewVmPlacement.addAll(overUtilizedHosts);
		excludedHostsForFindingNewVmPlacement.addAll(switchedOffHosts);

		int numberOfHosts = getHostList().size();

		while (true) {
			if (numberOfHosts == excludedHostsForFindingUnderUtilizedHost.size()) {
				break;
			}

			PowerHost underUtilizedHost = getUnderUtilizedHost(excludedHostsForFindingUnderUtilizedHost);
			if (underUtilizedHost == null) {
				break;
			}

			Log.printLine("Under-utilized host: host #" + underUtilizedHost.getId() + "\n");

			excludedHostsForFindingUnderUtilizedHost.add(underUtilizedHost);
			excludedHostsForFindingNewVmPlacement.add(underUtilizedHost);

			List<? extends Vm> vmsToMigrateFromUnderUtilizedHost = getVmsToMigrateFromUnderUtilizedHost(underUtilizedHost);
			if (vmsToMigrateFromUnderUtilizedHost.isEmpty()) {
				continue;
			}

			Log.print("Reallocation of VMs from the under-utilized host: ");
			if (!Log.isDisabled()) {
				for (Vm vm : vmsToMigrateFromUnderUtilizedHost) {
					Log.print(vm.getId() + " ");
				}
			}
			Log.printLine();

			List<Map<String, Object>> newVmPlacement = getNewVmPlacementFromUnderUtilizedHost(
					vmsToMigrateFromUnderUtilizedHost,
					excludedHostsForFindingNewVmPlacement);

			excludedHostsForFindingUnderUtilizedHost.addAll(extractHostListFromMigrationMap(newVmPlacement));

			migrationMap.addAll(newVmPlacement);
			Log.printLine();
		}

		return migrationMap;
	}

	/**
	 * Prints the over utilized hosts.
	 * 
	 * @param overUtilizedHosts the over utilized hosts
	 */
	protected void printOverUtilizedHosts(List<PowerHostUtilizationHistory> overUtilizedHosts) {
		if (!Log.isDisabled()) {
			Log.printLine("Over-utilized hosts:");
			for (PowerHostUtilizationHistory host : overUtilizedHosts) {
				Log.printLine("Host #" + host.getId());
				Log.printLine("VMs for Host #" + host.getId());
				for (Vm v:host.getVmList())
					Log.printLine(v.getId());
			}
			Log.printLine();
		}
	}

	/**
	 * Find host for vm.
	 * 
	 * @param vm the vm
	 * @param excludedHosts the excluded hosts
	 * @return the power host
	 */


	public boolean isHostOverUtilizedAfterAllocation(PowerHost host, Vm vm) {
		boolean isHostOverUtilizedAfterAllocation = true;
		if (host.vmCreate(vm)) {
			isHostOverUtilizedAfterAllocation = isHostOverUtilized(host);
			host.vmDestroy(vm);
		}
		return isHostOverUtilizedAfterAllocation;
	}

	public PowerHost findHostForVm(Vm vm, Set<? extends Host> excludedHosts) {
		double minPower = Double.MAX_VALUE;
		PowerHost allocatedHost = null;

		for (PowerHost host : this.<PowerHost> getHostList()) {
			if (excludedHosts.contains(host)) {
				continue;
			}
			if (host.isSuitableForVm1(vm)) {
				if (getUtilizationOfCpuMips(host) != 0 && isHostOverUtilizedAfterAllocation(host, vm)) {
					continue;
				}

				try {
					double powerAfterAllocation = getPowerAfterAllocation(host, vm);
					if (powerAfterAllocation != -1) {
						double powerDiff = powerAfterAllocation - host.getPower(); 
						if (powerDiff < minPower) {
							minPower = powerDiff;
							allocatedHost = host;
						}
					}
				} catch (Exception e) {
				}
			}
		}
		hostForNextFit.add(allocatedHost);

		return allocatedHost;
	}








	public PowerHost findHostForVm1(Vm vm, Set<? extends Host> excludedHosts) {

		PowerHost allocatedHost = null;
		PowerHost host1= vbproblem.src.vectorbin2.hostList1.get(vbproblem.src.vectorbin2.hostForvms[vm.getId()]);
		//PowerHost host1= vbproblem.src.GRVBP.hostList1.get(vbproblem.src.GRVBP.hostForvms[vm.getId()]);
		//PowerHost host1= vbproblem.src.KMeans.hostList1.get(vbproblem.src.KMeans.hostForvms[vm.getId()]);



		double powerAfterAllocation = getPowerAfterAllocation(host1, vm);
		double powerDiff = powerAfterAllocation - host1.getPower(); 
		double minPower = powerDiff;
		allocatedHost=host1;

		for (PowerHost host : this.<PowerHost> getHostList()) {
			if (excludedHosts.contains(host)) {
				continue;
			}
			if (host.isSuitableForVm(vm)) {
				if (getUtilizationOfCpuMips(host) != 0 && isHostOverUtilizedAfterAllocation(host, vm)) {
					continue;
				}

				try {
					powerAfterAllocation = getPowerAfterAllocation(host, vm);
					if (powerAfterAllocation != -1) {
						powerDiff = powerAfterAllocation - host.getPower(); 
						if (powerDiff < minPower) {
							minPower = powerDiff;
							allocatedHost = host;
						}
					}
				} catch (Exception e) {
				}
			}
		}
		return allocatedHost;
	}


	/**
	 * Checks if is host over utilized after allocation.
	 * 
	 * @param host the host
	 * @param vm the vm
	 * @return true, if is host over utilized after allocation
	 */



	/**
	 * Find host for vm.
	 * 
	 * @param vm the vm
	 * @return the power host
	 */
	@Override
	public PowerHost findHostForVm(Vm vm) {
		Set<Host> excludedHosts = new HashSet<Host>();
		if (vm.getHost() != null) {
			excludedHosts.add(vm.getHost());
		}
		return findHostForVm(vm, excludedHosts);
	}

	/**
	 * Extract host list from migration map.
	 * 
	 * @param migrationMap the migration map
	 * @return the list
	 */
	protected List<PowerHost> extractHostListFromMigrationMap(List<Map<String, Object>> migrationMap) {
		List<PowerHost> hosts = new LinkedList<PowerHost>();
		for (Map<String, Object> map : migrationMap) {
			hosts.add((PowerHost) map.get("host"));
		}
		return hosts;
	}

	/**
	 * Gets the new vm placement.
	 * 
	 * @param vmsToMigrate the vms to migrate
	 * @param excludedHosts the excluded hosts
	 * @return the new vm placement
	 */
	protected List<Map<String, Object>> getNewVmPlacement(
			List<? extends Vm> vmsToMigrate,
			Set<? extends Host> excludedHosts) {
		List<Map<String, Object>> migrationMap = new LinkedList<Map<String, Object>>();
		PowerVmList.sortByCpuUtilization(vmsToMigrate);
		for (Vm vm : vmsToMigrate) {
			
			PowerHost allocatedHost =NextFit(excludedHosts, vm);
			if (allocatedHost==null && NextFitInd==getHostList().size()-1) {
				NextFitInd=0;
				allocatedHost =NextFit(excludedHosts, vm);
			}	
		    //PowerHost allocatedHost =findHostForVm(vm, excludedHosts);
			if (allocatedHost != null) {
				allocatedHost.vmCreate(vm);
				//vm.setHost(allocatedHost);
				Log.printLine("VM #" + vm.getId() + " allocated to host #" + allocatedHost.getId());

				Map<String, Object> migrate = new HashMap<String, Object>();
				migrate.put("vm", vm);
				migrate.put("host", allocatedHost);
				migrationMap.add(migrate);
				double migCost=0;
				double[][] he= {{86,89.4,92.6,96,99.5,102,106,108,112,114,117},{93.7, 97.0, 101.0, 105.0, 110.0, 116.0, 121.0, 125.0, 129.0, 133.0, 135.0}};
				double kc=0;double P_max1=0;double P_max2=0;
				if (allocatedHost.getId()%2==0) {
					kc=he[0][0]/he[0][10];
					P_max1=he[0][10];
				}
				else {
					kc=he[1][0]/he[1][10];
					P_max1=he[1][10];

				}

				int index=-1;


				for (int i = 0; i < old_hostForVm.size(); ++i) {
					if (old_hostForVm.get(i).containsKey(vm)) {
						index= old_hostForVm.get(i).get(vm);
						break;
					}
				}


				if (index%2==0) {
					kc=he[0][0]/he[0][10];
					P_max2=he[0][10];
				}
				else {
					kc=he[1][0]/he[1][10];
					P_max2=he[1][10];

				}




				migCost=vm.getCurrentAllocatedRam()*6+(1-kc)*P_max1*0.01+(1-kc)*P_max2*0.01;
				TimeofVmMig.add(vm.getRam()/((double)allocatedHost.getBw()/ (2 * 8000)));
				vectorbin2.migrationCost.add(migCost);
			}
			
			
			vmList_items.add(vm);

			double[] I=vectorbin2.normalize_New_Vm(vm);
//			int w1=0;
//			for (int i=0;i<I.length;i++) {
//			   		w1+=I[i];}
//				w1=w1/3;
//				totalItemSize+=w1;
//				
			double w=0;
			int d=vectorbin2.d;
			for (int i=0;i<d;i++)
				w+=vectorbin2.I_new[i];
			w=w/d;
			sumOfItemSize+=w;
			totalItemSize+=w;
			itemsLimitSize= vectorbin2.epsilon*totalItemSize;
//			if (itemsLimitSize>3.5*sumOfItemSize)
//				itemsLimitSize= 0.4*vectorbin2.epsilon*totalItemSize;
			if (sumOfItemSize>itemsLimitSize || CloudSim.clock()>=86000) {// || vm_indexForVMList+1==1052)
				OfflineAlg();
//
			}
			
	}
		
		
		Iterator itr = migrationMapH.iterator(); 
		Iterator itrMig=migrationMap.iterator();
        while (itr.hasNext()) { 
        	Map<String, Object> m1=(Map<String, Object>) itr.next();
        	while (itrMig.hasNext()) { 
        		Map<String, Object> m2=(Map<String, Object>) itrMig.next();
        		if (m1.get("vm").equals(m2.get("vm")) && m1.get("host").equals(m2.get("host")))   
        			itrMig.remove();
                    break;
        	}
        	
        	}
           
        for (int i=0;i<migrationMapAdd.size();i++)
        	migrationMap.add(migrationMapAdd.get(i));
        
        
        
		//migrationMapAdd.clear();
		migrationMapH.clear();
		return migrationMap;

	}

	public PowerHost NextFit(Set<? extends Host> excludedHosts,Vm vm) {
		List<PowerHost> lph = this.<PowerHost> getHostList();
		PowerHost allocatedHost=null;
		for (int i=NextFitInd;i<lph.size();i++) {
			if (excludedHosts.contains(lph.get(i))) {
				continue;
			}
			if (lph.get(i).isSuitableForVm1(vm)) {
				if (getUtilizationOfCpuMips(lph.get(i)) != 0 && isHostOverUtilizedAfterAllocation(lph.get(i), vm)) {
					continue;
				}
				
				NextFitInd=i;
				allocatedHost=lph.get(i);
				hostForNextFit.add(allocatedHost);
				break;
			}
		}
	
		
		
		return allocatedHost;
	}

	public void OfflineAlg(){
		sumOfItemSize=0;
		//totalItemSize=0;
		//	
		
		for (Vm vm: vectorbin2.vmList1) {
		
		int ID = vm.getId();
		int VM_MIPS = (int) vm.getMips();
		int VM_PES = vm.getNumberOfPes();
		int VM_RAM = vm.getRam();
		int VM_BW = (int) vm.getBw();
		int userId = vm.getUserId();
		int VM_SIZE=(int) vm.getSize();


		vmListItemsDP.add(new PowerVm(
				ID,
				userId,
				VM_MIPS,
				VM_PES,
				VM_RAM,
				VM_BW,
				VM_SIZE,
				1,
				"Xen",
				new CloudletSchedulerDynamicWorkload(VM_MIPS, VM_PES),
				Constants.SCHEDULING_INTERVAL));
		}
//		for (Vm v:vmListItemsDP)
//			vmListItemsDP1.add(v);
		
		
		//
	//	vectorbin2.hostList1_copy.clear();
		vectorbin2.hostList1_copy.clear();
		//if (!flag) {
		for (Host host : this.getHostList()) {

				PowerHost  h=(PowerHost) host;
				int HOST_RAM=h.getRam();
				long HOST_BW=h.getBw();
				//List<Vm> vml=h.getVmList();
				List<Pe> peList = new ArrayList<Pe>();
				for (int j = 0; j < h.getNumberOfPes(); j++) {
					peList.add(new Pe(j, new PeProvisionerSimple(h.getTotalMips())));
				}
				PowerHost newh = new PowerHost(h.getId(), new RamProvisionerSimple(HOST_RAM), new BwProvisionerSimple(HOST_BW), h.getStorage(),
						peList, new VmSchedulerTimeSharedOverSubscription(peList), h.getPowerModel()
						); 
				newh.setDatacenter(h.getDatacenter());
//				for (Vm v:vml) {
//					if (vmListItemsDP1.contains(v))
//						continue;
//					newh.vmCreate(v);
//				}
				vectorbin2.hostList1_copy.add(newh);
				
				////  
			}
		

		//vectorbin2.vmList1_copy = new ArrayList<Vm>(vectorbin2.vmList1);
		vectorbin2.vmList_copy=new double[vectorbin2.vmList1.size()][1];

		vectorbin2.vmList1_copy = new ArrayList<Vm>(vmListItemsDP) ;
		
		vectorbin2.vmList_copy=new double[vmListItemsDP.size()][3];
		//vmListItemsDP1=vectorbin2.vmList1_copy;
		Collections.sort(vectorbin2.vmList1_copy, new Comparator<Vm>() {
			@Override
			public int compare(Vm vm1, Vm vm2) {
				return Double.compare(vm2.getMips(), vm1.getMips());
			}
		});

		vectorbin2.normalize_Vm_List(vmListItemsDP);
		

		vectorbin2.NewVmEntrance();
		
		vectorbin2.hostForDP.clear();
		Loop:
		for (Vm v:vmList_items)
		 for (PowerHost h:vectorbin2.hostList1_copy)
			 if (h.getVmList().contains(v)) {
				 vectorbin2.hostForDP.add(h);
				 continue Loop;
				 
			 }
		
		similarHosts();
		
		if (CloudSim.clock()>86100)
		
		
		
		
		
//		this.hostList.clear();
//	//	this.hostList.addAll((Collection<?>) vectorbin2.hostList1_copy);
//		for (int i1=0;i1<vectorbin2.hostList1_copy.size();i1++) {
//			Host  h= vectorbin2.hostList1_copy.get(i1);
//				int HOST_RAM=h.getRam();
//				long HOST_BW=h.getBw();
//				List<Vm> vml=h.getVmList();
//				List<Pe> peList = new ArrayList<Pe>();
//				for (int j = 0; j < h.getNumberOfPes(); j++) {
//					peList.add(new Pe(j, new PeProvisionerSimple(h.getTotalMips())));
//				}
//				Host newh= new Host(h.getId(), new RamProvisionerSimple(HOST_RAM), new BwProvisionerSimple(HOST_BW), h.getStorage(),
//						peList, new VmSchedulerTimeSharedOverSubscription(peList));
//				
//				newh.setDatacenter(h.getDatacenter());
//				for (Vm v:vml) {
//					newh.vmCreate(v);
//				}
//				Host e=newh;
//				this.getHostList().add(e);
//				
//			}
//			
		//List<PowerHost> lph = this.<PowerHost> getHostList();

		//this.getHostList().=(List<? extends Host>)vectorbin2.hostList1_copy;
		hostForNextFit.clear();
		vectorbin2.hostForDP.clear();
		vectorbin2.vmListItemsDP1.clear();
		vectorbin2.vmForNextFit.clear();
		vmList_items.clear();
		vectorbin2.onlineVms.clear();

		vmListItemsDP.clear();
		vmListItemsDP1.clear();
		//		
	}

	public void similarHosts() {

        // Two lists of hosts
        List<PowerHost> hostList1 = new ArrayList<>();
        List<PowerHost> hostList2 = new ArrayList<>();
		List<PowerHost> lph = this.<PowerHost> getHostList();

//        hostList1= hostForNextFit;
//        hostList2=vectorbin2.hostForDP;
		for (PowerHost h:lph)
			if (h.getVmList().size()!=0)
				hostList1.add(h);
       // hostList1=lph;
		
		for (PowerHost h:vectorbin2.hostList1_copy)
			if (h.getVmList().size()!=0)
			  hostList2.add(h);
        //hostList2=vectorbin2.hostList1_copy;
        
        
        computeMaxWeightMatching(hostList1, hostList2);
        
      //  VMMigration.chooseMigrationTarget(hostList1, hostList2);
        //return migrationMap;

    }

	
	
	public static void computeMaxWeightMatching(List<PowerHost> hostList, List<PowerHost> hostList1) {
        // Create the bipartite graph
        Graph<Host, DefaultWeightedEdge> bipartiteGraph = createBipartiteGraph(hostList, hostList1);

        biparti.visualizeBipartiteGraph(bipartiteGraph);
        
        // Compute the maximum weight matching
        //KolmogorovWeightedPerfectMatching<Host, DefaultWeightedEdge> matching = new KolmogorovWeightedPerfectMatching<Host, DefaultWeightedEdge>(bipartiteGraph);
        Map<Host, Host> matchedHosts1 =getMaxWeightedEdges(hostList,hostList1,bipartiteGraph);
          
        	
        
        
        System.out.println("This is the correct output");
        // Print the matching
        for (Map.Entry<Host, Host> entry : matchedHosts1.entrySet()) {
            System.out.println("Host " + entry.getKey().getId() + " is matched with Host " + entry.getValue().getId());
        }
        
        
        migrateProcess(hostList,hostList1);
    }

	  public static void migrateProcess(List<PowerHost> hostList, List<PowerHost> hostList1) {

	        int i=0;
	        while (i<vmsTagArray.length) {
	        	Host h1=null;
	        	Host h2=null;
	        	if (vmsTagArray[i]==0) {
	        		Vm v=vmsTag.get(i);
	        			        		
	        			L1:
	        		       	for (Host h:hostList)
	        		       		for (Vm v1:h.getVmList())
	        		       			if (v1.getId()==v.getId()) {
	        		       			  h1=h;
	        		       			  break L1;
	        		       			}
	        			L2:
	        		       	for (Host h:hostList1)
	        		       		for (Vm v1:h.getVmList())
	        		       			if (v1.getId()==v.getId()) {
	        		       			  h2=h;
	        		       			  break L2;
	        		       			}
	        			
	        			if (h2==null)
	        				System.out.print("HI");
	        			if (h2.isSuitableForVm(v))
	        			 {
	        			  h1.vmDestroy(v);
	        		      h2.vmCreate(v);
	        		      
	        	 		  Map<String, Object> migrate = new HashMap<String, Object>();
	        	          migrate.put("vm", v);
	        			  migrate.put("host", h1);
	        			  PowerVmAllocationPolicyMigrationAbstract.migrationMapH.add(migrate);	
	        			  
	        			  migrate = new HashMap<String, Object>();
	        	          migrate.put("vm", v);
	        			  migrate.put("host", h2);
	        			  PowerVmAllocationPolicyMigrationAbstract.migrationMapAdd.add(migrate);
	        				
	        			  PowerVmAllocationPolicyAbstract.getVmTable().put(v.getUid(), h2);
	        		      System.out.println( v.getId() + " is being migrated from Host "+ h1.getId() + " to "+  h2.getId());

	        		}
	        		  		
	        	}
	        	
	        	i++;
	        
	    }
		}
	
	public static Graph<Host, DefaultWeightedEdge> createBipartiteGraph(List<PowerHost> hostList, List<PowerHost> hostList1) {
        Graph<Host, DefaultWeightedEdge> bipartiteGraph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        // Add vertices
        for (Host host : hostList) {
            bipartiteGraph.addVertex(host);
        }
        for (Host host : hostList1) {
            bipartiteGraph.addVertex(host);
        }

        for (Host host : hostList)
           for (Vm vm1 : host.getVmList())
           	vmsTag.add(vm1);
    
        
        Collections.sort(vmsTag, new Comparator<Vm>() {
    @Override
    public int compare(Vm o1, Vm o2) {
        return Integer.compare(o1.getId(), o2.getId());
    }
    });

        
vmsTagArray=new int[vmsTag.size()];   	        
        
        
        // Add edges
        for (Host host1 : hostList) {
            for (Host host2 : hostList1) {
                int commonVmSize = getCommonVmSize(host1, host2);
                if (commonVmSize > 0) {
                    DefaultWeightedEdge edge = bipartiteGraph.addEdge(host1, host2);
                    bipartiteGraph.setEdgeWeight(edge, commonVmSize);
                }
            }
        }

        return bipartiteGraph;
    }

    private static int getCommonVmSize(Host host1, Host host2) {
        int commonVmSize = 0;
        
        
        
        
        for (Vm vm1 : host1.getVmList()) {
            for (Vm vm2 : host2.getVmList()) {
                if (vm1.getId()==vm2.getId()) {
                	Cloudlet cl=CustomRunner.cloudletList.get(vm1.getId());
    				double cpu=0;//= cl.getUtilizationOfCpu(CloudSim.clock());
    				cpu=cl.getUtilizationOfCpu(CloudSim.clock())*vm1.getMips();
                    commonVmSize += cpu;
                         }
                	//commonVmSize++;
                }
            
        }
        
        
        
        return commonVmSize;
    }
    
    
    public static Map<Host, Host> getMaxWeightedEdges(List<PowerHost> hostlist11, List<PowerHost> hostList2, Graph<Host, DefaultWeightedEdge> bipartiteGraph) {
    	Map<Host, Host> matchedHosts = new HashMap<>();

        // Iterate through the edges and find the maximum weight matching
        Set<DefaultWeightedEdge> usedEdges = new HashSet<>();
        int i=0;
        L1:
        while (i < Math.min(hostlist11.size(), hostList2.size())) {
            // Find the edge with the maximum weight
        	if (i==19)
        		System.out.print("hi");
            DefaultWeightedEdge maxWeightEdge = null;
            double maxWeight = 0;
            for (DefaultWeightedEdge edge : bipartiteGraph.edgeSet()) {
                if (!usedEdges.contains(edge)) {
                    double weight = bipartiteGraph.getEdgeWeight(edge);
                    if (weight > maxWeight) {
                        maxWeight = weight;
                        maxWeightEdge = edge;
                    }
                }
            }

            // Add the maximum weight edge to the matching
            if (maxWeightEdge==null)
            	break L1;
            
            
            Host host1 = bipartiteGraph.getEdgeSource(maxWeightEdge);
            Host host2 = bipartiteGraph.getEdgeTarget(maxWeightEdge);
            matchedHosts.put(host1, host2);
            for (Vm v1:host1.getVmList())
            	for (Vm v2:host2.getVmList())
            	 if (v1.getId()==v2.getId()) {	
            		 for(int i1=0;i1<vmsTag.size();i1++) 
            			 if (vmsTag.get(i1).getId()==v1.getId()){
            				 vmsTagArray[i1]=1;
            				 break;
            	            }
               }
           // matchedHosts.put(host2, host1);
            usedEdges.add(maxWeightEdge);

            // Remove all other edges connected to the matched hosts
            for (DefaultWeightedEdge edge : bipartiteGraph.edgeSet()) {
                if (bipartiteGraph.getEdgeSource(edge).equals(host1) || bipartiteGraph.getEdgeTarget(edge).equals(host1) ||
                    bipartiteGraph.getEdgeSource(edge).equals(host2) || bipartiteGraph.getEdgeTarget(edge).equals(host2)) {
            	// if (bipartiteGraph.getEdgeSource(edge).equals(host1) || bipartiteGraph.getEdgeTarget(edge).equals(host2)) {
            		 //bipartiteGraph.edgeSet().remove(edge);
            		 usedEdges.add(edge);
                }
            }
            i++;
        }
        return matchedHosts;
    }
	
	
	
	
	
	
	/**
	 * Gets the new vm placement from under utilized host.
	 * 
	 * @param vmsToMigrate the vms to migrate
	 * @param excludedHosts the excluded hosts
	 * @return the new vm placement from under utilized host
	 */
	protected List<Map<String, Object>> getNewVmPlacementFromUnderUtilizedHost(
			List<? extends Vm> vmsToMigrate,
			Set<? extends Host> excludedHosts) {
		List<Map<String, Object>> migrationMap = new LinkedList<Map<String, Object>>();
		PowerVmList.sortByCpuUtilization(vmsToMigrate);

		for (Vm vm : vmsToMigrate) {
			
			//PowerHost allocatedHost=findHostForVm(vm, excludedHosts);
			
			
			PowerHost allocatedHost=NextFit(excludedHosts,vm);
			if (allocatedHost==null && NextFitInd==getHostList().size()-1) {
				NextFitInd=0;
				allocatedHost =NextFit(excludedHosts, vm);
			}	
			if (allocatedHost != null) {
				//vm.getHost().vmDestroy(vm);
				allocatedHost.vmCreate(vm);
				Log.printLine("VM #" + vm.getId() + " allocated to host #" + allocatedHost.getId());

				Map<String, Object> migrate = new HashMap<String, Object>();
				migrate.put("vm", vm);
				migrate.put("host", allocatedHost);
				migrationMap.add(migrate);


				double migCost=0;
				double[][] he= {{86,89.4,92.6,96,99.5,102,106,108,112,114,117},{93.7, 97.0, 101.0, 105.0, 110.0, 116.0, 121.0, 125.0, 129.0, 133.0, 135.0}};
				double kc=0;double P_max1=0;double P_max2=0;
				if (allocatedHost.getId()%2==0) {
					kc=he[0][0]/he[0][10];
					P_max1=he[0][10];
				}
				else {
					kc=he[1][0]/he[1][10];
					P_max1=he[1][10];

				}

				int index=-1;


				for (int i = 0; i < old_hostForVm.size(); ++i) {
					if (old_hostForVm.get(i).containsKey(vm)) {
						index= old_hostForVm.get(i).get(vm);
						break;
					}
				}


				if (index%2==0) {
					kc=he[0][0]/he[0][10];
					P_max2=he[0][10];
				}
				else {
					kc=he[1][0]/he[1][10];
					P_max2=he[1][10];

				}




				migCost=vm.getCurrentAllocatedRam()*6+(1-kc)*P_max1*0.01+(1-kc)*P_max2*0.01;
				vectorbin2.migrationCost.add(migCost);
				TimeofVmMig.add(vm.getRam()/((double)allocatedHost.getBw()/ (2 * 8000)));
			

				vmList_items.add(vm);
				
				
				double[] I=vectorbin2.normalize_New_Vm(vm);
				int w1=0;
//				for (int i=0;i<I.length;i++) {
//				   		w1+=I[i];}
//					w1=w1/3;
//					totalItemSize+=w1;
					
				double w=0;
				int d=vectorbin2.d;
				for (int i=0;i<d;i++)
					w+=vectorbin2.I_new[i];
				w=w/d;
				sumOfItemSize+=w;
				totalItemSize+=w;
				//itemsLimitSize= 1.1*vectorbin2.epsilon*totalItemSize;
				itemsLimitSize= vectorbin2.epsilon*totalItemSize;

//				
//				if (itemsLimitSize>3.5*sumOfItemSize )
//						itemsLimitSize= 0.4*vectorbin2.epsilon*totalItemSize;
				if (sumOfItemSize>itemsLimitSize || CloudSim.clock()>=86100) {// || vm_indexForVMList+1==1052)
				   OfflineAlg();
////
				}

//				}
//////				

			}

			 else {
				Log.printLine("Not all VMs can be reallocated from the host, reallocation cancelled");
				for (Map<String, Object> map : migrationMap) {
					((Host) map.get("host")).vmDestroy((Vm) map.get("vm"));
				}
				migrationMap.clear();
				//migrationMapAdd.clear();
				migrationMapH.clear();
				break;
			}
		
			
		}
		
		Iterator itr = migrationMapH.iterator(); 
		Iterator itrMig=migrationMap.iterator();
        while (itr.hasNext()) { 
        	Map<String, Object> m1=(Map<String, Object>) itr.next();
        	while (itrMig.hasNext()) { 
        		Map<String, Object> m2=(Map<String, Object>) itrMig.next();
        		if (m1.get("vm").equals(m2.get("vm")) && m1.get("host").equals(m2.get("host")))   
        			//migrationMap.remove(m2);
        			itrMig.remove();
                    break;
        	}
        	
        	}
		
        
        
        for (int i=0;i<migrationMapAdd.size();i++)
        	migrationMap.add(migrationMapAdd.get(i));
        
        
        
	//	migrationMapAdd.clear();
        
        
        migrationMapH.clear();
		return migrationMap;
	}

	/**
	 * Gets the vms to migrate from hosts.
	 * 
	 * @param overUtilizedHosts the over utilized hosts
	 * @return the vms to migrate from hosts
	 */
	protected List<? extends Vm> getVmsToMigrateFromHosts(List<PowerHostUtilizationHistory> overUtilizedHosts) {
		List<Vm> vmsToMigrate = new LinkedList<Vm>();
		old_hostForVm.clear();
		for (PowerHostUtilizationHistory host : overUtilizedHosts) {
			while (true) {
				Vm vm = getVmSelectionPolicy().getVmToMigrate(host);
//				if (vm.getId()==48)
//					continue;
				if (vm==null)
					break;
				vmsToMigrate.add(vm);
				HashMap<Vm,Integer> mig=new HashMap<Vm,Integer>();
				
				if (vm.getHost()==null) {
					vm.setInMigration(true);;
					System.out.print("STOP");
					continue;
				}
				mig.put(vm, vm.getHost().getId());
				old_hostForVm.add(mig);
				//old_hostForVm.add(migrate);
//				if (vm.getId()==245)
//					System.out.print("STOPE");
				host.vmDestroy(vm);
				if (!isHostOverUtilized(host)) {
					break;
				}
			}
		}
		return vmsToMigrate;
	}

	/**
	 * Gets the vms to migrate from under utilized host.
	 * 
	 * @param host the host
	 * @return the vms to migrate from under utilized host
	 */
	protected List<? extends Vm> getVmsToMigrateFromUnderUtilizedHost(PowerHost host) {
		List<Vm> vmsToMigrate = new LinkedList<Vm>();
		for (Vm vm : host.getVmList()) {
			//			if (vm.getId()==0)
			//				continue;
			if (!vm.isInMigration()) {
//				if (vm.getId()==245 && CloudSim.clock()>60000)
//					System.out.print(245);;
				vmsToMigrate.add(vm);
				HashMap<Vm,Integer> mig=new HashMap<Vm,Integer>();
				if (vm.getHost()==null)
					vm.setHost(host);
				mig.put(vm, vm.getHost().getId());
				//host.vmDestroy(vm);
				old_hostForVm.add(mig);	
			}
		}
		return vmsToMigrate;
	}



	protected	List<? extends Vm>	getVmsToMigrateFromLowHosts(List<PowerHostUtilizationHistory> lowUtilizedHosts) {
		List<Vm> vmsToMigrate = new LinkedList<Vm>();
		for (PowerHostUtilizationHistory host : lowUtilizedHosts) {
			for(int i=0;i<(host.getVmList()).size();i++)
			{    
				if (!((host.getVmList()).get(i)).isInMigration()) {


					vmsToMigrate.add((host.getVmList()).get(i));
					//host.vmDestroy((host.getVmList()).get(i));
				}
			}
		}
		return vmsToMigrate;
	}

	protected abstract boolean isHostLowUtilized(PowerHost host);
	protected abstract boolean isHostMiddleUtilized(PowerHost host);
	protected abstract boolean isHostMediumUtilized(PowerHost host);
	//sprotected abstract boolean isHostOverUtilized(PowerHost host);

	/**
	 * Gets the over utilized hosts.
	 * 
	 * @return the over utilized hosts
	 */
	public List<PowerHostUtilizationHistory> getOverUtilizedHosts() {
		List<PowerHostUtilizationHistory> overUtilizedHosts = new LinkedList<PowerHostUtilizationHistory>();
		for (PowerHostUtilizationHistory host : this.<PowerHostUtilizationHistory> getHostList()) {
			//			if (CloudSim.clock()<900)
			//				return null;
			if (isHostOverUtilized(host)) {
				overUtilizedHosts.add(host);
			}
		}
		return overUtilizedHosts;
	}

	/**
	 * Gets the switched off host.
	 * 
	 * @return the switched off host
	 */
	public List<PowerHost> getSwitchedOffHosts() {
		List<PowerHost> switchedOffHosts = new LinkedList<PowerHost>();
		for (PowerHost host : this.<PowerHost> getHostList()) {
			if (host.getUtilizationOfCpu() == 0) {
				switchedOffHosts.add(host);
			}
		}
		return switchedOffHosts;
	}

	/**
	 * Gets the under utilized host.
	 * 
	 * @param excludedHosts the excluded hosts
	 * @return the under utilized host
	 */
	protected PowerHost getUnderUtilizedHost(Set<? extends Host> excludedHosts) {
		double minUtilization = 0.2;
		PowerHost underUtilizedHost = null;
		for (PowerHost host : this.<PowerHost> getHostList()) {
			if (excludedHosts.contains(host)) {
				continue;
			}
			double utilization = host.getUtilizationOfCpu();
			if (utilization > 0 && utilization < minUtilization
					&& !areAllVmsMigratingOutOrAnyVmMigratingIn(host)) {
				minUtilization = utilization;
				underUtilizedHost = host;
			}
		}
		return underUtilizedHost;
	}
	
	
	protected PowerHost getUnderUtilizedHost1(Set<? extends Host> excludedHosts) {
		double minUtilization = 0.3;
		PowerHost underUtilizedHost = null;
		for (PowerHost host : this.<PowerHost> getHostList()) {
			if (excludedHosts.contains(host)) {
				continue;
			}
			double utilization = host.getUtilizationOfCpu();
			if (utilization > 0 && utilization < minUtilization
					&& !areAllVmsMigratingOutOrAnyVmMigratingIn(host)) {
				minUtilization = utilization;
				underUtilizedHost = host;
			}
		}
		return underUtilizedHost;
	}

	/**
	 * Checks whether all vms are in migration.
	 * 
	 * @param host the host
	 * @return true, if successful
	 */
	protected boolean areAllVmsMigratingOutOrAnyVmMigratingIn(PowerHost host) {
		for (PowerVm vm : host.<PowerVm> getVmList()) {
			if (!vm.isInMigration()) {
				return false;
			}
			if (host.getVmsMigratingIn().contains(vm)) {
				return true;
			}
		}
		return true;
	}

	/**
	 * Checks if is host over utilized.
	 * 
	 * @param host the host
	 * @return true, if is host over utilized
	 */
	/////////////////////////////////////////////////////////////////////////////////////	
	//THE AFEF ALG.


	/////////////////////////////////////////////////////////////////////////////////	



	/**
	 * Adds the history value.
	 * 
	 * @param host the host
	 * @param metric the metric
	 */
	protected void addHistoryEntry(HostDynamicWorkload host, double metric) {
		int hostId = host.getId();
		if (!getTimeHistory().containsKey(hostId)) {
			getTimeHistory().put(hostId, new LinkedList<Double>());
		}
		if (!getUtilizationHistory().containsKey(hostId)) {
			getUtilizationHistory().put(hostId, new LinkedList<Double>());
		}
		if (!getMetricHistory().containsKey(hostId)) {
			getMetricHistory().put(hostId, new LinkedList<Double>());
		}
		if (!getTimeHistory().get(hostId).contains(CloudSim.clock())) {
			getTimeHistory().get(hostId).add(CloudSim.clock());
			getUtilizationHistory().get(hostId).add(host.getUtilizationOfCpu());
			getMetricHistory().get(hostId).add(metric);
		}
	}

	/**
	 * Save allocation.
	 */
	protected void saveAllocation() {
		getSavedAllocation().clear();
		for (Host host : getHostList()) {
			for (Vm vm : host.getVmList()) {
				if (host.getVmsMigratingIn().contains(vm)) {
					continue;
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("host", host);
				map.put("vm", vm);
				getSavedAllocation().add(map);
			}
		}
	}

	/**
	 * Restore allocation.
	 */
	protected void restoreAllocation() {
		for (Host host : getHostList()) {
			host.vmDestroyAll();
			host.reallocateMigratingInVms();
		}
		for (Map<String, Object> map : getSavedAllocation()) {
			Vm vm = (Vm) map.get("vm");
			PowerHost host = (PowerHost) map.get("host");
//			if (vm.getId()==245)
//				System.out.println("STOPE!!");
			if (vm.getHost()!=null) {
				if (vm.getHost()==host)
				  continue;
				else {
					PowerHost host1=(PowerHost) vm.getHost();
					host1.vmDestroy(vm);
								}
			}	
			if (!host.vmCreate(vm)) {
				Log.printLine("Couldn't restore VM #" + vm.getId() + " on host #" + host.getId());
				System.exit(0);
			}
			getVmTable().put(vm.getUid(), host);
		}
	}

	/**
	 * Gets the power after allocation.
	 * 
	 * @param host the host
	 * @param vm the vm
	 * 
	 * @return the power after allocation
	 */
	public static double getPowerAfterAllocation(PowerHost host, Vm vm) {
		double power = 0;
		try {
			power = host.getPowerModel().getPower(getMaxUtilizationAfterAllocation(host, vm));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return power;
	}

	/**
	 * Gets the power after allocation. We assume that load is balanced between PEs. The only
	 * restriction is: VM's max MIPS < PE's MIPS
	 * 
	 * @param host the host
	 * @param vm the vm
	 * 
	 * @return the power after allocation
	 */
	public static double getMaxUtilizationAfterAllocation(PowerHost host, Vm vm) {
		double requestedTotalMips = vm.getCurrentRequestedTotalMips();
		double hostUtilizationMips = getUtilizationOfCpuMips(host);
		double hostPotentialUtilizationMips = hostUtilizationMips + requestedTotalMips;
		double pePotentialUtilization = hostPotentialUtilizationMips / host.getTotalMips();
		return pePotentialUtilization;
	}

	/**
	 * Gets the utilization of the CPU in MIPS for the current potentially allocated VMs.
	 *
	 * @param host the host
	 *
	 * @return the utilization of the CPU in MIPS
	 */
	public static double getUtilizationOfCpuMips(PowerHost host) {
		double hostUtilizationMips = 0;
		for (Vm vm2 : host.getVmList()) {
			if (host.getVmsMigratingIn().contains(vm2)) {
				// calculate additional potential CPU usage of a migrating in VM
				hostUtilizationMips += host.getTotalAllocatedMipsForVm(vm2) * 0.9 / 0.1;
			}
			hostUtilizationMips += host.getTotalAllocatedMipsForVm(vm2);
		}
		return hostUtilizationMips;
	}

	/**
	 * Gets the saved allocation.
	 * 
	 * @return the saved allocation
	 */
	protected List<Map<String, Object>> getSavedAllocation() {
		return savedAllocation;
	}

	/**
	 * Sets the vm selection policy.
	 * 
	 * @param vmSelectionPolicy the new vm selection policy
	 */
	protected void setVmSelectionPolicy(PowerVmSelectionPolicy vmSelectionPolicy) {
		this.vmSelectionPolicy = vmSelectionPolicy;
	}

	/**
	 * Gets the vm selection policy.
	 * 
	 * @return the vm selection policy
	 */
	public  PowerVmSelectionPolicy getVmSelectionPolicy() {
		return vmSelectionPolicy;
	}

	/**
	 * Gets the utilization history.
	 * 
	 * @return the utilization history
	 */
	public Map<Integer, List<Double>> getUtilizationHistory() {
		return utilizationHistory;
	}

	/**
	 * Gets the metric history.
	 * 
	 * @return the metric history
	 */
	public Map<Integer, List<Double>> getMetricHistory() {
		return metricHistory;
	}

	/**
	 * Gets the time history.
	 * 
	 * @return the time history
	 */
	public Map<Integer, List<Double>> getTimeHistory() {
		return timeHistory;
	}

	/**
	 * Gets the execution time history vm selection.
	 * 
	 * @return the execution time history vm selection
	 */
	public List<Double> getExecutionTimeHistoryVmSelection() {
		return executionTimeHistoryVmSelection;
	}

	/**
	 * Gets the execution time history host selection.
	 * 
	 * @return the execution time history host selection
	 */
	public List<Double> getExecutionTimeHistoryHostSelection() {
		return executionTimeHistoryHostSelection;
	}

	/**
	 * Gets the execution time history vm reallocation.
	 * 
	 * @return the execution time history vm reallocation
	 */
	public List<Double> getExecutionTimeHistoryVmReallocation() {
		return executionTimeHistoryVmReallocation;
	}

	/**
	 * Gets the execution time history total.
	 * 
	 * @return the execution time history total
	 */
	public List<Double> getExecutionTimeHistoryTotal() {
		return executionTimeHistoryTotal;
	}

}

