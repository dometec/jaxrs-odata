package it.osys.jaxrsodata;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.JdbcConnectionAccess;
import org.hibernate.internal.SessionImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import it.osys.jaxrsodata.entity.TestEntity;

public class HSQLDBInitialize extends OData<TestEntity> {

	protected static EntityManagerFactory emf;
	protected static EntityManager em;
	protected static IDatabaseConnection connection;
	protected static IDataSet dataset;

	private static JdbcConnectionAccess jdbcConnectionAccess;
	private static Connection jdbcConnection;

	public HSQLDBInitialize() {
		super(TestEntity.class);
	}

	@BeforeAll
	public static void init() throws HibernateException, DatabaseUnitException, SQLException {

		emf = Persistence.createEntityManagerFactory("test_persistence_unit");
		em = emf.createEntityManager();

		jdbcConnectionAccess = ((SessionImpl) (em.getDelegate())).getJdbcConnectionAccess();
		jdbcConnection = jdbcConnectionAccess.obtainConnection();
		connection = new DatabaseConnection(jdbcConnection);
		connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());

		FlatXmlDataSetBuilder flatXmlDataSetBuilder = new FlatXmlDataSetBuilder();
		flatXmlDataSetBuilder.setColumnSensing(true);
		InputStream dataSet = Thread.currentThread().getContextClassLoader().getResourceAsStream("test-data.xml");
		dataset = flatXmlDataSetBuilder.build(dataSet);

	}

	@BeforeEach
	public void cleanDB() throws DatabaseUnitException, SQLException {
		DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
		connection.getConnection().commit();
	}

	@AfterAll
	public static void tearDown() throws SQLException {
		if (jdbcConnection != null)
			jdbcConnectionAccess.releaseConnection(jdbcConnection);

		if (em != null) {
			em.clear();
			em.close();
		}

		if (emf != null)
			emf.close();
	}

}
