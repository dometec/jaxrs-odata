package it.osys.jaxrsodata.orderby;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

import it.osys.jaxrsodata.antlr4.ODataOrderByParser.ExprContext;

public interface JPAOrderVisitor<T> {

	Object visit(ExprContext context);

	void setRoot(Root<T> root);

	void setCb(CriteriaBuilder cb);

}
