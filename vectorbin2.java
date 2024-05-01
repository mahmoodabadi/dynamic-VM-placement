package vbproblem.src;

import org.cloudbus.cloudsim.examples.power.Helper;
import org.cloudbus.cloudsim.examples.power.*;
import java.io.File;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.DatacenterBroker;
//import org.cloudbus.cloudsim.Host;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.io.*;
import java.util.*;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.power.*;
import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;
import org.cloudbus.cloudsim.util.ExecutionTimeMeasurer;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.examples.power.Constants;
import org.cloudbus.cloudsim.examples.power.Helper;
import org.cloudbus.cloudsim.examples.power.custom.CustomHelper;
import org.cloudbus.cloudsim.examples.power.custom.CustomRunner;
import org.cloudbus.cloudsim.examples.power.custom.CustomRunnerAbstract;
import org.cloudbus.cloudsim.lists.VmList;
import org.yaml.snakeyaml.Yaml;

import ilog.concert.IloException;
import ilog.concert.IloIntExpr;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;
import vbproblem.src.small_class;
import shi.AbsoluteCapacity;

public class vectorbin2 extends PowerVmAllocationPolicyMigrationAbstract {

	public vectorbin2(double[][] hostList, PowerVmSelectionPolicy vmSelectionPolicy) {
		super(hostList, vmSelectionPolicy);
		// TODO Auto-generated constructor stub
	}
	public static ArrayList<PowerHost> hostForNextFit=new ArrayList<PowerHost>();
	public static ArrayList<PowerHost> hostForDP=new ArrayList<PowerHost>();
	public static ArrayList<Vm> vmForNextFit=new ArrayList<Vm>();
	public static ArrayList<Vm> vmForDP=new ArrayList<Vm>();
	public static int globalLastHostIndex=0;
	public static int NxtIndSmll=0;

	public static int numOfSolve=0;
	
	public static int[] hostForvms;

	
	
	static double[][] host_copy=new double [CustomRunner.hostList.length][3];
	public static List <PowerHost> hostList1_copy=new ArrayList<PowerHost>();
	
	public static List <Vm> vmList1_copy;
	static public List <Vm> onlineVms=new ArrayList<Vm>();

	public static double[][] vmList_copy;
			
	public static ArrayList<Double> migrationCost=new ArrayList<Double>();
	static ArrayList <ArrayList<double[]>> Q_Class1 = new ArrayList<ArrayList<double[]>>();
	public static ArrayList<ArrayList<double[]>> round1;
	public static int [] requests;
	public static double totalItemSize=0;
	public static int [] req1;
	public static int d=1;
	public static double epsilon=0.375;
	static double alpha;
	static int[] flag_one;
	public static double [][] class_items;
	static double beta=0.5;

	public static double [][] class_items1= {{0.9453125, 0.21875, 0.1},{0.7578125, 0.4296875, 0.1},{0.3828125, 0.4296875, 0.1}};
	public static double [][] class_itm;
	public static ArrayList <double[]> small=new ArrayList<double[]>();
	public static int [] dynam_array;
	public static double [] dynam_array_energy;
	public static int [] dynam_array1;
	public static HashMap<Integer,int[]> dynam_posh=new  HashMap<Integer,int[]>();
	public static HashMap<Integer,Integer> dynam_arrh=new  HashMap<Integer,Integer>();
	public static int [][] dynam_pos;
	public static int [][] dynam_pos2;
	public static int [] dynam_pos1;
	public static double [] dynam_pos1_energy;
	
	static double [] bins_energy;
	static double [] bins_id;
	static double [] [] lg_ind;
	static int indexOfnum=0;
	static int count=0;
	static int [] pbind;
	static int [] pbind1;
	static ArrayList<int[] > posBins = new ArrayList<int[]>();
	static ArrayList<int[] > posBins1 = new ArrayList<int[]>();
	static ArrayList<int[] > posBins2 = new ArrayList<int[]>();

	public static int [] level_list;
	public static int[] sml_flg;

	static ArrayList <double[]> large=new ArrayList<double[]>();
	static int [] larg_ind;
	static int [] larg_ind1;
	static int [] small_ind;
	static int [] small_ind1;
	static int guss_m=0;
	static double [][] I;
	public static double[] I_new= {0,0,0};
	public static double[][] IListNew;

    public static int nextfitIndex=0;
	static double [][] host; 
	static double [][] host_real;
	static double [][] host_org;
	static int[] req11= {1,2,2};
	static int the_last_host=0;
	static ArrayList<double []>host_bins= new ArrayList<double[]>();
	static ArrayList<Integer> host_ids=new ArrayList<Integer>();
	static int[] host_filled;
	static double[] powers;
	public static PowerHost[] powerhost;
	static ArrayList <double[]> Q=new ArrayList<double[]>();
	static int[] vm_allocate;
    static ArrayList<Integer> large_vm_ind=new ArrayList<Integer>();

	/** The vm list. */
	public static List<Vm> vmList1=new ArrayList<Vm>();
	public static List<PowerHost> hostList1;


	public static int result;//=new ArrayList<Integer>();
	public static double[][] vmList;
	public static double[][]hostList;
	public static int [] vmls_ids;
	public static int [] largevm_ids;
	public static double [] R;
	static double [][] pm_rel= {{0.3110,86.4955},{0.4414,92.9955}};//,{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41}};
	static double[][] pm_rel_cap= {{0.6992481203007519, 1.0, 1.0},{1.0, 1.0, 1.0}};
	


