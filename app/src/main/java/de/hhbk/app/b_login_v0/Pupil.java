package de.hhbk.app.b_login_v0;

/**
 * Created by Markus on 27.10.2016.
 */

public class Pupil {
    private int id;
    private String lastname;
    private String firstname;
    private String klasse;

    public Pupil() {

    }

    public Pupil(int id, String lastname, String firstname, String klasse) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.klasse = klasse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getKlasse() {
        return klasse;
    }

    public void setKlasse(String klasse) {
        this.klasse = klasse;
    }
    @Override
    public String toString() {
        return Integer.toString(id) + ", " + lastname + ", " + firstname;
    }
}
