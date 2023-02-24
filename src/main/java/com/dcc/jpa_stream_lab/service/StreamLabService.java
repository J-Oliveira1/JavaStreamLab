package com.dcc.jpa_stream_lab.service;

import java.util.List;
import java.util.stream.Collectors;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcc.jpa_stream_lab.repository.ProductsRepository;
import com.dcc.jpa_stream_lab.repository.RolesRepository;
import com.dcc.jpa_stream_lab.repository.ShoppingcartItemRepository;
import com.dcc.jpa_stream_lab.repository.UsersRepository;
import com.dcc.jpa_stream_lab.models.Product;
import com.dcc.jpa_stream_lab.models.Role;
import com.dcc.jpa_stream_lab.models.ShoppingcartItem;
import com.dcc.jpa_stream_lab.models.User;

@Transactional
@Service
public class StreamLabService {
	
	@Autowired
	private ProductsRepository products;
	@Autowired
	private RolesRepository roles;
	@Autowired
	private UsersRepository users;
	@Autowired
	private ShoppingcartItemRepository shoppingcartitems;


    // <><><><><><><><> R Actions (Read) <><><><><><><><><>

    public List<User> RDemoOne() {
    	// This query will return all the users from the User table.
    	return users.findAll().stream().toList();
    }

    public long RProblemOne()
    {
        // Return the COUNT of all the users from the User table.
        // You MUST use a .stream(), don't listen to the squiggle here!
        // Remember yellow squiggles are warnings and can be ignored.
    	return users.findAll().stream().count();
    }

    public List<Product> RDemoTwo()
    {
        // This query will get each product whose price is greater than $150.
    	return products.findAll().stream().filter(p -> p.getPrice() > 150).toList();
    }

    public List<Product> RProblemTwo()
    {
        // Write a query that gets each product whose price is less than or equal to $100.
        // Return the list
        return products.findAll().stream().filter(product -> product.getPrice() <= 100).toList();
    }

    public List<Product> RProblemThree()
    {
        // Write a query that gets each product that CONTAINS an "s" in the products name.
        // Return the list
    	return products.findAll().stream().filter(product -> product.getName().toLowerCase().contains("s")).collect(Collectors.toList());
    }

    public List<User> RProblemFour()
    {
        // Write a query that gets all the users who registered BEFORE 2016
        // Return the list
        // Research 'java create specific date' and 'java compare dates'
        // You may need to use the helper classes imported above!
    	Calendar myDate = new GregorianCalendar(2016, Calendar.JANUARY, 1);
        Date my_obj = myDate.getTime();
        return users.findAll().stream().filter(user -> user.getRegistrationDate().before(my_obj)).toList();
    }

    public List<User> RProblemFive()
    {
        // Write a query that gets all of the users who registered AFTER 2016 and BEFORE 2018
        // Return the list
        Calendar myDate1 = new GregorianCalendar(2016, Calendar.JANUARY, 1);
        Calendar myDate2 = new GregorianCalendar(2018, Calendar.JANUARY, 1);
        Date my_obj1 = myDate1.getTime();
        Date my_obj2 = myDate2.getTime();
        return users.findAll().stream().filter(user -> user.getRegistrationDate().after(my_obj1) && user.getRegistrationDate().before(my_obj2)).toList();
    }

    // <><><><><><><><> R Actions (Read) with Foreign Keys <><><><><><><><><>

    public List<User> RDemoThree()
    {
        // Write a query that retrieves all of the users who are assigned to the role of Customer.
    	Role customerRole = roles.findAll().stream().filter(r -> r.getName().equals("Customer")).findFirst().orElse(null);
    	List<User> customers = users.findAll().stream().filter(u -> u.getRoles().contains(customerRole)).toList();

    	return customers;
    }

    public List<Product> RProblemSix()
    {
        // Write a query that retrieves all of the products in the shopping cart of the user who has the email "afton@gmail.com".
        // Return the list
        User user_products = users.findAll().stream().filter(user -> user.getEmail().equals("afton@gmail.com")).findFirst().orElse(null);
        List<Product> products = user_products.getShoppingcartItems().stream().map(p -> p.getProduct()).toList();
    	return products;
        //changed filter to map because product cannot be converted to boolean.
    }

