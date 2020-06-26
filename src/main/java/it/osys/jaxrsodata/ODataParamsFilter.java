package it.osys.jaxrsodata;

import java.util.ArrayList;

import org.eclipse.microprofile.openapi.OASFactory;
import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.Operation;
import org.eclipse.microprofile.openapi.models.media.Schema;
import org.eclipse.microprofile.openapi.models.parameters.Parameter;
import org.eclipse.microprofile.openapi.models.parameters.Parameter.In;
import org.eclipse.microprofile.openapi.models.parameters.Parameter.Style;

public class ODataParamsFilter implements OASFilter {

	@Override
	public Operation filterOperation(Operation operation) {

		if (operation.getSummary() != null && operation.getSummary().contains("odata")) {

			System.out.println("Enrich " + operation.getSummary() + " with OData Parameter");

			Schema sInt = OASFactory.createSchema();
			sInt.setType(Schema.SchemaType.INTEGER);

			Schema sBool = OASFactory.createSchema();
			sBool.setType(Schema.SchemaType.BOOLEAN);

			Schema sString = OASFactory.createSchema();
			sString.setType(Schema.SchemaType.STRING);

			Parameter top = OASFactory.createParameter();
			top.setName("$top");
			top.setIn(In.QUERY);
			top.setStyle(Style.SIMPLE);
			top.setSchema(sInt);
			top.setDescription("Max num. record returned");

			Parameter skip = OASFactory.createParameter();
			skip.setName("$skip");
			skip.setIn(In.QUERY);
			skip.setStyle(Style.SIMPLE);
			skip.setSchema(sInt);
			skip.setDescription("Skip to record");

			Parameter count = OASFactory.createParameter();
			count.setName("$count");
			count.setIn(In.QUERY);
			count.setStyle(Style.SIMPLE);
			count.setSchema(sBool);
			count.setDescription("Calculate the total record affected by $filter");

			Parameter orderby = OASFactory.createParameter();
			orderby.setName("$orderby");
			orderby.setIn(In.QUERY);
			orderby.setStyle(Style.SIMPLE);
			orderby.setSchema(sString);
			orderby.setDescription("Specify the order of records before $skip and $top");

			Parameter filter = OASFactory.createParameter();
			filter.setName("$filter");
			filter.setIn(In.QUERY);
			filter.setStyle(Style.SIMPLE);
			filter.setSchema(sString);
			filter.setDescription("Specify the filter to apply to record");

			if (operation.getParameters() == null)
				operation.setParameters(new ArrayList<>());

			operation.getParameters().add(top);
			operation.getParameters().add(skip);
			operation.getParameters().add(count);
			operation.getParameters().add(orderby);
			operation.getParameters().add(filter);

		}

		return OASFilter.super.filterOperation(operation);

	}

}