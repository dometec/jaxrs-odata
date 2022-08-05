package it.osys.jaxrsodata.filter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

import it.osys.jaxrsodata.antlr4.ODataFilterParser.ExprContext;

/**
 * The Interface JPAFilterVisitor.
 *
 * @param <T> the generic type
 * 
 * @author Domenico Briganti
 */
public interface JPAFilterVisitor<T> {

	/**
	 * Visit.
	 *
	 * @param context the context
	 * @return the object
	 */
	Object visit(ExprContext context);

	/**
	 * Sets the root.
	 *
	 * @param root the new root
	 */
	void setRoot(Root<T> root);

	/**
	 * Sets the cb.
	 *
	 * @param cb the new cb
	 */
	void setCb(CriteriaBuilder cb);
	
	/**
	 * Sets the entity manager.
	 *
	 * @param em the new entity manager
	 */
	void setEntityManager(EntityManager em);

}
