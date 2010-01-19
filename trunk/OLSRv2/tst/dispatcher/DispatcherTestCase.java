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

import static org.junit.Assert.*;

import java.awt.Point;

import layout.LayoutException;
import layout.UniformLayout;
import log.Log;
import log.dataserver.SqlWriter;
import main.SimulationParameters;
import main.SimulationParameters.LayoutMode;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import topology.Station;

import data.CsvWriter;
import dispatch.Dispatcher;
import dispatch.DispatcherException;
import events.Event;
import events.StopEvent;
import events.TopologyEvent;
import events.TopologyEvent.TopologyEventType;


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
		
		SimulationParameters.factor = (float) 0.2;
		SimulationParameters.layoutMode = LayoutMode.UNIFORM;
		SimulationParameters.xBoundry = 100;
		SimulationParameters.yBoundry = 100;
		SimulationParameters.receptionRadius = 40;
		SimulationParameters.staticMode = true;
		SimulationParameters.clusterRadius = 20;
		SimulationParameters.maxStations = 10;
		SimulationParameters.simulationEndTime = 3000;
		
		dispatcher.startSimulation();
	}

}
