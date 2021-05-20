package homework;


import java.util.LinkedHashMap;
import java.util.LinkedList;

public class CustomerReverseOrder extends LinkedList {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    public void add(Customer customer) {
        this.addLast(customer);
    }

    public Customer take() {
//        return null; // это "заглушка, чтобы скомилировать"
        return (Customer) this.removeLast();
    }
}
