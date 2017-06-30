package edu.lyon1.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
public class TweetController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @RequestMapping("/count")
    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from tweets", Integer.class);
    }

    @RequestMapping("/list")
    public List<Tweet> list(@RequestParam(name = "auteur", required = false) Optional<Utilisateur> auteur) {
        return auteur
                .map(a -> tweetRepository.findAllByAuteurOrderByDateDesc(a.getHandle()))
                .orElseGet(() -> tweetRepository.findAllByOrderByDateDesc());
    }

    @RequestMapping("/tweet")
    public Tweet tweet(@RequestParam("auteur") String auteur, @RequestParam("contenu") String contenu) {
        return tweetRepository.save(new Tweet(contenu, utilisateurRepository.findOne(auteur)));
    }

    @RequestMapping("/utilisateurs")
    public Iterable<Utilisateur> utilisateurs() {
        return utilisateurRepository.findAll();
    }

    @RequestMapping("/retweet")
    public ResponseEntity retweet(@RequestParam("utilisateur") String utilisateur, @RequestParam("tweet") Integer id) {
        Tweet tweet = tweetRepository.findOne(id);
        if (tweet.getAuteur().equals(utilisateur)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        tweet.getRetweeters().add(utilisateurRepository.findOne(utilisateur));
        try {
            tweetRepository.save(tweet);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/unretweet")
    public ResponseEntity unretweet(@RequestParam("utilisateur") String utilisateur, @RequestParam("tweet") Integer id) {
        Tweet tweet = tweetRepository.findOne(id);
        if (tweet.getAuteur().equals(utilisateur)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        tweet.getRetweeters().remove(utilisateurRepository.findOne(utilisateur));
        try {
            tweetRepository.save(tweet);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/tweet/{id}")
    public Tweet detailedTweet(@PathVariable("id") Integer id) {
        return tweetRepository.findOne(id);
    }

    @RequestMapping("/follow/{follower}/{followee}")
    public ResponseEntity follow(@PathVariable("follower") String followerHandle, @PathVariable("followee") String followeeHandle) {
        if (followerHandle == followeeHandle) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Utilisateur follower = utilisateurRepository.findOne(followerHandle);
        Utilisateur followee = utilisateurRepository.findOne(followeeHandle);

        if (follower == null || followee == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Collection<Utilisateur> followees = follower.getFollowees();
        followees.add(followee);
        follower.setFollowees(followees);

        try {
            utilisateurRepository.save(follower);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/feed/{utilisateur}")
    public List<Tweet> feed(@PathVariable("utilisateur") String utilisateurHandle) {
        Utilisateur utilisateur = utilisateurRepository.findOne(utilisateurHandle);

        if (utilisateur == null) {
            return null;
        }

        return tweetRepository.findAllByAuteurFolloweesOrderByDateDesc(utilisateur);
    }
}
