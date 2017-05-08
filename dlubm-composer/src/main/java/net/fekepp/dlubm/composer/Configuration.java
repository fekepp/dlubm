package net.fekepp.dlubm.composer;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

	// Deployment

	private String proxyNetwork;

	private Map<Level, String> images = new HashMap<Level, String>();

	private String domain;

	private boolean withDetailedSubdomain;

	private boolean withGlobalDomain;

	// DLUBM

	private String ontology;

	private int seed;

	private Level granularity;

	private int universityAmount;

	private int universityOffset;

	private int universityLimit;

	private String universityTemplate;

	private int departmentLimit;

	private String departmentTemplate;

	public Configuration() {
		proxyNetwork = "traefik_proxy";
		images.put(Level.ONTOLOGY, "fekepp/dlubm:latest");
		images.put(Level.GLOBAL, "fekepp/dlubm:latest");
		images.put(Level.UNIVERSITY, "fekepp/dlubm:latest");
		images.put(Level.DEPARTMENT, "fekepp/dlubm:latest");
		domain = "dlubm.local";
		withDetailedSubdomain = false;
		withGlobalDomain = false;
		ontology = null;
		seed = 0;
		granularity = Level.GLOBAL;
		universityAmount = 1;
		universityOffset = 0;
		universityLimit = 0;
		departmentLimit = 0;
		universityTemplate = null;
		departmentTemplate = null;
	}

	public String getProxyNetwork() {
		return proxyNetwork;
	}

	public void setProxyNetwork(String proxyNetwork) {
		this.proxyNetwork = proxyNetwork;
	}

	public Map<Level, String> getImages() {
		return images;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String hostname) {
		this.domain = hostname;
	}

	public boolean isWithDetailedSubdomain() {
		return withDetailedSubdomain;
	}

	public void setWithDetailedSubdomain(boolean withSubdomain) {
		this.withDetailedSubdomain = withSubdomain;
	}

	public boolean isWithGlobalDomain() {
		return withGlobalDomain;
	}

	public void setWithGlobalDomain(boolean withGlobalDomain) {
		this.withGlobalDomain = withGlobalDomain;
	}

	public String getOntology() {
		return ontology;
	}

	public void setOntology(String ontology) {
		this.ontology = ontology;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public Level getGranularity() {
		return granularity;
	}

	public void setGranularity(Level granularity) {
		this.granularity = granularity;
	}

	public int getUniversityAmount() {
		return universityAmount;
	}

	public void setUniversityAmount(int universityAmount) {
		this.universityAmount = universityAmount;
	}

	public int getUniversityOffset() {
		return universityOffset;
	}

	public void setUniversityOffset(int universityOffset) {
		this.universityOffset = universityOffset;
	}

	public int getUniversityLimit() {
		return universityLimit;
	}

	public void setUniversityLimit(int universityLimit) {
		this.universityLimit = universityLimit;
	}

	public String getUniversityTemplate() {
		return universityTemplate;
	}

	public void setUniversityTemplate(String universityTemplate) {
		this.universityTemplate = universityTemplate;
	}

	public int getDepartmentLimit() {
		return departmentLimit;
	}

	public void setDepartmentLimit(int departmentLimit) {
		this.departmentLimit = departmentLimit;
	}

	public String getDepartmentTemplate() {
		return departmentTemplate;
	}

	public void setDepartmentTemplate(String departmentTemplate) {
		this.departmentTemplate = departmentTemplate;
	}

}
