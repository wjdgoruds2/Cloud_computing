import java.util.Scanner;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.Tag;
public class ec2_instance {
	private AmazonEC2 ec2;
    private void init() throws Exception {
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
		.withRegion("us-east-1") 
		.build();
    }

	public static void main(String[] args) {
		
		Scanner s = new Scanner(System.in);
		boolean istrue=true;
		while(istrue) {
		    System.out.println("------------------------------------------------------------");
		    System.out.println("	1. list instance		2. available zones"); 
		    System.out.println("	3. start instance		4. available regions"); 
		    System.out.println("	5. stop instance		6. create instance"); 
		    System.out.println("	7. reboot instance 		8. list images"); 
		    System.out.println("	99. quit"); 
		    System.out.println("------------------------------------------------------------");
		    System.out.print("Enter an integer:");
		    int num=s.nextInt();
		    
		    if (num==1) {
		    	list_instance();
		    }
		    else if (num==2) {
		    	available_instance();
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
		    else if (num==99) {
		    	istrue=false;
		    }
		  }
	}
	public static void list_instance() {
		System.out.println("1");
	}
	public static void available_instance() {
		System.out.println("2");
	}
	public static void start_instance() {
		System.out.println("3");
	}
	public static void available_regions() {
		System.out.println("4");
	}
	public static void stop_instance() {
		System.out.println("5");
	}
	public static void create_instance() {
		System.out.println("6");
	}
	public static void reboot_instance() {
		System.out.println("7");
	}
	public static void list_images() {
		System.out.println("8");
	}
	
	
}
