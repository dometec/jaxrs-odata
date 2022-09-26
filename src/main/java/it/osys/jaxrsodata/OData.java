package it.osys.jaxrsodata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import it.osys.jaxrsodata.antlr4.ODataFilterLexer;
import it.osys.jaxrsodata.antlr4.ODataFilterParser;
import it.osys.jaxrsodata.antlr4.ODataFilterParser.ExprContext;
import it.osys.jaxrsodata.antlr4.ODataOrderByLexer;
import it.osys.jaxrsodata.antlr4.ODataOrderByParser;
import it.osys.jaxrsodata.exceptions.FormatExceptionException;
import it.osys.jaxrsodata.filter.DefaultJPAFilterVisitor;
import it.osys.jaxrsodata.filter.JPAFilterVisitor;
import it.osys.jaxrsodata.orderby.DefaultJPAOrderVisitor;
import it.osys.jaxrsodata.orderby.JPAOrderVisitor;

/**
 * Base Class. 1- Set entity manager 2- Call getAll (and countAll) passing
 * QueryOption
 *
 * @param <T> Entity class
 * 
 * @author Domenico Briganti
 */
public class OData<T> {

	/** The em. */
	private EntityManager em;
	
	/** The entity class. */
	private Class<T> entityClass;
	
	/** The visitor filter. */
	private JPAFilterVisitor<T> visitorFilter = new DefaultJPAFilterVisitor<T>();
	
	/** The visitor order. */
	private JPAOrderVisitor<T> visitorOrder = new DefaultJPAOrderVisitor<T>();

	/**
	 *  I need the class since I can't rely on to Reflection to get the Generic Type.
	 *
	 * @param clazz The Entity class
	 */
	public OData(Class<T> clazz) {
		entityClass = clazz;
	}

	/**
	 * Sets the visitor filter.
	 *
	 * @param visitorFilter the new visitor filter
	 */
	public void setVisitorFilter(JPAFilterVisitor<T> visitorFilter) {
		this.visitorFilter = visitorFilter;
	}

	/**
	 * Sets the visitor order by.
	 *
	 * @param visitorOrderBy the new visitor order by
	 */
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
	 * Query the db for entity getting the result according to query options.
	 *
	 * @param queryOptions
	 *            the query options
	 * @return the recordset
	 */
	public List<T> get(QueryOptions queryOptions) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> query = cb.createQuery(entityClass);
		Root<T> root = query.from(entityClass);

		visitorFilter.setCb(cb);
		visitorFilter.setEntityManager(em);
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
	 * @param queryOptions
	 *            the query options
	 * @return the number of record impacted by the filter 
	 */
	public long count(QueryOptions queryOptions) {
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
	public List<Order> createOrderPredicate(JPAOrderVisitor<T> visitor, String order) {

		List<Order> out = new ArrayList<>();
		
		final ODataOrderByLexer lexer = new ODataOrderByLexer(CharStreams.fromString(order));
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		final ODataOrderByParser parser = new ODataOrderByParser(tokens);

		final ODataOrderByParser.ExprContext context = parser.expr();
		
		visitor.visit(context, out);
		
		return out;
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
	public Predicate createWherePredicate(JPAFilterVisitor<T> visitor, String filter) {

		final ODataFilterLexer lexer = new ODataFilterLexer(CharStreams.fromString(filter));
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		final ODataFilterParser parser = new ODataFilterParser(tokens);

		final ExprContext context = parser.expr();

		return (Predicate) visitor.visit(context);

	}

	/**
	 * Gets the path from field.
	 *
	 * @param <T> the generic type
	 * @param root the root
	 * @param field the field
	 * @return the path from field
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Path<Object> getPathFromField(Root<T> root, String field) {
		String[] fieldname = field.split("/");
		Path<Object> path = null;
		for (int idx = 0; idx < fieldname.length; idx++) {

			String attributeName = fieldname[idx];

			if (path != null) {
				path = path.get(attributeName);
			} else {
				path = root.get(attributeName);
			}

			if ((fieldname.length - 1) > idx) {

				if (path.getJavaType().isAssignableFrom(Set.class)) {

					Optional<Join<T, ?>> opJoin = root.getJoins().stream().filter(p -> p.getAttribute().getName().equals(attributeName))
							.findFirst();
					Join<T, ?> join = opJoin.orElseGet(() -> root.join(attributeName));
					path = join.get(fieldname[++idx]);

				}

				if (path.getJavaType().isAssignableFrom(Map.class)) {

					Optional<Join<T, ?>> opJoin = root.getJoins().stream().filter(p -> p.getAttribute().getName().equals(attributeName))
							.findFirst();
					MapJoin join = (MapJoin) opJoin.orElseGet(() -> root.join(attributeName));
					String f = fieldname[++idx];
					if (f.equals("key"))
						path = join.key();
					else if (f.equals("value"))
						path = join.value();
					else
						throw new FormatExceptionException("Only key or value you can specify in a map filter, got: " + f);

				}
			}

		}

		return path;
	}

}
