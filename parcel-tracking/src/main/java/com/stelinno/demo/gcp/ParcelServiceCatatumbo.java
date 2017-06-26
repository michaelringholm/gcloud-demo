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
		EntityManager em = emf.createDefaultEntityManager();
		
		ParcelCatatumbo parcel = new ParcelCatatumbo("DK55557777", new Date());
		parcel = em.insert(parcel);
		System.out.printf("parcel with ID %d created successfully", parcel.getId());
	}
	
}
