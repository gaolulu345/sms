package com.sms.admin.data.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserAutoResourceDTO {

    List<AutoResourceDTO> menu = new ArrayList<>();

    List<AutoResourceDTO> op = new ArrayList<>();

}
