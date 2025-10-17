package com.star.recommendationservice.service;

import com.star.recommendationservice.error.UserNotFoundException;
import com.star.recommendationservice.model.Rule;
import com.star.recommendationservice.repository.RecommendationsServiceRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RuleChecker {
    private RecommendationsServiceRepository recommendationsServiceRepository;

    private final static List<String> QUERY_NAMES = List.of("USER_OF", "ACTIVE_USER_OF",
            "TRANSACTION_SUM_COMPARE", "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW");
    private final static List<String> PRODUCT_TYPES = List.of("DEBIT", "CREDIT", "INVEST", "SAVING");
    private final static List<String> TRANSACTION_TYPES = List.of("DEPOSIT", "WITHDRAW");
    private final static List<String> SIGNS = List.of(">", "<", "=", ">=", "<=");


    public RuleChecker(RecommendationsServiceRepository recommendationsServiceRepository) {
        this.recommendationsServiceRepository = recommendationsServiceRepository;
    }

    public void userIsExistCheck(UUID userId) throws UserNotFoundException {
        if (recommendationsServiceRepository.userIsExist(userId) == 0) {
            throw new UserNotFoundException("Нет пользователя с таким ID");
        }
    }
    public int ruleCheck(UUID userId, Rule rule) throws UserNotFoundException {
        if (!QUERY_NAMES.contains(rule.getQuery())) {
            return 0;
        }
        List<String> args = rule.getArguments();
        switch (rule.getQuery()) {
            case "USER_OF":
                if (args.isEmpty() || !PRODUCT_TYPES.contains(args.get(0))) return 0;
                int result1 = recommendationsServiceRepository.userOfCheck(userId, args.get(0));
                if ((result1 == 1) != rule.isNegate()) {
                    return 1;
                } else {
                    return 0;
                }
            case "ACTIVE_USER_OF":
                if (args.isEmpty() || !PRODUCT_TYPES.contains(args.get(0))) return 0;
                int result2 = recommendationsServiceRepository.activeUserOfCheck(userId, args.get(0));
                if ((result2 == 1) != rule.isNegate()) {
                    return 1;
                } else {
                    return 0;
                }
            case "TRANSACTION_SUM_COMPARE":
                if (args.size() < 4 || !PRODUCT_TYPES.contains(args.get(0))
                || !TRANSACTION_TYPES.contains(args.get(1))
                || !SIGNS.contains(args.get(2))
                || Integer.parseInt(args.get(3)) < 0) {
                    return 0;
                }
                boolean result3 = compareFromStringSign(recommendationsServiceRepository.userTransactionsSum(userId, args.get(0), args.get(1)),
                        args.get(2), Integer.parseInt(args.get(3)));
                if (result3 != rule.isNegate()) {
                    return 1;
                } else {
                    return 0;
                }
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW":
                if (args.size() < 2 || !PRODUCT_TYPES.contains(args.get(0))
                        || !SIGNS.contains(args.get(1))) {
                    return 0;
                }
                boolean result4 = compareFromStringSign(recommendationsServiceRepository.userTransactionsSum(userId, args.get(0), TRANSACTION_TYPES.get(0)),
                        args.get(1), recommendationsServiceRepository.userTransactionsSum(userId, args.get(0), TRANSACTION_TYPES.get(1)));
                if (result4 != rule.isNegate()) {
                    return 1;
                } else {
                    return 0;
                }
            default: return 0;
        }
    }

    private static boolean compareFromStringSign(int leftValue, String sign, int rightValue) {
        return switch (sign) {
            case ">" -> leftValue > rightValue;
            case "<" -> leftValue < rightValue;
            case "=" -> leftValue == rightValue;
            case ">=" -> leftValue >= rightValue;
            case "<=" -> leftValue <= rightValue;
            default -> false;
        };
    }
}
