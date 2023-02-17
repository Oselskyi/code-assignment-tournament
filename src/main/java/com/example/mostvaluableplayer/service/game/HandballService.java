package com.example.mostvaluableplayer.service.game;

import com.example.mostvaluableplayer.model.GameStats;
import com.example.mostvaluableplayer.model.HandballPlayer;
import com.example.mostvaluableplayer.model.Player;
import com.example.mostvaluableplayer.model.Sportsman;
import com.example.mostvaluableplayer.gateway.PlayerStatsGateway;
import com.example.mostvaluableplayer.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HandballService implements GameService<HandballPlayer> {

    private final PlayerStatsGateway<HandballPlayer> handballPlayerPlayerStatsGateway;
    private final TeamService teamService;

    @Override
    public List<Sportsman> calculateRatingForEveryPlayer(GameStats gameStats) {

        List<HandballPlayer> handballPlayers = handballPlayerPlayerStatsGateway.parseGameStatsToPlayers(gameStats.getLines());
        List<Sportsman> ratedPlayers = new ArrayList<>();
        String winner = defineWinners(handballPlayers);
        for (HandballPlayer handballPlayer :
                handballPlayers) {
            Sportsman sportsman = new Sportsman(handballPlayer.getNickname(), calculateGameRating(handballPlayer, winner));

            ratedPlayers.add(sportsman);
        }
        return ratedPlayers;
    }

    private String defineWinners(List<HandballPlayer> playerStatsList) {
        var teamScoreMap = playerStatsList.stream()
                .collect(Collectors.groupingBy(Player::getTeamName, Collectors.summingInt(HandballPlayer::getGoalMade)));

        return teamService.getWinner(teamScoreMap);
    }

    private int calculateGameRating(HandballPlayer player, String winnerTeam) {
        int rating = player.getGoalMade() * 2 - player.getGoalReceive();
        if (player.getTeamName().equals(winnerTeam)) {
            rating += 10;
        }
        return rating;

    }
}