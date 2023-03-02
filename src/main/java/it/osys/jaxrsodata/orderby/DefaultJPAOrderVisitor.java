package it.osys.jaxrsodata.orderby;

import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

import it.osys.jaxrsodata.OData;
import it.osys.jaxrsodata.antlr4.ODataOrderByParser.ExprContext;

/**
 * The Class DefaultJPAOrderVisitor.
 *
 * @param <T> the generic type
 * 
 * @author Domenico Briganti
 */
public class DefaultJPAOrderVisitor<T> implements JPAOrderVisitor<T> {

	/** The root. */
	private Root<T> root;

	/** The cb. */
	private CriteriaBuilder cb;

	/**
	 * Sets the root.
	 *
	 * @param root the new root
	 */
	@Override
	public void setRoot(Root<T> root) {
		this.root = root;
	}

	/**
	 * Sets the cb.
	 *
	 * @param cb the new cb
	 */
	@Override
	public void setCb(CriteriaBuilder cb) {
		this.cb = cb;
	}

	/**
	 * Visit.
	 *
	 * @param context the context
	 * @param orders the list where Order are added
	 */
	@Override
	public void visit(ExprContext context, List<Order> orders) {

		if (context.getChild(0) == null)
			return;

		String fields = context.FIELD().getText();
		Path<Object> path = OData.getPathFromField(root, fields);
		if (context.DESC() != null)
			orders.add(cb.desc(path));
		else
			orders.add(cb.asc(path));

		if (context.expr() != null)
			visit(context.expr(), orders);

	}

}