package main.java;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import main.java.dao.AnsattDAO;
import main.java.dao.AvdelingDAO;
import main.java.dao.ProsjektDAO;
import main.java.entity.Ansatt;
import main.java.entity.Avdeling;
import main.java.entity.Prosjekt;
import main.java.entity.ProsjektDeltakelse;

public class Main {
    
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private static EntityManagerFactory emf;
    private static AnsattDAO ansattDAO;
    private static AvdelingDAO avdelingDAO;
    private static ProsjektDAO prosjektDAO;
    
    public static void main(String[] args) {
        
        emf = Persistence.createEntityManagerFactory("firmaPU");
        ansattDAO = new AnsattDAO(emf);
        avdelingDAO = new AvdelingDAO(emf);
        prosjektDAO = new ProsjektDAO(emf);
        
        boolean fortsett = true;
        while (fortsett) {
            skrivMeny();
            int valg = Integer.parseInt(scanner.nextLine());
            
            switch (valg) {
                case 1:
                    finnAnsattMedId();
                    break;
                case 2:
                    finnAnsattMedBrukernavn();
                    break;
                case 3:
                    listAlleAnsatte();
                    break;
                case 4:
                    oppdaterStillingOgLonn();
                    break;
                case 5:
                    leggTilNyAnsatt();
                    break;
                case 6:
                    finnAvdelingMedId();
                    break;
                case 7:
                    listAnsattePaaAvdeling();
                    break;
                case 8:
                    oppdaterAvdeling();
                    break;
                case 9:
                    leggTilNyAvdeling();
                    break;
                case 10:
                    leggTilNyttProsjekt();
                    break;
                case 11:
                    registrerProsjektdeltakelse();
                    break;
                case 12:
                    registrerTimer();
                    break;
                case 13:
                    skrivUtProsjektinfo();
                    break;
                case 0:
                    fortsett = false;
                    System.out.println("Programmet avsluttes.");
                    break;
                default:
                    System.out.println("Ugyldig valg. Prøv igjen.");
            }
            
            System.out.println("\nTrykk enter for å fortsette...");
            scanner.nextLine();
        }
        
        emf.close();
        scanner.close();
    }
    
    private static void skrivMeny() {
        System.out.println("\n--- MENY ---");
        System.out.println("1. Søk etter ansatt på ID");
        System.out.println("2. Søk etter ansatt på brukernavn");
        System.out.println("3. List alle ansatte");
        System.out.println("4. Oppdater stilling og/eller lønn");
        System.out.println("5. Legg til ny ansatt");
        System.out.println("6. Søk etter avdeling på ID");
        System.out.println("7. List alle ansatte på en avdeling");
        System.out.println("8. Oppdater hvilken avdeling en ansatt jobber på");
        System.out.println("9. Legg til ny avdeling");
        System.out.println("10. Legg til nytt prosjekt");
        System.out.println("11. Registrer prosjektdeltakelse");
        System.out.println("12. Før timer for ansatt på prosjekt");
        System.out.println("13. Skriv ut info om prosjekt");
        System.out.println("0. Avslutt");
        System.out.print("Valg: ");
    }
    
