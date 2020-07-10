package machine;

public class CoffeeMaker {
    private int amountOfWater;
    private int amountOfMilk;
    private int amountOfCoffeeBeans;
    private int amountOfDisposableCups;
    private int amountOfMoney;

    private STATES state;

    public CoffeeMaker() {
        this(0, 0, 0, 0, 0);
    }

    public CoffeeMaker(int amountOfWater,
                       int amountOfMilk,
                       int amountOfCoffeeBeans,
                       int amountOfDisposableCups,
                       int amountOfMoney) {
        this.amountOfWater = amountOfWater;
        this.amountOfMilk = amountOfMilk;
        this.amountOfCoffeeBeans = amountOfCoffeeBeans;
        this.amountOfDisposableCups = amountOfDisposableCups;
        this.amountOfMoney = amountOfMoney;
        state = STATES.CHOOSE_ACTION;
        printActions();
    }

    private STATES getState() {
        return state;
    }

    private void setState(STATES state) {
        switch (state) {
            case CHOOSE_ACTION:
                printActions();
                break;
            case FILL_WATER:
                System.out.println("Write how many ml of water do you want to add:");
                break;
            case FILL_MILK:
                System.out.println("Write how many ml of milk do you want to add:");
                break;
            case FILL_BEANS:
                System.out.println("Write how many grams of coffee beans do you want to add:");
                break;
            case FILL_CUPS:
                System.out.println("Write how many disposable cups of coffee do you want to add:");
                break;
        }
        this.state = state;
    }

    public void handleAction(String line) {
        switch (getState()) {
            case CHOOSE_ACTION:
                switch (line) {
                    case "buy":
                        proposeCoffee();
                        setState(STATES.CHOOSE_COFFEE);
                        break;
                    case "fill":
                        setState(STATES.FILL_WATER);
                        break;
                    case "take":
                        System.out.println("I gave you $" + takeMoney() + "\n");
                        printActions();
                        break;
                    case "remaining":
                        printState();
                        printActions();
                        break;
                    default:
                        System.out.println("Unknown option");
                        printActions();
                        break;
                }
                break;
            case CHOOSE_COFFEE:
                buyCoffee(line);
                break;
            case FILL_WATER:
            case FILL_MILK:
            case FILL_BEANS:
            case FILL_CUPS:
                fill(line);
                break;
        }
    }

    private void buyCoffee(String line) {
        int option;
        if (line.equals("back")) {
            setState(STATES.CHOOSE_ACTION);
            return;
        } else {
            try {
                option = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                option = 0;
            }
        }
        if (option == 0 || option > typesOfCoffee.values().length) {
            System.out.println("Unknown type of coffee");
        } else {
            typesOfCoffee coffee = typesOfCoffee.values()[option - 1];
            if (amountOfDisposableCups <= 0) {
                System.out.println("Sorry, not enough disposable cups!");
            } else if (amountOfWater < coffee.getWaterAmount()) {
                System.out.println("Sorry, not enough water!");
            } else if (amountOfMilk < coffee.getMilkAmount()) {
                System.out.println("Sorry, not enough milk!");
            } else if (amountOfCoffeeBeans < coffee.getBeansAmount()) {
                System.out.println("Sorry, not enough coffee beans!");
            } else {
                System.out.println("I have enough resources, making you a coffee!");
                amountOfWater -= coffee.getWaterAmount();
                amountOfMilk -= coffee.getMilkAmount();
                amountOfCoffeeBeans -= coffee.getBeansAmount();
                amountOfDisposableCups--;
                amountOfMoney += coffee.getCost();
            }
            setState(STATES.CHOOSE_ACTION);
        }
    }

    private void fill(String line) {
        int amount;
        boolean isWithoutException = true;
        try {
            amount = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            amount = 0;
            System.out.println("You have to type number");
            isWithoutException = false;
        }
        if (isWithoutException) {
            switch (getState()) {
                case FILL_WATER:
                    amountOfWater += amount;
                    setState(STATES.FILL_MILK);
                    break;
                case FILL_MILK:
                    amountOfMilk += amount;
                    setState(STATES.FILL_BEANS);
                    break;
                case FILL_BEANS:
                    amountOfCoffeeBeans += amount;
                    setState(STATES.FILL_CUPS);
                    break;
                case FILL_CUPS:
                    amountOfDisposableCups += amount;
                    setState(STATES.CHOOSE_ACTION);
                    break;
            }
        }
    }

    private void printActions() {
        System.out.println("Write action (buy, fill, take, remaining, exit):");
    }

    private void proposeCoffee() {
        System.out.print("What do you want to buy? ");
        for (typesOfCoffee coffee : typesOfCoffee.values()) {
            System.out.print(coffee.ordinal() + 1 + " - " + coffee.name().toLowerCase());
            System.out.print(coffee.ordinal() == typesOfCoffee.values().length - 1 ?
                                     ", back - to main menu:\n" :
                                     ", ");
        }
    }

    private int takeMoney() {
        int money = amountOfMoney;
        amountOfMoney = 0;
        return money;
    }

    private void printState() {
        System.out.println("The coffee machine has:\n"
                                   + amountOfWater
                                   + " of water\n"
                                   + amountOfMilk
                                   + " of milk\n"
                                   + amountOfCoffeeBeans
                                   + " of coffee beans\n"
                                   + amountOfDisposableCups
                                   + " of disposable cups\n$"
                                   + amountOfMoney
                                   + " of money\n");

    }

    private enum typesOfCoffee {
        ESPRESSO(250, 0, 16, 4),
        LATTE(350, 75, 20, 7),
        CAPPUCCINO(200, 100, 12, 6);

        public int getWaterAmount() {
            return waterAmount;
        }

        public int getMilkAmount() {
            return milkAmount;
        }

        public int getBeansAmount() {
            return beansAmount;
        }

        public int getCost() {
            return cost;
        }

        private final int waterAmount;
        private final int milkAmount;
        private final int beansAmount;
        private final int cost;

        typesOfCoffee(int waterAmount, int milkAmount, int beansAmount, int cost) {
            this.waterAmount = waterAmount;
            this.milkAmount = milkAmount;
            this.beansAmount = beansAmount;
            this.cost = cost;
        }
    }

    private enum STATES {
        CHOOSE_ACTION,
        CHOOSE_COFFEE,
        FILL_WATER,
        FILL_MILK,
        FILL_BEANS,
        FILL_CUPS
    }
}
