package de.cavas.k8;

import java.io.IOException;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.proto.V1.Pod;
import io.kubernetes.client.util.Config;


/*
 * implementation of Kubernetes client based on the official kubernetes java api (https://github.com/kubernetes-client/java)s 
 */




public class K8sApi {
	
	public static void main(String[] args) throws IOException, ApiException {
//		InputStream input = new FileInputStream("admin.conf");
		
		   ApiClient client = Config.defaultClient(); //fromConfig("admin.conf");// 
	        Configuration.setDefaultApiClient(client);
	        
	        CoreV1Api api = new CoreV1Api();
	      
	        
	        V1PodList list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);
	        for (V1Pod item : list.getItems()) {
	            System.out.println(item.getMetadata().getName());
	            System.out.println(item.getKind());
	        }

//		KubernetesClient client = new DefaultKubernetesClient("");
////		System.out.println(client.getVersion());
//		NamespaceList myNs = client.namespaces().list();
//		
//		
//		System.out.println(myNs.getKind());
	}
	

}
