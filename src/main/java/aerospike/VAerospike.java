package aerospike;

import com.aerospike.client.*;
import com.aerospike.client.policy.WritePolicy;
import dto.Address;
import dto.Appointment;
import dto.Patient;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class VAerospike {

    private static final Random r = new Random(System.currentTimeMillis());
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws UnknownHostException, ParseException {

        AerospikeClient client = new AerospikeClient("172.28.128.3", 3000);
        WritePolicy writePolicy = new WritePolicy();
        writePolicy.sendKey = true;

        int id;
        while (true) {
            System.out.println("P R Z Y C H O D N I A\nWybierz rodzaj operacji\n1. Wyswietl wszystkie wizyty\n2. Wyswietlanie wizyt po numerze ID\n3. Dodaj\n4. Aktualizuj\n5. Usun\n6. Przetwarzanie danych [caps-lock OFF dla imion pacjentow]\n7. Przetwarzanie danych [caps-lock ON dla imion pacjentow]\n8. Zakoncz prace klastra");
            int mode = input.nextInt();
            input.nextLine();
            switch (mode) {
                case 1:
                    getAll(client, writePolicy);
                    break;
                case 2:
                    System.out.println("Podaj ID wizyty");
                    id = input.nextInt(); input.nextLine();
                    Record record = client.get(writePolicy, new Key("test", "timetable", id));
                    Appointment appointment = (Appointment) record.getValue("mybin");
                    System.out.println(appointment.toString());
                    break;
                case 3:
                    client.put(writePolicy, new Key("test", "timetable", Math.abs(r.nextInt())), new Bin("mybin", addAppointment()));
                    System.out.println("PUT => " + "timetable");
                    break;
                case 4:
                    System.out.println("Podaj ID wizyty ktora chcesz edytowac");
                    id = input.nextInt(); input.nextLine();
                    Key key = new Key("test", "timetable", id);
                    client.put(writePolicy, key, new Bin("mybin", updateAppointment(client.get(writePolicy, key).getValue("mybin"))));
                    break;
                case 5:
                    System.out.println("Podaj ID wizyty ktora chcesz usunac");
                    id = input.nextInt(); input.nextLine();
                    client.delete(writePolicy, new Key("test", "timetable", id));
                    break;
                case 6:
                    changeOfNameCases(client, writePolicy, false);
                    break;
                case 7:
                    changeOfNameCases(client, writePolicy, true);
                    break;
                case 8:
                    client.close();
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

    public static Appointment updateAppointment(Object object) throws ParseException {

        Appointment appointment = (Appointment) object;
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

    public static void getAll(AerospikeClient client, WritePolicy writePolicy) {

        Map<Integer, Object> timetable = parseAllToMap(client, writePolicy);
        System.out.println("Harmonogram");
        for (Map.Entry<Integer, Object> e : timetable.entrySet()) {
            System.out.println(e.toString());
        }
    }

    private static void changeOfNameCases(AerospikeClient client, WritePolicy writePolicy, Boolean upper) {
        Map<Integer, Object> map = parseAllToMap(client, writePolicy);
        Appointment appointment;

        Iterator<Integer> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            Integer key = iterator.next();
            appointment = (Appointment) map.get(key);
            if(upper)
                appointment.getPatient().setName(appointment.getPatient().getName().toUpperCase());
            else
                appointment.getPatient().setName(appointment.getPatient().getName().toLowerCase());
            client.put(writePolicy, new Key("test", "timetable", key), new Bin("mybin", appointment));
        }
    }

    public static Map<Integer, Object> parseAllToMap(AerospikeClient client, WritePolicy writePolicy){
        ScanKeySet scanKeySet = new ScanKeySet();
        List<Key> keys = scanKeySet.runScan(client, "test", "timetable");
        Map<Integer, Object> tpmMap = new HashMap<>();
        Map<Integer, Object> timetable;
        for(Key k : keys){
            tpmMap.put(k.userKey.toInteger(), client.get(writePolicy, k).getValue("mybin"));
        }
        timetable = tpmMap;
        return timetable;
    }

}
