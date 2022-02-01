package it.osys.jaxrsodata;

import org.hibernate.EmptyInterceptor;

public class CurrentLanguageInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 1L;

	private static final ThreadLocal<String> threadLocalValue = new ThreadLocal<>();

	public static void setLang(String lang) {
		threadLocalValue.set(lang);
	}

	@Override
	public String onPrepareStatement(String sql) {
		CharSequence lang = threadLocalValue.get() == null ? "" : threadLocalValue.get();
		System.err.println(lang);
		return sql.replace("{CURRENT_LANGUAGE}", lang);
	}

}