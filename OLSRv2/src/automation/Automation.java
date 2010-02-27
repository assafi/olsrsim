/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: Automation.java
 * Author: Assaf
 * Date: 22/02/2010
 *
 */
package automation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import log.sqlproxy.SqlProxy;
import log.sqlproxy.SqlProxyDefinitions;
import log.sqlproxy.SqlProxyException;
import main.SimulationParameters.LayoutMode;
import main.SimulationParameters.ProtocolDataSendMode;
import main.SimulationParameters.ProtocolMprMode;
import main.SimulationParameters.StationsMode;
import main.SimulationParameters.StationsSpeed;

/**
 * @author Assaf
 *
 */
public class Automation {

	private static SqlProxy proxy = null;
	private static String host = SqlProxyDefinitions.host;
	private static int port = SqlProxyDefinitions.port;
	private static String database = SqlProxyDefinitions.database;
	private static String dbUsername = SqlProxyDefinitions.user;
	private static String dbPassword = SqlProxyDefinitions.password;

	private static String fileName = "logs/Record_27022010_b";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			init();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			System.exit(1);
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.exit(1);
		}

		ProtocolMprMode protocolMode = ProtocolMprMode.ALL_MPRS;
//		for (ProtocolMprMode protocolMode : ProtocolMprMode.values()) {
			ProtocolDataSendMode protocolDataSendMode = ProtocolDataSendMode.MPRS;
//			for (ProtocolDataSendMode protocolDataSendMode : ProtocolDataSendMode.values()) {
				LayoutMode layoutMode = LayoutMode.CLUSTER;
//				for (LayoutMode layoutMode : LayoutMode.values()) {
					for (double gamma = 0.6 ; gamma <= 0.6 ; gamma += 0.1) {
						StationsMode stationsMode = StationsMode.STATIC;
						StationsSpeed stationSpeed = StationsSpeed.LOW;
//						recordSim(protocolMode, protocolDataSendMode, layoutMode, gamma, stationsMode, stationSpeed, fileName);

						stationsMode = StationsMode.DYNAMIC;
//						recordSim(protocolMode, protocolDataSendMode, layoutMode, gamma, stationsMode, stationSpeed, fileName);
						stationSpeed = StationsSpeed.MEDIUM;
						recordSim(protocolMode, protocolDataSendMode, layoutMode, gamma, stationsMode, stationSpeed, fileName);
						stationSpeed = StationsSpeed.HIGH;
						recordSim(protocolMode, protocolDataSendMode, layoutMode, gamma, stationsMode, stationSpeed, fileName);
					}
