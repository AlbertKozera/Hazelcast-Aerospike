package hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import dto.Address;
import dto.Appointment;
import dto.Patient;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HMember {

    private static final Random r = new Random(System.currentTimeMillis());
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws UnknownHostException, ParseException {
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.OFF);
        Config config = HConfig.getConfig();
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);
        Map<Long, Appointment> timetable = instance.getMap("timetable");

        Long id;
        while (true) {
            System.out.println("P R Z Y C H O D N I A\nWybierz rodzaj operacji [Member]\n1. Dodaj\n2. Aktualizuj\n3. Usun\n4. Przetwarzanie danych [caps-lock OFF dla imion pacjentow]\n5. Zakoncz prace klastra");
            int mode = input.nextInt();
            input.nextLine();
            switch (mode) {
                case 1:
                    timetable.put((long) Math.abs(r.nextInt()), addAppointment());
                    System.out.println("PUT => " + "timetable");
                    break;
                case 2:
                    System.out.println("Podaj ID wizyty ktora chcesz edytowac");
                    id = input.nextLong();
                    input.nextLine();
                    timetable.replace(id, updateAppointment(timetable.get(id)));
                    break;
                case 3:
                    System.out.println("Podaj ID wizyty ktora chcesz usunac");
                    id = input.nextLong(); input.nextLine();
                    timetable.remove(id);
                    break;
                case 4:
                    namesToUpperCase(timetable);
                    break;
                case 5:
                    instance.shutdown();
                    System.exit(0);
            }
        }
    }

    public static Appointment addAppointment() throws ParseException {
        System.out.println(">>DANE OSOBOWE<<");
        System.out.println("Wprowadz imie");
        String name = input.nextLine();
        System.out.println("Wprowadz nazwisko");
        String lastName = input.nextLine();
        System.out.println("Wprowadz rok urodzenia");
        int birthYear = input.nextInt();
        input.nextLine();
        System.out.println("Wprowadz numer telefonu [przykład: 111222333]");
        int phoneNumber = input.nextInt();
        input.nextLine();
        System.out.println(">ADRES ZAMIESZKANIA<");
        System.out.println("Wprowadz ulice");
        String street = input.nextLine();
        System.out.println("Wprowadz miasto");
        String city = input.nextLine();
        System.out.println("Wprowadz kod pocztowy [przyklad: 28404]");
        int zipCode = input.nextInt();
        input.nextLine();
        // Object residentialAddress
        Address residentialAddress = new Address(street, zipCode, city);
        System.out.println("Czy adres zameldowania jest taki sam jak adres zamieszkania? [tak|nie]");
        String tmp = input.nextLine();
        Address registeredAddress;
        if (tmp.equals("tak")) {
            // Object registeredAddress
            registeredAddress = new Address(street, zipCode, city);
        } else {
            System.out.println(">ADRES ZAMELDOWANIA<");
            System.out.println("Wprowadz ulice");
            street = input.nextLine();
            System.out.println("Wprowadz miasto");
            city = input.nextLine();
            System.err.println("Wprowadz kod pocztowy [przyklad: 28404]");
            zipCode = input.nextInt();
            input.nextLine();
            // Object registeredAddress
            registeredAddress = new Address(street, zipCode, city);
        }
        // Date of visit ---> 2011-01-18 00:00:00
        System.out.println(">DATA WIZYTY<");
        System.out.println("Wprowadz date wizyty [przyklad: DD-MM-RRRR 00:00:00]");
        String date_s = input.nextLine();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        Date date = formatter.parse(date_s);
        // Object patient
        Patient patient = new Patient(name, lastName, birthYear, phoneNumber, registeredAddress, residentialAddress);

        return new Appointment(date, patient);
    }

    public static Appointment updateAppointment(Appointment appointment) throws ParseException {
        String date_s, name, lastName, street, city;
        int birthYear, phoneNumber, zipCode;
        System.out.println("Ktore pole chcesz edytowac?");
        System.out.println("1. Data wizyty\n2. Imie\n3. Nazwisko\n4. Rok urodzenia\n5. Numer telefonu\n6. Adres Zamieszkania\n7. Adres Zameldowania\n8. Wszystkie");
        int mode = input.nextInt();
        input.nextLine();
        switch (mode) {
            case 1:
                System.out.println("Wprowadz date wizyty [przyklad: DD-MM-RRRR 00:00:00]");
                date_s = input.nextLine();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
                Date date = formatter.parse(date_s);
                appointment.setDateOfTheVisit(date);
                return appointment;
            case 2:
                System.out.println("Wprowadz imie");
                name = input.nextLine();
                appointment.getPatient().setName(name);
                return appointment;
            case 3:
                System.out.println("Wprowadz nazwisko");
                lastName = input.nextLine();
                appointment.getPatient().setLastName(lastName);
                return appointment;
            case 4:
                System.out.println("Wprowadz rok urodzenia");
                birthYear = input.nextInt();
                input.nextLine();
                appointment.getPatient().setBirthYear(birthYear);
                return appointment;
            case 5:
                System.out.println("Wprowadz numer telefonu [przykład: 111222333]");
                phoneNumber = input.nextInt();
                input.nextLine();
                appointment.getPatient().setPhoneNumber(phoneNumber);
                return appointment;
            case 6:
                System.out.println("Wprowadz ulice");
                street = input.nextLine();
                System.out.println("Wprowadz miasto");
                city = input.nextLine();
                System.out.println("Wprowadz kod pocztowy [przyklad: 28404]");
                zipCode = input.nextInt();
                input.nextLine();
                Address residentialAddress = new Address(street, zipCode, city);
                appointment.getPatient().setResidentialAddress(residentialAddress);
                return appointment;
            case 7:
                System.out.println("Wprowadz ulice");
                street = input.nextLine();
                System.out.println("Wprowadz miasto");
                city = input.nextLine();
                System.out.println("Wprowadz kod pocztowy [przyklad: 28404]");
                zipCode = input.nextInt();
                input.nextLine();
                Address registeredAddress = new Address(street, zipCode, city);
                appointment.getPatient().setRegisteredAddress(registeredAddress);
                return appointment;
            case 8:
                return addAppointment();
            default:
                return appointment;
        }

    }

    public static void namesToUpperCase(Map<Long, Appointment> timetable) {
        for (Map.Entry<Long, Appointment> entry : timetable.entrySet()) {
            Appointment appointment = entry.getValue();
            appointment.getPatient().setName(appointment.getPatient().getName().toLowerCase());
            timetable.replace(entry.getKey(), appointment);
        }

    }

}