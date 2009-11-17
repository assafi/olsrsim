/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: LocalInformationBase.java
 * Author: Eli Nazarov
 * Date: Nov 14, 2009
 *
 */
package protocol.InformationBases;

import java.util.HashMap;
import java.util.Map;

import protocol.ProtocolException;

/**
 * @author Eli Nazarov
 *
 */
public class LocalInformationBase {
	
	//TODO add the removed database
	private Map<String, String> localActiveInterfaces = null;
	
	
	public LocalInformationBase(){
		localActiveInterfaces = new HashMap<String, String>();
	}
	
	public void addInterfaceAddress(String iface, String addr) throws ProtocolException{
		
		//TODO see if one iface can have more then one address
		if (localActiveInterfaces.containsKey(iface)){
			throw new ProtocolException("Local Interface already exists");
		}
		
		localActiveInterfaces.put(iface, addr);
	}
	
	public void removeInterface(String iface) throws ProtocolException{
		
		//TODO see if one iface can have more then one address
		
		localActiveInterfaces.remove(iface);
	}
	
	public String getInterfaceAddress(String iface) throws ProtocolException{
		
		if (!localActiveInterfaces.containsKey(iface)){
			throw new ProtocolException("Local Interface dosn't exist");
		}
		
		return localActiveInterfaces.get(iface);
	}

	/**
	 * @return all local Active Interfaces
	 */
	public Map<String, String> getAllLocalInterfaces() {
		return localActiveInterfaces;
	}
	
	public boolean isInterfaceExists(String iface){	
		return localActiveInterfaces.containsKey(iface);
	}
}

