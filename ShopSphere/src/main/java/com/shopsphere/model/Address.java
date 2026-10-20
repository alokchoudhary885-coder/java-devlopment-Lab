package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a shipping or billing address associated with a customer account.
 */
public class Address implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private boolean isDefault;

    /**
     * Default no-argument constructor.
     */
    public Address() {
    }

    /**
     * Constructor without id (useful for persisting new address records).
     *
     * @param userId     Identifier of the user who owns this address
     * @param street     Street line address (house number, street name, apartment)
     * @param city       City name
     * @param state      State or province
     * @param postalCode Postal or ZIP code
     * @param country    Country name
     * @param isDefault  Flag indicating if this is the user's primary/default address
     */
    public Address(int userId, String street, String city, String state, String postalCode, String country, boolean isDefault) {
        this.userId = userId;
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.isDefault = isDefault;
    }

    /**
     * Full parameterized constructor matching the database schema.
     *
     * @param id         Unique address identifier
     * @param userId     Identifier of the user who owns this address
     * @param street     Street line address
     * @param city       City name
     * @param state      State or province
     * @param postalCode Postal or ZIP code
     * @param country    Country name
     * @param isDefault  Flag indicating if this is the default address
     */
    public Address(int id, int userId, String street, String city, String state, String postalCode, String country, boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.isDefault = isDefault;
    }

    // --- Business / Convenience Methods ---

    /**
     * Returns a human-readable single-line formatted address string.
     *
     * @return Formatted address
     */
    public String getFormattedAddress() {
        StringBuilder sb = new StringBuilder();
        if (street != null && !street.isBlank()) sb.append(street);
        if (city != null && !city.isBlank()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(city);
        }
        if (state != null && !state.isBlank()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(state);
        }
        if (postalCode != null && !postalCode.isBlank()) {
            if (!sb.isEmpty()) sb.append(" - ");
            sb.append(postalCode);
        }
        if (country != null && !country.isBlank()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(country);
        }
        return sb.toString();
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    // Bean-spec alternative getters/setters for JSP Expression Language compatibility
    public boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return id == address.id && userId == address.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", userId=" + userId +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
