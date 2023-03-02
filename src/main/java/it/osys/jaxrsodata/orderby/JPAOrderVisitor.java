package it.osys.jaxrsodata.orderby;

import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

import it.osys.jaxrsodata.antlr4.ODataOrderByParser.ExprContext;

/**
 * The Interface JPAOrderVisitor.
 *
 * @param <T> the generic type
 * 
 * @author Domenico Briganti
 */
public interface JPAOrderVisitor<T> {

	/**
	 * Visit.
	 *
	 * @param context the context
	 * @param orders the list where Order are added
	 */
	void visit(ExprContext context, List<Order> orders);

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

}
