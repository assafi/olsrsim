/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: LocalInfoBaseTest.java
 * Author: Eli Nazarov
 * Date: Nov 15, 2009
 *
 */
package protocol;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import protocol.InformationBases.LocalInformationBase;
import protocol.OLSRv2Protocol.ProtocolException;

/**
 * @author Eli Nazarov
 *
 */
public class LocalInfoBaseTest {

	static public LocalInformationBase base = null;
	private String addr1 = new String("10.0.0.1");
	private String addr2 = new String("10.0.0.2");
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		base = new LocalInformationBase();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		base = null;
	}

	/**
	 * Test method for {@link protocol.InformationBases.LocalInformationBase#addInterfaceAddress(java.lang.String, protocol.Address)}.
	 */
	@Test
	public void testAddInterfaceAddress() {
		try {
			base.addInterfaceAddress("eth0", addr1);
			base.addInterfaceAddress("eth1", addr2);
		} catch (ProtocolException e) {
			fail();
		}
	}
	
	@Test
	public void testRemoveInterface() {
		try {
			base.addInterfaceAddress("eth2", addr1);
		} catch (ProtocolException e) {
			fail();
		}
		
		try {
			base.removeInterface("eth2");
		} catch (ProtocolException e) {
			fail();
		}
		
		if (base.isInterfaceExists("eth2")){
			fail();
		}
		
	}

	/**
	 * Test method for {@link protocol.InformationBases.LocalInformationBase#getAllLocalInterfaces()}.
	 */
	@Test
	public void testGetAllLocalInterfaces() {
		Map<String, String> map =  base.getAllLocalInterfaces();
		System.out.println(map.get("eth0"));
		System.out.println(map.get("eth1"));
	}

}
