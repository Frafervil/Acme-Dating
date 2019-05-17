package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Challenge;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {


}
