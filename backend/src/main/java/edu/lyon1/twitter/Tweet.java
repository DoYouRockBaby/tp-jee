package edu.lyon1.twitter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Entity(name = "tweets")
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Timestamp date;
    private String contenu;
    @ManyToOne
    @JoinColumn(name="auteur")
    private Utilisateur auteur;
    @OneToMany
    @JoinTable(
            name="retweets",
            joinColumns = @JoinColumn( name="tweet"),
            inverseJoinColumns = @JoinColumn( name="utilisateur")
    )
    private Collection<Utilisateur> retweeters;

    public Tweet() {
    }

    public Tweet(Integer id, Timestamp date, String contenu, Utilisateur auteur) {
        this.id = id;
        this.date = date;
        this.contenu = contenu;
        this.auteur = auteur;
    }

    public Tweet(String contenu, Utilisateur auteur) {
        this.contenu = contenu;
        this.auteur = auteur;
        this.date = Timestamp.from(Instant.now());
        this.retweeters = new LinkedList<Utilisateur>();
    }

    public Integer getId() {
        return id;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getContenu() {
        return contenu;
    }

    public Utilisateur getAuteur() {
        return auteur;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public void setAuteur(Utilisateur auteur) {
        this.auteur = auteur;
    }

    public Collection<Utilisateur> getRetweeters() {
        return retweeters;
    }

    public void setRetweeters(Collection<Utilisateur> retweeters) {
        this.retweeters = retweeters;
    }
}
