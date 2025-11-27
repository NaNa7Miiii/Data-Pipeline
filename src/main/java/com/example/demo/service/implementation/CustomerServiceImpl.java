package com.example.demo.service.implementation;

import com.example.demo.dto.request.OrderInsightRequestDto;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.interfaces.CustomerInsightService;
import com.example.demo.service.interfaces.OrderCityView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerInsightService {
    @Autowired
    public CustomerRepository customerRepository;

    @Override
    // returns the unique number of customers, by customerUniqueId
    public int getCustomerCount(OrderInsightRequestDto request) { return countUniqueCustomers(loadCustomers(request)); }

    @Override
    // returns the average customer lifetime value: total order values / number of unique customer
    public double getAvgCLV(OrderInsightRequestDto request) {
        List<OrderCityView> orders = loadCustomers(request);
        double totalValue = computeGmv(orders);
        int customerCount = countUniqueCustomers(orders);
        return customerCount == 0 ? 0 : (double) totalValue / customerCount;
    }

    @Override
    // returns the geographic distribution of customers by cities
    public Map<String, Integer> getCustomerCityDistribution(OrderInsightRequestDto request) {
        return calculateCityDistribution(loadCustomers(request));
    }

    @Override
    // returns the customer repeat rate
    public double getRepeatRate(OrderInsightRequestDto request) {
        return calculateRepeatRate(loadCustomers(request));
    }


//    @Override
//    public int getNewCustomerCount(OrderInsightRequestDto request) {
//
//    } // customers in filter criteria with no previous purchases
//
//    @Override
//    public double getAvgPurchaseInterval(OrderInsightRequestDto request) {
//
//    } // avg purchase interval for returning customers

    private List<OrderCityView> loadCustomers(OrderInsightRequestDto request) {
        LocalDateTime start = request.startDate();
        LocalDateTime end = request.endDate();
        String city = request.city();
        String category = request.category();
        return customerRepository.findCustomersByFilters(start, end, city, category);
    }

    private int countUniqueCustomers(List<OrderCityView> orders) {
        long count = orders.stream().map(OrderCityView::getUniqueCustomerId).distinct().count();
        return (int) count;
    }

    private double computeGmv(List<OrderCityView> orders) {
        return orders.stream()
                .mapToDouble(o -> o.getPrice() == null ? 0.0 : o.getPrice())
                .sum();
    }

    // function to calculate city distribution (should be implemented via PRD file)
    private Map<String, Integer> calculateCityDistribution(List<OrderCityView> orders) {
        Map<String, Integer> distribution = new HashMap<>();
        for (OrderCityView order : orders) {
            String city = order.getCustomerCity();
            if (city == null) continue;
            distribution.put(city, distribution.getOrDefault(city, 0) + 1);
        }
        return distribution;
    }

    // function to calculate repeat rate (should be implemented via PRD file)
    private double calculateRepeatRate(List<OrderCityView> orders) {
        if (orders.isEmpty()) return 0.0;
        Map<String, Integer> customerOrderCounter = new HashMap<>();
        for (OrderCityView order: orders) {
            customerOrderCounter.put(order.getCustomerId(),
                    customerOrderCounter.getOrDefault(order.getCustomerId(), 0) + 1);
        }
        long repeatCustomers = customerOrderCounter.values().stream()
                .filter(count -> count > 1)
                .count();
        int totalCustomers = customerOrderCounter.size();
        return (double) repeatCustomers / totalCustomers;
    }
}
