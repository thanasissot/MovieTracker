package asot.me.rest.controller;

import asot.me.rest.dto.AppUserDto;
import asot.me.rest.service.AppUserService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    public ResponseEntity<AppUserDto> getAllAppUsers() {
        return ResponseEntity.ok(appUserService.getAuthenticatedAppUser());
    }

}