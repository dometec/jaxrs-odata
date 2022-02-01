package it.osys.jaxrsodata.filter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import it.osys.jaxrsodata.OData;
import it.osys.jaxrsodata.antlr4.ODataFilterParser.ExprContext;

public class DefaultJPAFilterDao<T> {

	private ExprContext context;

	private Root<T> root;
	private CriteriaBuilder cb;

	private Object value;
	private Object field;

	private EntityManager em;

	/**
	 * Set JPA root
	 * 
	 * @param root The Root entity
	 */
	public void setRoot(Root<T> root) {
		this.root = root;
	}

	/**
	 * Set CriteriaBuilder
	 * 
	 * @param cb the Criteria Builder
	 */
	public void setCb(CriteriaBuilder cb) {
		this.cb = cb;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	/**
	 * Setup this class by passing the context
	 * 
	 * @param context The ANTLR Context
	 */
	public void setup(ExprContext context) {

		if (context.children != null) {

			this.context = context;

			Integer indexField = 0;
			Integer indexValue = 2;

			// If the first element of the filter is a function (ex.
			// "tolower(value)") we need to shift the index of value and field
			// accordingly.

			if (this.context.TOLOWER() != null || this.context.TOUPPER() != null) {

				this.field = this.context.getChild(indexField + 2).getText();
				this.value = this.context.getChild(indexValue + 3).getText().replace("'", "");

			} else if (this.context.CONTAINS() != null) {

				this.field = this.context.getChild(indexField + 2).getText();
				this.value = this.context.getChild(indexValue + 2).getText().replace("'", "");

			} else if (this.context.IN() != null) {

				this.field = this.context.FIELD().getText();

				if (this.context.STRINGLITERAL().size() > 0)
					this.value = this.context.STRINGLITERAL().stream().map(e -> e.getText().replace("'", "")).collect(Collectors.toSet());

				if (this.context.NUMBER().size() > 0)
					this.value = this.context.NUMBER().stream().map(e -> Long.parseLong(e.getText())).collect(Collectors.toSet());

			} else {

				this.field = this.context.getChild(indexField).getText();
				this.value = this.context.getChild(indexValue).getText().replace("'", "");

				Path<Object> path = OData.getPathFromField(this.root, this.field.toString());
				this.value = convValueToFieldType(path, this.value.toString().replace("'", ""));

			}

		}
	}

	/**
	 * When is requested to make some filtering on db query we need to check the
	 * value (String) against the field on db. In order to do this the value
	 * must be converted to the same type of the field.
	 * 
	 * @param clazz
	 * @param value
	 * @return Typed value.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object convValueToFieldType(Path<Object> path, String value) {

		// If i just want to check if the db field is empty just skip value
		// casting.
		if (value.equals("null"))
			return null;

		Class<? extends Object> javaType = path.getJavaType();

		// Cast value accorting to db field type.
		if (javaType.isAssignableFrom(String.class))
			return value;

		if (javaType.isEnum())
			return Enum.valueOf((Class<Enum>) javaType, value);

		if (javaType.isAssignableFrom(Integer.class) || javaType.isAssignableFrom(int.class))
			return Integer.valueOf(value);

		if (javaType.isAssignableFrom(Long.class))
			return Long.valueOf(value);

		if (javaType.isAssignableFrom(Float.class))
			return Float.valueOf(value);

		if (javaType.isAssignableFrom(LocalDateTime.class))
			return LocalDateTime.parse(value.split("\\.")[0]);

		if (javaType.isAssignableFrom(LocalTime.class))
			return LocalTime.parse(value);

		if (javaType.isAssignableFrom(LocalDate.class))
			return LocalDate.parse(value);

		if (javaType.isAssignableFrom(Boolean.class) || javaType.isAssignableFrom(boolean.class))
			return Boolean.valueOf(value);

		if (javaType.isAssignableFrom(Set.class)) {
			// TODO, convert to Set's type?
			return value;
		}

		return em.getReference(javaType, value);

	}

	/**
	 * This is the final part. It's generate predicates according on what is
	 * requested by the context.
	 * 
	 * @return Predicate
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate getPredicate() {

		if (this.context != null) {

			Path path = OData.getPathFromField(this.root, this.field.toString());

			if (this.context.NULL() != null) {
				if (this.context.EQ() != null) {
					return cb.isNull(path);
				}
				return cb.isNotNull(path);
			}

			if (context.CONTAINS() != null) {
				return cb.like(path.as(String.class), "%" + this.value.toString() + "%");
			}

			if (this.context.EQ() != null) {
				if (context.TOLOWER() != null) {
					return cb.equal(cb.lower(path), this.value);
				} else if (context.TOUPPER() != null) {
					return cb.equal(cb.upper(path), this.value);
				} else
					return cb.equal(path, this.value);
			}

			if (this.context.NE() != null)
				return cb.notEqual(path, this.value);

			if (this.context.IN() != null) {
				In in = cb.in(path);
				in.value(this.value);
				return in;
			}

			if (this.context.HAS() != null) {
				if (path.getJavaType() == LocalDateTime.class)
					return cb.isMember(LocalDateTime.parse(this.value.toString()), path);
				if (path.getJavaType() == LocalTime.class)
					return cb.isMember(LocalTime.parse(this.value.toString()), path);
				if (path.getJavaType() == LocalDate.class)
					return cb.isMember(LocalDate.parse(this.value.toString()), path);
				if (path.getJavaType() == Long.class)
					return cb.isMember(Long.parseLong(this.value.toString()), path);
				if (path.getJavaType() == Float.class)
					return cb.isMember(Float.parseFloat(this.value.toString()), path);
				return cb.isMember(Integer.parseInt(this.value.toString()), path);
			}

			if (this.context.GT() != null) {
				if (path.getJavaType() == LocalDateTime.class)
					return cb.greaterThan(path, LocalDateTime.parse(this.value.toString()));
				if (path.getJavaType() == LocalTime.class)
					return cb.greaterThan(path, LocalTime.parse(this.value.toString()));
				if (path.getJavaType() == LocalDate.class)
					return cb.greaterThan(path, LocalDate.parse(this.value.toString()));
				if (path.getJavaType() == Long.class)
					return cb.greaterThan(path, Long.parseLong(this.value.toString()));
				if (path.getJavaType() == Float.class)
					return cb.greaterThan(path, Float.parseFloat(this.value.toString()));
				return cb.greaterThan(path, Integer.parseInt(this.value.toString()));
			}

			if (this.context.GE() != null) {
				if (path.getJavaType() == LocalDateTime.class)
					return cb.greaterThanOrEqualTo(path, LocalDateTime.parse(this.value.toString()));
				if (path.getJavaType() == LocalTime.class)
					return cb.greaterThanOrEqualTo(path, LocalTime.parse(this.value.toString()));
				if (path.getJavaType() == LocalDate.class)
					return cb.greaterThanOrEqualTo(path, LocalDate.parse(this.value.toString()));
				if (path.getJavaType() == Long.class)
					return cb.greaterThanOrEqualTo(path, Long.parseLong(this.value.toString()));
				if (path.getJavaType() == Float.class)
					return cb.greaterThanOrEqualTo(path, Float.parseFloat(this.value.toString()));
				return cb.greaterThanOrEqualTo(path, Integer.parseInt(this.value.toString()));
			}

			if (this.context.LT() != null) {
				if (path.getJavaType() == LocalDateTime.class)
					return cb.lessThan(path, LocalDateTime.parse(this.value.toString()));
				if (path.getJavaType() == LocalTime.class)
					return cb.lessThan(path, LocalTime.parse(this.value.toString()));
				if (path.getJavaType() == LocalDate.class)
					return cb.lessThan(path, LocalDate.parse(this.value.toString()));
				if (path.getJavaType() == Long.class)
					return cb.lessThan(path, Long.parseLong(this.value.toString()));
				if (path.getJavaType() == Float.class)
					return cb.lessThan(path, Float.parseFloat(this.value.toString()));
				return cb.lessThan(path, Integer.parseInt(this.value.toString()));
			}

			if (this.context.LE() != null) {
				if (path.getJavaType() == LocalDateTime.class)
					return cb.lessThanOrEqualTo(path, LocalDateTime.parse(this.value.toString()));
				if (path.getJavaType() == LocalTime.class)
					return cb.lessThanOrEqualTo(path, LocalTime.parse(this.value.toString()));
				if (path.getJavaType() == LocalDate.class)
					return cb.lessThanOrEqualTo(path, LocalDate.parse(this.value.toString()));
				if (path.getJavaType() == Long.class)
					return cb.lessThanOrEqualTo(path, Long.parseLong(this.value.toString()));
				if (path.getJavaType() == Float.class)
					return cb.lessThanOrEqualTo(path, Float.parseFloat(this.value.toString()));
				return cb.lessThanOrEqualTo(path, Integer.parseInt(this.value.toString()));
			}
		}

		return null;

	}

}
