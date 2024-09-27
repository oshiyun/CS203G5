package csd.cuemaster.profile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import csd.cuemaster.user.User;
import csd.cuemaster.user.User.UserRole;

@Service
public class ProfileServiceImpl implements ProfileService {

    private ProfileRepository profiles;

    public ProfileServiceImpl(ProfileRepository profiles) {
        this.profiles = profiles;
    }

    @Override
    public List<Profile> getSortedPlayers(List<User> users) {
        if (users == null || users.isEmpty()) {
            return new ArrayList<>();
        }
        return users.stream()
                .filter(user -> user.getRole() == UserRole.PLAYER)
                .sorted(Comparator.comparingInt(user -> ((User) user).getProfile().points).reversed())
                .map(user -> getProfile(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Profile getProfile(Long id){
        return profiles.findById(id).orElse(null);
    }
}