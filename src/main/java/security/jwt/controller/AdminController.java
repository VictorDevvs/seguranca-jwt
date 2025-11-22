package security.jwt.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping
    @PreAuthorize("hasAuthority('read')")
    public String get(){
        return "GET:: admin";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('create')")
    public String post(){
        return "POST:: admin";
    }

    @PutMapping
    @PreAuthorize("hasAuthority('update')")
    public String put(){
        return "PUT:: admin";
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('delete')")
    public String delete(){
        return "DELETE:: admin";
    }
}
