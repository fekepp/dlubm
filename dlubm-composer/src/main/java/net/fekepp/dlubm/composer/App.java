package net.fekepp.dlubm.composer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class App {

	public static final String APP_NAME = "scal-server";

	public static final Option OPTION_PORT = Option.builder("p").longOpt("port").desc("port to be used by the server")
			.hasArg().type(Number.class).required().build();

	public static final Option OPTION_STORAGE_DIRECTORY = Option.builder("s").longOpt("storage-directory")
			.desc("storage directory to be used by the server").hasArg().type(String.class).required().build();

	public static final Option OPTION_TEMPORARY_STORAGE_DIRECTORY = Option.builder("t")
			.longOpt("temporary-storage-directory").desc("temporary storage directory to be used by the server")
			.hasArg().type(String.class).required().build();

	public static final Options getOptions() {
		Options options = new Options();
		// options.addOption(OPTION_PORT);
		// options.addOption(OPTION_STORAGE_DIRECTORY);
		// options.addOption(OPTION_TEMPORARY_STORAGE_DIRECTORY);
		return options;
	}

	public static void main(String[] args) {

		// Remove existing handlers attached to j.u.l root logger (optional)
		SLF4JBridgeHandler.removeHandlersForRootLogger();

		// Add SLF4JBridgeHandler to j.u.l's root logger
		SLF4JBridgeHandler.install();

		// Create a default command line parser
		CommandLineParser parser = new DefaultParser();

		// Create a command line to be parsed
		CommandLine commandLine = null;

		// Create a help formatter used in case of trouble
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.setLeftPadding(4);

		// Try to parse the options and start the controller
		try {

			// Parse the command line options
			commandLine = parser.parse(getOptions(), args);

			Configuration configuration = new Configuration();

			configuration.getImages().put(Level.ONTOLOGY, "fekepp/dlubm:1.9.0-pr");
			configuration.getImages().put(Level.GLOBAL, "fekepp/dlubm:1.9.0-pr");
			configuration.getImages().put(Level.UNIVERSITY, "fekepp/dlubm:1.9.0-pr");
			// configuration.getImages().put(Level.UNIVERSITY,
			// "fekepp/dlubm-scal:latest");
			configuration.getImages().put(Level.DEPARTMENT, "fekepp/dlubm:1.9.0-pr");
			configuration.setDomain("dlubm.ddns.me");
			configuration.setWithDetailedSubdomain(false);
			configuration.setWithGlobalDomain(true);

			configuration.setOntology(null);
			configuration.setSeed(0);
			configuration.setGranularity(Level.DEPARTMENT);
			configuration.setUniversityOffset(0);
			configuration.setUniversityAmount(5);
			configuration.setUniversityLimit(1);
			configuration.setDepartmentLimit(1);
			configuration.setUniversityTemplate(null);
			configuration.setDepartmentTemplate(null);

			Composer composer = new Composer(configuration);
			composer.compose();

			// // Create a controller
			// ServerController controller = new ServerController();
			//
			// // Configure port
			// Object portOptionParsedValue =
			// commandLine.getParsedOptionValue(OPTION_PORT.getOpt());
			// if (portOptionParsedValue instanceof Long && (Long)
			// portOptionParsedValue > 0
			// && (Long) portOptionParsedValue <= 65535) {
			// controller.setPort(((Long) portOptionParsedValue).intValue());
			// } else {
			// throw new ParseException("Bad input string: " +
			// portOptionParsedValue);
			// }
			//
			// // Configure storage directory
			// Object storageDirectoryOptionParsedValue = commandLine
			// .getParsedOptionValue(OPTION_STORAGE_DIRECTORY.getOpt());
			// if (storageDirectoryOptionParsedValue instanceof String
			// && ((String) storageDirectoryOptionParsedValue).length() > 0) {
			// controller.setStorageDirectory(Paths.get((String)
			// storageDirectoryOptionParsedValue));
			// } else {
			// throw new ParseException("Bad input string: " +
			// storageDirectoryOptionParsedValue);
			// }
			//
			// // Configure temporary storage directory
			// Object temporaryStorageDirectoryOptionParsedValue = commandLine
			// .getParsedOptionValue(OPTION_TEMPORARY_STORAGE_DIRECTORY.getOpt());
			// if (temporaryStorageDirectoryOptionParsedValue instanceof String
			// && ((String) temporaryStorageDirectoryOptionParsedValue).length()
			// > 0) {
			// controller.setStorageDirectoryTemporary(Paths.get((String)
			// temporaryStorageDirectoryOptionParsedValue));
			// } else {
			// throw new ParseException("Bad input string: " +
			// temporaryStorageDirectoryOptionParsedValue);
			// }
			//
			// // Start the controller
			// controller.startBlocking();

		}

		// Handle parse exceptions thrown in case of wrong options
		catch (ParseException e) {

			// Print the failure message generated during parsing
			System.out.println(e.getMessage().replace("For input string", "Bad input string") + "\n");

			// Print helpful information about options
			helpFormatter.printHelp(APP_NAME, getOptions());

			// Return with exit code != 0 to indicate that something went wrong
			System.exit(1);

		}

	}

}