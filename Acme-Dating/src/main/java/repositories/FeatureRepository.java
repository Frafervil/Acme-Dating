package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Feature;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Integer> {


}
