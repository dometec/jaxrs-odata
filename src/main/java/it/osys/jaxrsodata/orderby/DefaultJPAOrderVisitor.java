package it.osys.jaxrsodata.orderby;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import it.osys.jaxrsodata.OData;
import it.osys.jaxrsodata.antlr4.ODataOrderByParser.ExprContext;

public class DefaultJPAOrderVisitor<T> implements JPAOrderVisitor<T> {

	private Root<T> root;
	private CriteriaBuilder cb;

	@Override
	public void setRoot(Root<T> root) {
		this.root = root;
	}

	@Override
	public void setCb(CriteriaBuilder cb) {
		this.cb = cb;
	}

	@Override
	public Object visit(ExprContext context) {

		if (context.getChild(0) == null)
			return null;

		String fields = context.getChild(0).getText();
		Path<Object> path = OData.getPathFromField(root, fields);

		if (context.DESC() != null)
			return cb.desc(path);

		return cb.asc(path);

	}

}