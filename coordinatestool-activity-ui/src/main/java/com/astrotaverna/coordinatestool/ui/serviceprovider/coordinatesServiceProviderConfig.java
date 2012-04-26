package com.astrotaverna.coordinatestool.ui.serviceprovider;

import java.net.URI;

public class coordinatesServiceProviderConfig {
	private URI uri=URI.create("http://example.com");
    private int numberOfServices=6;
    public URI getUri() {
        return uri;
    }
 
    public void setUri(URI uri) {
        this.uri = uri;
    }
 
    public int getNumberOfServices() {
        return numberOfServices;
    }
 
    public void setNumberOfServices(int numberOfServices) {
        this.numberOfServices = numberOfServices;
    }

}
