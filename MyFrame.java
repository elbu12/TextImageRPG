package main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class MyFrame extends JFrame{
	private DirectionPanel directionPanel;
	private RoomButtonPanel roomButtonPanel;
	private JTextArea textArea;
	private ButtonPanel buttonPanel;
	private JLabel imageLabel;
	//The main map
	private final JPanel mainMapPanel;
	//The current map
	private JPanel currentMapPanel;
	//reused when adding new maps
	private final GridBagConstraints gbc;
	//Map panels should only be added once
	private HashSet <JPanel> mapPanels;

	public MyFrame(Scenario[][] scenarios, Driver driver) {
		super("Game");
		// Set up visual layout
		setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 3;
		imageLabel = new JLabel(new ImageIcon());
		add(imageLabel, gbc);
		gbc.gridx = 1;
		gbc.gridheight = 1;
		gbc.insets.top = 10;
		directionPanel = new DirectionPanel(driver);
		add(directionPanel, gbc);
		roomButtonPanel = new RoomButtonPanel(driver);
		roomButtonPanel.setVisible(false);
		add(roomButtonPanel, gbc);
		gbc.gridy = 1;
		gbc.insets.left = 10;
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setColumns(40);
		textArea.setRows(3);
		textArea.setFocusable(false);
		add(textArea, gbc);
		gbc.gridy = 2;
		gbc.insets.left = 0;
		gbc.insets.top = 0;
		buttonPanel = new ButtonPanel(driver);
		add(buttonPanel, gbc);
		mainMapPanel = new JPanel(new GridBagLayout());
		int height = scenarios.length;
		int width = scenarios[0].length;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		for (int i=0; i<height; i++) {
			gbc.gridx = 0;
			for (int j=0; j<width; j++) {
				scenarios[i][j].makeLabel();
				mainMapPanel.add(scenarios[i][j].getLabel(), gbc);
				gbc.gridx++;
			}
			gbc.gridy++;
		}
		mapPanels = new HashSet <> ();
		setMapPanel(mainMapPanel);
		addKeyListener(new KeyHandler(driver));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void drawScenario(Scenario scenario) {
		scenario.updateDirectionPanel(this);
		setButtons(scenario.getButtons());
		setText(scenario.getMainText());
		setImage(scenario.getImage());
		//We also update the scenario labels on the map
		//panel, but this is done directly by the
		//scenarios
	}

	//Either the direction buttons or room buttons are visible,
	//never both
	public void setDirections(boolean[] directions) {
		roomButtonPanel.setVisible(false);
		directionPanel.setVisible(true);
		directionPanel.setButtons(directions);
	}

	public void setRoomButtons(ArrayList[] rooms) {
		if (rooms == null) {
			setDirections(Scenario.NO_DIRECTIONS);
			return;
		}
		directionPanel.setVisible(false);
		roomButtonPanel.setVisible(true);
		for (byte i=0; i<4; i++) {
			ArrayList <RoomScenario> list = (ArrayList <RoomScenario>)(rooms[i]);
			roomButtonPanel.setButtons(i, list);
		}
	}
	
	public void setButtons(String... texts) {
		buttonPanel.setButtons(texts);
	}

	public void setText(String text) {
		textArea.setText(text);
	}

	public void setImage(Image image) {
		ImageIcon icon = (ImageIcon) (imageLabel.getIcon());
		icon.setImage(image);
		imageLabel.repaint();
	}
	
	public void setMapPanel(JPanel panel) {
		if (!mapPanels.contains(panel)) {
			mapPanels.add(panel);
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.gridwidth = 3;
			gbc.gridheight = 1;
			if (panel != mainMapPanel) {
				panel.setPreferredSize(mainMapPanel.getPreferredSize());
			}
			add(panel, gbc);
		}
		if (currentMapPanel != panel) {
			if (currentMapPanel != null) {
				currentMapPanel.setVisible(false);
			}
			panel.setVisible(true);
			currentMapPanel = panel;
		}
	}
	public void resetMapPanel() {
		if (currentMapPanel != mainMapPanel) {
			currentMapPanel.setVisible(false);
			currentMapPanel = mainMapPanel;
			mainMapPanel.setVisible(true);
		}
	}

	// testing
	public static void main(String[] args) {
	
	}
}
