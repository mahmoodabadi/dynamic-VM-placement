package vbproblem.src;


import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.VmSchedulerTimeSharedOverSubscription;
import org.cloudbus.cloudsim.examples.power.custom.CustomPowerModel;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyAbstract;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationAbstract;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.matching.blossom.v5.KolmogorovWeightedPerfectMatching;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.shape.mxImageShape;

import com.mxgraph.view.mxGraph;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

public class biparti {
	
	public static List<Vm> vmList = new ArrayList<Vm>();
    public static List<PowerHost> hostList1 = new ArrayList<PowerHost>();
    public static List<PowerHost> hostList = new ArrayList<PowerHost>();
    public static List<Vm> vmsTag=new ArrayList<Vm>();
    
    public static int[] vmsTagArray;
	
    public static void main(String[] args) {
    String vmm = "Xen";
	vmCreate(1,2,200,1,512,1000,10000,vmm,new CloudletSchedulerTimeShared());
	vmCreate(2,2,300,1,512,1000,10000,vmm,new CloudletSchedulerTimeShared());
	vmCreate(3,2,250,1,512,1000,10000,vmm,new CloudletSchedulerTimeShared());
	vmCreate(4,2,100,1,512,1000,10000,vmm,new CloudletSchedulerTimeShared());
	vmCreate(5,2,300,1,512,1000,10000,vmm,new CloudletSchedulerTimeShared());
	vmCreate(6,2,200,1,512,1000,10000,vmm,new CloudletSchedulerTimeShared());
	vmCreate(7,2,250,1,512,1000,10000,vmm,new CloudletSchedulerTimeShared());
	vmCreate(8,2,100,1,512,1000,10000,vmm,new CloudletSchedulerTimeShared());
	vmCreate(9,2,300,1,512,1000,10000,vmm,new CloudletSchedulerTimeShared());
	
	
	
	
	
	
	List<Pe> peList = new ArrayList<Pe>();

	 int mips = 3000;
	// mips = 800;

	// int hostId=0;
    int ram = 2048; //host memory (MB)
     //long storage = 1000000; //host storage
   int  bw = 10000;


   // 3. Create PEs and add these into a list.
	peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

	
	hostCreate1(1, new RamProvisionerSimple(2048),new BwProvisionerSimple(10000),1000000,peList,new VmSchedulerTimeShared(peList));
	hostCreate(11, new RamProvisionerSimple(2048),new BwProvisionerSimple(10000),1000000,peList,new VmSchedulerTimeShared(peList));
	hostCreate1(2, new RamProvisionerSimple(2048),new BwProvisionerSimple(10000),1000000,peList,new VmSchedulerTimeShared(peList));
	hostCreate(22, new RamProvisionerSimple(2048),new BwProvisionerSimple(10000),1000000,peList,new VmSchedulerTimeShared(peList));
	hostCreate1(3, new RamProvisionerSimple(2048),new BwProvisionerSimple(10000),1000000,peList,new VmSchedulerTimeShared(peList));
	hostCreate(33, new RamProvisionerSimple(2048),new BwProvisionerSimple(10000),1000000,peList,new VmSchedulerTimeShared(peList));
	
	
	hostList.get(0).vmCreate(vmList.get(0));
	hostList.get(0).vmCreate(vmList.get(1));
	hostList.get(0).vmCreate(vmList.get(2));
	
	hostList.get(1).vmCreate(vmList.get(3));
	hostList.get(1).vmCreate(vmList.get(4));
	hostList.get(1).vmCreate(vmList.get(5));

	
	hostList.get(2).vmCreate(vmList.get(6));
	hostList.get(2).vmCreate(vmList.get(7));
	hostList.get(2).vmCreate(vmList.get(8));

	
	hostList1.get(0).vmCreate(vmList.get(1));
	hostList1.get(0).vmCreate(vmList.get(4));
	hostList1.get(0).vmCreate(vmList.get(5));
	
	hostList1.get(1).vmCreate(vmList.get(6));
	hostList1.get(1).vmCreate(vmList.get(3));
	hostList1.get(1).vmCreate(vmList.get(7));

	
	hostList1.get(2).vmCreate(vmList.get(0));
	hostList1.get(2).vmCreate(vmList.get(2));
	hostList1.get(2).vmCreate(vmList.get(8));
	System.getProperty("java.class.path");
	

	
	computeMaxWeightMatching(hostList,hostList1);
	
	
}


    
    