    public long RProblemSeven()
    {
        // Write a query that retrieves all of the products in the shopping cart of the user who has the email "oda@gmail.com" and returns the sum of all of the products prices.
    	// Remember to break the problem down and take it one step at a time!
        User user_sum = users.findAll().stream().filter(user -> user.getEmail().equals("oda@gmail.com")).findFirst().orElse(null);
        List<Product> products = user_sum.getShoppingcartItems().stream().map(p -> p.getProduct()).toList();
        long sum = products.stream().mapToLong(p -> p.getPrice()).sum();
        //Total price is 713, tested this function with problem 6 by changing problems 6 email and using this email to verify total sum.
    	return sum;

    }

    public List<Product> RProblemEight()
    {
        // Write a query that retrieves all of the products in the shopping cart of users who have the role of "Employee".
    	// Return the list

        Role employee_role = roles.findAll().stream().filter(role -> role.getName().equals("Employee")).findFirst().orElse(null);
        List<Product> products = shoppingcartitems.findAll().stream().filter(shoppingcartItem -> shoppingcartItem.getUser().getRoles().contains(employee_role)).map(shoppingcartItem -> shoppingcartItem.getProduct()).toList();

    	return products;
    }

    // <><><><><><><><> CUD (Create, Update, Delete) Actions <><><><><><><><><>

    // <><> C Actions (Create) <><>

    public User CDemoOne()
    {
        // Create a new User object and add that user to the Users table.
        User newUser = new User();        
        newUser.setEmail("joe@fake.com");
        newUser.setPassword("password");
        users.save(newUser);
        return newUser;
    }

    public Product CProblemOne()
    {
        // Create a new Product object and add that product to the Products table.
        // Return the product
    	Product newProduct = new Product();
        newProduct.setDescription("Testing testing");
        newProduct.setName("Watch test");
        newProduct.setPrice(59);
        products.save(newProduct);
        return newProduct;
        
    }

    public List<Role> CDemoTwo()
    {
        // Add the role of "Customer" to the user we just created in the UserRoles junction table.
    	Role customerRole = roles.findAll().stream().filter(r -> r.getName().equals("Customer")).findFirst().orElse(null);
    	User david = users.findAll().stream().filter(u -> u.getEmail().equals("david@gmail.com")).findFirst().orElse(null);
    	david.addRole(customerRole);
    	return david.getRoles();
    }

    public ShoppingcartItem CProblemTwo()
    {
    	// Create a new ShoppingCartItem to represent the new product you created being added to the new User you created's shopping cart.
        // Add the product you created to the user we created in the ShoppingCart junction table.
        // Return the ShoppingcartItem

    	return null;
    	
    }

    // <><> U Actions (Update) <><>

    public User UDemoOne()
    {
         //Update the email of the user we created in problem 11 to "mike@gmail.com"
          User user = users.findAll().stream().filter(u -> u.getEmail().equals("david@gmail.com")).findFirst().orElse(null);
          user.setEmail("mike@gmail.com");
          return user;
    }

    public Product UProblemOne()
    {
        // Update the price of the product you created to a different value.
        // Return the updated product
    	return null;
    }

    public User UProblemTwo()
    {
        // Change the role of the user we created to "Employee"
        // HINT: You need to delete the existing role relationship and then create a new UserRole object and add it to the UserRoles table

    	return null;
    }

    //BONUS:
    // <><> D Actions (Delete) <><>

    // For these bonus problems, you will also need to create their associated routes in the Controller file!
    
    // DProblemOne
    // Delete the role relationship from the user who has the email "oda@gmail.com".

    // DProblemTwo
    // Delete all the product relationships to the user with the email "oda@gmail.com" in the ShoppingCart table.

    // DProblemThree
    // Delete the user with the email "oda@gmail.com" from the Users table.

}
