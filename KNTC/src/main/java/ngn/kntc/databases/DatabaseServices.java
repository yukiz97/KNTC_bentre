package ngn.kntc.databases;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import oracle.ucp.UniversalConnectionPoolAdapter;
import oracle.ucp.admin.UniversalConnectionPoolManager;
import oracle.ucp.admin.UniversalConnectionPoolManagerImpl;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import ngn.kntc.modules.KNTCProps;

public class DatabaseServices implements ServletContextListener{
	private static PoolDataSource pdsMySQL = PoolDataSourceFactory.getPoolDataSource();
	
	public DatabaseServices(){
		
	}

	public static Connection getConnection() throws SQLException{
		Connection cn=null;
		cn=pdsMySQL.getConnection();
		System.out.println("- MY SQL Browser: "+pdsMySQL.getBorrowedConnectionsCount()+" and availabel: "+pdsMySQL.getAvailableConnectionsCount());
		return cn;
	}
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("Initial Databse........");
		createUCPDatasourceForMySQL();
		System.out.println("Initial Databse Success");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Destroy context");
		try {
			UniversalConnectionPoolManager mgr = UniversalConnectionPoolManagerImpl.getUniversalConnectionPoolManager();
			mgr.stopConnectionPool("JDBC_UCP_MYSQL");
			mgr.destroyConnectionPool("JDB_UCP_MYSQL");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void createUCPDatasourceForMySQL() {
		try {
			KNTCProps props = new KNTCProps();
			String url=props.getProperty("jdbc.default.server.url");
			String userName=props.getProperty("jdbc.default.username");
			String password=props.getProperty("jdbc.default.password");
			String className = props.getProperty("jdbc.default.driverclassname");
			pdsMySQL.setConnectionFactoryClassName(className);
			pdsMySQL.setURL(url);
			pdsMySQL.setUser(userName);
			pdsMySQL.setPassword(password);
			pdsMySQL.setConnectionPoolName("JDBC_UCP_MYSQL");
			pdsMySQL.setInitialPoolSize(100);
			pdsMySQL.setMinPoolSize(50);
			pdsMySQL.setMaxPoolSize(100);
			pdsMySQL.setMaxConnectionReuseTime(300);
			pdsMySQL.setAbandonedConnectionTimeout(10);
			UniversalConnectionPoolManager mgr = UniversalConnectionPoolManagerImpl.getUniversalConnectionPoolManager();
			mgr.createConnectionPool((UniversalConnectionPoolAdapter)pdsMySQL);
			mgr.startConnectionPool("JDBC_UCP_MYSQL");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}


