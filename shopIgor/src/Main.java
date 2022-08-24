//package igorshopigor;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;


public class Main {
    static ArrayList<Book> books = new ArrayList<>();
    static ArrayList<Customer> customers = new ArrayList<>();
    static ArrayList<Employee> employees = new ArrayList<>();
    static ArrayList<Order> orders = new ArrayList<>();

    public static void main(String[] args) {
          initData();

          // вывод первой задачи
          String booksInfo =
                  String.format("Общее количество проданных книг %d на сумму %f евро", getCountOfSoldBooks(), getAllPriceOfSouldBooks());
          System.out.println(booksInfo);

          // вывод второй задачи
        for (Employee employee : employees) {
            System.out.println(employee.getName() +" продал(а) " +getProfitByEmployee(employee.getId()).toString());
        }

        // вывод третьей задачи
        ArrayList<BookAdditional> soldBooksCount = getCountOfSoldBooksByGenre();
        HashMap<BookGenre,Double> soldBookPrices = getPriceOfSoldBooksByGenre();

        String soldBookStr = "По жанру: %s продано %d книг(и) общей стоимостью %f евро";
        for (BookAdditional bookAdditional : soldBooksCount) {
            double price = soldBookPrices.get(bookAdditional.getGenre());
            System.out.println(
                    String.format(
                            soldBookStr,
                            bookAdditional.getGenre().name(),bookAdditional.getCount(),price));
        }
    }

    //получить количество проданных книг по жанрам
    public static ArrayList<BookAdditional> getCountOfSoldBooksByGenre() {
        ArrayList<BookAdditional> result = new ArrayList<>();
        int countArt=0,countPr=0,countPs=0;
        for (Order order : orders) {
            countArt += getCountOfSoldBooksByGenre(order,BookGenre.Art);
            countPr += getCountOfSoldBooksByGenre(order,BookGenre.Programming);
            countPs += getCountOfSoldBooksByGenre(order,BookGenre.Psychology);
        }
        result.add(new BookAdditional(BookGenre.Art,countArt));
        result.add(new BookAdditional(BookGenre.Programming,countPr));
        result.add(new BookAdditional(BookGenre.Psychology,countPs));

        return result;
    }

    //получаем количество книг в одном заказе по жанру
    public static int getCountOfSoldBooksByGenre(Order order, BookGenre genre) {
        int count = 0;
        for (long bookId : order.getBooks()) {
            Book book = getBookById(bookId);
            if (book != null && book.getGenre() == genre)
                count++;
        }
        return count;
    }

    //получаем стоимость проданных книг по жанрам
    public static HashMap<BookGenre,Double> getPriceOfSoldBooksByGenre() {
        HashMap<BookGenre, Double> result = new HashMap<>();
        double priceArt = 0, pricePr = 0, pricePs = 0;

        for (Order order : orders) {
            priceArt += getPriceOfSoldBooksByGenre(order, BookGenre.Art);
            pricePr += getPriceOfSoldBooksByGenre(order, BookGenre.Programming);
            pricePs += getPriceOfSoldBooksByGenre(order, BookGenre.Psychology);
        }
        result.put(BookGenre.Art,priceArt);
        result.put(BookGenre.Programming,pricePr);
        result.put(BookGenre.Psychology,pricePs);

        return result;

    }

    //получаем общую стоимость книг в одном заказе по жанру
    public static double getPriceOfSoldBooksByGenre(Order order, BookGenre genre) {
        double price = 0;
        for (long bookId : order.getBooks()) {
            Book book = getBookById(bookId);
            if (book != null && book.getGenre() == genre)
                price += book.getPrice();
        }
        return price;
    }

    //получаем количество и стоимость проданного товара для конкретного продавца
    public static Profit getProfitByEmployee(long employeeId) {
        int count = 0; double price = 0;
        for (Order order : orders) {
            if (order.getEmployeeId() == employeeId) {
                price += getPriceOfSoldBooksInOrder(order);
                count += order.getBooks().length;
            }
        }
        return new Profit(count, price);
    }

    //получаем общую стоимость проданных книг по всем заказам

    public static double getAllPriceOfSouldBooks() {
        double price = 0;
        for (Order order : orders) {
            price += getPriceOfSoldBooksInOrder(order);

        }
        return price;
    }

    // получаем общую стоимость одного заказа
    public static double getPriceOfSoldBooksInOrder(Order order) {
        double price = 0;
        for (long bookId : order.getBooks()) {
            Book book = getBookById(bookId);
            if (book != null)
               price += book.getPrice();
        }
        return price;
    }

    // получаем общее количество проданных книг
    public static int getCountOfSoldBooks() {
        int count = 0;
        for (Order order : orders) {
            //count = count + order.getBooks().length; ниже равнозначная запись короче
            count += order.getBooks().length;
        }
        return count;
    }
    public static Book getBookById(long id) {
        Book current = null;

        for (Book book : books) {
            if (book.getId() == id) {
                current = book;
                break;
            }
        }

        return current;
    }

    public static void initData() {
        employees.add(new Employee(1, "Ivan Ivanov", 32));
        employees.add(new Employee(2, "Pjotr Petrov", 30));
        employees.add(new Employee(3, "Anna Vassiljeva", 26));

        customers.add(new Customer(1, "Aleksei Sidorov", 25));
        customers.add(new Customer( 2, "Dmitri Romanov", 32));
        customers.add(new Customer(3, "Kirill Simonov",19));
        customers.add(new Customer(4, "Daniil Kirijenko", 45));
        customers.add(new Customer( 5, "Elina Vorotyntseva", 23));

        books.add(new Book(1, "Война и мир", "Лев Толстой",25, BookGenre.Art));
        books.add(new Book(2, "Преступление и наказание", "Фёдор Достоевский",27, BookGenre.Art));
        books.add(new Book(3, "Мёртвые души", "Николай Гоголь",  30, BookGenre.Art));
        books.add(new Book(4, "Руслан и Людмила", "Александр Пушкин",  32, BookGenre.Art));

        books.add(new Book( 5, "Введение в психоанализ", "Зигмунд Фрейд",  26, BookGenre.Psychology));
        books.add(new Book(6, "Психоанализ 2", "Дейл Карнеги", 36, BookGenre.Psychology));
        books.add(new Book(7,  "Психоанализ 3", "Роберт Чалдини", 40, BookGenre.Psychology));

        books.add(new Book(8,"Совершенный код", "Стив Макконнел", 19, BookGenre.Programming));
        books.add(new Book(9, "Улучшение кода", "Мартин Фаулер", 19, BookGenre.Programming));
        books.add(new Book(10, "Построение и анализ", "Томас Кармен", 19, BookGenre.Programming));
        books.add(new Book(11, "Код", "Джо Джонсон", 15, BookGenre.Programming));

        orders.add(new Order(1, 1, 1, new long[]{8,9,10,11}));
        orders.add(new Order(2, 2,1, new long[]{1}));

        orders.add(new Order(3,  3,2, new long[]{5,6,7}));
        orders.add(new Order(4, 4, 2, new long[]{1,2,3,4}));

        orders.add(new Order(5, 5,3, new long[]{2,5,9}));
    }
}