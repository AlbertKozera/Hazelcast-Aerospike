package hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicates;
import dto.Appointment;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HClient {

    private static final Random r = new Random(System.currentTimeMillis());
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws UnknownHostException, ParseException {
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.OFF);
        ClientConfig clientConfig = HConfig.getClientConfig();
        HazelcastInstance instance = HazelcastClient.newHazelcastClient(clientConfig);
        IMap<Long, Appointment> timetable = instance.getMap("timetable");

        Long id;
        while (true) {
            System.out.println("P R Z Y C H O D N I A\nWybierz rodzaj operacji [Client]\n1. Wyswietl wszystkie wizyty\n2. Wyswietl konkretna wizyte\n3. Przetwarzanie danych [caps-lock ON dla imion pacjentow]\n4. Zakoncz prace klastra");
            int mode = input.nextInt();
            input.nextLine();
            switch (mode) {
                case 1:
                    getAll(timetable);
                    break;
                case 2:
                    int typeOfGet = -1;
                    while (typeOfGet != 4) {
                        System.out.println("1. Wyswietlanie wizyt po numerze ID\n2. Wyswietlanie wizyt w przedziale czasowym\n3. Wyswietlanie wizyt na podstawie pierwszej litery imienia pacjenta\n4. Powrot");
                        typeOfGet = input.nextInt();
                        input.nextLine();
                        switch (typeOfGet) {
                            case 1:
                                System.out.println("Podaj ID wizyty");
                                id = input.nextLong();
                                input.nextLine();
                                System.out.println(timetable.get(id));
                                break;
                            case 2:
                                String from_s, to_s;
                                System.out.println("Podaj dolny przedzial czasowy [przyklad: DD-MM-RRRR 00:00:00]");
                                from_s = input.nextLine();
                                System.out.println("Podaj gorny przedzial czasowy [przyklad: DD-MM-RRRR 00:00:00]");
                                to_s = input.nextLine();
                                getByTime(timetable, from_s, to_s);
                                break;
                            case 3:
                                char letter;
                                System.out.println("Podaj litere");
                                letter = input.next().charAt(0);
                                getByFirstLetterOfName(timetable, letter);
                                break;
                        }
                    }
                case 3:
                    timetable.executeOnEntries(new HEntryProcessor());
                    break;
                case 4:
                    instance.shutdown();
                    System.exit(0);
            }
        }

    }

    public static void getAll(Map<Long, Appointment> timetable) {
        System.out.println("Harmonogram");
        for (Entry<Long, Appointment> e : timetable.entrySet()) {
            System.out.println(e.toString());
        }
    }

    public static void getByTime(IMap<Long, Appointment> timetable, String from_s, String to_s) throws ParseException {
        Date from, to;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        from = formatter.parse(from_s);
        to = formatter.parse(to_s);

        Collection<Appointment> appointmentsByTime = timetable.values(Predicates.between("dateOfTheVisit", from, to));
        System.out.println("Harmonogram [" + from_s + "] - [" + to_s + "]");

        for (Appointment e : appointmentsByTime) {
            System.out.println(e.toString());
        }
    }

    public static void getByFirstLetterOfName(IMap<Long, Appointment> timetable, char letter) {
        Collection<Appointment> appointmentsByFirstLetterOfName = timetable.values(Predicates.sql("patient.getName() LIKE '" + letter + "%'"));
        System.out.println("Umowione wizyty dla pacjentow na litere: " + letter);
        for (Appointment e : appointmentsByFirstLetterOfName) {
            System.out.println(e.toString());
        }
    }

}