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
        @ApiImplicitParam(paramType = "query", name = "$search", dataType = "string", value = "String to search in items"),
        @ApiImplicitParam(paramType = "query", name = "$orderby", dataType = "string", value = "Order by the result", example = "id asc"),
        @ApiImplicitParam(paramType = "query", name = "$filter", dataType = "string", value = "Filter the result", example = "id eq 1")
})
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ODataImplicitParams {

}