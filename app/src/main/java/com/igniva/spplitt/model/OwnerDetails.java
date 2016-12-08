package com.igniva.spplitt.model;

/**
 * Created by igniva-php-08 on 21/6/16.
 */
public class OwnerDetails {
    String owner_username;
    String owner_email;
    String owner_picture;
    String owner_mobile;
    public String getOwner_username() {
        return owner_username;
    }

    public String getOwner_email() {
        if(owner_email.equals("") || owner_email.equalsIgnoreCase("null"))
            {
                return "";
            }

        else return owner_email;
    }

    public String getOwner_picture() {
        return owner_picture;
    }

    public String getOwner_mobile() {
        return owner_mobile;
    }
}
