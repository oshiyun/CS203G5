package csd.cuemaster.profile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * We only need this interface declaration
 * Spring will automatically generate an implementation of the repo
 * 
 * JpaRepository provides more features by extending PagingAndSortingRepository, which in turn extends CrudRepository
 * For the purpose of this exercise, CrudRepository would also be sufficient
 */
@Repository
public interface ProfileRepository extends JpaRepository <Profile, Long> {

    Optional<Profile> findByUserId(Long user_id);
}
