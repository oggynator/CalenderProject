import biweekly.parameter.ParticipationLevel;
import biweekly.parameter.ParticipationStatus;
import biweekly.parameter.Role;
import biweekly.property.Attendee;
import biweekly.property.Organizer;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        CalendarInviteVO calendarInviteVO = new CalendarInviteVO();
        calendarInviteVO.setSummary("Event title");
        calendarInviteVO.setMessage("Event message");

        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 9, 1, 15, 00); //The month is not correct, if set to the actual month, it is one month ahead

        calendarInviteVO.setStartDate(calendar.getTime());
        calendarInviteVO.setDuration(30);

        List<Attendee> attendeeList = new ArrayList<Attendee>();

        Organizer organizer = new Organizer("Test Organizer", "mcdickinacup@gmail.com");

        Attendee attendee = new Attendee("August H", "august.hejberg@gmail.com");

        attendee.setRsvp(true);
        attendee.setParticipationStatus(ParticipationStatus.NEEDS_ACTION);
        attendee.setParticipationLevel(ParticipationLevel.REQUIRED);
        attendee.setRole(Role.ATTENDEE);
        attendeeList.add(attendee);

        calendarInviteVO.setAttendees(attendeeList);
        calendarInviteVO.setOrganizer(organizer);

        String inviteMessage = InviteUtil.getInvitationBody(calendarInviteVO);

        List<String> emails = new ArrayList<String>();
        emails.add(organizer.getEmail());

        for(Attendee attendee1: attendeeList) {
            emails.add(attendee1.getEmail());
        }

        MailSender mailSender = new MailSender();
        mailSender.sendMail(inviteMessage, "REQUEST", emails);

    }
}