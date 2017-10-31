package SpriteAnimator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import SpriteAnimator.Listeners.*;
import SpriteManipulator.SpriteManipulator;

public class GUI {
	// version number
	static final String VERSION = SpriteAnimator.VERSION;
	static final String[] ALLFRAMES = Database.ALLFRAMES;

	private static final String[] MODES = {
			"Normal play",
			"Step-by-step",
			//"All frames" disabled for now
	};

	private static final String[] SWORDLEVELS = {
			"No sword",
			"Fighter's sword",
			"Master sword",
			"Tempered sword",
			"Butter sword"
	};

	private static final String[] SHIELDLEVELS = {
			"No shield",
			"Fighter's shield",
			"Red shield",
			"Mirror shield"
	};

	private static final String[] MAILLEVELS = {
			"Green mail",
			"Blue mail",
			"Red mail",
			"Bunny"
	};

	private static final String[] BACKGROUNDS = Backgrounds.BACKGROUNDNAMES;

	// use func
	public void printGUI(String[] args) throws IOException {
		//try to set LaF
		try {
			UIManager.setLookAndFeel("metal");
		} catch (UnsupportedLookAndFeelException
				| ClassNotFoundException
				| InstantiationException
				| IllegalAccessException e) {
			// try to set System default
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (UnsupportedLookAndFeelException
					| ClassNotFoundException
					| InstantiationException
					| IllegalAccessException e2) {
					// do nothing
			} //end System
		} // end Nimbus

		ToolTipManager.sharedInstance().setInitialDelay(100);
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE); // 596:31:23.647
		final JFrame frame = new JFrame("Sprite Animator " + VERSION);
		final Dimension d = new Dimension(800, 600);
		Border rightPad = BorderFactory.createEmptyBorder(0,0,0,5);
		Border fullPad = BorderFactory.createEmptyBorder(3,3,3,3);
		Dimension textDimension = new Dimension(50,20);

		// image loading
		final JPanel loadWrap = new JPanel(new BorderLayout());
		final JPanel loadBtnWrap = new JPanel(new BorderLayout());
		loadBtnWrap.setPreferredSize(new Dimension(200, 16));
		loadWrap.setBorder(fullPad);
		final JTextField fileName = new JTextField("");
		final JButton loadBtn = new JButton("Load sprite");
		final JButton reloadBtn = new JButton("Reload sprite");

		loadBtnWrap.add(reloadBtn,BorderLayout.EAST);
		loadBtnWrap.add(loadBtn,BorderLayout.CENTER);
		loadWrap.add(loadBtnWrap, BorderLayout.EAST);
		loadWrap.add(fileName,BorderLayout.CENTER);

		// Tool Tip constants info
		final String REBUILDS =
				"This action requires a rebuild of the animation data, resetting the current frame to 1.";
		final String REBUILDS_PLURAL =
				"These actions require a rebuild of the animation data, resetting the current frame to 1.";

		// controls panel
		final JPanel controls = new JPanel(new GridBagLayout());
		final JPanel controlsWrap = new JPanel(new BorderLayout());
		controls.setBorder(fullPad);
		GridBagConstraints c = new GridBagConstraints();

		// negative so everything can just ++
		c.gridy = -1;

		// animation playing
		final JComboBox<String> animOptions = new JComboBox<String>(getAnimNames());
		final JLabel theWordAnimation = new JLabel("Animation:", SwingConstants.RIGHT);
		theWordAnimation.setBorder(rightPad);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy++;
		controls.add(theWordAnimation,c);
		c.gridwidth = 2;
		c.gridx = 1;
		controls.add(animOptions, c);

		// animation mode
		final JComboBox<String> modeOptions = new JComboBox<String>(MODES);
		final JLabel theWordMode = new JLabel(" ", SwingConstants.RIGHT);
		theWordMode.setBorder(rightPad);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy++;
		controls.add(theWordMode,c);
		c.gridwidth = 2;
		c.gridx = 1;
		controls.add(modeOptions, c);

