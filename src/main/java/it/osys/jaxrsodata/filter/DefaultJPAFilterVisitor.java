package it.osys.jaxrsodata.filter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import it.osys.jaxrsodata.antlr4.ODataFilterParser;
import it.osys.jaxrsodata.antlr4.ODataFilterParser.ExprContext;

/**
 * The Class DefaultJPAFilterVisitor.
 *
 * @param <T> the generic type
 * 
 * @author Domenico Briganti
 */
public class DefaultJPAFilterVisitor<T> implements JPAFilterVisitor<T> {

	/** The root. */
	private Root<T> root;
	
	/** The cb. */
	private CriteriaBuilder cb;
	
	/** The em. */
	private EntityManager em;

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
	 * Sets the entity manager.
	 *
	 * @param em the new entity manager
	 */
	@Override
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	/**
	 * Visit.
	 *
	 * @param context the context
	 * @return the object
	 */
	@Override
	public Object visit(ExprContext context) {

		if (context.AND() != null)
			return cb.and((Predicate) visit(context.expr(0)), (Predicate) visit(context.expr(1)));

		if (context.OR() != null)
			return cb.or((Predicate) visit(context.expr(0)), (Predicate) visit(context.expr(1)));

		if (context.NOT() != null)
			return cb.not((Predicate) visit(context.expr(0)));

		if (context.start.getType() == ODataFilterParser.BR_OPEN)
			return visit(context.expr(0));

		DefaultJPAFilterDao<T> filterDao = new DefaultJPAFilterDao<T>();
		filterDao.setCb(cb);
		filterDao.setRoot(root);
		filterDao.setEm(em);
		filterDao.setup(context);

		Predicate predicate = filterDao.getPredicate();
		if (predicate != null)
			return predicate;

		throw new IllegalStateException();
	}

}
