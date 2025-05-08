package com.company.enroller.controllers;


import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
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

    //Pobieranie listy ze spotkaniami
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingservice.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    //Pobieranie jednego spotkania po id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
        Meeting meeting = meetingservice.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    //Dodawanie spotkania
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addPMeeting(@RequestBody Meeting meeting) {
        if (meetingservice.findById(meeting.getId()) != null) {
            return new ResponseEntity<String>(
                    "Unable to create. A meeting with login " + meeting.getId() + " already exist.",
                    HttpStatus.CONFLICT);
        }
        meetingservice.add(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }

    //Usuwanie spotkania
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        Meeting meeting = meetingservice.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingservice.delete(meeting);
        return new ResponseEntity<Meeting>(HttpStatus.OK);
    }

    //Edytowanie spotkania
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Meeting updatedMeeting) {
        Meeting meeting = meetingservice.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingservice.update(updatedMeeting);
        return new ResponseEntity<Participant>(HttpStatus.OK);
    }

//    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
//    public ResponseEntity<?> getMeetingParticipant(@PathVariable("id") long id) {
//        Meeting meeting = meetingservice.findById(id);
//        if (meeting == null) {
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
//        }
//        meetingservice.findById(id);
//    }
    // dodać pobieranie participants. (dodać metodę do meetingService

}