		// blank
		c.gridy++;
		c.ipady = 10;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// background
		final JComboBox<String> bgDisp = new JComboBox<String>(BACKGROUNDS);
		final JLabel theWordBackground = new JLabel("Background:", SwingConstants.RIGHT);
		theWordBackground.setBorder(rightPad);
		setToolTip(theWordBackground,
					"Changes the backdrop.");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy++;
		controls.add(theWordBackground,c);
		c.gridwidth = 2;
		c.gridx = 1;
		controls.add(bgDisp, c);

		// blank
		c.gridy++;
		c.ipady = 10;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// sword level
		final JComboBox<String> swordLevel = new JComboBox<String>(SWORDLEVELS);
		final JLabel theWordSword = new JLabel("Gear:", SwingConstants.RIGHT); // not actually the word "Sword"
		theWordSword.setBorder(rightPad);
		setToolTip(theWordSword,
				"Controls the level and display of the sword and shield, " +
				"and the current mail palette.",
				REBUILDS_PLURAL);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy++;
		controls.add(theWordSword,c);
		c.gridwidth = 2;
		c.gridx = 1;
		controls.add(swordLevel, c);

		// shield level
		final JComboBox<String> shieldLevel = new JComboBox<String>(SHIELDLEVELS);
		final JLabel theWordShield = new JLabel("", SwingConstants.RIGHT);
		theWordShield.setBorder(rightPad);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy++;
		controls.add(theWordShield,c);
		c.gridwidth = 2;
		c.gridx = 1;
		controls.add(shieldLevel, c);

		// mail level
		final JComboBox<String> mailLevel = new JComboBox<String>(MAILLEVELS);
		final JLabel theWordMail = new JLabel("", SwingConstants.RIGHT);
		theWordMail.setBorder(rightPad);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy++;
		controls.add(theWordMail,c);
		c.gridwidth = 2;
		c.gridx = 1;
		controls.add(mailLevel, c);

		// blank
		c.gridy++;
		c.ipady = 10;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// other equipment
		final JButton equipBtn = new JButton("Toggle");
		final JLabel thePhraseMiscellaneousSpritesWithAColon = new JLabel("Misc. sprites:", SwingConstants.RIGHT);
		final JLabel equipStatus = new JLabel("ON", SwingConstants.CENTER);
		thePhraseMiscellaneousSpritesWithAColon.setBorder(rightPad);
		setToolTip(thePhraseMiscellaneousSpritesWithAColon,
				"When <b>miscellaneous sprites</b> are on " +
						"animations that contain sprites other than the playable character " +
						"will display their relevant sprites.",
				"This option does not affect the display of swords, shields, or swag duck.",
				REBUILDS);
		c.gridwidth = 1;
		c.gridy++;
		c.gridx = 0;
		controls.add(thePhraseMiscellaneousSpritesWithAColon, c);
		c.gridx = 1;
		controls.add(equipStatus, c);
		c.gridx = 2;
		controls.add(equipBtn, c);

		// shadows
		final JButton shadowBtn = new JButton("Toggle");
		final JLabel theWordShadowsWithAColon = new JLabel("Shadows:", SwingConstants.RIGHT);
		final JLabel shadowStatus = new JLabel("--", SwingConstants.CENTER);
		theWordShadowsWithAColon.setBorder(rightPad);
		setToolTip(theWordShadowsWithAColon,
				"When <b>shadows</b> are on " +
						"certain animations will display a shadow below the sprite.",
				"This option will not affect shadows that belong to other sprites.",
				REBUILDS);
		c.gridwidth = 1;
		c.gridy++;
		c.gridx = 0;
		controls.add(theWordShadowsWithAColon, c);
		c.gridx = 1;
		controls.add(shadowStatus, c);
		c.gridx = 2;
		controls.add(shadowBtn, c);

