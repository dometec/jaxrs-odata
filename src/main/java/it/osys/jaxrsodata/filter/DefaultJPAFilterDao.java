package it.osys.jaxrsodata.filter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.criteria.CriteriaBuilder;
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
			this.value = convValueToFieldType(root.get(this.field.toString()).getJavaType(), this.value.toString().replace("'", ""));
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

		throw new NotImplementedException("Field type " + javaType.getClass().getName() + " not implemented yet!");
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
	public Predicate getPredicate() {

		if (this.context != null) {

			if (this.context.NULL() != null) {
				if (this.context.EQ() != null) {
					return cb.isNull(root.get(this.field.toString()));
				}
				return cb.isNotNull(root.get(this.field.toString()));
			}

			if (context.CONTAINS() != null) {
				if (context.parent != null && context.getParent().getChild(0).getText().equals("not"))
					return cb.notLike(root.get(this.field.toString()).as(String.class), "%" + this.value.toString() + "%");
				return cb.like(root.get(this.field.toString()).as(String.class), "%" + this.value.toString() + "%");
			}

			if (this.context.EQ() != null) {
				if (context.TOLOWER() != null) {
					return cb.equal(cb.lower(root.get(this.field.toString())), this.value);
				} else if (context.TOUPPER() != null) {
					return cb.equal(cb.upper(root.get(this.field.toString())), this.value);
				} else
					return cb.equal(root.get(this.field.toString()), this.value);
			}

			if (this.context.NE() != null)
				return cb.notEqual(root.get(this.field.toString()), this.value);

			if (this.context.GT() != null) {
				if (root.get(this.field.toString()).getJavaType() == LocalDateTime.class)
					return cb.greaterThan(root.get(this.field.toString()), LocalDateTime.parse(this.value.toString()));
				if (root.get(this.field.toString()).getJavaType() == LocalTime.class)
					return cb.greaterThan(root.get(this.field.toString()), LocalTime.parse(this.value.toString()));
				if (root.get(this.field.toString()).getJavaType() == LocalDate.class)
					return cb.greaterThan(root.get(this.field.toString()), LocalDate.parse(this.value.toString()));
				if (root.get(this.field.toString()).getJavaType() == Long.class)
					return cb.greaterThan(root.get(this.field.toString()), Long.parseLong(this.value.toString()));
				return cb.greaterThan(root.get(this.field.toString()), Integer.parseInt(this.value.toString()));
			}

			if (this.context.GE() != null) {
				if (root.get(this.field.toString()).getJavaType() == LocalDateTime.class)
					return cb.greaterThanOrEqualTo(root.get(this.field.toString()), LocalDateTime.parse(this.value.toString()));
				if (root.get(this.field.toString()).getJavaType() == LocalTime.class)
					return cb.greaterThanOrEqualTo(root.get(this.field.toString()), LocalTime.parse(this.value.toString()));
				if (root.get(this.field.toString()).getJavaType() == LocalDate.class)
					return cb.greaterThanOrEqualTo(root.get(this.field.toString()), LocalDate.parse(this.value.toString()));
				if (root.get(this.field.toString()).getJavaType() == Long.class)
					return cb.greaterThanOrEqualTo(root.get(this.field.toString()), Long.parseLong(this.value.toString()));
				return cb.greaterThanOrEqualTo(root.get(this.field.toString()), Integer.parseInt(this.value.toString()));
			}

			if (this.context.LT() != null) {
				if (root.get(this.field.toString()).getJavaType() == LocalDateTime.class)
					return cb.lessThan(root.get(this.field.toString()), LocalDateTime.parse(this.value.toString()));
				if (root.get(this.field.toString()).getJavaType() == LocalTime.class)
					return cb.lessThan(root.get(this.field.toString()), LocalTime.parse(this.value.toString()));
				if (root.get(this.field.toString()).getJavaType() == LocalDate.class)
					return cb.lessThan(root.get(this.field.toString()), LocalDate.parse(this.value.toString()));
				if (root.get(this.field.toString()).getJavaType() == Long.class)
					return cb.lessThan(root.get(this.field.toString()), Long.parseLong(this.value.toString()));
				return cb.lessThan(root.get(this.field.toString()), Integer.parseInt(this.value.toString()));
			}

			if (this.context.LE() != null) {
				if (root.get(this.field.toString()).getJavaType() == LocalDateTime.class)
					return cb.lessThanOrEqualTo(root.get(this.field.toString()), LocalDateTime.parse(this.value.toString()));
				if (root.get(this.field.toString()).getJavaType() == LocalTime.class)
					return cb.lessThanOrEqualTo(root.get(this.field.toString()), LocalTime.parse(this.value.toString()));
				if (root.get(this.field.toString()).getJavaType() == LocalDate.class)
					return cb.lessThanOrEqualTo(root.get(this.field.toString()), LocalDate.parse(this.value.toString()));
				if (root.get(this.field.toString()).getJavaType() == Long.class)
					return cb.lessThanOrEqualTo(root.get(this.field.toString()), Long.parseLong(this.value.toString()));
				return cb.lessThanOrEqualTo(root.get(this.field.toString()), Integer.parseInt(this.value.toString()));
			}
		}

		return null;
	}

}
