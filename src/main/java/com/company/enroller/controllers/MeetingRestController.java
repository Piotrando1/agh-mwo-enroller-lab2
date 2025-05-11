package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingservice;

    @Autowired
    ParticipantService participantService;


    // Pobranie listy spotkań
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingservice.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    // Pobranie pojedynczego spotkania
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
        Meeting meeting = meetingservice.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    // Dodanie spotkania
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addPMeeting(@RequestBody Meeting meeting) {
        if (meetingservice.findById(meeting.getId()) != null) {
            return new ResponseEntity<String>(
                    "Unable to create. A meeting with id " + meeting.getId() + " already exist.",
                    HttpStatus.CONFLICT);
        }
        meetingservice.add(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }

    // Usunięcie spotkania
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        Meeting meeting = meetingservice.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingservice.delete(meeting);
        return new ResponseEntity<Meeting>(HttpStatus.OK);
    }

    // Edycja spotkania
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Meeting updatedMeeting) {
        Meeting meeting = meetingservice.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingservice.update(updatedMeeting);
        return new ResponseEntity<Participant>(HttpStatus.OK);
    }


    //GET meetings/{id}/participants - pobiera zarejestrowanych uczestników spotkania
    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id) {
        Meeting meeting = meetingservice.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(),
                HttpStatus.OK);
    }

    //POST meetings/{id}/participants - dodaje uczestnika do spotkania
    @RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
    public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") long id,
                                                     @RequestBody Participant participant) {
        Meeting meeting = meetingservice.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Participant p = participantService.findByLogin(participant.getLogin());
        if (p == null) {
            return new ResponseEntity<>("Participant not found", HttpStatus.NOT_FOUND);
        }

        if (meeting.getParticipants().contains(p)) {
            return new ResponseEntity<>("Participant already registered",
                    HttpStatus.CONFLICT);
        }

        meeting.addParticipant(p);
        meetingservice.update(meeting);
        return new ResponseEntity<Participant>(p, HttpStatus.CREATED);
    }

    //DELETE meetings/{id}/participants/{login} - usuwa uczestnika ze spotkania
    @RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeParticipantFromMeeting(@PathVariable("id") long id,
                                                          @PathVariable("login") String login) {
        Meeting meeting = meetingservice.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Participant p = participantService.findByLogin(login);
        if (p == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (!meeting.getParticipants().contains(p)) {
            return new ResponseEntity<>("Participant not registered",
                    HttpStatus.NOT_FOUND);
        }

        meeting.removeParticipant(p);
        meetingservice.update(meeting);
        return new ResponseEntity(HttpStatus.OK);
    }
}
