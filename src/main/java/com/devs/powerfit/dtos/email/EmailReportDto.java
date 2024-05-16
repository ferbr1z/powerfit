package com.devs.powerfit.dtos.email;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

@Data
public class EmailReportDto extends AbstractDto {
    private String reportEmail;
}
