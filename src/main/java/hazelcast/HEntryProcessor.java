package hazelcast;

import com.hazelcast.map.EntryProcessor;
import dto.Appointment;

import java.io.Serializable;
import java.util.Map;

class HEntryProcessor implements EntryProcessor<Long, Appointment, String>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String process(Map.Entry<Long, Appointment> e) {

        Appointment appointment = e.getValue();
        String name = appointment.getPatient().getName();
        name = name.toUpperCase();
        appointment.getPatient().setName(name);
        e.setValue(appointment);
        return name;



    }
}