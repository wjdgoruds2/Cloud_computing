import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressResult;
import com.amazonaws.services.ec2.model.AvailabilityZone;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeImagesRequest;
import com.amazonaws.services.ec2.model.DescribeImagesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.amazonaws.services.ec2.model.Image;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.IpRange;
import com.amazonaws.services.ec2.model.RebootInstancesRequest;
import com.amazonaws.services.ec2.model.RebootInstancesResult;
import com.amazonaws.services.ec2.model.Region;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.SecurityGroup;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult;
import com.amazonaws.services.ec2.model.DeleteSecurityGroupRequest;
import com.amazonaws.services.ec2.model.DeleteSecurityGroupResult;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsRequest;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult;

public class ec2_instance {
	static AmazonEC2 ec2;
    private static void init() throws Exception {
    	ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();

		try {
			credentialsProvider.getCredentials();
		} catch (Exception e) {
		throw new AmazonClientException(

			"Cannot load the credentials from the credential profiles file. " +
			"Please make sure that your credentials file is at the correct " +
			"location (~/.aws/credentials), and is in valid format.",e);
		}
		ec2 = AmazonEC2ClientBuilder.standard()
		.withCredentials(credentialsProvider)
		.withRegion("us-east-2") 
		.build();
    }

    public static void main(String[] args) throws Exception {
    	init();
    	Scanner menu = new Scanner(System.in);
    	Scanner id_string = new Scanner(System.in);
    	int num = 0;
		
		Scanner s = new Scanner(System.in);
		boolean istrue=true;
		while(istrue) {
			System.out.println(" ");
			System.out.println(" ");
			System.out.println("------------------------------------------------------------");
			System.out.println(" 	Amazon AWS Control Panel using SDK ");
			System.out.println(" ");
			System.out.println(" 		2017038062  정해경");
			System.out.println(" ");
			System.out.println(" Cloud Computing, Computer Science Department ");
			System.out.println(" 		at Chungbuk National University ");
			System.out.println("------------------------------------------------------------");
			System.out.println(" 1. list instance	2. available zones ");
			System.out.println(" 3. start instance	4. available regions ");
			System.out.println(" 5. stop instance	6. create instance ");
			System.out.println(" 7. reboot instance	8. list images ");
			System.out.println(" 9. list security group");
			System.out.println(" 10. describe security group");
			System.out.println(" 11. create security group");
			System.out.println(" 12. delete security group");
			System.out.println(" 99. quit ");
			System.out.println("------------------------------------------------------------");
			System.out.print("Enter an integer: ");
			num=menu.nextInt();
		    
		    if (num==1) {
		    	list_instance();
		    }
		    else if (num==2) {
		    	available_zones();
		    }
		    else if (num==3) {
		    	start_instance();
		    }
		    else if (num==4) {
		    	available_regions();
		    }
		    else if (num==5) {
		    	stop_instance();
		    }
		    else if (num==6) {
		    	create_instance();
		    }
		    else if (num==7) {
		    	reboot_instance();
		    }
		    else if (num==8) {
		    	list_images();
		    }
		    else if (num==9) {
		    	list_security_group();
		    }
		    else if (num==10) {
		    	describe_security_group();
		    }
		    else if (num==11) {
		    	create_security_group();
		    }
		    else if (num==12) {
		    	delete_security_group();
		    }
		    else if (num==99) {
		    	istrue=false;
		    }
		  }
	}
	
	public static void list_instance() {
		System.out.println("Listing instances....");
		boolean done = false;
		DescribeInstancesRequest request = new DescribeInstancesRequest();
		while(!done) {
			DescribeInstancesResult response = ec2.describeInstances(request);
			
			for(Reservation reservation : response.getReservations()) {
					for(Instance instance : reservation.getInstances()) {
						System.out.printf(
						"[id] %s, " +
						"[AMI] %s, " +
						"[type] %s, " +
						"[state] %10s, " +
						"[monitoring state] %s",
						instance.getInstanceId(),
						instance.getImageId(),
						instance.getInstanceType(),
						instance.getState().getName(),
						instance.getMonitoring().getState());
						}
					System.out.println();
					}
		request.setNextToken(response.getNextToken());
		
		if(response.getNextToken() == null) {
			done = true;
			}
		}
	}
	
	public static void available_zones() {
		DescribeAvailabilityZonesResult zones_response =
			    ec2.describeAvailabilityZones();

			for(AvailabilityZone zone : zones_response.getAvailabilityZones()) {
			    System.out.printf(
			        "Found availability zone %s " +
			        "with status %s " +
			        "in region %s",
			        zone.getZoneName(),
			        zone.getState(),
			        zone.getRegionName());
			}
	}
	
	public static void start_instance() {
		String instance_id=null;
		System.out.print("Enter instance id: ");  	
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			instance_id = br.readLine();
		} catch(IOException e){
			e.printStackTrace();
		}
		System.out.printf("Starting.... ",instance_id);

		StartInstancesRequest request = new StartInstancesRequest()
		    .withInstanceIds(instance_id);

