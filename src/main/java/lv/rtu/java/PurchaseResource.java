package lv.rtu.java;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

@Path("/purchases/{token}")
public class PurchaseResource {
	
	private static final Logger LOG = Logger.getLogger(PurchaseResource.class.getName());
	
	@GET
	@Produces(APPLICATION_JSON)
	public Map<String, Collection<Purchase>> getPurchaseLists(@PathParam("token") String token) {
		LOG.log(Level.INFO, "Get puchase list data for token [{0}]", token);
		
		final List<Purchase> list = PurchaseDao.findPurchaseListByToken(token);
		return Multimaps.index(list, new Function<Purchase, String>() {
			@Override
			public String apply(Purchase purchase) {
				return purchase.getList();
			}
		}).asMap();
	}
	
	@POST
	@Consumes(APPLICATION_JSON)
	public void postPurchaseLists(@PathParam("token") String token, Map<String, List<Purchase>> map) {
		LOG.log(Level.INFO, "Post puchase list data for token [{0}]", token);

		final List<Purchase> list = new ArrayList<Purchase>();
		for(Map.Entry<String, List<Purchase>> entry : map.entrySet()) {
			for(Purchase purchase : entry.getValue()) {
				purchase.setList(entry.getKey());
				purchase.setToken(token);
				list.add(purchase);
			}
		}
		
		PurchaseDao.storePurchaseListForToken(token, list);
	}
}