
[![Build Status](https://travis-ci.org/dometec/jaxrs-odata.svg?branch=master)](https://travis-ci.org/dometec/jaxrs-odata)

[![codecov](https://codecov.io/gh/dometec/jaxrs-odata/branch/master/graph/badge.svg)](https://codecov.io/gh/dometec/jaxrs-odata)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/it.osys/jaxrs-odata/badge.svg)](https://maven-badges.herokuapp.com/maven-central/it.osys/jaxrs-odata/)

[Maven generated site](https://docs.osys.it/it.osys.jaxrsodata/)

# Introduction

This library implements a subset (very very little) of [OData speficication](https://www.odata.org/documentation/), the [Quering Collection](http://docs.oasis-open.org/odata/odata/v4.01/odata-v4.01-part1-protocol.html#_Toc31358947).

The primary scope of this library is to implement the filter, order and pagination of JPA Entity using the OData sintax. The library use the language specified in OData Query Collection to create dinamically JPA Criteria Query.

The parameters can be taken directly from an JAX-RS Endpoint using the utility class QueryOptionsParser:

1. $skip - How many record skip before the first record returned
2. $top - How many record return
3. $count - Boolean value used to ask to count how many records are impacted by the filter
4. $orderby - Sort the records
5. $filter - Filter the records


  For more information on OData you can see https://www.odata.org/getting-started/.

# Installations

Add the library as dependency:

```
<dependency>
  <groupId>it.osys</groupId>
  <artifactId>jaxrs-odata</artifactId>
  <version>1.2.0</version>
</dependency>
```

# Usage

In the code, create an instance of OData class passing the Entity Bean Class. Set the Entity Manager and call <getAll> method or the <count> method passing the QueryOptions.

```
OData<Author> odata = new OData<Author>(Author.class);
odata.setEntityManager(em);
...
odata.get(queryOptions);
odata.count(queryOptions);
```

# Usage Examples

  With this rest Endpoint:

```
@GET
@Path("authors")
@Produces(MediaType.APPLICATION_JSON)
public Collection<Author> getAllAuthors(UriInfo info) {
  QueryOptions queryOptions = QueryOptionsParser.from(info);
  OData<Author> odata = new OData<Author>(Author.class);
  odata.setEntityManager(em);
  return odata.getAll(queryOptions);
}
```

_This is only an exampe, I advice to use a DTO and do not output JPA Entity Bean directly._

You can get authors, filtered, ordered and paginated using a request like this:

```http://<SERVER>/authors?$top=10&$skip=0&$filter=contains(name, 'Adriano')```
  
Or

```http://<SERVER>/authors?$top=10&$skip=10&$filter=bod ge '1980-01-01' and not (contains(name, 'Valerio'))&$order=address/city asc```
  
You can see many exampe in the test classes.  

# Other suggestions

## Output compliant to OData Specification

Use a bean like this to marshialing the output data:

```
import java.util.Collection;

import javax.json.bind.annotation.JsonbProperty;

public class ResultSet<T> {

	@JsonbProperty(value = "@odata.count")
	public long count;

	public Collection<T> value;

	@Override
	public String toString() {
		return "ResultSet [count=" + count + ", value=" + value + "]";
	}

}
```

The rest method, with the logic to output also the total record count:

```
	@GET
	@Path("authors")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultSet<Author> authors(UriInfo info) {

		QueryOptions queryOptions = QueryOptionsParser.from(info);

		OData<Author> odata = new OData<Author>(Author.class);
		odata.setEntityManager(em);
		ResultSet<Author> resultSet = new ResultSet<>();
		resultSet.value = odata.getAll(queryOptions);
		if (queryOptions.count)
			resultSet.count = odata.countAll(queryOptions);
		return resultSet;
	}
```

## Enrich Open API using Eclipse Microprofile

  You can use the filter method to enrich the swagger json with OData parameter when the Operation's summary contains "odata" string.
  Add the following content in file **src/main/resources/META-INF/microprofile-config.properties** with:

```
mp.openapi.filter=it.osys.jaxrsodata.ODataParamsFilter
```

And add this class:
  
```
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

      Parameter search = OASFactory.createParameter();
      search.setName("$search");
      search.setIn(In.QUERY);
      search.setStyle(Style.SIMPLE);
      search.setSchema(sString);
      search.setDescription("Specify a custom search string");

      if (operation.getParameters() == null)
        operation.setParameters(new ArrayList<>());

      operation.getParameters().add(top);
      operation.getParameters().add(skip);
      operation.getParameters().add(count);
      operation.getParameters().add(orderby);
      operation.getParameters().add(filter);
      operation.getParameters().add(search);

    }

    return OASFilter.super.filterOperation(operation);

  }

}
```
  
And annotate the method with **@Operation**. Remember to add "odata" in his summary.

```
...
import org.eclipse.microprofile.openapi.annotations.Operation;
...

	@Operation(summary = "Get Authors - odata")
	@GET
	@Path("authors")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultSet<Author> authors(UriInfo info) {
```

  
## Enrich Open API using Swagger

Add this annotation:

```
package it.osys.jaxrsodata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@ApiImplicitParams(value = {
  @ApiImplicitParam(paramType = "query", name = "$top", dataType = "int", value = "Max number item to get"),
  @ApiImplicitParam(paramType = "query", name = "$skip", dataType = "int", value = "How many record skip before the first item"),
  @ApiImplicitParam(paramType = "query", name = "$count", dataType = "boolean", value = "True to get also the total number of item the server has"),
  @ApiImplicitParam(paramType = "query", name = "$orderby", dataType = "string", value = "Order by the result", example = "id asc"),
  @ApiImplicitParam(paramType = "query", name = "$filter", dataType = "string", value = "Filter the result", example = "id eq 1"),
  @ApiImplicitParam(paramType = "query", name = "$search", dataType = "string", value = "String to search in items")
})
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ODataImplicitParams {

}
```

And add it to Endpoint:

```
@GET
@Path("authors")
@ODataImplicitParams
@Produces(MediaType.APPLICATION_JSON)
public ResultSet<Author> authors(UriInfo info) \{
```

# TODO

  * The input parameter reference fields in the Entity Bean, and the fields maybe do not has the same name of the field present in the return recordset.

# Demo

See https://github.com/dometec/jaxrs-odata-example

