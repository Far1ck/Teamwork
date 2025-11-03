package com.star.recommendationservice.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.star.recommendationservice.error.UserNotFoundException;
import com.star.recommendationservice.model.recommendation.Recommendation;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы Telegram API
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final TelegramBot telegramBot;

    private final RecommendationsService recommendationsService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, RecommendationsService recommendationsService) {
        this.telegramBot = telegramBot;
        this.recommendationsService = recommendationsService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            Long chatId = update.message().chat().id();
            String message = update.message().text();
            if (!recommendationsService.checkUserExistence(chatId) && !message.isEmpty()) {
                sendMessage(chatId, "На связи банк \"Star\"! Для получения рекомендаций введите, пожалуйста, ID пользователя...");
                recommendationsService.addNewUser(chatId);
            } else if (recommendationsService.checkUserExistence(chatId) && !message.isEmpty()) {
                try {
                    UUID uuid = UUID.fromString(message);
                    List<Recommendation> recommendations = recommendationsService.getUserRecommendations(uuid).getRecommendations();
                    String userName = recommendationsService.getUserName(uuid);
                    if (recommendations.isEmpty()) {
                        sendMessage(chatId, "Здравствуйте, " + userName + ". К сожалению, для Вас новых продуктов нет");
                    }
                    StringBuilder result = new StringBuilder("Здравствуйте, " + userName + ".\nНовые продукты для вас:\n\n");
                    int recommendationCounter = 1;
                    for (Recommendation recommendation : recommendations) {
                        result.append(recommendationCounter).append(". ").append(recommendation.getName()).append("\n\n")
                                .append(recommendation.getText()).append("\n\n");
                        recommendationCounter++;
                    }
                    sendMessage(chatId, result.toString());
                } catch (UserNotFoundException e) {
                    sendMessage(chatId, "Пользователь с таким ID не найден.");
                } catch (IllegalArgumentException e) {
                    sendMessage(chatId, "Некорректный формат ID.");
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId,
                message);
        telegramBot.execute(sendMessage);
    }

}
