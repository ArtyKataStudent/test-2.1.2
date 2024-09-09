import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
    }

    public static class Product implements Comparable<Product> {
        private Product productName;
        private Double price;

        public Product(Product productName, Double price) {
            this.productName = productName;
            this.price = price;
        }

        public int compareTo(Product product) {
            int result = this.productName.compareTo(product.productName);
            if (result == 0) {
                result = this.price.compareTo(product.price);
            }
            return result;
        }

        public Product getProductName() {
            return productName;
        }

        public void setProductName(Product productName) {
            this.productName = productName;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Product product = (Product) o;
            return Objects.equals(productName, product.productName) && Objects.equals(price, product.price);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productName, price);
        }

        public static class Human implements Buyer {

            private String firstName;
            private String lastName;
            private double money; // денежный счет человека
            private Set<Product> products; // купленные продукты

            public void buyProduct(Product product, Shop shop) {
                try {
                    shop.sellProduct(product.productName, this);
                } catch (SellProductException e) {
                    System.out.println(e.getMessage());
                } finally {
                    products.add(product);
                    money -= product.price;
                }
            }

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public Set<Product> getProducts() {
                return products;
            }

            public void setProducts(Set<Product> products) {
                this.products = products;
            }
        }


        public interface Buyer {
            void buyProduct(Product product, Shop shop);
        }


        public static class Shop {
            private String name;
            private double money; // денежный счет магазина
            private Map<Product, Integer> products; // продукты и их количество
            //  количество может быть 0 !


            public Shop(String name, double money, Map<Product, Integer> products) {
                this.name = name;
                this.money = money;
                this.products = products;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public Map<Product, Integer> getProducts() {
                return products;
            }

            public void setProducts(Map<Product, Integer> products) {
                this.products = products;
            }

            public void sellProduct(Product product, Human human) throws SellProductException {
                boolean a = products.containsValue(product.productName);
                if (a == false) {
                    throw new SellProductException("Продукта с именем " + product.productName + " нет в наличии");
                }
                if (product.price > human.getMoney()) {
                    throw new SellProductException("Уважаемый " + human.firstName + " " + human.lastName + ", для покупки товара недостаточно средств");
                } else if (product.price < human.getMoney()) {
                    products.remove(product.productName);
                    money += product.price - calculateNds(product.price);
                    System.out.println(human.firstName + ", вы успешно совершили покупку! C уважением, " + name);
                }
            }

            private double calculateNds(double price) {
                return price * 0.13;
            }

            public List<Product> printAndGetAllProductsWithCount() {
                Map<Product, Integer> sortedMap = products.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
                for (Map.Entry<Product, Integer> item : sortedMap.entrySet()) {
                    System.out.printf("1. %s - 2 - %f", item.getKey(), item.getValue());
                }
                return new ArrayList<Product>(sortedMap.values().size());
            }
        }


        public static class SellProductException extends Exception {
            public SellProductException(String s) {
            }
        }


        public class ConsoleService {
            private Shop shop;
            private Human human;

            public void start() {
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Задайте имя и стартовый капитал магазина");
                String shopName = sc.next();
                double startCapital = sc.nextDouble();
                System.out.println("2. Укажите количество товаров");
                String nameOfProduct = sc.next();
                double priceOfProduct = sc.nextDouble();
                int qantityInStock = sc.nextInt();
                System.out.println("имя, фамилия, и количество денежных средств");
                String firstName = sc.next();
                String lastName = sc.next();
                double countOfHumanMoney = sc.nextDouble();
                int variantOneOrTwo = sc.nextInt();
                if (variantOneOrTwo == 1) {
                    shop.printAndGetAllProductsWithCount();
                    if (!sc.hasNextInt() && sc.hasNext()) {
                        int selectedProductItem = sc.nextInt();
                    }
                } else {
                    System.out.println("пока");
                }
            }
        }
    }
}