package com.devs.powerfit.controllers.email;

import com.devs.powerfit.dtos.email.EmailReportDto;
import com.devs.powerfit.services.email.EmailReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailReportService emailReportService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/report")
    public ResponseEntity<EmailReportDto> update(@RequestBody @Valid EmailReportDto request) {
        return new ResponseEntity<>(emailReportService.update(request), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/report")
    public ResponseEntity<EmailReportDto> get(){
        return new ResponseEntity<>(emailReportService.get(), HttpStatus.OK);
    }

}



