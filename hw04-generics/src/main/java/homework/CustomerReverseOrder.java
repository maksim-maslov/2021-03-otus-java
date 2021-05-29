package homework;


import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    private final Deque<Customer> stackCustomer = new ArrayDeque<>();

    public void add(Customer customer) {
        this.stackCustomer.addLast(customer);
    }

    public Customer take() {
        return this.stackCustomer.removeLast();
    }
}
