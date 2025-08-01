package asot.me.rest.controller;

import asot.me.rest.dto.AppUserDto;
import asot.me.rest.service.AppUserService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Log4j2
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDto> getAppUser(@PathVariable Long id) {
        return ResponseEntity.ok(appUserService.getAppUser(id));
    }

    @GetMapping("/username")
    public ResponseEntity<AppUserDto> getAppUserByUsername(@PathParam("username") String username) {
        return ResponseEntity.ok(appUserService.getAppUserByUsername(username));
    }

    @GetMapping
    public ResponseEntity<Page<AppUserDto>> getAllAppUsers(
        @RequestParam(value = "_start", defaultValue = "0") int start,
        @RequestParam(value = "_end", defaultValue = "20") int end,
        @RequestParam(value = "_sort", defaultValue = "id") String sort,
        @RequestParam(value = "_order", defaultValue = "asc") String order
    ) {
        int size = end - start;
        int page = start / size;
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        return ResponseEntity.ok(appUserService.getAllAppUsers("", pageable));
    }

}