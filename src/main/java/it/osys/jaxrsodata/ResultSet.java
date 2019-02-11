package it.osys.jaxrsodata;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;

/**
 * Default output class
 * 
 * @author Domenico Briganti
 *
 * @param <T>
 */
public class ResultSet<T> {

	@XmlElement(name = "@count")
	public long count;

	public Collection<T> value;

	@Override
	public String toString() {
		return "ResultSet [count=" + count + ", value=" + value + "]";
	}

}
