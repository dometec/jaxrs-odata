package it.osys.jaxrsodata.filter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

import it.osys.jaxrsodata.antlr4.ODataFilterParser.ExprContext;

public interface JPAFilterVisitor<T> {

	Object visit(ExprContext context);

	void setRoot(Root<T> root);

	void setCb(CriteriaBuilder cb);
	
	void setEntityManager(EntityManager em);

}
