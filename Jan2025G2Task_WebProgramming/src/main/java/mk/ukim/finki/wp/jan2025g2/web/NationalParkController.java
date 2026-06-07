package mk.ukim.finki.wp.jan2025g2.web;

import mk.ukim.finki.wp.jan2025g2.model.NationalPark;
import mk.ukim.finki.wp.jan2025g2.model.ParkType;
import mk.ukim.finki.wp.jan2025g2.service.NationalParkService;
import mk.ukim.finki.wp.jan2025g2.service.ParkLocationService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping({"/", "/national-parks"})
public class NationalParkController {

    private final NationalParkService nationalParkService;
    private final ParkLocationService parkLocationService;

    public NationalParkController(NationalParkService nationalParkService, ParkLocationService parkLocationService) {
        this.nationalParkService = nationalParkService;
        this.parkLocationService = parkLocationService;
    }


    /**
     * This method should use the "list.html" template to display all national parks.
     * The method should be mapped on paths '/' and '/national-parks'.
     * The arguments that this method takes are optional and can be 'null'.
     * The filtered national parks that are the result of the call
     * findPage method from the NationalParkService should be displayed.
     * If you want to return a paginated result, you should also pass the page number and the page size as arguments.
     *
     * @param name       Filters national parks whose names contain the specified text
     * @param areaSize   Filters by area size bigger than the specified value
     * @param rating     Filters by rating greater than the specified value
     * @param parkType   Filters by park type
     * @param locationId Filters by location
     * @param pageNum    The page number
     * @param pageSize   The number of items per page
     * @return The view "list.html"
     */
    @GetMapping()
    public String listAll(@RequestParam(required = false) String name,
                          @RequestParam(required = false) Double areaSize,
                          @RequestParam(required = false) Double rating,
                          @RequestParam(required = false) ParkType parkType,
                          @RequestParam(required = false) Long locationId,
                          @RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          Model model) {

        Page<NationalPark> nationalParks = nationalParkService.findPage(name, areaSize, rating, parkType,locationId , pageNum - 1, pageSize);
        model.addAttribute("page", nationalParks);

        model.addAttribute("name", name);
        model.addAttribute("areaSize", areaSize);
        model.addAttribute("rating", rating);
        model.addAttribute("parkType", parkType);
        model.addAttribute("locationId", locationId);

        model.addAttribute("locations", this.parkLocationService.listAll());
        model.addAttribute("parkTypes", ParkType.values());

        return "list";
    }

    /**
     * This method should display the "form.html" template.
     * The method should be mapped on path '/national-parks/add'.
     *
     * @return The view "form.html".
     */

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAdd(Model model) {
        model.addAttribute("locations", this.parkLocationService.listAll());
        model.addAttribute("parkTypes", ParkType.values());
        return "form";
    }

    /**
     * This method should display the "form.html" template.
     * However, in this case, all 'input' elements should be filled with the appropriate value for the national park that is updated.
     * The method should be mapped on path '/national-parks/edit/[id]'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEdit(@PathVariable Long id, Model model) {
        model.addAttribute("park", nationalParkService.findById(id));
        model.addAttribute("locations", this.parkLocationService.listAll());
        model.addAttribute("parkTypes", ParkType.values());
        return "form";
    }

    /**
     * This method should create a new national park given the arguments it takes.
     * The method should be mapped on path '/national-parks'.
     * After the national park is created, all national parks should be displayed.
     *
     * @param name       The name of the national park
     * @param areaSize   The area size
     * @param rating     The rating of the park
     * @param parkType   The park type
     * @param locationId The location ID
     * @return Redirects to the list of national parks on '/national-parks'
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String create( @RequestParam String name,
                          @RequestParam Double areaSize,
                          @RequestParam Double rating,
                          @RequestParam ParkType parkType,
                          @RequestParam Long locationId) {
        nationalParkService.create(name,areaSize,rating,parkType,locationId);
        return "redirect:/national-parks";
    }

    /**
     * This method should update an existing national park given the arguments it takes.
     * The method should be mapped on path '/national-parks/[id]'.
     * After the national park is updated, all national parks should be displayed.
     *
     * @param id         The ID of the national park to update
     * @param name       The name of the national park
     * @param areaSize   The area size
     * @param rating     The rating of the park
     * @param parkType   The park type
     * @param locationId The location ID
     * @return Redirects to the list of national parks on '/national-parks'
     */
    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update( @PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam Double areaSize,
                         @RequestParam Double rating,
                         @RequestParam ParkType parkType,
                         @RequestParam Long locationId) {
        return "redirect:/national-parks";
    }

    /**
     * This method should delete the national park that has the appropriate identifier.
     * The method should be mapped on path '/national-parks/delete/[id]'.
     * After the national parks is deleted, all national parks should be displayed on '/national-parks'.
     *
     * @param id The ID of the national park to delete
     * @return Redirects to the list of national parks
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete( @PathVariable Long id) {
        nationalParkService.delete(id);
        return "redirect:/national-parks";
    }

    /**
     * This method should close the national parks that has the appropriate identifier.
     * The method should be mapped on path '/national-parks/close/[id]'.
     * After the selected national park is closed, all national parks should be displayed on '/national-parks'.
     *
     * @param id The ID of the national park to close
     * @return Redirects to the list of national parks
     */

    @PostMapping("/close/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String close( @PathVariable Long id) {
        nationalParkService.close(id);
        return "redirect:/national-parks";
    }
}

