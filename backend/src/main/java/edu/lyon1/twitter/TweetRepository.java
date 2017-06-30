package edu.lyon1.twitter;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TweetRepository extends CrudRepository<Tweet,Integer> {
    List<Tweet> findAllByAuteurOrderByDateDesc(String auteur);
    List<Tweet> findAllByOrderByDateDesc();
    List<Tweet> findAllByAuteurFolloweesOrderByDateDesc(Utilisateur auteur);
    //List<Tweet> findAllByOrderByDateDesc();
}
