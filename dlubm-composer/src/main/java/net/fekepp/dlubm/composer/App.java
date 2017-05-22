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

	public static final String APP_NAME = "dlubm-composer";

	public static final Option OPTION_IMAGE_ONTOLOGY = Option.builder("io").longOpt("image-ontology")
			.desc("Image for ongtology").hasArg().type(String.class).required(false).build();

	public static final Option OPTION_IMAGE_GLOBAL = Option.builder("ig").longOpt("image-global")
			.desc("Image for global level").hasArg().type(String.class).required(false).build();

	public static final Option OPTION_IMAGE_UNIVERSITY = Option.builder("iu").longOpt("image-university")
			.desc("Image for university level").hasArg().type(String.class).required(false).build();

	public static final Option OPTION_IMAGE_DEPARTMENT = Option.builder("id").longOpt("image-department")
			.desc("Image for department level").hasArg().type(String.class).required(false).build();

	public static final Option OPTION_NETWORK = Option.builder("n").longOpt("network").desc("Reverse proxy network")
			.hasArg().type(String.class).required().build();

	public static final Option OPTION_DOMAIN = Option.builder("d").longOpt("domain").desc("Reverse proxy domain")
			.hasArg().type(String.class).required().build();

	public static final Option OPTION_DOMAIN_GLOBAL = Option.builder("dg").longOpt("domain-global")
			.desc("Enable global domain").hasArg().type(Boolean.class).required(false).build();

	public static final Option OPTION_DOMAIN_DETAILED = Option.builder("dd").longOpt("domain-detailed")
			.desc("Enable detailed subdomains").hasArg().type(Boolean.class).required(false).build();

	public static final Option OPTION_GRANULARITY = Option.builder("g").longOpt("granularity")
			.desc("Granularity:\nGLOBAL, UNIVERSITY, DEPARTMENT").hasArg().type(String.class).required(false).build();

	public static final Option OPTION_SEED = Option.builder("s").longOpt("seed").desc("Seed").hasArg()
			.type(Number.class).required(false).build();

	public static final Option OPTION_UNIVERSITY_OFFSET = Option.builder("uo").longOpt("university-offset")
			.desc("University offset").hasArg().type(Number.class).required(false).build();

	public static final Option OPTION_UNIVERSITY_AMOUNT = Option.builder("ua").longOpt("university-amount")
			.desc("University amount").hasArg().type(Number.class).required(false).build();

	public static final Option OPTION_UNIVERSITY_LIMIT = Option.builder("ul").longOpt("university-limit")
			.desc("University limit").hasArg().type(Number.class).required(false).build();

	public static final Option OPTION_DEPARTMENT_LIMIT = Option.builder("dl").longOpt("department-limit")
			.desc("Department limit").hasArg().type(Number.class).required(false).build();

	public static final Option OPTION_UNIVERSITY_TEMPLATE = Option.builder("ut").longOpt("university-template")
			.desc("URI template for universities").hasArg().type(String.class).required(false).build();

	public static final Option OPTION_DEPARTMENT_TEMPLATE = Option.builder("dt").longOpt("department-template")
			.desc("URI template for departments").hasArg().type(String.class).required(false).build();

	public static final Option OPTION_ONTOLOGY = Option.builder("o").longOpt("ontology").desc("URI for the ontology")
			.hasArg().type(String.class).required(false).build();

	public static final Options getOptions() {
		Options options = new Options();
		options.addOption(OPTION_IMAGE_ONTOLOGY);
		options.addOption(OPTION_IMAGE_GLOBAL);
		options.addOption(OPTION_IMAGE_UNIVERSITY);
		options.addOption(OPTION_IMAGE_DEPARTMENT);
		options.addOption(OPTION_NETWORK);
		options.addOption(OPTION_DOMAIN);
		options.addOption(OPTION_DOMAIN_GLOBAL);
		options.addOption(OPTION_DOMAIN_DETAILED);
		options.addOption(OPTION_GRANULARITY);
		options.addOption(OPTION_SEED);
		options.addOption(OPTION_UNIVERSITY_OFFSET);
		options.addOption(OPTION_UNIVERSITY_AMOUNT);
		options.addOption(OPTION_UNIVERSITY_LIMIT);
		options.addOption(OPTION_DEPARTMENT_LIMIT);
		options.addOption(OPTION_UNIVERSITY_TEMPLATE);
		options.addOption(OPTION_DEPARTMENT_TEMPLATE);
		options.addOption(OPTION_ONTOLOGY);
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

		try {

			commandLine = parser.parse(getOptions(), args);

			Configuration configuration = new Configuration();

			Option option = null;
			Object value = null;

			option = OPTION_IMAGE_ONTOLOGY;
			value = commandLine.getParsedOptionValue(option.getOpt());
			if (value != null) {
				if (value instanceof String && ((String) value).length() > 0) {
					configuration.getImages().put(Level.ONTOLOGY, (String) value);
				} else {
					throw new ParseException("Bad input for option \"-" + option.getOpt() + ", --" + option.getLongOpt()
							+ "\": " + value);
				}
			}

			option = OPTION_IMAGE_GLOBAL;
			value = commandLine.getParsedOptionValue(option.getOpt());
			if (value != null) {
				if (value instanceof String && ((String) value).length() > 0) {
					configuration.getImages().put(Level.GLOBAL, (String) value);
				} else {
					throw new ParseException("Bad input for option \"-" + option.getOpt() + ", --" + option.getLongOpt()
							+ "\": " + value);
				}
			}

			option = OPTION_IMAGE_UNIVERSITY;
			value = commandLine.getParsedOptionValue(option.getOpt());
			if (value != null) {
				if (value instanceof String && ((String) value).length() > 0) {
					configuration.getImages().put(Level.UNIVERSITY, (String) value);
				} else {
					throw new ParseException("Bad input for option \"-" + option.getOpt() + ", --" + option.getLongOpt()
							+ "\": " + value);
				}
			}

			option = OPTION_IMAGE_DEPARTMENT;
			value = commandLine.getParsedOptionValue(option.getOpt());
			if (value != null) {
				if (value instanceof String && ((String) value).length() > 0) {
					configuration.getImages().put(Level.DEPARTMENT, (String) value);
				} else {
					throw new ParseException("Bad input for option \"-" + option.getOpt() + ", --" + option.getLongOpt()
							+ "\": " + value);
				}
			}

			option = OPTION_NETWORK;
			value = commandLine.getParsedOptionValue(option.getOpt());
			if (value != null) {
				if (value instanceof String && ((String) value).length() > 0) {
					configuration.setProxyNetwork((String) value);
				} else {
					throw new ParseException("Bad input for option \"-" + option.getOpt() + ", --" + option.getLongOpt()
							+ "\": " + value);
				}
			}

			option = OPTION_DOMAIN;
			value = commandLine.getParsedOptionValue(option.getOpt());
			if (value != null) {
				if (value instanceof String && ((String) value).length() > 0) {
					configuration.setDomain((String) value);
				} else {
					throw new ParseException("Bad input for option \"-" + option.getOpt() + ", --" + option.getLongOpt()
							+ "\": " + value);
				}
			}

			option = OPTION_DOMAIN_GLOBAL;
			value = commandLine.getParsedOptionValue(option.getOpt());
			if (value != null) {
				if (value instanceof Boolean) {
					configuration.setWithGlobalDomain((Boolean) value);
				} else {
					throw new ParseException("Bad input for option \"-" + option.getOpt() + ", --" + option.getLongOpt()
							+ "\": " + value);
				}
			}

			option = OPTION_DOMAIN_DETAILED;
			value = commandLine.getParsedOptionValue(option.getOpt());
			if (value != null) {
				if (value instanceof Boolean) {
					configuration.setWithDetailedSubdomain((Boolean) value);
				} else {
					throw new ParseException("Bad input for option \"-" + option.getOpt() + ", --" + option.getLongOpt()
							+ "\": " + value);
				}
			}

			option = OPTION_GRANULARITY;
			value = commandLine.getParsedOptionValue(option.getOpt());
			if (value != null) {
				if (value instanceof String && ((String) value).length() > 0 && Level.valueOf((String) value) != null) {
					configuration.setGranularity(Level.valueOf((String) value));
				} else {
					throw new ParseException("Bad input for option \"-" + option.getOpt() + ", --" + option.getLongOpt()
							+ "\": " + value);
				}
			}

			option = OPTION_SEED;
			value = commandLine.getParsedOptionValue(option.getOpt());
			if (value != null) {
				if (value instanceof Long && (Long) value >= 0 && (Long) value <= 65535) {
					configuration.setSeed(((Long) value).intValue());
				} else {
					throw new ParseException("Bad input for option \"-" + option.getOpt() + ", --" + option.getLongOpt()
							+ "\": " + value);
				}
			}

			option = OPTION_UNIVERSITY_OFFSET;
			value = commandLine.getParsedOptionValue(option.getOpt());
			if (value != null) {
				if (value instanceof Long && (Long) value >= 0 && (Long) value <= 65535) {
					configuration.setUniversityOffset(((Long) value).intValue());
				} else {
					throw new ParseException("Bad input for option \"-" + option.getOpt() + ", --" + option.getLongOpt()
							+ "\": " + value);
				}
			}

			option = OPTION_UNIVERSITY_AMOUNT;
			value = commandLine.getParsedOptionValue(option.getOpt());
			if (value != null) {
				if (value instanceof Long && (Long) value > 0 && (Long) value <= 65535) {
					configuration.setUniversityAmount(((Long) value).intValue());
				} else {
					throw new ParseException("Bad input for option \"-" + option.getOpt() + ", --" + option.getLongOpt()
							+ "\": " + value);
				}
			}

			option = OPTION_UNIVERSITY_LIMIT;
			value = commandLine.getParsedOptionValue(option.getOpt());
			if (value != null) {
				if (value instanceof Long && (Long) value > 0 && (Long) value <= 65535) {
					configuration.setUniversityLimit(((Long) value).intValue());
				} else {
					throw new ParseException("Bad input for option \"-" + option.getOpt() + ", --" + option.getLongOpt()
							+ "\": " + value);
				}
			}

			option = OPTION_DEPARTMENT_LIMIT;
			value = commandLine.getParsedOptionValue(option.getOpt());
			if (value != null) {
				if (value instanceof Long && (Long) value > 0 && (Long) value <= 65535) {
					configuration.setDepartmentLimit(((Long) value).intValue());
				} else {
					throw new ParseException("Bad input for option \"-" + option.getOpt() + ", --" + option.getLongOpt()
							+ "\": " + value);
				}
			}

			option = OPTION_UNIVERSITY_TEMPLATE;
			value = commandLine.getParsedOptionValue(option.getOpt());
			if (value != null) {
				if (value instanceof String && ((String) value).length() > 0) {
					configuration.setUniversityTemplate((String) value);
				} else {
					throw new ParseException("Bad input for option \"-" + option.getOpt() + ", --" + option.getLongOpt()
							+ "\": " + value);
				}
			}

			option = OPTION_DEPARTMENT_TEMPLATE;
			value = commandLine.getParsedOptionValue(option.getOpt());
			if (value != null) {
				if (value instanceof String && ((String) value).length() > 0) {
					configuration.setDepartmentTemplate((String) value);
				} else {
					throw new ParseException("Bad input for option \"-" + option.getOpt() + ", --" + option.getLongOpt()
							+ "\": " + value);
				}
			}

			option = OPTION_ONTOLOGY;
			value = commandLine.getParsedOptionValue(option.getOpt());
			if (value != null) {
				if (value instanceof String && ((String) value).length() > 0) {
					configuration.setOntology((String) value);
				} else {
					throw new ParseException("Bad input for option \"-" + option.getOpt() + ", --" + option.getLongOpt()
							+ "\": " + value);
				}
			}

			Composer composer = new Composer(configuration);
			composer.compose();

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