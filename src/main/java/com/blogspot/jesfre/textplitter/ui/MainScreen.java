/**
 * 
 */
package com.blogspot.jesfre.textplitter.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang.StringUtils;

import com.blogspot.jesfre.textplitter.TextSplitter;

/**
 * @author <a href="jorge.ruiz.aquino@gmail.com">Jorge Ruiz Aquino</a>
 *         02/08/2014
 */
public class MainScreen extends JPanel implements ActionListener {
	public static final Integer MAX_DIGITS = 5;
	public static final String[] ACCEPTED_FILE_EXTENSIONS = { "txt", "csv" };
	private static final String LARGE_FILE_COMMAND = "Open file...";
	private static final String TARGET_FOLDER_COMMAND = "Target folder...";
	private static final String SAVE_COMMAND = "Save files";

	private JTextArea taLog = null;

	private JFileChooser fcLargeFile = null;
	private JButton btnChooseLargeFile = null;
	private JLabel lblChooseLargeFile = null;

	private JFileChooser fcTargetFolder = null;
	private JButton btnChooseTargetFolder = null;
	private JLabel lblChooseTargetFolder = null;

	private JTextField txtLinesPerFile = null;
	private int linesPerFile = 0;
	private JButton btnSave = null;

	public MainScreen() {
		taLog = new JTextArea(TextSplitter.debug("Start."), 5, 20);
		taLog.setMargin(new Insets(5, 5, 5, 5));
		taLog.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(taLog);

		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				Arrays.toString(ACCEPTED_FILE_EXTENSIONS),
				ACCEPTED_FILE_EXTENSIONS);
		fcLargeFile = new JFileChooser();
		fcLargeFile.addChoosableFileFilter(filter);
		fcLargeFile.setAcceptAllFileFilterUsed(false);
		fcLargeFile.setCurrentDirectory(new java.io.File("."));

		btnChooseLargeFile = new JButton(LARGE_FILE_COMMAND);
		btnChooseLargeFile.addActionListener(this);
		lblChooseLargeFile = new JLabel();
		lblChooseLargeFile.setHorizontalAlignment(SwingConstants.LEFT);

		fcTargetFolder = new JFileChooser();
		fcTargetFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fcTargetFolder.setCurrentDirectory(new java.io.File("."));

		btnChooseTargetFolder = new JButton(TARGET_FOLDER_COMMAND);
		btnChooseTargetFolder.addActionListener(this);
		lblChooseTargetFolder = new JLabel();
		lblChooseTargetFolder.setHorizontalAlignment(SwingConstants.LEFT);

		txtLinesPerFile = new JTextField() {
			public void processKeyEvent(KeyEvent kevent) {
				char character = kevent.getKeyChar();
				try {
					if (character > 31 && character < 127) {
						if (super.getText().length() >= MAX_DIGITS) {
							return;
						}
						Integer.parseInt(character + "");
					}
					super.processKeyEvent(kevent);
				} catch (NumberFormatException nfe) {
				}
			}
		};
		JLabel lblLinesPerFile = new JLabel("Set the numer of lines per file: ");
		lblLinesPerFile.setHorizontalAlignment(SwingConstants.RIGHT);

		btnSave = new JButton(SAVE_COMMAND);
		btnSave.addActionListener(this);

		JPanel fieldsContainer = new JPanel();
		GridLayout fieldLayout = new GridLayout(0, 2);
		fieldLayout.setHgap(10);
		fieldLayout.setVgap(10);
		fieldsContainer.setLayout(fieldLayout);
		fieldsContainer.setPreferredSize(new Dimension(500, 150));

		fieldsContainer.add(btnChooseLargeFile);
		fieldsContainer.add(lblChooseLargeFile);
		fieldsContainer.add(btnChooseTargetFolder);
		fieldsContainer.add(lblChooseTargetFolder);
		fieldsContainer.add(lblLinesPerFile);
		fieldsContainer.add(txtLinesPerFile);
		fieldsContainer.add(new JLabel(""));
		fieldsContainer.add(btnSave);

		GridLayout layoutPanel = new GridLayout(0, 1);
		this.setLayout(layoutPanel);
		this.add(fieldsContainer);
		this.add(taLog);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(LARGE_FILE_COMMAND)) {

			// Open window selection of the large file
			int returnVal = fcLargeFile.showDialog(this, "Open");
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fcLargeFile.getSelectedFile();
				lblChooseLargeFile.setText(file.getAbsolutePath());
			}
			fcLargeFile.setSelectedFile(null);

		} else if (e.getActionCommand().equals(TARGET_FOLDER_COMMAND)) {

			// Open window selection of the target folder for the part files
			int returnVal = fcTargetFolder.showDialog(this, "Open");
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fcTargetFolder.getSelectedFile();
				lblChooseTargetFolder.setText(file.getAbsolutePath());
			}
			fcTargetFolder.setSelectedFile(null);

		} else if (e.getActionCommand().equals(SAVE_COMMAND)) {
			System.out.println(1);
			String filePath = lblChooseLargeFile.getText();
			System.out.println(2);
			TextSplitter.debug("Processing file [" + filePath + "]");
			System.out.println(3);
			String targetFolder = lblChooseTargetFolder.getText();
			System.out.println(4);
			String linesPerFileString = txtLinesPerFile.getText();
			System.out.println(5);
			if (StringUtils.isNotBlank(filePath)
					&& StringUtils.isNotBlank(targetFolder)
					&& StringUtils.isNotBlank(linesPerFileString)) {
				System.out.println(6);
				linesPerFile = Integer.valueOf(linesPerFileString);
				System.out.println(7);
				try {
					TextSplitter
							.splitFile(filePath, targetFolder, linesPerFile);
					System.out.println(8888);
				} catch (IOException e1) {
					System.out.println(9);
					e1.printStackTrace();
					TextSplitter.debug(e1.getMessage());
				}
				System.out.println(10);
			} else {
				System.out.println(11);
				TextSplitter.debug("Some required fields are missing.");
			}
			System.out.println(12);
			taLog.setText(TextSplitter.debug(""));
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("File Splitter");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(new MainScreen());
		frame.pack();
		frame.setVisible(true);
	}
}
