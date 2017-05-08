package net.fekepp.dlubm.composer.yaml;

import java.util.Map;

public class Composition {

	private Map<String, Network> networks;

	private Map<String, Service> services;

	private String version;

	public Map<String, Network> getNetworks() {
		return networks;
	}

	public void setNetworks(Map<String, Network> networks) {
		this.networks = networks;
	}

	public Map<String, Service> getServices() {
		return services;
	}

	public void setServices(Map<String, Service> services) {
		this.services = services;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
