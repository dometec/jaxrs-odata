package it.osys.jaxrsodata.orderby;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

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

		String[] fieldname = context.getChild(0).getText().split("/");
		Path<Object> path = null;
		for (int idx = 0; idx < fieldname.length; idx++)
			if (path != null)
				path = path.get(fieldname[idx]);
			else
				path = root.get(fieldname[idx]);

		if (context.DESC() != null)
			return cb.desc(path);

		return cb.asc(path);

	}

}