	public static PowerHost[] main(){
      
		

		
		
		//double [][] pm_rel= {{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41}};
		
		if (DatacenterBroker.vmList_items.size()>0) {
			System.out.print("TRUE");
			vectorbin2 vb= new vectorbin2(hostList,vmSelectionPolicy);
			host=host_org;
			if (Datacenter.Tag)
				vb.newly_arrived(Datacenter.v);
			else
			vb.newly_arrived(DatacenterBroker.vmList_items);
			onlineVms.add(DatacenterBroker.vmList_items.get(0));
			//PowerHost[] powerhost1=new PowerHost[10];
			return powerhost;

		}
		
		
		
		  
		
		

		//		try {
		//			Thread.sleep(1000);
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
//********getting hosts and vms from the customhelper**************//
		hostList=CustomRunner.hostList;
		hostList1=CustomRunner.hostList1;
		//hostList1_copy=new ArrayList (CustomRunner.hostList1);
		host=new double[hostList.length][d];
		host_real=new double[hostList.length][d];
		vmList1=CustomRunner.vmList1;
		//vmList1.add(CustomRunner.vmList1.get(0)); 
	//	vmList1.clear();
		vmList=CustomRunner.vmList;
		R=new double[hostList.length];
		IListNew=new double [vmList.length][d];

//*************Normalize the hosts and vms************************//	
		
		hostForvms=new int[vbproblem.src.vectorbin2.vmList.length];
		I=new double[vmList.length][d];
		vm_allocate=new int[vmList.length];
		
		largevm_ids=new int[I.length];
		normalize();
	 // OPT_energy_small_large(pm_rel_cap,pm_rel);
	//	OPT_vbp_small_large(pm_rel_cap);
	//	int limit1=OPT_energy(pm_rel_cap,pm_rel);
		//int limit=24;
	//OPT_vbp(pm_rel_cap,pm_rel);
	//	int limit=656;
		//solve_lp1(pm_rel,pm_rel_cap);
		//while (!solve_lp1_y(pm_rel,pm_rel_cap,limit))
//	    	limit++;
//
    Dynamic_bp_energy(pm_rel_cap,pm_rel);
	//Dynamic_bp(pm_rel_cap);
		int host_counts=0;
		double sum_R=0;
		for (int i=0;i<hostList1.size();i++)
			if (hostList1.get(i).getVmList().size()!=0)
				{
				host_counts++;
				double sum=0; double util=0, R=0;
				double[] host_i=new double [3];
		         
				
				
				double sum_CPU=0,sum_RAM=0,sum_BW=0;
				List<Vm> vml=hostList1.get(i).getVmList();
				
//				for (int j=0;j<vml.size();j++) {
//					sum_CPU+=I[vml.get(j).getId()][0];
//					sum_RAM+=I[vml.get(j).getId()][1];
//					sum_BW+=I[vml.get(j).getId()][2];
//
//				}
//				
				
				
				
				
				for (Vm vm:vml)
				  {
					
					sum_CPU+=vm.getMips();
					sum_RAM+=vm.getRam();
					sum_BW+=vm.getBw();
					
				  }
				
				double util_CPU=0, util_RAM, util_BW=0;
				
				util_CPU=sum_CPU/hostList[i][0];
				util_RAM=sum_RAM/hostList[i][1];
				util_BW=sum_BW/hostList[i][2];

				host_i[0]=1-util_CPU;
				host_i[1]=1-util_RAM;
				host_i[2]=1-util_BW;
				
					
				double min=Double.MAX_VALUE;
				for (int k=0;k<3;k++)
					if (host_i[k]<min)
						min=host_i[k];
				
				
				for (int k=0;k<3;k++)
					sum+=Math.abs(host_i[k]-min);
				
				
				util=util_CPU+util_RAM+util_BW;
				
				
				sum_R+=(sum+0.0001)/util;
		        

				}
		
		
		double w=0;
		for (int i=0;i<I.length;i++) {
		    w=0;
			for (int j=0;j<d;j++)
				w+=I[i][j];
			w=w/d;
			totalItemSize+=w;
			
		}
		
		
//		for (int i=0;i<host.length;i++)
//		{
//			
//			System.out.print("host "+ i + ": ");   
//
//			for (int j=0;j<host[i].length;j++)
//				System.out.print(host[i][j]+" ");
//			System.out.println("  ");   
//
//		}
		
	
		System.out.println("Number of Hosts:"+host_counts);
		System.out.println("Total R:"+sum_R);

		host_copy = new double[host.length][];
		for(int i = 0; i < host.length; i++)
		{
		  double[] aMatrix = host[i];
		  int   aLength = aMatrix.length;
		  host_copy[i] = new double[aLength];
		  System.arraycopy(aMatrix, 0, host_copy[i], 0, aLength);
		}
		//host_copy=host;

//		for (PowerHost h: hostList1) {
//			if (h.getVmList().size()>0)
//				for (Vm v:h.getVmList())
//				   hostList1_copy.get(h.getId()).vmCreate(v);
//		}
		

//		PowerHost[] hostCopy = new PowerHost[host.length];
//		hostList1_copy = new ArrayList<PowerHost>();
//		for(int i = 0; i < HostArray.length; i++)
//		{
//		  hostCopy[i] = HostArray[i];
//			hostList1_copy.add(HostArray[i]);	
//		  
//		}

//		hostList1_copy = new ArrayList<PowerHost>();
	//	hostList1_copy =  Arrays.asList(hostCopy);
	//	hostList1_copy.addAll(hostList1);
		
		
		for (PowerHost h : hostList1) {
			if (h.getVmList().size()!=0) {
				globalLastHostIndex=h.getId();
				
			}
		}
		
		
		//nextfitIndex=globalLastHostIndex;
		
		
       	return powerhost;
	}

	
	
	
	
	
	
	
public static void NewVmEntrance() {

		
	
	
	
	
	Q.clear();
			
	I=IListNew;
	vm_allocate=new int[IListNew.length];
	
	largevm_ids=new int[I.length];
    Dynamic_bp_energy_copy(pm_rel_cap,pm_rel);
    
    int count=0;
    
    for (PowerHost h:hostList1_copy)
    	if (h.getVmList().size()!=0)
    		count++;
    
    System.out.println("Number of OUTPUTS:"+ count);
    

//    double w=0;
//	for (int i=0;i<I.length;i++) {
//	    w=0;
//		for (int j=0;j<d;j++)
//			w+=I[i][j];
//		w=w/d;
//		totalItemSize+=w;
//		
//	}
//	
	
	
	}








//private static void normalize_copy() {
//	double[] max_org=new double[d];
//	max_org[0]=1;
//	max_org[1]=1;
//	max_org[2]=1;
//	vmls_ids=new int[vmList_copy.length];
//	for (int i=0;i<hostList_copy.length;i++)
//		for (int j=0;j<d;j++)
//			if (hostList_copy[i][j]>max_org[j])
//				max_org[j]=hostList_copy[i][j];
//
//				
//			
//	
//
//	for (int i=0;i<hostList_copy.length;i++)
//		for (int j=0;j<d;j++)
//				host[i][j]=hostList_copy[i][j]/max_org[j];
//			
//
//	for (int i=0;i<hostList_copy.length;i++)
//		for (int j=0;j<d;j++)
//				if (j!=d-1)
//				  host[i][j]*=0.625;
//
//	
//	
//	for (int i=0;i<vmList_copy.length;i++)
//		{
//		for (int j=0;j<d;j++)
//				I[i][j]=(vmList_copy[i][j]/max_org[j]);
//	//	vmls_ids[i]=(int) vmList[i][d];
//		}
//	host_org=new double[host.length][d];
//
//	
//	for (int i=0;i<host.length;i++)
//		for (int j=0;j<d;j++)
// 		   host_real[i][j]=host[i][j];
//	for (int i=0;i<host.length;i++)
//	{
//		if (host[i][0]+epsilon<1)
//			host[i][0]+=0.262218045112782;
//		else
//			host[i][0]+=epsilon;
//		for (int j=1;j<d-1;j++)
//			{
//			
//			host[i][j]+=epsilon;
//
//			}
//	}
//	for (int i=0;i<host.length;i++)
//		{
//		for (int j=0;j<d-1;j++)
//		
//			host_org[i][j]=host[i][j];
//		host_org[i][d-1]=1;
//		}
//	
//}








public void newly_arrived(List<Vm> vmList_items) {
	  Vm vm=vmList_items.get(0);
     // normalize_New_Vm(vm);
      Allocate_vm(vm);
      
      if (vm.getHost()==null)
	  { int k=W_host(pm_rel_cap,pm_rel);
	  	findHostForVm(k,vm);
	  }

}










private void findHostForVm(int k, Vm vm) {
	NextHost:
	for (int i=nextfitIndex;i<hostList.length;i++)
		//i%2==k && 
		if (hostList1.get(i).getVmList().size()==0)
			{
			    boolean flag=true;	
				for (int j=0;j<d;j++) {
				  if (host[i][j]>=I_new[j])
					  flag=true;
				  else {
					  flag=false;
					  continue NextHost;
				  }
				}
				
				if (hostList1.get(i).isSuitableForVm(vm))
				{
			    nextfitIndex=i;
				hostForNextFit.add(hostList1.get(i));
				vmForNextFit.add(vm);
				
	     		hostList1.get(i).vmCreate(vm);
				vm.setHost(hostList1.get(i));
				//vm_allocate[vm.getId()]=1;	
				PowerVmAllocationPolicyAbstract.getVmTable().put(vm.getUid(), hostList1.get(i));
				host_filled[i]=1;
				for (int k1=0;k1<d;k1++)
				   host[i][k1]=host[i][k1]-I_new[k1];
				double[] host_temp=new double[d];
				for (int j=0;j<d;j++)
			    	host_temp[j]=host_org[i][j]-host[i][j];
				host_bins.add(host_temp);
				host_temp=new double [d];
				host_ids.add(i);
				
				break;
				}
			
				}
	
	
	
	
	
	
	
	
	
	
			}









    






private static void Allocate_vm(Vm vm) {
	
	NextHost:
	for (int i=nextfitIndex;i<host.length;i++)
	{
		//if (i%2==1) {
		    boolean flag=true;	
			for (int j=0;j<d;j++) {
			  if (host[i][j]>=I_new[j])
				   flag=true;
			  else {
				  flag=false;
				  continue NextHost;
			  }
			  }
			//if (flag)
			  
			if (hostList1.get(i).isSuitableForVm(vm))
			{
			nextfitIndex=i;	
			hostForNextFit.add(hostList1.get(i));
			vmForNextFit.add(vm);
			hostList1.get(i).vmCreate(vm);
			vm.setHost(hostList1.get(i));
			//vm_allocate[vm.getId()]=1;	
			PowerVmAllocationPolicyAbstract.getVmTable().put(vm.getUid(), hostList1.get(i));
			for (int j=0;j<d;j++)
				host[i][j]-=I_new[j];
			
			
			
			
			return;
			}
		
		}
		
	}
	









private static int W_host(double[][] pm_rel_cap2, double[][] pm_rel2) {
	double util=0;double min_energy=Double.MAX_VALUE;
	double energy_temp=0;
	int wh=0;
	min_energy=Double.MAX_VALUE;
	util=I_new[0];
		for (int k=0;k<pm_rel.length;k++)
		{
			
			if (util>pm_rel_cap[k][0])
					continue;
			     energy_temp= compute_energy(util/pm_rel_cap[k][0],k);
			    if (energy_temp<min_energy) {
			        	min_energy=energy_temp;
			        	wh=k;
			    }   
		}
		return wh;
	}
	









public static double[] normalize_New_Vm(Vm vm) {
	double[] max_org=new double[d];
	max_org[0]=1;
	//max_org[1]=1;
//	max_org[2]=1;
	//Vm vm=vmList_items.get(0);
	double[] vm_values=new double[d];
	
	vm_values[0]=vm.getMips();
	//vm_values[1]=vm.getRam();
	//vm_values[2]=vm.getBw();
	
	
	for (int i=0;i<hostList.length;i++)
		for (int j=0;j<d;j++)
			if (hostList[i][j]>max_org[j])
				max_org[j]=hostList[i][j];

	
	
	for (int j=0;j<d;j++) 
	 	I_new[j]=(vm_values[j]/max_org[j]);
	return I_new;	

}

public static void normalize_Vm_List(List<Vm> vmList) {
	double[] max_org=new double[d];
	max_org[0]=1;
	//max_org[1]=1;
	//max_org[2]=1;
	IListNew=new double[vmList.size()][d];
	vmls_ids=new int[vmList_copy.length];

	
	//flag_one=new
	
	//		flag_one=new int[vmList.size()];

	
	
	
	for (int k=0;k<hostList.length;k++)
		for (int j=0;j<d;j++)
			if (hostList[k][j]>max_org[j])
				max_org[j]=hostList[k][j];

	
	for (int i1=0;i1<hostList.length;i1++)
		for (int j=0;j<d;j++)
				host_copy[i1][j]=(hostList[i1][j]/max_org[j])+0.375;
			
	
	for (int i=0;i<vmList.size();i++) {
		 Vm vm=vmList.get(i);
		 double[] vm_values=new double[d];
	  	 vm_values[0]=vm.getMips();
//		 vm_values[1]=vm.getRam();
	//	 vm_values[2]=vm.getBw();
	  	flag_one[vm.getId()]=0;
	 //   cpu=cl.getUtilizationOfCpu(CloudSim.clock())*vm1.getMips();

		 Cloudlet cl=CustomRunner.cloudletList.get(vm.getId());
	    double cpu= cl.getUtilizationOfCpu(CloudSim.clock());
	   
//    	for (int j=0;j<d;j++)
//	  	  IListNew[i][j]=(vm_values[j]/max_org[j]);

	    IListNew[i][0]=cpu;
//    	for (int j=1;j<d;j++)
//	  	  IListNew[i][j]=(vm_values[j]/max_org[j]);

	}

}








private static void Vm_File() throws IOException {
	InputStream input = new FileInputStream(new File("testconfig/all/bitbrains.txt"));
    Yaml yaml = new Yaml();
    Map<String,String> filenames = (Map<String,String>)yaml.load(input);

	input = new FileInputStream(new File(filenames.get("vms")));

    List<Map<String,Object>> vms = (List<Map<String,Object>>)yaml.load(input);
   
   
    
	for(int i=0;i<263;i++)
	{	
		Map<String,Object> vmwrap=vms.get(i);
		Map<String,Object> vm = (Map<String,Object>)vmwrap.get("VM");
	    int VM_MIPS = ((ArrayList<Integer>)vm.get("CpuCapacity")).get(0);

	   // while (VM_MIPS==2500)
	    //{
		int ID = ((ArrayList<Integer>)vm.get("ID")).get(0);
	    //int VM_MIPS = ((ArrayList<Integer>)vm.get("CpuCapacity")).get(0);
		int VM_PES = (int)vm.get("Cores");
		int VM_RAM = (int)vm.get("RAM");
		int VM_BW = (int)vm.get("BandWidth");
		int VM_SIZE = (int)vm.get("DiskCapacity");
		
		
	//	broker = Helper.createBroker();

		//int brokerId = broker.getId();

		
		Vm vm1= new PowerVm(
				ID,
				2,
				VM_MIPS,
				VM_PES,
				VM_RAM,
				VM_BW,
				VM_SIZE,
				1,
				"Xen",
				new CloudletSchedulerDynamicWorkload(VM_MIPS, VM_PES),
				Constants.SCHEDULING_INTERVAL);
		
		
		   FileWriter writer = new FileWriter("testconfig/all/vml.txt");
		   yaml.dump(vm1, writer);

	}
	//}
		
	}








private static void Dynamic_bp(double[][] pm_rel_cap) {
	//guss_m=Classification();
	//int best_res=Classification();
	guss_m=vmList.length;
	host_filled=new int[host.length];
	sml_flg=new int[vmList.length]; //for small items when finding corresponding vms
	
	
//**************Rounding and classification items*******************//
	round1=compround1(beta);
	//sml_flg=new int[vmList1.size()]; //for small items when finding corresponding vms

	class_items=new double [round1.size()][d];
	requests= new int [round1.size()];
	for (int i=0;i<round1.size();i++)
	{   
		requests[i]=round1.get(i).size();
		class_items[i]=round1.get(i).get(0);
	}

	int Rl=round1.size();
	int[] counters = new int[Rl];

	double [] size=new double [d];
	getBins1(size,counters,0,posBins);


	long  dynam_array_length=1;
	for (int i=0;i<requests.length;i++)
		dynam_array_length*=(requests[i]+1);
	dynam_array=new int [(int) dynam_array_length];
	dynam_pos=new int [(int) dynam_array_length][round1.size()];
	dynam_pos1=new int [(int) dynam_array_length];
	pbind=new int [posBins.size()];
	level_list=new int [hostList1.size()];
	int [] temp=new int [requests.length];
	for (int i=0;i<temp.length;i++)
		temp[i]=-1;
	for (int i=1;i<posBins.size();i++)//remove
	{
		int index=findIndex(posBins.get(i));
		pbind[i]=index;
		dynam_array[index]=1;
		dynam_pos1[index]=-1;
		for (int k=0;k<round1.size();k++)
		{
			dynam_pos[index][k]=-1;

		}
	}

	level_list=new int[posBins.size()];
	
	
////****************Dynamic Bin packing**************************************//
	int l=1;int u=vmList.length;
	int m=(l+u)/2;
	guss_m=(int) (m+Math.ceil((epsilon*m)/2));
	result=Dynamic_Bins(0,requests);
	
	System.out.println("Number of Bins:"+ result);
	
	result=Filled_hosts_vbp();
	//result=Filled_hosts();
	//guss_m=result;
//	while (result!=guss_m)
//	{
//		if (result>m)
//		  {l=m+1;
//		  m=(l+u)/2;
//		  guss_m=(int) (m+Math.ceil((epsilon*m)/2));
//		  }
//		else
//		{
//			u=m;
//			m=(l+u)/2;
//			guss_m=(int) (m+Math.ceil((epsilon*m)/2));
//		}
//	}
	double[] host_temp=new double [d];
	
	for (int i=0;i<host_filled.length;i++)
		if (host_filled[i]!=0)
		{
//			for (int j=0;j<d;j++)
//		    	host_temp[j]=host_org[i][j]-host[i][j];
//			host_bins.add(host_temp);
			host_bins.add(host[i]);

			host_temp=new double [d];
		}
//	for (int i=0;i<host.length;i++)
//	{
//		
//		System.out.print("host "+ i + ": ");   
//
//		for (int j=0;j<host[i].length;j++)
//			System.out.print(host[i][j]+" ");
//		System.out.println("  ");   
//
//	}
//	
		
		
	the_last_host=host_ids.get(host_ids.size()-1);
	the_last_host++;
//the_last_host=0;
	
	ArrayList <Integer> ind_m=new ArrayList<Integer>();
	boolean flag=solvemethod1_vbp(host_ids.size(),pm_rel_cap);
	int count=0;int temp1=0;int temp2=0;
	guss_m=(int) (result+Math.ceil((epsilon*result)/2));

	ind_m.add(guss_m);
	if (!flag)
		{
		l=result;
		m=(guss_m-result)/2;
		
		//u=vmList.length;
	//	int m1=(l+u)/2;
		m=result+m;
		while (m>result)
		{
		ind_m.add(m);	
		u=m;
		m=(u-l)/2;
		m=result+m;
		}
		count=ind_m.size()-1;
		int t=0;
		while (!flag)
		{
		if (count==ind_m.size()-1)
			temp1=ind_m.get(count);
		else
			temp1=ind_m.get(count)-ind_m.get(count+1);
		if (temp1>host_bins.size())
			t= temp1-host_bins.size();
		else
			t= temp1;
		for (int i=0;i<t;i++)
		{
			if (the_last_host<host.length-1) {
				host_temp=new double [d];
				host_bins.add(host_temp);
				the_last_host++;
				host_ids.add(the_last_host++);
			
			}
		}
	//	flag=solvemethod1(ind_m.get(count),pm_rel_cap);
		if (ind_m.get(count)>host_ids.size())
	    	flag=solvemethod1_vbp(host_ids.size()-1,pm_rel_cap);
		else
			flag=solvemethod1_vbp(ind_m.get(count),pm_rel_cap);

		if (flag)
			break;
		if (count>0)
		  count--;
		else {
			t=17;
			for (int i=0;i<t;i++)
			{
				if (the_last_host<host.length-1) {
					host_temp=new double [d];
					host_bins.add(host_temp);
					the_last_host++;

					host_ids.add(the_last_host++);
				
				}
			}
			flag=solvemethod1_vbp(642,pm_rel_cap);
		}
		}
						}


	}




private static void Dynamic_bp_energy_copy(double[][] pm_rel_cap,double[][] pm_rel) {
	//guss_m=Classification();
//	int best_res=Classification();	guss_m=vmList.length;
	host_filled=new int[host_copy.length];
	sml_flg=new int[I.length]; //for small items when finding corresponding vms

	small_class1.clear();
	//***************************
	posBins.clear();
	//sml_flg=new int[1];
	
	
	large.clear();
	small.clear();
//**************Rounding and classification items*******************//
	double beta=0.4;
	round1=compround1Copy(beta);
	
	if (large.size()>0) {
	class_items=new double [round1.size()][d];
	requests= new int [round1.size()];
	for (int i=0;i<round1.size();i++)
	{   
		requests[i]=round1.get(i).size();
		class_items[i]=round1.get(i).get(0);
	}
	
	
	

	int Rl=round1.size();
	int[] counters = new int[Rl];

	double [] size=new double [d];
	//int[] counters1=new int[3];
	getBins1(size,counters,0,posBins);
	//getBins1_type2(size,counters,0,posBins);

	bins_energy=new double[posBins.size()];
	bins_id=new double[posBins.size()];

	long  dynam_array_length=1;
	for (int i=0;i<requests.length;i++)   
		dynam_array_length*=(requests[i]+1);
	dynam_array=new int [(int) dynam_array_length];
	dynam_array_energy=new double [(int) dynam_array_length];
	dynam_pos=new int [(int) dynam_array_length][round1.size()];
	dynam_pos1=new int [(int) dynam_array_length];
	dynam_pos1_energy=new double[(int) dynam_array_length];
	pbind=new int [posBins.size()];
	level_list=new int [hostList1.size()];
	int [] temp=new int [requests.length];
	for (int i=0;i<temp.length;i++)
		temp[i]=-1;
	
	
	double sum_of_util=0;double min_energy=Double.MAX_VALUE;
	double energy_temp=0;
	
	for(int i=1;i<posBins.size();i++)
	{
		min_energy=Double.MAX_VALUE;
		sum_of_util=0;
		for (int j=0;j<requests.length;j++)
		if (posBins.get(i)[j]!=0)
			sum_of_util+=(posBins.get(i)[j]*class_items[j][0]);
		for (int k=0;k<pm_rel.length;k++)
		{
			
			if (sum_of_util>pm_rel_cap[k][0])
					continue;
			     energy_temp= compute_energy(sum_of_util/pm_rel_cap[k][0],k);
			    if (energy_temp<min_energy)
			    	{
			    	min_energy=energy_temp;
			    	bins_id[i]=k;
			    	}
				            
		}
		 bins_energy[i]=min_energy;
			
	}
	for (int i=1;i<posBins.size();i++)//remove
	{
		int index=findIndex(posBins.get(i));
		pbind[i]=index;
		dynam_array[index]=1;
		dynam_array_energy[index]=bins_energy[i];
		dynam_pos1[index]=-1;
		for (int k=0;k<round1.size();k++)
		{
			dynam_pos[index][k]=-1;

		}
	}

	level_list=new int[posBins.size()];
	
	double result1=0;
////****************Dynamic Bin packing**************************************//
	
	
	result1=Dynamic_Bins_energy(0,requests);
	
	System.out.println("Number of Bins:"+ result1);
	
	result=0;
	
	//Filled_bins1();
	result=Filled_hosts_copy();
	//result=Filled_hosts();
	
	int count0=0;int count1=0;
	
	for (int k=0;k<hostList1_copy.size();k++)
	{
		sum_of_util=0;
		if (hostList1_copy.get(k).getAvailableMips()!=hostList1_copy.get(k).getTotalMips())
		{	if (k%2==0)
			 count0++;
		    else
		    	count1++;
		}
		}

	System.out.println("Number of Bins:"+ count0);
	System.out.println("Number of Bins:"+ count1);

	double sum=0;double sum_of_energy=0;
	for (int i=0;i<hostList1_copy.size();i++)
		{
		sum=0;
		if (host_filled[i]==1)
		{
			
			List<Vm> vml=hostList1_copy.get(i).getVmList();
			for (Vm vm:vml)
			  sum+=hostList1_copy.get(i).getTotalAllocatedMipsForVm(vm);
			sum/=hostList1_copy.get(i).getTotalMips();
	        sum_of_energy+=compute_energy(sum,i%2);
		}
		}
	}
	
	double[] host_temp=new double [d];
host_bins.clear();
host_ids.clear();
//host_ids=hostForDP;
	for (int i=0;i<hostList1_copy.size();i++)
		if (hostList1_copy.get(i).getVmList().size()!=0)
		{
			for (int j=0;j<d;j++)
		    	host_temp[j]=host_org[i][j]-host_copy[i][j];
			host_bins.add(host_temp);
			host_temp=new double [d];
			the_last_host=hostList1_copy.get(i).getId();
			host_ids.add(i);
		}
	
	for (int i=0;i<hostForDP.size();i++)
		host_ids.add(hostForDP.get(i).getId());
	
		
		
	
		if (small.size()>0) {
	     	int l=1;
	     	int u=vmList_copy.length;
	     	int mid=(l+u)/2;
			guss_m=(int) (mid+Math.ceil((epsilon*mid)/2));	
		    ArrayList <Integer>  ind_m=new ArrayList<Integer>();		
		    boolean flag=solvemethod1Copy(result,pm_rel_cap);
		
		    int count=0;int temp1=0;int temp2=0;
		    ind_m.add(guss_m);
		    if (!flag)
				{
		    	 l=result;
		    	 mid=(guss_m-result)/2;
		    	 mid=result+mid;
		    	 while (mid>result)
		    	 {
		    		 ind_m.add(mid);	
		    		 u=mid;
		    		 mid=(u-l)/2;
		    		 mid=result+mid;
		    	 }
		   if (ind_m.size()>0) {	 
			count=ind_m.size()-1;
			
		   }
		   int t=0;
			numOfSolve=0;
			L1:
			while (!flag)
			{
		   
			if (count==ind_m.size()-1)
				temp1=ind_m.get(count);
			else
				temp1=ind_m.get(count)-ind_m.get(count+1);
			
			if (temp1>host_bins.size())
				t= temp1-host_bins.size();
			else
				t= temp1;
		   
			for (int i=0;i<t;i++)
			{
				if (the_last_host<host.length-2) {
				host_temp=new double [d];
				host_bins.add(host_temp);
				the_last_host+=2;
				host_ids.add(the_last_host);
				
				}
				else {
					the_last_host=1;
					continue;
				}
					
			}
		   
		//flag=solvemethod1(ind_m.get(count),pm_rel_cap);
//			if (numOfSolve>5) {
//				NxtIndSmll=0;
//				NextFit(small);
//				break L1;
//			}
			if (ind_m.get(count)>host_ids.size()) {
				flag=solvemethod1Copy(ind_m.get(count),pm_rel_cap);
				numOfSolve++;
			}
			else {
		    	flag=solvemethod1Copy(host_ids.size()-1,pm_rel_cap);
		    	numOfSolve++;
			}
				//flag=solvemethod1Copy(ind_m.get(count),pm_rel_cap);

			if (flag)
				break;
			if (count>0)
				  count--;
			else {
				l=ind_m.get(count);
				u=IListNew.length;
				mid=(u-l)/2;
				ind_m.clear();
				//u=vmList.length;
			//	int m1=(l+u)/2;
				mid=l+mid;
				while (mid>l)
				{
				ind_m.add(mid);
				u=mid;
				mid=(u-l)/2;
				mid=l+mid;
				
			}
				count=ind_m.size()-1;
			}
			}
			}
				
		}
			
//				

}

private static void NextFit(ArrayList<double[]> small2) {
	for (int i=0;i<small2.size();i++) {
		double[] sm=small2.get(i);
		Vm vm=null;
	   for (int j=0;j<vmList_copy.length;j++)
	   { 
		 if (compareItems1(sm, I[j]) && sml_flg[j]!=1)
		{
			sml_flg[j]=1;
			vm=vmList1_copy.get(j);
			flag_one[j]=1;
			break;
		}
	}
	
	  for (int k=NxtIndSmll;k<hostList1_copy.size();k++) {
		  PowerHost h=hostList1_copy.get(k);
		  if (h.isSuitableForVm(vm)) {
			  h.vmCreate(vm);
			  vm.setHost(h);
			  NxtIndSmll=h.getId();
			  break;
		  }
	  }
	   
}
}







private static void Dynamic_bp_energy(double[][] pm_rel_cap,double[][] pm_rel) {
	//guss_m=Classification();
//	int best_res=Classification();	guss_m=vmList.length;
	host_filled=new int[host.length];
	sml_flg=new int[vmList.length]; //for small items when finding corresponding vms
	
	//***************************
	if (IListNew[0][0]>0) {
		
		large_vm_ind.clear();
		small.clear();
		Q.clear();
	}
	
	
	
//	if (I_new[0]>0) {
//		posBins.clear();
//		sml_flg=new int[1];
//	}
//**************Rounding and classification items*******************//
	double beta=0.4;
	//double beta=epsilon/(2*d);

	round1=compround1(beta);
	class_items=new double [round1.size()][d];
	requests= new int [round1.size()];
	for (int i=0;i<round1.size();i++)
	{   
		requests[i]=round1.get(i).size();
		class_items[i]=round1.get(i).get(0);
	}
	
	
	

	int Rl=round1.size();
	int[] counters = new int[Rl];

	double [] size=new double [d];
	//int[] counters1=new int[3];
	getBins1(size,counters,0,posBins);
	//getBins1_type2(size,counters,0,posBins);

	bins_energy=new double[posBins.size()];
	bins_id=new double[posBins.size()];

	long  dynam_array_length=1;
	for (int i=0;i<requests.length;i++)   
		dynam_array_length*=(requests[i]+1);
	//dynam_array=new int [(int) dynam_array_length];
	dynam_array=new int [(int) dynam_array_length];

	dynam_array_energy=new double [(int) dynam_array_length];
	dynam_pos=new int [(int) dynam_array_length][round1.size()];
	dynam_pos1=new int [(int) dynam_array_length];
	dynam_pos1_energy=new double[(int) dynam_array_length];
	pbind=new int [posBins.size()];
	level_list=new int [hostList1.size()];
	int [] temp=new int [requests.length];
	for (int i=0;i<temp.length;i++)
		temp[i]=-1;
	
	
	double sum_of_util=0;double min_energy=Double.MAX_VALUE;
	double energy_temp=0;
	
	for(int i=1;i<posBins.size();i++)
	{
		min_energy=Double.MAX_VALUE;
		sum_of_util=0;
		for (int j=0;j<requests.length;j++)
		if (posBins.get(i)[j]!=0)
			sum_of_util+=(posBins.get(i)[j]*class_items[j][0]);
		for (int k=0;k<pm_rel.length;k++)
		{
			
			if (sum_of_util>pm_rel_cap[k][0])
					continue;
			     energy_temp= compute_energy(sum_of_util/pm_rel_cap[k][0],k);
			    if (energy_temp<min_energy)
			    	{
			    	min_energy=energy_temp;
			    	bins_id[i]=k;
			    	}
				            
		}
		 bins_energy[i]=min_energy;
			
	}
	for (int i=1;i<posBins.size();i++)//remove
	{
		int index=findIndex(posBins.get(i));
		pbind[i]=index;
		dynam_array[index]=1;
		dynam_array_energy[index]=bins_energy[i];
		dynam_pos1[index]=-1;
		for (int k=0;k<round1.size();k++)
		{
			dynam_pos[index][k]=-1;

		}
	}

	level_list=new int[posBins.size()];
	
	double result1=0;
////****************Dynamic Bin packing**************************************//
	int l=1;int u=vmList.length;
	int mid=(l+u)/2;
	guss_m=(int) (mid+Math.ceil((epsilon*mid)/2));
	
	result1=Dynamic_Bins_energy(0,requests);
	
	System.out.println("Number of Bins:"+ result1);
	
	
	
	//Filled_bins1();
	result=Filled_hosts();
	//result=Filled_hosts();
	
	int count0=0;int count1=0;
	
	for (int k=0;k<hostList1.size();k++)
	{
		sum_of_util=0;
		if (hostList1.get(k).getVmList().size()!=0)
		{	if (k%2==0)
			 count0++;
		    else
		    	count1++;
		}
		}

	System.out.println("Number of Bins:"+ count0);
	System.out.println("Number of Bins:"+ count1);

	double sum=0;double sum_of_energy=0;
	for (int i=0;i<hostList1.size();i++)
		{
		sum=0;
		if (host_filled[i]==1)
		{
			
			List<Vm> vml=hostList1.get(i).getVmList();
			for (Vm vm:vml)
			  sum+=hostList1.get(i).getTotalAllocatedMipsForVm(vm);
			sum/=hostList1.get(i).getTotalMips();
	        sum_of_energy+=compute_energy(sum,i%2);
		}
		}
//if (result>guss_m)
//{
//	while (result!=guss_m)
//	{
//		if (result>m)
//		  {l=m+1;
//		  m=(l+u)/2;
//			guss_m=(int) (m+Math.ceil((epsilon*m)/2));
//		  }
//		else
//		{
//			u=m;
//			m=(l+u)/2;
//			guss_m=(int) (m+Math.ceil((epsilon*m)/2));
//		}
//	}
//	
//}
//guss_m=(int) (result+Math.ceil((epsilon*result)/2))+1;


	double[] host_temp=new double [d];

	for (int i=0;i<host_filled.length;i++)
		if (host_filled[i]!=0)
		{
			for (int j=0;j<d;j++)
		    	host_temp[j]=host_org[i][j]-host[i][j];
			host_bins.add(host_temp);
			host_temp=new double [d];
			host_ids.add(i);	
		}
	
//	for (int i=host_ids.get(host_ids.size()-1)+1;i<=Math.abs(guss_m-result);i++)
//		host_ids.add(i);
//	for (int i=0;i<host.length;i++)
//	{
//		
//		System.out.print("host "+ i + ": ");   
//
//		for (int j=0;j<host[i].length;j++)
//			System.out.print(host[i][j]+" ");
//		System.out.println("  ");   
//
//	}
//	
		
		
		the_last_host=host_ids.get(host_ids.size()-1);
		//the_last_host++;
	//the_last_host=0;
		if (I_new[0]>0)
			return;
		ArrayList <Integer>  ind_m=new ArrayList<Integer>();
		boolean flag=solvemethod1(result,pm_rel_cap);
		//boolean flag=solvemethod1(guss_m,pm_rel_cap);
		int count=0;int temp1=0;int temp2=0;
		ind_m.add(guss_m);
		if (!flag)
			{
			l=result;
			mid=(guss_m-result)/2;
			
			//u=vmList.length;
		//	int m1=(l+u)/2;
			mid=result+mid;
			while (mid>result)
			{
			ind_m.add(mid);	
			u=mid;
			mid=(u-l)/2;
			mid=result+mid;
			}
			count=ind_m.size()-1;
			int t=0;
			while (!flag)
			{
			if (count==ind_m.size()-1)
				temp1=ind_m.get(count);
			else
				temp1=ind_m.get(count)-ind_m.get(count+1);
			if (temp1>host_bins.size())
				t= temp1-host_bins.size();
			else
				t= temp1;
			for (int i=0;i<t;i++)
			{
				if (the_last_host<host.length-2) {
				host_temp=new double [d];
				host_bins.add(host_temp);
				//if(the_last_host%2==1)
				  host_ids.add(the_last_host+=2);
				//else
				//{
				//	host_ids.add(++the_last_host);
				//	the_last_host++;
				//}
				}
			}
		//	flag=solvemethod1(ind_m.get(count),pm_rel_cap);
			if (ind_m.get(count)>host_ids.size())
		    	flag=solvemethod1(host_ids.size()-1,pm_rel_cap);
			else
				flag=solvemethod1(ind_m.get(count),pm_rel_cap);

			if (flag)
				break;
			if (count>0)
				  count--;
			else {
				l=ind_m.get(count);
				u=vmList.length;
				mid=(u-l)/2;
				ind_m.clear();
				//u=vmList.length;
			//	int m1=(l+u)/2;
				mid=l+mid;
				while (mid>l)
				{
				ind_m.add(mid);
				u=mid;
				mid=(u-l)/2;
				mid=l+mid;
				
			}
				count=ind_m.size()-1;
			}
			}
			}
			
			
//				else {
//					t=29;
//					for (int i=0;i<t;i++)
//					{
//						if (the_last_host<host.length-1) {
//							host_temp=new double [d];
//							host_bins.add(host_temp);
//							the_last_host++;
//
//							host_ids.add(the_last_host++);
//						
//						}
//					}
//					flag=solvemethod1(654,pm_rel_cap);
//				}
			//}
				//			}
	//	change_host();

}








private static double compute_R(int i, int j) {
	double sum=0; double util=0, R=0;
	double[] host_i=new double [3];
     
	//System.arraycopy(host_i, 0, host[i], 0, 1);

	
	double sum_CPU=0,sum_RAM=0,sum_BW=0;
	List<Vm> vml=hostList1.get(i).getVmList();
	int vm_id=0;
	for (Vm vm:vml)
	  {
		vm_id=vm.getId();
		sum_CPU+=I[vm_id][0];
		sum_RAM+=I[vm_id][1];
		sum_BW+=I[vm_id][2];
		
		
	  }
    
	sum_CPU+=I[j][0];
	sum_RAM+=I[j][1];
	sum_BW+=I[j][2];

	double util_CPU=0, util_RAM, util_BW=0;
	
	util_CPU=sum_CPU/host_real[i][0];
	util_RAM=sum_RAM/host_real[i][1];
	util_BW=sum_BW/host_real[i][2];

	host_i[0]=host_real[i][0]-util_CPU;
	host_i[1]=host_real[i][1]-util_RAM;
	host_i[2]=host_real[i][2]-util_BW;
	
		
	double min=Double.MAX_VALUE; double max=Double.MIN_VALUE;
	for (int k=0;k<3;k++)
		if (host_i[k]<min)
			min=host_i[k];
	if (util_CPU>max)
		max=util_CPU;
	
	if (util_RAM>max)
		max=util_RAM;
	
	if (util_BW>max)
		max=util_BW;
	
	
	for (int k=0;k<3;k++)
		sum+=Math.abs(host_i[k]-min);
	
	
	util=util_CPU+util_RAM+util_BW;
	
	
	R=(sum+0.0001)/util;
    
	return R;
	
}







static boolean solve_lp1_y(double [][] pm_rel,double[][] pm_rel_cap,int limit) {
	
	try {

		
	
		IloCplex model= new  IloCplex();
		IloIntVar [][] x=  new IloIntVar[I.length][host.length];

		IloIntVar [] y=  new IloIntVar[host.length];
		
		for (int i=0;i<host.length;i++)
		{
			String varname="y"+(i+1);
			y[i]= model.intVar(0,1,varname);
		}
		
		for (int i=0;i<I.length;i++)
		{
			for (int j=0;j<host.length;j++)
			{
				String varname="x"+(i+1)+""+(j+1);
				x[i][j]= model.intVar(0,1,varname);
			} 
			
		}


		for (int i=0;i<I.length;i++)
		{
			IloLinearNumExpr cons2=model.linearNumExpr();
			for (int j=0;j<host.length;j++)
				cons2.addTerm(1, x[i][j]);
			model.addEq(cons2,1);	
		}		

			
		for (int j=0;j<host.length;j++)
		{	
				IloLinearNumExpr cons=model.linearNumExpr();
				for (int i=0;i<I.length;i++)
					cons.addTerm(I[i][0],x[i][j]);
				model.addLe(cons,model.prod(y[j],pm_rel_cap[j % 2 ][0]));
				
		
			}
	

		IloLinearNumExpr cons3=model.linearNumExpr();
		for (int j=0;j<host.length;j++)
			cons3.addTerm(1,y[j]);
		model.addLe(cons3,limit);
		
		
		
		IloNumExpr objective=model.numExpr();
		for (int k=0;k<host.length;k++)
			for (int i=0;i<I.length;i++)
			   objective=model.sum(objective,model.prod((I[i][0]/pm_rel_cap[k%2][0])*pm_rel[k%2][0]+pm_rel[k%2][1],y[k],x[i][k]));
		
		model.addMinimize(objective);

		
		//IloCplex.Param.Barrier.ConvergeTol,1e-12
		
		//model.setParam(IloCplex.Param.SolutionTarget,2);
		//model.setParam(IloCplex.Param.Simplex.Tolerances.Optimality,1e-5);
		//model.setParam(IloCplex.Param.TimeLimit,60);
		//model.setParam(IloCplex.Param.Barrier.QCPConvergeTol,1e-8);

		//model.setParam(IloCplex.DoubleParam.TiLim, 10);	

		model.exportModel("cost_rel.lp");
		int x1[][]=new int[I.length][host.length];

		
		if (model.solve())
	{

			double objValue=model.getObjValue();
			System.out.println("obj_val=" + objValue);

			for (int i=0;i< x.length;i++)
				for (int j=0;j<y.length;j++)
				{	x1[i][j]=(int) model.getValue(x[i][j]);
					if (model.getValue(x[i][j])==1)
					{
						
						System.out.println("x[" +(i+1)+"]["+(j+1)+"] = "+(model.getValue(x[i][j])+0.0));
					}

				}
			for (int j=0;j<host.length;j++)
				System.out.println("y[" +(j+1)+"] = "+(model.getValue(y[j])+0.0));
	      model.end();
	      
	      Filled_hosts_lp(x1);
	      
	}
		else
			{
			System.out.print("Model not solved!");
			return false;
			}
} catch (IloException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
return true;
}





static void solve_lp1(double [][] pm_rel,double[][] pm_rel_cap) {
	
	try {

		
	
		IloCplex model= new  IloCplex();
		IloIntVar [][] x=  new IloIntVar[I.length][host.length];

			
		
		for (int i=0;i<I.length;i++)
		{
			for (int j=0;j<host.length;j++)
			{
				String varname="x"+(i+1)+""+(j+1);
				x[i][j]= model.intVar(0,1,varname);
			} 
			
		}


		for (int i=0;i<I.length;i++)
		{

			IloLinearNumExpr cons2=model.linearNumExpr();
			for (int j=0;j<host.length;j++)
				cons2.addTerm(1, x[i][j]);
			model.addEq(cons2,1);	
		}		

			
		
		
		for (int j=0;j<host.length;j++)
		{	
				IloLinearNumExpr cons=model.linearNumExpr();
				for (int i=0;i<I.length;i++)
					cons.addTerm(I[i][0],x[i][j]);
				model.addLe(cons,host[j][0]);
		
			}
	

		double sum_i=0;
			
		IloLinearNumExpr obj= model.linearNumExpr();
	
		for (int k=0;k<host.length;k++)
	    	{
    		for (int i=0;i<I.length;i++)
					obj.addTerm((I[i][0]/host[k][0])*pm_rel[k%2][0]+pm_rel[k%2][1],x[i][k]);
			} 	
		
		model.addMinimize(obj);
	

		//obj.addTerm((I[i][0]/pm_rel_cap[k%2][0])*pm_rel[k%2][0]+pm_rel[k%2][1],x[i][k]);



		model.exportModel("cost_rel.lp");
		int x1[][]=new int[I.length][host.length];

		
		if (model.solve())
	{

			double objValue=model.getObjValue();
			System.out.println("obj_val=" + objValue);

			for (int i=0;i< x.length;i++)
				for (int j=0;j<host.length;j++)
				{	x1[i][j]=(int) model.getValue(x[i][j]);
					if (model.getValue(x[i][j])==1)
					{
						
						System.out.println("x[" +(i+1)+"]["+(j+1)+"] = "+(model.getValue(x[i][j])+0.0));
					}

				}
			
	      model.end();
	      
	      Filled_hosts_lp(x1);
	}
		else
			System.out.print("Model not solved!");
} catch (IloException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

}




	
	
	

	private static void change_host() {
		int sum_of_mips=0;
		double energy1=0,energy2=0;
		List<Double> pe;
		for (int i=0;i<hostList1.size();i++)
		{	
		if (hostList1.get(i).getVmList().size()==0)
			continue;
		List <Vm> vml=hostList1.get(i).getVmList();
		sum_of_mips=0;
			if (vml.size()!=0)
			{				
				for (Vm vm:vml)
				{
					sum_of_mips+=vm.getMips();
				}
				if (sum_of_mips<=1860)
				{
					energy1=compute_energy1(sum_of_mips,0);
					energy2=compute_energy1(sum_of_mips,1);
					if (energy1<energy2 && i%2!=0)
					  change(hostList1.get(i),0);
					else
						if (energy2<energy1 && i%2==0)
							change(hostList1.get(i),1);
				}
			}
			
		}		
			
		
	}



	private static void change(PowerHost powerHost2,int ind) {
		List <Vm> vml=powerHost2.getVmList();
		ArrayList<Integer> vm_ind=new ArrayList<Integer>();
		int id=0;
		int i=0;int level=0;
		boolean flag=true;
		for (Vm vm:vml)
		{
			vm_ind.add(find_vm_ids(vml.get(i).getId()));
			i++;
		}
		powerHost2.vmDestroyAll();
		i=0;
		//powerHost2.vmDestroyAll();
		hostLoop:
		for (int h=0;h<hostList1.size();h+=2)
		{
		while (i<vm_ind.size())
			{
			id=vm_ind.get(i);
			
			 for (int j=0;j<d;j++)
				 if (host[h][j]-I[id][j]>=0 && h%2==ind)
				 {
					 host[h][j]-=I[id][j];
					 flag=true;
					 level=h;
				 }
			     else
			     {
			    	 if (hostList1.get(h).getVmList().size()==0)
			    	   host[h]=host_org[h];
					flag=false;
					i=0;
					continue hostLoop;
			     }
			 if (flag)
				i++; 
			}
		if (flag)
			break;
		
			}
		i=0;
		Lvl_loop:
		while (i<vm_ind.size())
		{
		
		if (hostList1.get(level).isSuitableForVm(vmList1.get(vm_ind.get(i))))
			{
		     hostList1.get(level).vmCreate(vmList1.get(vm_ind.get(i)));
		     vmList1.get(vm_ind.get(i)).setHost(hostList1.get(level));
			PowerVmAllocationPolicyAbstract.getVmTable().put(vmList1.get(vm_ind.get(i)).getUid(), hostList1.get(level));
			host_filled[level]=1;
			
			}
		else
			{
		    level++;
		    continue Lvl_loop;
			}
         i++;		
         }
			
				
	}



	private static int find_ids(int id) {
		for (int i=0;i<largevm_ids.length;i++)
			if (id==largevm_ids[i])
				return i;
		return -1;
	}

	private static int find_vm_ids(int id) {
		for (int i=0;i<vmList1.size();i++)
			if (id==vmList1.get(i).getId())
				return i;
		return -1;
	}

	private static int bsearch(int best) {
		int low=0;int high=vmList.length-1;int mid=0;
		while (low<=high)
			{
				mid=(low+high)/2;
				if (mid==best)
					return mid;
				if (mid<best)
				     low=mid+1;
				else
					if (mid>best)
					   high=mid-1;
			}

		return 0;
		
	}


	

	private static void Filled_hosts_lp(int[][] x) {
	
	
		for (int i=0;i<I.length;i++)
			for(int j=0;j<host.length;j++)
				if (x[i][j]==1)
				{
					if (hostList1.get(j).isSuitableForVm(vmList1.get(i)))
					{
				    hostList1.get(j).vmCreate(vmList1.get(i));
				    vmList1.get(i).setHost(hostList1.get(j));
					
					PowerVmAllocationPolicyAbstract.getVmTable().put(vmList1.get(i).getUid(), hostList1.get(j));
					//getVmTable().put(vm.getUid(), host);
					//host_filled[j]=1;
					for (int k=0;k<d;k++)
					  host[j][k]-=I[i][k];
				}
				}
					
	
		
		
		
	}

	
	
	
	
	private static int Filled_hosts_small() {
		
		ArrayList<double[]> items=new ArrayList<double[]>();
		ArrayList <Vm> vm=new ArrayList <Vm>();
		double[] real_item=new double[d];
		int lg_item;
		ArrayList <int[]> Final_bins=new ArrayList<int[]>();
		int [] temp=new int[requests.length];
		int [] temp1=new int [requests.length];

		temp1=posBins.get(dynam_pos1[findIndex(requests)]);
		Final_bins.add(temp1);
		for (int i=0;i<requests.length;i++)
			temp[i]=requests[i]-temp1[i];


		int index=findIndex(temp);

		while (dynam_pos1[index]!=-1 && !temp1.equals(0))
		{
     		temp1=posBins.get(dynam_pos1[index]);
			Final_bins.add(temp1);
			for (int i=0;i<temp.length;i++)
				temp[i]=temp[i]-temp1[i];

			index=findIndex(temp);
		}
		for (int i=0;i<pbind.length;i++)
			if (pbind[i]==index)
				Final_bins.add(posBins.get(i));

//		System.out.println("Used Bins Items:");
//		int ind=1;
//		for (int[] FB : Final_bins) {
//			System.out.println("Bin " + (ind++)+" :"+Arrays.toString(FB));
//		}
	    int level=0;
	    
		int index_vm=0;
		ArrayList<Integer> vm_ids=new ArrayList<Integer>();
		
		for (int i = 0; i< Final_bins.size(); i++)
		{
			if (level>host.length)
				break;
			vm.clear();	
			vm_ids.clear();
		  for (int j=0;j<requests.length;j++)
			{
				if (Final_bins.get(i)[j]!=0)
					{
					while (flag_one[index_vm]!=0)
						index_vm++;
						
					for (int k=0;k<Final_bins.get(i)[j];k++)
					{
						flag_one[index_vm]=1;
						 vm.add(vmList1.get(index_vm));
						 vm_ids.add(index_vm);
						 index_vm++;
					}
					
					}
			}
			
		
			
			boolean t=false;
			int i2=0;double sum=0;
			for (Vm vm1:vm)
				sum+=vm1.getMips();
			//level=0;
			double energy1=compute_energy(sum/hostList1.get(0).getTotalMips(),0);
			double energy2=compute_energy(sum/hostList1.get(1).getTotalMips(),1);
			
			L2:
			while (i2<vm.size() && level<host.length)
			{
//			   if (level%2!=1 && hostList1.get(level).getVmList().size()!=0)
//			   {
//				   level++;
//				   continue;
//			   }
				for (int k=0;k<host[level].length;k++)
					if (host[level][k]-I[vm_ids.get(i2)][k]>=0 )
						{
						host[level][k]-=I[vm_ids.get(i2)][k];
						t=true;
							}
						else
						{
							if (host_filled[level]!=1)
							  host[level]=host_org[level];
							t=false;
							i2=0;
							level++;
							break;
							
						}
				if (t)
				  i2++;
					
			}
			i2=0;
		
			host_filled[level]=1;
			the_last_host=level;
			
			Lvl_loop:
			
			while (i2<vm.size() && level<host.length-1)
				{
				if (hostList1.get(level).isSuitableForVm(vm.get(i2)))
					{
		     		hostList1.get(level).vmCreate(vm.get(i2));
					vm.get(i2).setHost(hostList1.get(level));
					vm_allocate[vm_ids.get(i2)]=1;	
					PowerVmAllocationPolicyAbstract.getVmTable().put(vm.get(i2).getUid(), hostList1.get(level));
					
//					for (int k=0;k<host[level].length;k++)
//							host[level][k]-=I[vm_ids.get(i2)][k];
					}
				else
					{
				    level++;
				    
					continue Lvl_loop;
					}
		         i2++;		
		         }
			level++;
		}

		
		
//		for (int i=0;i<host.length;i++)
//		{
//			
//			System.out.print("host "+ i + ": ");   
//
//			for (int j=0;j<host[i].length;j++)
//				System.out.print(host[i][j]+" ");
//			System.out.println("  ");   
//
//		}
		return Final_bins.size();

	}

	
	
	

	private static int Filled_hosts() {
		flag_one=new int[I.length];
		ArrayList<double[]> items=new ArrayList<double[]>();
		ArrayList <Vm> vm=new ArrayList <Vm>();
		double[] real_item=new double[d];
		int lg_item;
		ArrayList <int[]> Final_bins=new ArrayList<int[]>();
		int [] temp=new int[requests.length];
		int [] temp1=new int [requests.length];
		if (requests.length==1)
        	temp1=posBins.get(1);
        else
	 	    temp1=posBins.get(dynam_pos1[findIndex(requests)]);
		Final_bins.add(temp1);
//		if (I_new[0]>0)
//			temp1=posBins.get(0);
//		else
//		    temp1=posBins.get(dynam_pos1[findIndex(requests)]);
		//Final_bins.add(temp1);
		for (int i=0;i<requests.length;i++)
			temp[i]=requests[i]-temp1[i];


		int index=findIndex(temp);
        if (index!=0) {
		while (dynam_pos1[index]!=-1 && !temp1.equals(0))
		{
     		temp1=posBins.get(dynam_pos1[index]);
			Final_bins.add(temp1);
			for (int i=0;i<temp.length;i++)
				temp[i]=temp[i]-temp1[i];

			index=findIndex(temp);
		}
		for (int i=0;i<pbind.length;i++)
			if (pbind[i]==index)
				Final_bins.add(posBins.get(i));

		//System.out.println("Used Bins Items:");
		int ind=1;
        }
//		for (int[] FB : Final_bins) {
//			System.out.println("Bin " + (ind++)+" :"+Arrays.toString(FB));
//		}
	    int level=0;
		
	    if (I_new[0]>0)
	    	Final_bins.remove(0);
	    
		for (int i = 0; i<Final_bins.size(); i++)
		{
			if (i==45)
				System.out.print(flag);
			items.clear();

			for (int j=0;j<Final_bins.get(i).length;j++)
			{
				if (Final_bins.get(i)[j]!=0)
					{
					lg_item=find_class(j);
					for (int k=0;k<d;k++)
				    	real_item[k]=Q.get(lg_item)[k];
					for (int k1=0;k1<Final_bins.get(i)[j];k1++)
				     items.add(Add_item(real_item));
			}
				
			}
			vm.clear();
			ArrayList<Integer> vm_ids=new ArrayList<Integer>();
			loop:
				for (int i1=0;i1<items.size();i1++)
				{
					if (I_new[0]>0) {
						  vm.add(DatacenterBroker.vmList_items.get(0));
						  vm_ids.clear();
						  vm_ids.add(vm.get(0).getId());
						  vm_allocate=new int[vm.size()];
						  continue loop;
					}
					
					
					
					jloop:
						for (int j=0;j<Q.size();j++)
						{ 
							if (compareItems1(items.get(i1), Q.get(j)) && flag_one[large_vm_ind.get(j)]!=1)// && flag_one[j]!=1)
							{	 
								//for (int k1=0;k1<I.length;k1++)
									//if (large.get(j)[0]==I[k1][0] && flag_one[k1]!=1) {
								
								        vm.add(vmList1.get(large_vm_ind.get(j)));
										vm_ids.add(large_vm_ind.get(j));
										flag_one[large_vm_ind.get(j)]=1;
										break;
									
								// vm.add(vmList1.get(large_vm_ind.get(j)));
							}
							
							//continue loop;
							}
//							else
//								continue jloop;
//						}

				}
			
			boolean t=false;
			int i2=0;double sum=0;
			int t1=0;
			for (Vm vm1:vm) {
//				
				if (vm1.getId()==35)
					System.out.print("STOP");
				Cloudlet cl=CustomRunner.cloudletList.get(vm1.getId());
				double cpu=0;//= cl.getUtilizationOfCpu(CloudSim.clock());
				if (CloudSim.clock()==0)
					cpu=cl.getUtilizationOfCpu(0)*vm1.getMips();
				else
				    cpu=cl.getUtilizationOfCpu(CloudSim.clock())*vm1.getMips();
				sum+=cpu;
			}
				
			//sum*=100;	
				//sum+=vm1.getMips();
			double energy1=0,energy2=0;
			if (sum<=hostList1.get(0).getTotalMips())
			{	energy1=compute_energy(sum/hostList1.get(0).getTotalMips(),0);
				energy2=compute_energy(sum/hostList1.get(1).getTotalMips(),1);
			}
			if (energy1<energy2)
				t1=1;
			else if (energy2<energy1)
				t1=2;
//			if (level>=hostList1.size())
//				level=0;

			switch(t1) {
			case 1:
				level=find_level_odd(level,vm_ids,sum);
				break;
			case 2:
				level=find_level_even(level,vm_ids,sum);
				break;
			default:
				level=find_level_copy(sum,vm.get(i2),level);

				break;
			}
			
	      
			
			
		
			boolean flag=false;
				i2=0;
			HOST:
			while (i2<vm.size())// && level<host.length)
				{
				flag=false;
				if (hostList1.get(level).isSuitableForVm1(vm.get(i2)))
					{
		     		hostList1.get(level).vmCreate(vm.get(i2));
					vm.get(i2).setHost(hostList1.get(level));
					//vm_allocate[vm_ids.get(i2)]=1;	
					host_filled[level]=1;
					PowerVmAllocationPolicyAbstract.getVmTable().put(vm.get(i2).getUid(), hostList1.get(level));
					if (I_new[0]>0)
						for (int k=0;k<host[level].length;k++)
						  host[level][k]-=Q.get(0)[k];
					else
					{
					for (int k=0;k<host[level].length;k++)
					    	host[level][k]-=I[vm_ids.get(i2)][k];
					}
					flag=true;
					}
	        	
	         if (!flag)
				{
					level=find_level(sum,vm.get(i2),level);
					i2=0;
					i2++;
					continue HOST;
				}
	         i2++;
		         }
			
			level++;
		}
		

		
		int host_counts=0;
		double sum_R=0;
		for (int i=0;i<hostList1.size();i++)
			if (hostList1.get(i).getVmList().size()!=0)
				host_counts++;
		
		System.out.println("Number of Hosts:"+host_counts);
		return Final_bins.size();

	}

	
	
	static boolean checkPegs(int array[], int size)
	{
	  for (int i = 0; i < size; i++)
	  {
	      if(array[i] != 0)
	      {
	        return false;
	      }
	  }
	  return true;
	}
	
	private static int Filled_hosts_copy() {
		//flag_one=new int[I.length];
		ArrayList<double[]> items=new ArrayList<double[]>();
		ArrayList <Vm> vm=new ArrayList <Vm>();
		double[] real_item=new double[d];
		int lg_item;
		ArrayList <int[]> Final_bins=new ArrayList<int[]>();
		int [] temp=new int[requests.length];
		int [] temp1=new int [requests.length];
		
//		if (IListNew[0][0]>0)
//			temp1=posBins.get(0);
//		else
		
//		 if (requests.length==1)
//	        	temp1=posBins.get(1);
//	        else
//		 	    temp1=posBins.get(dynam_pos1[findIndex(requests)]);
		 
		    if (!checkPegs(temp1, temp1.length))
			   Final_bins.add(temp1);
		    else {
		    	for (int k=0;k<dynam_pos1.length;k++)
		    		if (dynam_pos1[k]==-1) {
		    			temp1=posBins.get(k);
		    			break;
		    		}
		    	Final_bins.add(temp1);
		    }
			for (int i=0;i<requests.length;i++)
				temp[i]=requests[i]-temp1[i];


			int index=findIndex(temp);
	        if (index!=0) { 
			while (dynam_pos1[index]!=-1 && !temp1.equals(0))
			{
	     		temp1=posBins.get(dynam_pos1[index]);
				Final_bins.add(temp1);
				for (int i=0;i<temp.length;i++)
					temp[i]=temp[i]-temp1[i];

				index=findIndex(temp);
			}
			for (int i=0;i<pbind.length;i++)
				if (pbind[i]==index)
					Final_bins.add(posBins.get(i));
	        }
	
		
		
		
		
		
//		if (dynam_pos1[findIndex(requests)]==-1) {
//			temp1=posBins.get(1);
//			Final_bins.add(temp1);
//		}
//		else {	
//		    temp1=posBins.get(dynam_pos1[findIndex(requests)]);
//		
//		Final_bins.add(temp1);
//    
//		for (int i=0;i<requests.length;i++)
//			temp[i]=requests[i]-temp1[i];
//
//
//		index=findIndex(temp);
//
//		while (dynam_pos1[index]!=-1 &&  !checkPegs(temp1,temp1.length))
//		{
//     		temp1=posBins.get(dynam_pos1[index]);
//			Final_bins.add(temp1);
//			for (int i=0;i<temp.length;i++)
//				temp[i]=temp[i]-temp1[i];
//
//			index=findIndex(temp);
//		}
//		for (int i=0;i<pbind.length;i++)
//			if (pbind[i]==index)
//				Final_bins.add(posBins.get(i));
//		
//		if (dynam_pos1[findIndex(requests)]==-1) {
//			temp1=posBins.get(1);
//			Final_bins.add(temp1);
//		}
		
		
		//}
//		System.out.println("Used Bins Items:");
//		int ind=1;
//		for (int[] FB : Final_bins) {
//			System.out.println("Bin " + (ind++)+" :"+Arrays.toString(FB));
//		}
	    int level=0;
	    
//	    for (int i=0;i<hostList1_copy.size();i+=2) {
//			   if (hostList1_copy.get(level).getVmList().size()==0) {
//				   level=i;
//				   break;
//			   }
//	    }
		for (int i = 0; i< Final_bins.size(); i++)
		{
			items.clear();

			for (int j=0;j<Final_bins.get(i).length;j++)
			{
				if (Final_bins.get(i)[j]!=0)
					{
					if (Final_bins.get(i)[j]==2)
						System.out.print("STOP");
					lg_item=find_class(j);
					for (int k=0;k<d;k++)
				    	real_item[k]=Q.get(lg_item)[k];
					for (int k1=0;k1<Final_bins.get(i)[j];k1++)
				     items.add(Add_item(real_item));
			}
				
			}
			vm.clear();
			ArrayList<Integer> vm_ids=new ArrayList<Integer>();
			ArrayList<Integer> I_ids=new ArrayList<Integer>();

			
			java.util.Arrays.sort(I, new java.util.Comparator<double[]>() {
			    public int compare(double[] a, double[] b) {
			        return Double.compare(b[0], a[0]);
			    }
			});
			
			
			
			loop:
				for (int i1=0;i1<items.size();i1++)
				{
					jloop:
						for (int j=0;j<Q.size();j++)
						{ 
							if (compareItems1(items.get(i1), Q.get(j)))// && flag_one[j]!=1)
							{	 
								if (compareItems1(items.get(i1), Q.get(j)) && flag_one[large_vm_ind.get(j)]!=1)// && flag_one[j]!=1)
								{	 
									//for (int k1=0;k1<I.length;k1++)
										//if (large.get(j)[0]==I[k1][0] && flag_one[k1]!=1) {
									
											for (int g=0;g<vmListItemsDP.size();g++)
												if (large_vm_ind.get(j)==vmListItemsDP.get(g).getId()) {
													vm.add(vmListItemsDP.get(g));
													break;
												}
									        
											vm_ids.add(large_vm_ind.get(j));
											flag_one[large_vm_ind.get(j)]=1;
											break;
										
									// vm.add(vmList1.get(large_vm_ind.get(j)));
								}
							}
							else
								continue jloop;
						}

				}
			
			boolean t=false;
			int i2=0;double sum=0;
			int t1=0;
			if (i==7)
				System.out.print(t);	
			
			for (Vm vm1:vm) {
//				
//				if (vm1.getId()==35)
//					System.out.print("STOP");
				Cloudlet cl=CustomRunner.cloudletList.get(vm1.getId());
				double cpu=0;//= cl.getUtilizationOfCpu(CloudSim.clock());
				if (CloudSim.clock()==0)
					cpu=cl.getUtilizationOfCpu(0)*vm1.getMips();
				else
				    cpu=cl.getUtilizationOfCpu(CloudSim.clock())*vm1.getMips();
				sum+=cpu;
			}
			
			
			double energy1=0,energy2=0;
//			if (sum<=hostList1_copy.get(0).getTotalMips())
//			{	energy1=compute_energy(sum/hostList1_copy.get(0).getTotalMips(),0);
//				energy2=compute_energy(sum/hostList1_copy.get(1).getTotalMips(),1);
//			}
//			else {
//				energy1=0;
//				energy2=compute_energy(sum/hostList1_copy.get(1).getTotalMips(),1);
//			}
//				
//			
//			
//			if (energy1<energy2)
//				t1=1;
//			else if (energy2<energy1)
//				t1=2;
//			if (level>=hostList1.size())
//				level=0;
			t1=1;
			
			switch(t1) {
			case 1:
				level=find_level_odd_copy(level,vm,sum);
				break;
			case 2:
				level=find_level_even_copy(level,vm_ids,sum);
				break;
			default:
				level=find_level_copy(sum,vm.get(i2),level);
				break;
			}
			
	      
			
			
		
			boolean flag=false;
				i2=0;
			HOST:
			while (i2<vm.size())// && level<host.length)
				{
				flag=false;
				if (hostList1_copy.get(level).isSuitableForVm1(vm.get(i2)))
					{
					if (CloudSim.clock()>86100)
						System.out.print("HI");
				    if (PowerVmAllocationPolicyMigrationAbstract.vmList_items.contains(vm.get(i2)))
     					hostForDP.add(hostList1_copy.get(level));
					vmForDP.add(vm.get(i2));
					hostList1_copy.get(level).vmCreate(vm.get(i2));
					vm.get(i2).setHost(hostList1_copy.get(level));
					//vm_allocate[vm_ids.get(i2)]=1;	
					host_filled[level]=1;
					//PowerVmAllocationPolicyAbstract.getVmTable1().put(vm.get(i2).getUid(), hostList1_copy.get(level));
					//for (int k=0;k<host_copy[level].length;k++)
						  //host_copy[level][k]-=Q.get(vm_ids.get(i2))[k];
						//  host_copy[level][k]-=I[vm_ids.get(i2)][k];

					if(hostForDP.size()==0)
						hostForDP.add(hostList1_copy.get(level));
					flag=true;
					}
	         
	         if (!flag)
				{
	        	 if (level<host_copy.length-1)
					level=find_level_copy(sum,vm.get(i2),level+1);
	        	 else
	        		 level=1;
					i2=0;
					continue HOST;
				}
				i2++;	

		         }
			level++;
		}
		
		
//		for (int i=0;i<host.length;i++)
//		{
//			
//			System.out.print("host "+ i + ": ");   
//
//			for (int j=0;j<host[i].length;j++)
//				System.out.print(host[i][j]+" ");
//			System.out.println("  ");   
//
//		}
		return Final_bins.size();

	}


	
	
	
	
	
	
	
	
	

	private static int find_level_even(int level, ArrayList<Integer> vm_ids, double sum) {
		int l=0;
		if (level%2==0 && hostList1.get(level).getVmList().size()==0)
			return level;
		if (level%2==1)
			level++;
		for (int i=level;i<hostList1.size();i+=2) {
			if (hostList1.get(i).getVmList().size()!=0)
				continue;
			else {
				l= i;
				break;
			}
		}
		if (l==0) {
		for (int i=1;i<hostList1.size();i+=2) {
			if (hostList1.get(i).getVmList().size()!=0)
				continue;
			else {
				l=level;
				break;
			}
		}
		}
	return l;	
		
	}

	private static int find_level_even_copy(int level, ArrayList<Integer> vm_ids, double sum) {
		int l=0;
		if (level%2==0 && hostList1_copy.get(level).getVmList().size()==0)
			return level;
		if (level%2==1)
			level++;
		for (int i=level;i<hostList1_copy.size();i+=2) {
			if (hostList1_copy.get(i).getVmList().size()!=0)
				continue;
			else {
				l= i;
				break;
			}
		}
		if (l==0) {
		for (int i=1;i<hostList1_copy.size();i+=2) {
			if (hostList1_copy.get(i).getVmList().size()!=0)
				continue;
			else {
				l=level;
				break;
			}
		}
		}
	return l;	
		
	}







	private static int find_level_odd_copy(int level, ArrayList<Vm> vm,double sum) {
		boolean flag=false;
		int l=0;
		if (level>=0 && level%2==0)
			level++;
		if (level==0)
			level++;
		LEVEL:
		for (int i=level;i<hostList1_copy.size();i+=2) {
		   if (hostList1_copy.get(i).getVmList().size()==0) {
			 flag=false;
			for(Vm v:vm)
				if (hostList1_copy.get(i).isSuitableForVm1(v))
					flag=true;
				else {
					flag=false;
					continue LEVEL;
				}
			 if (hostList1_copy.get(i).getAvailableMips()>sum && flag==true) {
				 l=i;
				 break;
			 }
				else
					{
					flag=false;
					continue LEVEL;
					}
		}
		}
//		if (l==0) {
//	    	find_level(sum,Vvm_ids.get(0),0);
//		}
	return l;	
	}

	private static int find_level_odd(int level, ArrayList<Integer> vm_ids,double sum) {
		boolean flag=false;
		int l=0;
		if (level%2==0)
			level++;
		
		LEVEL:
		for (int i=level;i<hostList1.size();i+=2) {
		   if (hostList1.get(level).getVmList().size()==0) {
			flag=false;
			for(int id:vm_ids)
				if (hostList1.get(level).isSuitableForVm1(vmList1.get(id)))
					flag=true;
				else
					{
					flag=false;
					continue LEVEL;
					}
			 if (hostList1.get(level).getAvailableMips()>sum && flag==true) {
				 l=level;
				 break;
			 }
				else
					{
					flag=false;
					continue LEVEL;
					}
		}
		}
//		if (l==0) {
//	    	find_level_copy(sum,vmList1.get(id));
//		}
	return l;	
	}

	
	
	
	
	
	private static int find_level(double sum, Vm vm, int level) {
		int l=0;
		boolean flag=false;
		int index=0;	
		LEVEL:
			for (int i=level;i<hostList1.size();i++) {
				l=i;	
		      if (hostList1.get(i).getVmList().size()==0 && sum<hostList1.get(i).getAvailableMips()) 
		    	  if (hostList1.get(i).isSuitableForVm(vm))
					  return i;
				     
			  }
			   
       return l;		
	}


	private static int find_level_copy(double sum, Vm vm,int level) {
		int l=0;
		boolean flag=false;
		int index=0;	
		LEVEL:
			for (int i=level;i<hostList1_copy.size();i++) {
				l=i;int size=0;
				if (hostList1_copy.get(i).getVmList().size()==0 && sum<hostList1_copy.get(i).getTotalMips())
				    if (hostList1_copy.get(i).isSuitableForVm1(vm))
					  return i;
			  }


       return l;		
	}




	private static int Filled_hosts_vbp() {
		flag_one=new int[I.length];
		ArrayList<double[]> items=new ArrayList<double[]>();
		ArrayList <Vm> vm=new ArrayList <Vm>();
		double[] real_item=new double[d];
		int lg_item;
		ArrayList <int[]> Final_bins=new ArrayList<int[]>();
		int [] temp=new int[requests.length];
		int [] temp1=new int [requests.length];
        if (requests.length==1)
        	temp1=posBins.get(1);
        else
	 	    temp1=posBins.get(dynam_pos1[findIndex(requests)]);
		Final_bins.add(temp1);
		for (int i=0;i<requests.length;i++)
			temp[i]=requests[i]-temp1[i];


		int index=findIndex(temp);
        if (index!=0) { 
		while (dynam_pos1[index]!=-1 && !temp1.equals(0))
		{
     		temp1=posBins.get(dynam_pos1[index]);
			Final_bins.add(temp1);
			for (int i=0;i<temp.length;i++)
				temp[i]=temp[i]-temp1[i];

			index=findIndex(temp);
		}
		for (int i=0;i<pbind.length;i++)
			if (pbind[i]==index)
				Final_bins.add(posBins.get(i));
        }
//		System.out.println("Used Bins Items:");
//		int ind=1;
//		for (int[] FB : Final_bins) {
//			System.out.println("Bin " + (ind++)+" :"+Arrays.toString(FB));
//		}
	    int level=0;
		
		for (int i = 0; i< Final_bins.size(); i++)
		{
			items.clear();

			for (int j=0;j<round1.size();j++)
			{
				if (Final_bins.get(i)[j]!=0)
					{
					lg_item=find_class(j);
					for (int k=0;k<d;k++)
				    	real_item[k]=Q.get(lg_item)[k];
					for (int k1=0;k1<Final_bins.get(i)[j];k1++)
				     items.add(Add_item(real_item));
			}
			}
			vm.clear();
			ArrayList<Integer> vm_ids=new ArrayList<Integer>();
			loop:
				for (int i1=0;i1<items.size();i1++)
				{
					jloop:
						for (int j=0;j<Q.size();j++)
						{ 
							if (compareItems1(items.get(i1), Q.get(j)) && flag_one[j]!=1)
							{	  vm.add(vmList1.get(large_vm_ind.get(j)));
							vm_ids.add(j);
							flag_one[j]=1;
							continue loop;
							}
							else
								continue jloop;
						}

				}
			
			boolean t=false;
			int i2=0;
			while (i2<vm.size() && level<host.length)
			{
				for (int k=0;k<host[level].length;k++)
				
					if (host[level][k]-Q.get(vm_ids.get(i2))[k]>=0 )
						{
						host[level][k]-=Q.get(vm_ids.get(i2))[k];
						t=true;
						
							}
						else
						{
							t=false;
							if (hostList1.get(level).getVmList().size()==0)
							  host[level]=host_org[level];
							i2=0;
							level++;
							break;
							
						}
				if (t)
				  i2++;
				
			}
			i2=0;
			if (level==host.length)
				break;
			host_filled[level]=1;
			//the_last_host=level;
			host_ids.add(level);
		
			while (i2<vm.size() && level<host.length-1) {
				
				if (hostList1.get(level).isSuitableForVm(vm.get(i2)))
					{
		     		hostList1.get(level).vmCreate(vm.get(i2));
					vm.get(i2).setHost(hostList1.get(level));
					vm_allocate[vm_ids.get(i2)]=1;	
					PowerVmAllocationPolicyAbstract.getVmTable().put(vm.get(i2).getUid(), hostList1.get(level));
					i2++;			
					}
				
		         }
			level++;
		}
		
		
//		for (int i=0;i<host.length;i++)
//		{
//			
//			System.out.print("host "+ i + ": ");   
//
//			for (int j=0;j<host[i].length;j++)
//				System.out.print(host[i][j]+" ");
//			System.out.println("  ");   
//
//		}
		return Final_bins.size();

	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	private static ArrayList <int[]> Filled_hosts_classification() {
		flag_one=new int[I.length];
		ArrayList<double[]> items=new ArrayList<double[]>();
		ArrayList <Vm> vm=new ArrayList <Vm>();
		double[] real_item=new double[d];
		int lg_item;
		ArrayList <int[]> Final_bins=new ArrayList<int[]>();
		int [] temp=new int[req1.length];
		int [] temp1=new int [req1.length];

		temp1=posBins1.get(dynam_pos1[findIndex_class(req1)]);
		Final_bins.add(temp1);
		for (int i=0;i<req1.length;i++)
			temp[i]=req1[i]-temp1[i];


		int index=findIndex_class(temp);

		while (dynam_pos1[index]!=-1 && !temp1.equals(0))
		{
     		temp1=posBins1.get(dynam_pos1[index]);
			Final_bins.add(temp1);
			for (int i=0;i<temp.length;i++)
				temp[i]=temp[i]-temp1[i];

			index=findIndex_class(temp);
		}
		for (int i=0;i<pbind.length;i++)
			if (pbind[i]==index)
				Final_bins.add(posBins1.get(i));

//		System.out.println("Used Bins Items:");
//		int ind=1;
//		for (int[] FB : Final_bins) {
//			System.out.println("Bin " + (ind++)+" :"+Arrays.toString(FB));
//		}
	    int level=0;
		
		for (int i = 0; i< Final_bins.size(); i++)
		{
			items.clear();

			for (int j=0;j<Q_Class1.size();j++)
			{
				if (Final_bins.get(i)[j]!=0)
					{
					lg_item=find_class_classification(j);
					for (int k=0;k<d;k++)
				    	real_item[k]=large.get(lg_item)[k];
					for (int k1=0;k1<Final_bins.get(i)[j];k1++)
				     items.add(Add_item(real_item));
			}
			}
			vm.clear();
			
			ArrayList<Integer> vm_ids=new ArrayList<Integer>();
			loop:
				for (int i1=0;i1<items.size();i1++)
				{
					jloop:
						for (int j=0;j<I.length;j++)
						{ 
							if (compareItems1(items.get(i1), large.get(j)) && flag_one[j]!=1)
							{	  vm.add(vmList1.get(j));
							vm_ids.add(j);
							flag_one[j]=1;
							continue loop;
							}
							else
								continue jloop;
						}

				}
			
			boolean t=false;
			int i2=0;double sum=0;
			  boolean t1=false,t2=false;
			for (Vm vm1:vm)
				sum+=vm1.getMips();
			double energy1=0,energy2=0;
			if (sum<=hostList1.get(0).getTotalMips())
			{	energy1=compute_energy(sum/hostList1.get(0).getTotalMips(),0);
				energy2=compute_energy(sum/hostList1.get(1).getTotalMips(),1);
			}
			if (energy1<energy2)
				t1=true;
			else if (energy2<energy1)
				t2=true;
			if (level>=hostList1.size())
				level=0;

			
	      
			L2:
			while (i2<vm.size() && level<host.length)
			{
				if (t1)
					level=find_level_odd(level, vm_ids, energy2);
				else if (t2)
					level=find_level_even(level, vm_ids, energy2);
		
					
				if (hostList1.get(level).getVmList().size()==0)
				  {	
				  for (int k=0;k<host[level].length;k++)
					  
				    	if (host[level][k]-I[vm_ids.get(i2)][k]>=0 )
						{
				    		host[level][k]-=I[vm_ids.get(i2)][k];
						    t=true;
							}
						else
						{
							t=false;
							if (hostList1.get(level).getVmList().size()==0)
							  host[level]=host_org[level];
							i2=0;
							level++;
							break;
						}
					  }
				else
					{
					while (hostList1.get(level).getVmList().size()!=0)
					 		  level++;
					continue L2;
					}
				if (!t && level>=host.length)
					{
					level=0;
					continue L2;		
					}
				if (t)
				  i2++;
		}
		
				i2=0;
		
			while (i2<vm.size() && level<host.length)
				{
				if (hostList1.get(level).isSuitableForVm(vm.get(i2)))
					{
		     		hostList1.get(level).vmCreate(vm.get(i2));
					vm.get(i2).setHost(hostList1.get(level));
					vm_allocate[vm_ids.get(i2)]=1;	
					host_filled[level]=1;
					PowerVmAllocationPolicyAbstract.getVmTable().put(vm.get(i2).getUid(), hostList1.get(level));
//					if (host[level][0]==host_org[level][0])
//						for (int k=0;k<host[level].length;k++)
//					    	host[level][k]-=Q.get(vm_ids.get(i2))[k];
					}
//				else
//					{
//					if (level<host.length-1)
//					  level++;
//				   	
//					else
//						level=0;
//					continue Lvl_loop;
//					}
		         i2++;		
		         }
			level++;
		}

//		for (int i=0;i<host.length;i++)
//		{
//			
//			System.out.print("host "+ i + ": ");   
//
//			for (int j=0;j<host[i].length;j++)
//				System.out.print(host[i][j]+" ");
//			System.out.println("  ");   
//
//		}
		return Final_bins;

	}


	
	
	private static ArrayList <int[]> Filled_hosts_classification_vbp() {
		flag_one=new int[I.length];
		ArrayList<double[]> items=new ArrayList<double[]>();
		ArrayList <Vm> vm=new ArrayList <Vm>();
		double[] real_item=new double[d];
		int lg_item;
		ArrayList <int[]> Final_bins=new ArrayList<int[]>();
		int [] temp=new int[req1.length];
		int [] temp1=new int [req1.length];

		temp1=posBins1.get(dynam_pos1[findIndex_class(req1)]);
		Final_bins.add(temp1);
		for (int i=0;i<req1.length;i++)
			temp[i]=req1[i]-temp1[i];


		int index=findIndex_class(temp);

		while (dynam_pos1[index]!=-1 && !temp1.equals(0))
		{
     		temp1=posBins1.get(dynam_pos1[index]);
			Final_bins.add(temp1);
			for (int i=0;i<temp.length;i++)
				temp[i]=temp[i]-temp1[i];

			index=findIndex_class(temp);
		}
		for (int i=0;i<pbind.length;i++)
			if (pbind[i]==index)
				Final_bins.add(posBins1.get(i));

//		System.out.println("Used Bins Items:");
//		int ind=1;
//		for (int[] FB : Final_bins) {
//			System.out.println("Bin " + (ind++)+" :"+Arrays.toString(FB));
//		}
	    int level=0;
		
		for (int i = 0; i< Final_bins.size(); i++)
		{
			items.clear();

			for (int j=0;j<Q_Class1.size();j++)
			{
				if (Final_bins.get(i)[j]!=0)
					{
					lg_item=find_class_classification(j);
					for (int k=0;k<d;k++)
				    	real_item[k]=I[lg_item][k];
					for (int k1=0;k1<Final_bins.get(i)[j];k1++)
				     items.add(Add_item(real_item));
			}
			}
			vm.clear();
			ArrayList<Integer> vm_ids=new ArrayList<Integer>();
			loop:
				for (int i1=0;i1<items.size();i1++)
				{
					jloop:
						for (int j=0;j<I.length;j++)
						{ 
							if (compareItems1(items.get(i1), I[j]) && flag_one[j]!=1)
							{	  vm.add(vmList1.get(j));
							vm_ids.add(j);
							flag_one[j]=1;
							continue loop;
							}
							else
								continue jloop;
						}

				}
			
			boolean t=false;
			int i2=0;
			while (i2<vm.size() && level<host.length)
			{
				for (int k=0;k<host[level].length;k++)
				
					if (host[level][k]-I[vm_ids.get(i2)][k]>=0 )
					//if (host[level][k]-Q.get(vm_ids.get(i2))[k]>=0 && host_org[level][0]==1)
						{
						host[level][k]-=I[vm_ids.get(i2)][k];
						t=true;
							}
						else
						{
							host[level]=host_org[level];
							t=false;
							i2=0;
							//if (level < host_filled.length-1)
							level++;
							break;
							
						}
				if (t)
				  i2++;
				
			}
			i2=0;
			
			
			host_filled[level]=1;
			host_ids.add(level);
			Lvl_loop:
			
			while (i2<vm.size() && level<host.length)
				{
				
				//if (hostList1.get(level).isSuitableForVm(vm.get(i2)) && host_org[level][0]==1)
				if (hostList1.get(level).isSuitableForVm(vm.get(i2)))
					{
		     		hostList1.get(level).vmCreate(vm.get(i2));
					vm.get(i2).setHost(hostList1.get(level));
					vm_allocate[vm_ids.get(i2)]=1;	
					PowerVmAllocationPolicyAbstract.getVmTable().put(vm.get(i2).getUid(), hostList1.get(level));
					
					
					}
				else //if (level < host_filled.length-1)
					{
				    level++;
				    
					continue Lvl_loop;
					}
		         i2++;		
		         }
			if (level < host_filled.length-1)
				level++;
		}
		
		
//		for (int i=0;i<host.length;i++)
//		{
//			
//			System.out.print("host "+ i + ": ");   
//
//			for (int j=0;j<host[i].length;j++)
//				System.out.print(host[i][j]+" ");
//			System.out.println("  ");   
//
//		}
		return Final_bins;

	}

	

	private static double[] Add_item(double[] real_item) {
		double[] items=new double[real_item.length];
		for (int k=0;k<real_item.length;k++)
		      items[k]=real_item[k];
		return items;
	}



	public static void normalize() {
			
	
		double[] max_org=new double[d];
		max_org[0]=1;
		//max_org[1]=1;
		//max_org[2]=1;
		vmls_ids=new int[vmList.length];
		for (int i=0;i<hostList.length;i++)
			for (int j=0;j<d;j++)
				if (hostList[i][j]>max_org[j])
					max_org[j]=hostList[i][j];

					
				
		

		for (int i=0;i<hostList.length;i++)
			for (int j=0;j<d;j++)
					host[i][j]=hostList[i][j]/max_org[j];
				

		for (int i=0;i<hostList.length;i++)
			for (int j=0;j<d;j++)
					host[i][j]+=0.375;
	
		
		
		for (int i=0;i<vmList1.size();i++)
			{
			Cloudlet cl=CustomRunner.cloudletList.get(vmList1.get(i).getId());
			double cpu= cl.getUtilizationOfCpu(0);//*vmList[i][0]/100;
		//	I[i][0]=cpu;
//			for (int j=0;j<d;j++)
//					I[i][j]=(vmList[i][j]/max_org[j]);
//			}
			I[i][0]=cpu;
//			for (int j=1;j<d;j++)
//					I[i][j]=(vmList[i][j]/max_org[j]);
//			}
			}
		host_org=new double[host.length][d];

		
		for (int i=0;i<host.length;i++)
			for (int j=0;j<d;j++)
     		   host_real[i][j]=host[i][j];
//		for (int i=0;i<host.length;i++)
//		{
//			if (host[i][0]+epsilon<1)
//				host[i][0]+=0.262218045112782;
//			else
//				host[i][0]+=epsilon;
//			for (int j=1;j<d-1;j++)
//				{
//				
//				host[i][j]+=epsilon;
//
//				}
//		}
		for (int i=0;i<host.length;i++)
			{
			for (int j=0;j<d;j++)
			
				host_org[i][j]=host[i][j];
			//host_org[i][d-1]=1;
			}
		
	}


	
	
	
	
	//******recursive bin packing 	
	
	
	public static int Dynamic_Bins(int level,int [] requests)
	{ 
		
		int ind=findIndex(requests);
		if (dynam_array[ind]!=0)
		{
			
			return dynam_array[ind]; 
			
		}
		else
		{

			int min=Integer.MAX_VALUE;
			int result=0;
	
			int [] t1=new int[requests.length];

			posLoop:
				for(int j=1;j<posBins.size();j++) {
					
				int [] pb=posBins.get(j);

					for(int m=0;m<requests.length;m++) {
						if (requests[m]-pb[m]<0)
							continue posLoop;
						t1[m]=requests[m]-pb[m];

					}
				
                  result=Dynamic_Bins(level+1,t1);
					
					if(result<min) {
						min=result;
						dynam_pos1[ind]=j;
						
					}
					//dynam_array[ind]=1+min;	
					}          
				
			dynam_array[ind]=1+min;	
			return (1+min);
		}	


	}

	public static int Dynamic_Bins_OPT(int level,int [] requests)
	{ 
		
		int ind=findIndex_class(requests);
		if (dynam_array[ind]!=0)
		{
			
			return dynam_array[ind]; 
			
		}
		else
		{

			int min=Integer.MAX_VALUE;
			int result=0;
	
			int [] t1=new int[requests.length];

			posLoop:
				for(int j=1;j<posBins1.size();j++) {
					
				int [] pb=posBins1.get(j);

					for(int m=0;m<requests.length;m++) {
						if (requests[m]-pb[m]<0)
							continue posLoop;
						t1[m]=requests[m]-pb[m];

					}
				
                  result=Dynamic_Bins_OPT(level+1,t1);
					
					if(result<min) {
						min=result;
						dynam_pos1[ind]=j;
						
					}
					//dynam_array[ind]=1+min;	
					}          
				
			dynam_array[ind]=1+min;	
			return (1+min);
		}	


	}

	
	
	public static double Dynamic_Bins_energy(int level,int [] requests)
	{ 
		
		int ind=findIndex(requests);
		if (dynam_array_energy[ind]!=0)
		{
			
			return dynam_array_energy[ind]; 
			
		}
		else
		{

			double min=Double.MAX_VALUE;
			double result=0;
	
			int [] t1=new int[requests.length];

			posLoop:
				for(int j=1;j<posBins.size();j++) {
					
				int [] pb=posBins.get(j);

					for(int m=0;m<requests.length;m++) {
						if (requests[m]-pb[m]<0)
							continue posLoop;
						t1[m]=requests[m]-pb[m];

					}
				
                  result=Dynamic_Bins_energy(level+1,t1)+bins_energy[j];
					
					if(result<min) {
						min=result;
						dynam_pos1[ind]=j;
						dynam_pos1_energy[ind]=j;
						
						
					}
					
					}          
			dynam_array_energy[ind]=min;		
			
			return (dynam_array_energy[ind]);
		}	


	}


	public static double Dynamic_Bins_energy_class(int level,int [] requests)
	{ 
		
		int ind=findIndex_class(requests);
		if (dynam_array_energy[ind]!=0)
		{
			
			return dynam_array_energy[ind]; 
			
		}
		else
		{

			double min=Double.MAX_VALUE;
			double result=0;
	
			int [] t1=new int[requests.length];

			posLoop:
				for(int j=1;j<posBins1.size();j++) {
					
				int [] pb=posBins1.get(j);

					for(int m=0;m<requests.length;m++) {
						if (requests[m]-pb[m]<0)
							continue posLoop;
						t1[m]=requests[m]-pb[m];

					}
				
                  result=Dynamic_Bins_energy_class(level+1,t1)+bins_energy[j];
					
					if(result<min) {
						min=result;
						dynam_pos1[ind]=j;
						dynam_pos1_energy[ind]=j;
						
						
					}
					
					}          
			dynam_array_energy[ind]=min;		
			
			return (dynam_array_energy[ind]);
		}	


	}

	
	
	
	public static int numberofhosts() {
		int count=0;
		for (int i=0;i<hostList1.size();i++)
			if (hostList1.get(i).getVmList().size()!=0)
				count++;
		return count;
	}






	public static boolean host_fit2(int[] pb, int level) {
		double[] temp=new double[d];
		flag_one=new int[I.length];

		
		for (int i=0;i<pb.length;i++)
			if (pb[i]!=0)
			{				
				for (int j=0;j<d;j++)
				{		
					temp[j]+=pb[i]*class_items[i][j];
					
				}
				
			}
		
		for (int j1=0;j1<d;j1++)
		{		
			if (temp[j1]>host[level][j1])
				return false;
		}
		host_filled[level]=1;
		return true;
		
}


	public static boolean host_fit(int[] pb, double[] hs,int level) {
		boolean flag=false;
		double[] temp=new double[d];

		int non_zero=0;
		for (int i=0;i<pb.length;i++)
			if (pb[i]!=0)
			{
				non_zero++;
				for (int j=0;j<d;j++)
				{		
					temp[j]+=pb[i]*class_items[i][j];
					

				}
			}
		
		for (int j1=0;j1<d;j1++)
		{		
			if (temp[j1]>hs[j1])
				return false;
			
		}
		
		
		ArrayList<double[]> items=new ArrayList<double[]>();
		ArrayList <Vm> vm=new ArrayList <Vm>();
		double[] real_item=new double[d];

		
		int lg_item;
		if (flag)
		{
			for (int i=0;i<pb.length;i++)
				if (pb[i]!=0)
				{
					lg_item=find_class(i);
					for (int k=0;k<d;k++)
						real_item[k]=large.get(lg_item)[k];
					items.add(real_item);
				}

			int [] index=new int [non_zero];
			loop:
				for (int i=0;i<items.size();i++)
				{
					jloop:
						for (int j=0;j<vmList1.size();j++)
						{ 
							if (compareItems1(items.get(i), I[j]) && flag_one[j]!=1)
							{	  vm.add(vmList1.get(j));
							flag_one[j]=1;
							index[i]=j;  
							continue loop;
							}
							else
								continue jloop;
						}

				}
			

			if (vm.size()==0)
				return true;

			for (int i=0;i<vm.size();i++)
				if (hostList1.get(level).isSuitableForVm(vm.get(i))) //&&  hostList1.get(level).vmCreate(vm.get(i)))
					{
						return true;

					}
					else
					{
						return false;
					}	
				}

		return flag;

	}




	public static double getpower(int level,int[] pb)
	{
		ArrayList<double[]> items=new ArrayList<double[]>();
		ArrayList <Vm> vm=new ArrayList <Vm>();
		double[] real_item=new double[d];
		double power=0;
		int lg_item;
		for (int i=0;i<pb.length;i++)
			if (pb[i]==1)
			{
				lg_item=find_class(i);
				for (int k=0;k<d;k++)
					real_item[k]=large.get(lg_item)[k];
				items.add(real_item);
			}



		loop:
			for (int i=0;i<items.size();i++)
			{
				for (int j=0;j<vmList1.size();j++)
				{ 
					if (compareItems1(items.get(i), I[j]))

					{

						vm.add(vmList1.get(j));
						continue loop;
					}
				}

			}

			for (int i=0;i<vm.size();i++)
				if (hostList1.get(level).isSuitableForVm(vm.get(i)))
				{
					power= PowerVmAllocationPolicyMigrationAbstract. getPowerAfterAllocation(hostList1.get(level), vm.get(i));
					if (power!=-1)
						hostList1.get(level).vmCreate(vm.get(i));
				}


			return power;	

	}



	
	
	static void sethost_null(int[] sml_flg)
	{
		int index=0;
		Vm vm=null;
		for (int i=0;i<small.size();i++)
	    	for (int j=0;j<vmList.length;j++)
		{ 
			if (compareItems1(small.get(i), I[j]))
				{
				sml_flg[j]=0;
				vm=vmList1.get(j);
				if (vm.getHost()!= null)
				  {
					index=vm.getHost().getId();
				  	hostList1.get(index).vmDestroy(vm);
				    vm.setHost(null);
				    PowerVmAllocationPolicyAbstract.getVmTable().remove(vm.getUid(), hostList1.get(index));
				}
				}
		}
	}
	    
		




	static void sethost_sml(double [] sm,int  level)
	{
		int index=0;
		Vm vm=null;
		
		for (int j=0;j<vmList.length;j++)
		{ 
			if (compareItems1(sm, I[j]) && sml_flg[j]!=1)
			{
				sml_flg[j]=1;
				vm=vmList1.get(j);
				if (vm.getId()==183)
					System.out.print(vm);
				index=j;
				flag_one[j]=1;
				break;
			}
		}
		
				
		hostList1.get(level).vmCreate(vm);
		vm.setHost(hostList1.get(level));
		for (int i=0;i<d;i++)
		{
	      	//host_bins.get(host_ind)[i]-=sm[i];
	      	host[level][i]=host[level][i]-sm[i];
		}	
	    PowerVmAllocationPolicyAbstract.getVmTable().put(vm.getUid(), hostList1.get(level));
				
		}
		


	static void sethost_smlCopy(double [] sm,int  level)
	{
		int index=0;
		Vm vm=null;
//		if (small.size()==1)
//			vm=DatacenterBroker.vmList_items.get(0);
//		else
//		{
		for (int j=0;j<vmList_copy.length;j++)
		{ 
			if (compareItems1(sm, I[j]) && sml_flg[j]!=1)
			{
				sml_flg[j]=1;
				vm=vmList1_copy.get(j);
				index=j;
				flag_one[j]=1;
				break;
			}
		}
		//}
				
		hostList1_copy.get(level).vmCreate(vm);
		vm.setHost(hostList1_copy.get(level));
		for (int i=0;i<d;i++)
		{
	      	//host_bins.get(host_ind)[i]-=sm[i];
	      	host_copy[level][i]=host_copy[level][i]-sm[i];
		}	
	    PowerVmAllocationPolicyAbstract.getVmTable1().put(vm.getUid(), hostList1_copy.get(level));
	    if (PowerVmAllocationPolicyMigrationAbstract.vmList_items.contains(vm))
		   hostForDP.add(hostList1_copy.get(level));
		vmForDP.add(vm);
		}
		
	
	
	
	
	
	

	public static int Dynamic_Bins4(int [] requests)
	{ 

		int ind=findIndex1(requests);
		if (dynam_array1[ind]!=0)
			return dynam_array1[ind];

		else
		{

			int min=Integer.MAX_VALUE;
			int result=0;
			int [] t1=new int[requests.length];

			posLoop:
				for(int j=1;j<posBins1.size();j++) {
					int [] pb=posBins1.get(j);
					//if posbins[j] fits in host
					for(int m=0;m<requests.length;m++) {
						if (requests[m]-pb[m]<0)
							continue posLoop;
						t1[m]=requests[m]-pb[m];


					}

					result=Dynamic_Bins4(t1);
					//int t1_ind=findIndex(t1);
					if(result<min) {
						min=result;
						dynam_pos1[ind]=j;
						//dynam_pos[ind]=t1;

					}


				}

			dynam_array1[ind]=1+min;		
			//

			return (1+min);
		}
	}










	public static double[][] Filled_bins1()
	{
		ArrayList <int[]> Final_bins=new ArrayList<int[]>();
		int [] temp=new int[requests.length];
		int [] temp1=new int [requests.length];

		temp1=posBins.get(dynam_pos1[findIndex(requests)]);
		Final_bins.add(temp1);
		for (int i=0;i<requests.length;i++)
			temp[i]=requests[i]-temp1[i];


		int index=findIndex(temp);

		while (dynam_pos1[index]!=-1)
		{

			//		    	Final_bins.add(dynam_pos[index]);
			//				index=findIndex(dynam_pos[index]);
			temp1=posBins.get(dynam_pos1[index]);
			Final_bins.add(temp1);
			for (int i=0;i<temp.length;i++)
				temp[i]=temp[i]-temp1[i];

			index=findIndex(temp);
		}
		for (int i=0;i<pbind.length;i++)
			if (pbind[i]==index)
				Final_bins.add(posBins.get(i));



//		System.out.println("Used Bins Items:");
//		int ind=1;
//		for (int[] FB : Final_bins) {
//			System.out.println("Bin " + (ind++)+" :"+Arrays.toString(FB));
//		}




		int m= dynam_array[dynam_array.length-1];
		double size[]=new double[d];
		double [][] Used_bins_cap=new double[Final_bins.size()][d];


		for (int i = 0; i< Final_bins.size(); i++)
		{

			for (int j=0;j<round1.size();j++)
			{
				if (Final_bins.get(i)[j]!=0)
					size=class_items[j];       
				for (int k1=0;k1<d;k1++) 
				{
					Used_bins_cap[i][k1]+=(size[k1]*Final_bins.get(i)[j]);

				}

			}
		}


		//System.out.println("The bins caps before real values");

//		for (int i=0;i<Used_bins_cap.length;i++)
//		{
//			System.out.print("Bin "+ i + ": ");   
//
//			for (int j=0;j<Used_bins_cap[i].length;j++)
//				System.out.print(Used_bins_cap[i][j]+" ");
//			System.out.println("  ");   
//
//		}


//		double[][] US_BC=null;
//
//		boolean res;
//		US_BC=new double[guss_m][d];
//		for (int i=0;i<Used_bins_cap.length;i++)
//			for (int j=0;j<d;j++)
//				US_BC[i][j]=Used_bins_cap[i][j];
//		res=solvemethod1(guss_m);
		return Used_bins_cap;				
	}




	private static void real_values(ArrayList<int[]> Final_bins, double[][] Used_bins_cap) {

		int lg_item=0,ind=0,count=0;		
		for (int i=0;i<large_flag.length;i++)
			large_flag[i]=0;

		for (int[] bin:Final_bins) {
			count=0;
			for (int i=0;i<bin.length;i++)
				if (bin[i]!=0)
				{
					for (int j=0;j<bin[i];j++)
					{
						lg_item=find_class(i);
						for (int k=0;k<d;k++)
						{ if (count!=0)
							Used_bins_cap[ind][k]+=large.get(lg_item)[k];
						else
						{
							Used_bins_cap[ind][k]=large.get(lg_item)[k];


						}
						}

					}
					count++;
				}
			ind++;

		}


//		System.out.println("The bins caps After real values");
//
//		for (int i=0;i<Used_bins_cap.length;i++)
//		{
//			System.out.print("Bin "+ i + ": ");   
//
//			for (int j=0;j<Used_bins_cap[i].length;j++)
//				System.out.print(Used_bins_cap[i][j]+" ");
//			System.out.println("  ");   
//
//		}


	}


	public static int find_class(int index)
	{

		for (int i=0;i<larg_ind1.length;i++)
			if (larg_ind1[i]==index && large_flag[i]!=1)
			{
				large_flag[i]=1;
				return i;

			}


		return 0;

	}

	public static int find_class_small(int index)
	{

		for (int i=0;i<larg_ind1.length;i++)
			if (larg_ind1[i]==index && large_flag[i]!=1)
			{
				large_flag[i]=1;
				return i;

			}


		return 0;

	}
	

	public static int find_class_classification(int index)
	{

		large_flag=new int[I.length];
		for (int i=0;i<I.length;i++)
			if (larg_ind[i]==index && large_flag[i]!=1)
			{
				large_flag[i]=1;
				return i;

			}


		return 0;

	}






	public static boolean compareItems1(double[] item1, double[] item2) {
		for (int i = 0; i < item1.length; i++) {
			if (item1[i] != item2[i])
				return false;
		}
		return true;
	}





	public static void Filled_bins()
	{
		ArrayList <int[]> Final_bins=new ArrayList<int[]>();
		Final_bins.add(dynam_pos[dynam_array.length-1]);
		int index=findIndex(dynam_pos[dynam_array.length-1]);


		while (dynam_pos[index][0]!=-1)
		{
			Final_bins.add(dynam_pos[index]);
			index=findIndex(dynam_pos[index]);
		}
		for (int i=0;i<pbind.length;i++)
			if (pbind[i]==index)
				Final_bins.add(posBins.get(i));


		int[][] Used_bins=new int [Final_bins.size()][round1.size()];
		for (int i=0;i<requests.length;i++)
			Used_bins[0][i]=Final_bins.get(Final_bins.size()-1)[i];
		int k=1;
		for (int i=Final_bins.size()-2;i>=0;i--)
		{
			for (int j=0;j<requests.length;j++)
			{ if (i!=0) {
				Used_bins[k][j]=(Final_bins.get(i-1)[j]-Final_bins.get(i)[j]);
			}

			else {
				Used_bins[Used_bins.length-1][j]=(requests[j]-Final_bins.get(0)[j]);

			}
			}
			k++;
		}

//		System.out.println("Used Bins Items:");
//
//
//
//
//		for (int i=0;i<Used_bins.length;i++)
//		{
//			System.out.print("Bin  "+(i+1)+": ");
//			for (int j=0;j<round1.size();j++)
//				System.out.print(Used_bins[i][j]+" ");
//
//			System.out.println(" ");
//		}


		int m= dynam_array[dynam_array.length-1];
		double size[]=new double[d];
		double [][] Used_bins_cap=new double[Used_bins.length][d];
		for (int i = 0; i< Used_bins.length; i++)
		{

			for (int j=0;j<Used_bins[i].length;j++)
			{
				if (Used_bins[i][j]!=0)
					size=round1.get(j).get(0);       
				for (int k1=0;k1<d;k1++) 
					Used_bins_cap[i][k1]+=(size[k1]*Used_bins[i][j]);
			}
		}

//		System.out.println("Used Bins Capacities:");
//		for (int i=0;i<Used_bins_cap.length;i++)
//		{	System.out.print("Bin  "+(i+1)+": ");
//		for (int j=0;j<d;j++)
//			System.out.print(Used_bins_cap[i][j]+" ");
//
//		System.out.println(" ");
//		}



		//solvemethod1(m,Used_bins_cap);


	}



	public static int findIndex1(int [] t1)
	{
		int res=t1[0];
		for (int k=1;k<req1.length;k++) {
			res=(res*(req1[k]+1)+t1[k]);
		}

		return res;
	}



	public static int findIndex(int [] t1)
	{
		int res=t1[0];
		for (int k=1;k<requests.length;k++) {
			res=(res*(requests[k]+1)+t1[k]);
		}

		
		return res;
	}

	
	
	
	public static int findIndex_class(int [] t1)
	{
		int res=t1[0];
		for (int k=1;k<req1.length;k++) {
			res=(res*(req1[k]+1)+t1[k]);
		}

		
		return res;
	}

	public static void arrayind(int[] counters,int level, ArrayList<int[]> bins) {


		if(level == counters.length)
		{   
			bins.add(Arrays.copyOf(counters, counters.length));

		}
		else 
			for (counters[level] = 0; counters[level] <= requests[level]; counters[level]++) {
				arrayind(counters, level + 1, bins);
			}


	}


	//PAcking items with their original value


	public static int OPT_energy_small_large(double[][] pm_rel_cap,double[][] pm_rel)
	{

		host_filled=new int[host.length];
		boolean t1=true;
		larg_ind=new int [I.length];
		sml_flg=new int[vmList.length];
		seperate_large_small();
		
		//ArrayList <ArrayList<double[]>> Q_Class1 = new ArrayList<ArrayList<double[]>>();		itemLoop:
		itemLoop:	
		for (int i = 0; i < large.size(); i++) {
				for (ArrayList<double[]> row : Q_Class1) {
					if (compareItems1(row.get(0), large.get(i))) {
						row.add(large.get(i));
						larg_ind[i]=Q_Class1.indexOf(row);
						continue itemLoop;
					}
				}
				ArrayList<double[]> qNewRow = new ArrayList<double[]>();
				qNewRow.add(large.get(i));
				Q_Class1.add(qNewRow);
				larg_ind[i]=Q_Class1.indexOf(qNewRow);


			}

		for (ArrayList<double[]> row : Q_Class1) {
			for (double[] cell : row) {
				System.out.println(Arrays.toString(cell));
			}
			System.out.println("***********************");
		}
		System.out.println(t1);



		class_itm=new double[Q_Class1.size()][d];
		req1=new int[Q_Class1.size()];
	
		for (int i=0;i<Q_Class1.size();i++)
		{   
			req1[i]=Q_Class1.get(i).size();
			class_itm[i]=Q_Class1.get(i).get(0);
		}




		int Rl=Q_Class1.size();
		int[] counters = new int[Rl];

		double [] size=new double [d];
		double [] size1=new double [d];
		posBins1 = new ArrayList<int[]>();;
		getBins2(size,counters,0,posBins1);
		//getBins3(size1,counters,0,posBins2);	
		
		
		long  dynam_array_length=1;
		for (int i=0;i<req1.length;i++)
			dynam_array_length*=(req1[i]+1);
		dynam_array=new int [(int) dynam_array_length];
		dynam_array_energy=new double [(int) dynam_array_length];
		dynam_pos=new int [(int) dynam_array_length][Q_Class1.size()];
		dynam_pos1=new int [(int) dynam_array_length];
		dynam_pos1_energy=new double[(int) dynam_array_length];
		pbind=new int [posBins1.size()];
		level_list=new int [hostList1.size()];
		int [] temp=new int [req1.length];
		for (int i=0;i<temp.length;i++)
			temp[i]=-1;
		bins_energy=new double[posBins1.size()];
		bins_id=new double[posBins1.size()];
		double sum_of_util=0;double min_energy=Double.MAX_VALUE;
		double energy_temp=0;
		
		
		
		double a=compute_energy(0.9022556390977444,1);
		
		double a1=compute_energy(0.8602150537634409,0);
		
		
		
		
		
		for(int i=1;i<posBins1.size();i++)
		{
			min_energy=Double.MAX_VALUE;
			sum_of_util=0;
			for (int j=0;j<req1.length;j++)
			if (posBins1.get(i)[j]!=0)
				sum_of_util+=(posBins1.get(i)[j]*class_itm[j][0]);
			for (int k=0;k<pm_rel.length;k++)
			{
				
				if (sum_of_util>pm_rel_cap[k][0])
						continue;
				//energy_temp=(sum_of_util/pm_rel_cap[k][0])*pm_rel[k][0]+pm_rel[k][1];
				   energy_temp= compute_energy(sum_of_util/pm_rel_cap[k][0],k);
				    if (energy_temp<min_energy)
				    	{
				    	min_energy=energy_temp;
				    	bins_id[i]=k;
				    	}
					            
			}
			 bins_energy[i]=min_energy;
				
		}
		for (int i=1;i<posBins1.size();i++)//remove
		{
			int index=findIndex_class(posBins1.get(i));
			pbind[i]=index;
			dynam_array[index]=1;
			dynam_array_energy[index]=bins_energy[i];
			dynam_pos1[index]=-1;
			for (int k=0;k<Q_Class1.size();k++)
			{
				dynam_pos[index][k]=-1;

			}
		}
		
		
		double result1=0;
	////****************Dynamic Bin packing**************************************//
		
		result1=Dynamic_Bins_energy_class(0,req1);
		
		System.out.println("Number of Bins:"+ result1);
		
		ArrayList <int[]> Final_bins=Filled_hosts_classification();
		int count0=0;int count1=0;
		
		for (int k=0;k<hostList1.size();k++)
		{
			sum_of_util=0;
			if (hostList1.get(k).getVmList().size()!=0)
			{	if (k%2==0)
				 count0++;
			    else
			    	count1++;
			}
 		}

		System.out.println("Number of Bins:"+ count0);
		System.out.println("Number of Bins:"+ count1);

		
		int l=1;int u=vmList.length;
		int m=(l+u)/2;
		guss_m=(int) (m+Math.ceil((epsilon*m)/2));

		
		count=0;
		System.out.println("Number of Bins:"+ result1);
		
		double sum=0;double sum_of_energy=0;
		for (int i=0;i<hostList1.size();i++)
			{
			sum=0;
			if (host_filled[i]==1)
			{
				count++;
				List<Vm> vml=hostList1.get(i).getVmList();
				for (Vm vm:vml)
				  sum+=hostList1.get(i).getTotalAllocatedMipsForVm(vm);
				sum/=hostList1.get(i).getTotalMips();
		        sum_of_energy+=compute_energy(sum,i%2);
			}
		
			}
		while (Final_bins.size()!=guss_m)
		{
			if (Final_bins.size()>m)
			  {l=m+1;
			  m=(l+u)/2;
			  guss_m=m;
			  }
			else
			{
				u=m;
				m=(l+u)/2;
				guss_m=m;
			}
		}
		double[] host_temp=new double [d];
		
		for (int i=0;i<host_filled.length;i++)
			if (host_filled[i]!=0)
			{
				for (int j=0;j<d;j++)
			    	host_temp[j]=host_org[i][j]-host[i][j];
				host_ids.add(i);
				host_bins.add(host_temp);
				host_temp=new double [d];
			}

//		for (int i=0;i<host.length;i++)
//		{
//			
//			System.out.print("host "+ i + ": ");   
//
//			for (int j=0;j<host[i].length;j++)
//				System.out.print(host[i][j]+" ");
//			System.out.println("  ");   
//
//		}
		
			
			
			the_last_host=host_ids.get(host_ids.size()-1);
			the_last_host++;
			//the_last_host=0;
			int t=0;
			result=Final_bins.size();
			ArrayList <Integer> ind_m=new ArrayList<Integer>();
			boolean flag=solvemethod1(guss_m,pm_rel_cap);
			int count=0;int temp1=0;int temp2=0;
			if (!flag)
				{
				l=guss_m+1;
				u=vmList.length;
				m=(l+u)/2;
				while (m>result+1)
				{
				ind_m.add(m);	
				u=m;
				m=(l+u)/2;
				}
				count=ind_m.size()-1;
				while (!flag)
				{
				if (count==ind_m.size()-1)
					temp1=ind_m.get(count);
				else
					temp1=ind_m.get(count)-ind_m.get(count+1);
				if (temp1>host_bins.size())
					t= temp1-host_bins.size();
				else
					t= temp1;
				for (int i=0;i<=t;i++)
				{
					host_temp=new double [d];
					host_bins.add(host_temp);
					if(the_last_host%2==1)
						  host_ids.add(the_last_host++);
						else
						{
							host_ids.add(++the_last_host);
							the_last_host++;
						}
									
				}
				flag=solvemethod1(ind_m.get(count),pm_rel_cap);
				count--;
				}
								}
	
	    List<Vm> vms_list = null;
		
		energy_temp=0;
		
			
			
			for (int k=0;k<hostList1.size();k++)
			{
				sum_of_util=0;
				if (hostList1.get(k).getVmList().size()!=0)
				{	vms_list=hostList1.get(k).getVmList();
					for (Vm vm:vms_list)
					 sum_of_util+=vm.getMips();
				   energy_temp+=(sum_of_util/pm_rel_cap[k%2][0])*pm_rel[k%2][0]+pm_rel[k%2][1];
				}
     		}
	
		System.out.println(energy_temp);
		change_host();
		return result;		   

		
		}
		
	
	
	public static double compute_energy(double energy,int k) {
		
    	if (k>1)
	    	k=k%2;
		double[][] he= {{86,89.4,92.6,96,99.5,102,106,108,112,114,117},{93.7, 97.0, 101.0, 105.0, 110.0, 116.0, 121.0, 125.0, 129.0, 133.0, 135.0}};
		int [] darsad= {0,10,20,30,40,50,60,70,80,90,100};
		double final_energy=0;
	    energy*=100;
		for (int i=1;i<11;i++)
		{
			if (energy>darsad[i])
				continue;
		   final_energy= ((energy-darsad[i-1])*he[k][i]+(darsad[i]-energy)*he[k][i-1])/10;
		   return final_energy;
		}		
			return 0;
	}


private static double compute_energy1(double energy,int k) {
		
    	if (k>1)
	    	k=k%2;
		double[][] he= {{86,89.4,92.6,96,99.5,102,106,108,112,114,117},{93.7, 97.0, 101.0, 105.0, 110.0, 116.0, 121.0, 125.0, 129.0, 133.0, 135.0}};
		int [] darsad= {0,10,20,30,40,50,60,70,80,90,100};
		double final_energy=0;
	    energy=(energy/hostList1.get(k).getTotalMips())*100;
		for (int i=0;i<11;i++)
		{
			if (energy>darsad[i])
				continue;
		   final_energy= ((energy-darsad[i-1])*he[k][i]+(darsad[i]-energy)*he[k][i-1])/10;
		   return final_energy;
		}		
			return 0;
	}






	public static int OPT_vbp_small_large(double[][] pm_rel_cap)
	{

		host_filled=new int[host.length];
		boolean t1=true;
		larg_ind=new int [I.length];
		sml_flg=new int[vmList.length];
		seperate_large_small();
		
		//ArrayList <ArrayList<double[]>> Q_Class1 = new ArrayList<ArrayList<double[]>>();		itemLoop:
		itemLoop:	
		for (int i = 0; i < large.size(); i++) {
				for (ArrayList<double[]> row : Q_Class1) {
					if (compareItems1(row.get(0), large.get(i))) {
						row.add(large.get(i));
						larg_ind[i]=Q_Class1.indexOf(row);
						continue itemLoop;
					}
				}
				ArrayList<double[]> qNewRow = new ArrayList<double[]>();
				qNewRow.add(large.get(i));
				Q_Class1.add(qNewRow);
				larg_ind[i]=Q_Class1.indexOf(qNewRow);


			}

		for (ArrayList<double[]> row : Q_Class1) {
			for (double[] cell : row) {
				System.out.println(Arrays.toString(cell));
			}
			System.out.println("***********************");
		}
		System.out.println(t1);



		class_itm=new double[Q_Class1.size()][d];
		req1=new int[Q_Class1.size()];
	
		for (int i=0;i<Q_Class1.size();i++)
		{   
			req1[i]=Q_Class1.get(i).size();
			class_itm[i]=Q_Class1.get(i).get(0);
		}




		int Rl=Q_Class1.size();
		int[] counters = new int[Rl];

		double [] size=new double [d];
		double [] size1=new double [d];
		posBins1 = new ArrayList<int[]>();;
		getBins2(size,counters,0,posBins1);
		//getBins3(size1,counters,0,posBins2);	
		
		
		long  dynam_array_length=1;
		for (int i=0;i<req1.length;i++)
			dynam_array_length*=(req1[i]+1);
		dynam_array=new int [(int) dynam_array_length];
		dynam_array_energy=new double [(int) dynam_array_length];
		dynam_pos=new int [(int) dynam_array_length][Q_Class1.size()];
		dynam_pos1=new int [(int) dynam_array_length];
		dynam_pos1_energy=new double[(int) dynam_array_length];
		pbind=new int [posBins1.size()];
		level_list=new int [hostList1.size()];
		int [] temp=new int [req1.length];
		for (int i=0;i<temp.length;i++)
			temp[i]=-1;
		
				
		
		for (int i=1;i<posBins1.size();i++)//remove
		{
			int index=findIndex_class(posBins1.get(i));
			pbind[i]=index;
			dynam_array[index]=1;
			dynam_pos1[index]=-1;
			for (int k=0;k<Q_Class1.size();k++)
			{
				dynam_pos[index][k]=-1;

			}
		}
		
		
		double result1=0;
	////****************Dynamic Bin packing**************************************//
		
		result1=Dynamic_Bins_OPT(0,req1);
		
		System.out.println("Number of Bins:"+ result1);
		
		ArrayList <int[]> Final_bins=Filled_hosts_classification_vbp();
		int l=1;int u=vmList.length;
		int m=(l+u)/2;
		guss_m=(int) (m+Math.ceil((epsilon*m)/2));

		
		
		System.out.println("Number of Bins:"+ result1);
		
		
		while (Final_bins.size()!=guss_m)
		{
			if (Final_bins.size()>m)
			  {l=m+1;
			  m=(l+u)/2;
			  guss_m=m;
			  }
			else
			{
				u=m;
				m=(l+u)/2;
				guss_m=m;
			}
		}
		double[] host_temp=new double [d];
		
		for (int i=0;i<host_filled.length;i++)
			if (host_filled[i]!=0)
			{
				for (int j=0;j<d;j++)
			    	host_temp[j]=host_org[i][j]-host[i][j];
				//host_ids.add(i);
				host_bins.add(host_temp);
				host_temp=new double [d];
			}
		for (int i=0;i<host.length;i++)
		{
			
			System.out.print("host "+ i + ": ");   

			for (int j=0;j<host[i].length;j++)
				System.out.print(host[i][j]+" ");
			System.out.println("  ");   

		}
		
			
			
		//	the_last_host=host_ids.get(host_ids.size()-1);
			
		//the_last_host=0;
			
			ArrayList <Integer> ind_m=new ArrayList<Integer>();
			boolean flag=solvemethod1(guss_m,pm_rel_cap);
			int count=0;int temp1=0;int temp2=0;
			if (!flag)
				{
				l=guss_m+1;
				u=vmList.length;
				m=(l+u)/2;
				while (m>result+1)
				{
				ind_m.add(m);	
				u=m;
				m=(l+u)/2;
				}
				count=ind_m.size()-1;
				while (!flag)
				{
				if (count==ind_m.size()-1)
					temp1=ind_m.get(count);
				else
					temp1=ind_m.get(count+1)-ind_m.get(count);
				for (int i=0;i<=temp1-result;i++)
				{
					host_temp=new double [d];
					host_bins.add(host_temp);
					host_ids.add(-1);
					
				}
				flag=solvemethod1(ind_m.get(count),pm_rel_cap);
				count--;
				}
				}

		return result;		   

		
		}

	
	
		
		
	public static int OPT_energy(double[][] pm_rel_cap,double[][] pm_rel)
	{

		host_filled=new int[host.length];
		boolean t1=true;
		larg_ind=new int [I.length];
		
		
		//ArrayList <ArrayList<double[]>> Q_Class1 = new ArrayList<ArrayList<double[]>>();		itemLoop:
		itemLoop:	
		for (int i = 0; i < I.length; i++) {
				for (ArrayList<double[]> row : Q_Class1) {
					if (compareItems1(row.get(0), I[i])) {
						row.add(I[i]);
						larg_ind[i]=Q_Class1.indexOf(row);
						continue itemLoop;
					}
				}
				ArrayList<double[]> qNewRow = new ArrayList<double[]>();
				qNewRow.add(I[i]);
				Q_Class1.add(qNewRow);
				larg_ind[i]=Q_Class1.indexOf(qNewRow);


			}

		for (ArrayList<double[]> row : Q_Class1) {
			for (double[] cell : row) {
				System.out.println(Arrays.toString(cell));
			}
			System.out.println("***********************");
		}
		System.out.println(t1);



		class_itm=new double[Q_Class1.size()][d];
		req1=new int[Q_Class1.size()];
	
		for (int i=0;i<Q_Class1.size();i++)
		{   
			req1[i]=Q_Class1.get(i).size();
			class_itm[i]=Q_Class1.get(i).get(0);
		}




		int Rl=Q_Class1.size();
		int[] counters = new int[Rl];

		double [] size=new double [d];
		double [] size1=new double [d];
		posBins1 = new ArrayList<int[]>();;
		getBins2(size,counters,0,posBins1);
		//getBins3(size1,counters,0,posBins2);	
		
		
		long  dynam_array_length=1;
		for (int i=0;i<req1.length;i++)
			dynam_array_length*=(req1[i]+1);
		dynam_array=new int [(int) dynam_array_length];
		dynam_array_energy=new double [(int) dynam_array_length];
		dynam_pos=new int [(int) dynam_array_length][Q_Class1.size()];
		dynam_pos1=new int [(int) dynam_array_length];
		dynam_pos1_energy=new double[(int) dynam_array_length];
		pbind=new int [posBins1.size()];
		level_list=new int [hostList1.size()];
		int [] temp=new int [req1.length];
		for (int i=0;i<temp.length;i++)
			temp[i]=-1;
		bins_energy=new double[posBins1.size()];
		bins_id=new double[posBins1.size()];
		double sum_of_util=0;double min_energy=Double.MAX_VALUE;
		double energy_temp=0;
		
		for(int i=1;i<posBins1.size();i++)
		{
			sum_of_util=0;
			for (int j=0;j<req1.length;j++)
			if (posBins1.get(i)[j]!=0)
				sum_of_util+=(posBins1.get(i)[j]*class_itm[j][0]);
			for (int k=0;k<pm_rel.length;k++)
			{
				min_energy=Double.MAX_VALUE;
				if (sum_of_util>pm_rel_cap[k][0])
						continue;
				    energy_temp=(sum_of_util/pm_rel_cap[k][0])*pm_rel[k][0]+pm_rel[k][1];
				    if (energy_temp<min_energy)
				    	{
				    	min_energy=energy_temp;
				    	bins_id[i]=k;
				    	}
					            
			}
			 bins_energy[i]=min_energy;
				
		}
		for (int i=1;i<posBins1.size();i++)//remove
		{
			int index=findIndex_class(posBins1.get(i));
			pbind[i]=index;
			dynam_array[index]=1;
			dynam_array_energy[index]=bins_energy[i];
			dynam_pos1[index]=-1;
			for (int k=0;k<Q_Class1.size();k++)
			{
				dynam_pos[index][k]=-1;

			}
		}
		
		
		double result1=0;
	////****************Dynamic Bin packing**************************************//
		
		result1=Dynamic_Bins_energy_class(0,req1);
		
		System.out.println("Number of Bins:"+ result1);
		
		ArrayList <int[]> Final_bins=Filled_hosts_classification();
		int l=1;int u=vmList.length;
		int m=(l+u)/2;
		guss_m=(int) (m+Math.ceil((epsilon*m)/2));

		
		
		System.out.println("Number of Bins:"+ result1);
		
		energy_temp=0;

	    List<Vm> vms_list = null;
        int level=0;
		for (int i=0;i<Final_bins.size();i++)
			{
			sum_of_util=0;
			for (int j=0;j<req1.length;j++)
				if (Final_bins.get(i)[j]!=0)
					sum_of_util+=Final_bins.get(i)[j]*class_itm[j][0];
			if (hostList1.get(level).getVmList().size()==0)
				level++;
			energy_temp+=(sum_of_util/pm_rel_cap[level%2][0])*pm_rel[level%2][0]+pm_rel[level%2][1];
				
			}
	    

	
		System.out.println(energy_temp);
		return result;		   

		
		}
	
	
	
	
		
		
	public static int OPT_vbp(double[][] pm_rel_cap,double[][] pm_rel)
	{
	
	host_filled=new int[host.length];
	boolean t1=true;
	larg_ind=new int [I.length];
	
	
	//ArrayList <ArrayList<double[]>> Q_Class1 = new ArrayList<ArrayList<double[]>>();		itemLoop:
	itemLoop:	
	for (int i = 0; i < I.length; i++) {
			for (ArrayList<double[]> row : Q_Class1) {
				if (compareItems1(row.get(0), I[i])) {
					row.add(I[i]);
					larg_ind[i]=Q_Class1.indexOf(row);
					continue itemLoop;
				}
			}
			ArrayList<double[]> qNewRow = new ArrayList<double[]>();
			qNewRow.add(I[i]);
			Q_Class1.add(qNewRow);
			larg_ind[i]=Q_Class1.indexOf(qNewRow);


		}

	for (ArrayList<double[]> row : Q_Class1) {
		for (double[] cell : row) {
			System.out.println(Arrays.toString(cell));
		}
		System.out.println("***********************");
	}
	System.out.println(t1);



	class_itm=new double[Q_Class1.size()][d];
	req1=new int[Q_Class1.size()];

	for (int i=0;i<Q_Class1.size();i++)
	{   
		req1[i]=Q_Class1.get(i).size();
		class_itm[i]=Q_Class1.get(i).get(0);
	}




	int Rl=Q_Class1.size();
	int[] counters = new int[Rl];

	double [] size=new double [d];
	double [] size1=new double [d];
	posBins1 = new ArrayList<int[]>();;
	getBins2(size,counters,0,posBins1);
	//getBins3(size1,counters,0,posBins2);	
	
	
	long  dynam_array_length=1;
	for (int i=0;i<req1.length;i++)
		dynam_array_length*=(req1[i]+1);
	dynam_array=new int [(int) dynam_array_length];
	dynam_array_energy=new double [(int) dynam_array_length];
	dynam_pos=new int [(int) dynam_array_length][Q_Class1.size()];
	dynam_pos1=new int [(int) dynam_array_length];
	dynam_pos1_energy=new double[(int) dynam_array_length];
	pbind=new int [posBins1.size()];
	level_list=new int [hostList1.size()];
	int [] temp=new int [req1.length];
	for (int i=0;i<temp.length;i++)
		temp[i]=-1;
	
	for (int i=1;i<posBins1.size();i++)//remove
	{
		int index=findIndex_class(posBins1.get(i));
		pbind[i]=index;
		dynam_array[index]=1;
		//dynam_array_energy[index]=bins_energy[i];
		dynam_pos1[index]=-1;
		for (int k=0;k<Q_Class1.size();k++)
		{
			dynam_pos[index][k]=-1;

		}
	}
	
	
	double result1=0;
////****************Dynamic Bin packing**************************************//
	
	result1=Dynamic_Bins_OPT(0,req1);
	
	System.out.println("Number of Bins:"+ result1);
	 @SuppressWarnings("unused")
	List <int[]> Final_bins=Filled_hosts_classification();
	//result=Filled_hosts();;
	int l=1;int u=vmList.length;
	int m=(l+u)/2;
	
	
	guss_m=(int) (m+Math.ceil((epsilon*m)/2));

	
	
	System.out.println("Number of Bins:"+ result1);
    List<Vm> vms_list = null;

	
	return result;		   

	
	}
		
		
		
		
		



	private static void seperate_large_small() {
		
		
		double beta=0.4;boolean t=false;
		for (int i=0;i<I.length;i++)
		{

			t=false;
			for (int j=0;j<d;j++)
				if (I[i][j]>=beta)
					t=true;


			if (t==true) {
				//large_vm_ind.add(i);
				double[] item = new double[d];
				large.add(item);
				for (int k=0;k<d;k++)
				{
					large.get(large.size()-1)[k]=I[i][k];
					
				}
				//largevm_ids[large.size()-1]= vmls_ids[i];

			}
			else
			{
				double[] item = new double[d];
				small.add(item);
				for (int k=0;k<d;k++)
					small.get(small.size()-1)[k]=I[i][k];



			}
		}
		System.out.println("Beta:"+ beta);
		System.out.println("Alpha:"+ alpha);
		//Finding large Objects//////////////////////////////////////////////////////////		

		

		larg_ind= new int [large.size()];

		System.out.println("LARGE ITEMS");
		int i=1;
		for (double[] LG : Q) {
			System.out.println("Large Items"+(i++)+" :"+Arrays.toString(LG));
		}
		System.out.println("***********************");

		lg_ind=new double[Q.size()][3];

		large_flag=new int[large.size()];
		System.out.println("SMALL ITMES");

		int index = 1;
		for (double[] SM : small) {
			System.out.println("Small Item " + (index++)+" :"+Arrays.toString(SM));
		}
		System.out.println("***********************");


		
	}








	public static int Bins(int level,int [] req1)
	{ 

		int ind=findIndex1(req1);
		if (dynam_array1[ind]!=0)
			return dynam_array1[ind];

		else
		{

			int min=Integer.MAX_VALUE;
			int result=0;
			int [] t1=new int[req1.length];

			posLoop:
				for(int j=1;j<posBins1.size();j++) {
					int [] pb=posBins1.get(j);

					while (!host_fit1(pb,level))	
						 level++;
					
						for(int m=0;m<req1.length;m++) {
							if (req1[m]-pb[m]<0)
								continue posLoop;
							t1[m]=req1[m]-pb[m];


						}

						result=Bins(level+1,t1);
						//int t1_ind=findIndex(t1);
						if(result<min) {
							min=result;
							dynam_pos1[ind]=j;
							//dynam_pos[ind]=t1;

						}
					
				}

			dynam_array1[ind]=1+min;		
			//

			return (1+min);
		}
	}





	private static boolean host_fit1(int[] pb,int level) {
		
		double[] temp=new double[d];
		for (int i=0;i<pb.length;i++)
			if (pb[i]!=0)
			{				
				for (int j=0;j<d;j++)
				{		
					temp[j]+=pb[i]*class_itm[i][j];
					
				}
				
			}
		
		for (int j1=0;j1<d;j1++)
		{		
			if (temp[j1]>host_real[level][j1])
				return false;
			//host_real[level][j1]-=temp[j1];
		}
		
		return true;
	
	}







	public static void F_bins1(int result)
	{
		ArrayList <int[]> Final_bins=new ArrayList<int[]>();
		int [] temp=new int[req1.length];
		int [] temp1=new int [req1.length];

		temp1=posBins1.get(dynam_pos1[findIndex1(req1)]);
		Final_bins.add(temp1);
		for (int i=0;i<req1.length;i++)
			temp[i]=req1[i]-temp1[i];


		int index=findIndex1(temp);

		while (dynam_pos1[index]!=-1)
		{

			//		    	Final_bins.add(dynam_pos[index]);
			//				index=findIndex(dynam_pos[index]);
			temp1=posBins1.get(dynam_pos1[index]);
			Final_bins.add(temp1);
			for (int i=0;i<temp.length;i++)
				temp[i]=temp[i]-temp1[i];

			index=findIndex1(temp);
		}
		for (int i=0;i<pbind1.length;i++)
			if (pbind1[i]==index)
				Final_bins.add(posBins1.get(i));



		System.out.println("Used Bins Items:");
		int ind=1;
		for (int[] FB : Final_bins) {
			System.out.println("Bin " + (ind++)+" :"+Arrays.toString(FB));
		}




		int m= dynam_array1[dynam_array1.length-1];
		double size[]=new double[d];
		double [][] Used_bins_cap=new double[m][d];


		for (int i = 0; i< Final_bins.size(); i++)
		{

			for (int j=0;j<Q_Class1.size();j++)
			{
				if (Final_bins.get(i)[j]!=0)
					size=class_itm[j];       
				for (int k1=0;k1<d;k1++) 
				{
					Used_bins_cap[i][k1]+=(size[k1]*Final_bins.get(i)[j]);

				}

			}
		}


		//System.out.println("The bins caps before real values");

		for (int i=0;i<Used_bins_cap.length;i++)
		{
			System.out.print("Bin "+ i + ": ");   

			for (int j=0;j<Used_bins_cap[i].length;j++)
				System.out.print(Used_bins_cap[i][j]+" ");
			System.out.println("  ");   

		}





	}





	public static ArrayList<ArrayList<double[]>> compround1(double beta )
	{


		//alpha=Math.pow(0.1, 2)/(2*Math.pow(d, 2));
		//alpha=0.34;
	//	alpha=0.45;
		//////////////alpha=0.37;
		beta=0.2;
		double lambda=(epsilon*beta)/(2*d);
		alpha=0.3;

		boolean t=false;
//		if (IListNew[0][0]>0) {
//			//vmList1=vmListItemsDP1;
//			large_vm_ind.clear();
//			small.clear();
//			Q.clear();
//		}
				
		for (int i=0;i<I.length;i++)
		{

			t=false;
			for (int j=0;j<d;j++)
				if (I[i][j]>=beta) {
					t=true;
					break;
				}
				else {
					t=false;
									}
					
			
				

			if (t==true) {
				large_vm_ind.add(vmList1.get(i).getId());
				double[] item = new double[d];
				large.add(item);
				for (int k=0;k<d;k++)
				{
					large.get(large.size()-1)[k]=I[i][k];
					
				}
				//largevm_ids[large.size()-1]= vmls_ids[i];
				largevm_ids[i]= vmls_ids[i];


			}
			else
			{
				double[] item = new double[d];
				small.add(item);
				for (int k=0;k<d;k++)
					small.get(small.size()-1)[k]=I[i][k];



			}
		}
		System.out.println("Beta:"+ beta);
		System.out.println("Alpha:"+ alpha);
		//Finding large Objects//////////////////////////////////////////////////////////		

		for (int i=0;i<large.size();i++)
		{
			double[] item = new double[d];
			Q.add(item);
			//Q.add(large.get(i));
    	for (int k=0;k<d;k++)
         {
				//if (k<d-1)
				//{

					Q.get(i)[k]=Math.ceil(large.get(i)[k]/alpha)*alpha;

				//}
				//else
				//{
                	//Q.get(i)[k]=large.get(i)[k];
//
			 //}
			}



		}

		larg_ind= new int [large.size()];
		//flag_one=new int[large.size()];

		System.out.println("LARGE ITEMS");
		int i=1;
		for (double[] LG : Q) {
			System.out.println("Large Items"+(i++)+" :"+Arrays.toString(LG));
		}
		System.out.println("***********************");

		lg_ind=new double[Q.size()][3];

		large_flag=new int[large.size()];
		System.out.println("SMALL ITMES");

		int index = 1;
		for (double[] SM : small) {
			System.out.println("Small Item " + (index++)+" :"+Arrays.toString(SM));
		}
		System.out.println("***********************");




		ArrayList<ArrayList<double[]>> Q_class_round =new ArrayList<ArrayList<double[]>>();
		System.out.println("1/alpha: "+ Math.ceil(1/alpha));
		ArrayList <ArrayList<Integer>> W = new ArrayList<ArrayList<Integer>>();
		ArrayList <ArrayList<double[]>> Q_Class = new ArrayList<ArrayList<double[]>>();
		Classification_first_d(alpha,W, Q_Class);
		System.out.println("Lambda:"+ lambda);
		Q_class_round =Classification_last_d(Q_Class,lambda);


		return Q_class_round;
	}

	
	
	public static ArrayList<ArrayList<double[]>> compround1Copy(double beta )
	{


		//alpha=Math.pow(0.1, 2)/(2*Math.pow(d, 2));
		//alpha=0.34;
	//	alpha=0.45;
		//////////////alpha=0.37;
		beta=0.2;
		double lambda=(epsilon*beta)/(2*d);
		alpha=0.2;

		boolean t=false;
		//if (IListNew[0][0]>0) {
			//vmList1=vmListItemsDP1;
			large_vm_ind.clear();
			small.clear();
			Q.clear();
		//}
				
		for (int i=0;i<I.length;i++)
		{

			t=false;
			for (int j=0;j<d;j++)
				if (I[i][j]>=beta) {
					t=true;
					break;
				}
				else {
					t=false;
									}
					
			
				

			if (t==true) {
				large_vm_ind.add(vmListItemsDP.get(i).getId());
				double[] item = new double[d];
				large.add(item);
				for (int k=0;k<d;k++)
				{
					large.get(large.size()-1)[k]=I[i][k];
					
				}
				//largevm_ids[large.size()-1]= vmls_ids[i];
				largevm_ids[i]= vmls_ids[i];


			}
			else
			{
				double[] item = new double[d];
				small.add(item);
				for (int k=0;k<d;k++)
					small.get(small.size()-1)[k]=I[i][k];



			}
		}
		System.out.println("Beta:"+ beta);
		System.out.println("Alpha:"+ alpha);
		//Finding large Objects//////////////////////////////////////////////////////////		

		for (int i=0;i<large.size();i++)
		{
			double[] item = new double[d];
			Q.add(item);
			//Q.add(large.get(i));
    	for (int k=0;k<d;k++)
         {
				//if (k<d-1)
				//{

					Q.get(i)[k]=Math.ceil(large.get(i)[k]/alpha)*alpha;

				//}
				//else
				//{
                	//Q.get(i)[k]=large.get(i)[k];
//
			 //}
			}



		}

		larg_ind= new int [large.size()];
		//flag_one=new int[large.size()];

		System.out.println("LARGE ITEMS");
		int i=1;
		for (double[] LG : Q) {
			System.out.println("Large Items"+(i++)+" :"+Arrays.toString(LG));
		}
		System.out.println("***********************");

		lg_ind=new double[Q.size()][3];

		large_flag=new int[large.size()];
		System.out.println("SMALL ITMES");

		int index = 1;
		for (double[] SM : small) {
			System.out.println("Small Item " + (index++)+" :"+Arrays.toString(SM));
		}
		System.out.println("***********************");




		ArrayList<ArrayList<double[]>> Q_class_round =new ArrayList<ArrayList<double[]>>();
		System.out.println("1/alpha: "+ Math.ceil(1/alpha));
		ArrayList <ArrayList<Integer>> W = new ArrayList<ArrayList<Integer>>();
		ArrayList <ArrayList<double[]>> Q_Class = new ArrayList<ArrayList<double[]>>();
		Classification_first_d(alpha,W, Q_Class);
		System.out.println("Lambda:"+ lambda);
		Q_class_round =Classification_last_d(Q_Class,lambda);


		return Q_class_round;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Classify large items to W^u classes for the first d-1 dimensions///////////////////////////

	public static boolean compareItems(double[] item1, double[] item2) {
		//for (int i = 0; i < item1.length - 1; i++) {
		for (int i = 0; i < item1.length; i++) {

			if (item1[i] != item2[i])
				return false;
		}
		return true;
	}

	public static void Classification_first_d(double alpha, ArrayList<ArrayList<Integer>> W, ArrayList<ArrayList<double[]>> Q_class)
	{

		boolean t1=true;


		itemLoop:
			for (int i = 0; i < Q.size(); i++) {
				for (ArrayList<double[]> row : Q_class) {
					if (compareItems(row.get(0), Q.get(i))) {
						row.add(Q.get(i));
						larg_ind[i]=Q_class.indexOf(row);
						continue itemLoop;
					}
				}
				ArrayList<double[]> qNewRow = new ArrayList<double[]>();
				qNewRow.add(Q.get(i));
				Q_class.add(qNewRow);
				larg_ind[i]=Q_class.indexOf(qNewRow);

			}
		for (ArrayList<double[]> row : Q_class) {
			row.sort(new Comparator<double[]>() {
				public int compare(double[] o1, double[] o2) {
					double diff = - o1[o1.length-1] + o2[o2.length-1];
					if (diff > 0)
						return 1;
					else if (diff == 0)
						return 0;
					else
						return -1;
				}
			});
		}
		for (ArrayList<double[]> row : Q_class) {
			for (double[] cell : row) {
				System.out.println(Arrays.toString(cell));
			}
			System.out.println("***********************");
		}
		System.out.println(t1);
	}


	//classify items in each W^u based on the last dimension: d
	public static ArrayList<ArrayList<double[]>> Classification_last_d(ArrayList<ArrayList<double[]>> Q_class, double lambda)
	{

		ArrayList<ArrayList<double[]>> Q_class_round = new ArrayList<ArrayList<double[]>>();
		ArrayList<ArrayList<double[]>> Q_prim=linear_group(Q_class,lambda);
		for (int i=0;i<Q_prim.size();i++)
		{
			ArrayList<double[]> group = Q_prim.get(i);
			ArrayList<double[]> round_group = new ArrayList<double[]>();
			Q_class_round.add(round_group);
			for (int k=0;k<group.size();k++)
			{
				double[] item = group.get(k);
				double[] roundItem = new double[d];


				round_group.add(roundItem);
				for (int l=0;l<item.length-1;l++)	
					roundItem[l]=item[l];

				roundItem[d-1]=group.get(0)[d-1];


			}

		}

		for (ArrayList<double[]> group : Q_class_round) {
			for (double[] item : group) {
				System.out.print(Arrays.toString(item) + ", ");
			}
			System.out.println();
		}
		System.out.println("****************************************");


		return Q_class_round;
	}


	public static ArrayList<ArrayList<double[]>> linear_group(ArrayList<ArrayList<double[]>> Q_class,double lambda){



		ArrayList<ArrayList<double[]>> Q_prim = new ArrayList<ArrayList<double[]>>();

		larg_ind1=new int[large.size()];
		boolean flag=true;
		ArrayList<double[]> Q_prim_row=new ArrayList<double[]>();

		for(ArrayList<double[]> row : Q_class)
		{
			int b = (int) Math.ceil(lambda * 1000*row.size()); //TODO: remove 2 coefficient
			System.out.println("b = " + b);


			//ArrayList<double[]> Q_prim_row=null;



			for (int j=0;j<row.size();j++)
			{
				if (j % b == 0) {
					Q_prim_row = new ArrayList<double[]>();
					Q_prim.add(Q_prim_row);
					flag=true;
				}
				Q_prim_row.add(row.get(j));
				int index_itm=findItems(row.get(j));
				larg_ind1[index_itm]=Q_prim.indexOf(Q_prim_row);




			}
		}

		for (ArrayList<double[]> group : Q_prim) {
			for (double[] item : group) {
				System.out.print(Arrays.toString(item) + ", ");
			}
			System.out.println();
		}
		System.out.println("****************************************");


		for (int i=0;i<large_flag.length;i++)
			large_flag[i]=0;
		return Q_prim;
	}

	static int[] large_flag;
	public static int findItems(double[] item) {
		boolean flag=false;int ind=0;
		loop:
			for (int i=0;i<Q.size();i++) {
				double[] group= Q.get(i);
				if (item[0] == group[0] && large_flag[i]!=1) {
					    large_flag[i]=1;
						flag=true;
						return i;
						
					
				}
//					else
//						flag=false;
//
//				}
//				if (flag==true)
//				{
//					large_flag[ind]=1;
//					return ind;
//				}
//				else
//				{
//					ind++;
//					continue loop;
//				}

				
			}

		return 0;

	}




	public static double [][] sort(double [] [] S)
	{
		Arrays.sort(S, new Comparator<double[]>() {      
			@Override
			public int compare(double[] o1, double[] o2) {
				int c=Double.compare(o2[3], o1[3]);
				if (c==1) 
					return 1; 
				else
					return -1; 


			}
		});
		return S;
	}



	public static void getBins2(double [] size,int[] counters,int level, ArrayList<int[]> bins) {


		if(level == counters.length)
		{   
			//s=performSum(counters,requests,round1,d);
			bins.add(Arrays.copyOf(counters, counters.length));

		}
		else 
		{
			//double [] temp = round1.get(level).get(0);
			double [] temp = class_itm[level];
			Outerloop:
				for (counters[level] = 0; counters[level] <= req1[level]; counters[level]++) 
				{
					for (int i=0;i<d;i++)
						if (size[i]>1)
							break Outerloop;

					getBins2(size,counters, level + 1, bins);

					for(int i=0;i<d;i++)
						size[i]+=temp[i];
				}
			for (int i=0;i<d;i++)
				size[i]-=counters[level]*temp[i];
		}


	}

	public static void getBins3(double [] size,int[] counters,int level, ArrayList<int[]> bins) {


		if(level == counters.length)
		{   
			//s=performSum(counters,requests,round1,d);
			bins.add(Arrays.copyOf(counters, counters.length));

		}
		else 
		{
			//double [] temp = round1.get(level).get(0);
			double [] temp = class_itm[level];
			Outerloop:
				for (counters[level] = 0; counters[level] <= req1[level]; counters[level]++) {
					for (int i=1;i<d;i++)
						if (size[0]<=0.69)
						{
						  if (size[i]>1)
							break Outerloop;
						}
						else
							break Outerloop;

					getBins3(size,counters, level + 1, bins);

					for(int i=0;i<d;i++)
						size[i]+=temp[i];
				}
			for (int i=0;i<d;i++)
				size[i]-=counters[level]*temp[i];
		}


	}






	public static void getBins1(double [] size,int[] counters,int level, ArrayList<int[]> bins) {


		if(level == counters.length)
		{   
			bins.add(Arrays.copyOf(counters, counters.length));

		}
		else 
		{
			
			double [] temp = class_items[level];
			
			Outerloop:
				for (counters[level] = 0; counters[level] <= requests[level]; counters[level]++) {
					for (int i=0;i<d;i++)
						if (size[i]>1.375)
							break Outerloop;
					
					getBins1(size,counters, level + 1, bins);

					for(int i=0;i<d;i++)
						size[i]+=temp[i];
				}
			for (int i=0;i<d;i++)
				size[i]-=counters[level]*temp[i];
		}


	}

	
	public static void getBins_small(double [] size,int[] counters,int level, ArrayList<int[]> bins,double [] temp) {


		if(level == counters.length)
		{   
			bins.add(Arrays.copyOf(counters, counters.length));

		}
		else 
		{
			
			//double [] temp = class_items[level];
			
			Outerloop:
				for (counters[level] = 0; counters[level] <= requests[level]; counters[level]++) {
					for (int i=0;i<d;i++)
						if (size[i]>1)
							break Outerloop;
					
					getBins_small(size,counters, level + 1, bins,temp);

					for(int i=0;i<d;i++)
						size[i]+=temp[i];
				}
			for (int i=0;i<d;i++)
				size[i]-=counters[level]*temp[i];
		}


	}
	
	
	public static void getBins1_type2(double [] size,int[] counters,int level, ArrayList<int[]> bins) {


		if(level == counters.length)
		{   
			//s=performSum(counters,requests,round1,d);
			bins.add(Arrays.copyOf(counters, counters.length));

		}
		else 
		{
			//double [] temp = round1.get(level).get(0);
			double [] temp = class_items[level];
			//double [] temp = {0.93,0.75,0.1};
			Outerloop:
				for (counters[level] = 0; counters[level] <= requests[level]; counters[level]++) {
				//for (counters[level] = 0; counters[level] <= requests[level]; counters[level]++) {
					for (int i=0;i<d;i++)
						//if (size[i]>1+epsilon)
						if (size[i]>0.6992481203007519)
							break Outerloop;
					//if (size[i]>1 || 
					getBins1_type2(size,counters, level + 1, bins);

					for(int i=0;i<d;i++)
						size[i]+=temp[i];
				}
			for (int i=0;i<d;i++)
				size[i]-=counters[level]*temp[i];
		}


	}
	

	public static double[] performSum(int[] counters, int[] requests,ArrayList<ArrayList<double[]>> round1,int d) {

		double [] s=new double [d];

		for (int level = 0; level < counters.length; level++)
		{
			double [] size=round1.get(level).get(0);
			for (int i=0;i<d;i++)
				s[i]+=size[i]*counters[level];

		}

		double [] max1=new double[2];
		max1[0]=s[0];
		max1[1]=s[d-1];
		for (int i=1; i<d-1;i++)
			if (s[i]>max1[0])
				max1[0]=s[i];
		return max1;


	}






	public static void print(double [] [] W)
	{
		System.out.println();
		for (int i=0; i<W.length;i++)
		{
			for (int k=0;k<W[i].length;k++)
				System.out.print ((W[i][k])+" ");
			System.out.println();

		}

	}


	static HashMap<Integer, Integer> Bin_status = new HashMap<>();
	static int[] small_bins=new int [small.size()];
	static int[] small_index=new int [small.size()];

	static ArrayList<small_class> small_class1=new ArrayList<small_class>();



	public static void solvemethod(int m, double [][] b1)

	{
		try {

			IloCplex model= new  IloCplex();



			IloNumVar [][] x=  new IloNumVar[small.size()][m];

			//b1 composed of the used capacity of each bin
			//A is the set of small items



			for (int i=0;i<small.size();i++)
			{
				for (int j=0;j<m;j++)
				{
					String varname="x"+(i+1)+""+(j+1);
					x[i][j]= model.numVar(0,Double.MAX_VALUE,varname);
				} 
				//(IloIntVar)
			}


			for (int i=0;i<small.size();i++)
			{

				IloLinearNumExpr cons2=model.linearNumExpr();
				for (int j=0;j<m;j++)
					cons2.addTerm(1, x[i][j]);
				model.addLe(cons2,1);	
			}		


			for (int j=0;j<m;j++)
			{	
				for (int l=0;l<d-1;l++)
				{
					IloLinearNumExpr cons=model.linearNumExpr();
					for (int i=0;i<small.size();i++)
						cons.addTerm(small.get(i)[l],x[i][j]);
					model.addLe(cons,(1+epsilon)-b1[j][l]);

				}

			}


			for (int j=0;j<m;j++)
			{	

				IloLinearNumExpr cons1=model.linearNumExpr();
				for (int i=0;i<small.size();i++)
					cons1.addTerm(small.get(i)[d-1],x[i][j]);
				model.addLe(cons1,1-b1[j][d-1]);



			}

			double [] cap_bin=new double [m];
			double max=0;
			for (int i=0;i<m;i++)
			{
				max=b1[i][0];
				for (int k=1;k<d;k++)
					if (b1[i][k]>max)
						max=b1[i][k];
				cap_bin[i]=max;		 
			}


			IloLinearNumExpr obj= model.linearNumExpr();

			for (int j=0;j<m;j++)
				for (int i=0;i<small.size();i++)
					obj.addTerm(cap_bin[j], x[i][j]);
			//obj.addTerm(0, x[i][j]);

			model.addMaximize(obj);
			//model.addMinimize(obj);




			model.exportModel("bin packing.lp");

			//ArrayList <double[]> DC_FF=new ArrayList<double []>();
			boolean isSolved=model.solve();

			if(isSolved) {
				double objValue=model.getObjValue();
				System.out.println("obj_val=" + objValue);

				for (int j=0;j<m;j++)
					for (int i=0;i< small.size();i++)

						System.out.println("x[" +(i+1)+"][" +(j+1)+"] = "+model.getValue(x[i][j]));


				int [] temp_index=new int [small.size()];

				item_loop:	
					for (int i=0;i<small.size();i++)
					{
						for (int j=0;j<m;j++)
							if (model.getValue(x[i][j])>0.0)
							{
								temp_index[i]=1;
								if (model.getValue(x[i][j])==1)
								{ 
									Bin_status.put(i+1,j+1);	
									for(int k=0;k<d;k++)
									{
										b1[j][k]+=small.get(i)[k];

									}

									continue item_loop;
								}
								else  
								{
									small_class sm_cls = new small_class();
									sm_cls.index=i;
									sm_cls.item=small.get(i);
									small_class1.add(sm_cls);
									//small_index[i]=DC_FF.size();

									continue item_loop;
								}

							}

					}
				for (int i=0;i<temp_index.length;i++)
					if (temp_index[i]!=1)
					{
						small_class sm_cls = new small_class();
						sm_cls.index=i;
						sm_cls.item=small.get(i);
						small_class1.add(sm_cls);
					}	

				//FFD(m);
			}
			else
			{
				System.out.print("Model not solved!");
				FFD_alg(m);
			}
		}
		catch (IloException ex) {
			ex.printStackTrace();
			System.err.println("Concert exception caught: " + ex);
		}


	}


	public static void FFD_ordCopy(int m)
	{

		small_class1.sort(new Comparator<small_class>(){  
			@Override  
			public int compare(small_class a, small_class b){  
				double diff = -a.item[0] + b.item[0]; 
				if (diff > 0)
					return 1;
				else if (diff == 0)
					return 0;
				else
					return -1;
			}


		}); 
		int index1=1;
		the_last_host=0;
		System.out.println("Small Items after Decsending Sort");
		for (small_class SM : small_class1) {
			System.out.println("Small Item " + (index1++)+" :"+Arrays.toString(SM.item));
		}
		System.out.println("***********************");


		ArrayList<double[]> Bin_list=new ArrayList<double[]>();

//		for (int i=0;i<hostForDP.size();i++)
//			Bin_list.add(host_bins.get(i));

		boolean flag=true;int k=0;
		item_loop:
			while (k<small_class1.size())
			{
                 flag=false;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
				int index=0;
				Vm vm=null;
				for (int j=0;j<vmList1_copy.size();j++)
				{ 
					if (compareItems1(small_class1.get(k).item, I[j]) && sml_flg[j]!=1)
					{
						sml_flg[j]=1;
						vm=vmList1_copy.get(j);
						index=j;
						flag_one[j]=1;
						break;
					}
				}
				
				boolean f=false;
				for (int i=0;i<hostForDP.size();i++) {
					for (int k1=0;k1<d;k1++)
					{
						if (host_copy[hostForDP.get(i).getId()][k1]-I[index][k1]>=0)
							f=true;
						else {
							f=false;
							break;
						}
					}
					if (f) {
						if (vm==null)
							return;
					  if (hostList1_copy.get(hostForDP.get(i).getId()).isSuitableForVm1(vm)) {
						hostList1_copy.get(hostForDP.get(i).getId()).vmCreate(vm);
						System.out.println(vm.getId());
						vm.setHost(hostList1_copy.get(hostForDP.get(i).getId()));
						for (int k1=0;k1<d;k1++)
							{
							host_copy[hostForDP.get(i).getId()][k1]-=I[index][k1];
							}
		     			vm_allocate[index]=1;	
						PowerVmAllocationPolicyAbstract.getVmTable1().put(vm.getUid(), hostList1_copy.get(hostForDP.get(i).getId()));
						host_filled[hostForDP.get(i).getId()]=1;
						flag=true;
						k++;
					    if (PowerVmAllocationPolicyMigrationAbstract.vmList_items.contains(vm))
  					        hostForDP.add(hostForDP.get(i));
						vmForDP.add(vm);
//						the_last_host=i;
						break;
						}
				}
				}
			
				int l=hostForDP.get(hostForDP.size()-1).getId();
				if (l==host_copy.length-2)
					l=1;

				
				while (!flag & l<host_copy.length-1) {
					
					l=l+1;
													     	  
			     	 for (int k1=0;k1<d;k1++)
						{
							if (host_copy[l][k1]-I[index][k1]>=0)
								f=true;
							else {
								f=false;
								break;
							}
						}
						if (f) {
							if (vm==null)
									return;
						  if (hostList1_copy.get(l).isSuitableForVm1(vm)) {
							hostList1_copy.get(l).vmCreate(vm);
							System.out.println(vm.getId());
							vm.setHost(hostList1_copy.get(l));
							for (int k1=0;k1<d;k1++)
								{
								host_copy[l][k1]-=I[index][k1];
								}
			     			vm_allocate[index]=1;	
							PowerVmAllocationPolicyAbstract.getVmTable1().put(vm.getUid(), hostList1_copy.get(l));
							host_filled[l]=1;
						    if (PowerVmAllocationPolicyMigrationAbstract.vmList_items.contains(vm))
      					     	 hostForDP.add(hostList1_copy.get(l)) ; 
					     	 vmForDP.add(vm);
							flag=true;
							k++;
							break;
							}
			     	  
						} 
					   }
	
				
	
			}
			

		System.out.println("Bins Filled Like:");
		for (int i=0;i<Bin_list.size();i++)
		{
			if (Bin_list.get(i)[0]!=0) {
			System.out.println("Bins "+(i+1)+": ");
			for(int j=0;j<d;j++)
					System.out.print(Bin_list.get(i)[j]+" ");
			}
			System.out.println("");
		}

		for(Entry<Integer, Integer> entry : Bin_status.entrySet())
	    {   //print keys and values
	         System.out.print(entry.getKey() + ":" +entry.getValue()+" , ");
	    }
	
		System.out.println(" ");

	}

	public static void FFD_ord(int m)
	{

		small_class1.sort(new Comparator<small_class>(){  
			@Override  
			public int compare(small_class a, small_class b){  
				double diff = -a.item[0] + b.item[0]; 
				if (diff > 0)
					return 1;
				else if (diff == 0)
					return 0;
				else
					return -1;
			}


		}); 
		int index1=1;
		the_last_host=0;
		System.out.println("Small Items after Decsending Sort");
		for (small_class SM : small_class1) {
			System.out.println("Small Item " + (index1++)+" :"+Arrays.toString(SM.item));
		}
		System.out.println("***********************");


		ArrayList<double[]> Bin_list=new ArrayList<double[]>();

		for (int i=0;i<host_bins.size();i++)
			Bin_list.add(host_bins.get(i));

		boolean flag=true;int k=0;
		item_loop:
			while (k<small_class1.size())
			{
                 flag=false;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
				int index=0;
				Vm vm=null;
				for (int j=0;j<vmList.length;j++)
				{ 
					if (compareItems1(small_class1.get(k).item, I[j]) && sml_flg[j]!=1)
					{
						sml_flg[j]=1;
						vm=vmList1.get(j);
						if (vm.getId()==183)
							System.out.print(vm);
						index=j;
						flag_one[j]=1;
						break;
					}
				}
				
				
				for (int i=0;i<host_ids.size();i++)
					  if (hostList1.get(host_ids.get(i)).isSuitableForVm1(vm)) {
						hostList1.get(host_ids.get(i)).vmCreate(vm);
						System.out.println(vm.getId());
						vm.setHost(hostList1.get(host_ids.get(i)));
						for (int k1=0;k1<host[host_ids.get(i)].length;k1++)
							{
							host[host_ids.get(i)][k1]-=I[index][k1];
							}
		     			vm_allocate[index]=1;	
						PowerVmAllocationPolicyAbstract.getVmTable().put(vm.getUid(), hostList1.get(host_ids.get(i)));
						host_filled[host_ids.get(i)]=1;
						flag=true;
						k++;
//						the_last_host=i;
						break;
						}
				
				
	
		if (!flag) {
		  //sethost_FFD(small_class1.get(k).index);
			  sethost_FFD(vm,small_class1.get(k).index);

		m=m+1;	
		k++;
		}
			}
			

		System.out.println("Bins Filled Like:");
		for (int i=0;i<Bin_list.size();i++)
		{
			if (Bin_list.get(i)[0]!=0) {
			System.out.println("Bins "+(i+1)+": ");
			for(int j=0;j<d;j++)
					System.out.print(Bin_list.get(i)[j]+" ");
			}
			System.out.println("");
		}

		for(Entry<Integer, Integer> entry : Bin_status.entrySet())
	    {   //print keys and values
	         System.out.print(entry.getKey() + ":" +entry.getValue()+" , ");
	    }
	
		System.out.println(" ");

	}

	
	public static void FFD_ord_vbp(int m)
	{

		small_class1.sort(new Comparator<small_class>(){  
			@Override  
			public int compare(small_class a, small_class b){  
				double diff = -a.item[0] + b.item[0]; 
				if (diff > 0)
					return 1;
				else if (diff == 0)
					return 0;
				else
					return -1;
			}


		}); 
		int index1=1;
		the_last_host=0;
		System.out.println("Small Items after Decsending Sort");
		for (small_class SM : small_class1) {
			System.out.println("Small Item " + (index1++)+" :"+Arrays.toString(SM.item));
		}
		System.out.println("***********************");


		ArrayList<double[]> Bin_list=new ArrayList<double[]>();

		for (int i=0;i<host_bins.size();i++)
			Bin_list.add(host_bins.get(i));

		boolean flag=true;int k=0;
		item_loop:
			while (k<small_class1.size())
			{
                 flag=false;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
				int index=0;
				Vm vm=null;
				for (int j=0;j<vmList.length;j++)
				{ 
					if (compareItems1(small_class1.get(k).item, I[j]) && sml_flg[j]!=1)
					{
						sml_flg[j]=1;
						vm=vmList1.get(j);
						index=j;
						flag_one[j]=1;
						break;
					}
				}
				
				
				for (int i=0;i<host_ids.size();i++)
					  if (hostList1.get(host_ids.get(i)).isSuitableForVm(vm)) {
						hostList1.get(host_ids.get(i)).vmCreate(vm);
						System.out.println(vm.getId());
						vm.setHost(hostList1.get(host_ids.get(i)));
						for (int k1=0;k1<host[host_ids.get(i)].length;k1++)
							{
							host[host_ids.get(i)][k1]-=I[index][k1];
							}
		     			vm_allocate[index]=1;	
						PowerVmAllocationPolicyAbstract.getVmTable().put(vm.getUid(), hostList1.get(host_ids.get(i)));
						host_filled[host_ids.get(i)]=1;
						flag=true;
						k++;
//						the_last_host=i;
						break;
						}
				
				
	
		if (!flag) {
		  //sethost_FFD(small_class1.get(k).index);
			  sethost_FFD_vbp(vm,small_class1.get(k).index);

		m=m+1;	
		k++;
		}
			}
			

		System.out.println("Bins Filled Like:");
		for (int i=0;i<Bin_list.size();i++)
		{
			if (Bin_list.get(i)[0]!=0) {
			System.out.println("Bins "+(i+1)+": ");
			for(int j=0;j<d;j++)
					System.out.print(Bin_list.get(i)[j]+" ");
			}
			System.out.println("");
		}

		for(Entry<Integer, Integer> entry : Bin_status.entrySet())
	    {   //print keys and values
	         System.out.print(entry.getKey() + ":" +entry.getValue()+" , ");
	    }
	
		System.out.println(" ");

	}
	
	
	
	
	
	
	public static void abs_ord(int m)
	{

		small_class1.sort(new Comparator<small_class>(){  
			@Override  
			public int compare(small_class a, small_class b){  
				double diff = -a.item[0] + b.item[0]; 
				if (diff > 0)
					return 1;
				else if (diff == 0)
					return 0;
				else
					return -1;
			}


		}); 
		int index1=1;
		the_last_host=0;
		System.out.println("Small Items after Decsending Sort");
		for (small_class SM : small_class1) {
			System.out.println("Small Item " + (index1++)+" :"+Arrays.toString(SM.item));
		}
		System.out.println("***********************");


		ArrayList<double[]> Bin_list=new ArrayList<double[]>();

		for (int i=0;i<host_bins.size();i++)
			Bin_list.add(host_bins.get(i));

		boolean flag=true;int k=0;
		item_loop:
			while (k<small_class1.size())
			{
                 flag=false;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
				int index=0;
				Vm vm=null;
				for (int j=0;j<vmList.length;j++)
				{ 
					if (compareItems1(small_class1.get(k).item, I[j]) && sml_flg[j]!=1)
					{
						sml_flg[j]=1;
						vm=vmList1.get(j);
						index=j;
						flag_one[j]=1;
						break;
					}
				}

				//List<PowerHostUtilizationHistory> overUtilizedHosts = PowerVmAllocationPolicyMigrationAbstract.getOverUtilizedHosts();

				PowerVmSelectionPolicy vmSelectionPolicy = null;
				double parameter = 0;
				PowerVmAllocationPolicyMigrationAbstract fallbackVmSelectionPolicy = new PowerVmAllocationPolicyMigrationStaticThreshold(hostList1,
						vmSelectionPolicy,
						0.8);

				VmAllocationPolicy vmAllocationPolicy = new AbsoluteCapacity(
						hostList1,
						vmSelectionPolicy,
						parameter,
						Constants.SCHEDULING_INTERVAL,
						fallbackVmSelectionPolicy);
			   
						PowerHost host1= vmAllocationPolicy.findHostForVm(vm, null);
						if (host1!=null) {
						host1.vmCreate(vm);
						System.out.println(vm.getId());
						vm.setHost(host1);
						for (int k1=0;k1<host[host1.getId()].length;k1++)
							{
							host[host1.getId()][k1]-=I[index][k1];
							}
		     			vm_allocate[index]=1;	
						PowerVmAllocationPolicyAbstract.getVmTable().put(vm.getUid(), host1);
						host_filled[host1.getId()]=1;
						flag=true;
						k++;
						}
//				for (int i=0;i<host_ids.size();i++)
//					  if (hostList1.get(host_ids.get(i)).isSuitableForVm(vm)) {
//						hostList1.get(host_ids.get(i)).vmCreate(vm);
//						System.out.println(vm.getId());
//						vm.setHost(hostList1.get(host_ids.get(i)));
//						for (int k1=0;k1<host[host_ids.get(i)].length;k1++)
//							{
//							host[host_ids.get(i)][k1]-=I[index][k1];
//							}
//		     			vm_allocate[index]=1;	
//						PowerVmAllocationPolicyAbstract.getVmTable().put(vm.getUid(), hostList1.get(host_ids.get(i)));
//						host_filled[host_ids.get(i)]=1;
//						flag=true;
//						k++;
////						the_last_host=i;
//						break;
//						}
				
				
	
		if (!flag) {
		  //sethost_FFD(small_class1.get(k).index);
			  sethost_FFD(vm,small_class1.get(k).index);

		m=m+1;	
		k++;
		}
			
			}

		System.out.println("Bins Filled Like:");
		for (int i=0;i<Bin_list.size();i++)
		{
			if (Bin_list.get(i)[0]!=0) {
			System.out.println("Bins "+(i+1)+": ");
			for(int j=0;j<d;j++)
					System.out.print(Bin_list.get(i)[j]+" ");
			}
			System.out.println("");
		}

		for(Entry<Integer, Integer> entry : Bin_status.entrySet())
	    {   //print keys and values
	         System.out.print(entry.getKey() + ":" +entry.getValue()+" , ");
	    }
	
		System.out.println(" ");

	}

	
	
	
	
	
	public static void PABFD_ord(int m)
	{

		small_class1.sort(new Comparator<small_class>(){  
			@Override  
			public int compare(small_class a, small_class b){  
				double diff = -a.item[0] + b.item[0]; 
				if (diff > 0)
					return 1;
				else if (diff == 0)
					return 0;
				else
					return -1;
			}


		}); 
		int index1=1;
		the_last_host=0;
		System.out.println("Small Items after Decsending Sort");
		for (small_class SM : small_class1) {
			System.out.println("Small Item " + (index1++)+" :"+Arrays.toString(SM.item));
		}
		System.out.println("***********************");


		ArrayList<double[]> Bin_list=new ArrayList<double[]>();

		for (int i=0;i<host_bins.size();i++)
			Bin_list.add(host_bins.get(i));

		boolean flag=true;int k=0;
		item_loop:
			while (k<small_class1.size())
			{
                 flag=false;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
				int index=0;
				Vm vm=null;
				for (int j=0;j<vmList.length;j++)
				{ 
					if (compareItems1(small_class1.get(k).item, I[j]) && sml_flg[j]!=1)
					{
						sml_flg[j]=1;
						vm=vmList1.get(j);
						index=j;
						flag_one[j]=1;
						break;
					}
				}

				//List<PowerHostUtilizationHistory> overUtilizedHosts = PowerVmAllocationPolicyMigrationAbstract.getOverUtilizedHosts();

				PowerVmSelectionPolicy vmSelectionPolicy = null;
				double parameter = 0;
				PowerVmAllocationPolicyMigrationAbstract fallbackVmSelectionPolicy = new PowerVmAllocationPolicyMigrationStaticThreshold(hostList1,
						vmSelectionPolicy,
						0.8);

				VmAllocationPolicy vmAllocationPolicy = new AbsoluteCapacity(
						hostList1,
						vmSelectionPolicy,
						parameter,
						Constants.SCHEDULING_INTERVAL,
						fallbackVmSelectionPolicy);
			   
						PowerHost host1= vmAllocationPolicy.findHostForVm(vm, null);
						if (host1!=null) {
						host1.vmCreate(vm);
						System.out.println(vm.getId());
						vm.setHost(host1);
						for (int k1=0;k1<host[host1.getId()].length;k1++)
							{
							host[host1.getId()][k1]-=I[index][k1];
							}
		     			vm_allocate[index]=1;	
						PowerVmAllocationPolicyAbstract.getVmTable().put(vm.getUid(), host1);
						host_filled[host1.getId()]=1;
						flag=true;
						k++;
						}
//				for (int i=0;i<host_ids.size();i++)
//					  if (hostList1.get(host_ids.get(i)).isSuitableForVm(vm)) {
//						hostList1.get(host_ids.get(i)).vmCreate(vm);
//						System.out.println(vm.getId());
//						vm.setHost(hostList1.get(host_ids.get(i)));
//						for (int k1=0;k1<host[host_ids.get(i)].length;k1++)
//							{
//							host[host_ids.get(i)][k1]-=I[index][k1];
//							}
//		     			vm_allocate[index]=1;	
//						PowerVmAllocationPolicyAbstract.getVmTable().put(vm.getUid(), hostList1.get(host_ids.get(i)));
//						host_filled[host_ids.get(i)]=1;
//						flag=true;
//						k++;
////						the_last_host=i;
//						break;
//						}
				
				
	
		if (!flag) {
		  //sethost_FFD(small_class1.get(k).index);
			  sethost_FFD(vm,small_class1.get(k).index);

		m=m+1;	
		k++;
		}
			
			}

		System.out.println("Bins Filled Like:");
		for (int i=0;i<Bin_list.size();i++)
		{
			if (Bin_list.get(i)[0]!=0) {
			System.out.println("Bins "+(i+1)+": ");
			for(int j=0;j<d;j++)
					System.out.print(Bin_list.get(i)[j]+" ");
			}
			System.out.println("");
		}

		for(Entry<Integer, Integer> entry : Bin_status.entrySet())
	    {   //print keys and values
	         System.out.print(entry.getKey() + ":" +entry.getValue()+" , ");
	    }
	
		System.out.println(" ");

	}

	
	
	
	
	
	



	public static void FFD(int m,double[][] pm_rel_cap)
	{

		small_class1.sort(new Comparator<small_class>(){  
			@Override  
			public int compare(small_class a, small_class b){  
				double diff = -a.item[0] + b.item[0]; 
				if (diff > 0)
					return 1;
				else if (diff == 0)
					return 0;
				else
					return -1;
			}


		}); 
		int index1=1;
		the_last_host=0;
		System.out.println("Small Items after Decsending Sort");
		for (small_class SM : small_class1) {
			System.out.println("Small Item " + (index1++)+" :"+Arrays.toString(SM.item));
		}
		System.out.println("***********************");


		ArrayList<double[]> Bin_list=new ArrayList<double[]>();

		for (int i=0;i<host_bins.size();i++)
			Bin_list.add(host_bins.get(i));

		boolean flag=true;int k=0;
		item_loop:
			while (k<small_class1.size())
			{
				flag=false;
				bin_loop:
					//while (i<host_ids.size() && host_ids.get(i)!=-1)
					for (int i=0;i<host_ids.size();i++)					{
						for (int j=0;j<d;j++)
						{
							if (host[host_ids.get(i)][j]>=small_class1.get(k).item[j])
								flag=true;
							else
							{
								//i++;
								flag=false;
								continue bin_loop;
							}
						}
						

						if (flag)
						{
							
							sethost_sml(small_class1.get(k).item,host_ids.get(i));
							Bin_status.put((small_class1.get(k).index)+1,(host_ids.get(i)+1));
//							for(int k1=0;k1<d;k1++)
//								Bin_list.get(i)[k1]+=small_class1.get(k).item[k1];
							k++;
							continue item_loop;
						}
						


					}
		//int size= Bin_list.size();
	//	Bin_list.add(i,small_class1.get(k).item);
	//	Bin_status.put((small_class1.get(k).index)+1,i);
		//host_ids.add(host_ids.size()-1,-1);
		if (!flag)
		  //sethost_FFD(small_class1.get(k).index);
		m=m+1;	
		k++;

			}

		System.out.println("Bins Filled Like:");
		for (int i=0;i<Bin_list.size();i++)
		{
			if (Bin_list.get(i)[0]!=0) {
			System.out.println("Bins "+(i+1)+": ");
			for(int j=0;j<d;j++)
					System.out.print(Bin_list.get(i)[j]+" ");
			}
			System.out.println("");
		}

		for(Entry<Integer, Integer> entry : Bin_status.entrySet())
	    {   //print keys and values
	         System.out.print(entry.getKey() + ":" +entry.getValue()+" , ");
	    }
	
		System.out.println(" ");

	}

private static void sethost_FFD_copy(Vm vm, int index) {
		

		boolean f=false;
    	if (the_last_host>=hostList1_copy.size()-1)
    		the_last_host=0;
		for (int k=the_last_host;k<hostList1_copy.size();k++) {
			if (k%2==1) {
			for (int i=0;i<d;i++)
		       {
				if (host_copy[k][i]-small.get(index)[i]>0)
					f=true;
				else {
					f=false;
					break;
				}
				//host_bins.get(host_ids.get(k))[i]+=small.get(index)[i];
		       }
           if (f) {
			if (hostList1_copy.get(k).isSuitableForVm(vm))
				{
				hostList1_copy.get(k).vmCreate(vm);
				vm.setHost(hostList1_copy.get(k));
				PowerVmAllocationPolicyAbstract.getVmTable1().put(vm.getUid(), hostList1_copy.get(k));
				for (int i=0;i<d;i++)
			       {
					host_copy[k][i]-=small.get(index)[i];
					
					//host_bins.get(host_ids.get(k))[i]+=small.get(index)[i];
			       }
				sml_flg[index]=1;
				host_filled[k]=1;
				host_ids.add(k);
				//				host_ids.set(host_ids.indexOf(-1),k);
				the_last_host=k;
				break;
				
			}
           }
		}
		}
		if(vm.getHost()==null)
			{
			for (int k=0;k<hostList1_copy.size();k++)
			
				if (hostList1_copy.get(k).isSuitableForVm(vm))
				{
				hostList1_copy.get(k).vmCreate(vm);
				vm.setHost(hostList1_copy.get(k));
				PowerVmAllocationPolicyAbstract.getVmTable1().put(vm.getUid(), hostList1_copy.get(k));
				for (int i=0;i<d;i++)
			       {
					host_copy[k][i]-=small.get(index)[i];
					
					//host_bins.get(host_ids.get(k))[i]+=small.get(index)[i];
			       }
				host_filled[k]=1;
				host_ids.add(k);
				break;
				}
			}
			}





private static void sethost_FFD(Vm vm, int index) {
		
	if (vm.getId()==183)
		System.out.print(vm);
		
    	if (the_last_host>=hostList1.size()-1)
    		the_last_host=0;
		for (int k=the_last_host;k<hostList1.size();k++)
		//	if (hostList1.get(k).isSuitableForVm(vm))
			if (host_org[k][0]==1 && hostList1.get(k).isSuitableForVm1(vm))
				{
				hostList1.get(k).vmCreate(vm);
				vm.setHost(hostList1.get(k));
				PowerVmAllocationPolicyAbstract.getVmTable().put(vm.getUid(), hostList1.get(k));
				for (int i=0;i<d;i++)
			       {
					host[k][i]-=small.get(index)[i];
					
					//host_bins.get(host_ids.get(k))[i]+=small.get(index)[i];
			       }
				sml_flg[vm.getId()]=1;
				host_filled[k]=1;
				host_ids.add(k);
				//				host_ids.set(host_ids.indexOf(-1),k);
				the_last_host=k;
				break;
				
			}
		if(vm.getHost()==null)
			{
			for (int k=0;k<hostList1.size();k++)
			
				if (hostList1.get(k).isSuitableForVm1(vm))
				{
				hostList1.get(k).vmCreate(vm);
				vm.setHost(hostList1.get(k));
				PowerVmAllocationPolicyAbstract.getVmTable().put(vm.getUid(), hostList1.get(k));
				for (int i=0;i<d;i++)
			       {
					host[k][i]-=small.get(index)[i];
					
					//host_bins.get(host_ids.get(k))[i]+=small.get(index)[i];
			       }
				host_filled[k]=1;
				host_ids.add(k);
				break;
				}
			}
			}
	    
		
	
private static void sethost_FFD_vbp(Vm vm, int index) {
	
//	int ind=0;
//	Vm vm=null;
//	for (int j=0;j<vmList.length;j++)
//	{ 
//		if (compareItems1(small.get(index), I[j]) && sml_flg[j]!=1)
//		{
//			sml_flg[j]=1;
//			ind=0;
//			vm=vmList1.get(j);
//			break;
//		}
//	}
	
	if (the_last_host>=hostList1.size()-1)
		the_last_host=0;
	for (int k=the_last_host;k<hostList1.size();k++)
	//	if (hostList1.get(k).isSuitableForVm(vm))
		if (hostList1.get(k).isSuitableForVm(vm))
			{
			hostList1.get(k).vmCreate(vm);
			vm.setHost(hostList1.get(k));
			PowerVmAllocationPolicyAbstract.getVmTable().put(vm.getUid(), hostList1.get(k));
			for (int i=0;i<d;i++)
		       {
				host[k][i]-=small.get(index)[i];
				
				//host_bins.get(host_ids.get(k))[i]+=small.get(index)[i];
		       }
			//sml_flg[vm.getId()]=1;
			host_filled[k]=1;
			host_ids.add(k);
			//				host_ids.set(host_ids.indexOf(-1),k);
			the_last_host=k;
			break;
			
		}
	if(vm.getHost()==null)
		{
		for (int k=0;k<hostList1.size();k++)
		
			if (hostList1.get(k).isSuitableForVm(vm))
			{
			hostList1.get(k).vmCreate(vm);
			vm.setHost(hostList1.get(k));
			PowerVmAllocationPolicyAbstract.getVmTable().put(vm.getUid(), hostList1.get(k));
			for (int i=0;i<d;i++)
		       {
				host[k][i]-=small.get(index)[i];
				
				//host_bins.get(host_ids.get(k))[i]+=small.get(index)[i];
		       }
			host_filled[k]=1;
			host_ids.add(k);
			break;
			}
		}
		}
    
	




private static void sethost_FFD_small(int [] sm) {
	
	int ind=0;
	Vm vm=null;
//	for (int j=0;j<vmList.length;j++)
//	{ 
//		if (compareItems1(small.get(index), I[index]) && sml_flg[j]!=1)
//		{
//			sml_flg[j]=1;
//			ind=0;
//			vm=vmList1.get(j);
//			break;
//		}
//	}
	while (vm_allocate[ind]!=0)
		ind++;
		
	for (int i=0;i<sm.length;i++)
	{
		vm=vmList1.get(ind);
		if (vm.getHost()!=null)
		{
			ind++;
			continue;
		}
		for (int k=0;k<hostList1.size();k++)
			if (host_org[k][0]==1 && hostList1.get(k).isSuitableForVm(vm))
				{
				if (host[k][0]-I[ind][0]<0)
					continue;
				hostList1.get(k).vmCreate(vm);
				vm.setHost(hostList1.get(k));
				PowerVmAllocationPolicyAbstract.getVmTable().put(vm.getUid(), hostList1.get(k));
				for (int j=0;j<d;j++)
			       {
					//host[k][i]-=small.get(index)[i];
					host[k][j]-=I[ind][j];
					
					//host_bins.get(host_ids.get(k))[i]+=small.get(index)[i];
			       }
				
				host_filled[k]=1;
				vm_allocate[ind]=1;
				//host_ids.set(host_ids.indexOf(-1),k);
				the_last_host=k;
				ind++;
				break;
				
			}
	
	
	if(vm.getHost()==null)
		{
		for (int k=0;k<hostList1.size();k++)
		
			if (hostList1.get(k).isSuitableForVm(vm))
			{
			hostList1.get(k).vmCreate(vm);
			vm.setHost(hostList1.get(k));
			PowerVmAllocationPolicyAbstract.getVmTable().put(vm.getUid(), hostList1.get(k));
			for (int j=0;j<d;j++)
		       {
				host[k][j]-=I[ind][j];
				
				//host_bins.get(host_ids.get(k))[i]+=small.get(index)[i];
		       }
			host_filled[k]=1;
			vm_allocate[ind]=1;
		//	host_ids.set(host_ids.indexOf(-1),k);
			ind++;
			break;
			}
		}
    
	}
}






	public static void FFD_alg(int m)
	{


		small.sort(new Comparator<double[]>() {
			public int compare(double[] o1, double[] o2) {
				double diff = - o1[0] + o2[0];
				if (diff > 0)
					return 1;
				else if (diff == 0)
					return 0;
				else
					return -1;
			}
		});

		System.out.println("***********************");
		int index = 1;
		for (double[] SM : small) {
			System.out.println("Small Item " + (index++)+" :"+Arrays.toString(SM));
		}
		System.out.println("***********************");




		ArrayList<double[]> Bin_list=new ArrayList<double[]>();

		for (int i=0;i<host_bins.size();i++)
			Bin_list.add(host_bins.get(i));

		boolean flag=true;int k=0;
		item_loop:
			while (k<small.size())
			{
				bin_loop:
					for (int i=0;i<Bin_list.size();i++)
					{
						for (int j=0;j<d-1;j++)
						{
							if ((1+epsilon)-Bin_list.get(i)[j]>=small.get(k)[j])
								flag=true;
							else
								continue bin_loop;
						}
						if (1-Bin_list.get(i)[d-1]>=small.get(k)[d-1])
							flag=true;
						else
							continue bin_loop;

						if (flag)
						{
							Bin_status.put(k+1,i+1);
							sethost_sml(small.get(k),i);

							for(int k1=0;k1<d;k1++)
								Bin_list.get(i)[k1]+=small.get(k)[k1];
							k++;
							continue item_loop;
						}
						//m=m+1;


					}
		int size= Bin_list.size();
		Bin_list.add(size,small.get(k));
		Bin_status.put(k+1,Bin_list.size());
		//sethost_sml(small.get(k),Bin_list.size(),sml_flg);
		m=m+1;	
		k++;

			}

		System.out.println("Bins Filled Like:");
		for (int i=0;i<Bin_list.size();i++)
		{
			System.out.print("Bin "+(i+1)+": ");
			for(int j=0;j<d;j++)
				System.out.print(Bin_list.get(i)[j]+" ");
			System.out.println("");
		}

		System.out.println("Small Items order in bins: "+Bin_status); 

		System.out.println("Number of used bins: "+m);

	}


	public static void FFD_algCopy(int m)
	{


		small.sort(new Comparator<double[]>() {
			public int compare(double[] o1, double[] o2) {
				double diff = - o1[0] + o2[0];
				if (diff > 0)
					return 1;
				else if (diff == 0)
					return 0;
				else
					return -1;
			}
		});

		System.out.println("***********************");
		int index = 1;
		for (double[] SM : small) {
			System.out.println("Small Item " + (index++)+" :"+Arrays.toString(SM));
		}
		System.out.println("***********************");




		ArrayList<double[]> Bin_list=new ArrayList<double[]>();

		for (int i=0;i<host_bins.size();i++)
			Bin_list.add(host_bins.get(i));

		boolean flag=true;int k=0;
		item_loop:
			while (k<small.size())
			{
				bin_loop:
					for (int i=0;i<Bin_list.size();i++)
					{
						for (int j=0;j<d-1;j++)
						{
							if ((1+epsilon)-Bin_list.get(i)[j]>=small.get(k)[j])
								flag=true;
							else
								continue bin_loop;
						}
						if (1-Bin_list.get(i)[d-1]>=small.get(k)[d-1])
							flag=true;
						else
							continue bin_loop;

						if (flag)
						{
							Bin_status.put(k+1,i+1);
							sethost_sml(small.get(k),i);

							for(int k1=0;k1<d;k1++)
								Bin_list.get(i)[k1]+=small.get(k)[k1];
							k++;
							continue item_loop;
						}
						//m=m+1;


					}
		int size= Bin_list.size();
		Bin_list.add(size,small.get(k));
		Bin_status.put(k+1,Bin_list.size());
		//sethost_sml(small.get(k),Bin_list.size(),sml_flg);
		m=m+1;	
		k++;

			}

		System.out.println("Bins Filled Like:");
		for (int i=0;i<Bin_list.size();i++)
		{
			System.out.print("Bin "+(i+1)+": ");
			for(int j=0;j<d;j++)
				System.out.print(Bin_list.get(i)[j]+" ");
			System.out.println("");
		}

		System.out.println("Small Items order in bins: "+Bin_status); 

		System.out.println("Number of used bins: "+m);

	}

//compute energy()
//{
//	
//	double min_energy=Double.MAX_VALUE;
//	double host_energy=0;
//	double sum_of_mips=0;
//	int selected_host=0;
//	for (int k=0;k<2;k++)
//	{
//		sum_of_mips=0;
//		if (small.get(index)[0]>pm_rel_cap[k][0])
//			continue;
//		if (hostList1.get(k).getVmList().size()!=0)
//		{
//			for (Vm vm1:hostList1.get(k).getVmList())
//			   sum_of_mips+=vm1.getMips();
//			sum_of_mips+=vm.getMips();
//			sum_of_mips/= hostList1.get(k).getTotalMips();
//		}
//		else
//			sum_of_mips=vm.getMips()/hostList1.get(k).getTotalMips();
//	    host_energy=compute_energy(sum_of_mips,k);
//	    if (host_energy<min_energy)
//	    {
//	    	min_energy=host_energy;
//	    	selected_host=k;
//	    }
//	}
//	int ind2=the_last_host;
//	while (!hostList1.get(the_last_host).isSuitableForVm(vm))
//		the_last_host++;
//	while (!hostList1.get(ind2).isSuitableForVm(vm))
//		if (host_org[ind2][0]!=1)
//			ind2++;
		
//	for (int i=ind2;i<hostList1.size();i++)
//		if (host_org[i][0]==1 && hostList1.get(i).isSuitableForVm(vm))
//			{
//			ind2=i;
//			break;
//			}
//	
//	  sum_of_mips=0;
//	 if (hostList1.get(the_last_host).getVmList().size()!=0)
//		{
//			for (Vm vm1:hostList1.get(the_last_host).getVmList())
//			   sum_of_mips+=vm1.getMips();
//			sum_of_mips+=vm.getMips();
//			sum_of_mips/= hostList1.get(the_last_host).getTotalMips();
//		}
//		else
//			sum_of_mips=vm.getMips()/hostList1.get(the_last_host).getTotalMips();
//	    host_energy=compute_energy(sum_of_mips,the_last_host);
//	    if (host_energy<min_energy)
//	    {
//	    	min_energy=host_energy;
//	    	selected_host=the_last_host;
//	    }
//	  
//	    if (hostList1.get(ind2).getVmList().size()!=0)
//		{
//			for (Vm vm1:hostList1.get(ind2).getVmList())
//			   sum_of_mips+=vm1.getMips();
//			sum_of_mips+=vm.getMips();
//			sum_of_mips/= hostList1.get(ind2).getTotalMips();
//		}
//		else
//			sum_of_mips=vm.getMips()/hostList1.get(ind2).getTotalMips();
//	    host_energy=compute_energy(sum_of_mips,ind2);
//	    if (host_energy<min_energy)
//	    {
//	    	min_energy=host_energy;
//	    	selected_host=ind2;
//	    }
//}

	//public static boolean solvemethod1(int m, double [][] b1,int[] host_ids)
	public static boolean solvemethod1(int m,double[][] pm_rel_cap)

	{
		boolean isSolved=false;
		try {

			IloCplex model= new  IloCplex();

			//int s=host_ids.get(host_ids.size()-1)+1;

			IloNumVar [][] x=  new IloNumVar[small.size()][host_ids.size()];
			IloIntVar [] y=  new IloIntVar[m];
			

			//b1 composed of the used capacity of each bin


			for (int i=0;i<small.size();i++)
			{
				for (int j=0;j<host_ids.size();j++)
				{
					String varname="x"+(i+1)+""+(host_ids.get(j));
					x[i][j]= model.numVar(0,1,varname);

					//x[i][j]= model.numVar(0,Double.MAX_VALUE,varname);
				} 

			}
			
//			for (int j=0;j<m;j++)
//			{
//				String varname="y"+(j+1);
//				y[j]= model.intVar(0,1,varname);
//			} 

			
			
			
//				for (int j=0;j<m;j++) {
//
//				IloLinearNumExpr cons2=model.linearNumExpr();
//				for (int i=0;i<small.size();i++)
//				{
//						cons2.addTerm(1, x[i][j]);
//
//				}
//				model.addLe(cons2,y[j]);	
//				
//			}			

			
			
			

			for (int i=0;i<small.size();i++)
			{

				IloLinearNumExpr cons2=model.linearNumExpr();
				for (int j=0;j<host_ids.size();j++) {
						cons2.addTerm(1, x[i][j]);

				}
				model.addEq(cons2,1);	
				
			}			
			
			
			for (int j=0;j<host_ids.size();j++)
			{	
				for (int l=0;l<d;l++)	
				{
					IloLinearNumExpr cons=model.linearNumExpr();
					for (int i=0;i<small.size();i++)
						cons.addTerm( small.get(i)[l],x[i][j]);
					if (host[host_ids.get(j)][l]<0 )
						host[host_ids.get(j)][l]=host[host_ids.get(j)][l]*-1;
					
					model.addLe(cons,host[host_ids.get(j)][l]);


				}

			}



			double [] cap_bin=new double [m];
			double max=0;
			//model.addLe(cons,host[j][l]);

			//model.addLe(cons,pm_rel_cap[host_ids.get(j)%2][0]-host_bins.get(j)[l]);

//			for (int i=0;i<m;i++)
//			{
//				//max=host_bins.get(i)[0];
//				max=host_org[host_ids.get(i)][0]-host_bins.get(i)[0];
//
//				for (int k=1;k<d;k++)
//					if (host_bins.get(i)[k]>max)
//						max=host_bins.get(i)[k];
//				cap_bin[i]=max;		 
//			}

////			
//			for (int i=0;i<m;i++)
//			{
//				cap_bin[i]=host_org[host_ids.get(i)][0]-host_bins.get(i)[0];		 
//			}
//
//			IloLinearNumExpr obj= model.linearNumExpr();
////
//			for (int j=0;j<host_bins.size();j++)
//				for (int i=0;i<small.size();i++)
//					obj.addTerm(cap_bin[j], x[i][j]);
//			
//
//			model.addMaximize(obj);
////			
//
	//		model.addMaximize(obj);
			//model.addMinimize(obj);





			model.exportModel("bin packing.lp");


			isSolved=model.solve();
			if(isSolved) {
				double objValue=model.getObjValue();
				System.out.println("obj_val=" + objValue);

				for (int j=0;j<host_ids.size();j++)
					for (int i=0;i< small.size();i++)

						System.out.println("x[" +(i+1)+"][" +(j+1)+"] = "+model.getValue(x[i][j]));



				
				item_loop:	
					for (int i=0;i<small.size();i++)
						for (int j=0;j<host_ids.size();j++)
							if (model.getValue(x[i][j])>0.0)
							{
								if (model.getValue(x[i][j])==1 && j<=host_ids.size())
								{
									//sethost_sml(small.get(i),j,sml_flg);
									sethost_sml(small.get(i),host_ids.get(j));
									Bin_status.put(i,host_ids.get(j));
									//Host_cap(j,i);
									continue item_loop;
								}
								else 
								{
									small_class sm_cls = new small_class();
									sm_cls.index=i;
									sm_cls.item=small.get(i);
									small_class1.add(sm_cls);

									continue item_loop;
								}

							}
				ArrayList<double[]> Bin_list=new ArrayList<double[]>();


			if (small_class1.size()!=0)	
			{ 
				FFD_ord(m);
				//abs_ord(m);
				//dynam_small(small_class1);
				//check();
			}
				
			else
			{
				for (int i=0;i<host_bins.size();i++)
					Bin_list.add(host_bins.get(i));
				System.out.println("Bins Filled Like:");
				for (int i=0;i<Bin_list.size();i++)
				{
					System.out.print("Bin "+(i+1)+": ");
					for(int j=0;j<d;j++)
						System.out.print(Bin_list.get(i)[j]+" ");
					System.out.println("");
				}

				System.out.println("Small Items order in bins: "+Bin_status); 

				System.out.println("Number of used bins: "+m);
			}

			}
			else
			{
				
				System.out.print("Model not solved!");
				sethost_null(sml_flg);
				
			}



		}
		catch (IloException ex) {
			ex.printStackTrace(); 
		}
		return isSolved;

	}


	public static boolean solvemethod1Copy(int m,double[][] pm_rel_cap)

	{
		boolean isSolved=false;
		try {

			IloCplex model= new  IloCplex();

			//int s=host_ids.get(host_ids.size()-1)+1;

			IloNumVar [][] x=  new IloNumVar[small.size()][host_ids.size()];
			IloIntVar [] y=  new IloIntVar[m];
			

			//b1 composed of the used capacity of each bin


			for (int i=0;i<small.size();i++)
			{
				for (int j=0;j<host_ids.size();j++)
				{
					String varname="x"+(i+1)+""+(host_ids.get(j));
					x[i][j]= model.numVar(0,1,varname);

					//x[i][j]= model.numVar(0,Double.MAX_VALUE,varname);
				} 

			}
			
//			for (int j=0;j<m;j++)
//			{
//				String varname="y"+(j+1);
//				y[j]= model.intVar(0,1,varname);
//			} 

			
			
			
//				for (int j=0;j<m;j++) {
//
//				IloLinearNumExpr cons2=model.linearNumExpr();
//				for (int i=0;i<small.size();i++)
//				{
//						cons2.addTerm(1, x[i][j]);
//
//				}
//				model.addLe(cons2,y[j]);	
//				
//			}			

			
			
			

			for (int i=0;i<small.size();i++)
			{

				IloLinearNumExpr cons2=model.linearNumExpr();
				for (int j=0;j<host_ids.size();j++) {
						cons2.addTerm(1, x[i][j]);

				}
				model.addEq(cons2,1);	
				
			}			
			
			
			for (int j=0;j<host_ids.size();j++)
			{	
				for (int l=0;l<d;l++)	
				{
					IloLinearNumExpr cons=model.linearNumExpr();
					for (int i=0;i<small.size();i++)
						cons.addTerm( small.get(i)[l],x[i][j]);
					if (host_copy[host_ids.get(j)][l]<0 )
						host_copy[host_ids.get(j)][l]=host_copy[host_ids.get(j)][l]*-1;
					
					model.addLe(cons,host_copy[host_ids.get(j)][l]);


				}

			}



			double [] cap_bin=new double [m];
			double max=0;
			

			model.exportModel("bin packing.lp");


			isSolved=model.solve();
			if(isSolved) {
				double objValue=model.getObjValue();
				System.out.println("obj_val=" + objValue);

				for (int j=0;j<host_ids.size();j++)
					for (int i=0;i< small.size();i++)

						System.out.println("x[" +(i+1)+"][" +(j+1)+"] = "+model.getValue(x[i][j]));



				
				item_loop:	
					for (int i=0;i<small.size();i++)
						for (int j=0;j<host_ids.size();j++)
							if (model.getValue(x[i][j])>0.0)
							{
								if (model.getValue(x[i][j])==1 && j<=host_ids.size())
								{
									//sethost_sml(small.get(i),j,sml_flg);
									sethost_smlCopy(small.get(i),host_ids.get(j));
									Bin_status.put(i,host_ids.get(j));
									//Host_cap(j,i);
									continue item_loop;
								}
								else 
								{
									small_class sm_cls = new small_class();
									sm_cls.index=i;
									sm_cls.item=small.get(i);
									small_class1.add(sm_cls);

									continue item_loop;
								}

							}
				ArrayList<double[]> Bin_list=new ArrayList<double[]>();


			if (small_class1.size()!=0)	
			{ 
				FFD_ordCopy(m);
				//abs_ord(m);
				//dynam_small(small_class1);
				//check();
			}
				
			else
			{
				for (int i=0;i<host_bins.size();i++)
					Bin_list.add(host_bins.get(i));
				System.out.println("Bins Filled Like:");
				for (int i=0;i<Bin_list.size();i++)
				{
					System.out.print("Bin "+(i+1)+": ");
					for(int j=0;j<d;j++)
						System.out.print(Bin_list.get(i)[j]+" ");
					System.out.println("");
				}

				System.out.println("Small Items order in bins: "+Bin_status); 

				System.out.println("Number of used bins: "+m);
			}

			}
			else
			{
				
				System.out.print("Model not solved!");
			//	sethost_null(sml_flg);
				
			}



		}
		catch (IloException ex) {
			ex.printStackTrace(); 
		}
		return isSolved;

	}

	
	
	
	
	
	
	
	public static boolean solvemethod1_vbp(int m,double[][] pm_rel_cap)

	{
		boolean isSolved=false;
		try {

			IloCplex model= new  IloCplex();

			//int s=host_ids.get(host_ids.size()-1)+1;

			IloNumVar [][] x=  new IloNumVar[small.size()][host_ids.size()];
			IloIntVar [] y=  new IloIntVar[m];
			

			//b1 composed of the used capacity of each bin


			for (int i=0;i<small.size();i++)
			{
				for (int j=0;j<host_ids.size();j++)
				{
					String varname="x"+(i+1)+""+(host_ids.get(j));
					x[i][j]= model.numVar(0,1,varname);

					//x[i][j]= model.numVar(0,Double.MAX_VALUE,varname);
				} 

			}
			

			

			for (int i=0;i<small.size();i++)
			{

				IloLinearNumExpr cons2=model.linearNumExpr();
				for (int j=0;j<host_ids.size();j++) {
						cons2.addTerm(1, x[i][j]);

				}
				model.addEq(cons2,1);	
				
			}			
			
			
			for (int j=0;j<host_ids.size();j++)
			{	
				for (int l=0;l<d;l++)	
				{
					IloLinearNumExpr cons=model.linearNumExpr();
					for (int i=0;i<small.size();i++)
						cons.addTerm( small.get(i)[l],x[i][j]);
					model.addLe(cons,host[host_ids.get(j)][l]);


				}

			}



			model.exportModel("bin packing.lp");


			isSolved=model.solve();
			if(isSolved) {
				double objValue=model.getObjValue();
				System.out.println("obj_val=" + objValue);

				for (int j=0;j<host_ids.size();j++)
					for (int i=0;i< small.size();i++)

						System.out.println("x[" +(i+1)+"][" +(j+1)+"] = "+model.getValue(x[i][j]));



				
				item_loop:	
					for (int i=0;i<small.size();i++)
						for (int j=0;j<host_ids.size();j++)
							if (model.getValue(x[i][j])>0.0)
							{
								if (model.getValue(x[i][j])==1 && j<=host_ids.size())
								{
									//sethost_sml(small.get(i),j,sml_flg);
									sethost_sml(small.get(i),host_ids.get(j));
									Bin_status.put(i,host_ids.get(j));
									//Host_cap(j,i);
									continue item_loop;
								}
								else 
								{
									small_class sm_cls = new small_class();
									sm_cls.index=i;
									sm_cls.item=small.get(i);
									small_class1.add(sm_cls);

									continue item_loop;
								}

							}
				ArrayList<double[]> Bin_list=new ArrayList<double[]>();


			if (small_class1.size()!=0)	
			{ 
				FFD_ord_vbp(m);
				//abs_ord(m);
				//dynam_small(small_class1);
				//check();
			}
				
			else
			{
				for (int i=0;i<host_bins.size();i++)
					Bin_list.add(host_bins.get(i));
				System.out.println("Bins Filled Like:");
				for (int i=0;i<Bin_list.size();i++)
				{
					System.out.print("Bin "+(i+1)+": ");
					for(int j=0;j<d;j++)
						System.out.print(Bin_list.get(i)[j]+" ");
					System.out.println("");
				}

				System.out.println("Small Items order in bins: "+Bin_status); 

				System.out.println("Number of used bins: "+m);
			}

			}
			else
			{
				
				System.out.print("Model not solved!");
				sethost_null(sml_flg);
				
			}



		}
		catch (IloException ex) {
			ex.printStackTrace(); 
		}
		return isSolved;

	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static boolean solvemethod1_energy(int m,double[][] pm_rel_cap)

	{
		boolean isSolved=false;
		try {

			IloCplex model= new  IloCplex();


			//m=hostList1.size();
			IloNumVar [][] x=  new IloNumVar[small.size()][m];

			//b1 composed of the used capacity of each bin


			for (int i=0;i<small.size();i++)
			{
				for (int j=0;j<m;j++)
				{
					String varname="x"+(i+1)+""+(j+1);
					x[i][j]= model.numVar(0,Double.MAX_VALUE,varname);
				} 

			}


			for (int i=0;i<small.size();i++)
			{

				IloLinearNumExpr cons2=model.linearNumExpr();
				for (int j=0;j<m;j++)
					cons2.addTerm(1, x[i][j]);
				model.addEq(cons2,1);	
				
			}			


			for (int j=0;j<m;j++)
			{	
				for (int l=0;l<d;l++)	
				{
					IloLinearNumExpr cons=model.linearNumExpr();
					for (int i=0;i<small.size();i++)
						cons.addTerm( small.get(i)[l],x[i][j]);
					//model.addLe(cons,pm_rel_cap[host_ids.get(j)%2][0]-host_bins.get(j)[l]);
					model.addLe(cons,host[host_ids.get(j)][l]);
					//model.addLe(cons,host[j][l]);

				}

			}



			double [] cap_bin=new double [m];
			
			for (int i=0;i<m;i++)
			{
				cap_bin[i]=host[host_ids.get(i)][0];	
				//cap_bin[i]=host[i][0];
			}

			IloLinearNumExpr obj= model.linearNumExpr();

			for (int j=0;j<m;j++)
				for (int i=0;i<small.size();i++)
					obj.addTerm(cap_bin[j], x[i][j]);
			

			model.addMaximize(obj);
       //  model.addMinimize(obj);





			model.exportModel("bin packing.lp");


			isSolved=model.solve();
			if(isSolved) {
				double objValue=model.getObjValue();
				System.out.println("obj_val=" + objValue);

				for (int j=0;j<m;j++)
					for (int i=0;i< small.size();i++)

						System.out.println("x[" +(i+1)+"][" +(j+1)+"] = "+model.getValue(x[i][j]));



				
				item_loop:	
					for (int i=0;i<small.size();i++)
						for (int j=0;j<m;j++)
							if (model.getValue(x[i][j])>0.0)
							{
								if (model.getValue(x[i][j])==1 && j<=host_ids.size())
									{
									//sethost_sml(small.get(i),host_ids.get(j),sml_flg);
									Bin_status.put(i+1,j+1);
									Host_cap(j,i);
									continue item_loop;
								}
								else 
								{
									small_class sm_cls = new small_class();
									sm_cls.index=i;
									sm_cls.item=small.get(i);
									small_class1.add(sm_cls);

									continue item_loop;
								}

							}
				ArrayList<double[]> Bin_list=new ArrayList<double[]>();


			if (small_class1.size()!=0)	
			{ 
				FFD(m,pm_rel_cap);
				//dynam_small(small_class1);
				//check();
			}
				
			else
			{
				for (int i=0;i<host_bins.size();i++)
					Bin_list.add(host_bins.get(i));
				System.out.println("Bins Filled Like:");
				for (int i=0;i<Bin_list.size();i++)
				{
					System.out.print("Bin "+(i+1)+": ");
					for(int j=0;j<d;j++)
						System.out.print(Bin_list.get(i)[j]+" ");
					System.out.println("");
				}

				System.out.println("Small Items order in bins: "+Bin_status); 

				System.out.println("Number of used bins: "+m);
			}

			}
			else
			{
				
				System.out.print("Model not solved!");
				sethost_null(sml_flg);
				
			}



		}
		catch (IloException ex) {
			ex.printStackTrace(); 
		}
		return isSolved;

	}


	private static void check() {
      int i=0;
	  while (vm_allocate[i]!=0)
		  i++;
	  int[] sm=new int[vmList1.size()-i];
	  int ind=0;
	  if (i!=vmList1.size())
	  {
		  while (i<vmList1.size())
	  
		 {   
			  sm[ind++]=i;
			   i++;
		  }
		  sethost_FFD_small(sm);
	}
	}








	private static void dynam_small(ArrayList<small_class> small_class1) {
		
		int size_items=small_class1.size();
		double[] sm=small.get(0);
		double [] size=new double [d];
		double [][] pm_rel= {{0.3110,86.4955},{0.4414,92.9955}};//,{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41},{44.136,92.995},{65.182,105.41}};
		double[][] pm_rel_cap= {{0.6992481203007519, 1.0, 1.0},{1.0, 1.0, 1.0}};
		//int[] counters1=new int[3];
		requests= new int [1];
		requests[0]=size_items;
		posBins.clear();
		int[] counters = new int[1];
		getBins_small(size,counters,0,posBins,sm);
		//getBins1_type2(size,counters,0,posBins);

		bins_energy=new double[posBins.size()];
		bins_id=new double[posBins.size()];

		long  dynam_array_length=1;
		for (int i=0;i<requests.length;i++)
			dynam_array_length*=(requests[i]+1);
		dynam_array=new int [(int) dynam_array_length];
		dynam_array_energy=new double [(int) dynam_array_length];
		dynam_pos=new int [(int) dynam_array_length][1];
		dynam_pos1=new int [(int) dynam_array_length];
		dynam_pos1_energy=new double[(int) dynam_array_length];
		pbind=new int [posBins.size()];
		level_list=new int [hostList1.size()];
		int [] temp=new int [requests.length];
		for (int i=0;i<temp.length;i++)
			temp[i]=-1;
		
		
		double sum_of_util=0;double min_energy=Double.MAX_VALUE;
		double energy_temp=0;
		
		for(int i=1;i<posBins.size();i++)
		{
			min_energy=Double.MAX_VALUE;
			sum_of_util=0;
			for (int j=0;j<requests.length;j++)
			if (posBins.get(i)[j]!=0)
				sum_of_util+=(posBins.get(i)[j]*sm[0]);
			for (int k=0;k<pm_rel.length;k++)
			{
				
				if (sum_of_util>pm_rel_cap[k][0])
						continue;
				     energy_temp= compute_energy(sum_of_util/pm_rel_cap[k][0],k);
				    if (energy_temp<min_energy)
				    	{
				    	min_energy=energy_temp;
				    	bins_id[i]=k;
				    	}
					            
			}
			 bins_energy[i]=min_energy;
				
		}
		for (int i=1;i<posBins.size();i++)//remove
		{
			int index=findIndex(posBins.get(i));
			pbind[i]=index;
			dynam_array[index]=1;
			dynam_array_energy[index]=bins_energy[i];
			dynam_pos1[index]=-1;
			for (int k=0;k<requests.length;k++)
			{
				dynam_pos[index][k]=-1;

			}
		}

		level_list=new int[posBins.size()];
		
		double result1=0;
	////****************Dynamic Bin packing**************************************//
		int l=1;int u=vmList.length;
		int m=(l+u)/2;
		guss_m=(int) (m+Math.ceil((epsilon*m)/2));
		
		result1=Dynamic_Bins_energy(0,requests);
		
		System.out.println("Number of Bins:"+ result1);
		
		Filled_hosts_small();
		
	}








	private static void Host_cap(int j, int i) {
		for(int k=0;k<d;k++)
			host[j][k]-=small.get(i)[k];
	}



	public static void	Dynamic_Bins1(int [] requests)
	{ 


		int [] pbind=new int [posBins.size()];
//		for (int i=1;i<posBins.size();i++)//remove
//		{
//			int index=findIndex(posBins.get(i));
//			pbind[i]=index;
//			dynam_array[index]=1;
//			for (int k=0;k<round1.size();k++)
//				dynam_pos[index][k]=-1;
//		}
		//ArrayList<int []> Total_states=new ArrayList<int []>();
		//arrayind(counters,0, Total_states); 
		for(int i=1;i<dynam_array1.length;i++) {
			//if(i==0) continue;
			
				int min=Integer.MAX_VALUE;
				posLoop:
					for(int j=1;j<posBins1.size();j++) {
						if(dynam_array1[findIndex1(posBins1.get(j))]!=1) {
						
						int [] t1=new int [req1.length];
						for(int m=0;m<req1.length;m++) {
							if (requests[m]-posBins1.get(j)[m]>=0)
								t1[m]=requests[m]-posBins1.get(j)[m];
							else
								continue posLoop;

						}

						int temp=dynam_array1[findIndex1(t1)];//recursive
						if(temp<min) {
							min=temp;
//							for (int k=0;k<req1.length;k++)
//								dynam_pos1[i][k]=t1[k];
						}


					
				//						dynam_array[i]=1+min;
				dynam_array1[findIndex1(posBins1.get(j))]=1+min;

						}
			}

		}
		System.out.println("Total Number of Bins:" +(dynam_array1[dynam_array1.length-1]));

		ArrayList <int[]> Final_bins=new ArrayList<int[]>();
		Final_bins.add(dynam_pos[dynam_array.length-1]);
		int index=findIndex(dynam_pos[dynam_array.length-1]);

		while (dynam_pos[index][0]!=-1)
		{
			Final_bins.add(dynam_pos[index]);
			index=findIndex(dynam_pos[index]);
		}
		for (int i=0;i<pbind.length;i++)
			if (pbind[i]==index)
				Final_bins.add(posBins1.get(i));


		int[][] Used_bins=new int [Final_bins.size()][round1.size()];
		for (int i=0;i<round1.size();i++)
			Used_bins[0][i]=Final_bins.get(Final_bins.size()-1)[i];
		int k=1;
		for (int i=Final_bins.size()-2;i>=0;i--)
		{
			for (int j=0;j<Final_bins.get(i).length;j++)
			{ if (i!=0) {
				Used_bins[k][j]=(Final_bins.get(i-1)[j]-Final_bins.get(i)[j]);
			}

			else {
				Used_bins[Used_bins.length-1][j]=(round1.get(j).size()-Final_bins.get(i)[j]);

			}
			}
			k++;
		}

		System.out.println("Used Bins Items:");
		for (int i=0;i<Used_bins.length;i++)
		{
			for (int j=0;j<round1.size();j++)
				System.out.print(Used_bins[i][j]+" ");

			System.out.println(" ");
		}

		int m= dynam_array[dynam_array.length-1];
		double size[]=new double[d];
		double [][] Used_bins_cap=new double[m][d];
		for (int i = 0; i< Used_bins.length; i++)
		{

			for (int j=0;j<Used_bins[i].length;j++)
			{
				if (Used_bins[i][j]!=0)
					size=round1.get(j).get(0);       
				for (int k1=0;k1<d;k1++) 
					Used_bins_cap[i][k1]+=(size[k1]*Used_bins[i][j]);
			}
		}
		
		for (int i=0;i<Used_bins.length;i++)
		{
			for (int j=0;j<round1.size();j++)
				System.out.print(Used_bins_cap[i][j]+" ");

			System.out.println(" ");
		}

		
		
		
		
		
		if (small.size()!=0)
		  solvemethod(m, Used_bins_cap);

	}








	@Override
	public boolean isHostOverUtilized(PowerHost host) {
		// TODO Auto-generated method stub
		return false;
	}








	@Override
	protected boolean isHostLowUtilized(PowerHost host) {
		// TODO Auto-generated method stub
		return false;
	}








	@Override
	protected boolean isHostMiddleUtilized(PowerHost host) {
		// TODO Auto-generated method stub
		return false;
	}








	@Override
	protected boolean isHostMediumUtilized(PowerHost host) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
	

}


























