package com.example.mostvaluableplayer.service.game;

import com.example.mostvaluableplayer.model.GameType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Component
@RequiredArgsConstructor
public class GameServiceFactory {

    private static Map<GameType, GameService> gameServiceMap;

    @Autowired
    private GameServiceFactory(List<GameService> gameServices) {
        gameServiceMap = gameServices.stream().collect(Collectors.toMap(GameService::getType, identity()));
    }

    public GameService getGameService(String gameType) {
        return Optional.ofNullable(gameServiceMap.get(GameType.valueOf(gameType)))
                .orElseThrow(IllegalArgumentException::new);
    }
}
