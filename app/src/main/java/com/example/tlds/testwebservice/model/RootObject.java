package com.example.tlds.testwebservice.model;

import java.util.List;

/**
 * Created by TLDs on 30/03/2016.
 */

public class RootObject
{
    public List<Profiles>profiles;

    public List<Profiles> getProfiles(){
        return profiles;
    }
    public void setProfiles(List<Profiles>profiles){
        this.profiles = profiles;
    }

}

