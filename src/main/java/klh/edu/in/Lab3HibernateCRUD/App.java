package klh.edu.in.Lab3HibernateCRUD;

/**
 * Hello world!
 *
 */
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

/*
 * Main class to demonstrate:
 * - Insert records
 * - Sorting
 * - Pagination
 * - Filtering
 * - Aggregate functions using HQL
 */
public class App {

    public static void main(String[] args) {

        // Create SessionFactory
        SessionFactory factory = new Configuration()
                .configure()
                .addAnnotatedClass(Product.class)
                .buildSessionFactory();

        // Open Session
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        // -------- INSERT DATA --------
        session.persist(new Product(1,"Pen","Stationery",10,50));
        session.persist(new Product(2,"Notebook","Stationery",50,30));
        session.persist(new Product(3,"Mouse","Electronics",500,10));
        session.persist(new Product(4,"Keyboard","Electronics",700,5));
        session.persist(new Product(5,"Bag","Accessories",1200,15));
        session.persist(new Product(6,"Bottle","Accessories",300,0));

        tx.commit();
        session.close();

        // -------- HQL OPERATIONS --------
        Session session2 = factory.openSession();

        // 1. Sort by price ASC
        List<Product> list1 = session2
                .createQuery("FROM Product ORDER BY price ASC", Product.class)
                .list();
        System.out.println("\nPrice Ascending:");
        list1.forEach(p -> System.out.println(p.getName()+" "+p.getPrice()));

        // 2. Sort by price DESC
        List<Product> list2 = session2
                .createQuery("FROM Product ORDER BY price DESC", Product.class)
                .list();
        System.out.println("\nPrice Descending:");
        list2.forEach(p -> System.out.println(p.getName()+" "+p.getPrice()));

        // 3. Sort by quantity (highest first)
        List<Product> list3 = session2
                .createQuery("FROM Product ORDER BY quantity DESC", Product.class)
                .list();

        // 4. Pagination (First 3 products)
        Query<Product> q1 = session2.createQuery("FROM Product", Product.class);
        q1.setFirstResult(0);
        q1.setMaxResults(3);

        // 5. Aggregate functions
        Product totalCount = session2
                .createQuery("SELECT COUNT(*) FROM Product", Product.class)
                .uniqueResult();

        Object[] minMax = (Object[]) session2
                .createQuery("SELECT MIN(price), MAX(price) FROM Product")
                .uniqueResult();

        System.out.println("\nTotal Products: " + totalCount);
        System.out.println("Min Price: " + minMax[0]);
        System.out.println("Max Price: " + minMax[1]);

        // 6. LIKE examples
        List<Product> likeList = session2
                .createQuery("FROM Product WHERE name LIKE '%o%'", Product.class)
                .list();

        System.out.println("\nProducts containing 'o':");
        likeList.forEach(p -> System.out.println(p.getName()));

        session2.close();
        factory.close();
    }
}