		// shadows
		final JButton neutralBtn = new JButton("Toggle");
		final JLabel theWordNeutralWithAColon = new JLabel("Neutral:", SwingConstants.RIGHT);
		final JLabel neutralStatus = new JLabel("--", SwingConstants.CENTER);
		theWordNeutralWithAColon.setBorder(rightPad);
		setToolTip(theWordNeutralWithAColon,
				"When <b>neutral frames</b> are on " +
						"certain animations will begin with the sprite at the " +
						"default standing position.",
				REBUILDS);
		c.gridwidth = 1;
		c.gridy++;
		c.gridx = 0;
		controls.add(theWordNeutralWithAColon, c);
		c.gridx = 1;
		controls.add(neutralStatus, c);
		c.gridx = 2;
		controls.add(neutralBtn, c);

		// blank
		c.gridy++;
		c.ipady = 20;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// zoom
		final JLabel zoomLevel = new JLabel("x-", SwingConstants.RIGHT);
		final JButton bigBtn = new JButton("Zoom+");
		final JButton lilBtn = new JButton("Zoom-");
		setAllSizes(zoomLevel,textDimension);
		zoomLevel.setBorder(rightPad);
		c.gridy++;
		c.gridwidth = 1;
		c.gridx = 0;
		controls.add(zoomLevel, c);
		c.gridx = 1;
		controls.add(lilBtn, c);
		c.gridx = 2;
		controls.add(bigBtn, c);

		// speed
		final JButton fasterBtn = new JButton("Speed+");
		final JButton slowerBtn = new JButton("Speed-");
		final JLabel speedLevel = new JLabel("--%", SwingConstants.RIGHT);
		setAllSizes(speedLevel,textDimension);
		speedLevel.setBorder(rightPad);
		c.gridy++;
		c.gridx = 0;
		controls.add(speedLevel, c);
		c.gridx = 1;
		controls.add(slowerBtn, c);
		c.gridx = 2;
		controls.add(fasterBtn, c);

		// blank
		c.gridy++;
		c.ipady = 20;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// frame counter
		final JLabel theWordFrameWithAColon = new JLabel("Frame:", SwingConstants.RIGHT);
		final JLabel frameCur = new JLabel("-", SwingConstants.RIGHT);
		final JLabel frameMax = new JLabel("/ -");
		frameCur.setBorder(rightPad);
		frameMax.setBorder(rightPad);
		setAllSizes(frameCur,textDimension);
		setToolTip(theWordFrameWithAColon,
				"In this case, the word \"frame\" refers to a particular " +
				"frame of animation, rather than a repaint cycle of the SNES.");
		c.gridwidth = 1;
		c.gridy++;
		c.gridx = 0;
		c.weightx = 9;
		controls.add(theWordFrameWithAColon, c);
		c.weightx = 0;
		c.gridx = 1;
		controls.add(frameCur, c);
		c.gridx = 2;
		controls.add(frameMax, c);

		// play step reset
		final JButton playBtn = new JButton("Play");
		final JButton stepBtn = new JButton("--");
		final JButton resetBtn = new JButton("Reset");
		c.gridwidth = 1;
		c.gridy++;
		c.gridx = 0;
		controls.add(playBtn, c);
		c.gridx = 1;
		controls.add(stepBtn,c);
		c.gridx = 2;
		controls.add(resetBtn,c);

		// blank
		c.gridy++;
		c.ipady = 20;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// frame info
		final JLabel frameInfo = new JLabel("");
		c.gridwidth = 3;
		c.gridy++;
		c.gridx = 0;
		controls.add(frameInfo,c);

		// control panel done

