package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.model.dto.hau.StatisticalDTO;
import com.hau.ketnguyen.it.service.StatisticalService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StatisticalServiceImpl implements StatisticalService {
    @Override
    public List<StatisticalDTO> statisticalScore() {
        return null;
    }
}
