import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final CoinAcceptor coinAcceptor;

    private final CardAcceptor cardAcceptor;

    private final Scanner scanner = new Scanner(System.in);

    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        coinAcceptor = new CoinAcceptor(20);
        cardAcceptor = new CardAcceptor(20);
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        print("В автомате доступны:");
        showProducts(products);

        print("Монет на сумму: " + coinAcceptor.getAmount());
        print("Денег на карте: " + cardAcceptor.getAmount());

        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        chooseAction(allowProducts);

    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (coinAcceptor.getAmount() >= products.get(i).getPrice() || cardAcceptor.getAmount() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        print(" a - Пополнить баланс");
        showActions(products);
        print(" h - Выйти");
        String action = fromConsole().substring(0, 1);
        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    buyProduct(products, i);
                    break;
                }
            }
            if ("h".equalsIgnoreCase(action)) {
                isExit = true;
            } else if ("a".equalsIgnoreCase(action)) {
                paymentMethod();
            }
        } catch (IllegalArgumentException e) {
            print("Недопустимая буква. Попрбуйте еще раз.");
            chooseAction(products);
        }


    }

    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
        if (products.size() == 0) {
            print("Недостаточно средств, пополните баланс");
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }

    private void buyProduct(UniversalArray<Product> products, int i) {
        int productPrice = products.get(i).getPrice();
        int coinAmount = coinAcceptor.getAmount();
        int cardAmount = cardAcceptor.getAmount();
        try {
            print("Выберите способ оплаты\n1. Монеты.\n2. Карта");
            int choice = scanner.nextInt();
            if (choice == 1 && coinAmount >= productPrice) {
                coinAcceptor.setAmount(coinAmount - productPrice);
                print("Вы купили " + products.get(i).getName());
            } else if (choice == 2 && cardAmount >= productPrice) {
                cardAcceptor.setAmount(cardAmount - productPrice);
                print("Вы купили " + products.get(i).getName());
            } else if (cardAmount < productPrice || coinAmount < productPrice){
                print("Недостаточно средств, пополните баланс");
            } else {
                print("Такого варианта нет.");
            }
            boolean status = true;
            while (status) {
                print("Хотите что еще купить? (д/н)");
                String exitChoice = fromConsole();
                if (Objects.equals(exitChoice, "н")) {
                    status = false;
                    isExit = false;
                } else if (Objects.equals(exitChoice, "д")) {
                    status = false;
                } else if(!Objects.equals(exitChoice, "н") && !Objects.equals(exitChoice, "д")) {
                    print("Такого варианта нет");
                }
            }
        } catch (InputMismatchException e) {
            print("Неправильно данные");
        }
    }

    private void paymentMethod() {
        print("Выберите сопсоб оплаты\n1. Монеты\n2. Карта");
        String paymentChoice = fromConsole().substring(0,1);
        if (paymentChoice.equals("1")) {
            addBalanceCoin();
        } else if (paymentChoice.equals("2")) {
            addBalanceCard();
        }
    }

    private void addBalanceCoin() {
        try {
            print("Введите сумму");
            int amount = Integer.parseInt(fromConsole());
            coinAcceptor.setAmount(amount);
        } catch (InputMismatchException e) {
            print("Неправельные данные");
        }
    }

    private void addBalanceCard() {
        try {
            print("Введите номер карты");
            int cardNumber = Integer.parseInt(fromConsole());
            cardAcceptor.setCardNumber(cardNumber);
            print("Введите пароль от карты");
            int password = Integer.parseInt(fromConsole());
            cardAcceptor.setPassword(password);
            print("Введите сумму");
            int amount = new Scanner(System.in).nextInt();
            cardAcceptor.setAmount(amount);
        } catch (InputMismatchException e) {
            print("Неправельные данные");
        }
    }
}
