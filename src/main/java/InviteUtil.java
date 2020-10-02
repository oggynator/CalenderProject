import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VAlarm;
import biweekly.component.VEvent;
import biweekly.parameter.Related;
import biweekly.property.*;
import biweekly.util.Duration;
import biweekly.util.Frequency;
import biweekly.util.Recurrence;

import java.util.*;


public class InviteUtil {

    public static String getInvitationBody(CalendarInviteVO calendarInviteVO) {
        List<String> attendeesEmail = new ArrayList<String>();
        ICalendar ical = new ICalendar();
        VEvent event = new VEvent();

        event.setOrganizer(calendarInviteVO.getOrganizer().getEmail());

        UUID uuid = UUID.randomUUID();
        String uuidS = uuid.toString();
        System.out.println(uuid);
        event.setUid(uuidS); //Sets a UUID for the calender invite

        event.setDateStart(calendarInviteVO.getStartDate());

        Summary summary = event.setSummary(calendarInviteVO.getSummary());
        summary.setLanguage("da"); //Sets to danish, doesnt seem to have any effect in gMail
        event.setSummary(summary);

        Duration duration = new Duration.Builder().minutes(calendarInviteVO.getDuration()).build();
        event.setDuration(duration);

        if(calendarInviteVO.getAttendees() != null && calendarInviteVO.getAttendees().size() > 0) {
            for(Attendee attendee: calendarInviteVO.getAttendees()) {
                event.addAttendee(attendee);
                attendeesEmail.add(attendee.getEmail());
            }
        }

        event.setSequence(0);
        event.setCreated(new Date());
        event.setLastModified(new Date());

        /*
        Method.request for new events
        Method.cancel to cancel events
         */
        Method method = Method.request();
        ical.setMethod(method);//YES NO MAYBE  option
        ical.addEvent(event);
        return Biweekly.write(ical).go();
    }
}