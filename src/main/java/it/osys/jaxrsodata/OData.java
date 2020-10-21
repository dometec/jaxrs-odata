package it.osys.jaxrsodata;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import it.osys.jaxrsodata.antlr4.ODataFilterLexer;
import it.osys.jaxrsodata.antlr4.ODataFilterParser;
import it.osys.jaxrsodata.antlr4.ODataFilterParser.ExprContext;
import it.osys.jaxrsodata.antlr4.ODataOrderByLexer;
import it.osys.jaxrsodata.antlr4.ODataOrderByParser;
import it.osys.jaxrsodata.filter.DefaultJPAFilterVisitor;
import it.osys.jaxrsodata.filter.JPAFilterVisitor;
import it.osys.jaxrsodata.orderby.DefaultJPAOrderVisitor;
import it.osys.jaxrsodata.orderby.JPAOrderVisitor;

/**
 * Base Class. 1- Set entity manager 2- Call getAll (and countAll) passing
 * QueryOption
 *
 * @param <T>
 */
public class OData<T> {

	private EntityManager em;
	private Class<T> entityClass;
	private JPAFilterVisitor<T> visitorFilter = new DefaultJPAFilterVisitor<T>();
	private JPAOrderVisitor<T> visitorOrder = new DefaultJPAOrderVisitor<T>();

	/** I need the class since I can't rely on to Reflection to get the Generic Type **/
	public OData(Class<T> clazz) {
		entityClass = clazz;
	}

	public void setVisitorFilter(JPAFilterVisitor<T> visitorFilter) {
		this.visitorFilter = visitorFilter;
	}

	public void setVisitorOrderBy(JPAOrderVisitor<T> visitorOrderBy) {
		this.visitorOrder = visitorOrderBy;
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
	 */
	public List<T> getAll(QueryOptions queryOptions) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> query = cb.createQuery(entityClass);
		Root<T> root = query.from(entityClass);

		visitorFilter.setCb(cb);
		visitorFilter.setRoot(root);
		if (queryOptions.filter != null && !queryOptions.filter.isEmpty())
			query.where(createWherePredicate(visitorFilter, queryOptions.filter));

		visitorOrder.setCb(cb);
		visitorOrder.setRoot(root);

		if (queryOptions.orderby != null && !queryOptions.orderby.isEmpty())
			query.orderBy(createOrderPredicate(visitorOrder, queryOptions.orderby));

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
	 */
	public long countAll(QueryOptions queryOptions) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<T> root = query.from(entityClass);

		query.select(cb.count(root));

		visitorFilter.setCb(cb);
		visitorFilter.setRoot(root);

		if (queryOptions.filter != null)
			query.where(createWherePredicate(visitorFilter, queryOptions.filter));

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
	protected Order createOrderPredicate(JPAOrderVisitor<T> visitor, String order) {

		final ODataOrderByLexer lexer = new ODataOrderByLexer(CharStreams.fromString(order));
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
	 */
	protected Predicate createWherePredicate(JPAFilterVisitor<T> visitor, String filter) {

		final ODataFilterLexer lexer = new ODataFilterLexer(CharStreams.fromString(filter));
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		final ODataFilterParser parser = new ODataFilterParser(tokens);

		final ExprContext context = parser.expr();

		return (Predicate) visitor.visit(context);

	}

}
