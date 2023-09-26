package com.example.techlinkmobileallinone.utils;

public class Station {
    private String ID;
    private String Name;

    public Station(String ID, String Name) {
        this.ID = ID;
        this.Name = Name;
    }
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
    //to display object as a string in spinner
    @Override
    public String toString() {
        return Name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Station){
            Station c = (Station )obj;
            if(c.getName().equals(Name) && c.getID()==ID ) return true;
        }
        return false;
    }
}