    private static void finnAnsattMedId() {
        System.out.print("Oppgi ansatt-ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        Ansatt ansatt = ansattDAO.finnAnsattMedId(id);
        if (ansatt != null) {
            ansatt.skrivUtMedProsjekter();
        } else {
            System.out.println("Fant ingen ansatt med ID " + id);
        }
    }
    
    private static void finnAnsattMedBrukernavn() {
        System.out.print("Oppgi brukernavn: ");
        String brukernavn = scanner.nextLine();
        
        Ansatt ansatt = ansattDAO.finnAnsattMedBrukernavn(brukernavn);
        if (ansatt != null) {
            ansatt.skrivUtMedProsjekter();
        } else {
            System.out.println("Fant ingen ansatt med brukernavn " + brukernavn);
        }
    }
    
    private static void listAlleAnsatte() {
        List<Ansatt> ansatte = ansattDAO.finnAlleAnsatte();
        System.out.println("Alle ansatte:");
        for (Ansatt a : ansatte) {
            a.skrivUt();
        }
    }
    
    private static void oppdaterStillingOgLonn() {
        System.out.print("Oppgi ansatt-ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        Ansatt ansatt = ansattDAO.finnAnsattMedId(id);
        if (ansatt == null) {
            System.out.println("Fant ingen ansatt med ID " + id);
            return;
        }
        
        System.out.println("Nåværende informasjon:");
        ansatt.skrivUt();
        
        System.out.print("Ny stilling (trykk enter for å beholde nåværende): ");
        String nyStilling = scanner.nextLine();
        if (nyStilling.isEmpty()) {
            nyStilling = null;
        }
        
        System.out.print("Lønnsendring (positiv/negativ verdi, trykk enter for ingen endring): ");
        String lonnInput = scanner.nextLine();
        Double nyLonn = lonnInput.isEmpty() ? null : Double.parseDouble(lonnInput);
        
        ansattDAO.oppdaterStillingOgLonn(id, nyStilling, nyLonn);
        System.out.println("Ansatt oppdatert!");
    }
    
    private static void leggTilNyAnsatt() {
        System.out.print("Brukernavn (3-4 bokstaver): ");
        String brukernavn = scanner.nextLine();
        
        System.out.print("Fornavn: ");
        String fornavn = scanner.nextLine();
        
        System.out.print("Etternavn: ");
        String etternavn = scanner.nextLine();
        
        System.out.print("Ansettelsesdato (ÅÅÅÅ-MM-DD): ");
        LocalDate ansettelsesdato = LocalDate.parse(scanner.nextLine(), formatter);
        
        System.out.print("Stilling: ");
        String stilling = scanner.nextLine();
        
        System.out.print("Månedslønn: ");
        BigDecimal maanedslonn = new BigDecimal(scanner.nextLine());
        
        // List alle avdelinger
        List<Avdeling> avdelinger = avdelingDAO.finnAlleAvdelinger();
        System.out.println("Tilgjengelige avdelinger:");
        for (Avdeling a : avdelinger) {
            System.out.println(a.getId() + ": " + a.getNavn());
        }
        
        System.out.print("Velg avdeling (ID): ");
        int avdelingId = Integer.parseInt(scanner.nextLine());
        Avdeling avdeling = avdelingDAO.finnAvdelingMedId(avdelingId);
        
        if (avdeling == null) {
            System.out.println("Ugyldig avdeling. Ansatt ikke opprettet.");
            return;
        }
        
        Ansatt nyAnsatt = new Ansatt(brukernavn, fornavn, etternavn, ansettelsesdato, stilling, maanedslonn);
        nyAnsatt.setAvdeling(avdeling);
        
        ansattDAO.lagreNyAnsatt(nyAnsatt);
        System.out.println("Ny ansatt opprettet med ID: " + nyAnsatt.getId());
    }
    
    private static void finnAvdelingMedId() {
        System.out.print("Oppgi avdeling-ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        Avdeling avdeling = avdelingDAO.finnAvdelingMedId(id);
        if (avdeling != null) {
            avdeling.skrivUtMedAnsatte();
        } else {
            System.out.println("Fant ingen avdeling med ID " + id);
        }
    }
    
    private static void listAnsattePaaAvdeling() {
        System.out.print("Oppgi avdeling-ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        
        Avdeling avdeling = avdelingDAO.finnAvdelingMedId(id);
        if (avdeling == null) {
            System.out.println("Fant ingen avdeling med ID " + id);
            return;
        }
        
        List<Ansatt> ansatte = avdelingDAO.finnAnsattePaaAvdeling(id);
        System.out.println("Ansatte i avdeling " + avdeling.getNavn() + ":");
        for (Ansatt a : ansatte) {
            String sjefInfo = "";
            if (avdeling.getSjef() != null && a.getId().equals(avdeling.getSjef().getId())) {
                sjefInfo = " (Avdelingssjef)";
            }
            System.out.println(a.getId() + ": " + a.getFornavn() + " " + a.getEtternavn() + 
                    ", " + a.getStilling() + sjefInfo);
        }
    }
    
    private static void oppdaterAvdeling() {
        System.out.print("Oppgi ansatt-ID: ");
        int ansattId = Integer.parseInt(scanner.nextLine());
        
        Ansatt ansatt = ansattDAO.finnAnsattMedId(ansattId);
        if (ansatt == null) {
            System.out.println("Fant ingen ansatt med ID " + ansattId);
            return;
        }
        
        // Sjekk om ansatt er sjef
        if (ansatt.getAvdeling() != null && 
                ansatt.getAvdeling().getSjef() != null && 
                ansatt.getId().equals(ansatt.getAvdeling().getSjef().getId())) {
            System.out.println("Kan ikke flytte en ansatt som er sjef i sin avdeling!");
            return;
        }
        
        // List alle avdelinger
        List<Avdeling> avdelinger = avdelingDAO.finnAlleAvdelinger();
        System.out.println("Tilgjengelige avdelinger:");
        for (Avdeling a : avdelinger) {
            System.out.println(a.getId() + ": " + a.getNavn());
        }
        
        System.out.print("Velg ny avdeling (ID): ");
        int nyAvdelingId = Integer.parseInt(scanner.nextLine());
        
        try {
            ansattDAO.oppdaterAvdeling(ansattId, nyAvdelingId);
            System.out.println("Ansatt flyttet til ny avdeling!");
        } catch (Exception e) {
            System.out.println("Feil ved flytting av ansatt: " + e.getMessage());
        }
    }
    
    private static void leggTilNyAvdeling() {
        System.out.print("Navn på ny avdeling: ");
        String navn = scanner.nextLine();
        
        // List alle ansatte for å velge sjef
        List<Ansatt> ansatte = ansattDAO.finnAlleAnsatte();
        System.out.println("Velg en ansatt som skal være sjef:");
        for (Ansatt a : ansatte) {
            System.out.println(a.getId() + ": " + a.getFornavn() + " " + a.getEtternavn() + 
                    ", avdeling: " + (a.getAvdeling() != null ? a.getAvdeling().getNavn() : "Ingen"));
        }
        
        System.out.print("Velg sjef (ID): ");
        int sjefId = Integer.parseInt(scanner.nextLine());
        
        Ansatt sjef = ansattDAO.finnAnsattMedId(sjefId);
        if (sjef == null) {
            System.out.println("Ugyldig ansatt. Avdeling ikke opprettet.");
            return;
        }
        
        Avdeling nyAvdeling = new Avdeling(navn);
        
        try {
            avdelingDAO.lagreNyAvdeling(nyAvdeling, sjef);
            System.out.println("Ny avdeling opprettet med ID: " + nyAvdeling.getId());
            System.out.println("Ansatt " + sjef.getFornavn() + " " + sjef.getEtternavn() + 
                    " er nå sjef og flyttet til den nye avdelingen.");
        } catch (Exception e) {
            System.out.println("Feil ved opprettelse av avdeling: " + e.getMessage());
        }
    }
    
    private static void leggTilNyttProsjekt() {
        System.out.print("Navn på nytt prosjekt: ");
        String navn = scanner.nextLine();
        
        System.out.print("Beskrivelse: ");
        String beskrivelse = scanner.nextLine();
        
        Prosjekt nyttProsjekt = new Prosjekt(navn, beskrivelse);
        prosjektDAO.lagreNyttProsjekt(nyttProsjekt);
        
        System.out.println("Nytt prosjekt opprettet med ID: " + nyttProsjekt.getId());
    }
    
    private static void registrerProsjektdeltakelse() {
        // List alle ansatte
        List<Ansatt> ansatte = ansattDAO.finnAlleAnsatte();
        System.out.println("Velg ansatt:");
        for (Ansatt a : ansatte) {
            System.out.println(a.getId() + ": " + a.getFornavn() + " " + a.getEtternavn());
        }
        
        System.out.print("Ansatt ID: ");
        int ansattId = Integer.parseInt(scanner.nextLine());
        Ansatt ansatt = ansattDAO.finnAnsattMedId(ansattId);
        
        if (ansatt == null) {
            System.out.println("Ugyldig ansatt.");
            return;
        }
        
        // List alle prosjekter
        List<Prosjekt> prosjekter = prosjektDAO.finnAlleProsjekter();
        System.out.println("Velg prosjekt:");
        for (Prosjekt p : prosjekter) {
            System.out.println(p.getId() + ": " + p.getNavn());
        }
        
        System.out.print("Prosjekt ID: ");
        int prosjektId = Integer.parseInt(scanner.nextLine());
        Prosjekt prosjekt = prosjektDAO.finnProsjektMedId(prosjektId);
        
        if (prosjekt == null) {
            System.out.println("Ugyldig prosjekt.");
            return;
        }
        
        // Sjekk om ansatt allerede er registrert på prosjektet
        if (prosjektDAO.finnProsjektdeltakelse(ansattId, prosjektId) != null) {
            System.out.println("Ansatt er allerede registrert på dette prosjektet.");
            return;
        }
        
        System.out.print("Rolle: ");
        String rolle = scanner.nextLine();
        
        prosjektDAO.registrerProsjektdeltakelse(ansatt, prosjekt, rolle);
        System.out.println("Prosjektdeltakelse registrert!");
    }
    
    private static void registrerTimer() {
        System.out.print("Ansatt ID: ");
        int ansattId = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Prosjekt ID: ");
        int prosjektId = Integer.parseInt(scanner.nextLine());
        
        ProsjektDeltakelse pd = prosjektDAO.finnProsjektdeltakelse(ansattId, prosjektId);
        if (pd == null) {
            System.out.println("Fant ingen prosjektdeltakelse for denne ansatt/prosjekt kombinasjonen.");
            return;
        }
        
        System.out.print("Antall timer: ");
        int timer = Integer.parseInt(scanner.nextLine());
        
        prosjektDAO.registrerTimer(ansattId, prosjektId, timer);
        System.out.println(timer + " timer registrert på prosjektet!");
    }
    
    private static void skrivUtProsjektinfo() {
        System.out.print("Prosjekt ID: ");
        int prosjektId = Integer.parseInt(scanner.nextLine());
        
        Prosjekt prosjekt = prosjektDAO.finnProsjektMedId(prosjektId);
        if (prosjekt == null) {
            System.out.println("Fant ingen prosjekt med ID " + prosjektId);
            return;
        }
        
        prosjekt.skrivUtMedDeltagelser();
    }
}