package it.osys.jaxrsodata;

import java.io.InputStream;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.HibernateException;
import org.hibernate.internal.SessionImpl;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import it.osys.jaxrsodata.Odata;
import it.osys.jaxrsodata.entity.TestEntity;

public class HSQLDBInitialize extends Odata<TestEntity> {

	protected static EntityManagerFactory emf;
	protected static EntityManager em;
	protected static IDatabaseConnection connection;
	protected static IDataSet dataset;

	@BeforeClass
	public static void init() throws HibernateException, DatabaseUnitException {

		emf = Persistence.createEntityManagerFactory("test_persistence_unit");
		em = emf.createEntityManager();

		connection = new DatabaseConnection(((SessionImpl) (em.getDelegate())).connection());
		connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());

		FlatXmlDataSetBuilder flatXmlDataSetBuilder = new FlatXmlDataSetBuilder();
		flatXmlDataSetBuilder.setColumnSensing(true);
		InputStream dataSet = Thread.currentThread().getContextClassLoader().getResourceAsStream("test-data.xml");
		dataset = flatXmlDataSetBuilder.build(dataSet);

	}

	/**
	 * Will clean the dataBase before each test
	 * 
	 * @throws SQLException
	 * @throws DatabaseUnitException
	 */
	@Before
	public void cleanDB() throws DatabaseUnitException, SQLException {
		DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
	}

	@AfterClass
	public static void tearDown() {
		if (em != null) {
			em.clear();
			em.close();
		}

		if (emf != null)
			emf.close();
	}

}
