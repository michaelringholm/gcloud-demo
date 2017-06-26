package com.stelinno.demo.gcp;




import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.KeyFactory;

/***
 * https://github.com/objectify/objectify/wiki
 * @author Stelinno
 *
 */
public class ParcelServiceGCD {
	public ParcelServiceGCD(){

    }
	
	public void insertNew() {
		Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	    KeyFactory keyFactory = datastore.newKeyFactory().setKind("parcel");
	    IncompleteKey key = keyFactory.setKind("parcel").newKey();
		
		FullEntity<IncompleteKey> parcel = FullEntity.newBuilder(key)
		.set("trackAndTraceNo", 12312).build();
		datastore.add(parcel);
	}
	
	public Parcel read() {
		
		/*Query<Entity> query = Query.newEntityQueryBuilder().setKind("visit")
		        .setOrderBy(StructuredQuery.OrderBy.desc("timestamp")).setLimit(10).build();
		    QueryResults<Entity> results = datastore.run(query);

		    resp.setContentType("text/plain");
		    PrintWriter out = resp.getWriter();
		    out.print("Last 10 visits:\n");
		    while (results.hasNext()) {
		      Entity entity = results.next();
		      out.format("Time: %s Addr: %s\n", entity.getDateTime("timestamp"),
		          entity.getString("user_ip"));*/
		return null;
	}
	
}
