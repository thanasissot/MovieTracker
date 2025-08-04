package asot.me.rest.service;

import asot.me.rest.configuration.AuthUtil;
import asot.me.rest.dom.AppUser;
import asot.me.rest.dto.AppUserDto;
import asot.me.rest.mapper.AppUserMapper;
import asot.me.rest.repository.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;

    public AppUserDto getAuthenticatedAppUser() {
        String username = AuthUtil.getCurrentUsername();
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return appUserMapper.toDTO(appUser);
    }

    public AppUserDto getAppUser(Long id) {
        AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AppUser not found with id: " + id));
        return appUserMapper.toDTO(appUser);
    }

    public AppUserDto getAppUserByUsername(String username) {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("AppUser not found with username: " + username));
        return appUserMapper.toDTO(appUser);
    }

    public Page<AppUserDto> getAllAppUsers(String username, Pageable pageable) {
        Page<AppUser> appUserPage = null;

        return appUserRepository.findAll(pageable).map(appUserMapper::toDTO);
    }

}