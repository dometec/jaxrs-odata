package it.osys.jaxrsodata;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.NotNull;

import it.osys.jaxrsodata.ODataFilterParser.ExprContext;
import it.osys.jaxrsodata.exceptions.NotImplementedException;
import it.osys.jaxrsodata.filter.JPAFilterVisitor;
import it.osys.jaxrsodata.orderby.DefaultJPAOrderByVisitor;
import it.osys.jaxrsodata.orderby.JPAOrderByVisitor;
import it.osys.jaxrsodata.queryoptions.QueryOptions;

/**
 * Base Class. 1- Set entity manager 2- Call getAll or countAll passing
 * QueryOption
 *
 * @param <T>
 */
public class OData<T> {

	private EntityManager em;

	private Class<T> entityClass;

	/** I need the class since I can't rely on to Reflection to get che Generic Type **/
	public OData(Class<T> clazz) {
		entityClass = clazz;
	}

	/**
	 * Sets the entity manager.
	 *
	 * @param em
	 *            the new entity manager
	 */
	
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	/**
	 * Query the db getting all result according to query options.
	 *
	 * @param visitor
	 *            the visitor
	 * @param queryOptions
	 *            the query options
	 * @return the all
	 * @throws NotImplementedException
	 *             the not implemented exception
	 */
	public List<T> getAll(JPAFilterVisitor<T> visitor, QueryOptions queryOptions) throws NotImplementedException {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> query = cb.createQuery(entityClass);
		Root<T> root = query.from(entityClass);

		visitor.setCb(cb);
		visitor.setRoot(root);
		if (queryOptions.filter != null && !queryOptions.filter.isEmpty())
			query.where(createWherePredicate(visitor, queryOptions.filter));

		JPAOrderByVisitor<T> visitorOrderBy = new DefaultJPAOrderByVisitor<T>();
		visitorOrderBy.setCb(cb);
		visitorOrderBy.setRoot(root);

		if (queryOptions.orderby != null && !queryOptions.orderby.isEmpty())
			query.orderBy(createOrderPredicate(visitorOrderBy, queryOptions.orderby));

		TypedQuery<T> namedQuery = em.createQuery(query);
		namedQuery.setFirstResult(queryOptions.skip);
		namedQuery.setMaxResults(queryOptions.top);

		return namedQuery.getResultList();
	}

	/**
	 * Count all result.
	 *
	 * @param visitor
	 *            the visitor
	 * @param queryOptions
	 *            the query options
	 * @return the long
	 * @throws NotImplementedException
	 *             the not implemented exception
	 */
	public long countAll(JPAFilterVisitor<T> visitor, QueryOptions queryOptions) throws NotImplementedException {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<T> root = query.from(entityClass);

		query.select(cb.count(root));

		visitor.setCb(cb);
		visitor.setRoot(root);

		if (queryOptions.filter != null)
			query.where(createWherePredicate(visitor, queryOptions.filter));

		TypedQuery<Long> namedQuery = em.createQuery(query);

		return namedQuery.getSingleResult();
	}

	/**
	 * Creates order predicate.
	 *
	 * @param visitor
	 *            the visitor
	 * @param order
	 *            the order
	 * @return the order
	 */
	protected Order createOrderPredicate(JPAOrderByVisitor<T> visitor, @NotNull String order) {

		final ODataOrderByLexer lexer = new ODataOrderByLexer(new ANTLRInputStream(order));
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		final ODataOrderByParser parser = new ODataOrderByParser(tokens);

		final ODataOrderByParser.ExprContext context = parser.expr();

		return (Order) visitor.visit(context);
	}

	/**
	 * Creates where predicate.
	 *
	 * @param visitor
	 *            the visitor
	 * @param filter
	 *            the filter
	 * @return the predicate
	 * @throws NotImplementedException
	 *             the not implemented exception
	 */
	protected Predicate createWherePredicate(JPAFilterVisitor<T> visitor, String filter) throws NotImplementedException {

		final ODataFilterLexer lexer = new ODataFilterLexer(new ANTLRInputStream(filter));
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		final ODataFilterParser parser = new ODataFilterParser(tokens);

		final ExprContext context = parser.expr();

		return (Predicate) visitor.visit(context);

	}

}
