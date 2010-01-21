/**
 * OLSRv2
 * 
 * Team members: Assaf Israel, Eli Nazarov, Asi Bross
 *
 * File: InputsPanel.java
 * Author: Asi
 * Date: 01/01/2010
 *
 */
package gui;

import gui.input_params.ClusterNum;
import gui.input_params.ClusterRadius;
import gui.input_params.InputException;
import gui.input_params.InputParam;
import gui.input_params.LayoutMode;
import gui.input_params.StationsMode;
import gui.input_params.WorldSize;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import main.SimulationParameters;

/**
 * @author Asi
 *
 */
public class LayoutTab extends JPanel {
	private static final long serialVersionUID = -8962273795833586231L;
	private static LayoutTab instance = null;

	private List<InputParam> parameters;
	
	private ClusterRadius clusterRadius;
	private ClusterNum clusterNum;
	
	private JPanel innerPanel;
	private boolean clusterParamsVisible = false;
	
	/**
	 * @return
	 */
	public static LayoutTab getInstance() {
		if(null == instance) {
			instance = new LayoutTab();
		}
		return instance;
	}
	
	/**
	 * 
	 */
	private LayoutTab() {
		this.setBackground(GUIManager.BACKGROUND);
		this.setBorder(new LineBorder(Color.black));
		innerPanel = new JPanel();
		this.setInputEntries();
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		clusterRadius = new ClusterRadius();
		clusterNum = new ClusterNum();
		innerPanel.setLayout(new GridLayout(parameters.size(), 1));
		this.add(innerPanel);
		for (InputParam entry : parameters) {
			innerPanel.add(entry);
		}
	}
	
	/**
	 * @param b
	 */
	public void setClusterEntries(boolean b) {
		if(b == clusterParamsVisible) {
			return;
		}
		clusterParamsVisible = b;
		if(b) {
			parameters.add(clusterRadius);
			parameters.add(clusterNum);
			innerPanel.add(clusterRadius);
			innerPanel.add(clusterNum);
		}
		else {
			parameters.remove(clusterRadius);
			parameters.remove(clusterNum);
			innerPanel.remove(clusterRadius);
			innerPanel.remove(clusterNum);
		}
		innerPanel.setLayout(new GridLayout(parameters.size(), 1));
	}
	
	/**
	 * @throws InputException 
	 * 
	 */
	public void updateParams() throws InputException {
		for (InputParam param : parameters) {
			param.updateParamValue();
		}
	}

	private void setInputEntries() {
		parameters = new LinkedList<InputParam>();
		parameters.add(new WorldSize());
		parameters.add(new StationsMode());
		parameters.add(new LayoutMode());
		if(SimulationParameters.layoutMode == SimulationParameters.LayoutMode.CLUSTER) {
			setClusterEntries(true);
		}
	}
}
