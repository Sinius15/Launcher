package com.sinius15.suite.launcher.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.sinius15.suite.launcher.Data;
import com.sinius15.suite.launcher.GameLauncher;
import com.sinius15.suite.launcher.Launcher;
import com.sinius15.suite.launcher.MessageConsole;
import com.sinius15.suite.launcher.OptionManager;
import com.sinius15.suite.launcher.io.LoggingInner;

public class LauncherFrame extends JFrame {

	private static final long serialVersionUID = -9026214061969231008L;
	private JPanel contentPane;
	public JTextField txtUsername;
	public JPasswordField passwordField;
	public boolean passwordFieldHadFocus = false;
	public boolean textFieldHadFocus = false;
	public JButton btnOptions, btnPlayOffline, btnPlay;
	public JComboBox<String> gameBox;
	public JComboBox<String> versionCombo;
	public JCheckBox autoUpdate;
	public String latestGame = "none";

	public LauncherFrame() {
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Launcher.saveGame((String) gameBox.getSelectedItem());
			}
		});
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e2) {
			e2.printStackTrace();
		}
		setTitle("Sinius's Launcher");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 683, 451);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnPlay = new JButton("Play");
		btnPlay.setFont(new Font("Arial", Font.PLAIN, 14));
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					OptionManager.saveOptions(Data.CONFIG_FILE);
					Launcher.saveGame((String) gameBox.getSelectedItem());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if(LoggingInner.logIn(txtUsername.getText(), new String(passwordField.getPassword()), Data.launcherFrame))
						GameLauncher.LaunchGame(Launcher.getSelectedGame(), true);
			}
		});
		btnPlay.setBounds(581, 362, 89, 28);
		contentPane.add(btnPlay);
		
		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Arial", Font.PLAIN, 12));
		txtUsername.setText("Username");
		txtUsername.setBounds(425, 362, 146, 20);
		txtUsername.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if(textFieldHadFocus)
					return;
				txtUsername.setText("");
				textFieldHadFocus = true;
			}
		});
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		passwordField = new JPasswordField("Password");
		passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
		passwordField.setEchoChar((char)0);
		passwordField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if(passwordFieldHadFocus)
					return;
				passwordField.setEchoChar((char)9679);
				passwordField.setText("");
				passwordFieldHadFocus = true;
			}
		});
		passwordField.setBounds(425, 392, 146, 20);
		contentPane.add(passwordField);
		
		btnOptions = new JButton("Options");
		btnOptions.setFont(new Font("Arial", Font.PLAIN, 14));
		btnOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Data.optionFrame.setVisible(true);
				OptionManager.updateValuesToFrame();
				Data.launcherFrame.setEnabled(false);
			}
		});
		btnOptions.setBounds(10, 361, 109, 54);
		contentPane.add(btnOptions);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 11, 660, 339);
		contentPane.add(scrollPane);
		
		JTextPane console = new JTextPane();
		console.setFont(new Font("Arial", Font.PLAIN, 12));
		console.setPreferredSize(new Dimension(100000, 1000));
		scrollPane.setViewportView(console);
		
		MessageConsole mc = new MessageConsole(console);
		
		btnPlayOffline = new JButton("Play \r\nOffline");
		btnPlayOffline.setFont(new Font("Arial", Font.PLAIN, 11));
		btnPlayOffline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					OptionManager.saveOptions(Data.CONFIG_FILE);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				GameLauncher.LaunchGame(Launcher.getSelectedGame(), false);
			}
		});
		btnPlayOffline.setBounds(581, 392, 89, 20);
		contentPane.add(btnPlayOffline);
		
		JLabel lblGame = new JLabel(" Game:");
		lblGame.setFont(new Font("Arial", Font.PLAIN, 12));
		lblGame.setBounds(125, 362, 56, 20);
		contentPane.add(lblGame);
		
		
		gameBox = new JComboBox<String>();
		gameBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(latestGame.equals(gameBox.getSelectedItem()))
					return;
				latestGame = (String) gameBox.getSelectedItem();
				Launcher.updateSelectedGame();
				Launcher.saveGame(latestGame);
			}
		});
		gameBox.setBounds(253, 366, 151, 20);
		gameBox.setFont(new Font("Arial", Font.PLAIN, 13));
		contentPane.add(gameBox);
		
		versionCombo = new JComboBox<String>();
		versionCombo.setFont(new Font("Arial", Font.PLAIN, 12));
		versionCombo.setModel(new DefaultComboBoxModel<String>(new String[] {"no internet connection"}));
		versionCombo.setBounds(253, 393, 151, 20);
		contentPane.add(versionCombo);
		
		autoUpdate = new JCheckBox("Use latest version");
		autoUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				versionCombo.setEnabled(!autoUpdate.isSelected());
			}
		});
		autoUpdate.setFont(new Font("Arial", Font.PLAIN, 12));
		autoUpdate.setBounds(125, 391, 125, 24);
		contentPane.add(autoUpdate);
		mc.redirectOut();
		mc.redirectErr(Color.RED, null);
		setLocationRelativeTo(null);
	}
}
