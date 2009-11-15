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
package protocol;

import java.util.HashMap;

/**
 * @author Eli Nazarov
 *
 */
public class LocalInformationBase {
	private HashMap<String, Address> localActiveInterfaces = null;
	
	private static LocalInformationBase instance = null;
	
	private LocalInformationBase(){
	}
	
	public static synchronized LocalInformationBase getInstance(){
		if (null == instance){
			instance = new LocalInformationBase();
			instance.localActiveInterfaces = new HashMap<String, Address>();
		}
		
		return instance;
	}
	
	public void addInterfaceAddress(String iface, Address addr) throws ProtocolException{
		
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
	
	public Address getInterfaceAddress(String iface) throws ProtocolException{
		
		if (!localActiveInterfaces.containsKey(iface)){
			throw new ProtocolException("Local Interface dosn't exist");
		}
		
		return localActiveInterfaces.get(iface);
	}

	/**
	 * @return all local Active Interfaces
	 */
	public HashMap<String, Address> getAllLocalInterfaces() {
		return localActiveInterfaces;
	}
	
	public boolean isInterfaceExists(String iface){	
		return localActiveInterfaces.containsKey(iface);
	}
}

