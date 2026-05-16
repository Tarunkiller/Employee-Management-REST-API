package com.hexagon.empmanagement.service;

import com.hexagon.empmanagement.dto.AuthRequestDTO;
import com.hexagon.empmanagement.dto.AuthResponseDTO;
import com.hexagon.empmanagement.dto.RegisterRequestDTO;

public interface AuthService {
    void register(RegisterRequestDTO request);
    AuthResponseDTO login(AuthRequestDTO request);
}
