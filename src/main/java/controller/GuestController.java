package controller;

import model.Guest;
import service.GuestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/guests")
public class GuestController {
    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping("all")
    public List<Guest> getAllGuests() {
        return guestService.getAllGuests();
    }

    @GetMapping("/byId/{id}")
    public Optional<Guest> getGuestById(@PathVariable String id) {
        return guestService.getGuestById(id);
    }

    @PostMapping("/add_one")
    public Guest addGuest(@RequestBody Guest guest) {
        return guestService.addGuest(guest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteGuest(@PathVariable String id) {
        guestService.deleteGuest(id);
    }
}