		ec2.startInstances(request);
		System.out.printf("Successfully started instance %s",instance_id);
	}
	
	public static void available_regions() {
		DescribeRegionsResult regions_response = ec2.describeRegions();

		for(Region region : regions_response.getRegions()) {
		    System.out.printf(
		        "Found region %s " +
		        "with endpoint %s",
		        region.getRegionName(),
		        region.getEndpoint());
		}
	}
	
	public static void stop_instance() {
		String instance_id=null;
		System.out.print("Enter instance id: ");  
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			instance_id = br.readLine();
		} catch(IOException e){
			e.printStackTrace();
		}

		StopInstancesRequest request = new StopInstancesRequest()
		    .withInstanceIds(instance_id);

		ec2.stopInstances(request);
		System.out.printf("Successfully stop instance %s",instance_id);
	}
	
	public static void create_instance() {
		String ami_id=null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			ami_id = br.readLine();
		} catch(IOException e){
			e.printStackTrace();
		}
		System.out.print("Enter AMI id: ");  
		
		RunInstancesRequest run_request = new RunInstancesRequest()
			    .withImageId(ami_id)
			    .withInstanceType(InstanceType.T2Micro)
			    .withMaxCount(1)
			    .withMinCount(1);

		RunInstancesResult run_response = ec2.runInstances(run_request);

		String reservation_id = run_response.getReservation().getInstances().get(0).getInstanceId();

        System.out.printf(
            "Successfully started EC2 instance %s based on AMI %s",reservation_id, ami_id);
	}
	
	public static void reboot_instance() {
		String instance_id=null;
		System.out.print("Enter instance id: ");  
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			instance_id = br.readLine();
		} catch(IOException e){
			e.printStackTrace();
		}  
		System.out.printf("Rebooting.... ",instance_id);

		RebootInstancesRequest request = new RebootInstancesRequest()
		    .withInstanceIds(instance_id);

		RebootInstancesResult response = ec2.rebootInstances(request);
		System.out.printf("Successfully rebooted instance %s",instance_id);
	}
	
	public static void list_images() {
		DescribeImagesRequest request = new DescribeImagesRequest();
		request.withOwners("self");
		System.out.println("Listing images....");
		
		DescribeImagesResult result = ec2.describeImages(request);
	    List<Image> imageList = result.getImages();
	    for(Image image : imageList){
	    	
	    	String image_id=image.getImageId();
	    	String name=image.getName();
	    	String owner=image.getOwnerId();
	    	System.out.printf("[ImageID] %s, [Name] %s, [Owner] %s",image_id,name,owner);
	    
	    }
	}
	public static void list_security_group() {
		DescribeSecurityGroupsRequest request =new DescribeSecurityGroupsRequest();
		System.out.println("Listing security group....");
		DescribeSecurityGroupsResult response =ec2.describeSecurityGroups(request);
		for(SecurityGroup group : response.getSecurityGroups()) {
            System.out.printf(
                "\n[security group id] : %s  " +
                "[group name] : %s  " +
                "[vpc id] : %s  " +
                "[description] : %s  ",
                group.getGroupId(),
                group.getGroupName(),
                group.getVpcId(),
                group.getDescription());
		}
	}
	public static void describe_security_group() {
		String sercurity_group_id=null;
		System.out.print("Enter security group id: ");  
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			sercurity_group_id = br.readLine();
		} catch(IOException e){
			e.printStackTrace();
		}  

		DescribeSecurityGroupsRequest request =
	            new DescribeSecurityGroupsRequest()
	                .withGroupIds(sercurity_group_id);

	        DescribeSecurityGroupsResult response =
	            ec2.describeSecurityGroups(request);
	        for(SecurityGroup group : response.getSecurityGroups()) {
	            System.out.printf(
	                "\nFound security group with id : %s " +
	                "\nOwnerId : %s " +
	                "\nTags : %s " +
	                "\nvpc id : %s " +	                    
	                "\ndescription : %s",
	                group.getGroupId(),
	                group.getOwnerId(),
	                group.getTags(),
	                group.getVpcId(),
	                group.getDescription());
	            List<IpPermission> ipdes = group.getIpPermissions();
	            for (int k = 0; k < ipdes.size(); k++) {
	            	System.out.println();
	            	System.out.println("============================");
	    			System.out.println((k+1)+"번째 protocol description");
	    			System.out.println("============================");
	            	System.out.printf("[IpProtocol] : %s, [FromPort] : %s, [ToPort] : %s, [getIpv4Ranges] : %s ",
	            			ipdes.get(k).getIpProtocol(),
	    	                ipdes.get(k).getFromPort(),
	    	                ipdes.get(k).getToPort(),
	    	                ipdes.get(k).getIpv4Ranges());
	            	System.out.println();
	     
	            }
	            
	    	}   
	        
	}
	public static void create_security_group() {
		String group_name=null;
		String group_desc=null;
		String vpc_id=null;
		int cnt=0;
		System.out.printf("-Enter security group_name: ");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			group_name = br.readLine();
		} catch(IOException e){
			e.printStackTrace();
		}
		System.out.printf("-Enter security group_desc: ");  
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			group_desc = br.readLine();
		} catch(IOException e){
			e.printStackTrace();
		}
		System.out.printf("-Enter security vpc_id: ");  
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			vpc_id = br.readLine();
		} catch(IOException e){
			e.printStackTrace();
		}

		CreateSecurityGroupRequest create_request = new
		    CreateSecurityGroupRequest()
		        .withGroupName(group_name)
		        .withDescription(group_desc)
		        .withVpcId(vpc_id);

		CreateSecurityGroupResult create_response =ec2.createSecurityGroup(create_request);
		
		IpRange ip_range = new IpRange().withCidrIp("0.0.0.0/0");
		Scanner scanner = new Scanner(System.in);
		System.out.print("Number of inbound to set: ");
        int count = scanner.nextInt();
        List<IpPermission> ipPermissions = new ArrayList<>();

        for (int i = 0; i < count; i++) {
        	String protocolname=null;
        	int endport=0;
        	int startport=0;
        	System.out.println("============================================================");
			System.out.println((i+1)+"번째 inbound 설정");
			System.out.println("============================================================");
        	System.out.println(" 1. 사용자 지정 TCP 	2. 사용자 지정 UDP ");
    		System.out.println(" 3. 모든 TCP		4. 모든 TCP ");
    		System.out.println(" 5. 모든 ICMP-IPv4	6. SSH ");
    		System.out.println(" 7. SMTP		8. HTTP ");
    		System.out.println("============================================================");
        	System.out.print("--->Enter an protocol: ");
        	int protocol = scanner.nextInt();
        	if (protocol==1) {
        		System.out.print("--->Enter an startport: ");
            	startport = scanner.nextInt();
            	
            	System.out.print("--->Enter an endport: ");
            	endport = scanner.nextInt();
        		ipPermissions.add(
                        new IpPermission()
            	            .withIpProtocol("tcp")
            	            .withToPort(endport)
            	            .withFromPort(startport)
            	            .withIpv4Ranges(ip_range));
		    }
		    else if (protocol==2) {
		    	System.out.print("--->Enter an startport: ");
	        	startport = scanner.nextInt();
	        	
	        	System.out.print("--->Enter an endport: ");
	        	endport = scanner.nextInt();
		    	ipPermissions.add(
                        new IpPermission()
            	            .withIpProtocol("udp")
            	            .withToPort(endport)
            	            .withFromPort(startport)
            	            .withIpv4Ranges(ip_range));
		    }
		    else if (protocol==3) {
		    	ipPermissions.add(
                        new IpPermission()
            	            .withIpProtocol("tcp")
            	            .withToPort(65535)
            	            .withFromPort(0)
            	            .withIpv4Ranges(ip_range));
		    }
		    else if (protocol==4) {
		    	ipPermissions.add(
                        new IpPermission()
            	            .withIpProtocol("udp")
            	            .withToPort(65535)
            	            .withFromPort(0)
            	            .withIpv4Ranges(ip_range));
		    }
		    else if (protocol==5) {
		    	ipPermissions.add(
                        new IpPermission()
            	            .withIpProtocol("icmp-ipv4")
            	            .withIpv4Ranges(ip_range));
		    }
		    else if (protocol==6) {
		    	ipPermissions.add(
                        new IpPermission()
            	            .withIpProtocol("tcp")
            	            .withToPort(22)
            	            .withFromPort(22)
            	            .withIpv4Ranges(ip_range));
		    }
		    else if (protocol==7) {
		    	ipPermissions.add(
                        new IpPermission()
            	            .withIpProtocol("tcp")
            	            .withToPort(25)
            	            .withFromPort(25)
            	            .withIpv4Ranges(ip_range));
		    }
		    else if (protocol==8) {
		    	ipPermissions.add(
                        new IpPermission()
            	            .withIpProtocol("tcp")
            	            .withToPort(80)
            	            .withFromPort(80)
            	            .withIpv4Ranges(ip_range));
		    }
        	System.out.println();	
        }
        
        AuthorizeSecurityGroupIngressRequest auth_request = new
            AuthorizeSecurityGroupIngressRequest()
                .withGroupName(group_name)
                .withIpPermissions(ipPermissions);

        AuthorizeSecurityGroupIngressResult auth_response =
            ec2.authorizeSecurityGroupIngress(auth_request);

        System.out.printf(
            "Successfully added ingress policy to security group %s",group_name);
	}
	public static void delete_security_group() {
		String delete_group_id=null;
		System.out.print("Enter security group id: ");  
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			delete_group_id = br.readLine();
		} catch(IOException e){
			e.printStackTrace();
		}  
		System.out.println("Deleting.... "+delete_group_id);

		DeleteSecurityGroupRequest request = new DeleteSecurityGroupRequest()
		    .withGroupId(delete_group_id);

		DeleteSecurityGroupResult response = ec2.deleteSecurityGroup(request);
		System.out.printf("Successfully deleted security group id %s",delete_group_id);
	}
	
	
}
