package com.journalbook;

import com.journalbook.entity.Entry;
import com.journalbook.entity.User_;
import com.journalbook.repository.EntryRepository;
import com.journalbook.repository.UserRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.ws.rs.QueryParam;

@RestController
public class JournalBookController {

    @Autowired
    UserRepository ur;

    @Autowired
    EntryRepository er;

    @Autowired
    Util util;

    @RequestMapping("/")
    User_ hello() {
        return ur.findByToken("admin");
        // return "Hello World!";
    }

    @RequestMapping("/createUser")
    User_ createUser(@QueryParam("firstName") String firstName, @QueryParam("middleName") String middleName,
            @QueryParam("surname") String surname, @QueryParam("emailAddress") String emailAddress,
            @QueryParam("phoneNumber") String phoneNumber, @QueryParam("password") String password) {
        User_ u = new User_();
        u.setActive(true);
        u.setCreatedDate(new Date());
        u.setEmailAddress(emailAddress);
        u.setFirstName(firstName);
        u.setMiddleName(middleName);
        u.setModifiedDate(new Date());
        u.setPhoneNumber(phoneNumber);
        u.setSurname(surname);
        u.setPassword(util.hashPassword(password));
        u.setToken(util.generateToken());
        ur.save(u);
        return u;
    }

    @RequestMapping("/login")
    User_ login(@QueryParam("emailAddress") String emailAddress, @QueryParam("password") String password) {
        User_ user = ur.findByEmailAddress(emailAddress);
        if (user != null) {
            Boolean checkPassword = util.checkHashedPassword(password, user.getPassword());
            if (checkPassword) {
                return user;
            }
        }
        return null;
    }

    @RequestMapping("/newJournalEntry")
    Entry newJournalEntry(@QueryParam("entryNote") String entryNote, @QueryParam("userToken") String userToken) {
        User_ user = ur.findByToken(userToken);
        if (user != null) {
            Entry e = new Entry();
            e.setEntryDate(new Date());
            e.setEntryNote(entryNote);
            e.setUser(user);
            er.save(e);
            return e;
        }
        return null;
    }

    @RequestMapping("/getTodayJournalEntry")
    List<Entry> getTodayJournalEntry(@QueryParam("userToken") String userToken) {
        List<Entry> es = new ArrayList<>();
        User_ user = ur.findByToken(userToken);
        if (user != null) {
            List<Entry> ess = er.findByUser_(user);
            for (Entry e : ess) {
                if (e.getEntryDate().getTime() >= util.today().getTime() && e.getEntryDate().getTime() <= new Date().getTime()) {
                    es.add(e);
                }
            }

        }
        return es;
    }

    @RequestMapping("/getJournalEntry")
    List<Entry> getTodayJournalEntry(@QueryParam("userToken") String userToken, @QueryParam("upperTime") Long upperTime,
            @QueryParam("lowerTime") Long lowerTime) {
        List<Entry> es = new ArrayList<>();
        User_ user = ur.findByToken(userToken);
        if (user != null) {
            List<Entry> ess = er.findByUser_(user);
            for (Entry e : ess) {
                if (e.getEntryDate().getTime() >= lowerTime && e.getEntryDate().getTime() <= upperTime) {
                    es.add(e);
                }
            }

        }
        return es;
    }

}
