package org.purl.wf4ever.astrotaverna.tjoin.ui.serviceprovider;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import org.purl.wf4ever.astrotaverna.tjoin.ui.serviceprovider.AddColumnByExpressionServiceDesc;
import org.purl.wf4ever.astrotaverna.tjoin.ui.serviceprovider.AddSkyCoordsServiceDesc;
import org.purl.wf4ever.astrotaverna.tjoin.ui.serviceprovider.CheckTemplateFillerServiceDesc;
import org.purl.wf4ever.astrotaverna.tjoin.ui.serviceprovider.CoordTransformationServiceDesc;
import org.purl.wf4ever.astrotaverna.tjoin.ui.serviceprovider.FormatConversionServiceDesc;
import org.purl.wf4ever.astrotaverna.tjoin.ui.serviceprovider.GetListFromColumnServiceDesc;
import org.purl.wf4ever.astrotaverna.tjoin.ui.serviceprovider.ResolveCoordsServiceDesc;
import org.purl.wf4ever.astrotaverna.tjoin.ui.serviceprovider.SelectColumnsServiceDesc;
import org.purl.wf4ever.astrotaverna.tjoin.ui.serviceprovider.SelectRowsServiceDesc;
import org.purl.wf4ever.astrotaverna.tjoin.ui.serviceprovider.StiltsServiceDesc;
import org.purl.wf4ever.astrotaverna.tjoin.ui.serviceprovider.StiltsServiceIcon;
import org.purl.wf4ever.astrotaverna.tjoin.ui.serviceprovider.TcatListServiceDesc;
import org.purl.wf4ever.astrotaverna.tjoin.ui.serviceprovider.TcatServiceDesc;
import org.purl.wf4ever.astrotaverna.tjoin.ui.serviceprovider.TemplateFillerServiceDesc;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionProvider;

public class StiltsServiceProvider implements ServiceDescriptionProvider {
	
	//OJO!!!!!!!!!!!!!!!!!!!!
	//write down a real URI
	private static final URI providerId = URI
		.create("http://www.iaa.es/service-provider/tjoin");
	
	/**
	 * Do the actual search for services. Return using the callBack parameter.
	 */
	@SuppressWarnings("unchecked")
	public void findServiceDescriptionsAsync(
			FindServiceDescriptionsCallBack callBack) {
		// Use callback.status() for long-running searches
		// callBack.status("Resolving example services");

		List<ServiceDescription> results = new ArrayList<ServiceDescription>();

		// FIXME: Implement the actual service search/lookup instead
		// of dummy for-loop
		//for (int i = 1; i <= 5; i++) {
		//	StiltsServiceDesc service = new StiltsServiceDesc();
		//	// Populate the service description bean
		//	service.setExampleString("Example " + i);
		//	service.setExampleUri(URI.create("http://localhost:8192/service"));

		//	// Optional: set description
		//	service.setDescription("Service example number " + i);
		//	results.add(service);
		//}
		
		StiltsServiceDesc service = new StiltsServiceDesc();
		service.setTypeOFInput("String");
		service.setDescription("Join two VOTables");
		
		results.add(service);
		
		//ServiceDescription
		SelectColumnsServiceDesc service2 = new SelectColumnsServiceDesc();
		service2.setTypeOfInput("String");
		service2.setTypeOfFilter("Column names");
		service2.setDescription("Select columns from a VOTable");
		
		results.add(service2);
		
		SelectRowsServiceDesc service3 = new SelectRowsServiceDesc();
		service3.setTypeOfInput("String");
		//service3.setTypeOfFilter("Column names");
		service3.setDescription("Select rows from a VOTable");
		
		results.add(service3);
		
		CoordTransformationServiceDesc service4 = new CoordTransformationServiceDesc();
		service4.setTypeOfInput("String");
		service4.setDescription("Add Coordinate units conversion");
		
		results.add(service4);
		
		FormatConversionServiceDesc service5 = new FormatConversionServiceDesc();
		service5.setTypeOfInput("String");
		//service3.setTypeOfFilter("Column names");
		service5.setDescription("VOTable format conversion");
		
		results.add(service5);
		
		AddColumnByExpressionServiceDesc service6 = new AddColumnByExpressionServiceDesc();
		service6.setTypeOfInput("String");
		//service3.setTypeOfFilter("Column names");
		service6.setDescription("Add a column to VOTable");
		
		results.add(service6);
		
		AddSkyCoordsServiceDesc service7 = new AddSkyCoordsServiceDesc();
		service7.setTypeOfInput("String");
		service7.setDescription("Coordenates reference system transformation");
		
		results.add(service7);
		
		ResolveCoordsServiceDesc service8 = new ResolveCoordsServiceDesc();
		service8.setTypeOfInput("String");
		service8.setDescription("Name resolver");
		results.add(service8);
		
		TcatServiceDesc service9 = new TcatServiceDesc();
		service9.setTypeOfInput("String");
		service9.setDescription("Concatenate two VOTables");
		results.add(service9);
		
		TcatListServiceDesc service10 = new TcatListServiceDesc();
		service10.setTypeOfInput("String");
		service10.setDescription("Concatenate a list of VOTables");
		results.add(service10);
		
		GetListFromColumnServiceDesc service11 = new GetListFromColumnServiceDesc();
		service11.setTypeOfInput("String");
		service11.setDescription("Extract column from a VOTable as a list");
		results.add(service11);
		
		TemplateFillerServiceDesc service12 = new TemplateFillerServiceDesc();
		service12.setTypeOfInput("String");
		service12.setDescription("fill template file from VOTable");
		results.add(service12);
		
		CheckTemplateFillerServiceDesc service13 = new CheckTemplateFillerServiceDesc();
		service13.setTypeOfInput("String");
		service13.setDescription("Validate template file against VOTable");
		results.add(service13);
		
		AddCommonRowToVOTableServiceDesc service14 = new AddCommonRowToVOTableServiceDesc();
		service14.setTypeOfInput("String");
		service14.setCommonRowPosition("Left");
		service14.setDescription("Add common fields to a VOTable");
		results.add(service14);
		
		CrossMatch2ServiceDesc service15 = new CrossMatch2ServiceDesc();
		service15.setTypeOfInput("String");
		service15.setDescription("Crossmatch two VOTables");
		results.add(service15);
		
		//change done in wf4ever
		
		//Put here additional descriptions for other services
		//............
		//............
		//............
		//............
		

		// partialResults() can also be called several times from inside
		// for-loop if the full search takes a long time
		callBack.partialResults(results);

		// No more results will be coming
		callBack.finished();
	}

	/**
	 * Icon for service provider
	 */
	public Icon getIcon() {
		return StiltsServiceIcon.getIcon();
	}

	/**
	 * Name of service provider, appears in right click for 'Remove service
	 * provider'
	 */
	public String getName() {
		return "My astro services";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public String getId() {
		return providerId.toASCIIString();
	}

}
