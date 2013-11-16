package com.sinius15.suite.launcher.gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import com.sinius15.suite.launcher.io.Downloader;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DownlaodFrame extends JFrame {

	private static final long serialVersionUID = -4800316003710327194L;
	private JPanel contentPane;
	public JProgressBar progressBar;

	public DownlaodFrame() {
		setTitle("Downlaoder");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 285, 116);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Downloader.cancleTasks();
			}
		});
		btnCancel.setBounds(184, 61, 89, 23);
		contentPane.add(btnCancel);
		
		JLabel lblDownloadingRequiredFiles = new JLabel("Downloading required files...");
		lblDownloadingRequiredFiles.setBounds(10, 11, 287, 14);
		contentPane.add(lblDownloadingRequiredFiles);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(10, 36, 263, 14);
		contentPane.add(progressBar);
	}
}
