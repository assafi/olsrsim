/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: DispatcherTestCase.java
 * Author: Assaf
 * Date: 23/12/2009
 *
 */
package dispatcher;

import layout.LayoutException;
import log.Log;
import main.SimulationParameters;
import main.SimulationParameters.LayoutMode;
import main.SimulationParameters.StationsMode;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import data.CsvWriter;
import dispatch.Dispatcher;
import dispatch.DispatcherException;


/**
 * @author Assaf
 *
 */
public class DispatcherTestCase {

	private static Log log = null;
	private static Dispatcher dispatcher = null;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		log = Log.getInstance();
		log.createLog(new CsvWriter());	
		dispatcher = Dispatcher.getInstance();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		log.updateDB();
		log.close();
		log = null;
		dispatcher = null;
	}

	/**
	 * @throws DispatcherException
	 * @throws LayoutException
	 */
	@Test
	public void testStartSimulation() throws DispatcherException, LayoutException {
		
		SimulationParameters.topologyPoissonicRate = (float) 0.2;
		SimulationParameters.dataEventsPoissonicRate = (float) 0.2;
		SimulationParameters.layoutMode = LayoutMode.CLUSTER;
		SimulationParameters.xBoundry = 300;
		SimulationParameters.yBoundry = 300;
		SimulationParameters.receptionRadius = 50;
		SimulationParameters.stationsMode = StationsMode.DYNAMIC;
		SimulationParameters.clusterRadius = 50;
		SimulationParameters.maxStations = 150;
		SimulationParameters.simulationEndTime = 1000;
		
		dispatcher.startSimulation();
	}

}
