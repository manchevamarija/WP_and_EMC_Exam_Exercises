package mk.ukim.finki.wp.kol2024g1.web;

import mk.ukim.finki.wp.kol2024g1.model.Reservation;
import mk.ukim.finki.wp.kol2024g1.model.RoomType;
import mk.ukim.finki.wp.kol2024g1.service.HotelService;
import mk.ukim.finki.wp.kol2024g1.service.ReservationService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping({"/", "/reservations"})
public class ReservationsController {

    private final ReservationService reservationService;
    private final HotelService hotelService;

    public ReservationsController(ReservationService reservationService, HotelService hotelService) {
        this.reservationService = reservationService;
        this.hotelService = hotelService;
    }

    // =========================
    // LIST
    // =========================
    @GetMapping
    public String listAll(
            @RequestParam(required = false) String guestName,
            @RequestParam(required = false) RoomType roomType,
            @RequestParam(required = false) Long hotel,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Model model
    ) {
        Page<Reservation> page =
                reservationService.findPage(guestName, roomType, hotel, pageNum - 1, pageSize);

        model.addAttribute("page", page);
        model.addAttribute("guestName", guestName);
        model.addAttribute("roomType", roomType);
        model.addAttribute("hotel", hotel);

        model.addAttribute("hotels", hotelService.listAll());
        model.addAttribute("roomTypes", RoomType.values());

        return "list";
    }

    // =========================
    // ADD FORM
    // =========================
    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("hotels", hotelService.listAll());
        model.addAttribute("roomTypes", RoomType.values());
        return "form";
    }

    // =========================
    // EDIT FORM
    // =========================
    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable Long id, Model model) {
        model.addAttribute("reservation", reservationService.findById(id));
        model.addAttribute("hotels", hotelService.listAll());
        model.addAttribute("roomTypes", RoomType.values());
        return "form";
    }

    // =========================
    // CREATE
    // =========================
    @PostMapping
    public String create(
            @RequestParam String guestName,
            @RequestParam LocalDate dateCreated,
            @RequestParam Integer daysOfStay,
            @RequestParam RoomType roomType,
            @RequestParam(required = false) Long hotel,
            @RequestParam(required = false) Long hotelId
    ) {
        Long finalHotelId = hotel != null ? hotel : hotelId;

        reservationService.create(
                guestName,
                dateCreated,
                daysOfStay,
                roomType,
                finalHotelId
        );

        return "redirect:/reservations";
    }

    // =========================
    // UPDATE
    // =========================
    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @RequestParam String guestName,
            @RequestParam LocalDate dateCreated,
            @RequestParam Integer daysOfStay,
            @RequestParam RoomType roomType,
            @RequestParam(required = false) Long hotel,
            @RequestParam(required = false) Long hotelId
    ) {
        Long finalHotelId = hotel != null ? hotel : hotelId;

        reservationService.update(
                id,
                guestName,
                dateCreated,
                daysOfStay,
                roomType,
                finalHotelId
        );

        return "redirect:/reservations";
    }

    // =========================
    // DELETE
    // =========================
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        reservationService.delete(id);
        return "redirect:/reservations";
    }

    // =========================
    // EXTEND STAY
    // =========================
    @RequestMapping(value = "/extend/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    @PreAuthorize("hasRole('USER')")
    public String extend(@PathVariable Long id) {
        reservationService.extendStay(id);
        return "redirect:/reservations";
    }

}
