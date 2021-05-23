package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны


    private final NavigableMap<Customer, String> customers = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
//        return null; // это "заглушка, чтобы скомилировать"

        return this.customers.firstEntry();
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
//        return null; // это "заглушка, чтобы скомилировать"

        return this.customers.higherEntry(customer);
    }

    public void add(Customer customer, String data) {
        this.customers.put(customer, data);
    }
}
