package net.swofty.type.skyblockgeneric.bazaar;

import net.swofty.commons.ServiceType;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.protocol.objects.bazaar.*;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.user.HypixelPlayer;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class BazaarConnector {
    private final HypixelPlayer player;
    private final ProxyService bazaarService;
    private static final DecimalFormat FORMATTER = new DecimalFormat("#,###.##");

    public BazaarConnector(HypixelPlayer player) {
        this.player = player;
        this.bazaarService = new ProxyService(ServiceType.BAZAAR);
    }

    private UUID getPlayerProfileUUID() {
        return player.getProfiles().getCurrentlySelected();
    }

    public CompletableFuture<List<BazaarOrder>> getPendingOrders() {
        var message = new BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersMessage(
                player.getUuid(),
                getPlayerProfileUUID()
        );

        return bazaarService.<BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersMessage,
                        BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersResponse>handleRequest(message)
                .thenApply(response -> response.orders.stream()
                        .map(order -> new BazaarOrder(
                                order.orderId(),
                                order.itemName(),
                                OrderSide.valueOf(order.side()),
                                order.price(),
                                order.amount(),
                                order.profileUUID()
                        ))
                        .toList());
    }

    public CompletableFuture<Boolean> cancelOrder(UUID orderId) {
        var message = new BazaarCancelProtocolObject.CancelMessage(
                orderId,
                player.getUuid(),
                getPlayerProfileUUID()
        );

        return bazaarService.<BazaarCancelProtocolObject.CancelMessage,
                        BazaarCancelProtocolObject.CancelResponse>handleRequest(message)
                .thenApply(response -> response.successful);
    }

    public CompletableFuture<BazaarItemData> getItemData(ItemType itemType) {
        var message = new BazaarGetItemProtocolObject.BazaarGetItemMessage(itemType.name());

        return bazaarService.<BazaarGetItemProtocolObject.BazaarGetItemMessage,
                        BazaarGetItemProtocolObject.BazaarGetItemResponse>handleRequest(message)
                .thenApply(response -> {
                    var buyOrders = response.buyOrders().stream()
                            .map(order -> new MarketOrder(
                                    order.playerUUID(),
                                    order.profileUUID(),
                                    order.price(),
                                    order.amount()
                            ))
                            .toList();

                    var sellOrders = response.sellOrders().stream()
                            .map(order -> new MarketOrder(
                                    order.playerUUID(),
                                    order.profileUUID(),
                                    order.price(),
                                    order.amount()
                            ))
                            .toList();

                    return new BazaarItemData(response.itemName(), buyOrders, sellOrders);
                });
    }

    public CompletableFuture<BazaarStatistics> getItemStatistics(ItemType itemType) {
        return getItemData(itemType).thenApply(data -> {
            var buyStats = calculateBuyStatistics(data.buyOrders());
            var sellStats = calculateSellStatistics(data.sellOrders());

            return new BazaarStatistics(
                    buyStats.highestPrice(),
                    buyStats.lowestPrice(),
                    buyStats.averagePrice(),
                    sellStats.lowestPrice(),
                    sellStats.highestPrice(),
                    sellStats.averagePrice()
            );
        });
    }

    public CompletableFuture<BazaarResult> createBuyOrder(ItemType itemType, double pricePerUnit, int quantity) {
        var message = new BazaarBuyProtocolObject.BazaarBuyMessage(
                itemType.name(),
                quantity,
                pricePerUnit,
                player.getUuid(),
                getPlayerProfileUUID()
        );

        return bazaarService.<BazaarBuyProtocolObject.BazaarBuyMessage,
                        BazaarBuyProtocolObject.BazaarBuyResponse>handleRequest(message)
                .thenApply(response -> new BazaarResult(
                        response.successful,
                        response.successful ? "Buy order created!" : "Failed to create buy order"
                ));
    }

    public CompletableFuture<BazaarResult> createSellOrder(ItemType itemType, double pricePerUnit, int quantity) {
        var message = new BazaarSellProtocolObject.BazaarSellMessage(
                itemType.name(),
                player.getUuid(),
                getPlayerProfileUUID(),
                pricePerUnit,
                quantity
        );

        return bazaarService.<BazaarSellProtocolObject.BazaarSellMessage,
                        BazaarSellProtocolObject.BazaarSellResponse>handleRequest(message)
                .thenApply(response -> new BazaarResult(
                        response.successful,
                        response.successful ? "Sell order created!" : "Failed to create sell order"
                ));
    }

    public CompletableFuture<BazaarResult> instantBuy(ItemType itemType, int quantity) {
        return getItemStatistics(itemType).thenCompose(stats -> {
            if (stats.bestAsk() <= 0) {
                return CompletableFuture.completedFuture(
                        new BazaarResult(false, "No sell offers available!")
                );
            }

            double priceWithFee = stats.bestAsk() * 1.04;
            double totalCost = priceWithFee * quantity;

            if (totalCost > player.getCoins()) {
                return CompletableFuture.completedFuture(
                        new BazaarResult(false, "Need " + FORMATTER.format(totalCost) + " coins!")
                );
            }

            int maxSpace = player.maxItemFit(itemType);
            if (quantity > maxSpace) {
                return CompletableFuture.completedFuture(
                        new BazaarResult(false, "Not enough inventory space!")
                );
            }

            player.removeCoins(totalCost);

            return createBuyOrder(itemType, priceWithFee, quantity)
                    .thenApply(result -> {
                        player.getBazaarConnector().processAllPendingTransactions();
                        return result;
                    });
        });
    }

    public CompletableFuture<BazaarResult> instantSell(ItemType itemType) {
        int availableAmount = player.getAmountInInventory(itemType);
        if (availableAmount <= 0) {
            return CompletableFuture.completedFuture(
                    new BazaarResult(false, "You don't have any " + itemType.getDisplayName() + "!")
            );
        }

        return getItemStatistics(itemType).thenCompose(stats -> {
            if (stats.bestBid() <= 0) {
                return CompletableFuture.completedFuture(
                        new BazaarResult(false, "No buy orders available!")
                );
            }

            var takenItems = player.takeItem(itemType, availableAmount);
            if (takenItems == null) {
                return CompletableFuture.completedFuture(
                        new BazaarResult(false, "Failed to remove items from inventory!")
                );
            }

            return createSellOrder(itemType, stats.bestBid(), availableAmount)
                    .thenApply(result -> {
                        player.getBazaarConnector().processAllPendingTransactions();
                        return result;
                    });
        });
    }

    public CompletableFuture<Boolean> isOnline() {
        return bazaarService.isOnline();
    }

    public CompletableFuture<List<PendingTransaction>> getPendingTransactions() {
        var message = new BazaarGetPendingTransactionsProtocolObject.BazaarGetPendingTransactionsMessage(
                player.getUuid(),
                getPlayerProfileUUID()
        );

        return bazaarService.<BazaarGetPendingTransactionsProtocolObject.BazaarGetPendingTransactionsMessage,
                        BazaarGetPendingTransactionsProtocolObject.BazaarGetPendingTransactionsResponse>handleRequest(message)
                .thenApply(response -> response.transactions.stream()
                        .map(txInfo -> new PendingTransaction(
                                txInfo.id,
                                txInfo.transactionType,
                                txInfo.transactionData,
                                txInfo.createdAt
                        ))
                        .collect(Collectors.toList()));
    }

    public CompletableFuture<TransactionProcessResult> processPendingTransactions(List<String> transactionIds) {
        var message = new BazaarProcessPendingTransactionsProtocolObject.BazaarProcessPendingTransactionsMessage(
                player.getUuid(),
                getPlayerProfileUUID(),
                transactionIds
        );

        return bazaarService.<BazaarProcessPendingTransactionsProtocolObject.BazaarProcessPendingTransactionsMessage,
                        BazaarProcessPendingTransactionsProtocolObject.BazaarProcessPendingTransactionsResponse>handleRequest(message)
                .thenApply(response -> new TransactionProcessResult(
                        response.processedCount,
                        response.failedCount,
                        response.successfulTransactionIds,
                        response.failedTransactionIds
                ));
    }

    public CompletableFuture<TransactionProcessResult> processAllPendingTransactions() {
        if (!isOnline().join()) return CompletableFuture.completedFuture(new TransactionProcessResult(0, 0, List.of(), List.of()));

        return getPendingTransactions().thenCompose(transactions -> {
            if (transactions.isEmpty()) {
                return CompletableFuture.completedFuture(
                        new TransactionProcessResult(0, 0, List.of(), List.of())
                );
            }

            player.sendMessage("§6[Bazaar] §7Processing " + transactions.size() + " pending transaction" +
                    (transactions.size() == 1 ? "" : "s") + "...");

            List<String> successfulIds = new java.util.ArrayList<>();
            List<String> failedIds = new java.util.ArrayList<>();

            for (PendingTransaction tx : transactions) {
                try {
                    boolean processed = BazaarAwarder.processPendingTransaction(
                            player,
                            tx.transactionType(),
                            tx.transactionData()
                    );

                    if (processed) {
                        successfulIds.add(tx.id());
                    } else {
                        failedIds.add(tx.id());
                    }
                } catch (Exception e) {
                    System.err.println("Failed to process pending transaction " + tx.id() + ": " + e.getMessage());
                    failedIds.add(tx.id());
                }
            }

            CompletableFuture<TransactionProcessResult> result;
            if (!successfulIds.isEmpty()) {
                result = processPendingTransactions(successfulIds)
                        .thenApply(dbResult -> new TransactionProcessResult(
                                successfulIds.size(),
                                failedIds.size(),
                                successfulIds,
                                failedIds
                        ));
            } else {
                result = CompletableFuture.completedFuture(
                        new TransactionProcessResult(0, failedIds.size(), List.of(), failedIds)
                );
            }

            return result.thenApply(finalResult -> {
                if (finalResult.isFullySuccessful()) {
                    player.sendMessage("§6[Bazaar] §aAll pending transactions processed successfully!");
                } else if (finalResult.hasFailures()) {
                    player.sendMessage("§6[Bazaar] §eProcessed " + finalResult.processedCount() +
                            " transaction" + (finalResult.processedCount() == 1 ? "" : "s") +
                            ", " + finalResult.failedCount() + " failed.");
                } else if (finalResult.processedCount() > 0) {
                    player.sendMessage("§6[Bazaar] §aProcessed " + finalResult.processedCount() +
                            " pending transaction" + (finalResult.processedCount() == 1 ? "" : "s") + "!");
                }
                return finalResult;
            });
        });
    }

    private OrderStatistics calculateBuyStatistics(List<MarketOrder> buyOrders) {
        if (buyOrders.isEmpty()) {
            return new OrderStatistics(0, 0, 0);
        }

        double total = buyOrders.stream().mapToDouble(MarketOrder::price).sum();
        double average = total / buyOrders.size();
        double highest = buyOrders.stream().mapToDouble(MarketOrder::price).max().orElse(0);
        double lowest = buyOrders.stream().mapToDouble(MarketOrder::price).min().orElse(0);

        return new OrderStatistics(highest, lowest, average);
    }

    private OrderStatistics calculateSellStatistics(List<MarketOrder> sellOrders) {
        if (sellOrders.isEmpty()) {
            return new OrderStatistics(0, 0, 0);
        }

        double total = sellOrders.stream().mapToDouble(MarketOrder::price).sum();
        double average = total / sellOrders.size();
        double highest = sellOrders.stream().mapToDouble(MarketOrder::price).max().orElse(0);
        double lowest = sellOrders.stream().mapToDouble(MarketOrder::price).min().orElse(0);

        return new OrderStatistics(highest, lowest, average);
    }

    public enum OrderSide {
        BUY, SELL
    }

    public record BazaarOrder(
            UUID orderId,
            String itemName,
            OrderSide side,
            double price,
            double amount,
            UUID profileUUID
    ) {
        public ItemType getItemType() {
            try {
                return ItemType.valueOf(itemName);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        public double getTotalValue() {
            return price * amount;
        }
    }

    public record MarketOrder(
            UUID playerUUID,
            UUID profileUUID,
            double price,
            double amount
    ) {}

    public record BazaarItemData(
            String itemName,
            List<MarketOrder> buyOrders,
            List<MarketOrder> sellOrders
    ) {
        public ItemType getItemType() {
            try {
                return ItemType.valueOf(itemName);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        public boolean hasBuyOrders() {
            return !buyOrders.isEmpty();
        }

        public boolean hasSellOrders() {
            return !sellOrders.isEmpty();
        }
    }

    public record BazaarStatistics(
            double bestBid,
            double worstBid,
            double averageBid,
            double bestAsk,
            double worstAsk,
            double averageAsk
    ) {}

    public record BazaarResult(
            boolean success,
            String message
    ) {}

    public record PendingTransaction(
            String id,
            String transactionType,
            org.json.JSONObject transactionData,
            java.time.Instant createdAt
    ) {
        public boolean isSuccessfulTransaction() {
            return "SuccessfulBazaarTransaction".equals(transactionType);
        }

        public boolean isExpiredOrderTransaction() {
            return "OrderExpiredBazaarTransaction".equals(transactionType);
        }

        public String getDescription() {
            try {
                if (isSuccessfulTransaction()) {
                    String itemName = transactionData.getString("itemName");
                    double quantity = transactionData.getDouble("quantity");
                    double price = transactionData.getDouble("pricePerUnit");
                    return String.format("Trade completed: %.0fx %s at %.2f coins each",
                            quantity, itemName, price);
                } else if (isExpiredOrderTransaction()) {
                    String itemName = transactionData.getString("itemName");
                    String side = transactionData.getString("side");
                    double quantity = transactionData.getDouble("remainingQty");
                    return String.format("%s order expired: %.0fx %s",
                            side, quantity, itemName);
                }
            } catch (Exception e) {
                // Fall back to generic description if parsing fails
            }
            return "Pending " + transactionType;
        }
    }

    public record TransactionProcessResult(
            int processedCount,
            int failedCount,
            List<String> successfulTransactionIds,
            List<String> failedTransactionIds
    ) {
        public boolean hasFailures() {
            return failedCount > 0;
        }

        public boolean isFullySuccessful() {
            return failedCount == 0 && processedCount > 0;
        }
    }

    private record OrderStatistics(
            double highestPrice,
            double lowestPrice,
            double averagePrice
    ) {}
}