		// Credits
		final JFrame aboutFrame = new JFrame("Acknowledgements");
		final TextArea peepsList = new TextArea("", 0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		peepsList.setEditable(false);
		peepsList.append("Written by fatmanspanda"); // hey, that's me
		peepsList.append("\n\nAnimation research:\n");
		peepsList.append("http://alttp.mymm1.com/sprites/includes/animations.txt\n");
		peepsList.append(GUIHelpers.join(new String[]{
				"MikeTrethewey", // it's mike
				"TWRoxas", // helped mike with his site
				}, ", "));
		peepsList.append("\nRyuTech");
		peepsList.append("\n\nCode contribution:\n");
		peepsList.append(GUIHelpers.join(new String[]{
				"MikeTrethewey", // God dammit, stop being so helpful
				"Zarby89", // spr conversion
				}, ", "));
		peepsList.append("\n\nResources and development:\n");
		peepsList.append(GUIHelpers.join(new String[]{
				"Veetorp", // provided most valuable documentation
				"Zarby89", // various documentation and answers
				"Sosuke3" // various snes code answers
				}, ", "));
		peepsList.append("\n\nTesting and feedback:\n");
		peepsList.append(GUIHelpers.join(new String[]{
				"Jighart",
				}, ", "));
		aboutFrame.add(peepsList);

		aboutFrame.setSize(600,300);
		aboutFrame.setLocation(150,150);
		aboutFrame.setResizable(false);
		// end credits

		// menu
		final JMenuBar menu = new JMenuBar();
		
		// file menu
		final JMenu fileMenu = new JMenu("File");
		menu.add(fileMenu);

		// exit
		final JMenuItem exit = new JMenuItem("Exit");
		fileMenu.add(exit);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}});
		// end file menu

		// help menu
		final JMenu helpMenu = new JMenu("Help");
		menu.add(helpMenu);

		// Acknowledgements
		final JMenuItem peeps = new JMenuItem("About");
		helpMenu.add(peeps);
		peeps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				aboutFrame.setVisible(true);
			}});
		// end help menu

		// But what if Ganon dabs back?
		ImageIcon ico = new ImageIcon(
				getClass().getResource("/SpriteAnimator/Images/DABSMALL.png")
			);
		ImageIcon icoTask = new ImageIcon(
				getClass().getResource("/SpriteAnimator/Images/DAB.png")
			);
		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(ico.getImage());
		icons.add(icoTask.getImage());
		frame.setIconImages(icons);

		// other frame organization
		final SpriteAnimator imageArea = new SpriteAnimator();
		final SpriteAnimator run = imageArea; // just a shorter name
		// need to wrap it for a border
		final JPanel imageWrap = new JPanel(new BorderLayout());
		imageWrap.setBorder(fullPad);
		imageWrap.add(imageArea,BorderLayout.CENTER);
		frame.add(imageWrap, BorderLayout.CENTER);

		controlsWrap.add(controls,BorderLayout.NORTH);
		frame.add(controlsWrap,BorderLayout.EAST);
		frame.add(loadWrap,BorderLayout.NORTH);
		frame.setSize(d);
		frame.setMinimumSize(d);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(150,150);
		frame.setJMenuBar(menu);

		// file explorer
		final JFileChooser explorer = new JFileChooser();
		FileNameExtensionFilter sprFilter =
				new FileNameExtensionFilter("ALttP sprite files", new String[] { "spr" });
		explorer.setAcceptAllFileFilterUsed(false);
		explorer.setFileFilter(sprFilter);

		// can't clear text due to wonky code
		// have to set a blank file instead
		final File EEE = new File("");

		// TODO: uncomment this for exports
		// explorer.setCurrentDirectory(new File(".")); // quick way to set to current .jar loc

		// clear focusability of all useless components
		for (Component comp : controls.getComponents()) {
			if (comp instanceof JLabel ||
					comp instanceof JButton) {
				comp.setFocusable(false);
			}
		}

		/*
		 * Action listeners
		 */
		// read steps and count them
		run.addStepListener(new StepListener() {
			public void eventReceived(StepEvent arg0) {
				frameCur.setText(run.getFrame());
			}
		});

		// Updates frame info, but only in the correct mode
		StepListener frameInfoWatcher = new StepListener() {
			public void eventReceived(StepEvent arg0) {
				try {
					frameInfo.setText(run.getFrameInfo());
				} catch (Exception e) {
					// nothing
				}
			}};

		// listen for speed changes
		run.addSpeedListener(new SpeedListener() {
			public void eventReceived(SpeedEvent arg0) {
				if (btnAllowed("speed", run.getMode())) {
					fasterBtn.setEnabled(!run.atMaxSpeed());
					slowerBtn.setEnabled(!run.atMinSpeed());
					speedLevel.setText(run.getSpeedPercent());
				} else {
					speedLevel.setText("");
					fasterBtn.setEnabled(false);
					slowerBtn.setEnabled(false);
				}
			}
		});

		// listen for mode changes
		run.addModeListener(new ModeListener() {
			public void eventReceived(ModeEvent arg0) {
				int mode = run.getMode();
				stepBtn.setEnabled(btnAllowed("step", mode));
				slowerBtn.setEnabled(btnAllowed("speed", mode));
				fasterBtn.setEnabled(btnAllowed("speed", mode));
				if (!btnAllowed("speed", mode)) {
					speedLevel.setText("");
				}
				String stepWord;
				switch (mode) {
					case 0 :
						stepWord = "Pause";
						run.removeStepListener(frameInfoWatcher);
						break;
					case 1 :
						stepWord = "Step";
						run.addStepListener(frameInfoWatcher);
						break;
					case 2 :
						stepWord = "Pause";
						run.removeStepListener(frameInfoWatcher);
						break;
					default :
						stepWord = "Step";
						run.removeStepListener(frameInfoWatcher);
						break;
				}
				stepBtn.setText(stepWord);
				modeOptions.setSelectedIndex(mode);
				playBtn.setEnabled(!run.isRunning());

				try {
					frameInfo.setText(run.getFrameInfo());
				} catch (Exception e) {
					// nothing
				}
			}
		});

		// listen for Zoom changes
		run.addZoomListener(new ZoomListener() {
			public void eventReceived(ZoomEvent arg0) {
				bigBtn.setEnabled(!run.tooBig());
				lilBtn.setEnabled(!run.vanillaSize());
				zoomLevel.setText("x" + run.getZoom());
			}
		});

		// listen for display changes
		run.addRebuildListener(new RebuildListener() {
			public void eventReceived(RebuildEvent arg0) {
				try {
					run.hardReset();
				} catch (Exception e) {
					// do nothing
				}
				frameMax.setText("/ " + run.maxFrame());
				equipStatus.setText(run.equipmentOn() ? "ON" : "OFF");
				shadowStatus.setText(run.shadowOn() ? "ON" : "OFF");
				neutralStatus.setText(run.neutralOn() ? "ON" : "OFF");
			}
		});

		// update GUI
		run.firePurge();

		// load sprite file
		loadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				explorer.setSelectedFile(EEE);
				int option = explorer.showOpenDialog(loadBtn);
				if (option == JFileChooser.CANCEL_OPTION) {
					return;
				}
				String n = "";

				// grab file name
				try {
					n = explorer.getSelectedFile().getPath();
				} catch (NullPointerException e) {
					// do nothing
				} finally {
					if (GUIHelpers.testFileType(n,"spr")) {
						fileName.setText(n);
					}
					else {
						return;
					}
				}

				GUIHelpers.loadSPR(n, animOptions.getSelectedIndex(), run, frame);
			}});
		
		// reload sprite file
		reloadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String curFileName = fileName.getText();
				if (curFileName.equals("") || !GUIHelpers.testFileType(curFileName,"spr")) {
					JOptionPane.showMessageDialog(frame,
							"Please load a file first",
							"Oops",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				GUIHelpers.loadSPR(curFileName, animOptions.getSelectedIndex(), run, frame);
			}});

		// animation select
		animOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					run.setAnimation(animOptions.getSelectedIndex());
				} catch(Exception e) {
					String animName = animOptions.getSelectedItem().toString();
					run.setAnimation(0);
					animOptions.setSelectedIndex(0);
					JOptionPane.showMessageDialog(frame,
							"There's a problem with the animation:\n" +
									animName,
							"OH NO",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				resetBtn.getActionListeners()[0].actionPerformed(
						new ActionEvent(resetBtn, ActionEvent.ACTION_PERFORMED,"",0,0));
			}});

		// mode select
		modeOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					run.setMode(modeOptions.getSelectedIndex());
				} catch (Exception e) {
					// do nothing
				}
			}});

		// zoom buttons
		bigBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.embiggen();
			}});

		lilBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.ensmallen();
			}});

		// speed buttons
		fasterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.faster();
			}});

		slowerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.slower();
			}});

		// play button
		playBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.setMode(0);
			}});

		// step button
		stepBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				switch (run.getMode()) {
					case 0 :
						run.pause();
						break;
					case 1 :
						run.step();
						break;
				}
			}});

		// reset button
		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					run.repaint();
					run.reset();
				} catch (Exception e) {
					// do nothing
				}
			}});

		// item toggle
		equipBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.switchEquipment();
			}});

		// shadow toggle
		shadowBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.switchShadow();
			}});

		// neutral toggle
		neutralBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.switchNeutral();
			}});

		// gear settings
		mailLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int level = mailLevel.getSelectedIndex();
				run.setMail(level);
			}});

		swordLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int level = swordLevel.getSelectedIndex();
				run.setSword(level);
			}});

		shieldLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int level = shieldLevel.getSelectedIndex();
				run.setShield(level);
			}});

		// background display
		bgDisp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int bg = bgDisp.getSelectedIndex();
				run.setBackground(bg);
			}});

		// turn on
		frame.setVisible(true);
	}

	/**
	 * See what buttons are allowed for what modes
	 */
	private static boolean btnAllowed(String n, int mode) {
		boolean allowed = false;
		n = n.toLowerCase();
		switch (n) {
			// both speed buttons
			case "speed" : {
				switch (mode) {
					case 0 :
					case 2 :
						allowed = true;
						break;
					case 1 :
						allowed = false;
						break;
				} // end mode switch
				break;
			} // end speed case
			// step button
			case "step" : {
				switch (mode) {
					case 2 :
						allowed = false;
						break;
					case 0 :
					case 1 :
						allowed = true;
						break;
				} // end mode switch
				break;
			} // end step case
			// other buttons (currently all are allowed)
			case "reset" :
			case "zoom" :
				allowed = true;
				break;
			default :
				allowed = false;
				break;
		}
		return allowed;
	}

	public static String[] getAnimNames() {
		String[] ret = new String[ALLFRAMES.length];
		for (int i = 0; i < ALLFRAMES.length; i++) {
			String r = ALLFRAMES[i];
			String[] animDataX = r.split("[\\[\\]]");
			r = animDataX[1];
			r = r.replaceAll("(Up|Down|Right|Left)$", "($1)");
			char r2[] = r.toCharArray();
			r = "";
			r += Character.toUpperCase(r2[0]);

			for (int j = 1; j < r2.length; j++) {
				char c = r2[j];
				if (Character.isUpperCase(c)) {
					if (Character.isAlphabetic(r2[j-1])) {
						r += " ";
					}
				} else if (Character.isDigit(c) || (c == '(')) {
					r += " ";
				}
				c = Character.toLowerCase(c);
				r += c;
			}
			ret[i] = r;
		}
		return ret;
	}

	/**
	 * Set min max and preferred size for a component
	 * @param c
	 * @param d
	 */
	private void setAllSizes(Component c, Dimension d) {
		c.setPreferredSize(d);
		c.setMaximumSize(d);
		c.setMinimumSize(d);
	}

	/**
	 * Tool tips are stupid omfg
	 */
	private static final String[] TOOLTIP_STYLES = {
			"width: 150px",
			"max-width: 150px",
			"padding: 2px",
	};

	/**
	 * Set a tool tip with text and warning in the preferred format
	 * @param c
	 * @param text
	 * @param warning
	 */
	private void setToolTip(JComponent c, String... text) {
		c.setToolTipText(
				"<html>" +
				"<div style=\"" + GUIHelpers.join(TOOLTIP_STYLES, ";") + "\">" +
				GUIHelpers.join(text, "<br /><br />") +
				"</div>" +
				"</html>");
		// set an underline for the component
		if (c instanceof JLabel) {
			String s =  ((JLabel) c).getText();
			((JLabel) c).setText(
					"<html>" +
					"<div style=\"border-bottom: 1px dotted;\">" +
					s +
					"</div>" +
					"</html>"
					);
		}
	}
}
