package model;
// Generated 21 juin 2019 14:34:53 by Hibernate Tools 4.3.1

import java.util.Set;




/**
 * Client generated by hbm2java
 */
public class Client extends Utilisateur {


     private int idclient;
     private Agence agence;
     private Conseiller conseiller;
     private Set<Compte> comptes;
     private Set<Message> messages;

    public Client() {
    }

    public Client(int idclient, String login, String pwd, String nom, String prenom,
            String adresse, String telephone, String mail, Conseiller conseiller, Agence agence) {
       super(login, pwd, nom, prenom, adresse, telephone, mail);
       this.idclient = idclient;
       this.conseiller = conseiller;
       this.agence = agence;
    }
    public Client(String login, String pwd, String nom, String prenom,
            String adresse, String telephone, String mail, Conseiller conseiller, Agence agence) {
       super(login, pwd, nom, prenom, adresse, telephone, mail);
       this.conseiller = conseiller;
       this.agence = agence;
    }
   
    public int getIdclient() {
        return this.idclient;
    }
    public void setIdclient(int idclient) {
        this.idclient = idclient;
    }
    public Conseiller getConseiller() {
        return this.conseiller;
    }
    public void setConseiller(Conseiller conseiller) {
        this.conseiller = conseiller;
    }
    public Agence getAgence() {
        return this.agence;
    }
    public void setAgence(Agence agence) {
        this.agence = agence;
    }
    public Set<Compte> getComptes() {
        return comptes;
    }
    public void setComptes(Set<Compte> comptes) {
        this.comptes = comptes;
    }
    public Set<Message> getMessages() {
        return messages;
    }
    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }
    
    @Override
    public boolean equals(Object that){
        if (!(that instanceof Client))
            return false;
        Client other = (Client) that;
        return new Integer(this.idclient).equals(other.getIdclient());
    }
}


