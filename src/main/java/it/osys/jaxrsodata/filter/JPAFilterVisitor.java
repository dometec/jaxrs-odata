package it.osys.jaxrsodata.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

import it.osys.jaxrsodata.ODataFilterParser.ExprContext;
import it.osys.jaxrsodata.exceptions.NotImplementedException;

public interface JPAFilterVisitor<T> {

	Object visit(ExprContext context) throws NotImplementedException;

	void setRoot(Root<T> root);

	void setCb(CriteriaBuilder cb);

}
