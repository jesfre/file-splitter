package com.blogspot.jesfre.textplitter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.LineIterator;

/**
 * @author <a href="jorge.ruiz@citi.com.mx">Jorge Ruiz Aquino</a> 07/11/2013
 */
public class TextSplitter {
	private static StringBuilder log = new StringBuilder();

	public static String debug(String entry) {
		log.append(entry);
		log.append("\n");
		System.out.println("Current LOG is... \n" + log.toString());
		return log.toString();
	}

	public static void main(String[] args) {
		String fileLocation = "D:/dev/projects/PagosMovilesComercios/merchant_db_data_backup-2013-11-01.sql";
		String outputFolder = "D:/dev/projects/PagosMovilesComercios/merchant_db_data_backup/";
		int createdFiles = 0;
		try {
			createdFiles = splitFile(fileLocation, outputFolder, 50000);
		} catch (IOException e) {
			e.printStackTrace();
			debug("Something goes wrong...");
		}
		debug(createdFiles + " created files.");
	}

	/**
	 * Split file.
	 * 
	 * @return the number of generated files
	 * @throws IOException
	 */
	public static int splitFile(String fileLocation, String outputFolder,
			int linesPerFile) throws IOException {
		File fileToRead = new File(fileLocation);
		String filename = FilenameUtils.getBaseName(fileLocation);
		String extension = FilenameUtils.getExtension(fileLocation);

		int fileCounter = 0;
		int counter = 1;
		List<String> newLines = new ArrayList<String>();

		LineIterator it = FileUtils.lineIterator(fileToRead);
		debug("Reading file...");
		try {
			while (it.hasNext()) {
				String line = it.nextLine();
				newLines.add(line);
				counter++;
				if (counter == linesPerFile
						|| (counter < linesPerFile && !it.hasNext())) {
					File outputFolderFile = new File(outputFolder);
					outputFolderFile.mkdirs();

					String newFileLocation = outputFolder + "/" + filename
							+ "-" + (++fileCounter) + "." + extension;
					File outputFile = new File(newFileLocation);
					FileUtils.writeLines(outputFile, newLines);
					debug("Created " + newFileLocation);

					newLines.clear();
					counter = 1;
				}
			}
		} finally {
			LineIterator.closeQuietly(it);
		}

		debug(fileCounter + " files created.");
		return fileCounter;
	}
}
