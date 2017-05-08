package net.fekepp.dlubm.composer.yaml;

import java.util.List;

public class Service {

	private Deploy deploy;

	private List<String> environment;

	private String image;

	private String[] labels;

	private List<String> networks;

	private List<String> ports;

	public Deploy getDeploy() {
		return deploy;
	}

	public void setDeploy(Deploy deploy) {
		this.deploy = deploy;
	}

	public List<String> getEnvironment() {
		return environment;
	}

	public void setEnvironment(List<String> environment) {
		this.environment = environment;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String[] getLabels() {
		return labels;
	}

	public void setLabels(String[] labels) {
		this.labels = labels;
	}

	public List<String> getNetworks() {
		return networks;
	}

	public void setNetworks(List<String> networks) {
		this.networks = networks;
	}

	public List<String> getPorts() {
		return ports;
	}

	public void setPorts(List<String> ports) {
		this.ports = ports;
	}

}
