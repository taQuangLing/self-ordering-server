package hust.server.domain.authen.service;

import hust.server.domain.authen.dto.response.PositionResponse;
import hust.server.domain.authen.entities.Position;
import hust.server.domain.authen.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionService {
    @Autowired
    private PositionRepository positionRepository;

    public List<PositionResponse> getPositionList(String userId) {
        List<Position> positionList = positionRepository.getByCreatedBy(userId);
        return positionList.stream().map(Position::toPositionResponse).collect(Collectors.toList());
    }
}
