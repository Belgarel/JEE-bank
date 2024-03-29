/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.ws.rs.NotFoundException;
import model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Home
 */
public class CompteService {
    private static CompteService instance;
    private CompteService()
    {
        this.operationService = OperationService.instance();
    }
    public static CompteService instance() {
        if (instance == null) {
            synchronized(CompteService.class) {
                instance = new CompteService();
            }
        }
        return instance;
    }
    
    private OperationService operationService;
    private static final String ibanCharacters = "ABCDEFGHIJKLMNOPQRSTUVWYZ0123456789";
    private static Random ibanGenerator = new Random();
    private String genererIban() {
        StringBuilder sb = new StringBuilder(33);
        for( int i = 0; i < 33; i++ ) {
            if (i%4 == 3)
               sb.append(' ');
            else
               sb.append(ibanCharacters.charAt(ibanGenerator.nextInt(ibanCharacters.length()) ) );
        }
        return sb.toString();
    }
    
    public Compte trouverParIban(Session session, String iban) {
        Compte compte = (Compte) session.get(Compte.class, iban);
        if (compte == null)
            throw new NotFoundException("Aucun compte n'a été trouvé.");
        return compte;
    }

    public void creerCompte(Session session, Set<Client> proprietaires, double montantInitial) {
        if (montantInitial < 50.)
            throw new IllegalArgumentException("Un compte ne peut être créé qu'avec un solde "
                    + "supérieur ou égal à 50€.");
        if (proprietaires == null || proprietaires.isEmpty())
            throw new IllegalArgumentException("Un compte doit avoir au moins un propriétaire.");
        
        // Création du compte
        Compte compte = new Compte(genererIban(), 0.);
        compte.setEntrees(new HashSet<Operation>());
        compte.setSorties(new HashSet<Operation>());
        compte.setClients(proprietaires);
        for (Client client : proprietaires)
            client.getComptes().add(compte);
        
        Transaction transaction = session.beginTransaction();
        try {
            session.save(compte);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
        }
        System.out.println("Création du compte " + compte.getIban());
        
        operationService.creerOperation(session, new Date(), "Dépôt initial", montantInitial, null, compte);
    }
    public void cloturerCompte(Session session, Compte compte) {
        if (compte.getSolde() != 0.)
            throw new InvalidParameterException("Le solde du compte doit être nul.");
        // Le compte n'est pas supprimé, ainsi l'historique des transactions est conservé.
        for (Client client : compte.getClients())
            client.getComptes().remove(compte);
        compte.setClients(new HashSet<Client>());
        Transaction transaction = session.beginTransaction();
        try {
            session.saveOrUpdate(compte);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
        }
        System.out.println("Clôture à jour du client " + compte.getIban());
    }
}
