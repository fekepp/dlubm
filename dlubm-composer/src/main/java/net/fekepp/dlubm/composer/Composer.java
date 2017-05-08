package net.fekepp.dlubm.composer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import net.fekepp.dlubm.composer.yaml.Composition;
import net.fekepp.dlubm.composer.yaml.Deploy;
import net.fekepp.dlubm.composer.yaml.External;
import net.fekepp.dlubm.composer.yaml.Network;
import net.fekepp.dlubm.composer.yaml.Service;

public class Composer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Configuration configuration;

	public Composer() {
		this(null);
	}

	public Composer(Configuration configuration) {
		this.configuration = configuration;
	}

	public void compose() {

		logger.info("Starting the creation of a composition");

		Composition compose = new Composition();

		// Version
		compose.setVersion("3");

		// Networks
		if (compose.getNetworks() == null) {
			compose.setNetworks(new HashMap<String, Network>());
		}

		External networkProxyExternal = new External();
		networkProxyExternal.setName("traefik_proxy");

		Network networkProxy = new Network();
		networkProxy.setExternal(networkProxyExternal);

		compose.getNetworks().put("proxy", networkProxy);

		// Services
		if (compose.getServices() == null) {
			compose.setServices(new HashMap<String, Service>());
		}

		// TODO Allow generation of only a certain level
		compose.getServices().putAll(generateServices(Level.DEPARTMENT));

		// TODO Generate ontology container

		if (configuration.getGranularity().equals(Level.GLOBAL)) {

			// TODO Generate global level container

		}

		if (configuration.getGranularity().equals(Level.GLOBAL)
				|| configuration.getGranularity().equals(Level.UNIVERSITY)) {

			// TODO Generate university level container

		}

		if (configuration.getGranularity().equals(Level.GLOBAL)
				|| configuration.getGranularity().equals(Level.UNIVERSITY)
				|| configuration.getGranularity().equals(Level.DEPARTMENT)) {

			// TODO Generate department level container

		}

		Representer representer = new Representer() {
			@Override
			protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue,
					Tag customTag) {
				if (propertyValue == null) {
					return null;
				} else {
					return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
				}
			}
		};

		// representer.addClassTag(Compose.class, new Tag("!Compose"));
		representer.addClassTag(Composition.class, Tag.MAP);
		// representer.addClassTag(Service.class, Tag.MAP);

		DumperOptions options = new DumperOptions();
		options.setPrettyFlow(false);

		Yaml yaml = new Yaml(representer, options);

		// Dump & Log
		String output = yaml.dump(compose);
		System.out.println(output);

	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	private Map<String, Service> generateServices(Level depth) {

		Map<String, Service> services = new HashMap<String, Service>();

		int seed = configuration.getSeed();
		Level granularity = configuration.getGranularity();
		String granularityAcronym = configuration.getGranularity().toString().substring(0, 1).toLowerCase();
		int universityAmount = configuration.getUniversityAmount();
		int universityOffset = configuration.getUniversityOffset();
		int universityLimit = configuration.getUniversityLimit();
		int departmentLimit = configuration.getDepartmentLimit();

		logger.info("g={} | s={} | ua={} | uo={} | ul={} | dl={}", granularityAcronym, seed, universityAmount,
				universityOffset, universityLimit, departmentLimit);

		int hostCounter = 0;

		// Generate ontology service
		hostCounter++;
		logger.info("O | hc={}", hostCounter);
		Map<String, Service> ontologyService = generateService(Level.ONTOLOGY, 0, 0, 0, 0);
		services.putAll(ontologyService);

		// Generate global service
		hostCounter++;
		logger.info("G | hc={} | uha={}", hostCounter, universityAmount);
		Map<String, Service> globalService = generateService(Level.GLOBAL, universityAmount, universityOffset, 0, 0);
		services.putAll(globalService);

		// Generate university services
		if (granularity.equals(Level.DEPARTMENT) || granularity.equals(Level.UNIVERSITY)) {

			int universityHostAmount = 0;
			if (universityLimit > 0) {
				if (Math.floorMod(universityAmount, universityLimit) > 0) {
					universityHostAmount = Math.floorDiv(universityAmount, universityLimit) + 1;
				} else {
					universityHostAmount = Math.floorDiv(universityAmount, universityLimit);
				}
			}

			else {
				universityHostAmount = 1;
			}

			for (int universityHostIndex = 0; universityHostIndex < universityHostAmount; universityHostIndex++) {

				int hostUniversityOffset = universityOffset + universityHostIndex * universityLimit;
				int hostUniversityAmount = universityLimit;
				if ((universityHostIndex + 1) * universityLimit > universityAmount) {
					hostUniversityAmount = Math.floorMod(universityAmount, universityLimit);
				}

				hostCounter++;

				logger.info("U | hc={} | uha={} | uhi={} | huo={} | hua={}", hostCounter, universityHostAmount,
						universityHostIndex, hostUniversityOffset, hostUniversityAmount);

				Map<String, Service> universityService = generateService(Level.UNIVERSITY, hostUniversityAmount,
						hostUniversityOffset, 0, 0);

				services.putAll(universityService);

				// Generate department services
				if (granularity.equals(Level.DEPARTMENT)) {

					int departmentAmount = 15;
					int departmentHostAmount = 0;
					if (departmentLimit > 0) {
						if (Math.floorMod(departmentAmount, departmentLimit) > 0) {
							departmentHostAmount = Math.floorDiv(departmentAmount, departmentLimit) + 1;
						} else {
							departmentHostAmount = Math.floorDiv(departmentAmount, departmentLimit);
						}
					}

					for (int departmentHostIndex = 0; departmentHostIndex < departmentHostAmount; departmentHostIndex++) {

						int hostDepartmentOffset = departmentHostIndex * departmentLimit;
						int hostDepartmentAmount = departmentLimit;
						if ((departmentHostIndex + 1) * departmentLimit > departmentAmount) {
							hostDepartmentAmount = Math.floorMod(departmentAmount, departmentLimit);
						}

						hostCounter++;

						logger.info("D | hc={} | uha={} | uhi={} | huo={} | hua={} | hdo={} | hda={}", hostCounter,
								universityHostAmount, universityHostIndex, hostUniversityOffset, hostUniversityAmount,
								hostDepartmentOffset, hostDepartmentAmount);

						Map<String, Service> departmentService = generateService(Level.DEPARTMENT, hostUniversityAmount,
								hostUniversityOffset, hostDepartmentAmount, hostDepartmentOffset);

						services.putAll(departmentService);

					}

				}

			}

		}

		return services;

	}

	private Map<String, Service> generateService(Level hostDepth, int hostUniversityAmount, int hostUniversityOffset,
			int hostDepartmentAmount, int hostDepartmentOffset) {

		Map<Level, String> images = configuration.getImages();
		String hostname = configuration.getDomain();
		boolean withDetailedSubdomain = configuration.isWithDetailedSubdomain();
		boolean withGlobalDomain = configuration.isWithGlobalDomain();

		String ontology = configuration.getOntology();
		int seed = configuration.getSeed();
		Level granularity = configuration.getGranularity();
		String granularityAcronym = configuration.getGranularity().toString().substring(0, 1).toLowerCase();
		int universityAmount = configuration.getUniversityAmount();
		int universityOffset = configuration.getUniversityOffset();
		int universityLimit = configuration.getUniversityLimit();
		int departmentLimit = configuration.getDepartmentLimit();
		String universityTemplate = configuration.getUniversityTemplate();
		String departmentTemplate = configuration.getDepartmentTemplate();

		// Name (variable part)
		StringBuffer nameVariable = new StringBuffer();
		if (hostDepth.equals(Level.DEPARTMENT)) {
			nameVariable.append("d" + hostDepartmentOffset
					+ (hostUniversityAmount > 1 ? "-d" + (hostDepartmentOffset + hostDepartmentAmount - 1) : "") + "_");
		}

		if (hostDepth.equals(Level.DEPARTMENT) || hostDepth.equals(Level.UNIVERSITY)) {
			nameVariable.append("u" + (universityLimit > 0 ? hostUniversityOffset : "")
					+ (hostUniversityAmount > 1 ? "-u" + (hostUniversityOffset + hostUniversityAmount - 1) : ""));
		}

		if (hostDepth.equals(Level.GLOBAL)) {
			nameVariable.append("g");
		}

		if (hostDepth.equals(Level.ONTOLOGY)) {
			nameVariable.append("o");
		}

		// Name (fixed part)
		StringBuffer nameFixed = new StringBuffer();
		nameFixed.append("dl").append(departmentLimit);
		nameFixed.append("_ul").append(universityLimit);
		nameFixed.append("_uo").append(universityOffset);
		nameFixed.append("_ua").append(universityAmount);
		nameFixed.append("_g").append(granularityAcronym);
		nameFixed.append("_s").append(seed);

		// Name
		StringBuffer name = new StringBuffer();
		name.append(nameVariable);
		name.append("_");
		name.append(nameFixed);

		// Domain
		StringBuffer domain = new StringBuffer();
		if (withDetailedSubdomain) {
			domain.append(nameFixed.toString().replaceAll("_", ".")).append(".");
		}
		domain.append(hostname);

		// Host
		StringBuffer host = new StringBuffer();
		host.append(nameVariable.toString().replaceAll("_", "."));
		host.append(".").append(domain);

		// Ontology (if not overwritten)
		if (ontology == null) {
			ontology = "http://o." + domain + "/univ-bench.owl";
		}

		// Templates (if not overwritten)
		if (granularity.equals(Level.GLOBAL)) {
			if (universityTemplate == null) {
				universityTemplate = "http://g." + domain + "/u{UNIVERSITY_INDEX}#";
			}
			if (departmentTemplate == null) {
				departmentTemplate = "http://g." + domain + "/u{UNIVERSITY_INDEX}/d{DEPARTMENT_INDEX}#";
			}
		}

		else if (granularity.equals(Level.UNIVERSITY)) {
			if (universityLimit < 1) {
				if (universityTemplate == null) {
					universityTemplate = "http://u." + domain + "/u{UNIVERSITY_INDEX}#";
				}
				if (departmentTemplate == null) {
					departmentTemplate = "http://u." + domain + "/u{UNIVERSITY_INDEX}/d{DEPARTMENT_INDEX}#";
				}
			} else if (universityLimit == 1) {
				if (universityTemplate == null) {
					universityTemplate = "http://u{UNIVERSITY_INDEX}." + domain + "/u#";
				}
				if (departmentTemplate == null) {
					departmentTemplate = "http://u{UNIVERSITY_INDEX}." + domain + "/d{DEPARTMENT_INDEX}#";
				}
			} else if (universityLimit > 1) {
				if (universityTemplate == null) {
					universityTemplate = "http://u{UNIVERSITY_INDEX_OFFSET_MINIMUM}-u{UNIVERSITY_INDEX_OFFSET_MAXIMUM}."
							+ domain + "/u{UNIVERSITY_INDEX}#";
				}
				if (departmentTemplate == null) {
					departmentTemplate = "http://u{UNIVERSITY_INDEX_OFFSET_MINIMUM}-u{UNIVERSITY_INDEX_OFFSET_MAXIMUM}."
							+ domain + "/u{UNIVERSITY_INDEX}/d{DEPARTMENT_INDEX}#";
				}
			}
		}

		else if (granularity.equals(Level.DEPARTMENT)) {
			if (universityLimit < 1) {
				if (departmentLimit < 1) {
					if (universityTemplate == null) {
						universityTemplate = "http://u." + domain + "/u{UNIVERSITY_INDEX}#";
					}
					if (departmentTemplate == null) {
						departmentTemplate = "http://d." + domain + "/u{UNIVERSITY_INDEX}/d{DEPARTMENT_INDEX}#";
					}
				} else if (departmentLimit == 1) {
					// TODO Add templates aligned with DLUBM docker image
				} else if (departmentLimit > 1) {
					// TODO Add templates aligned with DLUBM docker image
				}
			} else if (universityLimit == 1) {
				if (departmentLimit < 1) {
					// TODO Add templates aligned with DLUBM docker image
				} else if (departmentLimit == 1) {
					if (universityTemplate == null) {
						universityTemplate = "http://u{UNIVERSITY_INDEX}." + domain + "/u#";
					}
					if (departmentTemplate == null) {
						departmentTemplate = "http://d{DEPARTMENT_INDEX}.u{UNIVERSITY_INDEX}." + domain + "/d#";
					}
				} else if (departmentLimit > 1) {
					// TODO Add templates aligned with DLUBM docker image
				}
			} else if (universityLimit > 1) {
				if (departmentLimit < 1) {
					// TODO Add templates aligned with DLUBM docker image
				} else if (departmentLimit == 1) {
					// TODO Add templates aligned with DLUBM docker image
				} else if (departmentLimit > 1) {
					if (universityTemplate == null) {
						universityTemplate = "http://u{UNIVERSITY_INDEX_OFFSET_MINIMUM}-u{UNIVERSITY_INDEX_OFFSET_MAXIMUM}."
								+ domain + "/u{UNIVERSITY_INDEX}#";
					}
					if (departmentTemplate == null) {
						departmentTemplate = "http://d{DEPARTMENT_INDEX_OFFSET_MINIMUM}u{DEPARTMENT_UNIVERSITY_INDEX_OFFSET_MINIMUM}-d{DEPARTMENT_INDEX_OFFSET_MAXIMUM}u{DEPARTMENT_UNIVERSITY_INDEX_OFFSET_MAXIMUM}.u{UNIVERSITY_INDEX_OFFSET_MINIMUM}-u{UNIVERSITY_INDEX_OFFSET_MAXIMUM}."
								+ domain + "/u{UNIVERSITY_INDEX}/d{DEPARTMENT_INDEX}#";
					}
				}

			}
		}

		// Service
		Service service = new Service();

		// Image
		switch (hostDepth) {
		case ONTOLOGY:
			service.setImage(images.get(hostDepth));
			break;
		case GLOBAL:
			service.setImage(images.get(hostDepth));
			break;
		case UNIVERSITY:
			service.setImage(images.get(hostDepth));
			break;
		case DEPARTMENT:
			service.setImage(images.get(hostDepth));
			break;
		}

		// Environment
		List<String> serviceEnvironment = new LinkedList<String>();

		serviceEnvironment.add("DLUBM_ONTOLOGY=" + ontology);
		serviceEnvironment.add("DLUBM_SEED=" + seed);
		serviceEnvironment.add("DLUBM_GRANULARITY=" + granularity);
		serviceEnvironment.add("DLUBM_UNIVERSITY_AMOUNT=" + universityAmount);
		serviceEnvironment.add("DLUBM_UNIVERSITY_OFFSET=" + universityOffset);
		serviceEnvironment.add("DLUBM_UNIVERSITY_LIMIT=" + universityLimit);
		serviceEnvironment.add("DLUBM_UNIVERSITY_TEMPLATE=" + universityTemplate);
		serviceEnvironment.add("DLUBM_DEPARTMENT_LIMIT=" + departmentLimit);
		serviceEnvironment.add("DLUBM_DEPARTMENT_TEMPLATE=" + departmentTemplate);

		serviceEnvironment.add("DLUBM_HOST_DEPTH=" + hostDepth);
		serviceEnvironment.add("DLUBM_HOST_UNIVERSITY_AMOUNT=" + hostUniversityAmount);
		serviceEnvironment.add("DLUBM_HOST_UNIVERSITY_OFFSET=" + hostUniversityOffset);
		serviceEnvironment.add("DLUBM_HOST_DEPARTMENT_AMOUNT=" + hostDepartmentAmount);
		serviceEnvironment.add("DLUBM_HOST_DEPARTMENT_OFFSET=" + hostDepartmentOffset);

		service.setEnvironment(serviceEnvironment);

		// Networks
		List<String> serviceNetworks = new LinkedList<String>();
		serviceNetworks.add("proxy");
		service.setNetworks(serviceNetworks);

		// Labels
		String[] labels = new String[] { "traefik.docker.network=traefik_proxy", "traefik.port=80",
				"traefik.frontend.rule=Host:" + host
						+ (hostDepth.equals(Level.GLOBAL) && withGlobalDomain ? "," + hostname : "") };
		service.setLabels(labels);

		// Deploy
		Deploy deploy = new Deploy();
		service.setDeploy(deploy);
		deploy.setLabels(labels.clone());

		// Create a map with name and service
		HashMap<String, Service> serviceMap = new HashMap<String, Service>();
		serviceMap.put(name.toString(), service);

		// Return the service map
		return serviceMap;

	}

}