    private static void migrateProcess() {

        int i=0;
        while (i<vmsTagArray.length) {
        	Host h1=null;
        	Host h2=null;
        	if (vmsTagArray[i]==0) {
        		Vm v=vmsTag.get(i);
        			        		
        			L1:
        		       	for (Host h:hostList)
        		       		for (Vm v1:h.getVmList())
        		       			if (v1.equals(v)) {
        		       			  h1=h;
        		       			  break L1;
        		       			}
        			L2:
        		       	for (Host h:hostList1)
        		       		for (Vm v1:h.getVmList())
        		       			if (v1.equals(v)) {
        		       			  h2=h;
        		       			  break L2;
        		       			}
        			
        			
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




	public static void visualizeBipartiteGraph(Graph<Host, DefaultWeightedEdge> bipartiteGraph) {
        // Create a JGraphX graph
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        // Add vertices to the JGraphX graph
        Map<Host, Object> vertexMap = new HashMap<>();
        for (Host host : bipartiteGraph.vertexSet()) {
            vertexMap.put(host, graph.insertVertex(parent, null, host.getId(), 0, 0, 80, 30));
        }

        // Add edges to the JGraphX graph
        for (DefaultWeightedEdge edge : bipartiteGraph.edgeSet()) {
            Host source = bipartiteGraph.getEdgeSource(edge);
            Host target = bipartiteGraph.getEdgeTarget(edge);
            graph.insertEdge(parent, null, bipartiteGraph.getEdgeWeight(edge), vertexMap.get(source), vertexMap.get(target));
        }

        // Apply a circular layout
        mxCircleLayout layout = new mxCircleLayout(graph);
        layout.execute(graph.getDefaultParent());

        // Create a graph component and display it
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        JFrame frame = new JFrame("Bipartite Graph");
        frame.getContentPane().add(graphComponent, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }   
    
    
    
private static void vmCreate(int vmid, int brokerid, int mips, int pesNumber, int ram, int bw, int size, String vmm,
		CloudletSchedulerTimeShared cloudletSchedulerTimeShared) {
	Vm vm = new Vm(vmid, brokerid, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
	vmList.add(vm);
}

private static void hostCreate(int hostId, RamProvisionerSimple ramProvisionerSimple,
		BwProvisionerSimple bwProvisionerSimple, int j, List<Pe> peList,
		VmSchedulerTimeShared vmSchedulerTimeShared) {
Double[] HOST_POWER={93.7, 97.0, 101.0, 105.0, 110.0, 116.0, 121.0, 125.0, 129.0, 133.0, 135.0};
	
	PowerHost newh=new PowerHostUtilizationHistory(
			hostId,
			new RamProvisionerSimple(2048),
			new BwProvisionerSimple(10000),
			j,
			peList,
			new VmSchedulerTimeSharedOverSubscription(peList),
			new CustomPowerModel(HOST_POWER));
	hostList.add(newh);
}

private static void hostCreate1(int hostId, RamProvisionerSimple ramProvisionerSimple,
		BwProvisionerSimple bwProvisionerSimple, int storage, List<Pe> peList,
		VmSchedulerTimeShared vmSchedulerTimeShared) {
	
	
	Double[] HOST_POWER={93.7, 97.0, 101.0, 105.0, 110.0, 116.0, 121.0, 125.0, 129.0, 133.0, 135.0};
	
	PowerHost newh=new PowerHostUtilizationHistory(
			hostId,
			new RamProvisionerSimple(2048),
			new BwProvisionerSimple(10000),
			storage,
			peList,
			new VmSchedulerTimeSharedOverSubscription(peList),
			new CustomPowerModel(HOST_POWER));
	hostList1.add(newh);
	
	
}


	
	
	
	
	    public static void computeMaxWeightMatching(List<PowerHost> hostList, List<PowerHost> hostList1) {
	        // Create the bipartite graph
	        Graph<Host, DefaultWeightedEdge> bipartiteGraph = createBipartiteGraph(hostList, hostList1);

	       // visualizeBipartiteGraph(bipartiteGraph);
	        
	        // Compute the maximum weight matching
	        //KolmogorovWeightedPerfectMatching<Host, DefaultWeightedEdge> matching = new KolmogorovWeightedPerfectMatching<Host, DefaultWeightedEdge>(bipartiteGraph);
	        Map<Host, Host> matchedHosts1 =getMaxWeightedEdges(hostList,hostList1,bipartiteGraph);
	          
	        	
	        
	        
	        System.out.println("This is the correct output");
	        // Print the matching
	        for (Map.Entry<Host, Host> entry : matchedHosts1.entrySet()) {
	            System.out.println("Host " + entry.getKey().getId() + " is matched with Host " + entry.getValue().getId());
	        }
	        
	        
	        migrateProcess();
	    }

	    private static Graph<Host, DefaultWeightedEdge> createBipartiteGraph(List<PowerHost> hostList, List<PowerHost> hostList1) {
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
	                    commonVmSize += vm1.getCurrentRequestedTotalMips();
	                         }
	                	//commonVmSize++;
	                }
	            
	        }
	        
	        
	        
	        return commonVmSize;
	    }
	    
	    
	    public static Map<Host, Host> getMaxWeightedEdges(List<PowerHost> hostlist1, List<PowerHost> hostList2, Graph<Host, DefaultWeightedEdge> bipartiteGraph) {
	    	Map<Host, Host> matchedHosts = new HashMap<>();

	        // Iterate through the edges and find the maximum weight matching
	        Set<DefaultWeightedEdge> usedEdges = new HashSet<>();
	        int i=0;
	        while (i < Math.min(hostList1.size(), hostList2.size())) {
	            // Find the edge with the maximum weight
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
	    
	    
	}
	


	

