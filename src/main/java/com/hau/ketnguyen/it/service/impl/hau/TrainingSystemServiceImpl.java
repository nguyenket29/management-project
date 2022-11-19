package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.model.dto.hau.TrainingSystemDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchTrainSysRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.TrainingSystemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class TrainingSystemServiceImpl implements TrainingSystemService {
    @Override
    public TrainingSystemDTO save(TrainingSystemDTO trainingSystemDTO) {
        return null;
    }

    @Override
    public TrainingSystemDTO edit(Long id, TrainingSystemDTO trainingSystemDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public TrainingSystemDTO findById(Long id) {
        return null;
    }

    @Override
    public PageDataResponse<TrainingSystemDTO> getAll(SearchTrainSysRequest request) {
        return null;
    }
}
