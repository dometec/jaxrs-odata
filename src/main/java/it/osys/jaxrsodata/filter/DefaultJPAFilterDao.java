package it.osys.jaxrsodata.filter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import it.osys.jaxrsodata.ODataFilterParser.ExprContext;
import it.osys.jaxrsodata.exceptions.NotImplementedException;

public class DefaultJPAFilterDao<T> {

	private ExprContext context;

	private Root<T> root;
	private CriteriaBuilder cb;

	private Object value;
	private Object field;

	private Integer indexField = 0;
	private Integer indexValue = 2;
	private Boolean isValueFromFunction = false;
	private Boolean isValueFromContainFunction = false;

	/**
	 * Gather field and value from context and set those according to the index
	 * inside the context.
	 * 
	 * @throws NotImplementedException
	 */
	private void setParameters() throws NotImplementedException {

		this.field = this.context.getChild(indexField).getText();
		this.value = this.context.getChild(indexValue).getText().replace("'", "");

		if (isValueFromFunction) {
			this.field = this.context.getChild(indexField + 2).getText();
			this.value = this.context.getChild(indexValue + 3).getText().replace("'", "");
		} else if (isValueFromContainFunction) {
			this.field = this.context.getChild(indexField + 2).getText();
			this.value = this.context.getChild(indexValue + 2).getText().replace("'", "");
			// [contains, (, device_type, ,, '7', )]
		} else {
			Path<Object> path = getPathFromField(this.field.toString());
			this.value = convValueToFieldType(path.getJavaType(), this.value.toString().replace("'", ""));
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
	 * @throws NotImplementedException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <F extends Object> F convValueToFieldType(Class<F> javaType, String value) throws NotImplementedException {

		// If i just want to check if the db field is empty just skip value
		// casting.
		if (value.equals("null"))
			return null;

		// Cast value accorting to db field type.
		if (javaType.isAssignableFrom(String.class))
			return (F) value;

		if (javaType.isEnum())
			return (F) Enum.valueOf((Class<Enum>) javaType, value);

		if (javaType.isAssignableFrom(Integer.class) || javaType.isAssignableFrom(int.class))
			return (F) Integer.valueOf(value);

		if (javaType.isAssignableFrom(Long.class))
			return (F) Long.valueOf(value);

		if (javaType.isAssignableFrom(LocalDateTime.class))
			return (F) LocalDateTime.parse(value.split("\\.")[0]);

		if (javaType.isAssignableFrom(LocalTime.class))
			return (F) LocalTime.parse(value);

		if (javaType.isAssignableFrom(LocalDate.class))
			return (F) LocalDate.parse(value);

		if (javaType.isAssignableFrom(Boolean.class) || javaType.isAssignableFrom(boolean.class))
			return (F) Boolean.valueOf(value);

		throw new NotImplementedException("Field type " + javaType.getName() + " not implemented yet!");
	}

	/**
	 * Set JPA root
	 * 
	 * @param root
	 */
	public void setRoot(Root<T> root) {
		this.root = root;
	}

	/**
	 * Set CriteriaBuilder
	 * 
	 * @param cb
	 */
	public void setCb(CriteriaBuilder cb) {
		this.cb = cb;
	}

	/**
	 * Setup this class by passing the context
	 * 
	 * @param context
	 * @throws NotImplementedException
	 */
	public void setup(ExprContext context) throws NotImplementedException {
		if (context.children != null) {
			this.context = context;

			// If the first element of the filter is a function (ex.
			// "tolower(value)") we need to shift the index of value and field
			// accordingly.
			if (this.context.getChild(indexField).getText().equals("tolower")
					|| this.context.getChild(indexField).getText().equals("toupper")) {
				isValueFromFunction = true;
			} else if (this.context.getChild(indexField).getText().equals("contains")) {
				isValueFromContainFunction = true;
			}

			setParameters();
		}
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

			Path path = getPathFromField(this.field.toString());

			if (this.context.NULL() != null) {
				if (this.context.EQ() != null) {
					return cb.isNull(path);
				}
				return cb.isNotNull(path);
			}

			if (context.CONTAINS() != null) {
				if (context.parent != null && context.getParent().getChild(0).getText().equals("not"))
					return cb.notLike(path.as(String.class), "%" + this.value.toString() + "%");
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

			if (this.context.GT() != null) {
				if (path.getJavaType() == LocalDateTime.class)
					return cb.greaterThan(path, LocalDateTime.parse(this.value.toString()));
				if (path.getJavaType() == LocalTime.class)
					return cb.greaterThan(path, LocalTime.parse(this.value.toString()));
				if (path.getJavaType() == LocalDate.class)
					return cb.greaterThan(path, LocalDate.parse(this.value.toString()));
				if (path.getJavaType() == Long.class)
					return cb.greaterThan(path, Long.parseLong(this.value.toString()));
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
				return cb.lessThanOrEqualTo(path, Integer.parseInt(this.value.toString()));
			}
		}

		return null;
	}

	private Path<Object> getPathFromField(String field) {
		String[] fieldname = field.split("/");
		Path<Object> path = null;
		for (int idx = 0; idx < fieldname.length; idx++)
			if (path != null)
				path = path.get(fieldname[idx]);
			else
				path = root.get(fieldname[idx]);
		return path;
	}

}
