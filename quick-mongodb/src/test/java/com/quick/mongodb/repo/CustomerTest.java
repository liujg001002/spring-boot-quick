package com.quick.mongodb.repo;

import com.quick.mongodb.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerTest {

    @Resource
    private CustomerRepository customerRepository;
    @Resource
    private MongoRepository mongoRepository;

    @Test
    public void execute() {

        long count = mongoRepository.count();
        System.out.println(count);

        mongoRepository.deleteAll();

        mongoRepository.save(new Customer("vector", "wang"));
        mongoRepository.save(new Customer("vector1", "wang"));
        mongoRepository.save(new Customer("bmhjqs", "wang"));

        System.out.println("customer fetch with findAll()");
        System.out.println("-----------------------------");
        for (Customer customer : (List<Customer>)mongoRepository.findAll()) {
            System.out.println(customer);
        }
        System.out.println();

        //条件匹配器
        Customer customer = new Customer();
        customer.setFirstName("vector");
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("firstName",ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("lastName",ExampleMatcher.GenericPropertyMatchers.contains());
//                                        .withMatcher("processStatus",ExampleMatcher.GenericPropertyMatchers.exact());//如果不设置匹配器默认精确匹配
        //排序
        Sort sort = Sort.by("lastName").descending();

        //定义example条件对象
        Example<Customer> example = Example.of(customer,exampleMatcher);

        List<Customer> list = mongoRepository.findAll(example,sort);
        for (Customer cus:  list) {
            System.out.println(cus);
        }

    }

    @Test
    public void findByCondition(){

        System.out.println("Customer found with findByFirstName('vector'):");
        System.out.println("--------------------------------");
        System.out.println(customerRepository.findByFirstName("vector"));


        System.out.println("Customers found with findByLastName('wang'):");
        System.out.println("--------------------------------");
        for (Customer customer : customerRepository.findByLastName("wang")) {
            System.out.println(customer);
        }
    }
}
