package service;

import model.Guest;
import repository.GuestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuestService {
    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    public Optional<Guest> getGuestById(String id) {
        return guestRepository.findById(id);
    }

    public Guest addGuest(Guest guest) {
        return guestRepository.save(guest);
    }

    public void deleteGuest(String id) {
        guestRepository.deleteById(id);
    }
}