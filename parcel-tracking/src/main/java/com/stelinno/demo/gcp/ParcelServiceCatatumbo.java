package com.stelinno.demo.gcp;

import java.util.Date;

import com.jmethods.catatumbo.EntityManager;
import com.jmethods.catatumbo.EntityManagerFactory;

/***
 * http://catatumbo.io/quick-start.html
 * @author iHedge
 *
 */
public class ParcelServiceCatatumbo {
	public void insertNew() {
		EntityManagerFactory emf = EntityManagerFactory.getInstance();
		//EntityManager em = emf.createDefaultEntityManager("com.stelinno.uddi");
		EntityManager em = emf.createEntityManager("stelinno-dev", "Stelinno-DEV-f9c6d1e34d94.json", "com.stelinno.uddi");
		
		ParcelCatatumbo parcel = new ParcelCatatumbo("DK55557777", new Date());
		parcel = em.insert(parcel);
		System.out.printf("parcel with ID %d created successfully", parcel.getId());
	}
	
}
