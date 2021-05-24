package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class OrganizationListSection implements Section {
    private final List<Organization> organizations = new ArrayList<>();

    public void addOrganization(Organization organization) {
        organizations.add(organization);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        for (Organization item : organizations) {
            builder.append("- ").append(item).append("\n");
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }
}
