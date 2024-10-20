package csd.cuemaster.profile;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import csd.cuemaster.user.UserNotFoundException;
import csd.cuemaster.user.UserRepository;
import csd.cuemaster.user.User;

@RestController
public class ProfileController {
    private ProfileService profileService; 
    private UserRepository users;
    private ProfileRepository profiles; 

    public ProfileController (ProfileService profileService, UserRepository users, ProfileRepository profiles){
        this.profileService = profileService;
        this.users = users;
        this.profiles = profiles; 
    }

    @GetMapping("/profiles")
    public List<Profile> getAllProfiles() {
        return profileService.getAllProfile();
    }

    @GetMapping("/user/{user_id}/profile/{profile_id}")
    public Profile getUserProfile(@PathVariable (value = "user_id") Long user_id,@PathVariable Long profile_id) {
        return profileService.getProfile(user_id,profile_id);
    }
    
    @PutMapping("/user/{user_id}/profile/edit")
    public Profile putExistingProfile(@PathVariable (value = "user_id") Long user_id, @Valid @RequestBody Profile newProfileInfo) { 
        return profileService.updateProfile(user_id, newProfileInfo);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("user/{user_id}/profile")
    public Profile postProfile(@PathVariable (value = "user_id") Long user_id, @Valid @RequestBody Profile profile){

        User user = users.findById(user_id).orElseThrow(() -> new UserNotFoundException(user_id));

        if(profiles.findByUserId(user_id).isPresent()){
            
            throw new ProfileAlreadyExistsException(user_id);
        }

        return profileService.addProfile(user, profile);
    }

    // @PostMapping("users/{user_id}/profile/profilephoto")
    // public String postMethodName(@PathVariable (value = "user_id") Long user_id,  @RequestBody byte[] imageData) {
    //     return profileService.addProfilePhoto(user_id,imageData);
    // }

    // Returns a sorted list of players.
    @GetMapping("/leaderboard")
    public List<Profile> getLeaderboard() {
        List<Profile> sortedProfileList = profileService.sort();
        // profileService.updateRank(profiles);
        return sortedProfileList;
    }

    // Changes a player's points.
    @PutMapping("/changepoints/{user_id}")
    public Profile changePoints(@PathVariable (value = "user_id") Long user_id, @RequestBody Profile profile) {
        Integer newpoints = profile.getPoints();
        return profileService.pointsSet(user_id, newpoints);
    }
}