package org.tsdes.intro.jee.jpa.embedded;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String city;
    private String country;
    private Long postcode;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getPostcode() {
        return postcode;
    }

    public void setPostcode(Long postcode) {
        this.postcode = postcode;
    }
}
