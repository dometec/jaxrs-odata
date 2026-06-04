package it.osys.jaxrsodata.filter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.AbstractQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ManagedType;

import it.osys.jaxrsodata.OData;
import it.osys.jaxrsodata.antlr4.ODataFilterParser;
import it.osys.jaxrsodata.antlr4.ODataFilterParser.ExprContext;
import it.osys.jaxrsodata.exceptions.FormatExceptionException;

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

	/** The enclosing query (criteria query or parent subquery). */
	private AbstractQuery<?> query;

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
	 * Sets the enclosing query, used to create {@code in (select ...)} subqueries.
	 *
	 * @param query the enclosing query
	 */
	@Override
	public void setQuery(AbstractQuery<?> query) {
		this.query = query;
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

		if (context == null)
			throw new IllegalArgumentException("Filter expression context must not be null");
		if (cb == null)
			throw new IllegalStateException("CriteriaBuilder must be set before visiting filter");
		if (root == null)
			throw new IllegalStateException("Root must be set before visiting filter");
		if (em == null)
			throw new IllegalStateException("EntityManager must be set before visiting filter");

		if (context.AND() != null)
			return cb.and((Predicate) visit(context.expr(0)), (Predicate) visit(context.expr(1)));

		if (context.OR() != null)
			return cb.or((Predicate) visit(context.expr(0)), (Predicate) visit(context.expr(1)));

		if (context.NOT() != null)
			return cb.not((Predicate) visit(context.expr(0)));

		if (context.start.getType() == ODataFilterParser.BR_OPEN)
			return visit(context.expr(0));

		if (context.IN() != null && context.SELECT() != null)
			return buildSubqueryIn(context);

		DefaultJPAFilterDao<T> filterDao = new DefaultJPAFilterDao<T>();
		filterDao.setCb(cb);
		filterDao.setRoot(root);
		filterDao.setEm(em);
		filterDao.setup(context);

		Predicate predicate = filterDao.getPredicate();
		if (predicate != null)
			return predicate;

		throw new FormatExceptionException("Unsupported or invalid filter expression: " + context.getText());
	}

	/**
	 * Builds a {@code outerField in (select selectField from Entity where expr)}
	 * predicate. The optional {@code where} expression is visited against the
	 * subquery root with a child visitor so nested subqueries keep working.
	 *
	 * @param context the IN-subquery expression context
	 * @return the {@code IN} predicate backed by a correlatable subquery
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Predicate buildSubqueryIn(ExprContext context) {

		if (query == null)
			throw new IllegalStateException("Query must be set before visiting an in (select ...) subquery");

		String outerField = context.FIELD(0).getText();
		String selectField = context.FIELD(1).getText();
		String entityName = context.FIELD(2).getText();

		Class<?> subEntity = resolveEntity(entityName);
		Class<?> selectType = resolvePathType(subEntity, selectField);

		Subquery sub = query.subquery(selectType);
		Root subRoot = sub.from(subEntity);
		Path selectPath = OData.getPathFromField(subRoot, selectField);
		sub.select(selectPath);

		if (context.WHERE() != null) {
			DefaultJPAFilterVisitor child = new DefaultJPAFilterVisitor();
			child.setCb(cb);
			child.setEntityManager(em);
			child.setRoot(subRoot);
			child.setQuery(sub);
			sub.where((Predicate) child.visit(context.expr(0)));
		}

		Path outerPath = OData.getPathFromField(root, outerField);
		return cb.in(outerPath).value(sub);
	}

	/**
	 * Resolves a JPA entity Java type from its entity name.
	 *
	 * @param entityName the JPA entity name (e.g. {@code PlantDelegated})
	 * @return the entity Java type
	 */
	private Class<?> resolveEntity(String entityName) {
		for (EntityType<?> et : em.getMetamodel().getEntities()) {
			if (et.getName().equals(entityName))
				return et.getJavaType();
		}
		throw new FormatExceptionException("Unknown entity in subquery: " + entityName);
	}

	/**
	 * Resolves the Java type of the (singular) attribute path {@code path}
	 * starting from {@code entity}, so the subquery can be created with the
	 * correct result type. Walks embeddables and {@code *-to-one} associations.
	 *
	 * @param entity the subquery entity
	 * @param path   the slash-separated attribute path of the selected field
	 * @return the Java type of the leaf attribute
	 */
	private Class<?> resolvePathType(Class<?> entity, String path) {
		ManagedType<?> current = em.getMetamodel().managedType(entity);
		Class<?> type = entity;
		String[] segments = path.split("/");
		for (int i = 0; i < segments.length; i++) {
			Attribute<?, ?> attribute = current.getAttribute(segments[i]);
			type = attribute.getJavaType();
			if (i < segments.length - 1)
				current = em.getMetamodel().managedType(type);
		}
		return type;
	}

}