//				}
//			}
//		}
	}

	/**
	 * @param protocolMode
	 * @param protocolDataSendMode
	 * @param layoutMode
	 * @param gamma
	 * @param stationsMode
	 * @param stationSpeed
	 * @param fileName
	 */
	private static void recordSim(ProtocolMprMode protocolMode,
			ProtocolDataSendMode protocolDataSendMode, LayoutMode layoutMode,
			double gamma, StationsMode stationsMode,
			StationsSpeed stationSpeed, String fileName) {

		File file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		String command = "java -jar OLSRv2.jar ";

		int validPeriod = 300;
		int tcInterval = 60;
		int helloInterval = 30;
		int transmitionTime = 1;
		int clusterRadius = 150;
		int clusterNum = 15;
		int xBoundry = 500;
		int yBoundry = 500;
		int receptionRadius = 100;
		int simulationEndTime = 2500;
		double topologyPoissonicRate = 0.2;
		double dataEventsPoissonicRate = gamma;
		int maxStations = 30;
		int stationHopDistance = 2;

		command += validPeriod + " " + tcInterval + " " + helloInterval + " ";
		command += transmitionTime + " " + protocolMode.name() + " " + protocolDataSendMode.name() + " ";
		command += layoutMode.name() + " " + clusterRadius + " " + clusterNum + " ";
		command += stationsMode.name() + " " + xBoundry + " " + yBoundry + " ";
		command += receptionRadius + " " + simulationEndTime + " " + topologyPoissonicRate + " ";
		command += dataEventsPoissonicRate + " " + maxStations + " " + stationSpeed.name() + " ";
		command += stationHopDistance;

		String out = null;
		String table = null;
		String util = null;
		try {
			Process p = Runtime.getRuntime().exec(command);

			BufferedReader stdInput = new BufferedReader(new 
					InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new 
					InputStreamReader(p.getErrorStream()));

			// read the output from the command
			while ((out = stdInput.readLine()) != null) {
				if (null == table) {
					System.out.println();
					table = out;
				}
			}

			// read any errors from the attempted command
			while ((out = stdError.readLine()) != null) {
				System.out.println(out);
			}


			//Wait for completion

			try { 
				p.waitFor(); 
			} 
			catch (InterruptedException e) {
				System.err.println("Process was interrupted"); 
			}


			stdInput.close();
			stdError.close();

			try {
				util = queryUtil(table, simulationEndTime, maxStations, transmitionTime);
				System.out.println(util);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (SqlProxyException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.out.println("exception happened - here's what I know: ");
			e.printStackTrace();
			System.exit(-1);
		}
		if (null != util) {

			try{
				// Create file 
				FileWriter fstream = new FileWriter(file, true);
				BufferedWriter bw = new BufferedWriter(fstream);
				
				bw.write("\n\n");
				bw.write("***** ***** *****\n");
				bw.write("Data rate: " + dataEventsPoissonicRate + "\n");
				bw.write("Station mode: " + stationsMode.name() + "\n");
				bw.write("Speed: " + stationSpeed.name() + "\n");
				bw.write("Utilization: " + util + "\n");
				bw.write("MPR Selection mode: " + protocolMode.name() + "\n");
				bw.write("Data transmission mode: " + protocolDataSendMode.name() + "\n");
				bw.write("Topology: " + layoutMode.name() + "\n");
				bw.write("Hello interval: " + helloInterval + "\n");
				bw.write("TC interval: " + tcInterval + "\n");
				
				bw.close();
			}catch (Exception e){//Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}
		}

		try {
			dropTable(table);
		} catch (SqlProxyException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * 
	 */
	private static void init() throws ClassNotFoundException, SQLException {
		proxy = new SqlProxy();
		proxy.connect(host, port, dbUsername, dbPassword, database);
	}

	/**
	 * @param table
	 * @return
	 * @throws SQLException 
	 * @throws SqlProxyException 
	 */
	private static String queryUtil(String table, int simulationEndTime, int maxStations,
			int transmissionTime) throws SQLException, SqlProxyException {
		if (!tableExists(table)) {
			throw new SQLException("Table " + table + " cannot be found");
		}
		PreparedStatement statement = null;
		ResultSet result = null;

		try {
			String query = "SELECT (num_data_msgs * " + transmissionTime + " ) / " +
			"(" + simulationEndTime + " * " + maxStations + ") " +
			"AS Utilization FROM " +
			"(SELECT count(*) AS num_data_msgs FROM " + table + " s1 " +
			"WHERE EVENT_TYPE='DATA_REACHED_2_TARGET') a ;" ;

			statement = proxy.preparedStatement(query);
			result = statement.executeQuery();

			/*
			 * If table exists, results are not empty.
			 */
			result.first();
			return result.getString("Utilization");
		} finally {
			SqlProxy.closeAllSQLConnections(new Object[] {statement,result});
		}
	}

	private static boolean tableExists(String table) throws SqlProxyException, SQLException {

		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			statement = proxy.preparedStatement("SELECT table_name FROM information_schema.tables " +
			"WHERE table_schema=? AND table_name=?");
			statement.setString(1, "olsr");
			statement.setString(2, table);
			result = statement.executeQuery();

			/*
			 * If table exists, results are not empty.
			 */
			return result.first();
		} finally {
			SqlProxy.closeAllSQLConnections(new Object[] {statement,result});
		}
	}

	private static void dropTable(String tableName) throws SqlProxyException, SQLException 
	{
		if (!tableExists(tableName)){
			return;
		}

		String query = "DROP TABLE " + tableName;
		PreparedStatement statement = null;
		try {
			statement = proxy.preparedStatement(query);
			statement.executeUpdate();
		} finally {
			SqlProxy.closeAllSQLConnections(new Object[] {statement});
		}
	}
}
