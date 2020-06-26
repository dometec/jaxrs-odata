package it.osys.jaxrsodata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@ApiImplicitParams(value = {
        @ApiImplicitParam(paramType = "query", name = "$top", dataType = "int", value = "OData $top parameter"),
        @ApiImplicitParam(paramType = "query", name = "$skip", dataType = "int", value = "OData $skip parameter"),
        @ApiImplicitParam(paramType = "query", name = "$count", dataType = "boolean", value = "OData $count parameter"),
        @ApiImplicitParam(paramType = "query", name = "$orderby", dataType = "string", value = "OData $orderby parameter", example = "id asc"),
        @ApiImplicitParam(paramType = "query", name = "$filter", dataType = "string", value = "OData $filter parameter", example = "id eq 1")
})
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ODataImplicitParams {

}